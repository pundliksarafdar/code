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
	$("#batches").change(function(){
		var divId=$("#division").val();
		var batch = $("#batches").val();
		if(divId!="-1" && batch!="-1" && batch!="" && batch!=null && batch!="Select Batch"){
		 	$("#uploadStudentExcelBtn").removeAttr('disabled');
		 	$("#uploadStudentExcelBtn").empty();
		}else{
			$("#uploadStudentExcelBtn").prop("disabled",true);
			}
		});
	
	 $("#uploadStudentExcelBtn").on("click",function(e){
			$("#addsuccess").hide();
			$(".studentform").hide();
			$(".classfloorID").hide();
			$(".studentInfoManually").hide();
			$(".studentInfobyID").hide();
			$("#countDiv").empty();
			$('#errorMSGDiv').empty();
			$('#errorMSGDiv').show();
			$("#countDiv").show();
			var handler = {};
			handler.success = function(e){				
				var uri = "rest/classownerservice/addExcelFile/"+e.fileid;
				var handlers = {};
				handlers.success = function(e){
					var divisionId = $('#division').val();
					
					var batchIDs = $("#batches").val();
					
					var StudentExcelUploadBean= {};
					StudentExcelUploadBean.divId=divisionId;
					StudentExcelUploadBean.batchId=batchIDs;
					StudentExcelUploadBean.fileName=e.fileid;
					
					var studentExcelUploadBean = JSON.stringify(StudentExcelUploadBean);
					
					var handlersSuccess = {};
					handlersSuccess.success = function(successResp){
						$("#countDiv").append(successResp.SUCCESS[0]);
						var errorResponse=successResp.ERROR;												
						if(errorResponse!=null && !errorResponse==""){
							var content="";
							for(var i=0; i<errorResponse.length; i++){
								content=content+"<tr>";
								var errorMessages=errorResponse[i].split("#");																
								content=content+"<td>"+errorMessages[0]+"</td><td>";
									for(var j=1;j<errorMessages.length;j++){										
										content=content+errorMessages[j]+"<br>";									
									}
								content="</td>"+content+"</tr>";
							}							
							var table='<table class="table"><thead><tr><th>Row number</th><th>Messages</th></tr></thead><tbody>'+content+'</tbody></table>';
							$("#errorMSGDiv").append(table);
							$($("#errorMSGDiv").find("table")).DataTable({
								paging : false,
								scrollY:"200px"
							});
						}
						
					}
					rest.post("rest/files/upload/student/xls/", handlersSuccess,
							studentExcelUploadBean, false);
					
					console.log("Success",e);
					}
				handlers.error = function(e){console.log("Error",e)}
				rest.post(uri,handlers);
			}
			handler.error = function(){};
			
			var submitDataFile = $(".excelUpload")[0];
			var file=document.getElementById("excelUploadBrowseID").value;
			var flagUpload=true;
			if(file==""){				
				$("#browseExcelErrorSpan").html("Please select the file!");
				flagUpload=false;
			}else{
				$("#browseExcelErrorSpan").html("");
				flagUpload=true;
			}
			if(flagUpload==true){
				rest.uploadExcelFile(submitDataFile ,handler,false);
			}
						
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
<select id="batches" style="width:100%">
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