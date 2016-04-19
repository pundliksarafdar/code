<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css" type="text/css" />
  <script src="../js/bootstrap-datetimepicker.min.js"></script> -->
  <style type="text/css">
 #attendanceStudentListDiv .dataTables_filter { visibility: hidden;}
  </style>
</head>
<body>
<jsp:include page="attendanceHeader.jsp" >
		<jsp:param value="active" name="attendances"/>
	</jsp:include>
	<div class="container" style="padding: 2%;background: #eee">
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
				<select class="btn btn-default" id="divisionSelect">
					<option value="-1">Select Division</option>
					<c:forEach var="division" items="${divisions}">
						<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out> <c:out value="${division.stream}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-2">
				<select class="btn btn-default" id="batchSelect">
					<option value="-1">Select Batch</option>
				</select>
			</div>
			<div class="col-md-3">
				<div id="datetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
			</div>
			<div class="col-md-1">
				<button class="btn btn-primary btn-sm" id="searchLectures">Search Lectures</button>
			</div>
		</div>
	</div>
	<div class="container" id="attendanceScheduleDiv">
	<table id="attendanceScheduleTable" class="table" style="width: 100%"></table>
	
	</div>
	<div class="container" style="display: none;" id="attendanceStudentListDiv">
	<div class="row" style="padding: 2%;">
	<div class="col-md-2">
	<button class="btn btn-primary btn-sm backtoSchedule">Back</button>
	</div>
	<div class="col-md-2">
	<button class="btn btn-primary btn-sm saveAttendance">Save Attendance</button>
	</div>
		<div class="col-md-offset-6 col-md-2">
	<input type="text" id="myInput" class="form-control input-sm">
	</div>
	</div>
	<table id="attendanceStudentListTable" class="table" style="width: 100%"></table>
	</div>
</body>
</html>