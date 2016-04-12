<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
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
<script>
var division = "";
var batch = "";
var queationPaperList = [];
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
	
	$("#searchExam").click(function(){
		division = $("#division").val();
		batch =  $("#batchSelect").val();
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createExamListTable(e);};
		handlers.error = function(e){
			$.notify({message: "Error while fetching exam list"},{type: 'danger'});
		};
		rest.post("rest/classownerservice/getExamList/"+division+"/"+batch,handlers);
		var handler = {};
		handler.success = function(e){console.log("Success",e);
		queationPaperList = e;
		createQuestionPaperListTable(e);
		}
		handler.error = function(e){
			$.notify({message: "Error"},{type: 'danger'});
		}
		rest.get("rest/classownerservice/getQuestionPaperList/"+division,handler);
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
					while(i < subjectnameArray.length){
				   		$(".subjectDiv").append("<div class='row well examSubjectPapers'><div class='col-md-3'><input type='checkbox' value='"+subjectidArray[i]+"' name='subjectCheckbox' id='subjectCheckbox'>"+
				   				subjectnameArray[i]+"<input type='hidden' class='examPaperID'></div><div class='col-md-4'>"+
				   				"<button class='btn btn-primary btn-xs chooseQuestionPaper'>Choose Question Paper</button>"+
				   				"<span class='questionPaperName'></span><input type='hidden' class='form-control selectedQuestionPaperID'></div><div class='col-md-1'><input type='text' class='form-control marks'></div>"+
				   				"<div class='col-md-3'><div class='col-md-6'>Duration  : </div><div class='col-md-3'><input type='number' class='form-control examHour' placeholder='HH'></div><div class='col-md-3'><input type='number' class='form-control examMinute' placeholder='MM'></div></div>"+
				   				"<div class='col-md-1'><button class='btn btn-primary btn-xs preview'>Preview</button></div></div>");
				   		i++;
				   }
			   }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	});
	var editExamID = "";
	$("#examList").on("click",".editExam",function(){
		$("input[name=subjectCheckbox]").prop("checked",false);
		$("#examListDiv").hide();
		$("#editModeDiv").show();
		division = $("#division").val();
		$(".editExamName").html($(this).closest("tr").find(".defaultExamName").html());
		editExamID = $(this).prop("id");
		var handlers = {};
		handlers.success = function(e){
		for(i=0;i<$(".examSubjectPapers").length;i++){
			for(j=0;j<e.length;j++){
				$('#headerDesc').val(e[j].header_id);
			 if($($(".examSubjectPapers")[i]).find("#subjectCheckbox").val() == e[j].sub_id){
				 $($(".examSubjectPapers")[i]).find("#subjectCheckbox").prop('checked', true);
				 $($(".examSubjectPapers")[i]).find(".marks").val( e[j].marks);
				 $($(".examSubjectPapers")[i]).find(".examHour").val(e[j].duration.split(":")[0]);
				 $($(".examSubjectPapers")[i]).find(".examMinute").val(e[j].duration.split(":")[1]);
				 $($(".examSubjectPapers")[i]).find(".examPaperID").val(e[j].exam_paper_id);
				 for(k=0;k<queationPaperList.length;k++){
					 if(queationPaperList[k].paper_id == e[j].question_paper_id){
						 $($(".examSubjectPapers")[i]).find(".questionPaperName").html(queationPaperList[k].paper_description);
						 $($(".examSubjectPapers")[i]).find(".selectedQuestionPaperID").val(queationPaperList[k].paper_id);
					 }
				 }
				} 
			}
			}}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/classownerservice/getExam/"+division+"/"+$(this).prop("id")+"/"+batch,handlers);
	});
	
	$(".cancelEdit").click(function(){
		$("#examListDiv").show();
		$("#editModeDiv").hide();
	});
	
	var that;
	$(".subjectDiv").on("click",".chooseQuestionPaper",function(){
		$("#questionPaperListModal").modal("toggle");
		that = $(this);
	});
	
	$("#questionPaperListModal").on("click",".confirmQuestionPaper",function(){
		that.closest("div").find(".questionPaperName").html($(this).closest("div").find(".paperDesc").val());
		that.closest("div").find(".selectedQuestionPaperID").val($(this).attr("id"));
		$("#questionPaperListModal").modal("toggle");
	});
	
	$("#saveExam").click(function(){
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
			exam_paperList.push(exam_paper);
			}
		}
		console.log(exam_paperList);
		var handlers = {};
		handlers.success = function(e){
			$.notify({message: "Exam updated successfuly"},{type: 'success'});
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
		exam_paperList = JSON.stringify(exam_paperList);
		rest.post("rest/classownerservice/updateExamPaper/"+editExamID+"/"+division+"/"+batch,handlers,exam_paperList);
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
					'<input type="button" class="btn btn-sm btn-primary confirmQuestionPaper" value="Choose" id="'+row.paper_id+'">'+
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
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary editExam" value="Edit" id="'+row.exam_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"},
			{ title: "Delete",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-danger deleteExam" value="Delete" id="'+row.exam_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	
}

</script>
</head>
<body>
<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="editExam"/>
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
				<span id="divisionError" class="patternError"></span>
			</div>
			<div class="col-md-3">
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchExam">Search</button>
			</div>
		</div>
	</div>
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
		<div class="row subjectDiv">
			
		
		</div>
		<div class="actionOption">
			<button class="btn btn-primary btn-sm" value="Save" id="saveExam">Save</button>
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
</body>
</html>