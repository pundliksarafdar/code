<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var teacherSubjectArray = [];
var divisionTempData = {};
divisionTempData.id = "-1";
divisionTempData.text = "Select Class";
var subjectTempData = {};
subjectTempData.id = "-1";
subjectTempData.text = "Select Subject";
var topicTempData = {};
topicTempData.id = "-1";
topicTempData.text = "Select Topic";
$(document).ready(function(){
	$("#instituteSelect").change(function(){
		var divisionArray = [];
		var subjectArray = [];
		var topicArray = [];
		divisionArray.push(divisionTempData);
		subjectArray.push(subjectTempData);
		topicArray.push(topicTempData);
		$("#divisionSelect").empty();
		$("#subjectSelect").empty();
		$("#topicSelect").empty();
		 $("#divisionSelect").select2({data:divisionArray});
		 $("#subjectSelect").select2({data:subjectArray});
		 $("#topicSelect").select2({data:topicArray});
		var inst_id = $(this).val();
		if(inst_id != "-1"){
		$("#instituteSelectError").html("");
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		if(e.divisionList.length > 0){
		var divisionArray = [];
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 		teacherSubjectArray = e.subjectList;
	    $("#divisionSelect").select2({data:divisionArray,placeholder:"Type Topic Name"});
		}else{
			 $("#divisionSelect").empty();
			 $("#divisionSelect").select2().val("").change();
			 $("#divisionSelect").select2({data:"",placeholder:"Class not available"});
		}
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}
	});
	$("#uploadQuestionPaperBtn").on("click",function(e){
		$("#countDiv").empty();
		$('#errorMSGDiv').empty();
		$('#errorMSGDiv').show();
					var handler = {};
					handler.success = function(e){				
						var uri = "rest/classownerservice/addExcelFile/"+e.fileid;
						var handlers = {};
						handlers.success = function(e){
						var quesType = $("#classownerQuestionTypeSelect").val();
							var divisionID = $("#divisionSelect").val();
							var subjectID = $("#subjectSelect").val();
						var inst_id = $("#instituteSelect").val();
							var QuestionExcelUploadBean= {};
							QuestionExcelUploadBean.sub_id=subjectID;
							QuestionExcelUploadBean.div_id=divisionID;
							QuestionExcelUploadBean.ques_type=quesType;
							QuestionExcelUploadBean.fileName=e.fileid;
							QuestionExcelUploadBean.inst_id=inst_id;
							var questionExcelUploadBean = JSON.stringify(QuestionExcelUploadBean);
						var handlersSuccess = {};
						handlersSuccess.success = function(successResp){
							$("#countDiv").append(successResp.addedQuestionsResponse[0]);
							var errorResponse=successResp.ERROR;												
							if(errorResponse!=null && !errorResponse=="" && errorResponse.length!=0){
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
						rest.post("rest/teacher/upload/xls/", handlersSuccess,
									questionExcelUploadBean, false);
						
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
	
	$("#divisionSelect").on("change",function(e){
		var subjectArray = [];
		var topicArray = [];
		subjectArray.push(subjectTempData);
		topicArray.push(topicTempData);
		$("#subjectSelect").empty();
		$("#topicSelect").empty();
		 $("#subjectSelect").select2({data:subjectArray});
		 $("#topicSelect").select2({data:topicArray});
		if($(this).val()!=-1){
			$("#divisionSelectError").html("");
			getSubjectsInDivision($(this).val());
		}else{
			$("#uploadQuestionPaperBtn").prop("disabled",true);
			}
	});
	$("#classownerQuestionTypeSelect").change(function(){
		var subjectID = $("#subjectSelect").val();
		var quesType = $("#classownerQuestionTypeSelect").val();
		if(subjectID!="-1" && quesType!="-1"){
			$("#uploadQuestionPaperBtn").removeAttr('disabled');
		 	$("#uploadQuestionPaperBtn").empty();
		}else{
			$("#uploadQuestionPaperBtn").prop("disabled",true);
		}
	});
	$("#subjectSelect").change(function(){
		var topicArray = [];
		topicArray.push(topicTempData);
		$("#topicSelect").empty();
		$("#topicSelect").select2({data:topicArray});
		 
		var divisionID = $("#divisionSelect").val();
		var subjectID = $("#subjectSelect").val();
		var inst_id = $("#instituteSelect").val();
		var quesType = $("#classownerQuestionTypeSelect").val();
					
		if(subjectID!="-1" && quesType!="-1"){
		 	$("#uploadQuestionPaperBtn").removeAttr('disabled');
		 	$("#uploadQuestionPaperBtn").empty();
		}else{
			$("#uploadQuestionPaperBtn").prop("disabled",true);
		}
		if(subjectID != "-1"){
		$("#subjectSelectError").html("");
		var handler = {};
		handler.success = function(e){console.log("Success",e);
 	 $.each(e,function(key,val){
			var data = {};
			data.id = val.topic_id;
			data.text = val.topic_name;
			topicArray.push(data);
		});
 	 
	    $("#topicSelect").select2({data:topicArray,placeholder:"Type Topic Name"});
	    $("#topicSelect").prop("disabled",false);
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getDivisionsTopics/"+inst_id+"/"+divisionID+"/"+subjectID,handler);
		}
	});
});

function getSubjectsInDivision(division){
	
	var inst_id = $("#instituteSelect").val();
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	$("#subjectSelect").empty();
	var subjectArray = [];
	var tempData = {};
	if(teacherSubjectArray.length > 0){
		tempData.id = "-1";
		tempData.text = "Select Subject";
		subjectArray.push(tempData);
	 $.each(e,function(key,val){
		$.each(teacherSubjectArray,function(innerKey,innerVal){
			if(val.subjectId == innerVal.subjectId){
		var data = {};
		data.id = val.subjectId;
		data.text = val.subjectName;
		subjectArray.push(data);
			return false;
			}
		});
	});
	 	if(subjectArray.length > 1){
    $("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
    $("#subjectSelect").prop("disabled",false);
	 	}else{
	 		subjectArray = [];
	 		$("#subjectSelect").select2({data:subjectArray,placeholder:"Subjects not available"});
		    $("#subjectSelect").prop("disabled",false);
	 	}
	}
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/teacher/getSubjectOfDivision/"+inst_id+"/"+division,handler);
}
</script>
</head>
<body>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li><a href="addteacherquestion">Add question</a></li>
		<li class="active"><a href="#addnotestab" data-toggle = "tab">Add Questions Through File</a></li>
		<li><a href="searchTeacherQuestion">Search/Edit question</a></li>
	</ul>
<form method="post" action="<c:out value="${forwardAction}" ></c:out>" id="commonSelectForm">
	<div class="container bs-callout" style="margin-bottom: 5px;background-color: #eee">
		
		<div align="center" style="font-size: larger;margin-bottom: 15px"><u><c:out value="${lable}"></c:out></u></div>
		<input type="hidden" id="quesstatus" name="quesstatus" value='<c:out value="${quesstatus}"></c:out>'>
		<input type="hidden" id="division" name="division" value='<c:out value="${division}"></c:out>'>
 		<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}"></c:out>'>
 		<input type="hidden" id="topicID" name="topicID" value='<c:out value="${topicID}"></c:out>'>
		<div class="row">
			<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
				 
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<select name="instituteSelect" id="instituteSelect" class="form-control" width="100px">
					<option value="-1">Select Institute</option>
					<c:forEach items="${requestScope.registerBeanList}" var="institute">
						<option value="<c:out value="${institute.regId}"></c:out>"><c:out value="${institute.className}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="instituteSelectError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<select name="division" id="divisionSelect" class="form-control" width="100px">
					<option value="-1">Select Class</option>							
				</select>
				<span id="divisionSelectError" class="validation-message"></span>
			</div>
			<div class="col-md-2 subjectDropDown">
				<select name="subject" id="subjectSelect" class="form-control" width="100px">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectSelectError" class="validation-message"></span>
			</div>
			<div class="col-md-2 topicDropDown">
				<select name="topic" id="topicSelect" class="form-control" width="100px">
					<option value="-1">Select Topic</option>
				</select>
			</div>
			<div class="col-md-2 questionTypeDropDown">
				<select name="subject" id="classownerQuestionTypeSelect" class="form-control" width="100px">
					<option value="-1">Select Question Type</option>
					<option value="1">Subjective</option>
					<option value="2">Objective</option>
					<option value="3">Paragraph</option>
				</select>
			</div>
		</div>
		<div class="row">	
			<div class="col-md-3">
				<a href="./SampleFiles/SubjectiveType_Sample.xls" class="btn" role="button">Sample Subjective Questions Excel</a>
			</div>
			<div class="col-md-3">
				<a href="./SampleFiles/MCQType_Sample.xls" class="btn" role="button">Sample Objective Questions Excel</a>
			</div>		
			<div class="col-md-3" id="browseExcelDiv">
			<span class="btn fileinput-button">
							<i class="glyphicon glyphicon-folder-open"></i> 
						<span>Browse Your Question Paper Excel</span>
							<input type="file" id="excelUploadBrowseID" class="excelUpload">							
						</span>
						<span class="error" id="browseExcelErrorSpan">
						</span>
			</div>	
			<div class="col-md-3">
				<input type="button" id="uploadQuestionPaperBtn" value="Upload Excel" disabled/>
			</div>	
 		</div>
 		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-6 control-label" id="countDiv"></div>
		</div>

		<div class="row">
			<div id="errorMSGDiv"></div>
		</div>		
		<div class="row">
			
		</div>
	</div>
	</form>
</body>

</html>