<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
<script type="text/javascript">
var subjectType = "";
var division  = "";
var subject  = "";
var editString = "";
var questionPaperPattern = {};
var subjectArray = [];
var topicMap = {}
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
function createPatternTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#patternListTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [{title:"#",data:null,render:function(data,event,row){
			return ++i;
			},sWidth:"10%"},
			{ title: "Pattern Name",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.pattern_name+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Marks",data:null,render:function(data,event,row){
				return row.marks;
			},sWidth:"20%"},
			{ title: "Edit",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary viewPattern" value="Edit" id="'+row.pattern_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"},
			{ title: "Delete",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-danger deletePattern" value="Delete" id="'+row.pattern_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	
}
$(document).ready(function(){
	$("#division").change(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").hide();
		$("#editPatternDiv").hide();
		var division = $("#division").val(); 
		if(division != "-1"){
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getSubjectOfDivisionForExam",
		    	 divisionId: division
		   		},
		   type:"POST",
		   success:function(data){
			   wosSubjectID = "";
			   wosTopicID = "";
			   $("#subject").empty();
			   $("#subject").append("<option value='-1'>Select Subject</option>");
			   data = JSON.parse(data);
			   if(data.status == "success"){
				   $("#subject").select2().val("-1").change();
				   var subjectnames = data.subjectnames;
				   var subjectIds = data.subjectids;
				   var subjectType = data.subjectType;
				   var i = 0;
				   var subjectnameArray = subjectnames.split(",");
					var subjectidArray =  subjectIds.split(",");  
					var subjectTypeArray = subjectType.split(",");  
				   while(i < subjectnameArray.length){
				   		if(subjectTypeArray[i] == "1"){
				   		$("#subject").append("<option class='combineSub' value='"+subjectidArray[i]+"'>"+subjectnameArray[i]+"</option>");
				   		}else{
				   		$("#subject").append("<option class='singleSub' value='"+subjectidArray[i]+"'>"+subjectnameArray[i]+"</option>");
				   		}
				   		i++;
				   }
			   }else{
				   $("#subject").empty();
					 $("#subject").select2().val("").change();
					 $("#subject").select2({data:"",placeholder:"Subjects not available"});
			   }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
		}else{
			 $("#subject").empty();
			 $("#subject").select2().val("").change();
			 $("#subject").select2({data:"",placeholder:"Select Subject"});			   
		}
	});
	
	 $("#subject").change(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").hide();
		$("#editPatternDiv").hide();
		subjectType = $(this).find('option:selected').prop("class");
		var handlers = {};
		handlers.success = function(e){
		console.log("Success",e);
		subjectArray = [];
		topicMap = e;
		for (var i in e) {
		    console.log('Key is: ' + i + '. Value is: ' + e[i]);
			subjectArray.push(e[i].subject);
		} 
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		division = $("#division").val();
		subject = $("#subject").val();
		if(subject != "-1" && subject != "" && subject != null){
		rest.post("rest/classownerservice/getSubjectsAndTopicsForExam/"+division+"/"+subject,handlers);
		}
	}); 
	$("#searchPattern").click(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").hide();
		$("#editPatternDiv").hide();
		$(".validation-message").html("");
		division = $("#division").val();
		subject = $("#subject").val();
		var validationFlag = false;
		var patternType = $("#patternType").val();
		if(division == "-1" || division == "" || division == null){
			$("#divisionError").html("Select Class");
			validationFlag = true;
		}
		
		if(subject == "-1" || subject == "" || subject == null){
			$("#subjectError").html("Select Subject");
			validationFlag = true;
		}
		if(validationFlag == false){
		var obj = {};
		/* obj.division = division;
		obj.patternType = patternType; */
		var handlers = {};
		handlers.success = function(e){
		console.log("Success",e)
		$("#patternListTable").find("tbody").empty();
		createPatternTable(e);
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").show();
		$("#editPatternDiv").hide();
		/* if(e.length > 0)
		{
			for(var i=0; i<e.length; i++){
			$("#patternListTable").find("tbody").append("<tr><td>"+(i+1)+"</td><td>"+e[i].pattern_name+"</td><td>"+e[i].marks+"</td><td><button class='btn btn-primary btn-xs viewPattern' id='"+e[i].pattern_id+"'>View/Edit</button></td><td><button class='btn btn-danger btn-xs deletePattern' id='"+e[i].pattern_id+"'>Delete</button></td>");
		}
		}else{
			$("#patternListTable").find("tbody").append("<tr><td colspan='5' align='center'>Paterns not available for selected criteria</td></tr>");
		} */	
		};
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/classownerservice/searchQuestionPaperPattern/"+division+"/"+subject+"/"+patternType,handlers,obj,false);
		}
	});
	
	$("#patternListTable").on("click",".viewPattern",function(){
		$("#viewPatternDiv").show();
		$("#patternListTableDiv").hide();
		var patternid = $(this).attr("id");
		var handlers = {};
		handlers.success = function(e){
		console.log("Success",e);
		$("#viewPatternData").empty();
		$("#viewPatternData").append("<div class='row' align='center' style='font-weight:bold'>"+e.pattern_name+"</div>")
		for(var i=0; i<e.questionPaperStructure; i++){
			e[i].status="";
		}
		questionPaperPattern = e;
		recursiveView(e.questionPaperStructure,0,e.questionPaperStructure);
		};
		handlers.error = function(e){
			$.notify({message: "Error"},{type: 'danger'});}
		editString = "";
		rest.post("rest/classownerservice/getQuestionPaperPattern/"+division+"/"+patternid,handlers);
	});
	
	$(".cancleView").click(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").show();
		$("#editPatternDiv").hide();
		$(".actionOptionWS").hide();
		$(".actionOptionWOS").hide();		
	});
	
	$(".editPattern").click(function(){
		$("#viewPatternDiv").hide();
		$("#editPatternDiv").show();
		$(".cancleEdit").show();
		$("#examPattern").empty();
		$("#examPatternName").val(questionPaperPattern.pattern_name);
		$("#totalMarks").val(questionPaperPattern.marks);
		$("#pattern_id").val(questionPaperPattern.pattern_id);
		alternateValueMap = [];
		if(questionPaperPattern.pattern_type == "WS"){
			$(".actionOptionWS").show();
		}else{
			$(".actionOptionWOS").show();
		}
		editString = recursiveEdit(questionPaperPattern.questionPaperStructure,questionPaperPattern.questionPaperStructure,editString,undefined,0);
		console.log(editString);
		$("#examPattern").append(editString);
	});
	
	$(".cancleEdit").click(function(){
		$("#viewPatternDiv").show();
		$(".actionOptionWS").hide();
		$(".actionOptionWOS").hide();
		$("#editPatternDiv").hide();
	});
	
	
	$("#patternType").change(function(){
		patternType = $("#patternType").val();
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
		on("click",ALTERNATE_ITEM,alternateItem);
	
	
	
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
		}else if($(this).closest("ul").attr("data-item-type")=="Question"){
			var subID = $(this).val();
			getTopics(subID,$(this));
		}
	});
	
	$("#patternListTable").on("click",".deletePattern",function(){
		var patternid = $(this).attr("id");
		var handlers = {};
		handlers.success = function(){
			$.notify({message: "Pattern successfuly deleted"},{type: 'success'});
			var patternType = $("#patternType").val();
			var obj = {};
			handlers.success = function(e){
			console.log("Success",e)
			$("#patternListTable").find("tbody").empty();
			createPatternTable(e);
			};
			handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
			rest.post("rest/classownerservice/searchQuestionPaperPattern/"+division+"/"+patternType,handlers,obj,false);
		};
		handlers.error = function(){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/classownerservice/deleteQuestionPaperPattern/"+division+"/"+patternid,handlers);
	});
	
});
var alternateValueMap = [];
function recursiveView(data,recursionLevel,dataArray){
	recursionLevel++;
	var preAlternateValue = 0;
	var currentAlternateValue = 0;
	if(data.length > 0){
		for(var i=0; i<data.length; i++){
			if(data[i].status != "viewed"){
			currentAlternateValue = data[i].alternate_value;
			if( currentAlternateValue != 0 ){
				if( currentAlternateValue  == alternateValueMap[recursionLevel] ){
					$("#viewPatternData").append("<div class='row' style='font-weight:bold;padding-left:"+recursionLevel+"%' align='center'>OR</div>")
				}
			}
			alternateValueMap[recursionLevel]=data[i].alternate_value;
			var itemType = data[i].item_type;
			if(itemType == "Section"){
				var itemName = data[i].item_description;
				$("#viewPatternData").append("<div align='center' style='font-weight:bold'>"+itemName+"</div>")
				data[i].status = "viewed";
			}else if(itemType == "Instruction"){
				var itemName = data[i].item_description;
				var itemNo = data[i].item_no;
				var itemMarks = data[i].item_marks;
				if(recursionLevel == 1 || recursionLevel==2){
				$("#viewPatternData").append("<div class='row' style='font-weight:bold'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-1'>"+itemMarks+"</div></div>")
				}else{
					$("#viewPatternData").append("<div class='row'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-1'>"+itemMarks+"</div></div>")
				}
				data[i].status = "viewed";
			}else{
				var itemName = data[i].item_description;
				var itemNo = data[i].item_no;
				var itemMarks = data[i].item_marks;
				if(recursionLevel == 1 || recursionLevel==2){
				$("#viewPatternData").append("<div class='row' style='font-weight:bold'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>Question</div><div class='col-md-1'>"+itemMarks+"</div></div>")
				}else{
					$("#viewPatternData").append("<div class='row'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>Question</div><div class='col-md-1'>"+itemMarks+"</div></div>")
				}
				data[i].status = "viewed";
			}
			var item_id = data[i].item_id;
			var newDataArray = [];
			for(var j=0; j<dataArray.length;j++){
			if( item_id == dataArray[j].parent_id ){
				newDataArray.push(dataArray[j]);
			}
			}
			if( newDataArray.length > 0){
				recursiveView(newDataArray,recursionLevel,dataArray)
			}
			
		}
	}
}
}

function recursiveEdit(questionPaperPatternArray,newQuestionPaperPatternArray,editString,parent_id,recursionLevel){
	++recursionLevel;
	var preAlternateValue = 0;
	var currentAlternateValue = 0;
	for(var i=0; i<newQuestionPaperPatternArray.length; i++){
		if(newQuestionPaperPatternArray[i].status != "edited"){
		var itemType = newQuestionPaperPatternArray[i].item_type;
		currentAlternateValue = newQuestionPaperPatternArray[i].alternate_value;
		if( currentAlternateValue != 0 ){
			if( currentAlternateValue  == alternateValueMap[recursionLevel] ){
				editString = editString +"<li class='row ORClass' style='font-weight:bold;padding-left:"+recursionLevel+"%' align='center'>OR</li>";
			}
		}
		alternateValueMap[recursionLevel]=newQuestionPaperPatternArray[i].alternate_value;
		if(itemType == "Section"){
			var item_id = newQuestionPaperPatternArray[i].item_id;
			var subject_id = newQuestionPaperPatternArray[i].subject_id;
			var itemMarks = newQuestionPaperPatternArray[i].item_marks;
			editString = editString + "<li><ul class='sectionUl' data-item-type='Section' data-item-id='"+item_id+"'>";
			var itemName = newQuestionPaperPatternArray[i].item_description;
			editString = editString + "<div class='row sectionBackground sectionInfo'>"+
			'<div class="col-md-2">'+
			'<input type="text" id="createExamItemName" placeholder="Section Description" class="form-control" value="'+itemName+'">'+
			'<span id="descriptionError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-2">'+
			'<select class="form-control createExamSelectQuestionSubject" id="createExamSelectQuestionSubject">';
			if(subjectArray.length > 0){
				if(subjectType == "combineSub"){
				editString = editString + '<option value="-1">Select Subject</option>';
				}
			for(var k=0;k<subjectArray.length;k++){
				if(subjectArray[k].subjectId == subject_id){
					editString = editString + '<option value="'+subjectArray[k].subjectId+'" selected>'+subjectArray[k].subjectName+'</option>';	
				}else{
				editString = editString + '<option value="'+subjectArray[k].subjectId+'">'+subjectArray[k].subjectName+'</option>';
				}
			}
			}else{
				
			}
			editString = editString + '</select>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="number" id="createExamItemMarks" placeholder="Marks" class="form-control" value="'+itemMarks+'">'+
			'<span id="marksError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-2 well">'+
			'<div class="col-md-9">'+
				'<select class="form-control" id="createExamSelectQuestionType">'+
					'<option value="addInstruction">Add instruction</option>'+
					'<option value="addQuestion">Add question</option>'+
				'</select>'+
			'</div>'+
 			'<div class="col-md-1" style="font-size:12px">'+
				'<input type="button" value="Add" id="createExamAddItem" class="btn btn-primary btn-xs">'+
				'</div>'+
			'</div>'+
			'<div class="col-md-2 well">'+
			'<div class="col-md-7">'+
				'<select class="form-control" id="createExamSelectAlternateQuestion">'+
					'<option value="addSection">Add section</option>'+
					'<option value="addInstruction">Add instruction</option>'+
					'<option value="addQuestion">Add question</option>'+
				'</select>'+
			'</div>'+
			'<div class="col-md-1">'+
				'<input type="button" value="Alternate" id="createExamAddAlternateItem" class="btn btn-primary btn-xs">'+
				'</div>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="button" value="Delete Section" id="createExamDeleteItem" class="btn btn-danger btn-xs">'+
			'</div>'+
			'<div class="col-md-3" style="font-size:12px">'+
			'<input type="checkbox" class="sectionCheckbox" id="sectionCheckbox" value="true">Use this subject throughout this Section </div>'+
			'</div>';
			newQuestionPaperPatternArray[i].status = "edited";
		}else if(itemType == "Instruction"){
			var item_id = newQuestionPaperPatternArray[i].item_id;
			editString = editString + '<li><ul class="createExamPatternItem" data-item-type="Instruction" data-item-id="'+item_id+'" data-parent-id="'+parent_id+'">';
			var itemName = newQuestionPaperPatternArray[i].item_description;
			var itemNo = newQuestionPaperPatternArray[i].item_no;
			var itemMarks = newQuestionPaperPatternArray[i].item_marks;
			editString = editString + '<div class="row">'+
			'<div class="col-md-1">'+
			'<input type="text" id="questionNo" placeholder="Que No" class="form-control" value="'+itemNo+'">'+
			'<span id="questionNoError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-2">'+
			'<input type="text" id="createExamItemName" placeholder="Instruction Description" class="form-control" value="'+itemName+'">'+
			'<span id="descriptionError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="number" id="createExamItemMarks" placeholder="Marks" class="form-control" value="'+itemMarks+'">'+
			'<span id="marksError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-2 well">'+
			'<div class="col-md-9">'+
			'<select class="form-control" id="createExamSelectQuestionType">'+
			'<option value="addInstruction">Add instruction</option>'+
			'<option value="addQuestion">Add question</option>'+
			'</select>'+
			'</div>'+
			'<div class="col-md-1" style="font-size:12px">'+
			'<input type="button" value="Add" id="createExamAddItem" class="btn btn-primary btn-xs">'+
			'</div>'+
			'</div>'+
			'<div class="col-md-2 well">'+
			'<div class="col-md-7">'+
			'<select class="form-control" id="createExamSelectAlternateQuestion">'+
			'<option value="addInstruction">Add instruction</option>'+
			'<option value="addQuestion">Add question</option>'+
			'</select>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="button" value="Alternate" id="createExamAddAlternateItem" class="btn btn-primary btn-xs">'+
			'</div>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="button" value="Delete Instruction" id="createExamDeleteItem" class="btn btn-danger btn-xs">'+
			'</div>'+
			'</div>';
			newQuestionPaperPatternArray[i].status = "edited";
		}else{
			var item_id = newQuestionPaperPatternArray[i].item_id;
			editString = editString + '<li><ul class="createExamPatternItem" data-item-type="Question" data-item-id="'+item_id+'" data-parent-id="'+parent_id+'">';
			var itemName = newQuestionPaperPatternArray[i].item_description;
			var itemNo = newQuestionPaperPatternArray[i].item_no;
			var itemMarks = newQuestionPaperPatternArray[i].item_marks;
			var subject_id = newQuestionPaperPatternArray[i].subject_id;
			var question_topic = newQuestionPaperPatternArray[i].question_topic;
			var question_type =  newQuestionPaperPatternArray[i].question_type;
			editString = editString + '<div class="row">'+
			'<div class="col-md-1">'+
			'<input type="text" id="questionNo" placeholder="Que No" class="form-control" value="'+itemNo+'">'+
			'<span id="questionNoError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="number" id="createExamItemMarks" placeholder="Marks" class="form-control" value="'+itemMarks+'">'+
			'<span id="marksError" class="patternError"/>'+
			'</div>'+
			'<div class="col-md-2">'+
			'<select class="form-control" id="createExamSelectQuestionType">'+
			'<option value="-1">Question Type</option>';
			if( question_type == "1"){
				editString = editString +'<option value="1" selected>Subjective</option>'+
			'<option value="2">Objective</option>'+
			'<option value="3">Paragraph</option>';
			}else if( question_type == "2"){
				editString = editString + '<option value="1">Subjective</option>'+
				'<option value="2" selected>Objective</option>'+
				'<option value="3">Paragraph</option>';
			}else if( question_type == "3"){
				editString = editString + '<option value="1">Subjective</option>'+
				'<option value="2">Objective</option>'+
				'<option value="3" selected>Paragraph</option>';
			}else{
				editString = editString + '<option value="1">Subjective</option>'+
				'<option value="2">Objective</option>'+
				'<option value="3">Paragraph</option>';
			}
			editString = editString + '</select>'+
			'</div>'+
			'<div class="col-md-2">'+
			'<select class="form-control createExamSelectQuestionSubject" id="createExamSelectQuestionSubject">';
			if(subjectArray.length > 0){
				if(subjectType == "combineSub"){
				editString = editString + '<option value="-1">Select Subject</option>';
				}
			for(var k=0;k<subjectArray.length;k++){
				if(subjectArray[k].subjectId == subject_id){
					editString = editString + '<option value="'+subjectArray[k].subjectId+'" selected>'+subjectArray[k].subjectName+'</option>';	
				}else{
				editString = editString + '<option value="'+subjectArray[k].subjectId+'">'+subjectArray[k].subjectName+'</option>';
				}
			}
			}else{
				editString = editString + '<option value="-1">Select Subject</option>';
			}
			editString = editString + '</select>'+
			'</div>'+
			'<div class="col-md-2">'+
			'<select class="form-control createExamSelectQuestionTopic" id="createExamSelectQuestionTopic">'+
			'<option value="-1">Select Topic</option>';
			if(subject_id != -1 && subject_id != ""){
			if(topicMap[subject_id] != undefined ){
			for(var k=0;k<topicMap[subject_id].topics.length;k++){
				if(topicMap[subject_id].topics[k].topic_id == question_topic){
					editString = editString + '<option value="'+topicMap[subject_id].topics[k].topic_id+'" selected>'+topicMap[subject_id].topics[k].topic_name+'</option>';	
				}else{
				editString = editString + '<option value="'+topicMap[subject_id].topics[k].topic_id+'">'+topicMap[subject_id].topics[k].topic_name+'</option>';
				}
			}
			}
			}
			editString = editString + '</select>'+
			'</div>'+
			'<div class="col-md-2 well">'+
			'<div class="col-md-7">'+
			'<select class="form-control" id="createExamSelectAlternateQuestion">'+
			'<option value="addInstruction">Add instruction</option>'+
			'<option value="addQuestion">Add question</option>'+
			'</select>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="button" value="Alternate" id="createExamAddAlternateItem" class="btn btn-primary btn-xs">'+
			'</div>'+
			'</div>'+
			'<div class="col-md-1">'+
			'<input type="button" value="Delete Question" id="createExamDeleteItem" class="btn btn-danger btn-xs">'+
			'</div>'+
			'</div>';
			newQuestionPaperPatternArray[i].status = "edited";
		}
		var item_id = newQuestionPaperPatternArray[i].item_id;
		var newDataArray = [];
		for(var j=0; j<questionPaperPatternArray.length;j++){
		if( item_id == questionPaperPatternArray[j].parent_id ){
			newDataArray.push(questionPaperPatternArray[j]);
		}
		}
		if( newDataArray.length > 0){
			editString = recursiveEdit(questionPaperPatternArray,newDataArray,editString, newQuestionPaperPatternArray[i].item_id,recursionLevel)
		}
		editString = editString + "</ul></li>";
	}
	}
	
	return editString;
}

function getTopics(subID,that,scenario){
	topic_names="";
	topic_ids="";
	//var divisionID = $("#division").val();
	if(subID != ""){
	 $.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getDivisionsTopics",
		    	 divisionID:division,
		    	 subID:subID
		   		},
		   type:"POST",
		   async:false,
		   success:function(data){
			   var resultJson=JSON.parse(data);
			   topic_names=resultJson.topic_names.split(",");
			   topic_ids=resultJson.topic_ids.split(",");
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
		   },
		   error:function(){
			   }
		   });
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

function generatePattern(){
	verifyPattern();
	if(errorFlag == false){
	var alternateValue = 0;
	var preAlternateValue = 0;
	var questionPaperStructure = [];
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
		$("#examPatternNameError").html("Pattern with same name already available");
	}else {
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").show();
		$("#editPatternDiv").hide();
		$(".actionOptionWS").hide();
		$(".actionOptionWOS").hide();	
		$("#patternUpdatedNotification").modal("toggle");}
	}
	handlers.error = function(e){console.log("Error",e)}
	var QuestionPaperPattern = {};
	QuestionPaperPattern.pattern_name = $("#examPatternName").val();
	QuestionPaperPattern.class_id = division;
	QuestionPaperPattern.sub_id = $("#subject").val();
	QuestionPaperPattern.marks = $("#totalMarks").val();
	QuestionPaperPattern.pattern_id = $("#pattern_id").val();
	QuestionPaperPattern.questionPaperStructure = questionPaperStructure;
	var QuestionPaperPatternJson = JSON.stringify(QuestionPaperPattern);
	rest.post("rest/classownerservice/updateQuestionPaperPattern",handlers,QuestionPaperPatternJson,false);
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
		getTopics(selectedSectionSubject,that);
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
			topicSelectString = topicSelectString + "<option value='"+topic_ids[i]+"'>"+topic_names[i]+"</option>";
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
		if(subjectArray.length > 0){
			var subjectSelectString = "<div class='col-md-2'><select class='form-control createExamSelectQuestionSubject' id='createExamSelectQuestionSubject'><option value='-1'>Select Subject</option>";
			while(i < subjectArray.length)	
			{
				if(selectedSectionSubject != "" && selectedSectionSubject != "-1" && selectedSectionSubject == subjectArray[i].subjectId){
					subjectSelectString = subjectSelectString + "<option value='"+subjectArray[i].subjectId+"' selected>"+subjectArray[i].subjectName+"</option>";
				}else{
				subjectSelectString = subjectSelectString + "<option value='"+subjectArray[i].subjectId+"'>"+subjectArray[i].subjectName+"</option>";
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
	if($("#division").val() == "-1"){
		$("#divisionError").html("Select Class");
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
	<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="viewneditpattern"/>
	</jsp:include>
<div class="container" style="padding: 2%; background: #eee">
		<div class="row">
			<div class="col-md-3">
				<select id="division" name="division" class="form-control">
					<option value="-1">Select Class</option>
					<c:forEach items="${divisionList}" var="division">
						<option value="<c:out value="${division.divId }"></c:out>">
							<c:out value="${division.divisionName }"></c:out>
							<c:out value="${division.stream }"></c:out>
						</option>
					</c:forEach>
				</select>
				<span id="divisionError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<select id="subject" name="subject" class="form-control">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<select id="patternType" name="patternType" class="form-control">
					<option value="-1">Any Pattern Type</option>
					<option value="WS">Pattern With Section</option>
					<option value="WOS">Pattern Without Section</option>
				</select>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchPattern">Search</button>
			</div>
		</div>
	</div>
	<div class="container" id="patternListTableDiv" style="display: none;width: 100%">
		<table class="table" id="patternListTable" style="width: 100%">
		<thead>
			<tr>
				<th>Sr No.</th>
				<th>Pattern Description</th>
				<th>Marks</th>
				<th>View/Edit</th>
				<th>Delete</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
	</div>
	<div id="viewPatternDiv" style="display: none;" class="container">
		<div class="row">
			<div class="col-md-1"><button class="cancleView btn btn-primary btn-xs" >Back To List</button></div>
			<div class="col-md-1"><button class="editPattern btn btn-primary btn-xs" >Edit</button></div>
			<div class="col-md-10"></div>
		</div>
		<div id="viewPatternData"></div>
	</div>
	<div id="editPatternDiv" style="display: none;" class="container">
		<div class="row">
			<div class="col-md-1"><button class="cancleView btn btn-primary btn-xs" >Back To List</button></div>
			<div class="col-md-1"><button class="cancleEdit btn btn-primary btn-xs" style="display: none;">cancle Edit</button></div>
			<div class="col-md-10"></div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<input type="text" id="examPatternName" name="examPatternName"
					placeholder="Exam Pattern Name" class="form-control">
					<span id="examPatternNameError" class="patternError"></span>
					<input type="hidden" id="pattern_id" name="pattern_id">
			</div>
			<div class="col-md-3">
				<input type="number" placeholder="Total Marks" id="totalMarks"
					name="totalMarks" min="0" class="form-control">
				<span id="totalMarksError" class="patternError"></span>	
			</div>
			<div class="col-md-1 actionOptionWS" style="display: none;">
			<input id="addSection" value="Add Section" type="button" class="btn btn-primary btn-sm">
			</div>
			<div class="col-md-2 actionOptionWOS" style="display: none;">
				<select class="form-control" id="wosSelect">
					<option value="addInstruction">Add Instrution</option>
					<option value="addQuestion">Add Question</option>
				</select>
			</div>
			<div class="col-md-2 actionOptionWOS" style="display: none;"><button class="btn btn-primary btn-xs" id="addWOS">Add</button></div>
		</div>
		<ul id="examPattern" style="padding: 0%"></ul>
		<input id="createExamGenerate" value="Save" type="button" class="btn btn-primary btn-sm">
	</div>
	
	<div class="modal fade" id="patternUpdatedNotification" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel" align="center">
               Pattern
            </h4>
         </div>
         <div class="modal-body">
         Pattern updated successfully.
          </div>
         </div>
   </div>
</div>
</body>
</html>