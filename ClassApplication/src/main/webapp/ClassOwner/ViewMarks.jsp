<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
$(document).ready(function(){
	$("#searchMarks").click(function(){
		$(".validation-message").html("");
		var validationFlag = false;
		var division = $("#division").val();
		var batch = $("#batchSelect").val();
		var exam = $("#exam").val();
		var subject = $("#subjects").val();
		var viewType = $("#viewType").val()
		if(division == "-1"){
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
		rest.get("rest/studentmarks/getStudentExamMarks/"+division+"/"+batch+"/"+exam,handlers);
		}else if($("#viewType").val() == "2"){
			rest.get("rest/studentmarks/getStudentExamSubjectMarks/"+division+"/"+batch+"/"+subject,handlers);
		}
		}
		});
	
	
	$("#viewType").change(function(){
		$("#studentTableDiv").hide();
		getViewData();
	});
	
$("#batchSelect").change(function(){
	$("#studentTableDiv").hide();	
	$("#exam").empty();
	 $("#exam").select2().val("").change();
	 $("#exam").select2({data:"",placeholder:"Select Exam"});
	 $("#subjects").empty();
	 $("#subjects").select2().val("").change();
	 $("#subjects").select2({data:"",placeholder:"Select Subject"});
		 if($("#batchSelect").val() != "-1" && $("#batchSelect").val()!= "" && $("#batchSelect").val() != null){
		 $("#batchError").html("");
		getViewData();
		 }
	});
	
	$(".marksViewType").change(function(e){
		$("#studentTableDiv").hide();
		if($(this).val() != "-1"){
			$("#examError").html("");
		}
	})
	
	$("#division").change(function(){
		var divisionId = $("#division").val();
		$("#studentTableDiv").hide();
		$("#exam").empty();
		 $("#exam").select2().val("").change();
		 $("#exam").select2({data:"",placeholder:"Select Exam"});
		 $("#subjects").empty();
		 $("#subjects").select2().val("").change();
		 $("#subjects").select2({data:"",placeholder:"Select Subject"});
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
	$("#exam").val("-1")
	$("#exam").find('option:gt(0)').remove();
	$("#subjects").val("-1")
	$("#subjects").find('option:gt(0)').remove();
	if($("#viewType").val() != "-1" && division != "-1" && batch != "-1"){
	$("#viewTypeError").html("");
	if($("#viewType").val() == "1"){
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
			 $("#exam").select2().val("").change();
			 $("#exam").select2({data:"",placeholder:"Exam not available"});
		}
		$("#subjectsSelectDiv").hide();
		$("#examSelectDiv").show();
		};
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/classownerservice/getExamList/"+division+"/"+batch,handlers);
	}else{
	$.ajax({
	   url: "classOwnerServlet",
	   data: {
			 methodToCall: "fetchBatchSubject",
    		 batchName:batch,
    		 batchdivision:division
	   		},
	   type:"POST",
	   success:function(data){
		   data = JSON.parse(data);
		   if(data.subjectstatus == ""){
			   var subjectnames = data.Batchsubjects;
			   var subjectIds = data.BatchsubjectsIds;
			   var i = 0;
			   var subjectnameArray = subjectnames.split(",");
				var subjectidArray =  subjectIds.split(",");  
				subjectList = [];
				$("#subjects").empty();
				if(subjectnameArray.length > 0){
				$("#subjects").append("<option value='-1'>Select Subject</option>");
				$("#subjects").select2().val("-1").change();
				while(i < subjectnameArray.length){
			   		$("#subjects").append("<option value='"+subjectidArray[i]+"'>"+subjectnameArray[i]+"</option>");
			   		i++;
			   }
				}else{
					$("#subjects").empty();
					 $("#subjects").select2().val("").change();
					 $("#subjects").select2({data:"",placeholder:"Subject not available"});
				}
				$("#subjectsSelectDiv").show();
				$("#examSelectDiv").hide();
		   }
	   },
		error:function(){
	   		modal.launchAlert("Error","Error");
	   	}
	   });
	}
	}
}

function createStudentExamMarksTable(data){
	//$("#studentMarksTable").clear();
	$("#studentTableDiv").empty();
	var examTotalMarks = 0;
	var htmlString = "";
	htmlString = htmlString +"<table class='table' style='width:100%'><thead><tr><th>Roll No</th><th>Student Name</th>";
	for(i=0;i<data.examSubjectList.length;i++){
		htmlString = htmlString + "<th>"+data.examSubjectList[i].subjectName+"<font style='color:red'>/"+data.examSubjectList[i].marks+"</font></th>"
		examTotalMarks = examTotalMarks + data.examSubjectList[i].marks;
	}
	htmlString = htmlString + "<th>Total<font style='color:red'>/"+examTotalMarks+"</font></th><th>Percentage</th>"
	htmlString = htmlString + "</tr></thead>";
	htmlString = htmlString + "<tbody>";
	for(i=0;i<data.studentDataList.length;i++){
		if(data.studentDataList[i].roll_no == 0){
			htmlString = htmlString + "<tr><td>-</td>";	
		}else{
			htmlString = htmlString + "<tr><td>"+data.studentDataList[i].roll_no+"</td>";	
		}
		htmlString = htmlString + "<td>"+data.studentDataList[i].fname+" "+data.studentDataList[i].lname+"</td>";
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
	htmlString = htmlString +"<table class='table' style='width:100%'><thead><tr><th>Roll No</th><th>Student Name</th>";
	for(i=0;i<data.examList.length;i++){
		htmlString = htmlString + "<th>"+data.examList[i].exam_name+"</th>"
	}
	htmlString = htmlString + "</tr></thead>";
	htmlString = htmlString + "<tbody>";
	for(i=0;i<data.studentDataList.length;i++){
		if(data.studentDataList[i].roll_no == 0){
			htmlString = htmlString + "<tr><td>-</td>";	
		}else{
			htmlString = htmlString + "<tr><td>"+data.studentDataList[i].roll_no+"</td>";	
		}
		htmlString = htmlString + "<td>"+data.studentDataList[i].fname+" "+data.studentDataList[i].lname+"</td>";
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
<div class="container" style="padding: 2%; background: #eee">
		<div class="row">
			<div class="col-md-3">
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
			<div class="col-md-3">
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="validation-message"></span>
			</div>
			<div class="col-md-2">
				<select class="form-control" id="viewType" >
					<option value="-1">Select View</option>
					<option value="1">Exam View</option>
					<option value="2">Subject View</option>
				</select>
				<span id="viewTypeError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
			<div id="examSelectDiv" style="width: 100%">
				<select id="exam" name="exam" class="form-control marksViewType" style="width: 100%">
					<option value="-1">Select Exam</option>
				</select>
			</div>
			<div id="subjectsSelectDiv" style="display: none; width: 100%">
				<select id="subjects" name="subjects" class="form-control marksViewType"  style="width: 100%">
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