(function(){
	var SAVE_SUBJECTIVE_EXAM = "#saveSubjectiveExam";
	var ADD_SUBJECTIVE_QUESTION_IMAGE = "#addSubjectiveQuestionImage";
	var SUBJECTIVE_IMAGE_ROW = "#subjectiveImageRow";
	var ADD_SUBJECT_FORM = "form#addSubjectiveQuestion";
	var REMOVE_OPTION_IMAGE = ".answer_image_with_btn_remove";
	var CANCEL = "#cancelEdit";
	var saveSubjectiveExamUrl = "/rest/classownerservice/examservice/saveSubjective";
	
	
	$(document).ready(function(){
		
		$("body").on("change",ADD_SUBJECTIVE_QUESTION_IMAGE,showAndUploadImageForSubjective).
			on("click",SAVE_SUBJECTIVE_EXAM,saveExam).
			on("click",REMOVE_OPTION_IMAGE,remoteThisImage).
			on("click",CANCEL,cancelEdit);
	});
	
	function cancelEdit(){
		$("form#searchQuestion").submit();
	}
	
	function showAndUploadImageForSubjective(){
		var imageFile = $(ADD_SUBJECTIVE_QUESTION_IMAGE)[0];
		var handler = {};
		handler.success = function(e){console.log(e)}
		handler.error = function(e){console.log(e)}
		rest.uploadImageFile(imageFile ,handler,false);
		input = this;
		if ( input.files && input.files[0] ) {
	        var FR= new FileReader();
	        FR.onload = function(e) {
				var handler = {}
				var imgTag = "<img src='"+e.target.result+"' width='200px' height='200px' />";
				var hiddenTag = "<input type='hidden' id='questionImage' name='questionImage' value='{{questionImage}}' />";
				var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
				var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+hiddenTag+imgTag+"</div>"
				var imageRow = $(input).parents("div#subjectiveDiv").find(SUBJECTIVE_IMAGE_ROW);
				
				handler.success=function(successResp){
					imageColElement = imageColElement.replace("{{questionImage}}",successResp.fileid);
					imageRow.append(imageColElement);
				}
				handler.error=function(e){console.log("Error",e)}
				rest.uploadImageFile(input ,handler,false);
	        };
				FR.readAsDataURL( input.files[0] );
			}
	}
	
	function saveExam(){
		if($(ADD_SUBJECT_FORM).valid()){
			var subjectiveExamBean = new SubjectiveExamBean();
			//divId,subId,topicId,questionId is for from jsp page request variable
			subjectiveExamBean.classId = divId;//$("#classownerUploadexamDivisionName").val();
			subjectiveExamBean.subjectId = subId;//$("#classownerUploadexamSubjectNameSelect").val();
			subjectiveExamBean.topicId = topicId;//$("#classownerUploadQuestionTopicSelect").val();
			subjectiveExamBean.questionId = queId;//$("#questionId").val();
			
			//1 is for subjective
			subjectiveExamBean.questionType = "1";
			subjectiveExamBean.question = $("#subjectiveQuestion").val();
			subjectiveExamBean.marks = $("#questionmarks").val();
			subjectiveExamBean.images = [];
			var queImgs = $(ADD_SUBJECT_FORM).find("input#questionImage");
			$.each(queImgs,function(key,queImg){
				subjectiveExamBean.images.push($(queImg).val());
			});
			console.log(subjectiveExamBean);
			var handler = {};
			handler.success = function(e){
				$.notify({message: "Question updated successfuly"},{type: 'success'});
				setTimeout(function(){cancelEdit();},1000*2);
			}
			handler.error = function(e){console.log(e)}
			rest.put(saveSubjectiveExamUrl,handler,JSON.stringify(subjectiveExamBean),true);
		}else{
			console.log("not valid");
		}		
	}
	
	function remoteThisImage(){
		$(this).closest(".image_with_btn").remove();
	}
	
	
	function SubjectiveExamBean(){
		this.questionId;
		this.classId;
		this.subjectId;
		this.topicId;
		this.questionType;
		this.question;
		this.marks;
		this.images;
	}
})();

function loadSubjectiveQuestion(data){
	$("#subjectiveQuestion").val(data.que_text);
	$("#questionmarks").val(data.marks);
	$("#questionId").val(data.que_id);
	var images = "";
	for(var index=0;index<data.primaryImage.length;index++){
		var imgTag = "<img src='/rest/commonservices/image/"+data.primaryImage[index]+"' width='200px' height='200px' />";
		var hiddenTag = "<input type='hidden' id='questionImage' name='questionImage' value='{{questionImage}}' />";
		var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
		var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+hiddenTag+imgTag+"</div>"
		var imageId = getImageId(data.primaryImage[index]);
		imageColElement = imageColElement.replace('{{questionImage}}',imageId);
		images += imageColElement; 

	}
	$("#subjectiveImageRow").append(images);
}

function getImageId(url){
	var lastIndex = url.lastIndexOf("_");
	return url.slice(lastIndex+1);
}