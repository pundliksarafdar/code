<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style>
ul {
    list-style-type: none;
}
.modal-header{
background: white;
}
.modal-dialog{
width: 100%;
}
.col-md-*{
width: 100%;
margin : 0%;
padding :0%;}
.row{
width: 100%;
margin : 0%;
padding :0%;
}

.sectionBackground{
background: #A8D8D8;
padding-top: 1%;
padding-bottom: 1%;
margin-bottom: 1%;
}
.sectionUl{
padding: 0%;
}

.form-control{
	font-size:12px;
	height:25px;
    padding-top: 1px;
    padding-bottom: 1px;
    padding-left: 5px;
    padding-right: 5px;


}

.col-md-1,.col-md-2,.col-md-3{
padding-left: 10px;
padding-right: 10px;
}
.well .col-md-9,.well .col-md-1,.well .col-md-7{
margin: 1px;
padding: 0px;
}
.well{
padding: 0px;
margin-right: 2px
}

.patternError{
color: red;
font-size: 12px;
}
</style>
<script src="jquery-1.10.2.min.js"></script>
<script src="../js/PreviewPattern.js"></script>
<script>
var PAGE = "#createExam",
	ITEM_NAME = PAGE+"ItemName",
	ITEM_MARKS = PAGE+"ItemMarks",
	PARENT_ID = "data-parent-id",
	ID="data-item-id";
	
	/**/
var ADD_ITEM = PAGE + "AddItem",
	DELETE_ITEM = PAGE + "DeleteItem",
	GENERATE = PAGE + "Generate",
	PATTERN_ITEM =  ".createExamPatternItem",
	ALTERNATE_ITEM = PAGE + "AddAlternateItem";
var topic_names = "";
var topic_ids = "";
var enumData = {addSection:"addSection",addInstruction:"addInstruction",addQuestion:"addQuestion"};	
var errorFlag;
var patternType;
var subjectList = [];
var wosSubjectID = "";
var wosTopicID = "";

$(document).ready(function(){
	$("#instituteSelect").change(function(){
		var inst_id = $(this).val();
		$("#subject").empty();
		 $("#subject").select2().val("").change();
		 $("#subject").select2({data:"",placeholder:"Select Subjects"});
		 $(".createExamSelectQuestionSubject").empty();
		 $(".createExamSelectQuestionSubject").append("<option value='-1'>Select Subject</option>");
		 $(".createExamSelectQuestionTopic").empty();
		 $(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
		if(inst_id != "-1"){
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		$("#division").empty();
		var divisionArray = [];
		var tempData = {};
 		tempData.id = "-1";
 		tempData.text = "Select Class";
 		divisionArray.push(tempData);
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 	 	for(i=0;i<divisionArray.length;i++){
 	 		$("#division").append("<option value='"+divisionArray[i].id+"'>"+divisionArray[i].text+"</option>")
 	 	}
	   if(divisionArray.length == 1){
		   $("#division").empty();
			 $("#division").select2().val("").change();
			 $("#division").select2({data:"",placeholder:"Class not available"});
	   }else{
		   $("#division").select2().val("-1").change();
	   }
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}else{
			$("#division").empty();
			 $("#division").select2().val("").change();
			 $("#division").select2({data:"",placeholder:"Select Class"});
		}

	});
	
	$("#patternType").change(function(){
		patternType = $("#patternType").val();
		 wosSubjectID = "";
		 wosTopicID = "";
		$("#examPattern").empty();
		$("#actionOptions").hide();
		if(patternType == "WS"){
		$("#withSectionOptions").show();
		$("#withoutSectionOptions").hide();
		}else if(patternType == "WOS"){
		$("#withoutSectionOptions").show();
		$("#withSectionOptions").hide();
		}else{
			$("#withSectionOptions").hide();
			$("#withoutSectionOptions").hide();
		}
	});
	
	$("body").on("click","#addSection",addSection).
		on("click",ADD_ITEM,addNewType).
		on("click",GENERATE,generatePattern).
		on("click",DELETE_ITEM,deleteItem).
		on("click",ALTERNATE_ITEM,alternateItem).
		on("click","#previewExamPattern",previewPattern);
	
	$("#division").change(function(){
		var inst_id = $("#instituteSelect").val();
		var division = $("#division").val();
		 $(".createExamSelectQuestionSubject").empty();
		 $(".createExamSelectQuestionSubject").append("<option value='-1'>Select Subject</option>");
		 $(".createExamSelectQuestionTopic").empty();
		 $(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
		if(division != "-1" && division != "" && division != null){
		var handler = {};
		handler.success = function(data){console.log("Success",data);
		 wosSubjectID = "";
		   wosTopicID = "";
		   $("#subject").empty();
		   if(data.length > 0 ){
		   $("#subject").append("<option value='-1'>Select Subject</option>");
		   $("#subject").select2().val("-1").change();
			   var i = 0;  
				subjectList = [];
			   while(i < data.length){
				   if(data[i].sub_type == "1"){
				   		$("#subject").append("<option class='combineSub' value='"+data[i].subjectId+"'>"+data[i].subjectName+"</option>");
				   		}else{
				   		$("#subject").append("<option class='singleSub' value='"+data[i].subjectId+"'>"+data[i].subjectName+"</option>");
				   		}
			   		i++;
			   }
		   }else{
			   $("#subject").empty();
			   $("#subject").select2().val("").change();
			   $("#subject").select2({data:"",placeholder:"Subjects not available"}); 
		   }
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getSubjectOfDivisionForExam/"+inst_id+"/"+division,handler);
		}else{
			 $("#subject").empty();
			 $("#subject").select2().val("").change();
			 $("#subject").select2({data:"",placeholder:"Select Subjects"});
			
		}
		});
	
	$("#subject").change(function(){
		var inst_id = $("#instituteSelect").val();
		subjectList = [];
		if(this.value != "-1" && this.value != "" && this.value != null ){
		if ($(this).find('option:selected').prop("class") == "singleSub"){
			subjectList = [];
			var subjectObj = {};
		   	subjectObj.id = this.value;
		   	subjectObj.name = 	$(this).find('option:selected').text();
		   	subjectList.push(subjectObj);
			$(".createExamSelectQuestionSubject").empty();
			$(".createExamSelectQuestionSubject").append("<option value='"+this.value+"'>"+$(this).find('option:selected').text()+"</option>");
			getTopics(this.value,this,"allChange");
			
		}else if ($(this).find('option:selected').prop("class") == "combineSub"){
			var handlers = {};
			handlers.success = function(data){	
				$(".createExamSelectQuestionTopic").empty();
				$(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
				$(".createExamSelectQuestionSubject").empty();
				$(".createExamSelectQuestionSubject").append("<option value='-1'>Select Subject</option>");
				subjectList = [];
				var subjectObj = {};
			   	subjectObj.id = "-1";
			   	subjectObj.name = 	"Select Subject";
				subjectList.push(subjectObj);
			   	if(data != null){
			   		$.each(data,function(index,subject){
			   			var subjectInnerObj = {};
			   			subjectInnerObj.id = subject.subjectId;
			   			subjectInnerObj.name = 	subject.subjectName;
						subjectList.push(subjectInnerObj);
						$(".createExamSelectQuestionSubject").append("<option value='"+subject.subjectId+"'>"+subject.subjectName+"</option>");
			   		});
			   	}
			};
			handlers.error = function(){};
			rest.get("rest/teacher/getCombineSubjects/"+inst_id+"/"+this.value,handlers);
		}
		}else{
			$(".createExamSelectQuestionTopic").empty();
			$(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
			$(".createExamSelectQuestionSubject").empty();
			$(".createExamSelectQuestionSubject").append("<option value='-1'>Select Subject</option>");
		}
	});
	
	
	$("#examPattern").on("change",".createExamSelectQuestionSubject",function(){
		if($(this).closest("ul").attr("data-item-type") != "Section"){
		var subID = $(this).val();
		if(patternType == "WOS"){
		wosSubjectID = subID;
		}
		getTopics(subID,$(this));
		}
	});
	
	$("#examPattern").on("change",".createExamSelectQuestionTopic",function(){
		var topicID = $(this).val();
		if(patternType == "WOS"){
			wosTopicID = topicID;
		}
	});
	
	$("#addWOS").click(function(){
		var section = getItem(undefined,$("#wosSelect").val());
		$("#examPattern").append("<li>"+section.prop("outerHTML")+"</li>");
		if($("#examPattern").children().length > 0){
			$("#actionOptions").show();
		}
	});
	
	$("#examPattern").on("click",".sectionCheckbox",function(){
		if(this.checked == true){
			var selectedSubject = $(this).closest(".sectionInfo").find(".createExamSelectQuestionSubject").val();
			$(this).closest("ul").find(".createExamSelectQuestionSubject").val(selectedSubject);
			getTopics(selectedSubject,$(this).closest("ul"),"sectionSubject");
		}
		
	});
	
	$("#examPattern").on("change",".createExamSelectQuestionSubject",function(){
		if($(this).closest("ul").attr("data-item-type")=="Section" && $(this).closest(".sectionInfo").find(".sectionCheckbox:checked").val() == "true"){
			var selectedSubject = $(this).val();
			$(this).closest("ul").find(".createExamSelectQuestionSubject").val(selectedSubject);
			getTopics(selectedSubject,$(this).closest("ul"),"sectionSubject");
		}
	});
	
});

function getTopics(subID,that,scenario){
	topic_names="";
	topic_ids="";
	if(subID != ""){
	 var divisionID = $("#division").val();
		var inst_id = $("#instituteSelect").val();
		var handler = {};
		handler.success = function(e){console.log("Success",e);
		var topic_idsArray = [];
		var topic_namesArray = []
	 $.each(e,function(key,val){
			
		 topic_idsArray.push(val.topic_id);
		 topic_namesArray.push(val.topic_name);
			
		});
		topic_ids =topic_idsArray.join(",").split(",");
		topic_names =topic_namesArray.join(",").split(",");
	   if(scenario == "sectionSubject"){
		   $(that).find(".createExamSelectQuestionTopic").empty();
		   $(that).find(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
		   if(topic_names.length > 0 ){
			   var i=0;
			   while(i<topic_names.length){
				   if(topic_names[i] != ""){
					   $(that).find(".createExamSelectQuestionTopic").append("<option value='"+topic_ids[i]+"'>"+topic_names[i]+"</option>");
				   }
				   i++;						   
			   }
		   }   
	   }else if(scenario == "allChange"){
		   $(".createExamSelectQuestionTopic").empty();
		   $(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
		   if(topic_names.length > 0 ){
			   var i=0;
			   while(i<topic_names.length){
				   if(topic_names[i] != ""){
					   $(".createExamSelectQuestionTopic").append("<option value='"+topic_ids[i]+"'>"+topic_names[i]+"</option>");
				   }
				   i++;						   
			   }
		   }    
	   }else{
	   $(that).closest(".createExamPatternItem").first().find(".createExamSelectQuestionTopic").empty();
	   $(that).closest(".createExamPatternItem").first().find(".createExamSelectQuestionTopic").append("<option value='-1'>Select Topic</option>");
	   if(topic_names.length > 0 ){
		   var i=0;
		   while(i<topic_names.length){
			   if(topic_names[i] != ""){
				   $(that).closest(".createExamPatternItem").first().find(".createExamSelectQuestionTopic").append("<option value='"+topic_ids[i]+"'>"+topic_names[i]+"</option>");
			   }
			   i++;						   
		   }
	   }
	   }
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getDivisionsTopics/"+inst_id+"/"+divisionID+"/"+subID,handler,true,false);
	
	}
}

var addSection = function(){
	var section = getItem(undefined,enumData.addSection);
	$("#examPattern").append("<li>"+section.prop("outerHTML")+"</li>");
	if($("#examPattern").children().length > 0){
		$("#actionOptions").show();
	}
};

var deleteItem = function(){
	if($(this).closest('li').next().hasClass("ORClass") && $(this).closest('li').prev().hasClass("ORClass")){
		$(this).closest('li').next().remove();
	}else if($(this).closest('li').next().hasClass("ORClass")){
		$(this).closest('li').next().remove();
	}else if($(this).closest('li').prev().hasClass("ORClass")){
		$(this).closest('li').prev().remove();
	}
	$(this).closest('li').remove();
	if($("#examPattern").children().length == 0){
		$("#actionOptions").hide();
	}
};

var generatePattern = function(){
	var inst_id = $("#instituteSelect").val();
	verifyPattern();
	if(errorFlag == false){
	var alternateValue = 0;
	var preAlternateValue = 0;
	var questionPaperStructure = [];
	var patternType = $("#patternType").val();
	$.each($(".sectionUl"),function(key,elm){
		var itemType = $(elm).attr("data-item-type");
		var itemId = $(elm).attr(ID);
		var parentId = $(elm).attr(PARENT_ID);
		var itemString = $($(elm).find(".sectionInfo")).find(ITEM_NAME).val();
		var itemMarks = $($(elm).find(".sectionInfo")).find(ITEM_MARKS).val();
		var subject = $($(elm).find(".sectionInfo")).find(".createExamSelectQuestionSubject").val();
		
		var examPatternObject = new ExamPatternObject();
		examPatternObject.item_type = itemType;
		examPatternObject.item_id = itemId;
		examPatternObject.parent_id = parentId;
		examPatternObject.item_marks = itemMarks;
		examPatternObject.item_description = itemString;
		examPatternObject.item_no = "-1";
		examPatternObject.question_type = "-1";
		examPatternObject.question_topic = "-1";
		if($(elm).closest("li").prev("li").hasClass("ORClass"))
		{
			preAlternateValue = $($(elm).closest("li").prev("li")).prev("li").find("ul").attr("alternateValue");
			examPatternObject.alternate_value = preAlternateValue;
			$(elm).attr("alternateValue",preAlternateValue);
		}else if($(elm).closest("li").next("li").hasClass("ORClass"))
		{
			$(elm).attr("alternateValue",++alternateValue);
			examPatternObject.alternate_value = alternateValue;
		}else{
			$(elm).attr("alternateValue",0);
			examPatternObject.alternate_value = 0;
		}
		examPatternObject.subject_id = subject;
		questionPaperStructure.push(examPatternObject);
		console.log(questionPaperStructure);
		return questionPaperStructure;
	});
	
	preAlternateValue = 0;
	$.each($(PATTERN_ITEM),function(key,elm){
		var itemType = $(elm).attr("data-item-type");
		var itemId = $(elm).attr(ID);
		var parentId = $(elm).attr(PARENT_ID);
		var itemString = $(elm).find(ITEM_NAME).val();
		var itemMarks = $(elm).find(ITEM_MARKS).val();
		var questionNo = $(elm).find("#questionNo").val();
		var createExamSelectQuestionType = $(elm).find("#createExamSelectQuestionType").val();
		var createExamSelectQuestionTopic = $(elm).find("#createExamSelectQuestionTopic").val();
		var subject = $(elm).find("#createExamSelectQuestionSubject").val();	
		var examPatternObject = new ExamPatternObject();
		examPatternObject.item_type = itemType;
		examPatternObject.item_id = itemId;
		examPatternObject.parent_id = parentId;
		examPatternObject.item_marks = itemMarks;
		examPatternObject.item_description = itemString;
		examPatternObject.question_type = createExamSelectQuestionType;
		examPatternObject.question_topic = createExamSelectQuestionTopic;
		examPatternObject.item_no = questionNo;
		
		if($(elm).closest("li").prev("li").hasClass("ORClass"))
		{
			preAlternateValue = $($(elm).closest("li").prev("li")).prev("li").find("ul").attr("alternateValue");
			examPatternObject.alternate_value = preAlternateValue;
			$(elm).attr("alternateValue",preAlternateValue);
		}else if($(elm).closest("li").next("li").hasClass("ORClass"))
		{
			$(elm).attr("alternateValue",++alternateValue);
			examPatternObject.alternate_value = alternateValue;
		}else{
			$(elm).attr("alternateValue",0);
			examPatternObject.alternate_value = 0;
		}
		examPatternObject.subject_id = subject;
		questionPaperStructure.push(examPatternObject);
		console.log(questionPaperStructure);
		return questionPaperStructure;
	});
	var handlers = {};
	handlers.success = function(e){if(e==false){
		$.notify({message: "Pattern with same name already available"},{type: 'danger'});
	}else {
		$("#patternSavedNotification").modal("toggle");}
	}
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
	var QuestionPaperPattern = {};
	QuestionPaperPattern.pattern_name = $("#examPatternName").val();
	QuestionPaperPattern.class_id = $("#division").val();
	QuestionPaperPattern.sub_id = $("#subject").val();
	QuestionPaperPattern.marks = $("#totalMarks").val();
	QuestionPaperPattern.inst_id = inst_id;
	QuestionPaperPattern.pattern_type = patternType;
	QuestionPaperPattern.questionPaperStructure = questionPaperStructure;
	var QuestionPaperPatternJson = JSON.stringify(QuestionPaperPattern);
	rest.post("rest/teacher/addQuestionPaperPattern",handlers,QuestionPaperPatternJson,false);
	
	}
}

//This is to add alternate the section, question or instruction
var alternateItem = function(){
	var thisParentId = $(this).closest(".createExamPatternItem").attr("data-parent-id");
	var itemType = $(this).closest("ul").find("#createExamSelectAlternateQuestion").val();
	var section = getItem(thisParentId,itemType,$(this));
	var typeItem = $("<li/>").append(section);
	$($(this).closest("ul")).closest("li").after(typeItem);
	$($(this).closest("ul")).closest("li").after("<li align='center' style='padding-bottom:1%' class='ORClass'><span class='badge'>OR<span></li>");
}


//This is to add the section, question or 
var addNewType = function(){
	var thisParentId = $(this).closest(".createExamPatternItem").attr(ID);
	if( thisParentId == undefined){
		thisParentId = $(this).closest(".sectionUl").attr(ID);
	}
	var itemType = $(this).closest("ul").find("#createExamSelectQuestionType").val();
	var section = getItem(thisParentId,itemType,$(this));
	var typeItem = $("<li/>").append(section);
	$(this).closest("ul").append(typeItem);
}

//
function getItem(parent,itemType,that){
	var dataEnum = {addSection:"Section",addInstruction:"Instruction",addQuestion:"Question"};	
	var selectedSectionSubjectCheckbox = "";
	var selectedSectionSubject = "";
	if(itemType != "addSection" && $("#patternType").val() == "WS"){
		selectedSectionSubjectCheckbox=that.closest(".sectionUl").find("#sectionCheckbox:checked").val()
		if(selectedSectionSubjectCheckbox == "true"){
			selectedSectionSubject=that.closest(".sectionUl").first().find("#createExamSelectQuestionSubject").val();
		}
		if ($("#subject").find('option:selected').prop("class") != "singleSub"){
		getTopics(selectedSectionSubject,that);
		}
	}
	var section = $("<ul></ul>",{
		class:"createExamPatternItem",
		"data-item-type":dataEnum[itemType]
	});
	var questionNo = "";
	if(itemType == "addSection"){
		section = $("<ul></ul>",{
			class:"sectionUl",
			"data-item-type":dataEnum[itemType]
		});
	}else{
		questionNo = "<div class='col-md-1'><input type='text' id='questionNo' placeholder='Que No' class='form-control'/><span id='questionNoError' class='patternError'></div>";
	}
	var sectionNameInput = "";
	if(itemType != "addQuestion"){
	sectionNameInput = "<div class='col-md-2'><input type='text' id='createExamItemName' placeholder='"+dataEnum[itemType]+" Description' class='form-control'/><span id='descriptionError' class='patternError'></div>";
	}
	var sectionMarksInput = "<div class='col-md-1'><input type='number' id='createExamItemMarks' placeholder='Marks' class='form-control'/><span id='marksError' class='patternError'></div>";
/* 	var select = $("<select class='form-control'>",{
		id:"createExamSelectQuestionType"
	}); */
	var select = "";
		select = $("<div class='col-md-9'><select class='form-control' id='createExamSelectQuestionType'>"+
				"<option value='addInstruction'>Add instruction</option>"+
				"<option value='addQuestion'>Add question</option></select></div>");

		//select.html(options);		
	var div = $("<div class='col-md-3'></div>");
	var htmlSelect = select.prop("outerHTML");
	
	var alternateSelect = "";
	if(itemType == "addSection"){
		 alternateSelect = $("<div class='col-md-7'><select class='form-control' id='createExamSelectAlternateQuestion'><option value='addSection'>Add section</option>"+
					"<option value='addInstruction'>Add instruction</option>"+
					"<option value='addQuestion'>Add question</option></select></div>");
	}else{
		 alternateSelect = $("<div class='col-md-7'><select class='form-control' id='createExamSelectAlternateQuestion'>"+
					"<option value='addInstruction'>Add instruction</option>"+
					"<option value='addQuestion'>Add question</option></select></div>");
	}		
		var htmlalternateSelect = alternateSelect.prop("outerHTML");
		var alternate = "<div class='col-md-1'><input type='button' value='Alternate' id='createExamAddAlternateItem' class='btn btn-primary btn-xs'/></div>"
	
		var questionTypeSelect = $("<div class='col-md-2'><select class='form-control' id='createExamSelectQuestionType'><option value='-1'>Question Type</option>"+
			"<option value='1'>Subjective</option>"+
			"<option value='2'>Objective</option><option value='3'>Paragraph</option></select></div>");
		var questionTypeSelectHtml = questionTypeSelect.prop("outerHTML");
		var topicSelect = "";
		var i=0;
		if(topic_names.length > 0){
		var topicSelectString = "<div class='col-md-2'><select class='form-control createExamSelectQuestionTopic' id='createExamSelectQuestionTopic'><option value='-1'>Select Topic</option>";
		while(i < topic_names.length)	
		{
			if((wosTopicID != "" && wosTopicID==topic_ids[i] && $("#patternType").val() == "WOS")){
			topicSelectString = topicSelectString + "<option value='"+topic_ids[i]+"' selected>"+topic_names[i]+"</option>";
			}else{
				topicSelectString = topicSelectString + "<option value='"+topic_ids[i]+"'>"+topic_names[i]+"</option>";
			}
			i++;
		}
		topicSelectString = topicSelectString +	"</select></div>";
		topicSelect = $(topicSelectString);
		}else{
			var topicSelectString = "<div class='col-md-2'><select class='form-control createExamSelectQuestionTopic' id='createExamSelectQuestionTopic'><option value='-1'>Select Topic</option></select></div>";
			topicSelect = $(topicSelectString);
		}	
		
		var topicSelectHtml = topicSelect.prop("outerHTML");
	
		var subjectSelect = "";
		i =0;
		if(subjectList.length > 0){
			var subjectSelectString = "<div class='col-md-2'><select class='form-control createExamSelectQuestionSubject' id='createExamSelectQuestionSubject'>";
			while(i < subjectList.length)	
			{
				if((selectedSectionSubject != "" && selectedSectionSubject != "-1" && selectedSectionSubject == subjectList[i].id) || (wosSubjectID != "" && wosSubjectID==subjectList[i].id && $("#patternType").val() == "WOS")){
					subjectSelectString = subjectSelectString + "<option value='"+subjectList[i].id+"' selected>"+subjectList[i].name+"</option>";
				}else{
				subjectSelectString = subjectSelectString + "<option value='"+subjectList[i].id+"'>"+subjectList[i].name+"</option>";
				}
				i++;
			}
			subjectSelectString = subjectSelectString +	"</select></div>";
			subjectSelect = $(subjectSelectString);
			}else{
				var subjectSelectString = "<div class='col-md-2'><select class='form-control createExamSelectQuestionSubject' id='createExamSelectQuestionSubject'><option value='-1'>Select Subject</option></select></div>";
				subjectSelect = $(subjectSelectString);
			}	
		var subjectSelectHtml = subjectSelect.prop("outerHTML");
	var add = "<div class='col-md-1' style='font-size:12px'><input type='button' value='Add' id='createExamAddItem' class='btn btn-primary btn-xs'/></div>"
	var deleteSection = "<div class='col-md-1'><input type='button' value='Delete "+dataEnum[itemType]+"' id='createExamDeleteItem' class='btn btn-danger btn-xs'/></div>"
	var subjectCheckbox = "<div class='col-md-3' style='font-size:12px'><input type='checkbox' class='sectionCheckbox' id='sectionCheckbox' value='true'>Use this subject throughout this Section </div>";
	if(itemType == "addSection"){
		section.html('<div class="row sectionBackground sectionInfo">'+sectionNameInput+subjectSelectHtml+sectionMarksInput+'<div class="col-md-2 well">'+htmlSelect+add+'</div>'+'<div class="col-md-2 well">'+htmlalternateSelect+alternate+'</div>'+deleteSection+subjectCheckbox+"</div>");
	}else if(itemType == "addInstruction"){
		section.html('<div class="row">'+questionNo+sectionNameInput+sectionMarksInput+'<div class="col-md-2 well">'+htmlSelect+add+'</div>'+'<div class="col-md-2 well">'+htmlalternateSelect+alternate+'</div>'+deleteSection+"</div>");
	}else{
	section.html('<div class="row">'+questionNo+sectionNameInput+sectionMarksInput+questionTypeSelectHtml+subjectSelectHtml+topicSelectHtml+'<div class="col-md-2 well">'+htmlalternateSelect+alternate+'</div>'+deleteSection+"</div>");
	}
	section.attr(PARENT_ID,parent);
	
	var randomId = Math.random();
	section.attr(ID,randomId);
	return section;
}

function verifyPattern(){
	errorFlag = false;
	$(".patternError").empty();
	if($("#totalMarks").val().trim() == ""){
		$("#totalMarksError").html("Enter Marks");
		errorFlag = true;
	}
	if($("#examPatternName").val().trim() == ""){
		$("#examPatternNameError").html("Enter Name");
		errorFlag = true;
	}
	
	if($("#instituteSelect").val() == "-1" || $("#instituteSelect").val() == "" || $("#instituteSelect").val() == null){
		$("#instituteError").html("Select Institute");
		errorFlag = true;
	}
	
	if($("#division").val() == "-1" || $("#division").val() == "" || $("#division").val() == null){
		$("#divisionError").html("Select Class");
		errorFlag = true;
	}
	if($("#subject").val() == "-1" || $("#subject").val() == "" || $("#subject").val() == null){
		$("#subjectError").html("Select Subject");
		errorFlag = true;
	}
	
	var recursionLevel = 0;
	var sectionCount = $("#examPattern").children().length;
	var counter = 0;
	while(counter < sectionCount){
		recursiveVerify($("#examPattern").children()[counter],recursionLevel);
		counter++;
	}
}

function recursiveVerify(element,recursionLevel){
	if($(element).hasClass("ORClass")){
		$("#previewModal").find(".modal-body").append("<div align='center'>OR</div>")
	}else{
	var innerElement = $(element).find("ul");
	var itemType = innerElement.attr("data-item-type");
	if(itemType == "Section"){
		var itemName = innerElement.find("#createExamItemName").val().trim();
		if(itemName == ""){
			innerElement.find("#descriptionError").first().html("Enter Section Description");
			errorFlag = true;
		}
	}else if(itemType == "Instruction"){
		var itemName = innerElement.find("#createExamItemName").val();
		var itemNo = innerElement.find("#questionNo").val();
		var itemMarks = innerElement.find("#createExamItemMarks").val();
		if(itemName == ""){
			innerElement.find("#descriptionError").html("Enter Instruction");
			errorFlag = true;
		}
		if(itemNo == ""){
			innerElement.find("#questionNoError").html("Enter Question No");
			errorFlag = true;
		}
		if(itemMarks == ""){
			innerElement.find("#marksError").html("Enter Marks");
			errorFlag = true;
		}
	}else{
		var itemName = innerElement.find("#createExamItemName").val();
		var itemNo = innerElement.find("#questionNo").val();
		var itemMarks = innerElement.find("#createExamItemMarks").val();
		if(itemName == ""){
			innerElement.find("#descriptionError").html("Enter Instruction");
			errorFlag = true;
		}
		if(itemNo == ""){
			innerElement.find("#questionNoError").html("Enter Question No");
			errorFlag = true;
		}
		if(itemMarks == ""){
			innerElement.find("#marksError").html("Enter Marks");
			errorFlag = true;
		}
	}
	recursionLevel++;
	var sectionCount = innerElement.first().children().not('div').length;
	var counter = 0;
	while(counter < sectionCount){
		recursiveVerify(innerElement.first().children().not('div')[counter],recursionLevel);
		counter++;
	}
	}
}

function ExamPatternObject(){
		//OrId will have same id for ored question
		this.orId;
		this.ojectType;
		this.questionId;
		this.parentId;
		this.marks;
		this.itemString;
		this.tag;
}
</script>
</head>
<body>
	<jsp:include page="TeacherExamHeader.jsp" >
		<jsp:param value="active" name="createExamPatten"/>
	</jsp:include>
	<div class="container" style="padding: 2%; background: #eee">
		<div class="row">
			<div class="col-md-3">
				<select name="instituteSelect" id="instituteSelect" class="form-control" width="100px">
					<option value="-1">Select Institute</option>
					<c:forEach items="${requestScope.registerBeanList}" var="institute">
						<option value="<c:out value="${institute.regId}"></c:out>"><c:out value="${institute.className}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="instituteError" class="patternError"></span>
			</div>
			<div class="col-md-3">
				<select id="division" name="division" class="form-control">
					<option value="-1">Select Class</option>
				</select>
				<span id="divisionError" class="patternError"></span>
			</div>
			<div class="col-md-3">
				<select id="subject" name="subject" class="form-control">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectError" class="patternError"></span>
			</div>
			<div class="col-md-3">
				<select id="patternType" name="patternType" class="form-control">
					<option value="-1">Select Pattern Type</option>
					<option value="WS">Pattern With Section</option>
					<option value="WOS">Pattern Without Section</option>
				</select>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<input type="text" id="examPatternName" name="examPatternName"
					placeholder="Exam Pattern Name" class="form-control">
					<span id="examPatternNameError" class="patternError"></span>
			</div>
			<div class="col-md-2">
				<input type="number" placeholder="Total Marks" id="totalMarks"
					name="totalMarks" min="0" class="form-control">
				<span id="totalMarksError" class="patternError"></span>	
			</div>
		</div>
	</div>
	
<div id="withSectionOptions" style="display: none;" class="container" align="center">	
<input id="addSection" value="Add Section" type="button" class="btn btn-primary btn-sm">
</div>
<div id="withoutSectionOptions" style="display: none;" class="container" align="center">	
<div class="row">
<div class=" col-md-offset-4 col-md-2">
<select class="form-control" id="wosSelect">
<option value="addInstruction">Add Instrction</option>
<option value="addQuestion">Add Question</option>
</select>
</div>
<div class="col-md-2"><button class="btn btn-primary btn-xs" id="addWOS">Add</button></div>
</div>
</div>
<ul id="examPattern" style="padding: 0%"></ul>

<div id="actionOptions" class="container" style="display: none;">
<input id="createExamGenerate" value="Generate" type="button" class="btn btn-primary btn-sm">
<input id="previewExamPattern" value="Preview" type="button" class="btn btn-primary btn-sm">
</div>
<div class="modal fade" id="previewModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">�
            </button>
            <h4 class="modal-title" id="myModalLabel" align="center">
               Preview
            </h4>
         </div>
         <div class="modal-body">
          </div>
         </div>
   </div>
</div>
<div class="modal fade" id="patternSavedNotification" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">�
            </button>
            <h4 class="modal-title" id="myModalLabel" align="center">
               Pattern
            </h4>
         </div>
         <div class="modal-body">
         Pattern saved successfully.
          </div>
         </div>
   </div>
</div>
</body>
</html>