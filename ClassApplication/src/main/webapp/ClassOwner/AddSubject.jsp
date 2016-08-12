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
.subjectTable .editEnabled .editable{
	display:block;
}

.subjectTable .editEnabled .select2{
	display:block;
}

.subjectTable .select2{
	display:none;
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
						'<button class="btn btn-primary btn-xs btn-edit">Edit</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-delete">Delete</button>'+
					'</div>';
var BUTTONS_TOPIC = '<div class="default">' +
					'<button class="btn btn-primary btn-xs btn-manage">Manage Topics</button></div>';

var BUTTONS_COMBINESUBJECT_MANAGE = '<div class="default">' +
					'<button class="btn btn-primary btn-xs btn-combineEdit">Edit</button>&nbsp;'+
					'<button class="btn btn-danger btn-xs btn-delete">Delete</button>'+
				'</div>';
					
var BUTTONS_CANCEL = '<div class="editable">' +
						'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>';
					
var BUTTONS_COMBINECANCEL = '<div class="editable">' +
					'<button class="btn btn-success btn-xs btn-combinesave">Save</button>&nbsp;'+
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
var optionSelect = {
		onText:"Yes",
		offText:"No",
		onColor:"primary",
		offColor:"default"
	};
/*
New function
*/
function deleteSubject(subid){
	/* $.ajax({
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
}); */
	var table = $('#subjectTable').DataTable();
	var handlers = {};
	handlers.success = function(e){
		/* var table = $("#subjectTable").DataTable();
		$("#subjectTable").find(".editSubjectId[value='"+subid+"']").closest("tr").addClass('selected');
		table.row('.selected').remove().draw( false ); */
		console.log(e);
		$.notify({message: "Subject successfuly deleted"},{type: 'success'});
		deleteSuccessCallbackSubject(e);
	}
	handlers.error = function(){
		/* table.row($("#subjectTable").find(".editSubjectId[value='"+subid+"']").closest("tr")).remove().draw(); */
		$.notify({message: "Subject not deleted"},{type: 'danger'});
	}
	rest.deleteItem("rest/commonDelete/deleteSubject/"+subid,handlers);
}

/*this function will prompt to delete subject*/
function deleteSubjectPrompt(){
	var subjectIdToEdit = $(this).closest("tr").find(".editSubjectId").val();
	var subjectName = $(this).closest("tr").find(".defaultsubjectName").val();
	
	deleteConfirm(subjectIdToEdit,subjectName);
}

function deleteConfirm(subId,subjectName){
	modal.modalConfirm("Delete","Do you want to delete "+subjectName+"? All data related to this subject will get deleted.","Cancel","Delete",deleteSubject,[subId]);
}

function enableEdit(){
	var subjectName = $(this).closest("tr").find(".defaultsubjectName").text().trim();
	$(this).closest("tr").find(".editSubjectName").val(subjectName);
	$(this).closest("tr").addClass("editEnabled");
	//$(this).closest("tr").find(".editSubjectName").val("");
}

function cancelEdit(){
	$(this).closest("tr").find(".error").empty();
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
	$(this).closest("tr").find(".error").empty();
	if(subjectNameToEdit.trim() == ""){
		that.closest("tr").find("#subjectError").html("Subject name cannot be blank");
		return; 
	}
	if(!validateInput(subjectNameToEdit)){
		that.closest("tr").find("#subjectError").html("Only character and number is allowed");
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
			
		
			e = JSON.parse(e);
			if (e.status == "success"){
				if(e.added == "true"){
					cancelEdit.apply(that);
					that.closest("tr").find(".defaultsubjectName").text(subjectNameToEdit);		
					$.notify({message: 'Subject modified'},{type: 'success'});
				}else{
					that.closest("tr").find("#subjectError").html("Subject already exists");
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
	var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9 ]{1,}$/;
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
		$("input[type=\"checkbox\"]").bootstrapSwitch(optionSelect);
		$('body').on("click",".btn-edit",enableEdit)
			.on("click",".btn-cancel",cancelEdit)
			.on("click",".btn-save",saveNewSubjectName)
			.on("click",".btn-delete",deleteSubjectPrompt)
			.on("click",".btn-manage",manageLink);
			
		$('#tableSearchCustom').on("keyup click",filterGlobal);
		
		dataTable = $("#subjectTable").DataTable();
		$("#combinationSubjects").select2({data:"",placeholder:"Select Combinational Subject"});
		
		$('#manageSubjectAddSubject').on('click',function(){
			var subjectFlag = true;
			$('.addSubjectContainer').find(".error").html('');
			$(".combinationSubjectserror").html("");
		var subjectName = $('#subjectName').val();
		var combinationSub = $("#combinationSub").is(":checked");
		var subIds = "";
		if(combinationSub && $("#combinationSubjects").val() == null){
			$(".combinationSubjectserror").html("Select subjects");
			subjectFlag = false;
		}
		else if(combinationSub){
			subIds = $("#combinationSubjects").val().join(",");
		}
		if(!subjectName || subjectName.trim()==""){
			$('.addSubjectContainer').find(".error").html('Subject name cannot be blank');
			subjectFlag = false;
		}else if(!validateInput(subjectName)){
			subjectFlag = false;
			$('.addSubjectContainer').find(".error").html('Only character and numbers are allowed');
		}
			if(subjectFlag){
				allAjax.addSubject('',subjectName,combinationSub,subIds,successCallbackSubject,errorCallbackSubject);
			}
		
	});
		
		$('input[name="combinationSub"]').on('switchChange.bootstrapSwitch',function(e){
			if($(this).is(":checked")){
				var handlers = {};
				handlers.success = function(data){
					$("#combinationSubjects").prop("disabled",false);
					var subjectArray = [];
					$.each(data,function(key,val){
						var data = {};
						if(val.sub_type != "1"){
						data.id = val.subjectId;
						data.text = val.subjectName;
						subjectArray.push(data);
						}
					});
					$("#combinationSubjects").select2({data:subjectArray,placeholder:"Select Combinational Subject"});
				}
				handlers.error = function(e){}
				rest.get("rest/classownerservice/allInstituteSubjects/",handlers);
				
			}else{
				$("#combinationSubjects").select2().val("")
				$("#combinationSubjects").select2({data:"",placeholder:"Select Combinational Subject"});
				$("#combinationSubjects").prop("disabled",true);
			}
		});
		
		$('#subjectTable').on("click",".btn-combineEdit",function(){
			var combineSubjectsIds = $(this).closest("tr").find(".combineSubjectsIds").val();
			if(combineSubjectsIds != null && combineSubjectsIds != ""){
				combineSubjectsIds = combineSubjectsIds.split(",");
			}
			var handlers = {};
			var that = $(this);
			handlers.success = function(data){
				var subjectArray = [];
				$.each(data,function(key,val){
					var data = {};
					if(val.sub_type != "1"){
					data.id = val.subjectId;
					data.text = val.subjectName;
					subjectArray.push(data);
					}
				});
				$(that.closest("tr")).find(".editSubjectSelect").select2({data:subjectArray,placeholder:"Select Subjects"}).val(combineSubjectsIds).change();
			}
			handlers.error = function(e){}
			rest.get("rest/classownerservice/allInstituteSubjects/",handlers);
			var subjectName = $(this).closest("tr").find(".defaultsubjectName").text().trim();
			$(this).closest("tr").find(".editSubjectName").val(subjectName);
			$(this).closest("tr").addClass("editEnabled");
		});
		
		$('#subjectTable').on("click",".btn-combinesave",function(){
			var that = $(this);
			var subjectNameToEdit = $(this).closest("tr").find(".editSubjectName").val();
			$(this).closest("tr").find(".error").empty();
			if(subjectNameToEdit.trim() == ""){
				that.closest("tr").find("#subjectError").html("Subject name cannot be blank");
				return; 
			}
			if(!validateInput(subjectNameToEdit)){
				that.closest("tr").find("#subjectError").html("Only character and number is allowed");
				return; 
			}
			var subjectIdToEdit = $(this).closest("tr").find(".editSubjectId").val();
			var comsubjectsToEdit = $(this).closest("tr").find(".editSubjectSelect").val();
			if(comsubjectsToEdit == null || comsubjectsToEdit == ""){
				that.closest("tr").find("#editSubjectSelectError").html("Select subjects");
				return; 
			}
			var handlers = {};
			handlers.success = function(data){
				if(data == true){
					cancelEdit.apply(that);
					that.closest("tr").find(".defaultsubjectName").text(subjectNameToEdit);		
					that.closest("tr").find(".combineSubjectsIds").val(comsubjectsToEdit.join(","));
					$.notify({message: 'Subject modified'},{type: 'success'});
				}else{
					that.closest("tr").find("#subjectError").html("Subject already exists");
				}
				
			}
			handlers.error = function(e){}
			var subject = {};
			subject.subjectId = subjectIdToEdit;
			subject.subjectName = subjectNameToEdit;
			subject.com_subjects = comsubjectsToEdit.join(",");
			console.log(subject);
			rest.post("rest/classownerservice/updateCombineSubject/",handlers,JSON.stringify(subject));
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
			if(data.subjects.length > 0){
				for(i=0;i<data.subjects.length;i++){
					data.subjects[i].index = (i+1);
				}
			}
		$.notify({message: "Subject successfuly added"},{type: 'success'});
		dataTable = $('#subjectTable').DataTable({
			bDestroy:true,
			data: data.subjects,
			columns: [
				{title:"Sr No.",data:null,render:function(data,event,row){
					return row.index;
				},sWidth:"10%"},
				{ title: "Name",data:"subjectName",render:function(data,event,row){
					var modifiedObj = SUBJECT_NAME.replace("{{defaultsubjectNameValue}}",row.subjectName);
					modifiedObj = modifiedObj.replace("{{editSubjectNameValue}}",row.subjectName);
					modifiedObj = modifiedObj.replace("{{editSubjectIdValue}}",row.subjectId);
					modifiedObj = modifiedObj + '<span id="subjectError" class="error"></span>';
					if(row.sub_type == "1"){
					modifiedObj = modifiedObj + '<select class="form-control editable editSubjectSelect" multiple="multiple"></select>'
					modifiedObj = modifiedObj +'<input type="hidden" class="hide combineSubjectsIds" value="'+row.com_subjects+'"/>';
					modifiedObj = modifiedObj + '<span id="editSubjectSelectError" class="error"></span>';
					}
					return modifiedObj;
				},sWidth:"60%"} ,
				{ title: "Topics",data:data,render:function(data,event,row){
					if(row.sub_type != "1"){
						return BUTTONS_TOPIC;
					}else{
						return "";
					}
				},sWidth:"10%"},
				{ title: "Actions",data:null,render:function(data,event,row){
					if(row.sub_type != "1"){
					return BUTTONS_MANAGE+BUTTONS_CANCEL;
					}
					if(row.sub_type == "1"){
					return BUTTONS_COMBINESUBJECT_MANAGE+BUTTONS_COMBINECANCEL;
					}
					},sWidth:"20%"}
			]
		});
		
		/* dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); */
		}else{
			var message = data.message;
		//	$.notify({message: message},{type: 'danger'});
			$('.addSubjectContainer').find(".error").html(message);
		}
	}
	
	function deleteSuccessCallbackSubject(data){
		dataTable = $('#subjectTable').DataTable({
			bDestroy:true,
			data: data,
			columns: [
				{title:"Sr No.",data:null,sWidth:"10%"},
				{ title: "Name",data:data,render:function(data,event,row){
					var modifiedObj = SUBJECT_NAME.replace("{{defaultsubjectNameValue}}",row.subjectName);
					modifiedObj = modifiedObj.replace("{{editSubjectNameValue}}",row.subjectName);
					modifiedObj = modifiedObj.replace("{{editSubjectIdValue}}",row.subjectId);
					modifiedObj = modifiedObj + '<span id="subjectError" class="error"></span>';
					if(row.sub_type == "1"){
					modifiedObj = modifiedObj + '<select class="form-control editable editSubjectSelect" multiple="multiple"></select>'
					modifiedObj = modifiedObj +'<input type="hidden" class="hide combineSubjectsIds" value="'+row.com_subjects+'"/>';
					modifiedObj = modifiedObj + '<span id="editSubjectSelectError" class="error"></span>';
					}
					return modifiedObj;
				},sWidth:"60%"} ,
				{ title: "Topics",data:data,render:function(data,event,row){
					if(row.sub_type != "1"){
						return BUTTONS_TOPIC;
					}else{
						return "";
					}
				},sWidth:"10%"},
				{ title: "Actions",data:data,render:function(data,event,row){
					if(row.sub_type != "1"){
					return BUTTONS_MANAGE+BUTTONS_CANCEL;
					}
					if(row.sub_type == "1"){
					return BUTTONS_COMBINESUBJECT_MANAGE+BUTTONS_COMBINECANCEL;
					}
					},sWidth:"20%"} 
			]
		});
		
	 dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); 
	
	}
	
	
</script>
</head>
<body>
<jsp:include page="ClassSubjectNBatchHeaders.jsp" >
		<jsp:param value="active" name="addsubject"/>
	</jsp:include>
 <div class="well">
 <div class="row">
 <div class="col-md-3 addSubjectContainer">
      <input type="text" class="form-control" id="subjectName" maxlength="25" placeholder="Enter Subject Name">
    <div class="error"></div>
  </div><!-- /.col-lg-6 -->
   <div class="col-md-3">
  Combination Subject : <input type="checkbox" id="combinationSub" name="combinationSub">
  </div>
   <div class="col-md-3 addSubjectContainer">
      <select id="combinationSubjects" class="form-control" disabled="disabled" multiple="multiple"></select>
      <div class="combinationSubjectserror" style="color: red"></div>
  </div>
   <div class="col-md-3 addSubjectContainer">
        <button id="manageSubjectAddSubject" class="btn btn-primary" type="button">Add subject</button>
  </div>
  </div>
</div>

<div class="container">
 <table class="table table-striped subjectTable" id="subjectTable" width="100%">
	<thead>
		<th>#</th><th>Subject name</th><th>Topics</th><th>Actions</th>
	</thead>
 <c:forEach items="${listOfSubjects}" var="subject" varStatus="counter">
 	<tr>
 		<td width="10%"><c:out value="${counter.count}"></c:out></td>
 		<td width="70%">
			<input type="hidden" class="hide editSubjectId" value='<c:out value="${subject.subjectId}"></c:out>'/>
			<div class="default defaultsubjectName"><c:out value="${subject.subjectName}"></c:out></div>
			<input type="text" value='<c:out value="${subject.subjectName}"></c:out>' class="form-control editable editSubjectName"/>
			<span id="subjectError" class="error"></span>
			<c:if test="${subject.sub_type eq 1}">
			<select class="form-control editable editSubjectSelect" multiple="multiple"></select>
			<input type="hidden" class="hide combineSubjectsIds" value='<c:out value="${subject.com_subjects}"></c:out>'/>
			<span id="editSubjectSelectError" class="error"></span>
			</c:if>
			
		</td>
		<td>
		<c:if test="${subject.sub_type ne 1}">
				<button class="btn btn-primary btn-xs btn-manage">Manage Topics</button>
		</c:if>
		</td>
 		<td width="20%">
			<div class="default">
				
				<c:if test="${subject.sub_type eq 1}">
				<button class="btn btn-primary btn-xs btn-combineEdit">Edit</button>
				</c:if>
				<c:if test="${subject.sub_type ne 1}">
				<button class="btn btn-primary btn-xs btn-edit">Edit</button>
				</c:if>
				<button class="btn btn-danger btn-xs btn-delete">Delete</button>
			</div>
			<div class="editable">
				<c:if test="${subject.sub_type eq 1}">
				<button class="btn btn-success btn-xs btn-combinesave">Save</button>
				</c:if>
				<c:if test="${subject.sub_type ne 1}">
				<button class="btn btn-success btn-xs btn-save">Save</button>
				</c:if>
				<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>
			</div>
		</td>
 		
 	</tr>
 </c:forEach>
 </table>
 </div>
</body>
</html>