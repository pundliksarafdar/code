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
		
		var questionType= $("#questiontype").val();
		$("#MCQDiv").hide();
		$("#subjectiveDiv").hide();
		$("#paragraphDiv").hide();
		if(questionType == "1"){
			$("#subjectiveDiv").show();
		}else if(questionType == "2"){
			$("#MCQDiv").show();
		}else if(questionType == "3"){
			$("#paragraphDiv").show();
		}
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
			}
		});
		
		$("#classownerUploadexamSubjectNameSelect").change(function(){
			var divisionID = $("#classownerUploadexamDivisionName").val();
			var subjectID = $("#classownerUploadexamSubjectNameSelect").val();
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
		
		$(".CancleUploading").click(function(){
			$("#editQuestionForm").submit();
		});
		
		var indexOption = 0;
		var currentOptionUploadImageBtn;
		indexOption=$("#indexOption").val();
		$(".addOptionUploadExam").on('click',function(){
			var imgBtn = '<span class="btn btn-success fileinput-button">'+
				'<i class="glyphicon glyphicon-folder-open"></i> '+
						'<span>Add images </span>'+
				'<input type="file" class="uploadOptionImageModalFileBtn" multiple name="fileUpload">'+
			'</span>';
			var submitquestionsuploadoptionsText = 
				"<div class='form-group'>"+
					"<input type='hidden' id='optionImageCount' name='optionImageCount' value='0'>"+
					"<label class='col-sm-2 control-label'>&nbsp;</label>"+
					"<div class='col-sm-10'>"+
						"<div class='input-group'>"+
							"<span class='input-group-addon'>"+
							"<input type='checkbox' name='answersOptionCheckBox' value='"+indexOption+"' id='answersOptionCheckBox"+indexOption+"'>"+
							"</span>"+
							"<textarea class='form-control' name='answersOptionText'></textarea>"+
							"<span class='input-group-addon'>"+
							"<i class='glyphicon glyphicon-trash removeOption' id='removeOption_"+indexOption+"'></i>"+
							"</span>"+
						"</div>"+
						"<div class='row images_row_with_btn'></div>"+
						"<div class='addMorebuttonImageWrapper'>"+
							"<div id='optionImagesDiv'></div>"+
						imgBtn+
						"</div>"+
				"</div>";
				
			$("#submitquestionsuploadoptions").append(submitquestionsuploadoptionsText);
			indexOption++;
		});
		
		$('form[action="editquestion"]').on("click",".removeOption",function(){
			var removeid=$(this).prop("id").split("_")[1];
			if(removeid < indexOption){
			while(removeid <= indexOption){
				var answersOptionCheckBoxid="#answersOptionCheckBox"+(parseInt(removeid)+1);
			$(answersOptionCheckBoxid).val(removeid);	
			$(answersOptionCheckBoxid).prop("id","answersOptionCheckBox"+(parseInt(removeid)));
			var newremoveID="#removeOption_"+(parseInt(removeid)+1);
			$(newremoveID).prop("id","removeOption_"+(parseInt(removeid)));
			removeid++;
			}	
			}
			$(this).parents(".form-group").remove();
			indexOption--;
		});
		
		$('form[action="editquestion"]').on("click",".uploadAnswerImage",function(){
				currentOptionUploadImageBtn = $(this);
				$("#uploadOptionImageModal").modal("show");
				$("#uploadOptionImageModalAnswerImages").empty();
				$("#uploadOptionImageModal form")[0].reset();
				var valueText = currentOptionUploadImageBtn.parents(".form-group").find("textarea").val();
				$("#uploadOptionImageModal").find("p#optionText").text(valueText);
		});
		
		
		$("input.startuplodingexamSave").on('click',function(){
			var flag = false;
			$(".validation-message").empty();
			var quesType = $("#questiontype").val();
			if($("#classownerUploadexamDivisionName").val() == "-1"){
				$("#classownerUploadexamDivisionNameError").html("Select Class!");
				flag = true;
			}
			if($("#classownerUploadexamSubjectNameSelect").val() == "-1"){
				$("#classownerUploadexamSubjectNameSelectError").html("Select Subject!");
				flag = true;
			}
			if(quesType == "1"){
				if($("#questionmarks").val() == "0" || $("#questionmarks").val()==""){
					$("#questionMarksError").html("Enter Marks!");
					flag = true;
				}
				if($("#subjectiveQuestion").val() == ""){
					$("#subjectiveQuestionError").html("Enter Question!");
					flag = true;
				}
				if(flag==false){
				$("#subjectiveDiv").find('form[action="editquestion"]').submit();
				}
			}else if(quesType == "2"){
			var inValid = $('form[action="editquestion"]').validateCustome();
			if(!inValid){
				var invalidOption = validateOption();
				if(!invalidOption){
					$("#MCQDiv").find("#division").val($("#classownerUploadexamDivisionName").val());
					$("#MCQDiv").find("#subject").val($("#classownerUploadexamSubjectNameSelect").val());
					$("#MCQDiv").find("#topicID").val($("#classownerUploadQuestionTopicSelect").val());
					$("#MCQDiv").find("#actionname").val("submitquestions");
					$("#MCQDiv").find("#questiontype").val($("#classownerQuestionTypeSelect").val());
					$("#MCQDiv").find('form[action="editquestion"]').submit();
				}
			}
			}else if(quesType == "3"){
				if($("#paragraphDiv").find("#paragraphText").val().trim()==""){
					$("#paragraphTextError").html("Enter Paragraph!");
					flag = true;		
				}
				if($("#paragraphDiv").find("#questionmarks").val().trim()==""){
					$("#paraTotalMarksError").html("Enter Marks!");
					flag = true;
				}
				var paraQuestions =document.getElementsByName('paraQuestions');
				if(paraQuestions.length>0){
				for(var i=0;i<paraQuestions.length;i++){
					if(paraQuestions[i].value == "")
						{
						$("#paraQuestionsError").html("Questions Or Marks Cannot be Blank!");
						flag = true;
						break;
						}
				}
				var paraPerQuestionMarks = document.getElementsByName('paraPerQuestionMarks');
				for(var i=0;i<paraPerQuestionMarks.length;i++){
					if(paraPerQuestionMarks[i].value == "")
						{
						$("#paraQuestionsError").html("Questions And Marks Cannot be Blank!");
						flag = true;
						break;
						}
				}
				}else{
					$("#paraQuestionsError").html("Add Questions!");
					flag = true;
				}
				if(flag==false){
				$("#paragraphDiv").find('form[action="editquestion"]').submit();
				}
			}
		});
		
		/* $("#CancleUploading").on("click",function(){
			//$("#editquestion").prop("action","cancleuploading");
			$("#actionname").val("cancleuploading");
			$("#editquestion").submit();
		}); */
		$(".paragraphQuestionDiv").on("click",".removeQuestion",function(){
			$(this).closest(".paragraphQuestionDivText").remove();
		});
		$(".addParaQuestions").click(function(){
			$(".paragraphQuestionDiv").append("<div class='row paragraphQuestionDivText' style='width:100%'><div class='input-group col-md-10' style='margin-top:1%'><textarea class='form-control' id='paraQuestions' name='paraQuestions'></textarea><span class='input-group-addon'>"+
			"<i class='glyphicon glyphicon-trash removeQuestion'></i>"+
			"</span></div><div class='col-md-2'><input type='number' name='paraPerQuestionMarks' placeholder='Marks' class='form-control' style='margin-top:15%'></div> </div>");
		});
		
		function onAddMoreOptionImage(){
			var input = this;
			var hiddenVariable = $(input).parents(".form-group").find("input#optionImageCount");
			//if imagecount variable is not present then create a variable
			if(!hiddenVariable || hiddenVariable.length == 0){
				$(input).parents(".form-group").append("<input type='hidden' id='optionImageCount' name='optionImageCount' value='0' />");
				hiddenVariable = $(input).parents(".form-group").find("input#optionImageCount");
			}
			var prevHiddenVariable = parseInt(hiddenVariable.val());
			if(isNaN(prevHiddenVariable)){
				prevHiddenVariable = 0;
			}
			hiddenVariable.val(prevHiddenVariable+parseInt(input.files.length));
			if($(input).parents("div.addMorebuttonImageWrapper").find("#optionImagesDiv").find(".row").length == 0){
				$(input).parents("div.addMorebuttonImageWrapper").find("#optionImagesDiv").append("<div class='row'></div>");
			}
			for(var index=0;index<input.files.length;index++){
			if ( input.files && input.files[0] ) {
	        var FR= new FileReader();
	        FR.onload = function(e) {
				var imgTag = "<img src='"+e.target.result+"' width='200px' height='200px' />";
				var hiddenTag = "<input type='hidden' value='"+e.target.result+"' name='optionImagesPrev'/>";
				var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
				var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+imgTag+hiddenTag+"</div>"
				var imageRow = $(input).parents("div.addMorebuttonImageWrapper").find("#optionImagesDiv .row");
				imageRow.append(imageColElement);	
				
				
	        };
				FR.readAsDataURL( input.files[index] );
			}
	    }
		}
		
		function addQuestionImages(){
			var input = this;
			for(var index=0;index<input.files.length;index++){
			if ( input.files && input.files[0] ) {
	        var FR= new FileReader();
	        FR.onload = function(e) {
				var imgTag = "<img src='"+e.target.result+"' width='200px' height='200px'/>";
				var hiddenTag = "<input type='hidden' value='"+e.target.result+"' name='questionImagesStr'/>";
				var closeButton = '<button type="button" class="Question_image_with_btn_remove close" aria-hidden="true">&times;</button>';
				<!-- This element contains the object -->
				var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+imgTag+hiddenTag+"</div>"
				var imageRow = $("#questionImageFileList");
				imageRow.append(imageColElement);	
				
				
	        };
				FR.readAsDataURL( input.files[index] );
			}
		}
		}
		
		function removeQuestionImage(){
			$(this).parent("div").remove();
		}
		
		function removeAnswerImage(){
			var imageCount = $(this).parents(".form-group").find("#optionImageCount").val();
			$(this).parents(".form-group").find("#optionImageCount").val(imageCount-1);
			$(this).parent("div").remove();
		}
		
		$('form[action="editquestion"]').on("change","input[type='file']",setQuestionSize);
			//function for edit option images
		$("body").on("change",".uploadOptionImageModalFileBtn",onAddMoreOptionImage);
		$("#submitquestionsuploadfile").on("change",addQuestionImages);
		$("body").on("click",".Question_image_with_btn_remove",removeQuestionImage);
		$("body").on("click",".answer_image_with_btn_remove",removeAnswerImage);
		
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
	
	function setQuestionSize(){
		var size = 0;
		var allImageFiles = $("input[type='file']");
		$.each(allImageFiles,function(index,file){
			$.each(file.files,function(indexx,fileInner){
				size = size + fileInner.size;	
			});
			
		});
		if(allImageFiles.length != 0){
			$("#startuplodingexamImageFileSize").removeClass("hide").html("Question size is "+(size/(1024*1024)).toFixed(3) +"MB approximately");
		}else{
			$("#startuplodingexamImageFileSize").addClass("hide");
		}
	}
	</script>
	<%-- <form method="post" action="<c:out value="${forwardAction}" ></c:out>" id="commonSelectForm">
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
			
		</div>
	</div>
	</form> --%>
	<input type="hidden" name="questiontype" id="questiontype" value="<c:out value='${questiontype}'></c:out>">
	<div id="subjectiveDiv" style="display: none" class="container">
	<form action="editquestion" method="post" enctype="multipart/form-data" id="editquestion" class="form-horizontal corex-form-container">
 			<input type="hidden" id="division" name="division" value="<c:out value='${division}'></c:out>">
 			<input type="hidden" id="subject" name="subject" value="<c:out value='${subject}'></c:out>">
 			<input type="hidden" id="actionname" name="actionname" value="updateQuestion">
 			<input type="hidden" id="questiontype" name="questiontype" value="<c:out value='${questiontype}'></c:out>">
 			<input type="hidden" id="indexOption" name="indexOption" value="<c:out value="${indexOption}" ></c:out>">
 			<input type="hidden" id="topicID" name="topicID" value="<c:out value='${topicID}'></c:out>">
 			<input type="hidden" id="currentPage" name="currentPage" value='<c:out value="${currentPage}"></c:out>'>
 		<input type="hidden" id="totalPages" name="totalPages" value='<c:out value="${totalPages}"></c:out>'>
 		<input type="hidden" id="questionNumber" name="questionNumber" value='<c:out value="${questionNumber}"></c:out>'>
 		<input type="hidden" id="preSelectedMarks" name="preSelectedMarks" value='<c:out value="${preSelectedMarks}"></c:out>'>
 			<div class="form-group">
 			<label for="examname" class="col-md-2 control-label">Question </label>
 			<div class="col-md-10">
 				<textarea rows="5" cols="100" id="subjectiveQuestion" name="question" class="form-control"><c:out value="${question}"></c:out></textarea>
 				<span class="validation-message" id="subjectiveQuestionError"></span>
 			</div>
 			</div>
 			<div class="form-group">
				<label for="questionmarks" class="col-sm-2 control-label">Marks</label>
				<div class="col-sm-2">
					<input type="number" required class="form-control" id="questionmarks" name="questionmarks" maxlength="5" size="5" style="width: 50%;" min="1" value="<c:out value="${questionmarks}"></c:out>"></input>
					<span class="validation-message" id="questionMarksError"></span>
				</div>
			</div>
 			<div class="form-group">
			<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<input type="button" class="btn btn-default startuplodingexamSave" value="Save"/>
					<input type="button" class="btn btn-default CancleUploading" value="Cancel" id="CancleUploading"/>
				</div>
		</div>
 	</form>
	
	</div>
	<div id="MCQDiv" style="display: none"> 
 <form action="editquestion" method="post" enctype="multipart/form-data" id="editquestion" class="form-horizontal corex-form-container">
 			<input type="hidden" id="division" name="division">
 			<input type="hidden" id="subject" name="subject">
 			<input type="hidden" id="actionname" name="actionname" value="updateQuestion">
 			<input type="hidden" id="questiontype" name="questiontype">
 			<input type="hidden" id="indexOption" name="indexOption" value="<c:out value="${indexOption}" ></c:out>">
 			<input type="hidden" id="topicID" name="topicID">
 			<input type="hidden" id="preSelectedMarks" name="preSelectedMarks" value='<c:out value="${preSelectedMarks}"></c:out>'>
			<div class="form-group">
				<label for="examname" class="col-sm-2 control-label">Question </label>
				<div class="col-sm-10">
					<div class="validation-message hide"></div>
					<textarea class="form-control" id="question" name="question" required><c:out value="${requestScope.questionData.question}"></c:out></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="questionmarks" class="col-sm-2 control-label">Marks</label>
				<div class="col-sm-2">
					<div class="validation-message hide"></div>
					<input type="number" required class="form-control" id="questionmarks" name="questionmarks" maxlength="5" size="5" style="width: 50%;" min="1" value="<c:out value="${requestScope.questionData.marks}"></c:out>"></input>
				</div>
				
				<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-2">
				<span class="btn btn-success fileinput-button">
					<i class="glyphicon glyphicon-folder-open"></i>
					<span>Add images </span>
					<input type="file" id="submitquestionsuploadfile" multiple>
				</span>	
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<div id="questionImageFileList" class="row">
					<c:forEach items="${requestScope.questionData.questionImage}" var="image">
						<div class="col-sm-3">
							<input type="hidden" value='<c:out value="${image }"></c:out>' name="questionImagesStr">
							<button type="button" class="Question_image_with_btn_remove close" aria-hidden="true">&times;</button>
							<img src='<c:out value="${image }"></c:out>' width="200px" height="200px" tyle="padding:5px;"/>
						</div>
					</c:forEach>
					</div>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<a class="btn btn-default addOptionUploadExam">Add Option</a>
				</div>
			</div>
			<div class="form-btn-group" id="submitquestionsuploadoptions" mandatorySelection="1" validationMessage="Please select atleast an answer">
						<div class="validation-message hide"></div>
			<c:forEach items="${requestScope.questionData.options}" var="options" varStatus="counter">
				<div class="form-group">
				<input type="hidden" id="optionImageCount" name="optionImageCount" value='<c:out value="${requestScope.questionData.optionImageCount[counter.index]}"></c:out>'/>
				<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<div class='input-group'>
						<span class='input-group-addon'>
						<c:choose>
						<c:when test="${not empty requestScope.questionData.answers}">
						<c:set var="ansstatus" value="N" scope="page" />
							<c:forEach items="${answerList}" var="ans">
							<c:if test="${ans eq counter.index}">
							<c:set var="ansstatus" value="Y" scope="page" />
							</c:if>
							</c:forEach>
							<c:choose>
								<c:when test="${ansstatus eq 'Y'}">
								<input type="checkbox" name='answersOptionCheckBox' id='answersOptionCheckBox<c:out value="${counter.index}"></c:out>' checked="checked" value='<c:out value="${counter.index}"></c:out>'>
								</c:when>
								<c:otherwise>
								<input type="checkbox" name='answersOptionCheckBox' id='answersOptionCheckBox<c:out value="${counter.index}"></c:out>' value='<c:out value="${counter.index}"></c:out>'>
								</c:otherwise>
							</c:choose>
							
						</c:when>
						<c:otherwise>
						<input type='checkbox' name='answersOptionCheckBox'>
						</c:otherwise>
							</c:choose>
							</span>
						<textarea class='form-control' name='answersOptionText'><c:out value="${options}"></c:out></textarea>
						<span class='input-group-addon'> <i
							class='glyphicon glyphicon-trash removeOption' id='removeOption_<c:out value="${counter.index}"></c:out>'></i>
						</span>
					</div>
					
					<c:if test="${not empty requestScope.optionImageEndCount}">
						<c:set var="lastCountIndex" value="${requestScope.optionImageEndCount[counter.index]}"></c:set>
						<c:choose>
							<c:when test="${counter.index eq 0 }">
								<c:set var="startCountIndex" value="0"></c:set>	
							</c:when>
							<c:otherwise>
								<c:set var="startCountIndex" value="${requestScope.optionImageEndCount[counter.index-1]}"></c:set>
							</c:otherwise>
						</c:choose>
							<div class="row images_row_with_btn">
							<c:choose>
							<c:when test="${lastCountIndex gt 0 }">
							<c:forEach items="${requestScope.questionData.answerImage}" var="optionImage" begin='${startCountIndex }' end="${lastCountIndex-1}" varStatus="optionImageCounter">
								<div class="col-sm-3 image_with_btn">
									<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>
									<img src='<c:out value="${optionImage }"></c:out>' width="200px" style="padding:5px;" id="optionImages<c:out value='${optionImageCounter.index }'></c:out>"/>
									<input type="hidden" value='<c:out value="${optionImage }"></c:out>' name="optionImagesPrev">
								</div>
							</c:forEach>
							</c:when>
							</c:choose>	
							</div>
							
						</c:if>
					<div class="addMorebuttonImageWrapper">	
						<div id='optionImagesDiv'></div>
						<span class="btn btn-success fileinput-button">
							<i class="glyphicon glyphicon-folder-open"></i> 
							<span>Add images </span>
							<input type="file" class="uploadOptionImageModalFileBtn" multiple>
						</span>	
					
					</div>
				
				</div>
				</div>
			</c:forEach>
			
		</div>
		<div id="startuplodingexamImageFileSize" class="alert alert-info hide">
		
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<input type="button" class="btn btn-default startuplodingexamSave" value="Save"/>
					<c:if test="${actionname ne 'submitquestions'}">
					<input type="button" class="btn btn-default CancleUploading" value="Cancel" id="CancleUploading"/>
					</c:if>
				</div>
		</div>
</form>
	
	</div>
	
	<div id="paragraphDiv" style="display: none" class="container">
	<form action="editquestion" method="post" enctype="multipart/form-data" id="editquestion" class="form-horizontal corex-form-container">
 			<input type="hidden" id="division" name="division" value="<c:out value='${division}'></c:out>">
 			<input type="hidden" id="subject" name="subject" value="<c:out value='${subject}'></c:out>">
 			<input type="hidden" id="actionname" name="actionname" value="updateQuestion">
 			<input type="hidden" id="questiontype" name="questiontype" value="<c:out value='${questiontype}'></c:out>">
 			<input type="hidden" id="indexOption" name="indexOption" value="<c:out value="${indexOption}" ></c:out>">
 			<input type="hidden" id="topicID" name="topicID" value="<c:out value='${topicID}'></c:out>">
 			<input type="hidden" id="currentPage" name="currentPage" value='<c:out value="${currentPage}"></c:out>'>
 			<input type="hidden" id="totalPages" name="totalPages" value='<c:out value="${totalPages}"></c:out>'>
 			<input type="hidden" id="questionNumber" name="questionNumber" value='<c:out value="${questionNumber}"></c:out>'>
 			<input type="hidden" id="preSelectedMarks" name="preSelectedMarks" value='<c:out value="${preSelectedMarks}"></c:out>'>
 			<div class="form-group">
 			<label for="examname" class="col-md-2 control-label">Paragraph </label>
 			<div class="col-md-10">
 				<span class="validation-message" id="paragraphTextError"></span>
 				<textarea rows="5" cols="100" id="paragraphText" name="paragraphText" class="form-control"><c:out value="${paragraphText}"></c:out></textarea>
 			</div>
 			</div>
 			<div class="form-group">
				<label for="questionmarks" class="col-sm-2 control-label">Total Marks</label>
				<div class="col-sm-2">
					<span class="validation-message" id="paraTotalMarksError"></span>
					<input type="number" required class="form-control" id="questionmarks" name="questionmarks" maxlength="5" size="5" style="width: 50%;" min="1" value="<c:out value="${questionmarks}"></c:out>"></input>
				</div>
				<div class="col-sm-2">
					<a class="addParaQuestions btn btn-primary">Add Questions</a>
				</div>
			</div>
			<div class="form-group">
				<label for="questionmarks" class="col-sm-2 control-label" style="padding-top:2%">Questions</label>
				<div class="col-sm-10 paragraphQuestionDiv">
					<div class="validation-message" id="paraQuestionsError"></div>
					<c:forEach items="${paraQuestions}" varStatus="loop">
							<div class='row paragraphQuestionDivText' style='width:100%'>
							<div class='input-group col-md-10' style='margin-top:1%'>
								<textarea class='form-control' id='paraQuestions' name='paraQuestions'><c:out value="${paraQuestions[loop.index]}"/></textarea>
								<span class='input-group-addon'><i class='glyphicon glyphicon-trash removeQuestion'></i></span>
							</div>
							<div class='col-md-2'>
								<input type='number' name='paraPerQuestionMarks' placeholder='Marks' class='form-control' style='margin-top:15%' value="<c:out value="${paraPerQuestionMarks[loop.index]}"/>">
							</div>
							 </div>
					</c:forEach>
				</div>
			</div>
 			<div class="form-group">
			<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<input type="button" class="btn btn-default startuplodingexamSave" value="Save"/>
					<input type="button" class="btn btn-default CancleUploading" value="Cancel" id="CancleUploading"/>
				</div>
		</div>
 	</form>
	
	</div>
	
	<form action="editquestion" id="editQuestionForm">
		<input type="hidden" id="division" name="division" value='<c:out value="${division}"></c:out>'>
 		<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}"></c:out>'>
 		<input type="hidden" id="topicID" name="topicID" value='<c:out value="${topicID}"></c:out>'>
 		<input type="hidden" id="currentPage" name="currentPage" value='<c:out value="${currentPage}"></c:out>'>
 		<input type="hidden" id="totalPages" name="totalPages" value='<c:out value="${totalPages}"></c:out>'>
 		<input type="hidden" id="questionNumber" name="questionNumber" value='<c:out value="${questionNumber}"></c:out>'>
 		<input type="hidden" id="questiontype" name="questiontype" value='<c:out value="${questiontype}"></c:out>'>
 		<input type="hidden" id="preSelectedMarks" name="preSelectedMarks" value='<c:out value="${preSelectedMarks}"></c:out>'>
 		<input type="hidden" id="actionname" name="actionname" value="cancleEdit">
</form>
	</body>
	</html>