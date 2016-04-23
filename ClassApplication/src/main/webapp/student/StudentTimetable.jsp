<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style>

/*This style is to add css*/
.corex-edit{
	display: none;
}

.corex-delete{
	display: none;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="js/underscore-min.js"></script>
	<script type="text/javascript" >
	_.templateSettings = {
		interpolate: /\<\@\=(.+?)\@\>/gim,
		evaluate: /\<\@(.+?)\@\>/gim,
		escape: /\<\@\-(.+?)\@\>/gim
	};
	</script>
	
	<script type="text/javascript" src="js/calendar.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
</head>
<body>
<div class="container">
	<div class="row">
	<div class="col-md-3" id="addModifyTimetableForm">
		<form id="scheduleForm" onsubmit="return false;">
		
		<div>
		<label for="classSelect">Class select</label><br>
			<select class="btn btn-default" id="classSelect" style="width:100%;" name="classSelect">
				<option value="-1">Select Class</option>
			</select>
		</div>
		
		<div>
		<label for="divisionSelect">Division select</label><br>
			<select class="btn btn-default" id="divisionSelect" style="width:100%;" name="divisionSelect">
				<option value="-1">Select Division</option>
			</select>
		</div>
		<div>
		<label for="batchSelect">Select batch</label><br>
			<select class="btn btn-default" id="batchSelect" style="width:100%;" name="batchSelect">
				<option value="-1">Select batch</option>
			</select>
		</div>
		<div>
		<label for="subjectSelect">Select subject</label><br>
			<select class="btn btn-default" id="subjectSelect" style="width:100%;" name="subjectSelect">
				<option value="-1">Select subject</option>
			</select>
		</div>
		
		<div id="buttons">
			<div class="form-group">
                <button class="btn btn-success" id="view">View</button>
			</div>
		</div>
		</form>
	</div>
	
	<div id="calendarContainer" class="col-md-9" style="display:none">
	<div class="page-header">

		<div class="row">
			<div class="col-md-8">
				<div class="form-group" style="width:50%;">
					<div class='input-group date' id='calendarDate'>
						<input type='text' class="form-control" name="calendarDate" placeholder="Choose date"/>
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div>
				</div>
			</div>
			<div class="btn-group col-md-4">
				<button class="btn btn-warning active" data-calendar-view="month">Month</button>
				<button class="btn btn-warning" data-calendar-view="week">Week</button>
				<button class="btn btn-warning" data-calendar-view="day">Day</button>
			</div>
		</div>
		<div id="calendar"></div>
	</div>
	</div>
	<div id="calendarShowMessageContainer" class="col-md-9">
		<h4>Please select from left side to view timetable</h4>
	</div>
</div>

</body>
</html>