<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<h4>Start Uploading Exam</h4>
<script>
$(document).ready(function(){
	var indexOption = 0;
	var currentOptionUploadImageBtn;
	
	$(".addOptionUploadExam").on('click',function(){
		var submitquestionsuploadoptionsText = "<div class='form-group'><div class='input-group'><span class='input-group-addon'><input type='checkbox' name='answersOptionCheckBox' value='"+indexOption+"'></span><textarea class='form-control' name='answersOptionText'></textarea><span class='input-group-addon'><i class='glyphicon glyphicon-picture uploadAnswerImage'></i></span><span class='input-group-addon'><i class='glyphicon glyphicon-trash removeOption'></i></span></div><div id='optionImagesDiv"+indexOption+"'></div></div>";
		$("#submitquestionsuploadoptions").append(submitquestionsuploadoptionsText);
		indexOption++;
	});
	
	$('form[action="uploadexams"]').on("click",".removeOption",function(){
		$(this).parents(".form-group").remove();
	});
	
	$('form[action="uploadexams"]').on("click",".uploadAnswerImage",function(){
			currentOptionUploadImageBtn = $(this);
			$("#uploadOptionImageModal").modal("show");
			$("#uploadOptionImageModalAnswerImages").empty();
			$("#uploadOptionImageModal form")[0].reset();
			var valueText = currentOptionUploadImageBtn.parents(".form-group").find("textarea").val();
			$("#uploadOptionImageModal").find("p#optionText").text(valueText);
	});
	
	$("#submitquestionsuploadfile").on("change",function(e){
			
	});
	
	$("#uploadOptionImageModal #uploadOptionImageModalSaveImage").on("click",function(){
		var modalOptionImageFiles = $("#uploadOptionImageModal input#uploadOptionImageModalFileBtn");
		//console.log(modalOptionImageFiles[0].files);
		currentOptionUploadImageBtn.parents(".form-group").find("input[name='optionImages']").remove();
		currentOptionUploadImageBtn.parents(".form-group").append("<input type='file' name='optionImages' class='hide'/>");
		var fileInput = currentOptionUploadImageBtn.parents(".form-group").find("input[name='optionImages']")[0];
		fileInput.files = modalOptionImageFiles[0].files;
		var imageDivId = currentOptionUploadImageBtn.parents(".form-group").find("[id^='optionImagesDiv']");
		readURL(fileInput,imageDivId.attr("id"));
	});
	
	var uploadedMarks = <c:out value="${requestScope.uploadedMarks}"></c:out>;
	var totalMarks = <c:out value="${sessionScope.exammarks}"></c:out>;
	console.log(uploadedMarks+":"+totalMarks)
	var uploadedMarksPercentage = (uploadedMarks/totalMarks)*100;
	$("div#uploadedMarksCompleted").css("width",uploadedMarksPercentage+"%");
	$("div#uploadedMarksInCompleted").css("width",100-uploadedMarksPercentage+"%");
});

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
</script>
<div class="modal fade" id="uploadOptionImageModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Upload Image for options</h4>
      </div>
      <div class="modal-body">
        <form>
		<p id="optionText"></p>
		<hr/>
		<input type="file" id="uploadOptionImageModalFileBtn" multiple onchange="readURL(this,'uploadOptionImageModalAnswerImages');">
		<div id="uploadOptionImageModalAnswerImages">
		
		</div>
		</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="uploadOptionImageModalSaveImage">Save Files</button>
      </div>
    </div>
  </div>
</div>
<div class="progress active" style="height:25px;">
	<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
		aria-valuemin="0" aria-valuemax="100" style="width: 50%" id="uploadedMarksCompleted">
		<div class="pull-left">
			<c:out value="${sessionScope.examname}"></c:out>
		</div>		
	</div>
	<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="45"
		aria-valuemin="0" aria-valuemax="100" style="width: 50%" id="uploadedMarksInCompleted">
		<div class="pull-right">
			Total Marks <c:out value="${sessionScope.exammarks}"></c:out>
		</div>
	</div>
</div>
<form action="uploadexams">
	<input type="hidden" name="actionname" value="submitquestions"/>
		<div>
			<div class="form-group">
				<label for="examname">Question </label>
				<textarea class="form-control" id="question" name="examname"></textarea>
			</div>
			<div class="form-group">
				<label for="exammarks">Marks</label>
				<input type="number" class="form-control" id="questionmarks" name="examname" maxlength="5" size="5" style="width: 10%;"></input>
			</div>
			<div class="form-group">
				<input type="file" class="btn btn-default" onchange="readURL(this,'fileList');" id="submitquestionsuploadfile" multiple >
			</div>
			<div id="fileList">
			</div>
			<div class="form-group">
				<a class="btn btn-default addOptionUploadExam">Add Option</a>
			</div>
			<div class="" id="submitquestionsuploadoptions">
				
			</div>
		</div>
		<div class="form-group">
			<input type="submit" class="btn btn-default" value="Save"/>
		</div>
</form>