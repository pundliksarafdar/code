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
	.table>caption+thead>tr:first-child>th, .table>colgroup+thead>tr:first-child>th, .table>thead:first-child>tr:first-child>th, .table>caption+thead>tr:first-child>td, .table>colgroup+thead>tr:first-child>td, .table>thead:first-child>tr:first-child>td{
border-bottom: 1px solid black;
}
</style>
</head>
<body>
	 
	<jsp:include page="ClassownerSettingHeader.jsp" >
		<jsp:param value="active" name="classownerSettings"/>
	</jsp:include>
	
	 <h4>Notification settings</h4>
	 <form role="form" id="editNotificationSettingForm">
	 <div class="alert alert-danger">You dont have sms, email</div>
	 <div class="container">
	 <label>Fee</label>
	 <table class="table table-striped">
	 <thead>
	 	<tr>
	 		<th>SMS</th>
	 		<th>Email</th>
	 		<th>Parent</th>
	 		<th>Student</th>
	 		<th>Due Date</th>
	 		<th></th>
	 	</tr>
	 </thead>
	 <tbody>
	 	<tr>
	 		<td><input type="checkbox" data-size="mini" data-size="mini" id="smsPayment" class="smsAccess"></td>
	 		<td><input type="checkbox" data-size="mini" id="emailPayment" class="emailAccess"></td>
	 		<td><input type="checkbox" data-size="mini" data-size="mini" id="parentPayment"></td>
	 		<td><input type="checkbox" data-size="mini" id="studentPayment"></td>
	 		<td></td>
	 		<td>On Payment</td>
	 	</tr>
	 	<tr>
	 		<td><input type="checkbox" data-size="mini" id="smsPaymentDue"  class="smsAccess"></td>
	 		<td><input type="checkbox" data-size="mini" id="emailPaymentDue" class="emailAccess"></td>
	 		<td><input type="checkbox" data-size="mini" data-size="mini" id="parentPaymentDue"></td>
	 		<td><input type="checkbox" data-size="mini" id="studentPaymentDue"></td>
	 		<td>
	 					<div id="datetimepicker" class="input-group col-md-5">
						<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="paymentDueDate" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span></div>
	 		</td>
	 		<td>On fee due</td>
	 	</tr>
	 </tbody>
	 </table>
	 </div>
	 
	  <div class="container">
	  <label>Attendance</label>
	  <table class="table table-striped">
	 <thead>
	 	<tr>
	 		<th>SMS</th>
	 		<th>Email</th>
	 		<th>Parent</th>
	 		<th>Student</th>
	 		<th>Choose Days</th>
	 		<th>Below</th>
	 		<th></th>
	 	</tr>
	 </thead>
	 <tbody>
	 <tr>
	 	<td><input type="checkbox" data-size="mini" id="smsAttendanceDaily" class="smsAccess"></td>
	 	<td><input type="checkbox" data-size="mini" id="emailAttendanceDaily" class="emailAccess"></td>
	 	<td><input type="checkbox" data-size="mini" data-size="mini" id="parentAttendanceDaily"></td>
	 	<td><input type="checkbox" data-size="mini" id="studentAttendanceDaily"></td>
	 	<td><div id="weeklyDaySelection"><input type="checkbox" id="Sun"><label for="Sun">Sun</label>&nbsp;
		  		<input type="checkbox" id="Mon"><label for="Mon">Mon</label>&nbsp;
		  		<input type="checkbox" id="Tue"><label for="Tue">Tue</label>&nbsp;
		  		<input type="checkbox" id="Wed"><label for="Wed">Wed</label>&nbsp;
		  		<input type="checkbox" id="Thu"><label for="Thu">Thu</label>&nbsp;
		  		<input type="checkbox" id="Fri"><label for="Fri">Fri</label>&nbsp;
		  		<input type="checkbox" id="Sat"><label for="Sat">Sat</label>&nbsp;</div>
		 </td>
		  		<td></td>
	 	<td>Daily Attendance</td>
	 </tr>
	 <tr>
	 	<td><input type="checkbox" data-size="mini" id="smsAttendanceWeekly" class="smsAccess"></td>
	 	<td><input type="checkbox" data-size="mini" id="emailAttendanceWeekly" class="emailAccess"></td>
	 	<td><input type="checkbox" data-size="mini" data-size="mini" id="parentAttendanceWeekly"></td>
	 	<td><input type="checkbox" data-size="mini" id="studentAttendanceWeekly"></td>
	 	<td></td>
	 	<td><input id="emailAttendanceWeeklyThreshold" type="number" class="form-control" min="0" max="100"/></td>
	 	<td>Weekly Attendance</td>
	 </tr>
	 <tr>
	 	<td><input type="checkbox" data-size="mini" id="smsAttendanceMonthly" class="smsAccess"></td>
	 	<td><input type="checkbox" data-size="mini" id="emailAttendanceMonthly" class="emailAccess"></td>
	 	<td><input type="checkbox" data-size="mini" data-size="mini" id="parentAttendanceMonthly"></td>
	 	<td><input type="checkbox" data-size="mini" id="studentAttendanceMonthly"></td>
	 	<td></td>
	 	<td><input id="emailAttendanceMonthlyThreshold" type="number" class="form-control" min="0" max="100" /></td>
	 	<td>Monthly Attendance</td>
	 </tr>
	 </tbody>
	 </table>
	 </div>
	  <!-- <label>Time table</label>
	  <div class="form-group well">
		  <label class="form-label" for="">Newly added timetable</label>
		  <div>
		  	<label for="">Sms</label>
		  	<input type="checkbox" data-size="mini" id="smsTimetableNewEntry" class="smsAccess">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailTimetableNewEntry" class="emailAccess">
		  	
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
		  	<input type="checkbox" data-size="mini" id="smsTimetableEditEntry" class="smsAccess">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailTimetableEditEntry" class="emailAccess">
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
		  	<input type="checkbox" data-size="mini" id="smsProgressCardManual" class="smsAccess">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailProgressCardManual" class="emailAccess">
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
		  	<input type="checkbox" data-size="mini" id="smsProgressCardAfterEveryExam" class="smsAccess">
		  	<label for="">Email</label>
		  	<input type="checkbox" data-size="mini" id="emailProgressCardAfterEveryExam" class="emailAccess">
		  </div>
		  <div>
			  	<label for="">Parent</label>
			  	<input type="checkbox" data-size="mini" data-size="mini" id="parentProgressCardAfterEveryExam">
			  	<label for="">Student</label>
			  	<input type="checkbox" data-size="mini" id="studentProgressCardAfterEveryExam">
			  </div>
	  </div> -->
	  <div class="container">
	  <input type="button" class="btn btn-success" value="Save" id="save"/>
	  </div>
	 </form>
</body>
</html>