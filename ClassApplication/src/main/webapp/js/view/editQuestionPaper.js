var DIVISION_DROPDOWN = "#division";
var SEARCH_QUESTION_PAPER = "#searchQuestionPaper";
var PAPER_LIST_TABLE = "#editQuestionPaperList";
var EDIT_QUESTION_PAPER = ".editQuestionPaper";
var EDIT_QUESTION_PAPER_CONTAINER = "#editQuestionPaperListContainer";

var ROW_ITEM = ".questionRow";
var QUETION_TYPE = "question_type";
var ITEM_ID = "item_id";
var ITEM_MARKS = "item_marks";
var QUESTION = ".question";
var QUESTION_ID = "question_id";
var REGENERATE_QUE_BTN = ".regenerateQuestion";
var CHOOSE_QUE_BTN = ".chooseQuestion"
var SELECT_SUBJECT = ".selectSubject";
var SELECT_TOPIC = ".selectTopic";
var SAVE_SECTION = "#saveSection";
var REGENERATE_QUE_BTN = ".regenerateQuestion";
var GENERATE_QUESTION_PAPER = "#generateQuestionPaper";

var QUESTION_LIST_MODAL = "#questionListModal";
var QUESTION_LIST_LOADING = "#questionListLoading";
var QUESTION_LIST = "#questionList";
var QUESTION_LIST_TABLE = "#questionListTable";
var CHOOSE_QUESTION = ".chooseQuestionFromTable";
var SAVE_QUESTION_PAPER = "#saveQuestionPaper"
var QUESTION_PAPER_DESC = "#saveQuestionPaperDesc";
var BACK_TO_LIST = "#backToList";
var DELETE_QUESTION = ".deleteQuestionPaper";
/*URL*/
var baseURL = "/rest/classownerservice/";
var getList = baseURL + "getQuestionPaperList/";
var getQuestionPaperUrl = baseURL + "getQuestionPaper/";
var deleteQuestionPaperUrl = baseURL + "deleteQuestionPaper/";

/**/
var qustionPaperListTable;
var topicNSubject = {};
var questionPaperServicebeanList;
var questionListTable;
var selectedChooseQuestionItemId;
var patternId;
var paperId;
$(document).ready(function(){
	
	$("#division").change(function(){
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
	
	$(SAVE_SECTION).hide();
	$("body").on("click",BACK_TO_LIST,backToQuestionPaperList);
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
		$(SAVE_QUESTION_PAPER).prop("disabled",false);
		$(QUESTION_LIST_MODAL).modal('hide');
	});
	
	$(SAVE_QUESTION_PAPER).on("click",function(){
		var compSubjectIds = [];
		for(var i = 0 ; i<$(".selectSubject").length ; i++){
			if(jQuery.inArray($($(".selectSubject")[i]).val(),compSubjectIds) == -1){
			compSubjectIds.push($($(".selectSubject")[i]).val())
			}
		}
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
			$.notify({message: "Exam updated successfully"},{type: 'success'});
			/*loadSubjectAndTopic(resp);*/
			backToQuestionPaperList();
			$(SEARCH_QUESTION_PAPER).trigger("click");
		};
		handlers.error = function(e){};
		var division = $("#division").val();
		var questionPaperName = $(QUESTION_PAPER_DESC).val();
		var desc = $(QUESTION_PAPER_DESC).val();
		if(desc && desc.trim().length!==0){
			/*questionPaperData.desc = desc;*/
			rest.post("rest/classownerservice/updateQuestionPaper/"+patternId+"/"+questionPaperName+"/"+division+"/"+paperId+"/"+compSubjectIds,handlers,JSON.stringify(questionPaperData),true);	
		}else{
			$("#saveQuestionPaperName").focus();
		}
		
	});
	
	$("body").on("click",SEARCH_QUESTION_PAPER,searchQuestionPaper).
		on("click",EDIT_QUESTION_PAPER,loadQuestionPaper).
		on("click",DELETE_QUESTION,deleteQuestionPaper);
		;
	
		$("body").on("click",REGENERATE_QUE_BTN,function(e){
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
			var url = "rest/classownerservice/generateQuestionPaperSpecific/"+$("#division").val();
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
				}else{
					$("[item_id='"+generateExamObject.item_id+"']").find(QUESTION).html('<div class="error">Questions are not available for criteria</div>');	
					$("[item_id='"+generateExamObject.item_id+"']").find(QUESTION).removeData(QUESTION_ID);
				}
						
		if($(".question").find(".error").length == 0){
			$(SAVE_QUESTION_PAPER).prop("disabled",false);
		}else{
			$(SAVE_QUESTION_PAPER).prop("disabled",true);
		}
			}
			handler.error = function(e){console.log(e);}
			rest.post(url,handler,JSON.stringify(RegenerateObj));
			});	
			
			$("body").on("click",GENERATE_QUESTION_PAPER,function(){
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
		var url = "rest/classownerservice/generateQuestionPaper/"+$("#division").val();
		var handler = {};
		handler.success = function(e){
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
			if(availibilityFlag){$.notify({message: "Exam generated successfully"},{type: 'success'});}
			}
		}
		handler.error = function(e){console.log(e);}
		rest.post(url,handler,JSON.stringify(generateExamObjectList));
	});
			
			$("#subject").change(function(){
				var handlers = {};
				handlers.success = function(resp){
					loadSubjectAndTopic(resp);
				};
				handlers.error = function(e){};
				var subject = $(this).val();
				var division = $("#division").val();
				rest.post("rest/classownerservice/getSubjectsAndTopicsForExam/"+division+"/"+subject,handlers);
			});
});

/*Function*/
var deleteQuestionPaper = function(){
	var trow = $(this).closest('tr');
	var data = qustionPaperListTable.row(trow).data();
	var handler = {};
	handler.success = function(){
		$.notify({message: "Exam updated successfully"},{type: 'success'});
		searchQuestionPaper();
	};
	handler.error = function(){};
	var paperId = data.paper_id;
	var divisionId = $(DIVISION_DROPDOWN).val();		
	if(paperId){
		rest.deleteItem(deleteQuestionPaperUrl+divisionId+"/"+paperId,handler);
	}
}

var loadQuestionPaper = function(){
	var trow = $(this).closest('tr');
	var data = qustionPaperListTable.row(trow).data();
	var handler = {};
	handler.success = loadQuestionPaperSuccess;
	handler.error = loadQuestionPaperError;
	var paperId = data.paper_id;
	var divisionId = $(DIVISION_DROPDOWN).val();
		
	if(paperId){
		$("#viewPatternData").show();
		rest.get(getQuestionPaperUrl+divisionId+"/"+paperId,handler);
	}
	
}

var backToQuestionPaperList = function(){
	$(EDIT_QUESTION_PAPER_CONTAINER).show();
	$(SAVE_SECTION).hide();
	$(BACK_TO_LIST).hide();
	$("#viewPatternData").empty();
}
var loadQuestionPaperSuccess = function(resp){
	var e = resp.fileObject;
	questionPaperServicebeanList = resp.generateQuestionPaperServicebeanList;
	$(BACK_TO_LIST).show();
	$(EDIT_QUESTION_PAPER_CONTAINER).hide();
	patternId = e.pattern_id;
	paperId = e.paper_id;
	$("#viewPatternData").empty();
		$("#viewPatternData").append("<div class='row' align='center' style='font-weight:bold'>"+e.paper_description+"</div>")
		questionPaperPattern = e;
		recursiveView(e.questionPaperFileElementList,0,e.questionPaperFileElementList);
		loadSubjectAndTopicSelect();
	$(QUESTION_PAPER_DESC).val(e.paper_description);	
	if($(".question").find(".error").length == 0){
		$(SAVE_QUESTION_PAPER).prop("disabled",false);
	}else{
		$(SAVE_QUESTION_PAPER).prop("disabled",true);
	}
	$(SAVE_SECTION).show();
}

var loadQuestionPaperError = function(e){
	console.log(e);
}

var searchQuestionPaper = function(){
	$("#editQuestionPaperListContainer").hide();
	$("#viewPatternData").hide();
	$("#saveSection").hide();
	$(".validation-message").html("");
	var handler = {};
	handler.success = searchQuestionPaperSuccess;
	handler.error = searchQuestionPaperError;
	var divisionId = $(DIVISION_DROPDOWN).val();
	var subject = $("#subject").val();
	var validationFlag = false;
	if(divisionId == "-1" || divisionId == "" || divisionId == null){
		$("#divisionError").html("Select Class");
		validationFlag = true;
	}
	
	if(subject == "-1" || subject == "" || subject == null){
		$("#subjectError").html("Select Subject");
		validationFlag = true;
	}
	if(validationFlag == false){
		$("#editQuestionPaperListContainer").show();
		rest.get(getList+divisionId+"/"+subject,handler);
	}
	
	/*var handlers = {};
		handlers.success = function(resp){
			loadSubjectAndTopic(resp);
		};
		handlers.error = function(e){};
		
		rest.post("rest/classownerservice/getSubjectsAndTopics/"+divisionId,handlers);*/
}

var searchQuestionPaperSuccess = function(data){
	
	qustionPaperListTable = $(PAPER_LIST_TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns:[
	{
		title: "Question paper",data:'paper_description'
	},
	{
		title: "Created date",data:'created_dt'
	},
	{
		title: "Marks",data:'marks'
	},
	{
		title: "",data:null,sWidth:"10%",render:function(){return "<div><a class='btn-link editQuestionPaper'>Edit</a>&nbsp;<a class='btn-link deleteQuestionPaper'>Delete</a></div>"},bSortable:false
	}]});
}

var searchQuestionPaperError = function(){
	
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
		loadTopicSelect(that,topicNSubject[val] && topicNSubject[val].topic?topicNSubject[val].topic:[]);
		if($(this).val() == null){
			$(this).val("-1")
		}
	});
}

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
	$(SAVE_QUESTION_PAPER).prop("disabled",true);
	/*
	$.each($(SELECT_SUBJECT),function(){
		var that= $(this);
		loadTopicSelect(that,topicNSubject[val] && topicNSubject[val].topic?topicNSubject[val].topic:[]);
	});
	loadTopicSelect(that,topicNSubject[val] && topicNSubject[val].topic?topicNSubject[val].topic:[]);
	*/
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

function loadTopicSelect(selectSub,topics){
	var topicSelect = selectSub.closest('.row').find(SELECT_TOPIC);
	var topic = "<option value='-1'>Select topic</option>";
	$.each(topics,function(key,val){
		topic = topic + "<option value='"+val.topicId+"'>"+val.topicName+"</option>";
	});
	topicSelect.empty();
	topicSelect.append(topic);
	var val = topicSelect.attr("value");
	if(val==0){
		val = -1;
	}
	topicSelect.val(val);
}

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
				$("#viewPatternData").append("<div class='row' style='font-weight:bold'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-2 pull-right'>"+itemMarks+"</div></div>")
				}else{
					$("#viewPatternData").append("<div class='row'><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-2 pull-right'>"+itemMarks+"</div></div>")
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
					html:data[i].questionbank?data[i].questionbank.que_text:"<span class='error'>Question is not available</span>"
				}).data(QUESTION_ID,(data[i].questionbank?data[i].questionbank.que_id:undefined));
				
				var selectSubject = $("<select/>",{
					class:"btn btn-default selectSubject btn-xs",
					style:"width:100%;",
					value:data[i].questionbank?data[i].questionbank.sub_id:-1
				}).on('change',loadSubjectRelatedToTopicSelect);
				
				var subjectDiv = $("<div/>",{
					class:"col-md-2"
				}).append(selectSubject);
				
				var selectTopic = $("<select/>",{
					class:"btn btn-default selectTopic btn-xs",
					style:"width:100%;",
					value:data[i].questionbank?data[i].questionbank.topic_id:-1
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

var loadQuestionList = function(subject_id,item_id,question_type,item_type,question_topic,item_marks){
			selectedChooseQuestionItemId = item_id;
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
			
			var url = "rest/classownerservice/getQuestionList/"+$("#division").val();
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
