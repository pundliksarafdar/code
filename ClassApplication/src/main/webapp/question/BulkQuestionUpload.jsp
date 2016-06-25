<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
$("#classownerUploadexamDivisionName").on("change",function(e){
	if($("#classownerUploadexamSubjectNameSelect").val()!= null ){
		$("#classownerUploadexamSubjectNameSelect").select2().val("-1").change();
		$("#classownerUploadexamSubjectNameSelect").find('option:gt(0)').remove();
		}
		if($("#classownerUploadQuestionTopicSelect").val()!= null ){
		$("#classownerUploadQuestionTopicSelect").select2().val("-1").change();
		$("#classownerUploadQuestionTopicSelect").find('option:gt(0)').remove();
		}
	if($(this).val()!=-1){
		getSubjectsInDivision($(this).val());
	}else{
		$("#uploadQuestionPaperBtn").prop("disabled",true); 
	}
});

$("#classownerUploadexamSubjectNameSelect").change(function(){
	var divisionID = $("#classownerUploadexamDivisionName").val();
	var subjectID = $("#classownerUploadexamSubjectNameSelect").val();
	var quesType = $("#classownerQuestionTypeSelect").val();
	if($("#classownerUploadQuestionTopicSelect").val()!= null ){
		$("#classownerUploadQuestionTopicSelect").select2().val("-1").change();
		$("#classownerUploadQuestionTopicSelect").find('option:gt(0)').remove();
		}
	if(subjectID!="-1" && quesType!="-1"){
	 	$("#uploadQuestionPaperBtn").removeAttr('disabled');
	 	$("#uploadQuestionPaperBtn").empty();
	}else{
		$("#uploadQuestionPaperBtn").prop("disabled",true);
	}
	if($(this).val()!="-1"){
$.ajax({
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "getDivisionsTopics",
	    	 divisionID: divisionID,
	    	 subID:subjectID
	   		},
	   type:"POST",
	   success:function(data){
		   $("#classownerUploadQuestionTopicSelect").removeAttr('disabled');
		   $("#classownerUploadQuestionTopicSelect").empty();
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
		 	 
			    $("#classownerUploadQuestionTopicSelect").select2({data:subjectArray,placeholder:"Type Topic Name"});
		 	 }else{
		 		 $("#classownerUploadQuestionTopicSelect").select2({data:"",placeholder:"Topic Not Found"});
		 	 }
			    //displaySubjectDropDown(data);
	   },
		error:function(){
	   		modal.launchAlert("Error","Error");
	   	}
	   });
	}else{
		/* $("#classownerUploadQuestionTopicSelect").prop("disabled",true); */
	}
});
});

function getSubjectsInDivision(division){
	
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getSubjectOfDivision",
		    	 divisionId: division
		   		},
		   type:"POST",
		   success:function(data){
			   $("#classownerUploadexamSubjectNameSelect").removeAttr('disabled');
			   $("#classownerUploadexamSubjectNameSelect").empty();
			   var subjectArray = [];
			 	var data = JSON.parse(data);
			 	if(data.status == "success"){
			 		var tempData = {};
			 		tempData.id = "-1";
			 		tempData.text = "Select Subject";
			 		subjectArray.push(tempData);
			 	 $.each(data.subjectJson,function(key,val){
						var data = {};
						data.id = val.subjectId;
						data.text = val.subjectName;
						subjectArray.push(data);
					});
			 	 
				    $("#classownerUploadexamSubjectNameSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
			 	 }else{
			 		 $("#classownerUploadexamSubjectNameSelect").select2({data:"",placeholder:"Subject Not Found"});
			 	 }
				    //displaySubjectDropDown(data);
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	}

</script>
</head>
<body>
<jsp:include page="../ClassOwner/QuestionBankHeader.jsp" >
		<jsp:param value="active" name="bulkQuestionupload"/>
</jsp:include>
<form method="post" action="<c:out value="${forwardAction}" ></c:out>" id="commonSelectForm">
	<div class="container bs-callout" style="margin-bottom: 5px;background-color: #eee">
		
		<div align="center" style="font-size: larger;margin-bottom: 15px"><u><c:out value="${lable}"></c:out></u></div>
		<div class="row">
			<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
				 
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<select name="division" id="classownerUploadexamDivisionName" class="form-control" width="100px">
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="divisionError" class="validation-message"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="classownerUploadexamSubjectNameSelect" class="form-control" width="100px">
					<option value="-1">Select Subject</option>
					<c:forEach items="${requestScope.subjects}" var="subject">
						<option value="<c:out value="${subject.subjectId}"></c:out>"><c:out value="${subject.subjectName}"></c:out></option>
					</c:forEach>
				</select>
				<span id="subjectError" class="validation-message"></span>
			</div>
			<div class="col-md-3 topicDropDown">
				<select name="subject" id="classownerUploadQuestionTopicSelect" class="form-control" width="100px" >
					<option value="-1">Select Topic</option>
					<c:forEach items="${requestScope.topics}" var="topic">
						<option value="<c:out value="${topic.topic_id}"></c:out>"><c:out value="${topic.topic_name}"></c:out></option>
					</c:forEach>
				</select>
				<span id="topicError" class="validation-message"></span>
			</div>
			<div class="col-md-3 questionTypeDropDown">
				<select name="subject" id="classownerQuestionTypeSelect" class="form-control" width="100px">
					<option value="-1">Select Question Type</option>
					<option value="1">Subjective</option>
					<option value="2">Objective</option>
					<option value="3">Paragraph</option>
				</select>
			</div>
		</div>
		<div class="row">	
			<div class="col-md-3">
				<a href="./SampleFiles/SubjectiveType_Sample.xls" class="btn" role="button">Sample Subjective Questions Excel</a>
			</div>
			<div class="col-md-3">
				<a href="./SampleFiles/MCQType_Sample.xls" class="btn" role="button">Sample Objective Questions Excel</a>
			</div>		
			<div class="col-md-3" id="browseExcelDiv">
			<span class="btn fileinput-button">
							<i class="glyphicon glyphicon-folder-open"></i> 
							<span>Browse Your Question Paper Excel</span>
							<input type="file" id="excelUploadBrowseID" class="excelUpload">							
						</span>
						<span class="error" id="browseExcelErrorSpan">
						</span>
			</div>	
			<div class="col-md-3">
				<input type="button" id="uploadQuestionPaperBtn" value="Upload Excel" disabled/>
			</div>	
 		</div>
		<div class="row">
			<div class="col-md-4" ></div>
			<div class="col-md-6 control-label" id="countDiv"></div>
		</div>

		<div class="row">
			<div id="errorMSGDiv"></div>
		</div> 		
	</div>
	</form>
</body>
</html>