<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script>

$(document).ready(function(){
	$("#searchExam").click(function(){
		var division = $("#division").val();
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createExamListTable(e);};
		handlers.error = function(e){console.log("Error",e)};
		rest.post("rest/classownerservice/getExamList/"+division,handlers);
	});
	
	$("#examList").on("click",".editExam",function(){
		$("#examListDiv").hide();
		$("#editModeDiv").show();
	});
	
	$(".cancelEdit").click(function(){
		$("#examListDiv").show();
		$("#editModeDiv").hide();
	});
});

function createExamListTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#examList').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Exam Description",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.exam_name+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Edit",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary editExam" value="Edit" id="'+row.exam_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"},
			{ title: "Delete",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-danger deleteExam" value="Delete" id="'+row.exam_id+'">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	
}

</script>
</head>
<body>
<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="editExam"/>
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
				<span id="divisionError" class="patternError"></span>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchExam">Search</button>
			</div>
		</div>
	</div>
	<div class="container" id="examListDiv">
		<table id="examList"></table>
	</div>
	<div id="editModeDiv" style="display: none;" class="container">
	<div class="row">
			<div class="col-md-2"><button class="btn btn-primary btn-xs cancelEdit" value="Cancel Edit">Cancel Edit</button></div>
	</div>
		<div class="row">
			<div class="col-md-2">Select Header</div>
			<div class="col-md-3">
				<select id="headerDesc" name="headerDesc" class="form-control">
					<option value="-1">Select Header</option>
					<c:forEach items="${headerList}" var="headerX">
						<option value="<c:out value="${headerX.header_id}"></c:out>">
							<c:out value="${headerX.header_name}"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="row subjectDiv">
		
		</div>
		<div class="actionOption">
			<button class="btn btn-primary btn-sm" value="Save" id="saveExam">Save</button>
		</div>
	</div>		
</body>
</html>