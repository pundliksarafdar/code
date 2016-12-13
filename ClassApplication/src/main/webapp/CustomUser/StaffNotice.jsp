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
	$("#role").select2({placeholder:"Select Roles"});
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
		$("#exam").empty();
		 $("#exam").select2().val("").change();
		 $("#exam").select2({data:"",placeholder:"Select Exam"});
		$("#subjectTableDiv").hide();
		$("#studentTableDiv").hide();
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
	
	$("input[name=applicableTo]").change(function(){
		if($("input[name=applicableTo]:checked").val() == "all"){
			$("#role").prop("disabled",true);
		}else{
			$("#role").prop("disabled",false);
		}
	});
	
	$("#save").click(function(){
		$(".error").html("");
		var flag = true;
		var notice = {};
		if($("input[name=applicableTo]:checked").val() == "all"){
		notice.role = 0;
		}else{
			notice.role = $("#role").val();
			if(notice.role == null){
				$("#roleError").html("Select Role/s");
				flag = false;
			}else{
				notice.role = notice.role.join(",")
			}
		}
		notice.notice = $("#noticeMsg").val().trim();
		notice.start_date = $("#startdate").val().split("-").reverse().join("-");
		notice.end_date = $("#enddate").val().split("-").reverse().join("-");
		var startDate = new Date(notice.start_date);
		var endDate = new Date(notice.end_date);
		if(startDate > endDate){
			$("#dateError").html("End Date Cannot be less than start date");
			flag = false;
		}
		if(notice.notice == ""){
			$("#msgError").html("Please enter notice");
			flag = false;	
		}
		if(flag){
		var commonHandler = {};
		commonHandler.success = function(e){
			$.notify({message: "Notice saved successfully"},{type: 'success'});
		
		}
		commonHandler.error = function(e){
			$.notify({message: "Notice not saved successfully"},{type: 'danger'});
			}
		rest.post("rest/customuserservice/saveStaffNotice",commonHandler,JSON.stringify(notice));	
		}
	});
});
</script>
</head>
<body>
<jsp:include page="NoticeBoardHeader.jsp" >
		<jsp:param value="active" name="customUserStaffNoticeBoard"/>
	</jsp:include>
<div class="container">
<div class="row" style="padding: 2%">
	<div class="col-md-2">Notice :</div>
	<div class="col-md-6"><textarea id="noticeMsg" class="form-control"></textarea>
						<span class="error" id="msgError"></span>	
	</div>
</div>
<div class="row">
	<div class="col-md-2">Notice Applicable To :</div>
	<div class="col-md-2"><input type="radio" name="applicableTo" value="all" checked="checked"> All Staff</div>
	<div class="col-md-2"><input type="radio" name="applicableTo" value="specific"> Specific Staff</div>
</div>
<div class="row">
	<div class="col-md-2">Select Role :</div>
	<div class="col-md-3"><select id="role" class="form-control" disabled multiple="multiple">
		<optgroup label="Standard Role">
		<option value='2s'>Teacher</option>
		</optgroup>
		<optgroup label="Custom Role">
		<c:forEach items="${roleList}" var="role" varStatus="counter">
				<option value='<c:out value="${role.roll_id}"></c:out>c'><c:out value="${role.roll_desc}"></c:out></option>
		</c:forEach>
		</optgroup>
	</select>
		<span class="error" id="roleError"></span>	
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
					<span class="error" id="dateError"></span>
	</div>
</div>
<div class="row">
	<div class="col-md-2"><button id="save" class="btn btn-sm btn-success">Save</button></div>
</div>
</div>
</body>
</html>