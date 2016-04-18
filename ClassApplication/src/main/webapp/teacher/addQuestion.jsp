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
		$("#instituteSelect").change(function(){
			var inst_id = $(this).val();
			var handler = {};
			handler.success = function(e){
			console.log("Success",e);
			$("#divisionSelect").empty();
			var divisionArray = [];
			var tempData = {};
	 		tempData.id = "-1";
	 		tempData.text = "Select Class";
	 		divisionArray.push(tempData);
	 	 $.each(e.divisionList,function(key,val){
				var data = {};
				data.id = val.divId;
				data.text = val.divisionName+" "+val.stream;
				divisionArray.push(data);
			});
	 	 
		    $("#divisionSelect").select2({data:divisionArray,placeholder:"Type Topic Name"});
			}
			handler.error = function(e){console.log("Error",e)};
			rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);

		});
		$("#subjectiveQuestion").addExpresssion();
		if($("#quesstatus").val() == "success"){
			$.notify({message: "Question successfuly added"},{type: 'success'});
			$("#subjectSelect").prop("disabled",false);
			$("#topicSelect").prop("disabled",false);
			if(!$("#commonSelectForm").find("#topicID").val() == "-1" && !$("#commonSelectForm").find("#topicID").val() == "0"){
			$("#topicSelect").select2().val($("#commonSelectForm").find("#topicID").val()).change();;
			}else if($("#commonSelectForm").find("#topicID").val() == "0"){
				$("#topicSelect").empty();
				 $("#topicSelect").select2({data:"",placeholder:"Topic Not Found"});
			}else {
				$("#topicSelect").select2();
			}
			$("#subjectSelect").select2().val($("#commonSelectForm").find("#subject").val()).change();;
			$("#divisionSelect").select2().val($("#commonSelectForm").find("#division").val()).change();
		}
		$("#divisionSelect").select2();
		$("#classownerQuestionTypeSelect").change(function(){
			var quesType = $("#classownerQuestionTypeSelect").val();
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
		$("#divisionSelect").on("change",function(e){
			//$(SUBJECT_DROPDOWN).hide();
			//$(BATCH_DROPDOWN).hide();
			//$(ADD_BUTTON).hide();
			if($(this).val()!=-1){
				var uploadExam = new UploadExam();
				uploadExam.getSubjectsInDivision($(this).val());
			}else{
				$("#subjectSelect").prop("disabled",true);
				$("#topicSelect").prop("disabled",true);
				$("#classownerUploadexamAddExam").prop("disabled",true);
			}
		});
		
		$("#subjectSelect").change(function(){
			var divisionID = $("#divisionSelect").val();
			var subjectID = $("#subjectSelect").val();
			var inst_id = $("#instituteSelect").val();
			var handler = {};
			handler.success = function(e){console.log("Success",e);
			$("#topicSelect").empty();
			var topicArray = [];
			var tempData = {};
	 		tempData.id = "-1";
	 		tempData.text = "Select Topic";
	 		topicArray.push(tempData);
	 	 $.each(e,function(key,val){
				var data = {};
				data.id = val.topic_id;
				data.text = val.topic_name;
				topicArray.push(data);
			});
	 	 
		    $("#topicSelect").select2({data:topicArray,placeholder:"Type Topic Name"});
		    $("#topicSelect").prop("disabled",false);
			}
			handler.error = function(e){console.log("Error",e)}
			rest.get("rest/teacher/getDivisionsTopics/"+inst_id+"/"+divisionID+"/"+subjectID,handler);
		});
		
		
	});
	
	function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	
		var inst_id = $("#instituteSelect").val();
		var handler = {};
		handler.success = function(e){console.log("Success",e);
		var subjectArray = [];
		var tempData = {};
 		tempData.id = "-1";
 		tempData.text = "Select Subject";
 		subjectArray.push(tempData);
 	 $.each(e,function(key,val){
			var data = {};
			data.id = val.subjectId;
			data.text = val.subjectName;
			subjectArray.push(data);
		});
 	 
	    $("#subjectSelect").select2({data:subjectArray,placeholder:"Type Topic Name"});
	    $("#subjectSelect").prop("disabled",false);
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getSubjectOfDivision/"+inst_id+"/"+division,handler);
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
				<select name="instituteSelect" id="instituteSelect" class="form-control" width="100px">
					<option value="-1">Select Institute</option>
					<c:forEach items="${requestScope.registerBeanList}" var="institute">
						<option value="<c:out value="${institute.regId}"></c:out>"><c:out value="${institute.className}"></c:out></option>
					</c:forEach>							
				</select>
			</div>
			<div class="col-md-3">
				<select name="division" id="divisionSelect" class="form-control" width="100px">
					<option value="-1">Select Class</option>							
				</select>
				<span id="divisionSelectError" class="validation-message"></span>
			</div>
			<div class="col-md-2 subjectDropDown">
				<select name="subject" id="subjectSelect" class="form-control" width="100px" disabled="disabled">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectSelectError" class="validation-message"></span>
			</div>
			<div class="col-md-2 topicDropDown">
				<select name="topic" id="topicSelect" class="form-control" width="100px" disabled="disabled">
					<option value="-1">Select Topic</option>
				</select>
			</div>
			<div class="col-md-2 questionTypeDropDown">
				<select name="subject" id="classownerQuestionTypeSelect" class="form-control" width="100px">
					<option value="-1">Select Question Type</option>
					<option value="1">Subjective</option>
					<option value="2">Objective</option>
					<option value="3">Paragraph</option>
				</select>
			</div>
		</div>
		<div class="row">
			
		</div>
	</div>
	</form>
	<jsp:include page="addSubjectiveQuestion.jsp"></jsp:include>
	<jsp:include page="addObjectiveQuestion.jsp"></jsp:include>
	<jsp:include page="addParagraphQuestion.jsp"></jsp:include>
	</body>
	</html>