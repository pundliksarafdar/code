<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
	<div class="col-md-3" id="addModifyTimetableForm" data-toggle="tooltip" title="You can edit timetable here" data-trigger="manual" >
		<form id="scheduleForm" onsubmit="return false;">
		<div>
		<label for="divisionSelect">Division select</label><br>
			<select class="btn btn-default" id="divisionSelect" style="width:100%;" name="divisionSelect">
				<option value="-1">Select Division</option>
				<c:forEach var="division" items="${divisions}">
					<option value='<c:out value="${division.divId}"></c:out>'>
						<c:out value="${division.divisionName}"></c:out>
						<c:out value="${division.stream }"></c:out>
					</option>
				</c:forEach>
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
		<div>
		<label for="teacherSelect">Select teacher</label><br>
			<select class="btn btn-default" id="teacherSelect" style="width:100%;" name="teacherSelect">
				<option value="-1">Select teacher</option>
			</select>
		</div>
		<div>
		<label for="startDate">Start date</label>
			<div class="form-group">
                <div class='input-group date' id='startDate'>
                    <input type='text' class="form-control" name="startDate" required/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
		</div>
		<div>
		<label for="startTime">Start time</label>
			<div class="form-group">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control" name="startTime" required/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
		</div>
		<div>
		<label for="endTime">End time</label>
			<div class="form-group">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control" name="endTime" required/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
		</div>
		<div>
		<label for="repeat">Repeat</label>
			<div class="form-group">
                <input type="checkbox" id="repeat" data-size="mini">
            </div>
		</div>
		
		<div class="repeatDependant">
		<label for="endDate">End Date</label>
			<div class="form-group">
                <div class='input-group date' id='endDate'>
                    <input type='text' class="form-control" name="endDate"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
		</div>
		
		<div  class="repeatDependant">
		<label for="repetitionSelect">Select repetition</label>
		<div id="repetitionSelect">
			<label for="sun">S</label>
			<input type="checkbox" value="1" id="sun"  title="Sunday"/ >
			&nbsp;
			<label for="mon">M</label>
			<input type="checkbox" value="2" id="mon" title="Monday"/>
			&nbsp;
			<label for="tue">T</label>
			<input type="checkbox" value="3" id="tue" title="Tuesday"/>
			&nbsp;
			<label for="wed">W</label>
			<input type="checkbox" value="4" id="wed" title="Wednsday"/>
			&nbsp;
			<label for="thu">T</label>
			<input type="checkbox" value="5" id="thu" title="Thursday"/>
			&nbsp;
			<label for="fri">F</label>
			<input type="checkbox" value="6" id="fri" title="Friday"/>
			&nbsp;
			<label for="sat">S</label>
			<input type="checkbox" value="7" id="sat" title="Saturday"/>
		</div>
		</div>
		<div id="buttons">
			<div class="form-group">
                <button class="btn btn-success" id="save">Save</button>
				<button class="btn btn-success hide" id="edit">Update</button>
				<button class="btn btn-success hide" id="reset">Reset</button>
            </div>
		</div>
		</form>
	</div>
	
	<div id="calendarContainer" class="col-md-9">
	<div class="page-header">

		<div class="row">
			<div class="form-inline col-md-6">
				<div class="form-group" style="width:50%;">
					<div class='input-group date' id='calendarDate'>
						<input type='text' class="form-control" name="calendarDate" placeholder="Choose date"/>
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div>
				</div>
			</div>
			<div class="btn-group col-md-6">
				<button class="btn btn-warning active" data-calendar-view="month">Month</button>
				<button class="btn btn-warning active" data-calendar-view="week">Week</button>
				<button class="btn btn-warning" data-calendar-view="day">Day</button>
			</div>
		</div>

		<h3></h3>
		
		<div id="calendar"></div>
	</div>
	</div>
	</div>
	<div class="modal fade" id="events-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title">Event</h3>
				</div>
				<div class="modal-body" style="height: 400px">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
</div>

</body>
</html>