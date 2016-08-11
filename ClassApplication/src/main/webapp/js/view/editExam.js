$(document).ready(function(){
	var eExam = new EditExam();
	$("body").on("click",".preview",eExam.showPreview)
		.on("click","#cancelPreview",eExam.cancelPreview);
	$("#editQuestionPaper").cfSummerNote();
	
});

function EditExam(){
	var SELECTED_QUETION_ID = ".selectedQuestionPaperID";
	var HEADER_DESC = "#headerDesc";
	var DIVISION = "#division";
	var examUrl = "rest/classownerservice/exam/";

	var baseURL = "/rest/classownerservice/";
	var getQuestionPaperUrl = baseURL + "getQuestionPaper/";
	var getHeaderUrl = baseURL+"getHeader/";

	var ITEM_TYPE = {
			SECTION:"Section",
			QUESTION:"Question",
			INSTRUCTION:"Instruction"
		}

	
	this.showPreview = function(){
		console.log("showPreview");
		var paperId = $(this).closest('.examSubjectPapers').find(SELECTED_QUETION_ID).val();
		var marks = $(this).closest('.examSubjectPapers').find(".marks").val();
		
		var headerId = $(HEADER_DESC).val();
		var divisionId = $(DIVISION).val();
		var handler = {};
		handler.success = previewSuccess;
		handler.error = function(e){};
		
		var handlerHeader = {};
		handlerHeader.success = function(e){};
		handlerHeader.error = function(e){};
		if(paperId.trim()==""){
			$.notify({message: "Please select question paper"},{type: 'danger'});
			return;
		}
		rest.get(getQuestionPaperUrl+divisionId+"/"+paperId,handler);
		rest.get(getHeaderUrl+headerId,handlerHeader);

	}
	
	function previewSuccess(examData){
		console.log(examData);
		var data = examData.fileObject;
		var questionPaperFileElementList = data.questionPaperFileElementList;
		$(PREVIEW_PAGE_CONTENT).empty();
		var text = "";
		$.each(questionPaperFileElementList,function(){
			if(this.item_type == ITEM_TYPE.SECTION){
				console.log("Section..."+this.item_description);
				text = this.item_description;
			}else if(this.item_type == ITEM_TYPE.INSTRUCTION){
				console.log("Instruction..."+this.item_description);
				text = this.item_description;
			}else if(this.item_type == ITEM_TYPE.QUESTION){
				console.log("Question bank..."+this.questionbank.que_text);
				text = this.questionbank.que_text;
			}
			$("#editQuestionPaperPreviewDiv").show();
			$("#editModeDiv").hide();
			//$('#editQuestionPaper').summernote('insertText', text);
		});
	}
	
	this.cancelPreview = function(){
		$("#editQuestionPaperPreviewDiv").hide();
		$("#editModeDiv").show();
	}
}