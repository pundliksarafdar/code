<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
	.row:hover{
		background-color: rgba(10,10,10,0.1);
	}
</style>
<script type="text/javascript" src="js/view/sendAlert.js"></script>
</head>
<body>
	<div>
		<div>
			<div class="container row">
	  		<div class="col-md-6">
		  		<div>
		  		<select name="divisionSelect" id="divisionSelect" class="form-control" width="100px">
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				</div>
			</div>
			<div class="col-md-6">
				<div id="sendAlert">
	  			<Select name="divisionSelect" id="batchSelect" class="form-control" width="100px"></Select>
	  			</div>
	  		</div>
	  		</div>
		</div>
		
		<div class="container">
			<div class="row">
			<label class="col-md-3">Choose method</label>	
				<div class="col-md-9">
					<input type="checkbox" value="sms" id="sms"><label for="sms">SMS</label>
					<input type="checkbox" value="sms" id="email"><label for="email">Email</label>
				</div>
			</div>
			<div class="row">
			<label class="col-md-3">Choose whome</label>
				<div class="col-md-9">
					<input type="checkbox" value="parent" id="parent"/><label for="parent">Parent</label>
					<input type="checkbox" value="student" id="student"/><label for="student">Student</label>
				</div>	
			</div>
		
		
		<div class="row">
			<label class="col-md-3">Fee due</label>	
			<input type="button" value="Send" class="btn btn-success"/>
		</div>
		
		<div class="row">
			<div class="col-md-2">
			<label>Attendance</label>
			</div>
			</div>
			<div class="row">
			<div class="col-md-1">
			</div>
			<div class="col-md-9">	
			<div class="container">
			<div class="row">
				<label class="col-md-3">Day</label>
				<div class="col-md-3">
					<div class='input-group date' id='datepickerDay'>
	                    <input type='text' class="form-control" placeholder="Select date"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
                	</div>
				</div>
				<div class="col-md-3">
					<input type='text' class="form-control"  placeholder="Minimum attendance"/>
				</div>
				<div class="col-md-3">
					<input type='button' class="btn btn-success"  value="Send"/>
				</div>
			</div>
			<div class="row">
				<label class="col-md-3">Week</label>
				<div class="col-md-3">
					<div class='input-group date' id='datepickerWeek'>
	                    <input type='text' class="form-control"  placeholder="Select week"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
                	</div>
				</div>
				<div class="col-md-3">
					<input type='text' class="form-control"  placeholder="Minimum attendance"/>
				</div>
				<div class="col-md-3">
					<input type='button' class="btn btn-success"  value="Send"/>
				</div>
			</div>
			<div class="row">
				<label class="col-md-3">Month</label>
				<div class="col-md-3">
					<div class='input-group date' id='datepickerMonth'>
	                    <input type='text' class="form-control"  placeholder="Select month"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
                	</div>
				</div>
				<div class="col-md-3">
					<input type='text' class="form-control"  placeholder="Minimum attendance"/>
				</div>
				<div class="col-md-3">
					<input type='button' class="btn btn-success"  value="Send"/>
				</div>
			</div>
			</div>
		</div>
			</div>
			
			<div class="row">
			<div class="col-md-2">
			<label>Progress card</label>
			</div>
			<div class="col-md-8">
				<select id="examSelect"  multiple="multiple" style="width:50%;"></select>
			</div>
			<div class="col-md-2">
				<input type='button' class="btn btn-success"  value="Send"/>
			</div>
			</div>
	</div>
	</div>
</body>
</html>