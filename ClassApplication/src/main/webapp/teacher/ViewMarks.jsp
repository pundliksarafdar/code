<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
var divisionTempData = {};
divisionTempData.id = "-1";
divisionTempData.text = "Select Class";
var batchTempData = {};
batchTempData.id = "-1";
batchTempData.text = "Select Batch";
var examTempData = {};
examTempData.id = "-1";
examTempData.text = "Select Exam";
$(document).ready(function(){
	$("#instituteSelect").change(function(){
		$("#studentTableDiv").hide();
		var divisionArray = [];
		var batchArray = [];
		divisionArray.push(divisionTempData);
		batchArray.push(batchTempData);
		$("#division").empty();
		$("#batchSelect").empty();
		$("#exam").empty();
		 $("#division").select2({data:divisionArray});
		 $("#batchSelect").select2({data:batchArray});
		 $("#exam").append("<option value='-1'>Select Exam</option>");
		 $("#subjects").empty();
		 $("#subjects").append("<option value='-1'>Select Subject</option>");
		 $("#exam").select2().val("-1").change();
		 $("#subjects").select2().val("-1").change();
		var inst_id = $(this).val();
		if(inst_id != "-1"){
		$("#instituteError").html("");
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 	 	if(divisionArray.length > 1){
 	 	$("#division").select2({data:divisionArray});
 	 	}else{
 	 		$("#division").empty();
 	 		$("#division").select2({data:"",placeholder:"Class not available"});	
 	 	}
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}
	});
	
	$("#searchMarks").click(function(){
		$("#studentTableDiv").hide();
		$(".validation-message").html("");
		var validationFlag = false;
		var inst_id = $("#instituteSelect").val();
		var division = $("#division").val();
		var batch = $("#batchSelect").val();
		var exam = $("#exam").val();
		var subject = $("#subjects").val();
		var viewType = $("#viewType").val()
		if(inst_id == "-1"){
			$("#instituteError").html("Select Institute");
			validationFlag = true;
		}
		if(division == "-1" || division == "" || division == null){
			$("#divisionError").html("Select Class");
			validationFlag = true;
		}
		if(batch == "-1" || batch == "" || batch == null){
			$("#batchError").html("Select Batch");
			validationFlag = true;
		}
		if(viewType == "-1"){
			$("#viewTypeError").html("Select View");
			validationFlag = true;
		}else{
			if(viewType == "1"){
		if(exam == "-1" || exam == "" || exam == null){
			$("#examError").html("Select Exam");
			validationFlag = true;
		}
		}else{
			if(subject == "-1" || subject == "" || subject == null){
				$("#examError").html("Select Subject");
				validationFlag = true;
			}
		}
		}
		if(!validationFlag){
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		if($("#viewType").val() == "1"){
		createStudentExamMarksTable(e);
		}else{
			createStudentExamSubjectMarksTable(e);
		}
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		if($("#viewType").val() == "1"){
		rest.get("rest/teacher/getStudentExamMarks/"+inst_id+"/"+division+"/"+batch+"/"+exam,handlers);
		}else if($("#viewType").val() == "2"){
			rest.get("rest/teacher/getStudentExamSubjectMarks/"+inst_id+"/"+division+"/"+batch+"/"+subject,handlers);
		}
		}
		});
	
	
	$("#viewType").change(function(){
		$("#studentTableDiv").hide();
		var examArray = [];
		examArray.push(examTempData);
		$("#exam").empty();
		$("#exam").append("<option value='-1'>Select Exam</option>");
		$("#subjects").empty();
		$("#subjects").append("<option value='-1'>Select Subject</option>");
		$("#exam").select2().val("-1").change();
		$("#subjects").select2().val("-1").change();
		if($("#viewType").val() != "-1"){
		$("#viewTypeError").html("");
		getViewData();
		}
	});
	
	$(".viewTypes").change(function(e){
		if($(this).prop("id") != "-1"){
			$("#examError").html("");	
		}
	});
	
	$("#batchSelect").change(function(){
		$("#studentTableDiv").hide();
		$("#exam").empty();
		$("#exam").append("<option value='-1'>Select Exam</option>");
		$("#subjects").empty();
		$("#subjects").append("<option value='-1'>Select Subject</option>");
		$("#exam").select2().val("-1").change();
		 $("#subjects").select2().val("-1").change();
		 if($("#batchSelect").val() != "-1"){
		 $("#batchError").html("");
		getViewData();
		 }
	});
	
	
	$("#division").change(function(){
		$("#studentTableDiv").hide();
		var batchArray = [];
		batchArray.push(batchTempData);
		$("#batchSelect").empty();
		$("#exam").empty();
		 $("#batchSelect").select2({data:batchArray});
		 $("#exam").append("<option value='-1'>Select Exam</option>");
		 $("#subjects").empty();
		 $("#subjects").append("<option value='-1'>Select Subject</option>");
		 $("#exam").select2().val("-1").change();
		 $("#subjects").select2().val("-1").change();
	var division = $("#division").val();
	var inst_id = $("#instituteSelect").val();
	if(division != "-1"){
	$("#divisionError").html("");
	var handlers = {};
	handlers.success = function(e){console.log("Success",e);
	    $.each(e,function(key,val){
			var data = {};
			data.id = val.batch_id;
			data.text = val.batch_name;
			batchArray.push(data);
		});
	    if(batchArray.length > 1){
	    $("#batchSelect").select2({data:batchArray,placeholder:"type batch name"}); 
	    }else{
	    	$("#batchSelect").empty();
	    	 $("#batchSelect").select2({data:"",placeholder:"Batch not available"}); 	
	    }
	};
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get("rest/teacher/getBatchesofDivision/"+inst_id+"/"+division,handlers);
	}
	}); 
	 
	$('#subjectTable').on("click",".fillMarks",function(){
		var division = $("#division").val();
		var batch = $("#batchSelect").val();
		var exam = $("#exam").val();
		exam_marks = $(this).closest("div").find(".subject_marks").val();
		subject = $(this).attr("id");
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createStudentMarksTable(e)
		$("#subjectTableDiv").hide();
		$("#studentTableDiv").show();
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		
		rest.post("rest/classownerservice/getStudentForMarksFill/"+division+"/"+batch+"/"+exam,handlers);
	});
	
	$(".backToSubjectList").click(function(){
		 $("#subjectTableDiv").show();
			$("#studentTableDiv").hide();
	});
	
	$(".saveMarks").click(function(){
		var table = $('#studentTable').DataTable();
		 var dataArray = [];
		var data = table.data();
		 for(var i=0;i<data.length;i++){
			 data[i].sub_id = subject;
			 dataArray.push(data[i]);
		 }
		console.log(data);
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		$.notify({message: "Marks updated"},{type: 'success'});
		 $("#subjectTableDiv").show();
			$("#studentTableDiv").hide();
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/classownerservice/saveStudentMarks/",handlers,JSON.stringify(dataArray));
	});
	
	$('#studentTable').on( 'blur', '.Marks', function () {
		var table = $('#studentTable').DataTable();
    var cell = table.cell( $(this).closest("td"));
    if(parseInt($(this).val())> exam_marks){
    	$(this).css("border-color","red");
    }else{
    	$(this).css("border-color","#fff");
    }
    cell.data().marks=$(this).val()
} );
});

function getViewData(){
	var division = $("#division").val(); 
	var batch = $("#batchSelect").val(); 
	var inst_id = $("#instituteSelect").val();
	if($("#viewType").val() != "-1"){
	if($("#viewType").val() == "1"){
		$("#subjectDiv").hide();
		$("#examDiv").show();
		if(batch != "-1"){
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		$("#exam").empty();
		if(e.length > 0){
		$("#exam").append("<option value='-1'>Select Exam</option>");
		$("#exam").select2().val("-1").change();
		for(var i=0;i<e.length;i++){
			$("#exam").append("<option value='"+e[i].exam_id+"'>"+e[i].exam_name+"</option>");
		}
		}else{
			$("#exam").empty();
	        $("#exam").select2({data:"",placeholder:"Exam not available"}); 
		}
		};
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.get("rest/teacher/getExamList/"+inst_id+"/"+division+"/"+batch,handlers);
		}
	}else{		
	$("#subjectDiv").show();
	$("#examDiv").hide();
	if(batch != "-1"){
	var handlers = {};
	handlers.success = function(e){console.log("Success",e);
	$("#subjects").empty();
	if(e.length > 0){
	$("#subjects").append("<option value='-1'>Select Subject</option>");
	$("#subjects").select2().val("-1").change();
	var i = 0;
	while(i < e.length){
   		$("#subjects").append("<option value='"+e[i].subjectId+"'>"+e[i].subjectName+"</option>");
   		i++;
  	 }
	}else{
		$("#subjects").empty();
        $("#subjects").select2({data:"",placeholder:"Subject not available"}); 
	}
	};
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get("rest/teacher/getBatchSubjects/"+inst_id+"/"+division+"/"+batch,handlers);
	}
	}
	}
}

function createStudentExamMarksTable(data){
	//$("#studentMarksTable").clear();
	$("#studentTableDiv").empty();
	var examTotalMarks = 0;
	var htmlString = "";
	htmlString = htmlString +"<table class='table' style='width:100%'><thead><tr><th>Student Name</th>";
	for(i=0;i<data.examSubjectList.length;i++){
		htmlString = htmlString + "<th>"+data.examSubjectList[i].subjectName+"/"+data.examSubjectList[i].marks+"</th>"
		examTotalMarks = examTotalMarks + data.examSubjectList[i].marks;
	}
	htmlString = htmlString + "<th>Total/"+examTotalMarks+"</th><th>Percentage</th>"
	htmlString = htmlString + "</tr></thead>";
	htmlString = htmlString + "<tbody>";
	for(i=0;i<data.studentDataList.length;i++){
		htmlString = htmlString + "<tr><td>"+data.studentDataList[i].fname+" "+data.studentDataList[i].lname+"</td>";
		var studentTotalMarks = 0 ;
		for(j=0;j<data.examSubjectList.length;j++){
			if(i<data.studentDataList.length){
			htmlString = htmlString + "<td>"+data.studentDataList[i].marks+"</td>";
			studentTotalMarks = studentTotalMarks + data.studentDataList[i].marks;
			if((j+1)<data.examSubjectList.length){
			i++;
			}
			}else{
				break;
			}
		}
		htmlString = htmlString + "<td>"+studentTotalMarks+"</td>"
		htmlString = htmlString + "<td>"+parseFloat(((studentTotalMarks/examTotalMarks)*100).toFixed(2))+"%</td>"
		htmlString = htmlString + "</tr>"
	}
	
	htmlString = htmlString + "</tbody></table>";
	$("#studentTableDiv").append(htmlString);
	$("#studentTableDiv").show();
	$("#studentTableDiv").find("table").DataTable();
	
}

function createStudentExamSubjectMarksTable(data){
	//$("#studentMarksTable").clear();
	$("#studentTableDiv").empty();
	var htmlString = "";
	htmlString = htmlString +"<table class='table' style='width:100%'><thead><tr><th>Student Name</th>";
	for(i=0;i<data.examList.length;i++){
		htmlString = htmlString + "<th>"+data.examList[i].exam_name+"</th>"
	}
	htmlString = htmlString + "</tr></thead>";
	htmlString = htmlString + "<tbody>";
	for(i=0;i<data.studentDataList.length;i++){
		htmlString = htmlString + "<tr><td>"+data.studentDataList[i].fname+" "+data.studentDataList[i].lname+"</td>";
		for(j=0;j<data.examList.length;j++){
			if(i<data.studentDataList.length){
			htmlString = htmlString + "<td>"+data.studentDataList[i].marks+"</td>";
			if((j+1)<data.examList.length){
			i++;
			}
			}else{
				break;
			}
		}
		htmlString = htmlString + "</tr>"
	}
	
	htmlString = htmlString + "</tbody></table>";
	$("#studentTableDiv").append(htmlString);
	$("#studentTableDiv").show();
	$("#studentTableDiv").find("table").DataTable();
}

</script>
</head>
<body>
<jsp:include page="ExamMarksHeader.jsp" >
		<jsp:param value="active" name="viewexamMarks"/>
	</jsp:include>
<div class="well">
		<div class="row">
		<div class="col-md-3">
				<select name="instituteSelect" id="instituteSelect" class="form-control" width="100px">
					<option value="-1">Select Institute</option>
					<c:forEach items="${requestScope.registerBeanList}" var="institute">
						<option value="<c:out value="${institute.regId}"></c:out>"><c:out value="${institute.className}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="instituteError" class="validation-message"></span>
			</div>
			<div class="col-md-2">
				<select id="division" name="division" class="form-control">
					<option value="-1">Select Class</option>
					<c:forEach items="${divisionList}" var="division">
						<option value="<c:out value="${division.divId }"></c:out>">
							<c:out value="${division.divisionName }"></c:out>
							<c:out value="${division.stream }"></c:out>
						</option>
					</c:forEach>
				</select>
				<span id="divisionError" class="validation-message"></span>
			</div>
			<div class="col-md-2">
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="validation-message"></span>
			</div>
			<div class="col-md-2">
				<select class="form-control" id="viewType" >
					<option value="1">Exam View</option>
					<option value="2">Subject View</option>
				</select>
				<span id="viewTypeError" class="validation-message"></span>
			</div>
			<div class="col-md-2">
				<div id="examDiv">
				<select id="exam" name="exam" class="form-control viewTypes">
					<option value="-1">Select Exam</option>
				</select>
				</div>
				<div id="subjectDiv" style="display: none;width: 100%">
				<select id="subjects" name="subjects" class="form-control viewTypes"  style="width: 100%">
					<option value="-1">Select Subject</option>
				</select>
				</div>
				<span id="examError" class="validation-message"></span>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchMarks">Search</button>
			</div>
		</div>
	</div>
	<div class="container" id="subjectTableDiv">
	<table class="table" id="subjectTable"></table>
	</div>
	
	<div class="container" id="studentTableDiv" style="display: none">
	</div>
</body>
</html>