<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
	$("#batches").select2({data:'',placeholder:"type batch name"});
	$("#division").change(function(){
		getBatchesOfDivision();
	});
});

function getBatchesOfDivision(){
	var divisionId = $('#division').val();

	if(!divisionId || divisionId.trim()=="" || divisionId == -1){
		$("#batches").empty();
		 $("#batches").select2().val("").change();
		 $("#batches").select2({data:"",placeholder:"Select Batch"});
	}else{		
	  $.ajax({
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "fetchBatchesForDivision",
			 regId:'',
			 divisionId:divisionId,						 
	   		},
	   type:"POST",
	   success:function(e){
		   $('#batches').empty();
		   var batchDataArray = [];
		    var data = JSON.parse(e);
		    if(data.status != "error"){
		    $.each(data.batches,function(key,val){
				var data = {};
				data.id = val.batch_id;
				data.text = val.batch_name;
				batchDataArray.push(data);
			});
		    $("#batches").select2({data:batchDataArray,placeholder:"type batch name"});
		    }else{
		    	 $("#batches").select2({data:null,placeholder:"Batch not found"});
		    }
	   	},
	   error:function(e){
		  
	   }
	   
});
	
}
}
</script>
</head>
<body>
<ul class="nav nav-tabs" style="border-radius:10px">
  <li><a href="managestudent">Add Student</a></li>
   <li class="active"><a href="#tabBody" data-toggle = "tab">Add Student Through File</a></li>
  <li><a href="viewstudent">View Student</a></li>
</ul>

<div id="tabBody">
<div class="container" style="padding: 2%;background: #eee">
<div class="row">
<div class="col-md-4">
<label>Select Class and Batch For Student:</label>
</div>
<div class="col-md-3">
	<select id="division" class="form-control">
		<option value="-1">Select Class</option>
		<c:forEach items="${divisions}" var="division">
			<option value=<c:out value='${division.divId }'></c:out>><c:out value="${division.divisionName }"></c:out> <c:out value="${division.stream }"></c:out></option>
		</c:forEach>
	</select>
	<span class="error" id="divisionError"></span>
</div>
<div class="col-md-3">
<select id="batches" multiple="multiple" style="width:100%">
	<option value="-1">Select Batch</option>
</select>
<span class="error" id="batchError"></span>
</div>
</div>
<div class="row">	
			<div class="col-md-3">
				<a href="/SampleFiles/SampleStudent.xls" class="btn" role="button">Sample Student Excel</a>
			</div>
			<div class="col-md-3" id="browseExcelDiv">
			<span class="btn fileinput-button">
							<i class="glyphicon glyphicon-folder-open"></i> 
							<span>Browse Student Excel Sheet</span>
							<input type="file" id="excelUploadBrowseID" class="excelUpload">							
						</span>
						<span class="error" id="browseExcelErrorSpan">
						</span>
			</div>	
			<div class="col-md-3">
				<input type="btn btn-sm" class="btn btn-primary btn-sm" id="uploadStudentExcelBtn" value="Upload Excel" disabled/>
			</div>	
 		</div>
</div>
</div>
</body>
</html>