var division  = "";
var subject = "";
var editString = "";
var questionPaperPattern = {};
var subjectArray = [];
var topicMap = {}
var PAGE = "#createExam",
ITEM_NAME = PAGE+"ItemName",
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

var topicNSubject = {};
var questionPaperServicebeanList;
var questionListTable;
//this pattern id is set when you select pattern
var patternId;

var SELECT_SUBJECT = ".selectSubject";
var SELECT_TOPIC = ".selectTopic";
var GENERATE_QUESTION_PAPER = "#generateQuestionPaper";
var ROW_ITEM = ".questionRow";
var QUETION_TYPE = "question_type";
var ITEM_ID = "item_id";
var ITEM_MARKS = "item_marks";
var QUESTION = ".question";
var QUESTION_ID = "question_id";
var REGENERATE_QUE_BTN = ".regenerateQuestion";
var CHOOSE_QUE_BTN = ".chooseQuestion"
var QUESTION_LIST_MODAL = "#questionListModal";
var QUESTION_LIST_LOADING = "#questionListLoading";
var QUESTION_LIST = "#questionList";
var QUESTION_LIST_TABLE = "#questionListTable";
var CHOOSE_QUESTION = ".chooseQuestionFromTable";
var SAVE_QUESTION_PAPER = "#saveQuestionPaper";
var SAVE_SECTION = "#saveSection";
var QUESTION_PAPER_DESC = "#saveQuestionPaperDesc";
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
			{ title: "",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary viewPattern" value="Create" id="'+row.pattern_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%",bSortable:false}
		]
	});
}

function loadSubjectAndTopic(topicNSubjectAttr){
	var subNTopic = {};
	$.each(topicNSubjectAttr,function(key,val){
		var subNTopicDetail = {};
		var subjectName = val.subject.subjectName;
		subNTopicDetail.subjectName = subjectName;
		
		if(!subNTopicDetail.topic){
			subNTopicDetail.topic = [];
		}
		$.each(val.topics,function(key,val){
			var topicData = {};
			var topicName = val.topic_name;
			var topicId = val.topic_id;
			topicData.topicName = topicName;
			topicData.topicId = topicId;
			subNTopicDetail.topic.push(topicData);
		});
		
		subNTopic[key] = subNTopicDetail;
	});
	topicNSubject = subNTopic;
}

function loadSubjectAndTopicSelect(){
	var optionString = "<option value='-1'>Select subject</option>";
	$.each(topicNSubject,function(key,val){
		optionString = optionString + "<option value='"+key+"'>"+val.subjectName+"</option>";
	});
	$(SELECT_SUBJECT).append(optionString);
	$.each($(SELECT_SUBJECT),function(){
		var that= $(this);
		var val = $(this).attr("value");
		$(this).val(val);
		if($(this).val() != null && $(this).val() != "-1"){
		if(topicNSubject[val] != undefined){
		loadTopicSelect(that,topicNSubject[val].topic);
		}
		}else{
		$(this).val("-1");
		loadTopicSelect(that,[]);	
		}
	});
}

function loadTopicSelect(selectSub,topics){
	var topicSelect = selectSub.closest('.row').find(SELECT_TOPIC);
	var topic = "";
	topic = topic + "<option value='-1'>Select topic</option>";
	$.each(topics,function(key,val){
		topic = topic + "<option value='"+val.topicId+"'>"+val.topicName+"</option>";
	});
	topicSelect.append(topic);
	var val = topicSelect.attr("value");
	topicSelect.val(val);
}

function getSubjectName(subId){
	var sub = topicNSubject.subject;
}


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
	
	$("body").on("click",REGENERATE_QUE_BTN,function(e){
			var inst_id = $("#instituteSelect").val();
			var RegenerateObj = {};
			var selectSub = $(this).closest('.row').find(SELECT_SUBJECT);
			var selectTopic = $(this).closest('.row').find(SELECT_TOPIC);
			
			var generateExamObject = new GenerateExamObject();
			
			generateExamObject.subject_id = selectSub.val();
			generateExamObject.item_id = $(this).closest(ROW_ITEM).data(ITEM_ID);
			generateExamObject.question_type = $(this).closest(ROW_ITEM).data(QUETION_TYPE);
			generateExamObject.item_type = "Question";	
			generateExamObject.question_topic = selectTopic.val();
			generateExamObject.item_marks = $(this).closest(ROW_ITEM).data(ITEM_MARKS);
			
			RegenerateObj.questionPaperStructure = generateExamObject;
			RegenerateObj.generateQuestionPaperServicebeanList = questionPaperServicebeanList;
			var currentIds = [];
			$.each($(QUESTION),function(key,val){
				if( $($(val).closest('.questionRow')).find('.selectSubject').val() == selectSub.val()){
					currentIds.push($(val).data(QUESTION_ID));
				}
			});
			RegenerateObj.currentIds = currentIds;
			var url = "rest/teacher/generateQuestionPaperSpecific/"+inst_id+"/"+$("#division").val();
			var handler = {};
			handler.success = function(e){
				//This list is required for regeneration of exam list
				questionPaperServicebeanList = e.questionPaperServicebeanList;
				if(e.questionPaperDataList && e.questionPaperDataList.length){
					$.each(e.questionPaperDataList,function(key,val){
						if(val.dataStatus != "N" && val.dataStatus != "MN"){
						$("[item_id='"+val.item_id+"']").find(QUESTION).text(val.questionbank.que_text);	
						$("[item_id='"+val.item_id+"']").find(QUESTION).data(QUESTION_ID,val.questionbank.que_id);
						}else if(val.dataStatus == "MN"){
							$.notify({message: "More Questions are not available for criteria"},{type: 'danger'});
						}else if(val.dataStatus == "N"){
							$.notify({message: "Questions are not available for criteria"},{type: 'danger'});
							$("[item_id='"+val.item_id+"']").find(QUESTION).html("<div class='error'>Questions are not available for criteria</div>");
						}
					});			
					if($(".question").find(".error").length == 0){
						$(SAVE_SECTION).show();
					}
				}
			}
			handler.error = function(e){console.log(e);}
			rest.post(url,handler,JSON.stringify(RegenerateObj));
			});
		
	$("body").on("click",GENERATE_QUESTION_PAPER,function(){
		var inst_id = $("#instituteSelect").val();
		var generateExamObjectList = [];
		$.each($(ROW_ITEM),function(key,val){
			var selectSub = $(this).find(SELECT_SUBJECT);
			var selectTopic = $(this).find(SELECT_TOPIC);
			
			var generateExamObject = new GenerateExamObject();
			
			generateExamObject.subject_id = selectSub.val();
			generateExamObject.item_id = $(this).data(ITEM_ID);
			generateExamObject.question_type = $(this).data(QUETION_TYPE);
			generateExamObject.item_type = "Question";	
			generateExamObject.question_topic = selectTopic.val();
			generateExamObject.item_marks = $(this).data(ITEM_MARKS);
			generateExamObjectList.push(generateExamObject);
		});
		
		//console.log(generateExamObjectList);
		var url = "rest/teacher/generateQuestionPaper/"+inst_id+"/"+$("#division").val();
		var handler = {};
		
		handler.success = function(e){
			//This flag is false if no availibility
			var availibilityFlag = true;
			questionPaperServicebeanList = e.questionPaperServicebeanList;
			if(e.questionPaperDataList.length){
				$.each(e.questionPaperDataList,function(key,val){
					if(val.dataStatus != "N"){
						$("[item_id='"+val.item_id+"']").find(QUESTION).text(val.questionbank.que_text);	
						$("[item_id='"+val.item_id+"']").find(QUESTION).data(QUESTION_ID,val.questionbank.que_id);
					}else{
						availibilityFlag = false;
						$.notify({message: "Questions are not available for criteria"},{type: 'danger'});
						$("[item_id='"+val.item_id+"']").find(QUESTION).html("<div class='error'>Questions are not available for criteria</div>");
					}
					
				});
			$(".noRegenerate").removeClass("noRegenerate");
			}
			if(availibilityFlag){
				$.notify({message: "Exam generated successfully"},{type: 'success'});
				$(SAVE_SECTION).show();
			}else{
				$(SAVE_SECTION).hide();
			}
		}
		handler.error = function(e){console.log(e);}
		rest.post(url,handler,JSON.stringify(generateExamObjectList));
	});
	
	$("body").on("click",CHOOSE_QUE_BTN,function(){
			var RegenerateObj = {};
			var selectSub = $(this).closest('.row').find(SELECT_SUBJECT);
			var selectTopic = $(this).closest('.row').find(SELECT_TOPIC);
			var subject_id = selectSub.val();
			var item_id = $(this).closest(ROW_ITEM).data(ITEM_ID);
			var question_type = $(this).closest(ROW_ITEM).data(QUETION_TYPE);
			var item_type = "Question";	
			var question_topic = selectTopic.val();
			var item_marks = $(this).closest(ROW_ITEM).data(ITEM_MARKS);
			
		loadQuestionList(subject_id,item_id,question_type,item_type,question_topic,item_marks);
	});
	
	$("body").on("click",CHOOSE_QUESTION,function(){
		var tRow = $(this).closest('tr');
		var data = questionListTable.row(tRow).data();
		updateQuestionList(data);
		$(QUESTION_LIST_MODAL).modal('hide');
	});
	
	$(SAVE_QUESTION_PAPER).on("click",function(){
		var inst_id = $("#instituteSelect").val();
		var compSubjectIds = [];
		for(var i = 0 ; i<$(".selectSubject").length ; i++){
			if(jQuery.inArray($($(".selectSubject")[i]).val(),compSubjectIds) == -1){
			compSubjectIds.push($($(".selectSubject")[i]).val())
			}
		}
		compSubjectIds = compSubjectIds.join(",");
		var questionPaperData = {};
		$.each($(QUESTION),function(key,val){
			var questionPaperFileElement = {};
			questionPaperFileElement.subject_id = $($(val).closest('.questionRow')).find('.selectSubject').val();
			questionPaperFileElement.ques_no = $(val).data(QUESTION_ID);
			questionPaperFileElement.question_topic = $($(val).closest('.questionRow')).find('.selectTopic').val();
			console.log(questionPaperFileElement);
			questionPaperData[$(val).closest('[item_id]').attr('item_id')] = questionPaperFileElement;
		});
		var handlers = {};
		handlers.success = function(resp){
			if(resp == true){
			$.notify({message: "Exam saved successfully"},{type: 'success'});
			}else{
				$.notify({message: "Exam with same description already present.Please enter different description"},{type: 'danger'});
			}
		};
		handlers.error = function(e){};
		var division = $("#division").val();
		subject = $("#subject").val();
		var questionPaperName = $(QUESTION_PAPER_DESC).val();
		var desc = $(QUESTION_PAPER_DESC).val();
		if(desc && desc.trim().length!==0){
			//questionPaperData.desc = desc;
			rest.post("rest/teacher/saveQuestionPaper/"+inst_id+"/"+patternId+"/"+questionPaperName+"/"+division+"/"+subject+"/"+compSubjectIds,handlers,JSON.stringify(questionPaperData),true);	
		}else{
			$("#saveQuestionPaperName").focus();
		}
		
	});
	
	$(QUESTION_LIST_MODAL).on('shown.bs.modal', function () {
	
	});
	
	
	$("#subject").change(function(){
		var inst_id = $("#instituteSelect").val();
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").hide();
		var handlers = {};
		handlers.success = function(resp){
			loadSubjectAndTopic(resp);
		};
		handlers.error = function(e){};
		var subject = $(this).val();
		var division = $("#division").val();
		if(subject != "-1" && subject != "" && subject != null){
			rest.post("rest/teacher/getSubjectsAndTopicsForExam/"+inst_id+"/"+division+"/"+subject,handlers);
		}
	});
	
	$("#searchPattern").click(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").hide();
		$(".validation-message").html("");
		var inst_id = $("#instituteSelect").val();
		division = $("#division").val();
		subject = $("#subject").val();
		var patternType = $("#patternType").val();
		var validationFlag = false;
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
		var handlers = {};
		handlers.success = function(e){
		$("#patternListTable").find("tbody").empty();
		createPatternTable(e);
		$("#patternListTableDiv").show();
		};
		handlers.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/searchQuestionPaperPattern/"+inst_id+"/"+division+"/"+subject+"/"+patternType,handlers);
		}
	});
	
	$("#patternListTable").on("click",".viewPattern",function(){
		$("#viewPatternDiv").show();
		$("#patternListTableDiv").hide();
		var inst_id = $("#instituteSelect").val();
		patternId = $(this).attr("id");
		var handlers = {};
		handlers.success = function(e){
		$("#viewPatternData").empty();
		$("#viewPatternData").append("<div class='row' align='center' style='font-weight:bold'>"+e.pattern_name+"</div>")
		for(var i=0; i<e.questionPaperStructure; i++){
			e[i].status="";
		}
		questionPaperPattern = e;
		recursiveView(e.questionPaperStructure,0,e.questionPaperStructure);
		loadSubjectAndTopicSelect();
		$(SAVE_SECTION).addClass("noRegenerate");
		};
		handlers.error = function(e){
		console.log("Error",e)}
		editString = "";
		rest.get("rest/teacher/getQuestionPaperPattern/"+inst_id+"/"+division+"/"+patternId,handlers);
	});
	
	$(".cancleView").click(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTableDiv").show();
		$(".actionOptionWS").hide();
		$(".actionOptionWOS").hide();		
	});
	
	/*$("#patternType").change(function(){
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
	});*/
});

var alternateValueMap = [];
function loadSubjectRelatedToTopicSelect(){
	var subjectId = $(this).val();
	
	var topicSelect = $(this).closest('.row').find(SELECT_TOPIC);
	var topic = "<option value='-1'>Select topic</option>";
	var topics = topicNSubject[subjectId]?topicNSubject[subjectId].topic:[];
	$.each(topics,function(key,val){
		topic = topic + "<option value='"+val.topicId+"'>"+val.topicName+"</option>";
	});
	topicSelect.empty();
	topicSelect.append(topic);
	
	$($(this).closest(".questionRow")).find(QUESTION).text("");	
	$($(this).closest(".questionRow")).find(QUESTION).data(QUESTION_ID,-1);
	$($(this).closest(".questionRow")).find(QUESTION).html("<div class='error'>Press refresh to get Question</div>");
	$(SAVE_SECTION).hide();
	/*
	$.each($(SELECT_SUBJECT),function(){
		var that= $(this);
		loadTopicSelect(that,topicNSubject[val] && topicNSubject[val].topic?topicNSubject[val].topic:[]);
	});
	loadTopicSelect(that,topicNSubject[val] && topicNSubject[val].topic?topicNSubject[val].topic:[]);
	*/
}

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
				$("#viewPatternData").append("<div class='row' style='font-weight:bold'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-2 pull-right' title='Marks'>"+itemMarks+"</div></div>")
				}else{
					$("#viewPatternData").append("<div class='row'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-2 pull-right' title='Marks'>"+itemMarks+"</div></div>")
				}
				data[i].status = "viewed";
			}else{
				var itemName = data[i].item_description;
				var itemNo = data[i].item_no;
				var itemMarks = data[i].item_marks;
				
				var rowItem = $("<div/>",{
					class:"row questionRow",
					style:'font-weight:bold',
					item_id:data[i].item_id
				});
				
				rowItem.data(QUETION_TYPE,data[i].question_type);
				rowItem.data(ITEM_ID,data[i].item_id);
				rowItem.data(ITEM_MARKS,data[i].item_marks);
				
				var itemNumber = $("<div/>",{
					class:"col-md-1",
					style:'padding-left:'+recursionLevel+'%;',
					text:itemNo
				});
				
				var question = $("<div/>",{
					class:"col-md-5 question",
					text:"Question"
				});
				
				var selectSubject = $("<select/>",{
					class:"btn btn-default selectSubject btn-xs",
					style:"width:100%;",
					value:data[i].subject_id
				}).on('change',loadSubjectRelatedToTopicSelect);
				
				var subjectDiv = $("<div/>",{
					class:"col-md-2"
				}).append(selectSubject);
				
				var selectTopic = $("<select/>",{
					class:"btn btn-default selectTopic btn-xs",
					style:"width:100%;",
					value:data[i].question_topic
				});
				
				var topicDiv = $("<div/>",{
					class:"col-md-2"
				}).append(selectTopic);
				
				var itemMarks = $("<div/>",{
					class:"col-md-1",
					text:itemMarks
				});
				
				var iBtn = $("<i/>",{
					class:"glyphicon glyphicon-refresh regenerateQuestion",
					title:"Reload question"
					
				});
				
				var chooseBtn = $("<i/>",{
					class:"glyphicon glyphicon-book chooseQuestion",
					title:"Choose question"
				});
				
				var regenerateBtn = $("<div/>",{
					class:"col-md-1"
				}).append(iBtn).append(chooseBtn);
				
				rowItem.append(itemNumber);
				rowItem.append(question);
				rowItem.append(subjectDiv);
				rowItem.append(topicDiv);
				rowItem.append(itemMarks);
				rowItem.append(regenerateBtn);
				
				$("#viewPatternData").append(rowItem);
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
			
			$("#viewPatternDiv").find('.regenerateQuestion,.chooseQuestion').addClass("noRegenerate");	
			
		}
	}
}
}

function GenerateExamObject(){
		//OrId will have same id for ored question
		this.subject_id;
		this.question_topic;
		this.item_id;
		this.question_type;
		this.item_type;
		this.item_marks;		
}

var loadQuestionList = function(subject_id,item_id,question_type,item_type,question_topic,item_marks){
			var generateExamObject = new GenerateExamObject();
			generateExamObject.subject_id = subject_id;
			generateExamObject.item_id = item_id;
			generateExamObject.question_type = question_type;
			generateExamObject.item_type = "Question";	
			generateExamObject.question_topic = question_topic;
			generateExamObject.item_marks = item_marks;
			var RegenerateObj = {};
			RegenerateObj.questionPaperStructure = generateExamObject;
			RegenerateObj.generateQuestionPaperServicebeanList = questionPaperServicebeanList;
			var inst_id = $("#instituteSelect").val();
			var url = "rest/teacher/getQuestionList/"+inst_id+"/"+$("#division").val();
			var handler = {};
			$(QUESTION_LIST_MODAL).modal('show');
			$(QUESTION_LIST_LOADING).show();
			$(QUESTION_LIST).hide();
			handler.success = function(data){
				$(QUESTION_LIST_LOADING).hide();
				$(QUESTION_LIST).show();
				questionListTable = $(QUESTION_LIST_TABLE).DataTable({
				bDestroy:true,
				data: data,
				lengthChange: false,
				columns: [
					{ title: "Question",data:"questionbank.que_text",sWidth:"80%"},
					{ title: "Marks",data:"questionbank.marks",sWidth:"10%"},
					{ title: "",data:null,render:function(data,event,row){
						return "<a class='btn btn-success btn-xs chooseQuestionFromTable'>Choose</a>"
					},sWidth:"10%"}
					]
					});
			
			}
			handler.error = function(e){console.log(e);}
			rest.post(url,handler,JSON.stringify(RegenerateObj));
			}
			
	function updateQuestionList(data){
		$("[item_id='"+data.item_id+"']").find(QUESTION).text(data.questionbank.que_text);	
		$("[item_id='"+data.item_id+"']").find(QUESTION).data(QUESTION_ID,data.questionbank.que_id);
		if($(".question").find(".error").length == 0){
			$(SAVE_SECTION).show();
		}
	}