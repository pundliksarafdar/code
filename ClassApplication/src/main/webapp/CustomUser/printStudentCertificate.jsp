<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
@media print
{    
    .notPrintable
    {
        display: none !important;
    }
    .printable
    {
        display: block !important;
    }
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#division").change(function() {
			getBatchesOfDivision();
		});
		
		$("#studentTable").on("click",".btn-certificate-print",function(){
			$(".error").html("");
			var flag = true;
			var student_id = $($(this).closest("tr")).find("#studentId").val();
			var cert_id = $("#certificate").val();
			if(cert_id == "-1"){
				$("#certificateSelectError").html("Select certificate template");
				flag = false;
			}
			if(flag){
			var handlers = {};
			handlers.success=function(data){
				var win =window.open();
				win.document.write(data);
				win.print();
				win.close();
			};   
			handlers.error=function(){
				$.notify({message: "Student not deleted"},{type: 'danger'});
			};   
			
			rest.get("rest/customuserservice/getCertificateForPrint/"+cert_id+"/"+student_id,handlers);
			}
		});
		
		$("#batch").change(function(){
			$("#divisionError").empty();
			$("#batchError").empty();
			var divisionID = $("#division").val();
			var batchID = $("#batch").val();
			var flag = false;
			if(divisionID == "-1"){
				$("#divisionError").html("Select Division!");
				flag=true;
			}
			if(batchID == "-1" || batchID == null){
				$("#batchError").html("Select Batch!");
				flag=true;
			}
			if(flag == false){
				globalBatchID = batchID;
				globalDivisionID = divisionID;
			var handlers = {};
			handlers.success=function(data){
				var	dataTable = $('#studentTable').DataTable({
						bDestroy:true,
						data: data,
						lengthChange: true,
						columns: [
							{title:"Roll no",data:"rollNo",render:function(data,event,row){
								if(data == 0){
									return  "-";	
								}else{
								return data;
								}
							}},
							{ title: "Student Name",data:"studentUserBean",render:function(data,event,row){
								var input = "<input type='hidden' id='studentId' value='"+data.regId +"'>";
								var modifiedObj = data.fname+" "+data.lname;
								return modifiedObj+input;
							}},
							{ title: "Action",data:null,render:function(data){
								var buttons = '<div class="default">'+
								'<input type="button" class="btn btn-xs btn-primary btn-certificate-print" value="Print">&nbsp;'+
							'</div>';
							return buttons;
								}}
						]
					});
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			
			rest.get("rest/customuserservice/getStudentsRelatedtoBatch/"+divisionID+"/"+batchID,handlers);
			}
			});
	});
	function getBatchesOfDivision() {
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = $('#division').val();
		var batchDataArray = [];
		if (!divisionId || divisionId.trim() == "" || divisionId == -1) {
			$('#batch').empty();
			var tempData = {};
			tempData.id = "-1";
			tempData.text = "Select Batch";
			$("#batch").select2({
				data : tempData,
				placeholder : "Select Batch"
			});
		} else {
			var handlers = {};
			handlers.success=function(data){
				$('#batch').empty();
				if (data.length > 0) {
					var tempData = {};
					tempData.id = "-1";
					tempData.text = "Select Batch";
					batchDataArray.push(tempData);
					var batchData = {};
					$.each(data, function(key, val) {
						var data = {};
						data.id = val.batch_id;
						data.text = val.batch_name;
						batchDataArray.push(data);
						batchData[data.id] = val;
					});
					$("#batch").select2({
						data : batchDataArray,
						placeholder : "type batch name"
					}).data("batchData", batchData);
				} else {
					$("#batch").select2({
						data : "",
						placeholder : "No batch found"
					});
				}
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			
			rest.get("rest/customuserservice/getBatches/"+divisionId,handlers);
			}
	}
</script>
</head>
<body>
<div class="notPrintable">
	<jsp:include page="certificateHeaders.jsp">
		<jsp:param value="active" name="customeUserPrintStudentCertificate" />
	</jsp:include>
</div>	
	<div class="well notPrintable">
		<div class="row searchStudent">
			<div class="col-md-3">
				<select id="division" class="form-control">
					<option value="-1">Select Class</option>
					<c:forEach items="${divisions}" var="division">
						<option value=<c:out value='${division.divId }'></c:out>><c:out
								value="${division.divisionName }"></c:out>
							<c:out value="${division.stream }"></c:out></option>
					</c:forEach>
				</select> <span class="error" id="divisionError"></span>
			</div>
			<div class="col-md-3">
				<select id="batch" style="width: 100%" class="form-control">
					<option value="-1">Select Batch</option>
				</select> <span class="error" id="batchError"></span>
			</div>
			<div class="col-md-3">
				<select id="certificate" class="form-control">
					<option value="-1">Select Certificate</option>
					<c:forEach items="${certificateList}" var="certificate" varStatus="counter">
					<option value=<c:out value='${certificate.cert_id }'></c:out>><c:out
								value="${certificate.cert_desc }"></c:out>
					</c:forEach>
				</select>
				<div id="certificateSelectError" class="error"></div>
			</div>
		</div>
	</div>
	<div class="container notPrintable">
		<table class="table table-striped" id="studentTable"></table>
	</div>
	<div class="printable" style="display: none;"></div>
</body>
</html>