<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
	label.form-label + div {
		padding-left:10px;
	}
</style>
</head>
<body>
	 
	<jsp:include page="ClassownerSettingHeader.jsp" >
		<jsp:param value="active" name="classownerSettings"/>
	</jsp:include>
	
	 <h4>Edit notification settings</h4>
	 <form role="form" id="editNotificationSettingForm">
	 <div class="alert alert-danger">You dont have sms, email</div>
	 <label>Fee</label>
	  <div class="form-group well">
		  <label class="form-label" for="">On payment</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" data-size="mini" id="smsPayment">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailPayment">
		  </div>
		  <div>
		  	<label for="">Parent</label>
		  	<input type="checkbox" data-size="mini" data-size="mini" id="parentPayment">
		  	<label for="">Student</label>
		  	<input type="checkbox" data-size="mini" id="studentPayment">
		  </div>
		  <br/>
		  <label class="form-label" for="">On fee due</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsPaymentDue">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailPaymentDue">
		  </div>
	    <div>
		  	<label for="">Parent</label>
		  	<input type="checkbox" data-size="mini" data-size="mini" id="parentPaymentDue">
		  	<label for="">Student</label>
		  	<input type="checkbox" data-size="mini" id="studentPaymentDue">
		  </div>
		   <div class="row"><div class="col-md-1"><label for="">Due date:</label></div><div id="datetimepicker" class="col-md-3 input-group">
						<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="paymentDueDate" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span></div>
			</div>
	  </div>
	  
	  <label>Attendance</label>
	  <div class="form-group well">
		  <label  class="form-label" for="">Daily</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsAttendanceDaily">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailAttendanceDaily">
		  	<div id="weeklyDaySelection">
		  		<label for="">Send on &nbsp;</label>
		  		<input type="checkbox" id="Sun"><label for="Sun">Sun</label>&nbsp;
		  		<input type="checkbox" id="Mon"><label for="Mon">Mon</label>&nbsp;
		  		<input type="checkbox" id="Tue"><label for="Tue">Tue</label>&nbsp;
		  		<input type="checkbox" id="Wed"><label for="Wed">Wed</label>&nbsp;
		  		<input type="checkbox" id="Thu"><label for="Thu">Thu</label>&nbsp;
		  		<input type="checkbox" id="Fri"><label for="Fri">Fri</label>&nbsp;
		  		<input type="checkbox" id="Sat"><label for="Sat">Sat</label>&nbsp;
		  	</div>
		  	<div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentAttendanceDaily">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentAttendanceDaily">
			  </div>
		  </div>
		  <br/>
		  <label class="form-label" for="">Weekly</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsAttendanceWeekly">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailAttendanceWeekly">
		  	<div>
			  	<label for="">Below %</label>
			  	<input id="emailAttendanceWeeklyThreshold" type="number" class="form-control" min="0" max="100" style='width:10%;'/>
		  	</div>
		  	<div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentAttendanceWeekly">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentAttendanceWeekly">
			  </div>
		  </div>
		  <br/>
		  <label class="form-label" for="">Monthly</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsAttendanceMonthly">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailAttendanceMonthly">
		  	<div>
			  	<label for="">Below %</label>
			  	<input id="emailAttendanceMonthlyThreshold" type="number" class="form-control" min="0" max="100" style='width:10%;'/>
		  	</div>
		  	<div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentAttendanceMonthly">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentAttendanceMonthly">
			  </div>
		  </div>
	    	
	  </div>
	  <label>Time table</label>
	  <div class="form-group well">
		  <label class="form-label" for="">Newly added timetable</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsTimetableNewEntry">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailTimetableNewEntry">
		  	
		  	<div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentTimetableNewEntry">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentTimetableNewEntry">
			  </div>
		  </div>
		  <br/>
		  <label class="form-label" for="">Change in timetable</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsTimetableEditEntry">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailTimetableEditEntry">
		  </div>
		  	<div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentTimetableEditEntry">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentTimetableEditEntry">
			  </div>
	  </div>
	  <label>Progress card</label>
	  <div class="form-group well">
		  <label class="form-label" for="">Manual</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsProgressCardManual">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailProgressCardManual">
		  </div>
		  <div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentProgressCardManual">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentProgressCardManual">
			</div>
			<br/>
		  <label class="form-label" for="">After every exam</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsProgressCardAfterEveryExam">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailProgressCardAfterEveryExam">
		  </div>
		  <div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentProgressCardAfterEveryExam">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentProgressCardAfterEveryExam">
			  </div>
	  </div>
	  <input type="button" class="btn btn-default" value="Save" id="save"/>
	  
	 </form>
</body>
</html>