var SAVE_OBJECTIVE_EXAM = "#saveObjectiveExam";
	var ADD_OBJECTIVE_QUESTION_IMAGE = "#addObjectiveQuestionImage";
	var OBJECTIVE_IMAGE_ROW = "#objectiveImageRow";
	var ADD_OBJECT_FORM = "form#addObjectiveQuestionForm";
	var ADD_OPTION = "#addOption";
	var OPTION_COMPONENT = "#hiddenComponents .options";
	var OPTION_ROW = "#objectiveOptionRow";
	var UPLOAD_OPTION_IMAGE = "#uploadImage";
	var REMOVE_OPTION_IMAGE = ".answer_image_with_btn_remove";
	var DELETE_OPTION = ".deleteOption";
	var saveObjectiveExamUrl = "/rest/classownerservice/examservice/saveObjective";
	

(function(){
	$(document).ready(function(){
			$("body").on("change",ADD_OBJECTIVE_QUESTION_IMAGE,function(){
				showAndUploadImageForObjective("form#addObjectiveQuestionForm","objectiveQuestionImage",this,"objectiveQuestionImage");
			}).
			on("click",SAVE_OBJECTIVE_EXAM,saveExam).
			on("click",ADD_OPTION,addOption).
			on("change",UPLOAD_OPTION_IMAGE,function(){
				showAndUploadImageForObjective(".options","objectiveAnswersImage",this,"objectiveOptionImage");
			}).
			on("click",REMOVE_OPTION_IMAGE,remoteThisImage).
			on("click",DELETE_OPTION,deleteOption);
	});
	
	function showAndUploadImageForObjective(parent,imagename,that,imageType){
		var imageFile = $(ADD_OBJECTIVE_QUESTION_IMAGE)[0];
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
				var hiddenTag = "<input type='hidden' class='"+imageType+"' name='objectiveQuestionImage' value='{{"+imagename+"}}' />";
				var closeButton = '<button type="button" class="answer_image_with_btn_remove close" aria-hidden="true">&times;</button>';
				var imageColElement = "<div class='col-sm-3 image_with_btn'>"+closeButton+hiddenTag+imgTag+"</div>"
				var imageRow = $(input).closest(parent).find(OBJECTIVE_IMAGE_ROW);
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
	
	function saveExam(){
		if($(ADD_OBJECT_FORM).valid()){
			var objectiveExamBean = new ObjectiveExamBean();
			objectiveExamBean.classId = $("#classownerUploadexamDivisionName").val();
			objectiveExamBean.subjectId = $("#classownerUploadexamSubjectNameSelect").val();
			objectiveExamBean.topicId = $("#classownerUploadQuestionTopicSelect").val();
			objectiveExamBean.questionType = $("#classownerQuestionTypeSelect").val();
			objectiveExamBean.question = $("#objectiveQuestion").val();
			objectiveExamBean.marks = $("#objectiveQuestionmarks").val();
			objectiveExamBean.images = [];
			
			var queImgs = $(ADD_OBJECT_FORM).find("input.objectiveQuestionImage");
			$.each(queImgs,function(key,queImg){
				objectiveExamBean.images.push($(queImg).val());
			});
			
			//forming options with there image
			var optionsArr = [];
			var optionsDom = $("#objectiveOptionRow .options");
			$.each(optionsDom,function(index,option){
				var opt = new Options();
				opt.optionCorrect = $(option).find("input[type='checkbox']").is(":checked");
				opt.optionText = $(option).find("input[type='text']").val();
				
				//Collecting option images
				var optionImagesArr = [];
				var images = $(option).find("#objectiveImageRow .objectiveOptionImage");
				$.each(images,function(){
					optionImagesArr.push($(this).val());
				});
				opt.optionImage = optionImagesArr;
				optionsArr.push(opt);
			});
			
			var handler = {};
			handler.success = function(e){console.log(e)}
			handler.error = function(e){console.log(e)}
			
			objectiveExamBean.options = optionsArr;
			rest.post(saveObjectiveExamUrl,handler,JSON.stringify(objectiveExamBean),true);
			console.log(objectiveExamBean);
		}else{
			console.log("not valid");
		}		
	}
	
	
	function remoteThisImage(){
		$(this).closest(".image_with_btn").remove();
	}
	
	function deleteOption(){
		$(this).closest(".options").remove();
	}
	function ObjectiveExamBean(){
		this.classId;
		this.subjectId;
		this.topicId;
		this.questionType;
		this.question;
		this.marks;
		this.images;
		this.options;
	}
	
	function Options(){
		this.optionText;
		this.optionCorrect;
		this.optionId;
		this.optionImage;
	}
})();

function loadObjectiveQuestion(data){
	$("#objectiveQuestion").val(data.que_text);
	$("#objectiveQuestionmarks").val(data.marks);
	
	var images = "";
	for(var index=0;index<data.primaryImage.length;index++){
		images = images + "<img src=/rest/commonservices/image/"+data.primaryImage[index]+" width='200px' height='200px'>";
	}
	
	for(var index=1;index<=10;index++){
		if(data["opt_"+index]){
			addOptionValue(data["opt_"+index],true,data.secondaryImage[index-1]?data.secondaryImage[index-1]:[]);
		}else{
			break;
		}
	}
	$("#objectiveImageRow").append(images);
}

function addOptionValue(text,selected,images){
	var optionComponent = $(OPTION_COMPONENT).clone();
	$(optionComponent).find("input[type='text']").val(text);
	var optionImages = "";
	for(var index=0;index<images.length;index++){
		optionImages = optionImages + "<img src=/rest/commonservices/image/"+images[index]+" width='200px' height='200px'>";
	}
	
	$(optionComponent).find("#objectiveImageRow").append(optionImages);
	$(OPTION_ROW).append(optionComponent);
	
}