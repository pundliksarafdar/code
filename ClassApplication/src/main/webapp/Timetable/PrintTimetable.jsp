<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var getTimetable = "rest/schedule/getWeeklyScheduleForPrint/";
$(document).ready(function(){
	$("body").on("change",DIVISION_SELECT,getBatches);
	$("#search").click(function(){
		getTimeTableData();
	});
	 $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	 
	 $("#print").click(function(){
			var newWin= window.open("");
			newWin.document.write("<html><link href='/css/bootstrap.min.css' rel='stylesheet'><body class='container'>"+$("#printData").html()+"</body></html>");
			newWin.print();
			newWin.close();
	 });
});

function getBatches(){
	var divisionId = $('#divisionSelect').val();
	$("#batchSelect").val("-1")
	$("#batchSelect").find('option:gt(0)').remove();
	 $("#commonAttendanceDiv").hide();
	$("#attendanceScheduleDiv").hide();
	$("#attendanceStudentListDiv").hide();
	if(divisionId != "-1"){		
	$("#divisionError").html("");
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
		    if(data.status != "error"){
		    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
		    $("#batchSelect").select2().val("-1").change();
		    if(data.batches != null){
		    	$.each(data.batches,function(key,val){
		    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
				});
		    }}else{
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
}

function getTimeTableData(){
	$("#printButton").hide();
	$("#printDataTable").empty();
	var divId = $('#divisionSelect').val();
	var batchId = $('#batchSelect').val();
	var startDate = $("#date").val().trim().split("/").reverse().join("-");
	var handler = {};
	handler.success = function(data){
	console.log(data);
	var stringData = "";
	if(data != null){
		
		var maxSize = 0;
		stringData = stringData +"<tr>";
		$.each(data, function(key, val) {
			var date = new Date(key);
			key =moment(moment(date).format("DD/MM/YYYY"));
			stringData = stringData +"<th>"+key._i+"</th>";
			if(maxSize < val.length){
				maxSize = val.length;
			}
		});
		stringData = stringData +"</tr>";
		if(maxSize > 0){
		var i = 0;
		while(i<maxSize){
		$("#printButton").show();
		stringData = stringData +"<tr>";	
		$.each(data, function(key, val) {
			if(val.length > i){
			var stTime = new Date(val[i].start).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
			var edTime = new Date(val[i].end).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
			stringData = stringData +"<td>"+val[i].subjectname+"<br>"+stTime+"-"+edTime+"</td>";
			}else{
				stringData = stringData +"<td></td>";
			}
		});
		stringData = stringData +"</tr>";
		i++;
		}
		$("#printDataTable").append(stringData);
		}else{
			$.notify({message: "Lectures are not scheduled in selected week"},{type: 'danger'});
		}
	}
	};
	handler.error = function(e){};
	if(batchId != "-1" && batchId!=null && batchId != ""){
	rest.get(getTimetable+batchId+"/"+divId+"?startDate="+startDate,handler);
	}
}

</script>
</head>
<body>
<jsp:include page="TimetableHeader.jsp" >
		<jsp:param value="active" name="printTimetable"/>
</jsp:include>
<div class="well">
		<div class="row">
			<div class="col-md-3">
				<select class="form-control" id="divisionSelect">
					<option value="-1">Select Division</option>
					<c:forEach var="division" items="${divisions}">
						<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out> <c:out value="${division.stream}"></c:out></option>
					</c:forEach>
				</select>
				<span id="divisionError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<select class="form-control" id="batchSelect">
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="validation-message"></span>
			</div>
			<div class="col-md-4">
				<div id="datetimepicker" class="input-group" style="width :240px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Start Date of week" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				<span id="dateError" class="validation-message"></span>
			</div>
			<div class="col-md-1">
				<button class="btn btn-primary btn-sm" id="search">Search</button>
			</div>
		</div>
	</div>
<div class="container" id="printButton" style="display: none;margin-left: 1.3%">
<div class="row">
			<div class="col-md-3" style="padding-left: 0%">
				<button id="print" class="btn btn-success">Print</button>
			</div>
</div>
</div>	
<div id="printData">
	<table class="table table-bordered" id="printDataTable"></table>
</div>
</body>
</html>