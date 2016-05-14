var PARA_QUESTION_TMPL = "#parahiddenQuestion .paraQuestionTmpl";
var ADD_QUESTION = "#addQuestions";
var PARA_QUESTION = "#paraQuestion";
var PARA_QUESTION_IMAGE = "#addParaQuestionImage";
var PARA_QUESTION_TMPL = "#parahiddenQuestion .paraQuestionTmpl";
var IMAGE_ROW = "#paraQuestionImage";
var PARA_QUE_IMAGE = "#uploadParaQuestionImage";
var DELETE_PARA_OPTION = ".deleteParaQuestion";
var DELETE_IMAGE = ".answer_image_with_btn_remove";


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