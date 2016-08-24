<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var division = "";
var batch = "";
var subject = "";
var exam = "";
$(document).ready(function(){
	$("#batches").select2({data:'',placeholder:"type batch name"});
	$("#division").change(function(){
		getBatchesOfDivision();
	});
	
	$("#batches").change(function(){
		$("#examDiv").hide();
		$("#examSubjectDiv").hide();
	});
	
	$(".searchExams").click(function(){
		$(".validation-message").html("");
		 division = $('#division').val();
		 batch =  $("#batches").val();
		 $("#examDiv").hide();
		$("#examSubjectDiv").hide();
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
		
		createExamTable(e);
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getOnlineExamList/"+division+"/"+batch,handlers);
		}
	});
	
	$("#examTable").on("click",".attemptExamList",function(){
		exam = $(this).prop("id");
		var defaultExamName = $($(this).closest("tr")).find(".defaultExamName").html();
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		$("#examDiv").hide();
		$("#examSubjectDiv").show();
		$("#examSubjectList").html(defaultExamName);
		examSubjectTable(e);
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getOnlineExamSubjectList/"+division+"/"+batch+"/"+$(this).prop("id"),handlers);
	});
	
	$(".back").click(function(){
		$("#examDiv").show();
		$("#examSubjectDiv").hide();
	});
	
	/* $("#examSubjectTable").on("click",".attemptExamStart",function(){
		var division = $('#division').val();
		var batch =  $("#batches").val();
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		/* $("#examDiv").hide();
		$("#examSubjectDiv").show();
		examSubjectTable(e); 
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getOnlineExamPaper/"+division+"/"+$(this).prop("id"),handlers);
	}); */
	
	$("#examSubjectTable").on("click",".attemptExamStart",function(e){
		$("#question_paper_id").val($(this).prop("id"));
		$("#actionform").find("#division").val(division);
		$("#actionform").find("#batch").val(batch);
		$("#actionform").find("#exam").val(exam);
		$("#actionform").find("#subject").val($(this).closest("div").find(".subject").val());
		/* $("#actionform").submit(); */
		e.preventDefault();
		var handlers ={};
		handlers.success = function(e){console.log("Success",e);
		if(e){
			var formData = $("#actionform").serialize();
			console.log(formData);
			var params = [
			              'height='+screen.height,
			              'width='+screen.width,
			              'fullscreen=yes' // only works in IE, but here for completeness
			          ].join(',');
			var win = window.open("attemptExam?"+formData,"",params)
			if(win){win.moveTo(0,0);} 
			
		}else{
			$.notify({message: "Question Paper Not available"},{type: 'danger'});
		}
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/questionPaperAvailability/"+$(this).prop("id")+"/"+division+"/"+$(this).closest("div").find(".subject").val(),handlers);
	});
});

function examSubjectTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#examSubjectTable').DataTable({
		autoWidth: false,
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{title:"#",data:null},
			{ title: "Subject",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.sub_name+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Choose",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary attemptExamStart" value="Attempt" id="'+row.question_paper_id+'">'+
					'<input type="hidden" class="subject" value="'+row.sub_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	 dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw(); 
}


function createExamTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#examTable').DataTable({
		autoWidth: false,
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{title:"#",data:null},
			{ title: "Exam",data:null,render:function(data,event,row){
				var div = '<div class="default defaultExamName">'+row.exam_name+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Choose",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary attemptExamList" value="Attempt" id="'+row.exam_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	 dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw(); 
	 $("#examDiv").show();
}

function getBatchesOfDivision(){
	$("#examDiv").hide();
	$("#examSubjectDiv").hide();
	$(".chkBatch:checked").removeAttr('checked');
	$('#checkboxes').children().remove();
	$('div#addStudentModal .error').hide();
	var divisionId = $('#division').val();

	if(!divisionId || divisionId.trim()=="" || divisionId == -1){
		$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please select a division');
		$('div#addStudentModal .error').show();
		$('#batches').empty();
		 $("#batches").select2({data:null,placeholder:"Select Batch"});
	}else{		
	  $.ajax({
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "fetchBatchesForDivision",
			 regId:'',
			 divisionId:divisionId					 
	   		},
	   type:"POST",
	   success:function(e){
		   $('#batches').empty();
		   var batchDataArray = [];
		    var data = JSON.parse(e);
		    if(data.status != "error"){
		    	var tempData = {};
		    	tempData.id = "-1";
		    	tempData.text = "Select Batch";
				batchDataArray.push(tempData);
		    $.each(data.batches,function(key,val){
				var data = {};
				data.id = val.batch_id;
				data.text = val.batch_name;
				batchDataArray.push(data);
			});
		    $("#batches").select2({data:batchDataArray,placeholder:"type batch name"});
		    }else{
		    	 $("#batches").select2({data:null,placeholder:"Batch not Available"});
		    }
	   	},
	   error:function(e){
		   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
			$('div#addStudentModal .error').show();
	   }
	   
});
	
}
}
</script>
</head>
<body>
<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="onlineExamList"/>
	</jsp:include>
<div class="well">
<div class="row">
<div class="col-md-3">
	<select id="division" class="form-control">
		<option value="-1">Select Class</option>
		<c:forEach items="${divisions}" var="division">
			<option value=<c:out value='${division.divId }'></c:out>><c:out value="${division.divisionName }"></c:out> <c:out value="${division.stream }"></c:out></option>
		</c:forEach>
	</select>
	<span class="validation-message" id="divisionError"></span>
</div>
<div class="col-md-3">
<select id="batches" style="width:100%">
	<option value="-1">Select Batch</option>
</select>
<span class="validation-message" id="batchError"></span>
</div>
<div class="col-md-1">
	<button class="btn btn-primary btn-sm searchExams">Search</button>
</div>
</div>
</div>
<div style="margin-top: 2%">
<div class="container" id="examDiv" style="display: none;">
<div class="row" style="margin-top: 1%" align="center">
<h4>Exam List</h4>
</div>
<div class="row" style="width: 100%">
<table id="examTable" class="table" style="width: 100%"></table>
</div>
</div>
<div class="container" id="examSubjectDiv" style="display: none;">
<div class="row">
<button class="btn btn-primary btn-sm back">Back</button>
</div>
<div class="row" align="center">
<h4 id="examSubjectList" style="margin-top: 1%"></h4>
</div>
<div class="row" style="width: 100%">
<table id="examSubjectTable" class="table" style="width: 100%"></table>
</div>
</div>
</div>
 <form action="attemptExam" id="actionform" method="post" target="blank">
  <input type="hidden" id="subject" name="subject" >
  <input type="hidden" id="exam"  name="exam" >
  <input type="hidden" id="batch"  name="batch">
  <input type="hidden" id="division" name="division" >
  <input type="hidden" name="question_paper_id" id="question_paper_id" >
  <input type="hidden" name="actionname" id="actionname">
  </form>
</body>
</html>