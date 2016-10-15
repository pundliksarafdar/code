<%@page import="org.apache.commons.lang3.ArrayUtils"%>
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
	rest.deleteItem("rest/customuserservice/deleteSubject/"+subid,handlers);
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
	var link = "customUserAddtopics?actionname=initiate&subid="+subjectIdTo+"&subname="+subjectname;
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
	var subject = {};
	subject.subjectName = subjectNameToEdit;
	subject.subjectId = subjectIdToEdit;
	subject.sub_type = "0";	
	subject.com_subjects = "";
	var handlers = {};
	handlers.success = function(resp){
		if(resp == true){
			cancelEdit.apply(that);
			that.closest("tr").find(".defaultsubjectName").text(subjectNameToEdit);
			$.notify({message: "Subject updated"},{type: 'success'});
		}else{
			that.closest("tr").find("#subjectError").html("Subject already exists");
		}
		//successCallbackSubject(resp)
	}
	handlers.error = function(){
		$.notify({message: "Subject not added"},{type: 'danger'});
	}
	rest.post("rest/customuserservice/updateSubject",handlers,JSON.stringify(subject));
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
		var child_mod_access  = $("#accessRights").val().split(",");
		BUTTONS_MANAGE = '<div class="default">';
		if($.inArray( "5", child_mod_access) != "-1"){
			BUTTONS_MANAGE = BUTTONS_MANAGE +	'<button class="btn btn-primary btn-xs btn-edit">Edit</button>&nbsp;';
		}
		if($.inArray( "6", child_mod_access) != "-1" ){
			BUTTONS_MANAGE = BUTTONS_MANAGE +	'<button class="btn btn-danger btn-xs btn-delete">Delete</button>';
		}
		BUTTONS_MANAGE = BUTTONS_MANAGE +'</div>';
		
		BUTTONS_COMBINESUBJECT_MANAGE = '<div class="default">';
		if($.inArray( "5", child_mod_access) != "-1"){
			BUTTONS_COMBINESUBJECT_MANAGE = BUTTONS_COMBINESUBJECT_MANAGE +	'<button class="btn btn-primary btn-xs btn-combineEdit">Edit</button>&nbsp;';
		}
		if($.inArray( "6", child_mod_access) != "-1" ){
			BUTTONS_COMBINESUBJECT_MANAGE = BUTTONS_COMBINESUBJECT_MANAGE +	'<button class="btn btn-danger btn-xs btn-delete">Delete</button>';
		}
		BUTTONS_COMBINESUBJECT_MANAGE = BUTTONS_COMBINESUBJECT_MANAGE +'</div>';
		
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
				var subject = {};
				subject.subjectName = subjectName;
				if(combinationSub == true){
				subject.sub_type = "1";
				subject.com_subjects = subIds;
				}
				else {
					subject.sub_type = "0";	
					subject.com_subjects = subIds;
				}
				var handlers = {};
				handlers.success = function(resp){
					successCallbackSubject(resp)
				}
				handlers.error = function(){
					$.notify({message: "Class not added"},{type: 'danger'});
				}
				rest.post("rest/customuserservice/addSubject",handlers,JSON.stringify(subject));
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
				rest.get("rest/customuserservice/allInstituteSubjects/",handlers);
				
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
			rest.get("rest/customuserservice/allInstituteSubjects/",handlers);
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
			rest.post("rest/customuserservice/updateCombineSubject/",handlers,JSON.stringify(subject));
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
			if(data != false){
			$.notify({message: "Subject successfuly added"},{type: 'success'});
		dataTable = $('#subjectTable').DataTable({
			bDestroy:true,
			data: data,
			columns: [
				{title:"#",data:null},
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
		
	  dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); 
		}else{
			var message = data.message;
		//	$.notify({message: message},{type: 'danger'});
			$('.addSubjectContainer').find(".error").html("Subject is already exist");
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
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<jsp:include page="ClassSubjectNBatchHeaders.jsp" >
		<jsp:param value="active" name="customUserManagesubject"/>
	</jsp:include>
  <% if(ArrayUtils.contains(child_mod_access,"4")){ %>
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
<%} %>
<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
<div class="container" style="padding-top: 1%">
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
				<% if(ArrayUtils.contains(child_mod_access,"5")){ %>
				<c:if test="${subject.sub_type eq 1}">
				<button class="btn btn-primary btn-xs btn-combineEdit">Edit</button>
				</c:if>
				<c:if test="${subject.sub_type ne 1}">
				<button class="btn btn-primary btn-xs btn-edit">Edit</button>
				</c:if>
				<%} %>
				<% if(ArrayUtils.contains(child_mod_access,"6")){ %>
				<button class="btn btn-danger btn-xs btn-delete">Delete</button>
				<%} %>
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