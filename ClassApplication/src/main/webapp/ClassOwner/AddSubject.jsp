<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style type="text/css">
.dataTables_filter {
     display: none;
}

.subjectTable .editEnabled .editable{
	display:block;
}

.subjectTable .editEnabled .default{
	display:none;
}

.subjectTable .editable{
	display:none;
}

.subjectTable .default{
	display:show;
}
</style>
<script type="text/javascript" src="js/AddSubject.js"></script>
<script>
var BUTTONS_MANAGE = '<div class="default">' +
						'<button class="btn btn-primary btn-xs btn-manage">Manage</button>'+
						'<button class="btn btn-primary btn-xs btn-edit">Edit</button>'+
						'<button class="btn btn-danger btn-xs btn-delete">Delete</button>'+
					'</div>';
					
var BUTTONS_CANCEL = '<div class="editable">' +
						'<button class="btn btn-success btn-xs btn-save">Save</button>'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>';
					
var SUBJECT_NAME = '<input type="hidden" class="hide editSubjectId" value="{{editSubjectIdValue}}">'+
					'<div class="default defaultsubjectName">{{defaultsubjectNameValue}}</div>'+
					'<input type="text" value="{{editSubjectNameValue}}" class="form-control editable editSubjectName">';

var subjectid="";
function getSubject(subid){
	subjectid=subid;
	$("#editsubject").val($("#sub"+subid).val());
	$('div#ModifysubjectModal .error').hide();
	$('#ModifysubjectModal').modal('toggle');
}

/*
New function
*/
function deleteSubject(subid){
	$.ajax({
        url: 'classOwnerServlet',
        type: 'post',
        data: {
	    	 methodToCall: "deletesubject",
	    	 subjectid:subid
        },
        success: function(){
			$.notify({message: 'Deleted'},{type: 'success'});
			$('input[type="hidden"].editSubjectId[value="'+subid+'"]').closest("tr").remove();
        }, error: function(){
            $.notify({message: 'Unable to delete'},{type: 'danger'});
        }
});
}

/*this function will prompt to delete subject*/
function deleteSubjectPrompt(){
	var subjectIdToEdit = $(this).closest("tr").find(".editSubjectId").val();
	deleteConfirm(subjectIdToEdit);
}

function deleteConfirm(subId){
	modal.modalConfirm("Delete","Do you want to delete","Cancel","Delete",deleteSubject,[subId]);
}

function enableEdit(){
	var subjectName = $(this).closest("tr").find(".defaultsubjectName").text().trim();
	$(this).closest("tr").find(".editSubjectName").val(subjectName);
	$(this).closest("tr").addClass("editEnabled");
	//$(this).closest("tr").find(".editSubjectName").val("");
}

function cancelEdit(){
	$(this).closest("tr").removeClass("editEnabled");
}

function manageLink(){
	var subjectIdTo = $(this).closest("tr").find(".editSubjectId").val();
	var subjectname = $(this).closest("tr").find(".defaultsubjectName").text();
	var link = "addtopics?actionname=initiate&subid="+subjectIdTo+"&subname="+subjectname;
	location.href = link;
}

function saveNewSubjectName(){
	var that = $(this);
	var subjectNameToEdit = $(this).closest("tr").find(".editSubjectName").val();
	
	if(!validateInput(subjectNameToEdit)){
		$.notify({message: 'Only character and number is allowed'},{type: 'danger'});
		return; 
	}
	var subjectIdToEdit = $(this).closest("tr").find(".editSubjectId").val();
	 $.ajax({
		url: 'classOwnerServlet',
		type: 'post',
		data: {
			 methodToCall: "modifysubject",
			 subjectname:subjectNameToEdit,
			 subjectid:subjectIdToEdit
		},
		success:function(e){
			that.closest("tr").find(".defaultsubjectName").text(subjectNameToEdit);		
			cancelEdit.apply(that);
			e = JSON.parse(e);
			if (e.status == "success"){
				if(e.added){
					$.notify({message: 'Modified'},{type: 'success'});
				}else{
					$.notify({message: 'Error'},{type: 'danger'});
				}
			}else{
				$.notify({message: 'Error'},{type: 'danger'});
			}
		},
		error:function(e){
			console.log(e);
		}
	 });

}

function validateInput(inputText){
	var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9]{1,}$/;
	var isValidInput = CHAR_AND_NUM_VALIDATION.test(inputText);
	return isValidInput;
}
/*functions ends here*/

	var dataTable;
	function filterGlobal() {
		$('#subjectTable').DataTable().search(
			$('#tableSearchCustom').val()
			).draw();
	}

	$(document).ready(function(){
		$('body').on("click",".btn-edit",enableEdit)
			.on("click",".btn-cancel",cancelEdit)
			.on("click",".btn-save",saveNewSubjectName)
			.on("click",".btn-delete",deleteSubjectPrompt)
			.on("click",".btn-manage",manageLink);
			
		$('#tableSearchCustom').on("keyup click",filterGlobal);
		
		dataTable = $("#subjectTable").DataTable({"lengthChange": false});
				
		$('#manageSubjectAddSubject').on('click',function(){
		var subjectName = $('#subjectName').val();
		if(!subjectName || subjectName.trim()==""){
			$(this).closest('.addSubjectContainer').find(".error").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Subject name cannot be blank');
		}else{
			$(this).closest('.addSubjectContainer').find(".error").empty();
			if(validateInput(subjectName)){
				allAjax.addSubject('',subjectName,successCallbackSubject,errorCallbackSubject);
			}else{
				$(this).closest('.addSubjectContainer').find(".error").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Only character and numbers are allowed');
			}
		}
	});
		
	});
	
	function errorCallbackSubject(e){
		/*
		$('div#addSubjectModal .progress').addClass('hide');
		$('div#addSubjectModal .add').removeClass('hide');
		$('div#addSubjectModal .error').show();
		$('div#addSubjectModal .error').html('<strong>Error!</strong> Unable to add');
		*/
	}

	function successCallbackSubject(data){
		data = JSON.parse(data);
		if(data.status == "success"){
		$.notify({message: "Subject successfuly added"},{type: 'success'});
		dataTable = $('#subjectTable').DataTable({
			bDestroy:true,
			data: data.subjects,
			lengthChange: false,
			columns: [
				{title:"#",data:null,sWidth:"10%"},
				{ title: "Name",data:"subjectName",render:function(data,event,row){
					var modifiedObj = SUBJECT_NAME.replace("{{defaultsubjectNameValue}}",row.subjectName);
					modifiedObj = modifiedObj.replace("{{editSubjectNameValue}}",row.subjectName);
					modifiedObj = modifiedObj.replace("{{editSubjectIdValue}}",row.subjectId);
					return modifiedObj;
				},sWidth:"70%"},
				{ title: "",data:null,render:function(data){
					return BUTTONS_MANAGE+BUTTONS_CANCEL;
					},sWidth:"20%"}
			]
		});
		
		dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw();
		}else{
			var message = data.message;
			$.notify({message: message},{type: 'danger'});
		}
	}
	
	
</script>
</head>
<body>
 <br>
 <div class="container">
 <div class="row" style="background-color:#eee">
 
 <div class="col-lg-4">
	<input type="text" class="form-control" id="tableSearchCustom" placeholder="search">
 </div>
 <div class="col-lg-4 addSubjectContainer">
	
    <div class="input-group">
      <input type="text" class="form-control" id="subjectName">
      <span class="input-group-btn">
        <button id="manageSubjectAddSubject" class="btn btn-default" type="button">Add subject</button>
      </span>
    </div><!-- /input-group -->
    <div class="error"></div>
  </div><!-- /.col-lg-6 -->
  <div class="col-lg-3"></div>
  </div>
</div>

 <table class="table table-striped subjectTable" id="subjectTable" width="100%">
	<thead>
		<th>#</th><th>Subject name</th><th></th>
	</thead>
 <c:forEach items="${listOfSubjects}" var="subject" varStatus="counter">
 	<tr>
 		<td width="10%"><c:out value="${counter.count}"></c:out></td>
 		<td width="70%">
			<input type="hidden" class="hide editSubjectId" value='<c:out value="${subject.subjectId}"></c:out>'/>
			<div class="default defaultsubjectName"><c:out value="${subject.subjectName}"></c:out></div>
			<input type="text" value='<c:out value="${subject.subjectName}"></c:out>' class="form-control editable editSubjectName"/>
		</td>
 		<td width="20%">
			<div class="default">
				<button class="btn btn-primary btn-xs btn-manage">Manage</button>
				<button class="btn btn-primary btn-xs btn-edit">Edit</button>
				<button class="btn btn-danger btn-xs btn-delete">Delete</button>
			</div>
			<div class="editable">
				<button class="btn btn-success btn-xs btn-save">Save</button>
				<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>
			</div>
		</td>
 		
 	</tr>
 </c:forEach>
 </table>
</body>
</html>