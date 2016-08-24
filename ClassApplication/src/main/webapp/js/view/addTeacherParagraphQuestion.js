(function(){
	var ADD_QUESTION = "#addQuestions";
	var PARA_QUESTION = "#paraQuestion";
	var PARA_QUESTION_IMAGE = "#addParaQuestionImage";
	var PARA_QUESTION_TMPL = "#parahiddenQuestion .paraQuestionTmpl";
	var IMAGE_ROW = "#paraQuestionImage";
	var PARA_QUE_IMAGE = "#uploadParaQuestionImage";
	var DELETE_PARA_OPTION = ".deleteParaQuestion";
	var DELETE_IMAGE = ".answer_image_with_btn_remove";
	var SAVE_PARA_QUESTION = "#saveParagraphQuestion";
	var PARA_QUESTION_FORM = "form#paraQuestionForm";
	
	var PARA = PARA_QUESTION_FORM+" #paragraph";
	var MARKS = PARA_QUESTION_FORM+" #totalMarks";
	var PARA_IMAGE = PARA_QUESTION_FORM+" #paraQuestionImage .paraOptionImage";
	
	var PARA_QUESTIONS = PARA_QUESTION_FORM + " #paraQuestion .paraQuestionTmpl";
	var saveParaQuestionUrl = "/rest/teacher/paraquestion";
	
	$(document).ready(function(){
		$("body").on("click",ADD_QUESTION,addParaQuestion)
		.on("change",PARA_QUESTION_IMAGE,function(){
			showAndUploadImageForObjective("form#paraQuestionForm","paraImage",this,"paraOptionImage");
		})
		.on("change",PARA_QUE_IMAGE,function(){
			showAndUploadImageForObjective(".paraQuestionTmpl","paraImage",this,"paraQuestionImage");
		})
		.on("click",DELETE_PARA_OPTION,function(){
			$(this).closest(".paraQuestionTmpl").remove();
		})
		.on("click",DELETE_IMAGE,function(){
			$(this).closest(".image_with_btn").remove();
		})
		.on("click",SAVE_PARA_QUESTION,saveParaQuestion);
		;
	});
	
	function addParaQuestion(){
		var paraTmpl = $(PARA_QUESTION_TMPL).clone();
		paraTmpl.find('[type="text"]').addExpresssion(true);
		$(PARA_QUESTION).append(paraTmpl);
	}
	
	function showAndUploadImageForObjective(parent,imagename,that,imageType){
		var inst_id = $("#instituteSelect").val()
		var imageFile = $(that)[0];
		var handler = {};
		handler.success = function(e){console.log(e)}
		handler.error = function(e){console.log(e)}
		/*rest.uploadImageFile(imageFile ,handler,false);*/
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
				rest.uploadTeacherImageFile(input,inst_id ,handler,false);
	        };
				FR.readAsDataURL( input.files[0] );
			}
	}
	
	function saveParaQuestion(){
		var validateFlag = false;
		if($("#instituteSelect").val() == "-1"){
			$("#instituteSelectError").html("Select Institute");
			validateFlag = true;
		}
		if($("#divisionSelect").val() == "-1" || $("#divisionSelect").val() == "" || $("#divisionSelect").val() == null){
			$("#divisionSelectError").html("Select Class");
			validateFlag = true;
		}
		if($("#subjectSelect").val() == "-1" || $("#subjectSelect").val() == "" || $("#subjectSelect").val() == null){
			$("#subjectSelectError").html("Select Subject");
			validateFlag = true;
		}
		if($(PARA_QUESTION_FORM).valid() && !validateFlag){
			var paraQuestionBean = new ParaQuestionBean();
			paraQuestionBean.classId = $("#divisionSelect").val();
			paraQuestionBean.subjectId = $("#subjectSelect").val();
			paraQuestionBean.topicId = $("#topicSelect").val();
			paraQuestionBean.questionType = $("#classownerQuestionTypeSelect").val();
			paraQuestionBean.instId = $("#instituteSelect").val();
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
			console.log(paraQuestionBean);
			var handler={};
			handler.success = function(e){
				if(e == ""){
					$.notify({message: 'Question saved'},{type: 'success'});
					}else{
						$.notify({message: 'Institute doesn\'t have enough memory,question not saved'},{type: 'danger'});	
					}
			}
			handler.error = function(e){}
			rest.post(saveParaQuestionUrl,handler,JSON.stringify(paraQuestionBean),true);
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

})();