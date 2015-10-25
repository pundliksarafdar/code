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
			'<input type="file" class="uploadOptionImageModalFileBtn" multiple>'+
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
		var inValid = $('form[action="uploadexams"]').validateCustome();
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
			var imgTag = "<img src='"+e.target.result+"' width='200px' height='200px'/>";
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
	
	$('form[action="uploadexams"]').on("change","input[type='file']",setQuestionSize);
		//function for edit option images
	$("body").on("change",".uploadOptionImageModalFileBtn",onAddMoreOptionImage);
	$("#submitquestionsuploadfile").on("change",addQuestionImages);
	$("body").on("click",".Question_image_with_btn_remove",removeQuestionImage);
	$("body").on("click",".answer_image_with_btn_remove",removeAnswerImage);
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
 
 <form action="uploadexams" method="post" enctype="multipart/form-data" id="uploadexams" class="form-horizontal corex-form-container">
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
			<label class="control-label">
				<h4>Add Question</h4>
			</label>
			<hr>
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
				
				<label for="questionmarks" class="col-sm-2 control-label">Topic</label>
				<div class="col-sm-2">
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