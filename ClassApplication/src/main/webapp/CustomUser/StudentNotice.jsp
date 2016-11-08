<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
	$("#batchSelect").select2({placeholder:"Select Batch"})
	$('#startdatetimepicker').datetimepicker({
		format : 'DD-MM-YYYY',
		pickTime : false
	}).data("DateTimePicker").setDate(new Date());
	
	$('#enddatetimepicker').datetimepicker({
		format : 'DD-MM-YYYY',
		pickTime : false
	}).data("DateTimePicker").setDate(new Date());
	
	$("#startdatetimepicker").on("dp.change",function(){
		$('#enddatetimepicker').datetimepicker({
			format : 'DD-MM-YYYY',
			pickTime : false
		}).data("DateTimePicker").setDate($("#startdate").val());
	});
	$("#division").change(function(){
		var divisionId = $("#division").val();
			if(divisionId != "-1"){
			$("#divisionError").html("");
		var handlers = {};
		handlers.success=function(data){
			$('#batchSelect').empty();
			var batchDataArray = [];
			   if(data.length > 0){
				        	$.each(data,function(key,val){
				    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
						});
				        $("#batchSelect").select2({placeholder:"Select Batch"});
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
	
	$("input[name=applicableTo]").change(function(){
		if($("input[name=applicableTo]:checked").val() == "all"){
			$("#division").prop("disabled",true);
			$("#batchSelect").prop("disabled",true);
		}else{
			$("#division").prop("disabled",false);
			$("#batchSelect").prop("disabled",false);
		}
	});
	
	$("#save").click(function(){
		var notice = {};
		if($("input[name=applicableTo]:checked").val() == "all"){
		notice.div_id = 0;
		notice.batch_id = ""
		}else{
			notice.div_id = $("#division").val();
			notice.batch_id = $("#batchSelect").val().join(",")	
		}
		notice.notice = $("#noticeMsg").val();
		notice.start_date = $("#startdate").val().split("-").reverse().join("-");
		notice.end_date = $("#enddate").val().split("-").reverse().join("-");
		var commonHandler = {};
		commonHandler.success = function(e){
			$.notify({message: "Notice saved successfully"},{type: 'success'});
		
		}
		commonHandler.error = function(e){
			$.notify({message: "Notice not saved successfully"},{type: 'danger'});
			}
		rest.post("rest/customuserservice/saveStudentNotice",commonHandler,JSON.stringify(notice));	
	});
});
</script>
</head>
<body>
<jsp:include page="NoticeBoardHeader.jsp" >
		<jsp:param value="active" name="customUserNoticeBoard"/>
	</jsp:include>
<div class="container">
<div class="row" style="padding: 2%">
	<div class="col-md-2">Notice :</div>
	<div class="col-md-6"><textarea id="noticeMsg" class="form-control"></textarea></div>
</div>
<div class="row">
	<div class="col-md-2">Notice Applicable To :</div>
	<div class="col-md-2"><input type="radio" name="applicableTo" value="all" checked="checked"> All Students</div>
	<div class="col-md-2"><input type="radio" name="applicableTo" value="specific"> Specific Student</div>
</div>
<div class="row">
	<div class="col-md-2">Select Class :</div>
	<div class="col-md-3"><select id="division" class="form-control" disabled>
		<option value="-1">Select Class</option>
		<c:forEach items="${divisionList}" var="division" varStatus="counter">
				<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out> &nbsp; <c:out value="${division.stream}"></c:out></option>
		</c:forEach>
	</select></div>
</div>
<div class="row">
	<div class="col-md-2">Select Batch :</div>
	<div class="col-md-3"><select class="form-control" id="batchSelect" disabled multiple="multiple">
				</select>
	</div>
</div>
<div class="row">
	<div class="col-md-2">Start Date :</div>
	<div class="col-md-3"><div id="startdatetimepicker" class="input-group" style="width :250px;">
						<input class="form-control"
							type="text"  id="startdate" required="required"  readonly/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
	</div>
</div>
<div class="row">
	<div class="col-md-2">End Date :</div>
	<div class="col-md-3"><div id="enddatetimepicker" class="input-group" style="width :250px;">
						<input class="form-control"
							type="text"  id="enddate" required="required"  readonly /> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
	</div>
</div>
<div class="row">
	<div class="col-md-2"><button id="save" class="btn btn-sm btn-success">Save</button></div>
</div>
</div>
</body>
</html>