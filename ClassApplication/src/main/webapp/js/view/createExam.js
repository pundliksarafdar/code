var PREVIEW = ".preview";
var SELECTED_QUETION_ID = ".selectedQuestionPaperID"
var HEADER_DESC = "#headerDesc"
var DIVISION = "#division";
var PREVIEW_PAGE_HEADER = "#preview_pageHeader";
var PREVIEW_PAGE_CONTENT = "#preview_pageContent";
var ITEM_TYPE = {
	SECTION:"Section",
	QUESTION:"Question",
	INSTRUCTION:"Instruction"
}
var BATCH_SELECT = "#batchSelect";
var PREVIEW_PAGE = "#preview_page";

/************************/
var baseURL = "/rest/classownerservice/";
var getQuestionPaperUrl = baseURL + "getQuestionPaper/";
var getHeaderUrl = baseURL+"getHeader/";

$(document).ready(function(){
	$("#division").change(function(){
		var divisionId = $("#division").val();
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "fetchBatchesForDivision",
					 regId:'',
					 divisionId:divisionId,						 
			   		},
			   type:"POST",
			   success:function(e){
				   $('#batchSelect').empty();
				   var batchDataArray = [];
				    var data = JSON.parse(e);
				   /* $.each(data.batches,function(key,val){
						var data = {};
						data.id = val.batch_id;
						data.text = val.batch_name;
						batchDataArray.push(data);
					});
				    $("#batchSelect").select({data:batchDataArray,placeholder:"type batch name"});*/
				    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
				    if(data.batches != null){
				    	$.each(data.batches,function(key,val){
				    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
						});
				    }
			   	},
			   error:function(e){
				   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
					$('div#addStudentModal .error').show();
			   }
			   
		});
	});
	
	$("#batchSelect").change(function(){
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createQuestionPaperListTable(e);
		}
		handlers.error = function(e){console.log("Error",e)}
		var division = $("#division").val(); 
		var batch = $("#batchSelect").val(); 
		rest.get("rest/classownerservice/getQuestionPaperList/"+division,handlers);
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
				 methodToCall: "fetchBatchSubject",
	    		 batchName:batch,
	    		 batchdivision:division
		   		},
		   type:"POST",
		   success:function(data){
			   data = JSON.parse(data);
			   if(data.subjectstatus == ""){
				   var subjectnames = data.Batchsubjects;
				   var subjectIds = data.BatchsubjectsIds;
				   var i = 0;
				   var subjectnameArray = subjectnames.split(",");
					var subjectidArray =  subjectIds.split(",");  
					subjectList = [];
					$(".subjectDiv").empty();
					var tableRow = "<table class='table table-striped'>";
					while(i < subjectnameArray.length){
						tableRow += "<tr><td><div class='examSubjectPapers'><div class='col-md-3'><input type='checkbox' value='"+subjectidArray[i]+"' name='subjectCheckbox' id='subjectCheckbox'>"+
		   				subjectnameArray[i]+"</div><div class='col-md-4'>"+
		   				"<button class='btn btn-primary btn-xs chooseQuestionPaper'>Choose Question Paper</button>"+
		   				"<span class='questionPaperName'></span><input type='hidden' class='form-control selectedQuestionPaperID'></div><div class='col-md-1'><input type='text' class='form-control marks' readOnly></div>"+
		   				"<div class='col-md-3'><div class='col-md-6'>Duration  : </div><div class='col-md-3'><input type='number' class='form-control examHour' placeholder='HH'></div><div class='col-md-3'><input type='number' class='form-control examMinute' placeholder='MM'></div></div>"+
		   				"<div class='col-md-1'><button class='btn btn-primary btn-xs preview'>Preview</button></div></div></td></tr>"
				   		i++;
				   }
					tableRow +="</table>"
					$(".subjectDiv").append(tableRow);
			   }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	});
	var that;
	$(".subjectDiv").on("click",".chooseQuestionPaper",function(){
		$("#questionPaperListModal").modal("toggle");
		that = $(this);
	});
	
	$("#questionPaperListModal").on("click",".confirmQuestionPaper",function(){
		that.closest("div").find(".questionPaperName").html($(this).closest("div").find(".paperDesc").val());
		that.closest("div").find(".selectedQuestionPaperID").val($(this).attr("id"));
		that.closest("div.examSubjectPapers").find(".marks").val($(this).attr("marks"));
		$("#questionPaperListModal").modal("toggle");
	});
	
	$("#saveExam").click(function(){
		var examName = ""
		var examID = "";
			examName = $("#newExamName").val();
			
		if(!$("#examDataForm").valid()){
			return false;
		}	
		var division = $("#division").val();
		var exam_paperList = [];
		var i = 0;
		for(i=0;i<$(".examSubjectPapers").length;i++){
			
			if($($(".examSubjectPapers")[i]).find("#subjectCheckbox").is(":checked")){
			var exam_paper = {};
			exam_paper.exam_id = examID;	
			exam_paper.div_id = $("#division").val(); 
			exam_paper.batch_id = $("#batchSelect").val(); 
			exam_paper.sub_id = $($(".examSubjectPapers")[i]).find("#subjectCheckbox").val();
			exam_paper.marks = $($(".examSubjectPapers")[i]).find(".marks").val();
			exam_paper.duration = $($(".examSubjectPapers")[i]).find(".examHour").val()+":"+$($(".examSubjectPapers")[i]).find(".examMinute").val();
			exam_paper.question_paper_id = $($(".examSubjectPapers")[i]).find(".selectedQuestionPaperID").val();
			exam_paper.header_id = $("#headerDesc").val();
			exam_paperList.push(exam_paper);
			}
		}
		console.log(exam_paperList);
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		}
		handlers.error = function(e){console.log("Error",e)}
		exam_paperList = JSON.stringify(exam_paperList);
		rest.post("rest/classownerservice/saveExamPaper/"+examName,handlers,exam_paperList);
		});
		
		$("body").on("click",PREVIEW,preview);
});

/* function Exam_Paper(){
	
}
 */

function createQuestionPaperListTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#questionPaperListTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Paper Description",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.paper_description+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Marks",data:null,render:function(data,event,row){
				return row.marks;
			},sWidth:"20%"},
			{ title: "Choose",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
				'<input type="button" class="btn btn-sm btn-primary confirmQuestionPaper" value="Choose" id="'+row.paper_id+'" marks="'+row.marks+'">'+
					'<input type="hidden" value="'+row.paper_description+'" class="paperDesc">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	
}

function preview(){
	var paperId = $(this).closest('.examSubjectPapers').find(SELECTED_QUETION_ID).val();
	var headerId = $(HEADER_DESC).val();
	var divisionId = $(DIVISION).val();
	var handler = {};
	handler.success = previewSuccess;
	handler.error = previewError;
	
	var handlerHeader = {};
	handlerHeader.success = getHeaderSuccess;
	handlerHeader.error = getHeaderError;
	rest.get(getQuestionPaperUrl+divisionId+"/"+paperId,handler);
	rest.get(getHeaderUrl+headerId,handlerHeader);
}

function previewSuccess(data){
	var questionPaperFileElementList = data.questionPaperFileElementList;
	$.each(questionPaperFileElementList,function(){
			var parentDiv = $("<div/>",{
					class:"row",
					item_type:this.item_type,
					item_id:this.item_id
				});
			
			$("<div/>",{
				text:this.item_no != -1 ? this.item_no : "",
				style:'text-align:left;',
				class:"col-xs-1",
			}).appendTo(parentDiv);
			
			if(this.item_type == ITEM_TYPE.SECTION){
				var	div = $("<div/>",{
						text:this.item_description,
						style:'text-align:center;',
						class:"col-xs-10"
					}).appendTo(parentDiv);
			}
			
			if(this.item_type == ITEM_TYPE.INSTRUCTION){
				var	div = $("<div/>",{
						text:this.item_description,
						style:'text-align:left;',
						class:"col-xs-10"
					}).appendTo(parentDiv);
			}

			if(this.item_type == ITEM_TYPE.QUESTION){
					$("<div/>",{
						text:this.questionbank.que_text,
						style:'text-align:left;',
						class:"col-xs-10",
					}).appendTo(parentDiv);
			}
			
				$("<div/>",{
					text:this.item_marks,
					style:'text-align:left;',
					class:"col-xs-1"
				}).appendTo(parentDiv);
					
				if(this.parent_id){
					$(PREVIEW_PAGE_CONTENT).find('[item_id="'+this.parent_id+'"]').append(parentDiv);
				}else{
					$(PREVIEW_PAGE_CONTENT).append(parentDiv);
				}
	});
}

function previewError(error){
	console.log(error);
}

function getHeaderSuccess(e){
	console.log(e);
	$(PREVIEW_PAGE_HEADER).html(e);
}

function getHeaderError(e){
	
}