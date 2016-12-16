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
var getTimetable = "rest/customuserservice/getWeeklyScheduleForPrint/";
var getBatchListUrl = "rest/customuserservice/getInstituteBatch/";
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
		 var  data = $("#printData").html();
		 var newWin=window.open('','Print-Window');
		  newWin.document.open();
		  newWin.document.write('<html><link href="/css/bootstrap.min.css" rel="stylesheet"><body onload="window.print()">'+data+'</body></html>');
		  newWin.document.close();
		  setTimeout(function(){newWin.close();},10);
	 });
});

function getBatches(){
	$(BATCH_SELECT).empty();
	var handler = {}
	var division = $(this).val();
	if(division>-1){
		handler.success = function(batches){
			if(batches.length >0){
				var options = "<option value='-1'>Select Batch</option>";
				$.each(batches,function(index,val){
						options = options + "<option value='"+val.batch.batch_id+"'>"+val.batch.batch_name+"</option>";		
				});
				$(BATCH_SELECT).append(options);
				$(BATCH_SELECT).select2().val("-1").change();
				}else{
					$(BATCH_SELECT).empty();
					$(BATCH_SELECT).select2().val("").change();
					$(BATCH_SELECT).select2({data:"",placeholder:"Batch not available"});
				}
		};
		handler.error = function(){};
		rest.get(getBatchListUrl+division,handler);
	}else{
		var options = "<option value='-1'>Select Batch</option>";
		$(BATCH_SELECT).append(options);	
		$(BATCH_SELECT).select2().val("-1").change();
		$(CALENDAR_CONTAINER).hide();
	}
}

function getTimeTableData(){
	$("#printData").hide();
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
		$("#printData").show();
		$("#divName").html($("#divisionSelect option:selected").text());
		$("#batchName").html($("#batchSelect option:selected").text());
		$("#printButton").show();
		stringData = stringData +"<tr>";	
		$.each(data, function(key, val) {
			var teacherName = "";
			if(val.length > 0 && val[i] != undefined){
			var teacher = val[i].teacher.split(" ");
			if(teacher.length > 0){
				$.each(teacher, function(k, name) {
					teacherName = teacherName + name.charAt(0);	
				});
			}
			}
			if(val.length > i){
			var stTime = new Date(val[i].start).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
			var edTime = new Date(val[i].end).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
			stringData = stringData +"<td>"+val[i].subjectname+"/"+teacherName+"<br>"+stTime+"-"+edTime+"</td>";
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
		<jsp:param value="active" name="customUserPrintTimetable"/>
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
<div id="printData" style="display: none">
	<div class="container row">
	<div class="col-md-3">Class : <span id="divName"></span></div>
	<div class="col-md-3">Batch : <span id="batchName"></span></div>
   </div>
	<table class="table table-bordered" id="printDataTable"></table>
</div>
</body>
</html>