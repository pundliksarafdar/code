

$(document).ready(function(){
	var CLASS_SELECT = "#classownerUploadexamDivisionName";
	var SUB_SELECT = "#classownerUploadexamSubjectNameSelect";
	var TOPIC_SELECT = "#classownerUploadQuestionTopicSelect";
	var QUESTION_TYPE= "#classownerQuestionTypeSelect";
	
	/*$(CLASS_SELECT).on("change",disableAndClearForm);*/
	/*$(SUB_SELECT).on("change",disableAndClearForm);
	$(TOPIC_SELECT).on("change",disableAndClearForm);
	$(QUESTION_TYPE).on("change",disableAndClearForm);*/
	//$("body").on("change",ADD_SUBJECTIVE_QUESTION_IMAGE,showAndUploadImageForSubjective);
});

/*function disableAndClearForm(){
	var forms = $("#paragraphDiv,#MCQDiv,#subjectiveDiv").find("form");
	for(var formIndex=0;formIndex<forms.length;formIndex++){
		forms[formIndex].reset();
	}
	$("#QueAndOpttextPreview").empty();
	$("#QueAndOptMathBuffer").empty();
	$("#paragraphDiv,#MCQDiv,#subjectiveDiv").hide();
}*/
function showAndUploadImageForSubjective1(){
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
			var hiddenTag = "<input type='hidden' id='questionImage'/>";
			var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
			var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+imgTag+hiddenTag+"</div>"
			var imageRow = $(input).parents("div#subjectiveDiv").find(SUBJECTIVE_IMAGE_ROW);
			
			handler.success=function(successResp){
				$(imageColElement).find("#questionImage").val(successResp.fileid);
				imageRow.append(imageColElement);
			}
			handler.error=function(e){console.log("Error",e)}
			rest.uploadImageFile(input ,handler,false);
        };
			FR.readAsDataURL( input.files[0] );
		}

}