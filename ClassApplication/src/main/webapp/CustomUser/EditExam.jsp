<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
 .error{
     color: red;
    margin-left: 10px;
}
.subjectDiv .row{
padding-right: 0px;
width: 100%
}
.form-control{
font-size: 12px;
padding: 2px 2px;
height: 26px
}
.subjectDiv .col-md-1,.subjectDiv .col-md-3,.subjectDiv .col-md-4{
padding-right: 2px;
padding-left: 2px;
}
</style>
	<script src="js/summernote/summernote.js"></script>
	<script src="js/summernote-ext-print.js"></script>
    <script src="js/customPlugin/cfSummerNote.js"></script>	
<script>
var child_mod_access = [];
var headerText = "";
var division = "";
var batch = "";
var queationPaperList = [];
var PREVIEW_PAGE_HEADER = "#preview_pageHeader";
var MARKS = "#meta #marks";
var PREVIEW_PAGE_CONTENT = "#preview_pageContent";
var SELECTED_QUETION_ID = ".selectedQuestionPaperID";
var HEADER_DESC = "#headerDesc";
var DIVISION = "#division";
var examUrl = "rest/customuserservice/exam/";

var baseURL = "/rest/customuserservice/";
var getQuestionPaperUrl = baseURL + "getQuestionPaper/";
var getHeaderUrl = baseURL+"getHeader/";
var ITEM_TYPE = {
		SECTION:"Section",
		QUESTION:"Question",
		INSTRUCTION:"Instruction"
	}
	var BATCH_SELECT = "#batchSelect";
	var PREVIEW_PAGE = "#preview_page";

$(document).ready(function(){
	child_mod_access  = $("#accessRights").val().split(",");
	$("body").on("click",".deleteExam",deleteExam).
		on("click",".preview",preview);
	
	$("#batchSelect").change(function(){
		$("#examListDiv").hide();
		$("#editModeDiv").hide();
	});
	
	$("#division").change(function(){
		$("#examListDiv").hide();
		$("#editModeDiv").hide();
		var divisionId = $("#division").val();
		if(divisionId != "-1"){
			var handlers = {};
			handlers.success=function(data){
				$('#batchSelect').empty();
				var batchDataArray = [];
				   if(data.length > 0){
					    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
					    	 $("#batchSelect").select2().val("-1").change();
					    	$.each(data,function(key,val){
					    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
							});
					    }else{
					    	 $("#batchSelect").empty();
							 $("#batchSelect").select2().val("").change();
							 $("#batchSelect").select2({data:"",placeholder:"Batch not available"});
					    }
			};
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			
			rest.get("rest/customuserservice/getBatches/"+divisionId,handlers);
		}else{
			 $("#batchSelect").empty();
			 $("#batchSelect").select2().val("").change();
			 $("#batchSelect").select2({data:"",placeholder:"Select Batch"});
		}
	});
	
	$("#searchExam").click(searchExams);
	var editExamID = "";
	$("#examList").on("click",".editExam",function(){
		$(".examPaperID").val("");
		$(".marks").val("");
		$(".examHour").val("");
		$(".examMinute").val("");
		$(".examPaperID").val("");
		$(".selectedQuestionPaperID").val("");
		$(".examMinute").val("");
		$(".paper-type").val("-1");
		$("input[name=subjectCheckbox]").prop("checked",false);
		$(".passMarks").val("");
		$("#examListDiv").hide();
		$("#editModeDiv").show();
		division = $("#division").val();
		$(".editExamName").html($(this).closest("tr").find(".defaultExamName").html());
		editExamID = $(this).prop("id");
		var handlers = {};
		handlers.success = function(e){
			var innerHandlers = {};
			innerHandlers.success = function(data){
				queationPaperList = data;
				for(i=0;i<$(".examSubjectPapers").length;i++){
					for(j=0;j<e.length;j++){
						$('#headerDesc').val(e[j].header_id);
					 if($($(".examSubjectPapers")[i]).find("#subjectCheckbox").val() == e[j].sub_id){
						 $($(".examSubjectPapers")[i]).find("#subjectCheckbox").prop('checked', true);
						 $($(".examSubjectPapers")[i]).find(".marks").val( e[j].marks);
						 $($(".examSubjectPapers")[i]).find(".examHour").val(e[j].duration.split(":")[0]);
						 $($(".examSubjectPapers")[i]).find(".examMinute").val(e[j].duration.split(":")[1]);
						 $($(".examSubjectPapers")[i]).find(".examPaperID").val(e[j].exam_paper_id);
						 $($(".examSubjectPapers")[i]).find(".paper-type").val(e[j].paper_type);
						 $($(".examSubjectPapers")[i]).find(".passMarks").val(e[j].pass_marks);
						 for(k=0;k<queationPaperList.length;k++){
							 if(queationPaperList[k].paper_id == e[j].question_paper_id){
								 $($(".examSubjectPapers")[i]).find(".questionPaperName").html(queationPaperList[k].paper_description);
								 $($(".examSubjectPapers")[i]).find(".selectedQuestionPaperID").val(queationPaperList[k].paper_id);
							 }
						 }
						} 
					}
					}
			}
			innerHandlers.error = function(data){$.notify({message: "Error"},{type: 'danger'});};
			rest.get("rest/customuserservice/getExamQuestionPaperList/"+division+"/"+editExamID,innerHandlers);
			
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/customuserservice/getExam/"+division+"/"+$(this).prop("id")+"/"+batch,handlers);
	});
	
	$(".cancelEdit").click(function(){
		$("#examListDiv").show();
		$("#editModeDiv").hide();
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
		rest.get("rest/customuserservice/getQuestionPaperList/"+division+"/"+subject,handlers);
	});
	
	$("#questionPaperListModal").on("click",".confirmQuestionPaper",function(){
		that.closest("tr").find(".questionPaperName").html($(this).closest("div").find(".paperDesc").val());
		that.closest("tr").find(".selectedQuestionPaperID").val($(this).attr("id"));
		that.closest("tr.examSubjectPapers").find(".marks").val($(this).attr("marks"));
		$("#questionPaperListModal").modal("toggle");
	});
	
	$("#saveExam").click(function(){
		$(".subjectError").html("");
		var examName = ""
		var examID = "";
		var exam_paperList = [];
		var i = 0;
		for(i=0;i<$(".examSubjectPapers").length;i++){
			
			if($($(".examSubjectPapers")[i]).find("#subjectCheckbox").is(":checked")){
			var exam_paper = {};
			exam_paper.exam_id = editExamID;	
			exam_paper.div_id = $("#division").val(); 
			exam_paper.batch_id = batch; 
			exam_paper.sub_id = $($(".examSubjectPapers")[i]).find("#subjectCheckbox").val();
			exam_paper.marks = $($(".examSubjectPapers")[i]).find(".marks").val();
			exam_paper.duration = $($(".examSubjectPapers")[i]).find(".examHour").val()+":"+$($(".examSubjectPapers")[i]).find(".examMinute").val();
			exam_paper.question_paper_id = $($(".examSubjectPapers")[i]).find(".selectedQuestionPaperID").val();
			exam_paper.header_id = $("#headerDesc").val();
			exam_paper.exam_paper_id =  $($(".examSubjectPapers")[i]).find(".examPaperID").val();
			exam_paper.paper_type = $($(".examSubjectPapers")[i]).find(".paper-type").val();
			exam_paper.pass_marks = $($(".examSubjectPapers")[i]).find(".passMarks").val();
			exam_paperList.push(exam_paper);
			}
		}
		console.log(exam_paperList);
		if(exam_paperList.length > 0 ){
		var handlers = {};
		handlers.success = function(e){
			$.notify({message: "Exam updated successfuly"},{type: 'success'});
			$(".cancelEdit").trigger("click");
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
		exam_paperList = JSON.stringify(exam_paperList);
		rest.post("rest/customuserservice/updateExamPaper/"+editExamID+"/"+division+"/"+batch,handlers,exam_paperList);
		}else{
			$(".subjectError").html("Please select subjects");
		}
		});
});

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

function createExamListTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#examList').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Exam Description",data:null,render:function(data,event,row){
				var div = '<div class="default defaultExamName">'+row.exam_name+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Edit",data:null,render:function(data,event,row){
				var buttons = '<div class="default">';
				if($.inArray( "32", child_mod_access) != "-1"){
					buttons = buttons +'<input type="button" class="btn btn-xs btn-primary editExam" value="Edit" id="'+row.exam_id+'">';
				}
				buttons = buttons + '</div>';
				return buttons;
			},sWidth:"10%"},
			{ title: "Delete",data:null,render:function(data,event,row){
				var buttons = '<div class="default">';
				if($.inArray( "33", child_mod_access) != "-1"){
					buttons = buttons +'<input type="button" class="btn btn-xs btn-danger deleteExam" value="Delete" id="'+row.exam_id+'">';
				}
				buttons = buttons +'</div>';
				return buttons;
			},sWidth:"10%"}
		]
	});
	
}

function searchExams(){
	$("#examListDiv").hide();
	$("#editModeDiv").hide();
	$(".validation-message").html("");
	division = $("#division").val();
	batch =  $("#batchSelect").val();
	var validationFlag = false;
	if(division == "-1" || division == "" || division == null){
		$("#divisionError").html("Select Class");
		validationFlag = true;
	}
	
	if(batch == "-1" || batch == "" || batch == null){
		$("#batchError").html("Select Batch");
		validationFlag = true;
	}
	if(validationFlag == false){
	var handlers = {};
	handlers.success = function(e){console.log("Success",e);
	$("#examListDiv").show();
	createExamListTable(e);};
	handlers.error = function(e){
		$.notify({message: "Error while fetching exam list"},{type: 'danger'});
	};
	rest.post("rest/customuserservice/getExamList/"+division+"/"+batch,handlers);
	
	/* var handler = {};
	handler.success = function(e){console.log("Success",e);
	queationPaperList = e;
	createQuestionPaperListTable(e);
	}
	handler.error = function(e){
		$.notify({message: "Error"},{type: 'danger'});
	}
	rest.get("rest/classownerservice/getQuestionPaperList/"+division,handler); */
	var handler = {};
	handler.success=function(data){
		   if(data.length > 0){
			    var i = 0; 
				subjectList = [];
				$(".subjectDiv").empty();
				var tableRow = "<table class='table table-striped'>";
				tableRow += "<tr><th width='5%'></th><th width='10%'>Subjects</th>" +
				"<th width='30%'>Question Paper</th>"+
				"<th width='10%'>Marks</th>"+
				"<th width='10%'>Passing Marks</th>"+
				"<th width='20%'>Duration</th>"+
				"<th width='10%'>Paper Type</th><th width='5%'>Preview</th></tr>"

				while(i < data.length){
					tableRow += "<tr class='examSubjectPapers'><td><input type='checkbox' value='"+data[i].subjectId+"' name='subjectCheckbox' id='subjectCheckbox'></td><td>"+
					data[i].subjectName+"</td><td>"+
	   				"<button class='btn btn-primary btn-xs chooseQuestionPaper'>Choose Question Paper</button>"+
	   				"<span class='questionPaperName'></span><input type='hidden' class='form-control selectedQuestionPaperID'></td><td><input type='text' class='form-control marks' placeholder='Marks' title='Marks of selected question paper'></td>"+
	   				"<td><input type='text' class='form-control passMarks' placeholder='Passing' title='Passing Marks'></td>"+
	   				"<td><div class='col-md-3'><input type='number' class='form-control examHour' placeholder='HH'></div><div class='col-md-3'><input type='number' class='form-control examMinute' placeholder='MM'></div></td>"+
	   				"<td><select class='form-control paper-type'><option value='-1'>Paper Type</option><option value='1'>Offline</option><option value='2'>Online</option></select></td><td><button class='btn btn-primary btn-xs preview'>Preview</button></td></tr>"
			   		i++;
			   }
				tableRow +="</table>"
				$(".subjectDiv").append(tableRow);
				$(".actionOption").show();
		   }
	};
	handler.error=function(){
		$.notify({message: "Error"},{type: 'danger'});
	};   
	
	rest.get("rest/customuserservice/getBatchSubjects/"+division+"/"+batch,handler);
	}
}

function deleteExam(){
	var examId = $(this).attr("id");
	var handler = {};
	handler.success = function(e){
		searchExams();
		$.notify({message: "Exam successfuly deleted"},{type: 'success'});
	};
	handler.error = function(e){};
	rest.deleteItem(examUrl+examId,handler,true);
}

function preview(){
	var paperId = $(this).closest('.examSubjectPapers').find(SELECTED_QUETION_ID).val();
	var marks = $(this).closest('.examSubjectPapers').find(".marks").val();
	var headerId = $(HEADER_DESC).val();
	var divisionId = $(DIVISION).val();
	var handler = {};
	var that = this;
	handler.success = function(data){previewSuccess(data,that)};
	handler.error = previewError;
	
	var handlerHeader = {};
	handlerHeader.success = getHeaderSuccess;
	handlerHeader.error = getHeaderError;
	if(paperId.trim()==""){
		$.notify({message: "Please select question paper"},{type: 'danger'});
		return;
	}
	
	rest.get(getHeaderUrl+headerId,handlerHeader,true,false);
	rest.get(getQuestionPaperUrl+divisionId+"/"+paperId,handler,true,false);
	setTimeout(function(){
		$('#editQuestionPaper').summernote('insertNode',$("#pre_editQuestionPaper_view")[0]);	
	},1000);
	
	$(MARKS).html(marks);
}

function previewSuccess(resp,that){
	var subject = $(that).closest("tr").find(".subjectName").text();
	var className = $("#division option:selected").text();
	
	headerText = headerText.replace("{{marks}}",resp.fileObject.marks);
	headerText = headerText.replace("{{ExamName}}",resp.fileObject.paper_description);
	headerText = headerText.replace("{{SubjectName}}",subject);
	headerText = headerText.replace("{{Standard}}",className);
	
	$('#editQuestionPaper').show();
	$("#pre_editQuestionPaper_view").find("#header").html(headerText);
	
	$("#pre_editQuestionPaper_view").find("#qPaper").empty();
	var data = resp.fileObject;
	var questionPaperFileElementList = data.questionPaperFileElementList;
	$(PREVIEW_PAGE_CONTENT).empty();
	$.each(questionPaperFileElementList,function(){
			var parentDiv = $("<div/>",{
					class:"row",
					item_type:this.item_type
					
				});
			
			$("<div/>",{
				text:this.item_no != -1 ? this.item_no : "",
				style:'text-align:left;',
				class:"col-xs-1",
			}).appendTo(parentDiv);
			
			if(this.item_type == ITEM_TYPE.SECTION){
				var	div = $("<div/>",{
						html:"<b>"+this.item_description+"</b>",
						style:'text-align:center;',
						class:"col-xs-10"
					}).appendTo(parentDiv);
			}
			
			if(this.item_type == ITEM_TYPE.INSTRUCTION){
				var	div = $("<div/>",{
						html:"<b>"+this.item_description+"</b>",
						style:'text-align:left;',
						class:"col-xs-10"
					}).appendTo(parentDiv);
			}

			if(this.item_type == ITEM_TYPE.QUESTION && this.questionbank != null){
				if(this.questionbank.que_type == "3"){
					$("<div/>",{
						text:this.paragraphQuestion.paragraphText,
						style:'text-align:left;float:left;width:80%;',
						
					}).appendTo(parentDiv);
				}else{
					$("<div/>",{
						id:"questionDiv",
						html:"<div>"+this.questionbank.que_text+"</div>",
						style:'text-align:left;float:left;width:80%;',
						
					}).appendTo(parentDiv);
					
					if(this.questionbank.primaryImage){
						for(var imageSrcIndex in this.questionbank.primaryImage){
							var imgSrc = "/rest/commonservices/image/"+this.questionbank.primaryImage[imageSrcIndex];
							var img = $("<img/>",{
								src:imgSrc,
								width:"100px"
								
							});
							
							$("<div/>",{
								style:'text-align:left;float:left;width:80%;',
								
							}).append(img).appendTo(parentDiv);
									
						}
					}
					//Question type 2 is Subjective
					if(this.question_type == 2){
						console.log(this.questionbank.que_text);
						for(var index=1;index<10;index++){
							if(this.questionbank["opt_"+index]){
								$("<div/>",{
									text: index+") "+this.questionbank["opt_"+index],
									style:'text-align:left;float:left;width:80%;',
									
								}).appendTo(parentDiv.find("div#questionDiv"));
							}
						}	
					}
					
				}
			}if(this.item_type == ITEM_TYPE.QUESTION && this.questionbank == null){
				$("<div/>",{
					text:'Question not available',
					style:'text-align:left;color:red',
					class:"col-xs-10",
				}).appendTo(parentDiv);
			}
			
				$("<div/>",{
					text:this.item_marks,
					style:'text-align:left;',
					class:"col-xs-1"
				}).appendTo(parentDiv);
					
				var containerDiv = $("<div/>",{class:"container",item_id:this.item_id});
				containerDiv.append(parentDiv);
				
				if(this.parent_id!="undefined" && this.parent_id != null){
					$("#pre_editQuestionPaper_view").find("#qPaper").find('[item_id="'+this.parent_id+'"]').append(containerDiv);
				}else{
					$("#pre_editQuestionPaper_view").find("#qPaper").append(containerDiv);
				}
	});
	
	//$('#editQuestionPaper').summernote('insertNode',$(PREVIEW_PAGE_CONTENT)[0]);
	//$("#examPreviewModal").modal("show");
}

function previewError(error){
	console.log(error);
}

function getHeaderSuccess(e){
	headerText = e;
}

function getHeaderError(e){
	
}
</script>
</head>
<body>
<jsp:include page="ExamHeader.jsp" >
		<jsp:param value="active" name="customeUserEditExam"/>
	</jsp:include>
	<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
	<div class="well">
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
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="validation-message"></span>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchExam">Search</button>
			</div>
		</div>
	</div>
	<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
	<div class="container" id="examListDiv">
		<table id="examList"></table>
	</div>
	<div id="editModeDiv" style="display: none;" class="container">
	<div class="row">
			<div class="col-md-2"><button class="btn btn-primary btn-xs cancelEdit" value="Cancel Edit">Cancel Edit</button></div>
	</div>
	<div class="row">
			<div class="col-md-offset-4 col-md-2">
				<span class="editExamName"></span>
			</div>
	</div>
		<div class="row">
			<div class="col-md-2">Select Header</div>
			<div class="col-md-3">
				<select id="headerDesc" name="headerDesc" class="form-control">
					<option value="-1">Select Header</option>
					<c:forEach items="${headerList}" var="headerX">
						<option value="<c:out value="${headerX.header_id}"></c:out>">
							<c:out value="${headerX.header_name}"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div>
		<div class="subjectError error"></div>
		<div class="row subjectDiv">
			
		
		</div>
		<div class="actionOption">
			<button class="btn btn-primary btn-sm" value="Save" id="saveExam">Save</button>
		</div>
		</div>
	</div>
	<div id="editQuestionPaperPreviewDiv" style="display: none">
	<button class="btn btn-primary btn-sm" id="cancelPreview">Cancel preview</button>
		<form>
	        	<div id="editQuestionPaper" class="summernote"></div>
	    </form>	
	    <div id="pre_editQuestionPaper_view" class="pre_summernote">
	    	<div id="header"></div>
	    	<hr/>
	    	<div id="qPaper"></div>
	    </div>
    </div>
	<div class="modal fade" id="questionPaperListModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title" id="myModalLabel">Question Paper list</h4>
		  </div>
		  <div class="modal-body">
			<div id="questionPaperList">
				<table id="questionPaperListTable" style="width:100%;"></table>
			</div>
		  </div>
		</div>
	  </div>
	</div>
	<div class="modal fade" id="examPreviewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title" id="myModalLabel">Question Paper list</h4>
		  </div>
		  <div class="modal-body">
		  
		  	<div id="preview_pageHeader">
				
			</div>
			<div id="meta">
				Marks:-<span id="marks"></span>
			</div>
			<hr/>
			<div id="preview_pageContent">
				
			</div>
		  </div>
		</div>
	  </div>
	</div>	
</body>
</html>