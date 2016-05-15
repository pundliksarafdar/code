var PARA_QUESTION_TMPL = "#parahiddenQuestion .paraQuestionTmpl";
var ADD_QUESTION = "#addQuestions";
var PARA_QUESTION = "#paraQuestion";
var PARA_QUESTION_IMAGE = "#addParaQuestionImage";
var PARA_QUESTION_TMPL = "#parahiddenQuestion .paraQuestionTmpl";
var IMAGE_ROW = "#paraQuestionImage";
var PARA_QUE_IMAGE = "#uploadParaQuestionImage";
var DELETE_PARA_OPTION = ".deleteParaQuestion";
var DELETE_IMAGE = ".answer_image_with_btn_remove";
var SAVE_PARA = "#saveParagraphQuestion";
var PARA_QUESTION_FORM = "form#paraQuestionForm";

var PARA = PARA_QUESTION_FORM+" #paragraph";
var MARKS = PARA_QUESTION_FORM+" #totalMarks";
var PARA_IMAGE = PARA_QUESTION_FORM+" #paraQuestionImage .paraOptionImage";

var PARA_QUESTIONS = PARA_QUESTION_FORM + " #paraQuestion .paraQuestionTmpl";

/*URL*/
var saveParaQuestionUrl = "/rest/classownerservice/examservice/paraquestion";
$(document).ready(function(){
	$("body").on("click",ADD_QUESTION,addMoreParaQuestion)
		.on("change",PARA_QUESTION_IMAGE,function(){
			showAndUploadImageForParagraph("form#paraQuestionForm","paraImage",this,"paraOptionImage");
		})
		.on("change",PARA_QUE_IMAGE,function(){
			showAndUploadImageForParagraph(".paraQuestionTmpl","paraImage",this,"paraQuestionImage");
		})
		
		.on("click",SAVE_PARA,updateParagraphQuestion);
});

function addMoreParaQuestion(){
	var paraTmpl = $(PARA_QUESTION_TMPL).clone();
	$(PARA_QUESTION).append(paraTmpl);
}


function loadParaquestion(data,queId){
	$("#paragraph").val(data.paragraph);
	$("#totalMarks").val(data.marks);
	
	var images = "";
	for(var index=0;index<data.images.length;index++){
		//images = images + "<img src=/rest/commonservices/image/"+data.primaryImage[index]+" width='200px' height='200px'>";
		var imgTag = "<img src='/rest/commonservices/image/examImage_paragraph_"+queId+"_paragraph_"+data.images[index]+"' width='200px' height='200px' />";
		var hiddenTag = "<input type='hidden' name='objectQuestionImage' class='paraOptionImage' value='{{questionImage}}' />";
		var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
		var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+hiddenTag+imgTag+"</div>"
		var imageId = getImageId(data.images[index]);
		imageColElement = imageColElement.replace('{{questionImage}}',imageId);
		images += imageColElement;
	}	
	$("#paraQuestionImage").append(images);
	
	/*Create paragraph questions*/
	addParaQuestion(data.paraQuestion,queId);
}

function getImageId(url){
	var lastIndex = url.lastIndexOf("_");
	return url.slice(lastIndex+1);
}

function addParaQuestion(paraQuestion,queId){
	for(var index=0;index<paraQuestion.length;index++){
		var paraTmpl = $(PARA_QUESTION_TMPL).clone();
		paraTmpl.find("#paraQuestion").val(paraQuestion[index].questionText);
		paraTmpl.find("#questionMarks").val(paraQuestion[index].questionMarks);
		var images = "";
		for(var indexInner=0;indexInner < paraQuestion[index].queImage.length;indexInner++){
			//images = images + "<img src=/rest/commonservices/image/"+data.primaryImage[index]+" width='200px' height='200px'>";
			var imgTag = "<img src='/rest/commonservices/image/examImage_paragraph_"+queId+"_paragraph_questions_"+index+"_"+paraQuestion[index].queImage[indexInner]+"' width='200px' height='200px' />";
			var hiddenTag = "<input type='hidden' name='objectQuestionImage' class='paraOptionImage' value='{{questionImage}}' />";
			var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
			var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+hiddenTag+imgTag+"</div>"
			var imageId = getImageId(paraQuestion[index].queImage[indexInner]);
			imageColElement = imageColElement.replace('{{questionImage}}',imageId);
			images += imageColElement;
		}	
		paraTmpl.find("#paraQuestionImage").append(images);
		$(PARA_QUESTION).append(paraTmpl);	
	}	
}

function updateParagraphQuestion(){
	if($(PARA_QUESTION_FORM).valid()){
		var paraQuestionBean = new ParaQuestionBean();
		paraQuestionBean.classId = divId;
		paraQuestionBean.subjectId = subId;
		paraQuestionBean.topicId = topicId;
		paraQuestionBean.questionType = "3";//3 is for paragraph
		
		paraQuestionBean.paragraph = $(PARA).val();
		paraQuestionBean.marks = $(MARKS).val();
		paraQuestionBean.images = [];
		paraQuestionBean.paraQuestion = [];
		var paraImg = $(PARA_IMAGE);
		$.each(paraImg,function(){
			paraQuestionBean.images.push($(this).val());
		});
		
		var paraQuestion = $(PARA_QUESTIONS);
		$.each(paraQuestion,function(){
			var paraQuestion = new ParaQuestion();
			paraQuestion.questionText = $(this).find('[type="text"]').val();
			paraQuestion.questionMarks = $(this).find('#questionMarks').val();
			paraQuestion.queImage = [];
			
			var paraQuestionImage = $(this).find(".paraQuestionImage");
			$.each(paraQuestionImage,function(){
				paraQuestion.queImage.push($(this).val());
			});
			paraQuestionBean.paraQuestion.push(paraQuestion);
		});
		
		var handler={};
		handler.success = function(e){}
		handler.error = function(e){}
		
		var question = $("#paraQuestion").find(".paraQuestionTmpl");
		var allQuestionMarks = 0;
		
		$.each(question,function(){
			allQuestionMarks = allQuestionMarks + parseInt($(this).find("#questionMarks").val());
		});
		var totalMarks = parseInt($("#totalMarks").val());
		if(allQuestionMarks != totalMarks){
			//var validator = $( PARA_QUESTION_FORM ).validate();
			$(PARA_QUESTION_FORM).validate().showErrors({
				  "totalMarks": "Paragraph marks is less than total marks of questions"
			});
		}else{
			console.log(paraQuestionBean);
			rest.put(saveParaQuestionUrl+"/"+queId,handler,JSON.stringify(paraQuestionBean),true);	
		}
		
	}
}

function showAndUploadImageForParagraph(parent,imagename,that,imageType){
	var imageFile = $(that)[0];
	var handler = {};
	handler.success = function(e){console.log(e)}
	handler.error = function(e){console.log(e)}
	rest.uploadImageFile(imageFile ,handler,false);
	input = that;
	if ( input.files && input.files[0] ) {
        var FR= new FileReader();
        FR.onload = function(e) {
			var handler = {}
			var imgTag = "<img src='"+e.target.result+"' width='200px' height='200px' />";
			var hiddenTag = "<input type='hidden' class='"+imageType+"' value='{{"+imagename+"}}' />";
			var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
			var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+hiddenTag+imgTag+"</div>"
			var imageRow = $(input).closest(parent).find(IMAGE_ROW);
			handler.success=function(successResp){
				imageColElement = imageColElement.replace("{{"+imagename+"}}",successResp.fileid);
				imageRow.append(imageColElement);
			}
			handler.error=function(e){console.log("Error",e)}
			rest.uploadImageFile(input ,handler,false);
        };
			FR.readAsDataURL( input.files[0] );
		}
}

function ParaQuestionBean(){
	this.classId;
	this.subjectId;
	this.topicId;
	this.questionType;
	this.paragraph;
	this.marks;
	this.images;
	this.paraQuestion;
}

function ParaQuestion(){
	this.questionText;
	this.questionMarks;
	this.queImage;
}
