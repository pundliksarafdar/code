<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>
$(document).ready(function(){
	var indexOption = 0;
	var currentOptionUploadImageBtn;
	indexOption=$("#indexOption").val();
	$(".addOptionUploadExam").on('click',function(){
		var imgBtn = '<span class="btn btn-success fileinput-button">'+
			'<i class="glyphicon glyphicon-folder-open"></i> '+
					'<span>Add images </span>'+
			'<input type="file" id="uploadOptionImageModalFileBtn" multiple onchange="readURL(this,\'optionImagesDiv'+indexOption+'\'),appendCount(this,\'optionImagesDiv'+indexOption+'\');" name="optionImages">'
		'</span>'
		var submitquestionsuploadoptionsText = "<div class='form-group'><div class='input-group'><span class='input-group-addon'><input type='checkbox' name='answersOptionCheckBox' value='"+indexOption+"' id='answersOptionCheckBox"+indexOption+"'></span><textarea class='form-control' name='answersOptionText'></textarea><span class='input-group-addon'><i class='glyphicon glyphicon-trash removeOption' id='removeOption_"+indexOption+"'></i></span></div><div id='optionImagesDiv"+indexOption+"'></div>"+imgBtn+"</div>";
		$("#submitquestionsuploadoptions").append(submitquestionsuploadoptionsText);
		//$("#submitquestionsuploadoptions .form-group").append(imgBtn);
		indexOption++;
	});
	
	$('form[action="uploadexams"]').on("click",".removeOption",function(){
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
	
	$('form[action="uploadexams"]').on("click",".uploadAnswerImage",function(){
			currentOptionUploadImageBtn = $(this);
			$("#uploadOptionImageModal").modal("show");
			$("#uploadOptionImageModalAnswerImages").empty();
			$("#uploadOptionImageModal form")[0].reset();
			var valueText = currentOptionUploadImageBtn.parents(".form-group").find("textarea").val();
			$("#uploadOptionImageModal").find("p#optionText").text(valueText);
	});
	
	
	$("input.startuplodingexamSave").on('click',function(){
		var inValid = $('form[action="uploadexams"]').validate();
		if(!inValid){
			var invalidOption = validateOption();
			if(!invalidOption){
				$('form[action="uploadexams"]').submit();
			}
		}
	});
	
	$("#CancleUploading").on("click",function(){
		//$("#uploadexams").prop("action","cancleuploading");
		$("#actionname").val("cancleuploading");
		$("#uploadexams").submit();
	});
	
	$('form[action="uploadexams"]').on("change","input[type='file']",setQuestionSize);
	
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
		var options = $('input[name="optionImages"]');
		var hasError = false;
		$.each(options,function(index,fileChooser){
			if($(fileChooser).parents(".form-group").find("textarea").val().trim().length==0 && fileChooser.files.length == 0){
					$(fileChooser).parents(".form-group").find('.validation-message').empty();
					$(fileChooser).parents(".form-group").prepend("<div class='validation-message'>Please add image or option text</div>");
					hasError = true;
			}else{
					hasError = false;
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
<div>
<c:if test="${actionname eq 'submitquestions'}">
<c:choose>
<c:when test="${(institute != null) && (institute != '') && (currentPage eq 0)}">
<a type="button" class="btn btn-primary" href="teachercommoncomponent?forwardAction=uploadexams" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Back</a>
</c:when>
<c:otherwise>
<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=uploadexams" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Back</a>
 </c:otherwise>
 </c:choose>
 </c:if>
 <div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;">Add Question</div>
</div>
 <form action="uploadexams" method="post" enctype="multipart/form-data" id="uploadexams" >
	<input type="hidden" id="batch" name="batch" value="<c:out value="${batch}" ></c:out>">
	<input type="hidden" id="division" name="division" value="<c:out value="${division}" ></c:out>">
	<input type="hidden" id="subject" name="subject" value="<c:out value="${subject}" ></c:out>">
	<input type="hidden" id="questionNumber" name="questionNumber" value="<c:out value="${questionNumber}" ></c:out>">
	<input type="hidden" id="indexOption" name="indexOption" value="<c:out value="${indexOption}" ></c:out>">
	<input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
	<input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
	<input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
	<input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
	<input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
  <input type="hidden" name="searchedTopic" value='<c:out value="${searchedTopic}"></c:out>'>
	<input type="hidden" name="actionname" value='<c:out value="${actionname }"></c:out>' id="actionname"/>
	<input type="hidden" name="examname" value="<c:out value="${requestScope.examname}"></c:out>"/>
	<input type="hidden" name="uploadedMarks" value="<c:out value="${sessionScope.uploadedMarks}"></c:out>"/>
	<input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
		
			<div class="form-group">
				<label for="examname">Question </label>
				<div class="validation-message hide"></div>
				<textarea class="form-control" id="question" name="question" required><c:out value="${requestScope.questionData.question}"></c:out></textarea>
			</div>
			<div class="form-group">
				<label for="questionmarks">Marks</label>
				<div class="validation-message hide"></div>
				<input type="number" required class="form-control" id="questionmarks" name="questionmarks" maxlength="5" size="5" style="width: 10%;" min="1" max="5" value="<c:out value="${requestScope.questionData.marks}"></c:out>"></input>
			</div>
			<div class="form-group">
				<label for="questionmarks">Topic</label>
				<select class="form-control" id="topicID" name="topicID">
				<option value="-1">Select Topic</option>
				<c:if test="${selectedtopicID ne 0 && selectedtopicID ne -1}">
				<option value="<c:out value="${selectedtopicID}"></c:out>" selected="selected"><c:out value="${selectedtopicName}"></c:out></option>
				</c:if>
				<c:forEach items="${topics}" var="topic">
				<c:if test="${topic.topic_id ne selectedtopicID}">
				<option value="<c:out value="${topic.topic_id}"></c:out>"><c:out value="${topic.topic_name}"></c:out></option>	
				</c:if>
				</c:forEach>
				</select>
			</div>
			
			<div class="form-group">
				<span class="btn btn-success fileinput-button">
					<i class="glyphicon glyphicon-folder-open"></i>
					<span>Add images </span>
					<input type="file" onchange="readURL(this,'fileList');" id="submitquestionsuploadfile" multiple name="questionImages" value="<c:out value="${requestScope.questionData.marks}"></c:out>">
				</span>	
			</div>
			<div id="fileList">
			<c:forEach items="${requestScope.questionData.questionImage}" var="image">
				<img src='<c:out value="${image }"></c:out>' width="200px" height="200px" tyle="padding:5px;"/>
			</c:forEach>
			</div>
			<div class="form-group">
				<a class="btn btn-default addOptionUploadExam">Add Option</a>
			</div>
			<div class="form-btn-group" id="submitquestionsuploadoptions" mandatorySelection="1">
						<div class="validation-message hide"></div>
			<c:forEach items="${requestScope.questionData.options}" var="options" varStatus="counter">
				
				<div class='form-group'>
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
							class='glyphicon glyphicon-picture uploadAnswerImage'></i>
						</span> <span class='input-group-addon'> <i
							class='glyphicon glyphicon-trash removeOption' id='removeOption_<c:out value="${counter.index}"></c:out>'></i>
						</span>
					</div>
					<div id='optionImagesDiv<c:out value="${counter.index}"></c:out>'>
						<c:set var="lastCountIndex" value="${requestScope.optionImageEndCount[counter.index]}"></c:set>
						<c:choose>
							<c:when test="${counter.index eq 0 }">
								<c:set var="startCountIndex" value="0"></c:set>	
							</c:when>
							<c:otherwise>
								<c:set var="startCountIndex" value="${requestScope.optionImageEndCount[counter.index-1]}"></c:set>
							</c:otherwise>
						</c:choose>
							<c:forEach items="${requestScope.questionData.answerImage}" var="optionImage" begin='${startCountIndex }' end="${lastCountIndex-1}">
								<img src='<c:out value="${optionImage }"></c:out>' width="200px" height="200px" tyle="padding:5px;"/>
							</c:forEach>
						
					</div>
					
				</div>
			</c:forEach>
			
		</div>
		<div id="startuplodingexamImageFileSize" class="alert alert-info">
		
		</div>
		<div class="form-group">
			<input type="button" class="btn btn-default startuplodingexamSave" value="Save"/>
			<c:if test="${actionname ne 'submitquestions'}">
			<input type="button" class="btn btn-default CancleUploading" value="Cancel" id="CancleUploading"/>
			</c:if>
		</div>
</form>
</div>