<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
 <style type="text/css">
 .error{
     color: red;
    margin-left: 10px;
}

.editDiv{
display: none;
}

.topictable .editEnabled .editable{
	display:block;
}

.topictable .editEnabled .default{
	display:none;
}

.topictable .editable{
	display:none;
}

.topictable .default{
	display:show;
}
</style>
<script type="text/javascript">
var BUTTONS_MANAGE = '<div class="default">' +
'<button class="btn btn-primary btn-xs btn-edit">Edit</button>&nbsp;'+
'<button class="btn btn-danger btn-xs btn-delete">Delete</button>'+
'</div>';

var BUTTONS_CANCEL = '<div class="editable">' +
'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
'</div>';
var topicid="";
$(document).ready(function(){
	$("#topics_division").on("change",fetchTopic);
	$(".deletetopic").on("click",deletetopic);
	$("#addTopic").on("click",addTopic);
	/* $("#saveTopic").on("click",saveedittopic); */
	/* $("#topictable").on("click",".editTopic",edittopic);	
	$("#topictable").on("click",".cancleEdit",cancleEdit);
	$("#topictable").on("click",".saveTopic",saveedittopic);
	$("#topictable").on("click",".deletetopic",deletetopic); */
	$('body').on("click",".btn-edit",edittopic)
	.on("click",".btn-cancel",cancelEdit)
	.on("click",".btn-save",saveedittopic)
	.on("click",".btn-delete",deleteclassPrompt);
});

function addTopic(){
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	var topicname=$("#topicname").val();
	var flag=false;
	$("#topicnameerror").html("");
	var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9-_\s]{1,}$/;
	if(topicname.trim()==""){
		$("#topicnameerror").html("Please Enter Topic Name");
		flag=true;
	}else if(!CHAR_AND_NUM_VALIDATION.test(topicname.trim())){
		$("#topicnameerror").html("Invalid Topic Name");
	}else{
	var topic = {};
    topic.topic_name = topicname;
    topic.sub_id = subID;
    topic.div_id = divisionID;
	var handlers = {};
	handlers.success=function(resp){
		if(resp){
		$.notify({message: "Topic added successfuly"},{type: 'success'});
		fetchTopic();
		}else{
	   $("#topicnameerror").html("Topic name already available!!!");	
		}
	};   
	handlers.error=function(){
		$.notify({message: "Error"},{type: 'danger'});
	};   
	
	rest.post("rest/customuserservice/addTopic/",handlers,JSON.stringify(topic));
	}
	
}

var edittopicid="";
function edittopic(){
	topicid = $($(this).closest("tr")).find(".topicId").val();
	edittopicid=topicid;
	var topicname=$("#topic"+topicid).html();
	$($(this).closest("tr")).find(".editTopicName").val($($(this).closest("tr")).find(".defaultTopicName").html());
	$(this).closest("tr").addClass("editEnabled");
	/* $("#edittopicname").val(topicname);
	$("#topicedit").modal('toggle'); */
}

function cancelEdit(){
	$(this).closest("tr").removeClass("editEnabled");
}

function saveedittopic(){
	var that = this;
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	var topicname=$($(this).closest("tr")).find(".editTopicName").val();
	var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9-_\s]{1,}$/;
	if(topicname.trim()==""){
		$($(this).closest("tr")).find(".edittopicnameerror").html("Please Enter Topic Name");
		flag=true;
	}else if(!CHAR_AND_NUM_VALIDATION.test(topicname.trim())){
		$($(this).closest("tr")).find(".edittopicnameerror").html("Invalid Topic Name");
	}else{
	var topic = {};
	topic.topic_id = edittopicid;
    topic.topic_name = topicname;
    topic.sub_id = subID;
    topic.div_id = divisionID;
	var handlers = {};
	handlers.success=function(resp){
		if(resp){
		$.notify({message: "Topic updated successfuly"},{type: 'success'});
		fetchTopic();
		}else{
			 $($(that).closest("tr")).find(".edittopicnameerror").html("Topic name already available!!!");	
		}
	};   
	handlers.error=function(){
		$.notify({message: "Error"},{type: 'danger'});
	};   
	
	rest.post("rest/customuserservice/editTopic/",handlers,JSON.stringify(topic));
	}
	 
}

function deleteclassPrompt(){
	var topicID = $(this).closest("tr").find(".topicId").val();
	var topicName = $(this).closest("tr").find(".defaultTopicName").html();
	deleteConfirm(topicID,topicName);
}

function deleteConfirm(topicID,topicName){
	modal.modalConfirm("Delete","Do you want to delete "+topicName+"?","Cancel","Delete",deletetopic,[topicID]);
}

function deletetopic(topicid){
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	var handlers = {};
	handlers.success=function(){
		$.notify({message: "Topic successfuly deleted"},{type: 'success'});
		fetchTopic();
	};   
	handlers.error=function(){
		$.notify({message: "Error"},{type: 'danger'});
	};   
	
	rest.deleteItem("rest/customuserservice/deleteSubjectTopic/"+divisionID+"/"+subID+"/"+topicid,handlers);
	 
}
function fetchTopic(){
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	 if(divisionID!="-1"){
		 $("#topicname").prop("disabled",false);
		 $("#addTopic").prop("disabled",false);
	 }else{
		 $("#topicname").prop("disabled",true);
		 $("#addTopic").prop("disabled",true);
	 }	 
	 var handlers = {};
		handlers.success = function(data){
			 var dataTable = $('#topictable').DataTable({
					bDestroy:true,
					data: data,
					columns: [
						{title:"#",data:null},
						{ title: "Topic Name",data:data,render:function(data,event,row){
							var div = '<div class="default defaultTopicName">'+row.topic_name+'</div>';
							var input= '<input type="text" value="'+row.topic_name+'" class="form-control editable editTopicName">';
							var topicID = '<input type="hidden" value="'+row.topic_id+'" class="topicId">';
							var span = '<span class="editable edittopicnameerror error"></span>'
							return div+input+topicID+span;
						}},
						{ title: "Actions",data:null,render:function(data){
							return BUTTONS_MANAGE+BUTTONS_CANCEL;
						}}
					]
				});
			  dataTable.on( 'order.dt search.dt', function () {
			        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
			            cell.innerHTML = i+1;
						});
					}).draw(); 
		}
		handlers.error = function(){
			$.notify({message: "Class not added"},{type: 'danger'});
		}
		rest.get("rest/customuserservice/getDivisionsTopics/"+subID+"/"+divisionID,handlers);
	
}

</script>
</head>
<body>
<input type="hidden" value="<c:out value="${subid}"> </c:out>" id="subjectID">
<div class="container" style="margin-bottom: 5px;margin-left: 0%;padding: 0%">
			<a type="button" class="btn btn-primary" href="managesubject"> <span class="glyphicon glyphicon-circle-arrow-left"></span> Back To Manage Subjects</a>
</div>
<div class="well" style="margin-bottom: 5px">
	<div align="center" style="font-size: larger;">Add Topics</div>
			<div class="row">
				<div class='col-sm-6 header' style="padding-bottom: 10px;color: black;">*
					<c:out value="${subjectname}"></c:out>
				</div>
			</div>
			
			<div class="row">
			<div class="col-md-4">
			<select class="form-control" id="topics_division">
			<option value="-1">Select Class</option>
			<c:if test="${ (divisionList != null) }">
			<c:forEach items="${divisionList}" var="item">
				<option value="<c:out value="${item.divId}"> </c:out>"> <c:out value="${item.divisionName}"> </c:out> <c:out value="${item.stream}"> </c:out></option>
			</c:forEach>
			</c:if> 
			</select>
			</div>
			<div class="col-md-4">
			<input type="text" class="form-control" id="topicname" disabled="disabled" maxlength="50">
			<span id="topicnameerror" class="error"></span>
			</div>
			<div class="col-md-4">
			<button id="addTopic" class="btn btn-primary">Add</button>
			</div>
			</div>
</div>
		<div class="container" id="topicbasediv">            
  <table class="table table-striped topictable" id="topictable" style="width: 100%">
    </table>
    <div class="alert alert-info" align="center" id="notopicdiv" style="display: none">Topics are not available for selected criteria.</div>
    </div>	
    
    <div class="modal fade" id="topicedit" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Topic Edit
            </h4>
         </div>
         <div class="modal-body">
          <input type="text" id="edittopicname" class="form-control">
          <span id="edittopicnameerror" class="error"></span>
         </div>
         <div class="modal-footer">
         <button id="saveTopic" class="btn btn-primary">Save</button>
         </div>
         </div>
   </div>
</div>
   	<div class="modal fade" id="DeleteConfirmModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Topic Delete
            </h4>
         </div>
         <div class="modal-body" id="DeleteConfirmBody">
           
         </div>
         <div class="modal-footer">
	        <button type="button" class="btn btn-default"  data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="QuestionDeleteConfirm" onclick="deletetopic()">Ok</button>
      	</div>
         </div>
   </div>
</div> 
</body>
</html>