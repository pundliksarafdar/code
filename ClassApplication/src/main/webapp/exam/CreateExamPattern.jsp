<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style>
ul {
    list-style-type: none;
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
</style>
<script src="jquery-1.10.2.min.js"></script>
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
	
var enumData = {addSection:"addSection",addInstruction:"addInstruction",addQuestion:"addQuestion"};	

$(document).ready(function(){
	$("body").on("click","#addSection",addSection).
		on("click",ADD_ITEM,addNewType).
		on("click",GENERATE,generatePattern).
		on("click",DELETE_ITEM,deleteItem).
		on("click",ALTERNATE_ITEM,alternateItem);
	
});

var addSection = function(){
	var section = getItem(undefined,enumData.addSection);
	$("#examPattern").append("<li>"+section.prop("outerHTML")+"</li>");
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
};

var generatePattern = function(){
	var alternateValue = 0;
	var preAlternateValue = 0;
	var patternItem = [];
	$.each($(".sectionUl"),function(key,elm){
		var itemType = $(elm).attr("data-item-type");
		var itemId = $(elm).attr(ID);
		var parentId = $(elm).attr(PARENT_ID);
		var itemString = $($(elm).find(".sectionInfo")).find(ITEM_NAME).val();
		var itemMarks = $($(elm).find(".sectionInfo")).find(ITEM_MARKS).val();
		
		var examPatternObject = new ExamPatternObject();
		examPatternObject.ojectType = itemType;
		examPatternObject.questionId = itemId;
		examPatternObject.parentId = parentId;
		examPatternObject.marks = itemMarks;
		examPatternObject.itemString = itemString;
		if($(elm).closest("li").next("li").hasClass("ORClass"))
		{
			if(preAlternateValue != 0){
			examPatternObject.alternateValue = preAlternateValue;
			}else{
				examPatternObject.alternateValue = ++alternateValue;
				preAlternateValue = alternateValue;
			}
		}else if($(elm).closest("li").prev("li").hasClass("ORClass"))
		{
			if(preAlternateValue != 0){
			examPatternObject.alternateValue = preAlternateValue;
			}else{
				examPatternObject.alternateValue = ++alternateValue;
				preAlternateValue = alternateValue;
			}
		}else{
			preAlternateValue = 0;
			examPatternObject.alternateValue = 0;
		}
		patternItem.push(examPatternObject);
		console.log(patternItem);
		return patternItem;
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
		
		var examPatternObject = new ExamPatternObject();
		examPatternObject.ojectType = itemType;
		examPatternObject.questionId = itemId;
		examPatternObject.parentId = parentId;
		examPatternObject.marks = itemMarks;
		examPatternObject.itemString = itemString;
		examPatternObject.questionType = createExamSelectQuestionType;
		examPatternObject.questionTopic = createExamSelectQuestionTopic;
		examPatternObject.questionNo = questionNo;
		
		if($(elm).closest("li").next("li").hasClass("ORClass"))
		{
			if(preAlternateValue != 0){
			examPatternObject.alternateValue = preAlternateValue;
			}else{
				examPatternObject.alternateValue = ++alternateValue;
				preAlternateValue = alternateValue;
			}
		}else if($(elm).closest("li").prev("li").hasClass("ORClass"))
		{
			if(preAlternateValue != 0){
			examPatternObject.alternateValue = preAlternateValue;
			}else{
				examPatternObject.alternateValue = ++alternateValue;
				preAlternateValue = alternateValue;
			}
		}else{
			preAlternateValue = 0;
			examPatternObject.alternateValue = 0;
		}
		
		patternItem.push(examPatternObject);
		console.log(patternItem);
		return patternItem;
	});
}

//This is to add alternate the section, question or instruction
var alternateItem = function(){
	var thisParentId = $(this).closest(".createExamPatternItem").attr("data-parent-id");
	var itemType = $(this).closest("ul").find("#createExamSelectAlternateQuestion").val();
	var section = getItem(thisParentId,itemType);
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
	var section = getItem(thisParentId,itemType);
	var typeItem = $("<li/>").append(section);
	$(this).closest("ul").append(typeItem);
}

//
function getItem(parent,itemType){
	var dataEnum = {addSection:"Section",addInstruction:"Instruction",addQuestion:"Question"};	
	
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
		questionNo = "<div class='col-md-1'><input type='text' id='questionNo' placeholder='Que No' class='form-control'/></div>";
	}
	var sectionNameInput = "<div class='col-md-2'><input type='text' id='createExamItemName' placeholder='"+dataEnum[itemType]+" Description' class='form-control'/></div>";
	var sectionMarksInput = "<div class='col-md-1'><input type='number' id='createExamItemMarks' placeholder='Marks' class='form-control'/></div>";
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
		var topicSelect = $("<div class='col-md-2'><select class='form-control' id='createExamSelectQuestionTopic'><option value='-1'>Select Topic</option>"+
				"</select></div>");	
		var topicSelectHtml = topicSelect.prop("outerHTML");
	
	var add = "<div class='col-md-1'><input type='button' value='Add' id='createExamAddItem' class='btn btn-primary btn-xs'/></div>"
	var deleteSection = "<div class='col-md-1'><input type='button' value='Delete "+dataEnum[itemType]+"' id='createExamDeleteItem' class='btn btn-danger btn-xs'/></div>"
	if(itemType == "addSection"){
		section.html('<div class="row sectionBackground sectionInfo">'+sectionNameInput+sectionMarksInput+'<div class="col-md-2 well">'+htmlSelect+add+'</div>'+'<div class="col-md-2 well">'+htmlalternateSelect+alternate+'</div>'+deleteSection+"</div>");
	}else if(itemType == "addInstruction"){
		section.html('<div class="row">'+questionNo+sectionNameInput+sectionMarksInput+'<div class="col-md-2 well">'+htmlSelect+add+'</div>'+'<div class="col-md-2 well">'+htmlalternateSelect+alternate+'</div>'+deleteSection+"</div>");
	}else{
	section.html('<div class="row">'+questionNo+sectionNameInput+sectionMarksInput+questionTypeSelectHtml+topicSelectHtml+'<div class="col-md-2 well">'+htmlalternateSelect+alternate+'</div>'+deleteSection+"</div>");
	}
	section.attr(PARENT_ID,parent);
	
	var randomId = Math.random();
	section.attr(ID,randomId);
	return section;
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
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="active"><a href="#addstudenttab" data-toggle="tab">Create
				Exam Paper Pattern</a></li>
		<li><a href="viewstudent">View/Edit Exam Pattern</a></li>
		<li><a href="viewstudent">Set Question Paper</a></li>
		<li><a href="viewstudent">View/Edit Question paper</a></li>
		<li><a href="viewstudent">Create Exam</a></li>
		<li><a href="viewstudent">View/Edit Exam</a></li>
	</ul>
	<div class="container" style="padding: 2%; background: #eee">
		<div class="row">
			<div class="col-md-3">
				<input type="text" id="examPatternName" name="examPatternName"
					placeholder="Exam Pattern Name" class="form-control">
			</div>
			<div class="col-md-3">
				<select id="division" name="division" class="form-control">
					<option value="="-1">Select Class</option>
					<c:forEach items="${divisionList}" var="division">
						<option value="<c:out value="${division.divId }"></c:out>">
							<c:out value="${division.divisionName }"></c:out>
							<c:out value="${division.stream }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-3">
				<select id="subject" name="subject" class="form-control">
					<option value="-1">Select Subject</option>
				</select>
			</div>
			<div class="col-md-3">
				<input type="number" placeholder="Total Marks" id="totalMarks"
					name="totalMarks" min="0" class="form-control">
			</div>
		</div>
	</div>
<input id="addSection" value="Add Section" type="button" class="btn btn-primary">

<ul id="examPattern" style="padding: 0%"></ul>

<input id="createExamGenerate" value="Generate" type="button" class="btn btn-primary">
</body>
</html>