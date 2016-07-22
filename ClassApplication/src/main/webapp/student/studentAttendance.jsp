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
table.dataTable thead .sorting:after, table.dataTable thead .sorting_asc:after, table.dataTable thead .sorting_desc:after, table.dataTable thead .sorting_asc_disabled:after, table.dataTable thead .sorting_desc_disabled:after{
 position: relative;
 display: inline;
 float: right;
 bottom: 0px;
 right: 0px;
 }
/*  th, td { white-space: nowrap; } */
    .DTFC_RightWrapper{
    right: 0% !important;
    }
  </style>
</head>
<body>

	<div class="container" style="padding: 2%;background: #eee">
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
			<div class="col-md-3">
				<select class="form-control" id="batchSelect">
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="validation-message"></span>
			</div>
			<div class="col-md-2">
				<select class="form-control" id="attendanceType">
					<option value="1" selected="selected">Daily Attendance</option>
					<option value="2">Weekly Attendance</option>
					<option value="3">Monthly Attendance</option>
				</select>
			</div>
			<div class="col-md-2">
				<div id="datetimepicker" class="input-group">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				<span id="dateError" class="validation-message"></span>
			</div>
			<div class="col-md-1">
				<button class="btn btn-primary btn-sm" id="viewAttendance">View</button>
			</div>
		</div>
	</div>
	<div class="container" id="dailyAttendance" style="width: 100%;display: none;overflow-x: auto">
	<table id="attendanceScheduleTable" class="table-bordered" style="width: 100%"></table>
	
	</div>
	<div class="container" id="monthlyAttendance" style="width: 100%;display: none;overflow-x: auto">
	<table id="attendanceStudentListTable" class="table-bordered" style="width: 100%"></table>
	</div>
</body>
</html>