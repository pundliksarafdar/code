<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
		<style>
		.select2-container .select2-selection--single{
height: 34px;
}

.select2-container .select2-selection--single .select2-selection__rendered{
padding-top: 2px;
}
		</style>
	<body>
	<script>
	$(document).ready(function(){
		$("#subjectiveQuestion").addExpresssion();
		if($("#quesstatus").val() == "success"){
			$.notify({message: "Question successfuly added"},{type: 'success'});
			$("#classownerUploadexamSubjectNameSelect").prop("disabled",false);
			$("#classownerUploadQuestionTopicSelect").prop("disabled",false);
			if(!$("#commonSelectForm").find("#topicID").val() == "-1" && !$("#commonSelectForm").find("#topicID").val() == "0"){
			$("#classownerUploadQuestionTopicSelect").select2().val($("#commonSelectForm").find("#topicID").val()).change();;
			}else if($("#commonSelectForm").find("#topicID").val() == "0"){
				$("#classownerUploadQuestionTopicSelect").empty();
				 $("#classownerUploadQuestionTopicSelect").select2({data:"",placeholder:"Topic Not Found"});
			}else {
				$("#classownerUploadQuestionTopicSelect").select2();
			}
			$("#classownerUploadexamSubjectNameSelect").select2().val($("#commonSelectForm").find("#subject").val()).change();;
			$("#classownerUploadexamDivisionName").select2().val($("#commonSelectForm").find("#division").val()).change();
		}
		$("#classownerUploadexamDivisionName").select2();
		$("#classownerQuestionTypeSelect").change(function(){
			var quesType = $("#classownerQuestionTypeSelect").val();			
			var subId=$("#classownerUploadexamSubjectNameSelect").val();
			
			if(subId!="-1" && quesType!="-1"){
			 	$("#uploadQuestionPaperBtn").removeAttr('disabled');
			 	$("#uploadQuestionPaperBtn").empty();
			}else{
				$("#uploadQuestionPaperBtn").prop("disabled",true);
			}

			$("#MCQDiv").hide();
			$("#subjectiveDiv").hide();
			$("#paragraphDiv").hide();
			if(quesType == "1"){
				$("#subjectiveDiv").show();
			}else if(quesType == "2"){
				$("#MCQDiv").show();
			}else if(quesType == "3"){
				$("#paragraphDiv").show();
			}
		});
		$("#uploadQuestionPaperBtn").on("click",function(e){
			var handler = {};
			handler.success = function(e){				
				var uri = "rest/classownerservice/addExcelFile/"+e.fileid;
				var handlers = {};
				handlers.success = function(e){
					var quesType = $("#classownerQuestionTypeSelect").val();
					var divisionID = $("#classownerUploadexamDivisionName").val();
					var subjectID = $("#classownerUploadexamSubjectNameSelect").val();
					var QuestionExcelUploadBean= {};
					QuestionExcelUploadBean.sub_id=subjectID;
					QuestionExcelUploadBean.div_id=divisionID;
					QuestionExcelUploadBean.ques_type=quesType;
					QuestionExcelUploadBean.fileName=e.fileid;
					var questionExcelUploadBean = JSON.stringify(QuestionExcelUploadBean);
					var handlersSuccess = {};
					rest.post("rest/files/upload/xls/", handlersSuccess,
							questionExcelUploadBean, false);
					handlersSuccess.success = function(successResp){
						console.log("Success",successResp);
					}
					console.log("Success",e);
					}
				handlers.error = function(e){console.log("Error",e)}
				rest.post(uri,handlers);
			}
			handler.error = function(){};
			
			var submitDataFile = $(".excelUpload")[0];
			var file=document.getElementById("excelUploadBrowseID").value;
			var flagUpload=true;
			if(file==""){				
				$("#browseExcelErrorSpan").html("Please select the file!");
				flagUpload=false;
			}else{
				$("#browseExcelErrorSpan").html("");
				flagUpload=true;
			}
			if(flagUpload==true){
				rest.uploadExcelFile(submitDataFile ,handler,false);
			}
						
		});
		$("#classownerUploadexamDivisionName").on("change",function(e){
			//$(SUBJECT_DROPDOWN).hide();
			//$(BATCH_DROPDOWN).hide();
			//$(ADD_BUTTON).hide();
			if($(this).val()!=-1){
				var uploadExam = new UploadExam();
				uploadExam.getSubjectsInDivision($(this).val());
			}else{
				$("#classownerUploadexamSubjectNameSelect").prop("disabled",true);
				$("#classownerUploadQuestionTopicSelect").prop("disabled",true);
				$("#classownerUploadexamAddExam").prop("disabled",true);
				$("#uploadQuestionPaperBtn").prop("disabled",true);
			}
		});
		
		$("#classownerUploadQuestionTopicSelect").change(function(){
			var topicId=$("#classownerUploadQuestionTopicSelect").val();
			var quesType = $("#classownerQuestionTypeSelect").val();
			if(topicId!="-1" && quesType!="-1"){
			 	$("#uploadQuestionPaperBtn").removeAttr('disabled');
			 	$("#uploadQuestionPaperBtn").empty();
			}else{
				$("#uploadQuestionPaperBtn").prop("disabled",true);
 			}
		});
		
		$("#classownerUploadexamSubjectNameSelect").change(function(){
			var divisionID = $("#classownerUploadexamDivisionName").val();
			var subjectID = $("#classownerUploadexamSubjectNameSelect").val();
			var quesType = $("#classownerQuestionTypeSelect").val();
			
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
				$("#classownerUploadQuestionTopicSelect").prop("disabled",true);
			}
		});
		
		
	});
	
	function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	
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
	this.getSubjectsInDivision = getSubjectsInDivision;
	}
	
	function readURL(input,id) {
		$("#"+id).empty();
        for(var index=0;index<input.files.length;index++){
		if (input.files && input.files[index]) {
            var reader = new FileReader();

            reader.onload = function (e) {
				var images = "<img width='200px' height='200px' style='padding:5px;' src='"+e.target.result+"'/>";
				$("#"+id).append(images);
			};

            reader.readAsDataURL(input.files[index]);
        }
		}
    }
	
	function appendCount(input,id){
		var imageCount = $("<input/>",
		{
			name:"optionImageCount",
			value:input.files.length,
			type:'hidden'
		});
		$("#"+id).append(imageCount);
	}
	
	function validateOption(){
		var options = $('input.uploadOptionImageModalFileBtn');
		var hasError = false;
		$.each(options,function(index,fileChooser){
			$(fileChooser).parents(".form-group").removeClass("has-error");
			$(fileChooser).parents(".form-group").find(".validation-message").remove();
			if($(fileChooser).parents(".form-group").find("textarea").val().trim().length==0 && $(fileChooser).parents(".form-group").find("#optionImageCount").val() == 0){
					$(fileChooser).parents(".form-group").addClass("has-error");
					$(fileChooser).parents(".form-group").find(".col-sm-10").prepend("<div class='validation-message'>Error Please add either option text or image </div>");
					hasError = true;
					
			}
		});
		return hasError;
	}
	
	</script>
	<jsp:include page="../ClassOwner/QuestionBankHeader.jsp" >
		<jsp:param value="active" name="addquestion"/>
	</jsp:include>
	<form method="post" action="<c:out value="${forwardAction}" ></c:out>" id="commonSelectForm">
	<div class="container bs-callout" style="margin-bottom: 5px;background-color: #eee">
		
		<div align="center" style="font-size: larger;margin-bottom: 15px"><u><c:out value="${lable}"></c:out></u></div>
		<input type="hidden" id="quesstatus" name="quesstatus" value='<c:out value="${quesstatus}"></c:out>'>
		<input type="hidden" id="division" name="division" value='<c:out value="${division}"></c:out>'>
 		<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}"></c:out>'>
 		<input type="hidden" id="topicID" name="topicID" value='<c:out value="${topicID}"></c:out>'>
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
				<span id="classownerUploadexamDivisionNameError" class="validation-message"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="classownerUploadexamSubjectNameSelect" class="form-control" width="100px" disabled="disabled">
					<option value="-1">Select Subject</option>
					<c:forEach items="${requestScope.subjects}" var="subject">
						<option value="<c:out value="${subject.subjectId}"></c:out>"><c:out value="${subject.subjectName}"></c:out></option>
					</c:forEach>
				</select>
				<span id="classownerUploadexamSubjectNameSelectError" class="validation-message"></span>
			</div>
			<div class="col-md-3 topicDropDown">
				<select name="subject" id="classownerUploadQuestionTopicSelect" class="form-control" width="100px" disabled="disabled">
					<option value="-1">Select Topic</option>
					<c:forEach items="${requestScope.topics}" var="topic">
						<option value="<c:out value="${topic.topic_id}"></c:out>"><c:out value="${topic.topic_name}"></c:out></option>
					</c:forEach>
				</select>
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
				<a href="./question/SampleFiles/SubjectiveType_Sample.xls" class="btn" role="button">Sample Subjective Questions Excel</a>
			</div>
			<div class="col-md-3">
				<a href="./question/SampleFiles/MCQType_Sample.xls" class="btn" role="button">Sample Objective Questions Excel</a>
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
				<input type="button" id="uploadQuestionPaperBtn" value="Upload" disabled/>
			</div>	
 		</div>
	</div>
	</form>
	<jsp:include page="addSubjectiveQuestion.jsp"></jsp:include>
	<jsp:include page="addObjectiveQuestion.jsp"></jsp:include>
	<jsp:include page="addParagraphQuestion.jsp"></jsp:include>
	</body>
	</html>