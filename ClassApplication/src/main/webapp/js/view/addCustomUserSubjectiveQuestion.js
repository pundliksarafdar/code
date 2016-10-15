(function(){
	var SAVE_SUBJECTIVE_EXAM = "#saveSubjectiveExam";
	var ADD_SUBJECTIVE_QUESTION_IMAGE = "#addSubjectiveQuestionImage";
	var SUBJECTIVE_IMAGE_ROW = "#subjectiveImageRow";
	var ADD_SUBJECT_FORM = "form#addSubjectiveQuestion";
	var REMOVE_OPTION_IMAGE = ".answer_image_with_btn_remove";
	var TEXT_PREVIEW = ".textPreview";
	var saveSubjectiveExamUrl = "/rest/customuserservice/saveSubjective";
	
	
	$(document).ready(function(){
		$("body").on("change",ADD_SUBJECTIVE_QUESTION_IMAGE,showAndUploadImageForSubjective).
			on("click",SAVE_SUBJECTIVE_EXAM,saveExam).
			on("click",REMOVE_OPTION_IMAGE,remoteThisImage);
	});
	
	function showAndUploadImageForSubjective(){
		var imageFile = $(ADD_SUBJECTIVE_QUESTION_IMAGE)[0];
		var handler = {};
		handler.success = function(e){console.log(e)}
		handler.error = function(e){console.log(e)}
		rest.uploadCustomUserImageFile(imageFile ,handler,false);
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
				rest.uploadCustomUserImageFile(input ,handler,false);
	        };
				FR.readAsDataURL( input.files[0] );
			}
	}
	
	function saveExam(){
		$(".validation-message").html("");
		var validationFlag = false;
		var div_id = $("#classownerUploadexamDivisionName").val();
		var sub_id = $("#classownerUploadexamSubjectNameSelect").val();
		if(div_id == "-1" || div_id == null){
			$("#divisionError").html("Select Class");
			validationFlag = true;
		}
		if(sub_id == "-1" || sub_id == null){
			$("#subjectError").html("Select Subject");
			validationFlag = true;
		}
		
		if($(ADD_SUBJECT_FORM).valid() && validationFlag == false){
			var subjectiveExamBean = new SubjectiveExamBean();
			subjectiveExamBean.classId = $("#classownerUploadexamDivisionName").val();
			subjectiveExamBean.subjectId = $("#classownerUploadexamSubjectNameSelect").val();
			subjectiveExamBean.topicId = $("#classownerUploadQuestionTopicSelect").val();
			subjectiveExamBean.questionType = $("#classownerQuestionTypeSelect").val();
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
				if(e == ""){
				$.notify({message: 'Question saved'},{type: 'success'});
				}else{
					$.notify({message: 'Memory not available,question not saved'},{type: 'danger'});	
				}
			}
			handler.error = function(e){console.log(e)}
			rest.post(saveSubjectiveExamUrl,handler,JSON.stringify(subjectiveExamBean),true);
		}else{
			console.log("not valid");
		}		
	}
	
	function remoteThisImage(){
		$(this).closest(".image_with_btn").remove();
	}
	
	function SubjectiveExamBean(){
		this.classId;
		this.subjectId;
		this.topicId;
		this.questionType;
		this.question;
		this.marks;
		this.images;
	}
})();