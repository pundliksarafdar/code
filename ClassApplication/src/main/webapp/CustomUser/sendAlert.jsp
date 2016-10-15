<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
.row{
width: 100%;
}
	/* .row:hover{
		background-color: rgba(10,10,10,0.1);
	}
	 */
/* .toggle:after {
  content: '';
  text-shadow: 0 -1px 0 #bfbfbf;
  position: relative;
  width: 100%;
  height: 100%;
  float: left;
  margin: auto;
  
  -webkit-border-radius: 100%;
  -moz-border-radius: 100%;
  border-radius: 100%;
  
}

.toggle:checked:after {
  background: green; /* Old browsers */

} */
</style>
<script type="text/javascript" src="js/view/customUserSendAlert.js"></script>
</head>
<body>
	<div>
		<div class="container" style="width: 100%">
		<form id="baseform">
			<div class="row" style="width: 100%">
	  		<div class="col-md-3">
		  		<div>
		  		<select name="divisionSelect" id="divisionSelect" class="form-control" width="100px" required>
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				</div>
			</div>
			<div class="col-md-3">
				<div id="sendAlert">
	  			<Select name="batchSelect" id="batchSelect" class="form-control" width="100px">
	  				<option value="-1">Select Batch</option>
	  			</Select>
	  			</div>
	  		</div>
	  		
			<div class="col-md-3">	
				<div class="row">
					<div class="col-md-6">	
					<div class='input-group'>
      				<span class='input-group-addon'>
					<input type="checkbox" value="sms" id="sms" name="type" class="toggle" required>
					</span>
					<input type='text' class='form-control' readonly value='SMS' data-toggle='tooltip' title='SMS' style="cursor:default;">
					</div>
					</div>
					<div class="col-md-6">	
					<div class='input-group'>
      				<span class='input-group-addon'>
					<input type="checkbox" value="email" id="email" name="type" required>
					</span>
					<input type='text' class='form-control' readonly value='Email' data-toggle='tooltip' title='Email' style="cursor:default;">
					</div>
					</div>
					<input type="text" name="typeError" style="border: white;width: 0px;height: 0px;">
				</div>
			</div>
			<div class="col-md-3">
				<div class="row">
					<div class="col-md-6">	
					<div class='input-group'>
      				<span class='input-group-addon'>
					<input type="checkbox" value="parent" id="parent" name="sendTo" required/>
					</span>
					<input type='text' class='form-control' readonly value='Parent' data-toggle='tooltip' title='Parent' style="cursor:default;">
					</div>
					</div>
					<div class="col-md-6">	
					<div class='input-group'>
      				<span class='input-group-addon'>
					<input type="checkbox" value="student" id="student" name="sendTo" required/>
					</span>
					<input type='text' class='form-control' readonly value='Student' data-toggle='tooltip' title='Student' style="cursor:default;">
					</div>
					</div>
					<input type="text" name="sendToError" style="border: white;width: 0px;height: 0px;">
				</div>	
			</div>
		</div>
		</form>
		</div>
		<div class="well alert alert-success" id="notificationSummary" style="display: none;"></div>
		<div class="container">
		<div class="row">
			<label class="col-md-3">Fee due</label>	
			<input type="button" value="Send" class="btn btn-success" id="sendFeeDue"/>
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
				<form>
				<label class="col-md-3">Day</label>
				<input type='hidden' class="type" value="day"/>
				<div class="col-md-3">
					<div class='input-group date' id='datepickerDay'>
	                    <input type='text' class="form-control" placeholder="Select date" required name="dayDatePicker"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
                	</div>
				</div>
				<div class="col-md-3">
					<input type='text' class="form-control"   id="minAttendance" placeholder="Minimum attendance" required name="dayAttendance"/>
				</div>
				<div class="col-md-3">
					<input type='button' class="btn btn-success sendAttendance"  value="Send"/>
				</div>
				</form>
			</div>
			<div class="row">
				<form>
				<label class="col-md-3">Week</label>
				<input type='hidden' class="type" value="week"/>
				<div class="col-md-3">
					<div class='input-group date' id='datepickerWeek'>
	                    <input type='text' class="form-control"  placeholder="Select week" required name="monthDatePicker"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
                	</div>
				</div>
				<div class="col-md-3">
					<input type='text' class="form-control"   id="minAttendance" placeholder="Minimum attendance" name="weekAttendance" required/>
				</div>
				<div class="col-md-3">
					<input type='button' class="btn btn-success sendAttendance"  value="Send"/>
				</div>
				</form>
			</div>
			<div class="row">
				<form>
				<label class="col-md-3">Month</label>
				<input type='hidden' class="type" value="month"/>
				<div class="col-md-3">
					<div class='input-group date' id='datepickerMonth'>
	                    <input type='text' class="form-control"  placeholder="Select month" name="monthDatePicker" required/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
                	</div>
				</div>
				<div class="col-md-3">
					<input type='text' class="form-control" id="minAttendance" placeholder="Minimum attendance" name="monthAttendance" required/>
				</div>
				<div class="col-md-3">
					<input type='button' class="btn btn-success sendAttendance"  value="Send"/>
				</div>
				</form>
			</div>
			</div>
		</div>
			</div>
			
			<div class="row">
			<div class="col-md-2">
			<label>Progress card</label>
			</div>
			<form>
			<div class="col-md-8">
				<select id="examSelect" name="examSelect" class="form-control" multiple="multiple" style="width: 50%"></select>
			</div>
			<div class="col-md-2">
				<input type='button' class="btn btn-success"  value="Send" id="sendProgressCard"/>
			</div>
			</form>
			</div>
	</div>
	</div>
</body>
</html>