<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
#examDiv .form-control{
text-overflow: ellipsis;
}
#examDiv .form-control[readonly]{
cursor: auto;
}
</style>
<script>
var that = "";
var divisionText = "";
var batchText = "";
$(document).ready(function(){
	var globalBatchID ="";
	var globalDivisionID = ""; 
	$("#searchStudent").click(function(){
		$("#divisionError").empty();
		$("#batchError").empty();
		var divisionID = $("#division").val();
		var batchID = $("#batchSelect").val();
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
			divisionText = $('#division option:selected').text();
			batchText = $('#batchSelect option:selected').text();
			var handlers = {};
			handlers.success = function(e){console.log("Success",e);
			createStudentTable(e);
			}
			handlers.error = function(e){console.log("Error",e)};
			rest.get("rest/classownerservice/getStudentForProgressCard/"+divisionID+"/"+batchID,handlers);
			
			var handler = {};
			handler.success = function(e){
			$("#examDiv").empty();
			for(i=0;i<e.length;i++){
				$("#examDiv").append("<div class='col-md-3'><div class='input-group'>"+
      					"<span class='input-group-addon'>"+
						"<input type='checkbox' name='exams' value='"+e[i].exam_id+"'>"+
						"</span>"+
						"<input type='text' class='form-control' readonly value='"+e[i].exam_name+"' data-toggle='tooltip' title='"+e[i].exam_name+"'>"+
						"</div></div>");
			}
			};
			handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
			rest.post("rest/classownerservice/getExamList/"+divisionID+"/"+batchID,handler);
		}
		});
	
	
	$("#division").change(function(){
		var divisionId = $("#division").val();
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
				    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
				    if(data.batches != null){
				    	$.each(data.batches,function(key,val){
				    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
						});
				    }
			   	},
			   error:function(e){
				   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
					$('div#addStudentModal .error').show();
			   }
			   
		});
	});
	
	$(".backToSubjectList").click(function(){
		$("#examList").show();
		$("#progressCard").hide();
	});
	
	$('#studentTable').on("click",".btn-student-progressCard",function(){
		$("#examDiv").find(".error").remove();
		var divisionID = globalDivisionID;
		var  batchID = globalBatchID;
		var studentID= $(this).prop("id");
		var examIDs = [];
			$('input[name="exams"]:checked').each(function(){
				examIDs.push(this.value);
		});
		var examID = examIDs.join();
		var handler ={};
		handler.success = function(e){console.log("Success",e)
		createProgressCard(e);
		};
		handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		that = $(this);
		if(examIDs.length>0){
		rest.get("rest/studentmarks/getStudentProgressCard/"+divisionID+"/"+batchID+"/"+examID+"/"+studentID,handler);
		}else{
			$("#examDiv").append("<div class='error'>Please select atleast one subject!</div>")
		}
	});
	
	$(".progressCardPrint").click(function(){
		var data = $("#progressCardData").html();
			newWin= window.open("");
			newWin.document.write("<html><link href='css/bootstrap.min.css' rel='stylesheet'><body class='container'>"+data+"</body></html>");
			newWin.print();
			newWin.close();
	});
	
	$("#sendProgressCards").click(function(){
		$("#examDiv").find(".error").remove();
		var divisionID = globalDivisionID;
		var  batchID = globalBatchID;
		var examIDs = [];
			$('input[name="exams"]:checked').each(function(){
				examIDs.push(this.value);
		});
		var examID = examIDs.join();
		var handler ={};
		handler.success = function(e){
		console.log("Success",e);
		$.notify({message: "Progress card Sent Successfully"},{type: 'success'});
		};
		handler.error = function(e){
			$.notify({message: "Error"},{type: 'danger'});
		};
		that = $(this);
		if(examIDs.length>0){
		rest.get("rest/studentmarks/sendStudentProgressCard/"+divisionID+"/"+batchID+"/"+examID,handler);
		}else{
			$("#examDiv").append("<div class='error'>Please select atleast one subject!</div>")
		}
	});
});
function createProgressCard(data){
	$("#progressCardData").empty();
	var instituteName = $("#instituteName").val();
	var htmlString = "";
	htmlString = htmlString +"<div align='center' class='row'><b>"+instituteName+"</b></div>"
	htmlString = htmlString +"<div class='row'><b>Name : </b>"+that.closest("tr").find(".studentname").val()+"</div>";
	htmlString = htmlString +"<div class='row'><div class='col-md-3' style='padding: 0%;'><b>Class : </b>"+divisionText+"</div><div class='col-md-3'><b>Batch : </b>"+batchText+"</div></div>";
						
	htmlString = htmlString +"<div class='row'><table class='table table-bordered'><thead><tr><th></th>";
	for(i =0 ; i<data.subjectList.length;i++){
		htmlString = htmlString + "<th>"+data.subjectList[i].subjectName+"</th>";
	}
	htmlString = htmlString + "<th>Total</th>";
	htmlString = htmlString + "<th>Out Of</th>";
	htmlString = htmlString + "<th>Percentage</th>";
	htmlString = htmlString + "</tr></thead>";
	htmlString = htmlString + "<tbody>";
	for(i =0 ; i<data.examList.length;i++){
		htmlString = htmlString + "<tr><td>"+data.examList[i].exam_name+"</td>";
		var total_marks = 0;
				for(k=0;k<data.subjectList.length;k++){
					var flag = false;
						for(l=0;l<data.studentDataList.length;l++){
							if(data.examList[i].exam_id == data.studentDataList[l].exam_id && data.subjectList[k].subjectId == data.studentDataList[l].sub_id){
								console.log(data.studentDataList[l]);
								if(data.studentDataList[l].examPresentee == ""){
									htmlString = htmlString + "<td>-</td>";
								}else if(data.studentDataList[l].examPresentee == "A"){
									htmlString = htmlString + "<td>A</td>";
								}else{
								htmlString = htmlString + "<td>"+data.studentDataList[l].marks+"</td>";
								total_marks = total_marks + data.studentDataList[l].marks;
								}
								flag = true;
								break;
							}
						}
						if(flag == false ){
							htmlString = htmlString + "<td>-</td>";
						}
				}
				htmlString = htmlString + "<td>"+total_marks+"</td>";
				htmlString = htmlString + "<td>"+data.examList[i].total_marks+"</td>";
				htmlString = htmlString + "<td>"+parseFloat(((total_marks/data.examList[i].total_marks)*100).toFixed(2))+"%</td>";
		htmlString = htmlString + "</tr>";
	}
	htmlString = htmlString +"</tbody></table></div>";
	$("#progressCardData").append(htmlString);
	$("#examList").hide();
	$("#progressCard").show();
}

function createAllCard(data){
	$("#testDiv").empty();
	var z = 0; 
	var htmlString = "";
	while(z<studentData.length){
	var instituteName = $("#instituteName").val();
	
	htmlString = htmlString +"<div align='center' class='row'><b>"+instituteName+"</b></div>"
	htmlString = htmlString +"<div class='row'><b>Name : </b>"+studentData[z].fname+" "+studentData[z].lname+"</div>";
	htmlString = htmlString +"<div class='row'><div class='col-md-3' style='padding: 0%;'><b>Class : </b>"+divisionText+"</div><div class='col-md-3'><b>Batch : </b>"+batchText+"</div></div>";
						
	htmlString = htmlString +"<div class='row'><table class='table table-bordered'><thead><tr><th></th>";
	for(i =0 ; i<data.subjectList.length;i++){
		htmlString = htmlString + "<th>"+data.subjectList[i].subjectName+"</th>";
	}
	htmlString = htmlString + "<th>Total</th>";
	htmlString = htmlString + "<th>Out Of</th>";
	htmlString = htmlString + "<th>Percentage</th>";
	htmlString = htmlString + "</tr></thead>";
	htmlString = htmlString + "<tbody>";
	for(i =0 ; i<data.examList.length;i++){
		htmlString = htmlString + "<tr><td>"+data.examList[i].exam_name+"</td>";
		var total_marks = 0;
				for(k=0;k<data.subjectList.length;k++){
					var flag = false;
						for(l=0;l<data.studentDataList.length;l++){
							if(data.examList[i].exam_id == data.studentDataList[l].exam_id && data.subjectList[k].subjectId == data.studentDataList[l].sub_id && studentData[z].student_id == data.studentDataList[l].student_id){
								console.log(data.studentDataList[l]);
								if(data.studentDataList[l].examPresentee == ""){
									htmlString = htmlString + "<td>-</td>";
								}else if(data.studentDataList[l].examPresentee == "A"){
									htmlString = htmlString + "<td>A</td>";
								}else{
								htmlString = htmlString + "<td>"+data.studentDataList[l].marks+"</td>";
								total_marks = total_marks + data.studentDataList[l].marks;
								}
								flag = true;
								break;
							}
						}
						if(flag == false ){
							htmlString = htmlString + "<td>-</td>";
						}
				}
				htmlString = htmlString + "<td>"+total_marks+"</td>";
				htmlString = htmlString + "<td>"+data.examList[i].total_marks+"</td>";
				htmlString = htmlString + "<td>"+parseFloat(((total_marks/data.examList[i].total_marks)*100).toFixed(2))+"%</td>";
		htmlString = htmlString + "</tr>";
	}
	htmlString = htmlString +"</tbody></table></div>";
	z++;
	}
	$("#testDiv").append(htmlString);
	/* $("#examList").hide();
	$("#progressCard").show(); */
}
var studentData = [];
function createStudentTable(data){
	studentData = data;
	dataTable = $('#studentTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: true,
		columns: [
			{title:"Roll no",data:"rollNo",render:function(data,event,row){
				if(!data){
					data = '&mdash;'
				}
				return data;
			}},
			{ title: "Student Name",data:null,render:function(data,event,row){
				var input = "<input type='hidden' id='studentId' value='"+row.student_id +"'>"+"<input type='hidden' class='studentname' value='"+row.fname+" "+row.lname +"'>";
				var modifiedObj = row.fname+" "+row.lname;
				return modifiedObj+input;
			}},
			{ title: "Action",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
				'<input type="button" class="btn btn-xs btn-primary btn-student-progressCard" value="View Progress Card" id="'+row.student_id+'">'+
			'</div>'			
			return buttons;
				}}
		]
	});
	
	dataTable.on( 'order.dt search.dt', function () {
    dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
        cell.innerHTML = i+1;
		});
	}).draw();
}

</script>
</head>
<body>
<jsp:include page="ExamMarksHeader.jsp" >
		<jsp:param value="active" name="progressCard"/>
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
				<span id="divisionError" class="divisionError"></span>
			</div>
			<div class="col-md-2">
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="batchError"></span>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchStudent">Search</button>
			</div>
			<div class="col-md-3">
				<button class="form-control btn btn-success btn-sm" id="sendProgressCards">Send Progress Cards To Parents</button>
			</div>
		</div>
	</div>
	<div  class="container" style="padding: 2%" id='examList'>
	<div>
	Select exams to be included in progress card :
	</div>
	<div id="examDiv">
	</div>
	<div class="container" id="studentTableDiv">
	<table class="table" id="studentTable"></table>
	</div>
	</div>
	<div id="progressCard" style="display: none">
	<div class="container" style="margin :1%">
	<div class="row">
	<div class="col-md-3">
	<button class="btn btn-primary btn-sm backToSubjectList">Back To Student List</button>
	</div>
	<div class="col-md-3">
	<button class="btn btn-primary btn-sm progressCardPrint"> <span class="glyphicon glyphicon-print"></span>&nbsp;Print</button>
	</div>
	</div>
	</div>
	<div id="progressCardData" class="container" style="border: 1px solid;margin: 1%;padding-left: 5%;">
	</div>
	</div>
	<input type="hidden" id="instituteName" name="instituteName" value='<c:out value="${instituteName }"></c:out>'>
	<div id="testDiv" class="container">
	
	</div>
</body>
</html>