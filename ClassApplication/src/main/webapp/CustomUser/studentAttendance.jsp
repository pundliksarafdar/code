<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css" type="text/css" />
  <script src="../js/bootstrap-datetimepicker.min.js"></script>
  <style type="text/css">
 #attendanceStudentListDiv .dataTables_filter { visibility: hidden;}
  </style>
</head>
<body>
<jsp:include page="attendanceHeader.jsp" >
		<jsp:param value="active" name="customeUserAttendances"/>
	</jsp:include>
	<div class="well">
		<div class="row">
			<div class="col-md-3">
				<select class="form-control" id="divisionSelect">
					<option value="-1">Select Division</option>
					<c:forEach var="division" items="${divisions}">
						<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out> <c:out value="${division.stream}"></c:out></option>
					</c:forEach>
				</select>
				<span id="divisionError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<select class="form-control" id="batchSelect">
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<div id="datetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				<span id="dateError" class="validation-message"></span>
			</div>
			<div class="col-md-3">
				<button class="btn btn-primary btn-sm" id="searchLectures">Search Lectures</button>
			</div>
		</div>
	</div>
	<div class="container" id="commonAttendanceDiv" style="display: none;">
	<div class="row" style="padding: 1%;">
	<div class="col-md-4">
	Mark whole day attendance
	</div>
	<div class="col-md-2">
	<button class="btn btn-primary btn-sm comonMarkAttendance">Mark Attendance</button>
	</div>
	</div>
	</div>
	<div class="container" id="attendanceScheduleDiv">
	<table id="attendanceScheduleTable" class="table" style="width: 100%"></table>
	
	</div>
	<div class="container" style="display: none;" id="attendanceStudentListDiv">
	<div class="row" style="padding: 1%;">
	<div class="col-md-2">
	<button class="btn btn-primary btn-sm backtoSchedule">Back</button>
	</div>
	<div class="col-md-2">
	<button class="btn btn-primary btn-sm saveAttendance">Save Attendance</button>
	</div>
		<div class="col-md-offset-6 col-md-2">
	<input type="text" id="myInput" class="form-control input-sm" placeholder="Roll No">
	</div>
	</div>
	<table id="attendanceStudentListTable" class="table" style="width: 100%"></table>
	</div>
</body>
</html>