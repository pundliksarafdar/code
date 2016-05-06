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
	$("#instituteSelect").change(function(){
		var inst_id = $(this).val();
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		$("#division").empty();
		var divisionArray = [];
		var tempData = {};
 		tempData.id = "-1";
 		tempData.text = "Select Class";
 		divisionArray.push(tempData);
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 	 	for(i=0;i<divisionArray.length;i++){
 	 		$("#division").append("<option value='"+divisionArray[i].id+"'>"+divisionArray[i].text+"</option>")
 	 	}
	   // $("#division").select2({data:divisionArray,placeholder:"Type Topic Name"});
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);

	});
	$("#searchExam").click(function(){
		var inst_id = $("#instituteSelect").val();
		var division = $("#division").val();
		var batch = $("#batchSelect").val();
		var exam = $("#exam").val();
		var handlers = {};
		handlers.success = function(e){
		createExamSubjectTable(e)
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post("rest/teacher/getExamSubjects/"+inst_id+"/"+division+"/"+batch+"/"+exam,handlers);
	});
	
	$("#batchSelect").change(function(){
		var inst_id = $("#instituteSelect").val();
		var division = $("#division").val();
		var batch = $("#batchSelect").val();
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		$("#exam").empty();
		$("#exam").append("<option>Select Exam</option>");
		for(var i=0;i<e.length;i++){
			$("#exam").append("<option value='"+e[i].exam_id+"'>"+e[i].exam_name+"</option>");
		}
		};
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.get("rest/teacher/getExamList/"+inst_id+"/"+division+"/"+batch,handlers);
	});
	 $("#division").change(function(){
			var division = $("#division").val();
			var inst_id = $("#instituteSelect").val();
			var handlers = {};
			handlers.success = function(e){console.log("Success",e);
			 $('#batchSelect').empty();
			   var batchDataArray = [];
			 /* 
			    $.each(e,function(key,val){
					var data = {};
					data.id = val.batch_id;
					data.text = val.batch_name;
					batchDataArray.push(data);
				});
			    $("#batchSelect").select({data:batchDataArray,placeholder:"type batch name"}); */
			    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
			    if(e != null){
			    	$.each(e,function(key,val){
			    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
					});
			    }
			};
			handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
			rest.get("rest/teacher/getBatchesofDivision/"+inst_id+"/"+division,handlers);
		}); 
	$('#subjectTable').on("click",".fillMarks",function(){
		var inst_id = $("#instituteSelect").val();
		var division = $("#division").val();
		var batch = $("#batchSelect").val();
		exam_marks = $(this).closest("div").find(".subject_marks").val();
		var exam = $("#exam").val();
		subject = $(this).attr("id");
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createStudentMarksTable(e)
		$("#subjectTableDiv").hide();
		$("#studentTableDiv").show();
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		
		rest.get("rest/teacher/getStudentMarksForUpdate/"+inst_id+"/"+division+"/"+batch+"/"+exam+"/"+subject,handlers);
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
		handlers.success = function(e){
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

var subject = "";
var exam_marks = "";
function createExamSubjectTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#subjectTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Subject",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.subjectName+'</div>';
				return div;
			},sWidth:"30%"},
			{ title: "Marks",data:null,render:function(data,event,row){
				return row.marks;
			},sWidth:"20%"},
			{ title: "",data:null,render:function(data,event,row){
				return "<div><button class='btn btn-primary btn-sm fillMarks' id='"+row.subjectId+"'>Fill Marks</button><input type='hidden' class='subject_marks' value='"+row.marks+"'></div>";
			},sWidth:"20%"}
		]
	});
	
}

function createStudentMarksTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#studentTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
		          { title: "Roll No",data:null,render:function(data,event,row){
		        	  return row.roll_no;
		          },sWidth:"10%"},
			{ title: "Student",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.fname+" "+row.lname+'</div>';
				return div;
			},sWidth:"50%"},
			{ title: "Marks",data:null,render:function(data,event,row){
				return "<div class='presenteeDiv'><input type='text' class='form-control Marks'  value='"+row.marks+"'><input type='hidden' value='"+row.student_id+"' id='student_id'></div>"}
			,swidth:'30%'
			}
		]
	});
}

</script>
</head>
<body>
<jsp:include page="ExamMarksHeader.jsp" >
		<jsp:param value="active" name="editExamMarks"/>
	</jsp:include>
<div class="container" style="padding: 2%; background: #eee">
		<div class="row">
		<div class="col-md-3">
				<select name="instituteSelect" id="instituteSelect" class="form-control" width="100px">
					<option value="-1">Select Institute</option>
					<c:forEach items="${requestScope.registerBeanList}" var="institute">
						<option value="<c:out value="${institute.regId}"></c:out>"><c:out value="${institute.className}"></c:out></option>
					</c:forEach>							
				</select>
			</div>
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
				<span id="divisionError" class="patternError"></span>
			</div>
			<div class="col-md-3">
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
			</div>
			<div class="col-md-2">
				<select id="exam" name="exam" class="form-control">
					<option value="-1">Select Exam</option>
				</select>
				<span id="divisionError" class="patternError"></span>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchExam">Search</button>
			</div>
		</div>
	</div>
	<div class="container" id="subjectTableDiv">
	<table class="table" id="subjectTable"></table>
	</div>
	
	<div class="container" id="studentTableDiv" style="display: none">
	<div class="row">
			<div class="col-md-3">
	<button class="btn btn-primary btn-sm backToSubjectList">Back</button>
	</div>
	</div>
	<div>
	<table class="table" id="studentTable" style="width: 100%"></table>
	</div>
	<div class="row">
			<div class="col-md-offset-8 col-md-2">
	<button class="btn btn-success btn-sm saveMarks">Save</button>
	</div>
	</div>
	</div>
</body>
</html>