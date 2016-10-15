<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style>
		/* .select2-container .select2-selection--single{
height: 34px;
}

.select2-container .select2-selection--single .select2-selection__rendered{
padding-top: 2px;
} */

.paginationPage, .paginationStart, .paginationEnd{
cursor: pointer;
}
		</style>
<script>
var currentPage = 0;
var totalPages = 0;
var division;
var subject;
var questionType;
var topic="-1";
var selectedMarks ="-1";
var filterSearch = false;
var quesstatus;
var questionNumber;
var deleteStatus;
var subId="";
var child_mod_access  = [];
var subId = <c:out value="${subId}" default="-1"></c:out>;
var divId = <c:out value="${classId}" default="-1"></c:out>;
var questionType = <c:out value="${questionType}" default="-1"></c:out>;

$(document).ready(function(){
	/*Load value if return from cancel button inside list*/
	child_mod_access  = $("#accessRights").val().split(",");
	if($("#editQuestionForm").find("#actionname").val() == "editSuccess" || $("#editQuestionForm").find("#actionname").val() == "cancleEdit"){
		$("#searchQuestionDivision").select2().val($("#editQuestionForm").find("#division").val()).change();
		division = $("#editQuestionForm").find("#division").val();
		var uploadExam = new UploadExam();
		uploadExam.getSubjectsInDivision(division);
		subId = $("#editQuestionForm").find("#subject").val();
		subject = subId;
		questionType = $("#editQuestionForm").find("#questiontype").val();
		$("#searchQuestionType").select2().val(questionType).change();
		topic = $("#editQuestionForm").find("#topicID").val();
		currentPage = $("#editQuestionForm").find("#currentPage").val();
		totalPages = $("#editQuestionForm").find("#totalPages").val();
		selectedMarks=$("#editQuestionForm").find("#preSelectedMarks").val();
		searchQuestion(currentPage);
		if($("#editQuestionForm").find("#actionname").val() == "editSuccess" ){
		$.notify({message: "Question updated successfully!"},{type: 'success'});
		}
		$("#editQuestionForm").find("#actionname").val("");
	}
	$("#searchQuestionDivision").select2();
$("#searchQuestionDivision").on("change",function(e){
	
	$("#filterDiv").hide();
	$("#paginationDiv").hide();
	$("#questionListDiv").hide();
	if($("#searchQuestionSubject").val() != null){
	$("#searchQuestionSubject").select2().val("-1").change();
	$("#searchQuestionSubject").find('option:gt(0)').remove();
	}
	if($(this).val()!=-1){
		$("#searchQuestionDivisionError").html("");
		var uploadExam = new UploadExam();
		uploadExam.getSubjectsInDivision($(this).val());
	}/* else{
		$("#searchQuestionSubject").prop("disabled",true);
		$("#searchQuestionTopic").prop("disabled",true);
		$("#classownerUploadexamAddExam").prop("disabled",true);
	} */
});

$("#searchQuestionSubject").change(function(){
	$("#filterDiv").hide();
	$("#paginationDiv").hide();
	$("#questionListDiv").hide();
	if($("#searchQuestionSubject").val() != "-1"){
		$("#searchQuestionSubjectError").html("");
	}
});

$("#searchQuestionType").change(function(){
	$("#filterDiv").hide();
	$("#paginationDiv").hide();
	$("#questionListDiv").hide();
	if($("#searchQuestionType").val() != "-1"){
		$("#searchQuestionTypeError").html("");
	}
});

	$("#searchQuestion").click(searchQuestionBtnClick);
	
	$("#paginationDiv").on("click",".paginationPage",function(){
		searchQuestion($(this).text());
	});
	
	$("#paginationDiv").on("click",".paginationStart",function(){
		if(currentPage != 1){
		searchQuestion(1);
	}
	});
	
	$("#paginationDiv").on("click",".paginationEnd",function(){
		if(currentPage != totalPages){
		searchQuestion(totalPages);
		}
	});
	
	$("#marksFilter").click(function(){
		selectedMarks = "-1";
		$(".appliedFilter").find("#marksFilter").hide();
		currentPage = 0;
		searchQuestion(currentPage);
	});
	
	$("#topicFilter").click(function(){
		topic = "-1";
		$(".appliedFilter").find("#topicFilter").hide();
		currentPage = 0;
		searchQuestion(currentPage);	
	});
	
	$("#fliter").click(function(){
		
		topic =  $("#searchQuestionTopic").val();
		selectedMarks =  $("#searchQuestionMarks").val();
		if(topic != "-1" && topic != null){
		$(".appliedFilter").find("#topicFilter").html($("#searchQuestionTopic").select2('data')[0].text+"&nbsp;<i class='glyphicon glyphicon-remove' style='color:red;top:2px'></i>");
		$(".appliedFilter").find("#topicFilter").show();
		}else{
			topic = "-1";
			$(".appliedFilter").find("#topicFilter").hide();
		}
		if(selectedMarks != "-1"){
		$(".appliedFilter").find("#marksFilter").html($("#searchQuestionMarks").select2('data')[0].text+"&nbsp;<i class='glyphicon glyphicon-remove' style='color:red;top:2px'></i>");
		$(".appliedFilter").find("#marksFilter").show();
		}else{
			$(".appliedFilter").find("#marksFilter").hide();
		}
		$(".appliedFilter").show();
		currentPage = 0;
		filterSearch = true;
		searchQuestion(currentPage);
	});
	
	$("#QuestionDeleteConfirm").click(function(){
		var institute=$("form#actionform #institute").val();
		/* $.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "deleteQuestions",
			    	 questionNumber:questionNumber,
			    	 subject:subject,
			    	 division:division,
			    	 institute:institute,
			    	 quesstatus:quesstatus,
			    	 questionType:questionType
			   		},
			   		success:function(data){
			   			var resultJson = JSON.parse(data);
			   			deleteStatus = "Y";
			   			searchQuestion(currentPage);
			   		},
			   		error:function(error){
			   		
			   		}
		}); */
		
		var uri = "rest/customuserservice/deleteQuestion/"+questionNumber+"/"+subject+"/"+division+"/"+questionType;
		var handlers = {};
		handlers.success = function(data){
			searchQuestion(currentPage);
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
		rest.deleteItem(uri,handlers);
	});
	
	$("body").on("click",".editQuestion",function(e){
		/*
		$("#editQuestionForm").find("#division").val(division);
		$("#editQuestionForm").find("#subject").val(subject);
		$("#editQuestionForm").find("#currentPage").val(currentPage);
		$("#editQuestionForm").find("#totalPages").val(totalPages);
		$("#editQuestionForm").find("#questionNumber").val($(this).prop("id"));
		$("#editQuestionForm").find("#topicID").val(topic);
		$("#editQuestionForm").find("#questiontype").val(questionType);
		$("#editQuestionForm").find("#preSelectedMarks").val(selectedMarks);
		$("#editQuestionForm").submit();
		*/
		
		$("#editQuestionNewForm").find("#division").val(division);
		$("#editQuestionNewForm").find("#subject").val(subject);
		$("#editQuestionNewForm").find("#questionNumber").val($(this).prop("id"));
		$("#editQuestionNewForm").find("#topicID").val(topic);
		$("#editQuestionNewForm").find("#questiontype").val(questionType);
		$("#editQuestionForm").find("#questionNumber").val($(this).prop("id"));
		$("#editQuestionNewForm").submit();
	});
	
	$("body").on("click",".deleteQuestion",function(e){
		$("#questionNumber").val($(this).prop("id"));
		/* var subject=$("form#actionform #subject").val();
		var division=$("form#actionform #division").val(); */
		var institute=$("form#actionform #institute").val();
		questionNumber=$(this).prop("id");
		e.preventDefault();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "isQuestionAvailableInExam",
			    	 questionNumber:questionNumber,
			    	 subject:subject,
			    	 division:division,
			    	 institute:institute
			   		},
			   		success:function(data){
			   			var resultJson = JSON.parse(data);
						    quesstatus=resultJson.quesstatus;
						   if(quesstatus=="Y"){
							   $("#DeleteConfirmBody").empty();
							  var examnames=resultJson.examnames.split(",");
							   $("#DeleteConfirmBody").append("This Question is present in following exams-<br>")
							   for(var i=0;i<examnames.length;i++){
								   $("#DeleteConfirmBody").append((i+1)+"."+examnames[i]+"<br>");
							   }
							   $("#DeleteConfirmBody").append("Still you delete, this question will remain in exams but will not be available in search. Once you delete that exams this question will get deleted.<br>")
						   		$("#DeleteConfirmBody").append("Do you want to continue?");
							   quesstatus ="Y";
							   $("#DeleteConfirmModal").modal("toggle");
						   }else{
							   $("#DeleteConfirmBody").empty();
							   $("#DeleteConfirmBody").append("Are you sure?");
							   quesstatus = "";
							   $("#DeleteConfirmModal").modal("toggle");
						   }
			   		},
			   		error:function(error){
			   		
			   		}
		});
		/* $("#actionform").attr("action","deletequestion");
		$("#actionname").val("deletequestion");
		$("#actionform").submit(); */
	});
	
	/*
	This code is usefull to load the value in list and dropdown
	when we are canceling question edit 
	*/
	if(subId!=-1){
		$("#searchQuestionDivision").val(divId).trigger("change");
		$("#searchQuestionType").select2().val(questionType).change();
	}
	
	
});

function getTopics(){
	/* $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getDivisionsTopics",
		    	 divisionID: division,
		    	 subID:subject
		   		},
		   type:"POST",
		   success:function(data){
			   $("#searchQuestionTopic").removeAttr('disabled');
			   $("#searchQuestionTopic").empty();
			   var subjectArray = [];
			 	var data = JSON.parse(data);
			 	if(data.status == "success"){
			 		var tempData = {};
			 		tempData.id = "-1";
			 		tempData.text = "Select Topic";
			 		subjectArray.push(tempData);
			 	 $.each(data.topicJson,function(key,val){
						var data = {};
						data.id = val.topic_id;
						data.text = val.topic_name;
						subjectArray.push(data);
					});
			 	 
				    $("#searchQuestionTopic").select2({data:subjectArray,placeholder:"Type Topic Name"});
					 if(topic != "-1" && topic !=null){
						 $("#searchQuestionTopic").select2().val(topic).change();
						 $(".appliedFilter").find("#topicFilter").html($("#searchQuestionTopic").select2('data')[0].text+"&nbsp;<i class='glyphicon glyphicon-remove' style='color:red;top:2px'></i>");
						$(".appliedFilter").find("#topicFilter").show();
				    }
			 	 }else{
			 		 $("#searchQuestionTopic").select2({data:"",placeholder:"Topic Not Found"});
			 	 }
				    //displaySubjectDropDown(data);
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   }); */
	
	var uri = "rest/customuserservice/getDivisionsTopics/"+subject+"/"+division;
	var handlers = {};
	handlers.success = function(data){
		$("#searchQuestionTopic").removeAttr('disabled');
		   $("#searchQuestionTopic").empty();
		   var subjectArray = [];
		 	if(data.length > 0){
		 		var tempData = {};
		 		tempData.id = "-1";
		 		tempData.text = "Select Topic";
		 		subjectArray.push(tempData);
		 	 $.each(data,function(key,val){
					var data = {};
					data.id = val.topic_id;
					data.text = val.topic_name;
					subjectArray.push(data);
				});
		 	 
			    $("#searchQuestionTopic").select2({data:subjectArray,placeholder:"Type Topic Name"});
		 	 }else{
		 		 $("#searchQuestionTopic").select2({data:"",placeholder:"Topic Not Found"});
		 	 }
	}
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
	rest.get(uri,handlers);
	
}

function searchQuestion(pageNo){
	$(".validation-message").empty();
	
	var flag = false;
	if(division == "-1"){
		$("#searchQuestionDivisionError").html("Select Division!");
		flag = true;
	}
	if(subject == "-1" || subject == null){
		$("#searchQuestionSubjectError").html("Select Subject!");
		flag = true;
	}
	if(questionType == "-1"){
		$("#searchQuestionTypeError").html("Select Question Type!");
		flag = true;
	}
	if(flag == false){
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getCustomUserSearchedQuestions",
		    	 division: division,
		    	 subject:subject,
		    	 questionType:questionType,
		    	 currentPage:pageNo,
		    	 topic:topic,
		    	 marks:selectedMarks,
		    	 deleteStatus:deleteStatus
		   		},
		   type:"POST",
		   success:function(data){
			 	var data = JSON.parse(data);
			 	if(currentPage == 0 || deleteStatus=="Y"){
			 	totalPages = data.totalPages;
			 	}
				$("#questionListTable tbody").empty();
				$("#pagination").empty();
			 	if(totalPages != 0){
			 	$("#paginationDiv").show();
			 	$("#questionListDiv").show();
			 	$("#questionListNotAvailableAlert").hide();
			 	if(questionType != "3"){
			 	for(var i=0;i<data.questionList.length;i++){
			 		$("#questionListTable tbody").append("<tr><td>"+(i+1)+"</td><td>"+data.questionList[i].que_text+"</td><td>"+data.questionList[i].marks+"</td><td><a class='btn btn-primary editQuestion'"+
			 				"id='"+data.questionList[i].que_id+"'>Edit</a></td><td><a class='btn btn-danger deleteQuestion' id='"+data.questionList[i].que_id+"'>Delete</a></td>");
			 	
			 	}
			 	}else{
			 		for(var i=0;i<data.paragraphQuestion.length;i++){
			 			if(data.paragraphQuestion[i] && data.paragraphQuestion[i]!=null){
				 		$("#questionListTable tbody").append("<tr><td>"+(i+1)+"</td><td>"+data.paragraphQuestion[i].paragraph+"</td><td>"+data.paragraphQuestion[i].marks+"</td><td><a class='btn btn-primary editQuestion'"+
				 				"id='"+data.questionList[i].que_id+"'>Edit</a></td><td><a class='btn btn-danger deleteQuestion' id='"+data.questionList[i].que_id+"'>Delete</a></td>");
			 			}
				 	}
			 	}
			 	if($.inArray( "20", child_mod_access) == "-1"){
			 		$(".editQuestion").hide();
			 		}
			 		if($.inArray( "21", child_mod_access) == "-1"){
			 			$(".deleteQuestion").hide();
			 		}
			 	if((currentPage == 0 && filterSearch == false) || subId != "-1"){
			 	getTopics();
			 	$("#searchQuestionMarks").empty();
			 	var marksarray = [];
			 	var tempData = {};
			 	tempData.id = "-1";
		 		tempData.text = "Select Marks";
		 		marksarray.push(tempData);
			 	 $.each(data.marks,function(key,val){
						var data = {};
						data.id = val;
						data.text = val;
						marksarray.push(data);
					});
	
				    $("#searchQuestionMarks").select2({data:marksarray,placeholder:"Type Question Marks"});
				    $("#filterDiv").show();
				    if(selectedMarks != "-1" && selectedMarks != null){
				    	$("#searchQuestionMarks").select2().val(selectedMarks).change();
				    	$(".appliedFilter").find("#marksFilter").html($("#searchQuestionMarks").select2('data')[0].text+"&nbsp;<i class='glyphicon glyphicon-remove' style='color:red;top:2px'></i>");
						$(".appliedFilter").find("#marksFilter").show();
				    }
				    subId = "";
			 	}   
			 	
				 currentPage = data.currentPage;
			 	$("#pagination").append("<li><a class='paginationStart'>&laquo;</a></li>");
			 	for(var i=0 ;i<totalPages;i++){
			 		if((i+1) == currentPage){
			 		$("#pagination").append("<li class='active'><a>"+(i+1)+"</a></li>");
			 		}else{
			 			$("#pagination").append("<li><a class='paginationPage'>"+(i+1)+"</a></li>");
			 		}			 	
			 	}
			 	$("#pagination").append("<li><a class='paginationEnd'>&raquo;</a></li>");
			 	
			 	}else{
			 		$("#paginationDiv").hide();
			 		$("#questionListDiv").hide();
			 		$("#questionListNotAvailableAlert").show();
			 	} 	
			 	deleteStatus = "";
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	}
}

function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	
	var uri = "rest/customuserservice/getSubjectOfDivision/"+division;
	var handlers = {};
	handlers.success = function(data){
		$("#searchQuestionSubject").removeAttr('disabled');
		   $("#searchQuestionSubject").empty();
		   var subjectArray = [];
		 	if(data.length > 0){
		 		var tempData = {};
		 		tempData.id = "-1";
		 		tempData.text = "Select Subject";
		 		subjectArray.push(tempData);
		 	 $.each(data,function(key,val){
					var data = {};
					data.id = val.subjectId;
					data.text = val.subjectName;
					subjectArray.push(data);
				});
		 	 
			    $("#searchQuestionSubject").select2({data:subjectArray,placeholder:"Type Subject Name"});
		 	 }else{
		 		 $("#searchQuestionSubject").select2({data:"",placeholder:"Subject Not Found"});
		 	 }
			    //displaySubjectDropDown(data);
	}
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
	rest.get(uri,handlers);
	}
	this.getSubjectsInDivision = getSubjectsInDivision;
	}

var searchQuestionBtnClick  = function(){
	topic="-1";
	selectedMarks="-1";
	currentPage = 0;
	filterSearch = false;
	$("#filterDiv").hide();
	$(".appliedFilter").hide();
	division = $("#searchQuestionDivision").val();
	subject = $("#searchQuestionSubject").val();
	questionType = $("#searchQuestionType").val();
	searchQuestion(currentPage);
}
</script>
</head>
<body>
	<jsp:include page="QuestionBankHeader.jsp" >
		<jsp:param value="active" name="customeUserSearchQuestion"/>
	</jsp:include>
	<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<form method="post" action="<c:out value="${forwardAction}" ></c:out>" id="commonSelectForm">
	<div class="well">
		
		<div align="center" style="font-size: larger;margin-bottom: 15px"><u><c:out value="${lable}"></c:out></u></div>
		<input type="hidden" id="quesstatus" name="quesstatus" value='<c:out value="${quesstatus}"></c:out>'>
		<input type="hidden" id="division" name="division" value='<c:out value="${division}"></c:out>'>
 		<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}"></c:out>'>
 		<input type="hidden" id="topicID" name="topicID" value='<c:out value="${topicID}"></c:out>'>
 		<input type="hidden" id="currentPage" name="currentPage" value='0'>
		<div class="row">
			<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
				 
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<select name="division" id="searchQuestionDivision" class="form-control" width="100px">
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="searchQuestionDivisionError" class="validation-message"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="searchQuestionSubject" class="form-control" width="100px">
					<option value="-1">Select Subject</option>
					<c:forEach items="${requestScope.subjects}" var="subject">
						<option value="<c:out value="${subject.subjectId}"></c:out>"><c:out value="${subject.subjectName}"></c:out></option>
					</c:forEach>
				</select>
				<span id="searchQuestionSubjectError" class="validation-message"></span>
			</div>
			<div class="col-md-3 questionTypeDropDown">
				<select name=questionType id="searchQuestionType" class="form-control" width="100px">
					<option value="-1">Select Question Type</option>
					<option value="1">Subjective</option>
					<option value="2">Objective</option>
					<option value="3">Paragraph</option>
				</select>
				<span id="searchQuestionTypeError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<a class="btn btn-primary" id="searchQuestion">Search</a>
			</div>
		</div>
		
	</div>
	</form>
	<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
	<div class="container" id="filterDiv" style="display: none;">
		<div class="row">
			<div class="col-md-3 topicDropDown">
				<select name="topic" id="searchQuestionTopic" class="form-control" width="100px">
					<option value="-1">Select Topic</option>
					<c:forEach items="${requestScope.topics}" var="topic">
						<option value="<c:out value="${topic.topic_id}"></c:out>"><c:out value="${topic.topic_name}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-3 marksDropDown">
				<select name="marks" id="searchQuestionMarks" class="form-control" style="width:100%">
					<option value="-1">Select Marks</option>
				</select>
			</div>
			<div class="col-md-1" style="border-right:groove; ">
			<a class="btn btn-primary" id="fliter">Filter</a>
			</div>
			<div class="col-md-3 appliedFilter">
				Topic:&nbsp;<button type="button" class="btn btn-default" id="topicFilter" style="display: none"></button>
			</div>
			<div class="col-md-2 appliedFilter">	
				Marks:&nbsp;<button type="button" class="btn btn-default" id="marksFilter" style="display: none"></button>
			</div>
		</div>
	
	</div>
	<div id="questionListDiv" style="display: none">
		<table id="questionListTable" class="table table-striped">
			<thead>
				<tr>
					<th>Sr No.</th>
					<th>Question</th>
					<th>Marks</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	
	</div>
	<div id="paginationDiv" style="display: none">
		<ul class="pagination" id="pagination">
		</ul>
	</div>
	
	<div id="questionListNotAvailableAlert" style="display: none" class="alert alert-success">
		Questions not available for selected criteria!
	</div>
	
	<div class="modal fade" id="DeleteConfirmModal" tabindex="-1" role="dialog" 
	aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">�
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Question Delete
            </h4>
         </div>
         <div class="modal-body" id="DeleteConfirmBody">
           
         </div>
         <div class="modal-footer">
	        <button type="button" class="btn btn-default"  data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="QuestionDeleteConfirm">Ok</button>
      	</div>
         </div>
   </div>
</div>

<form action="editquestion" id="editQuestionForm">
		<input type="hidden" id="division" name="division" value='<c:out value="${division}"></c:out>'>
 		<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}"></c:out>'>
 		<input type="hidden" id="topicID" name="topicID" value='<c:out value="${topicID}"></c:out>'>
 		<input type="hidden" id="currentPage" name="currentPage" value='<c:out value="${currentPage}"></c:out>'>
 		<input type="hidden" id="totalPages" name="totalPages" value='<c:out value="${totalPages}"></c:out>'>
 		<input type="hidden" id="questionNumber" name="questionNumber" value='<c:out value="${questionNumber}"></c:out>'>
 		<input type="hidden" id="questiontype" name="questiontype" value='<c:out value="${questiontype}"></c:out>'>
 		<input type="hidden" id="actionname" name="actionname" value='<c:out value="${actionname}"></c:out>'>
 		<input type="hidden" id="preSelectedMarks" name="preSelectedMarks" value='<c:out value="${preSelectedMarks}"></c:out>'>
</form>
<form action="customeUserEditquestionnew" id="editQuestionNewForm" method="post">
		<input type="hidden" id="questionNumber" name="questionNumber" value='<c:out value="${questionNumber}"></c:out>'>
		<input type="hidden" id="division" name="division" value='<c:out value="${division}"></c:out>'>
 		<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}"></c:out>'>
 		<input type="hidden" id="topicID" name="topicID" value='<c:out value="${topicID}"></c:out>'>
 		<input type="hidden" id="questiontype" name="questiontype" value='<c:out value="${questiontype}"></c:out>'>
</form>
</body>
</html>