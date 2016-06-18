var PREVIEW = ".preview";
var SELECTED_QUETION_ID = ".selectedQuestionPaperID"
var HEADER_DESC = "#headerDesc"
var DIVISION = "#division";
var PREVIEW_PAGE_HEADER = "#preview_pageHeader";
var MARKS = "#meta #marks";
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
		$(".subjectDiv").empty();
		$(".actionOption").hide();
		var divisionId = $("#division").val();
		if(divisionId != "-1"){
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
				    if(data.status != "error"){
				    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
				    if(data.batches != null){
				    	 $("#batchSelect").select2().val("-1").change();
				    	$.each(data.batches,function(key,val){
				    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
						});
				    }
				    }else{
				    	 $("#batchSelect").empty();
						 $("#batchSelect").select2().val("").change();
						 $("#batchSelect").select2({data:"",placeholder:"Batch not available"});
				    }
			   	},
			   error:function(e){
				   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
					$('div#addStudentModal .error').show();
			   }
			   
		});
		}else{
			 $("#batchSelect").empty();
			 $("#batchSelect").select2().val("").change();
			 $("#batchSelect").select2({data:"",placeholder:"Select Batch"});
		}
	});
	
	$("#batchSelect").change(function(){
		$(".actionOption").hide();
		$(".subjectDiv").empty();
		var division = $("#division").val(); 
		var batch = $("#batchSelect").val(); 
		if(batch != "-1" && batch!= "" && batch != null){
		
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
					tableRow += "<tr><th width='5%'></th><th width='10%'>Subjects</th>" +
					"<th width='40%'>Question Paper</th>"+
	   				"<th width='10%'>Marks</th>"+
	   				"<th width='20%'>Duration</th>"+
	   				"<th width='10%'>Paper Type</th><th width='5%'>Preview</th></tr>"

					while(i < subjectnameArray.length){
						tableRow += "<tr class='examSubjectPapers'><td><input type='checkbox' value='"+subjectidArray[i]+"' name='subjectCheckbox' id='subjectCheckbox'></td><td>"+
		   				subjectnameArray[i]+"</td><td>"+
		   				"<button class='btn btn-primary btn-xs chooseQuestionPaper'>Choose Question Paper</button>"+
		   				"<span class='questionPaperName'></span><input type='hidden' class='form-control selectedQuestionPaperID'></td><td><input type='text' class='form-control marks' placeholder='Marks' title='Marks of selected question paper'></td>"+
		   				"<td><div class='col-md-3'><input type='number' class='form-control examHour' placeholder='HH'></div><div class='col-md-3'><input type='number' class='form-control examMinute' placeholder='MM'></div></td>"+
		   				"<td><select class='form-control paper-type'><option value='-1'>Paper Type</option><option value='1'>Offline</option><option value='2'>Online</option></select></td><td><button class='btn btn-primary btn-xs preview'>Preview</button></td></tr>"
				   		i++;
				   }
					tableRow +="</table>"
					$(".subjectDiv").append(tableRow);
					$(".actionOption").show();
			   }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
		}
	});
	var that;
	$(".subjectDiv").on("click",".chooseQuestionPaper",function(){
		var subject = $($(this).closest("tr")).find("#subjectCheckbox").val();
		var division = $("#division").val(); 
		that = $(this);
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createQuestionPaperListTable(e);
		$("#questionPaperListModal").modal("toggle");
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getQuestionPaperList/"+division+"/"+subject,handlers);
		
		
	});
	
	$("#questionPaperListModal").on("click",".confirmQuestionPaper",function(){
		that.closest("td").find(".questionPaperName").html($(this).closest("div").find(".paperDesc").val());
		that.closest("td").find(".selectedQuestionPaperID").val($(this).attr("id"));
		that.closest("tr.examSubjectPapers").find(".marks").val($(this).attr("marks"));
		$("#questionPaperListModal").modal("toggle");
	});
	
	$("#saveExam").click(function(){
		$(".subjectError").html("");
		$(".errorDiv").html("");
		var flag = false;
		var examName = ""
		var examID = "";
		if($("#examSelect").val() != "-1"){
			examID = $("#examSelect").val();
			examName = "-1";
		}else{
			examName = $("#newExamName").val();
		}	
		if(examName.trim() == "" && examID==""){
			$(".errorDiv").html("Please select or enter exam name");
			flag = true;
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
			exam_paper.paper_type = $($(".examSubjectPapers")[i]).find(".paper-type").val();
			exam_paperList.push(exam_paper);
			}
		}
		console.log(exam_paperList);
		if(exam_paperList.length == 0){
			$(".subjectError").html("Please select subjects");
			flag = true;
		}
		if(flag == false){
		var handlers = {};
		handlers.success = function(e){
			if(e == ""){
			$.notify({message: "Exam created successfuly"},{type: 'success'});
			}else{
				$(".errorDiv").html(e);
			}
		}
		handlers.error = function(e){
			$.notify({message: "Exam creation cause error"},{type: 'danger'});
		}
		exam_paperList = JSON.stringify(exam_paperList);
		rest.post("rest/classownerservice/saveExamPaper/"+examName,handlers,exam_paperList);
		}
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
	var marks = $(this).closest('.examSubjectPapers').find(".marks").val();
	var headerId = $(HEADER_DESC).val();
	var divisionId = $(DIVISION).val();
	var handler = {};
	handler.success = previewSuccess;
	handler.error = previewError;
	
	var handlerHeader = {};
	handlerHeader.success = getHeaderSuccess;
	handlerHeader.error = getHeaderError;
	if(paperId.trim()==""){
		$.notify({message: "Please select question paper"},{type: 'danger'});
		return;
	}
	rest.get(getQuestionPaperUrl+divisionId+"/"+paperId,handler);
	rest.get(getHeaderUrl+headerId,handlerHeader);
	
	$(MARKS).html(marks);
}

function previewSuccess(resp){
	var data = resp.fileObject;
	var questionPaperFileElementList = data.questionPaperFileElementList;
	$(PREVIEW_PAGE_CONTENT).empty();
	$.each(questionPaperFileElementList,function(){
			var parentDiv = $("<div/>",{
					item_type:this.item_type,
					item_id:this.item_id
				});
			
			var sparentDiv = $("<div/>",{
				
			});
			
			$("<div/>",{
				text:this.item_no != -1 ? this.item_no : "",
				style:'text-align:left;width:10%;float:left;',
				
			}).appendTo(sparentDiv);
			
			if(this.item_type == ITEM_TYPE.SECTION){
				var	div = $("<div/>",{
						text:this.item_description,
						style:'text-align:center;font-weight:bold;width:80%;float:left;',
						
					}).appendTo(sparentDiv);
			}
			
			if(this.item_type == ITEM_TYPE.INSTRUCTION){
				var	div = $("<div/>",{
						text:this.item_description,
						style:'text-align:left;width:80%;float:left;',
						
					}).appendTo(sparentDiv);
			}

			if(this.item_type == ITEM_TYPE.QUESTION){
					$("<div/>",{
						text:this.questionbank.que_text,
						style:'text-align:left;float:left;width:80%;',
						
					}).appendTo(sparentDiv);
			}
			
				$("<div/>",{
					text:this.item_marks,
					style:'text-align:left;float:left;width:10%;',
					
				}).appendTo(sparentDiv);
				(sparentDiv).appendTo(parentDiv);	
				if(this.parent_id!="undefined" &&this.parent_id!=null){
					$(PREVIEW_PAGE_CONTENT).find('[item_id="'+this.parent_id+'"]').append(parentDiv);
				}else{
					$(PREVIEW_PAGE_CONTENT).append(parentDiv);
				}
	});
	$("#examPreviewModal").modal("show");
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