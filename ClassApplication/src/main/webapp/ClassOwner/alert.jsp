<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
	<ul class="nav nav-tabs">
	  <li class="active"><a href=#teacher data-toggle="tab">Teacher</a></li>
	  <li><a href="#studentNParent" data-toggle="tab">Student and Parent</a></li>
	  <li><a href="#sendAlert" data-toggle="tab">Academic Alert</a></li>
	</ul>
	
	<div class="tab-content">
	  <div class="tab-pane active" id="teacher">
	  	Teacher
	  	<form id="selectTeacherMessage">
		  	<Select id="teachers" multiple="multiple" name="teacher" required="required" style="width: 50%;"></Select>
		  	<br>
		  	<br>
		  	<div><textarea placeholder="Message.." class="form-control" id="message" name="teacherMessage" required="required"></textarea></div>
		  	<br>
		  		<div>
				  	<input type="checkbox" id="email" name="messageType" value="email">
				  	<label for="email">EMAIL</label>
				  	&nbsp;&nbsp;
				  	<input type="checkbox" id="sms" name="messageType" value="sms">
				  	<label for="sms">SMS</label>
				  	&nbsp;&nbsp;
				  	<input type="checkbox" id="pushmessage" name="messageType" value="pushmessage">
				  	<label for="pushmessage">Push message</label>
			  	</div>
		  	<br>
		  	<input type="button" value="Send" class="btn btn-success" id="sendMessageToTeacher">
		  	
	  	</form>
	  </div>
	  <div class="tab-pane" id="studentNParent">
	  Student and Parent
	  <form id="selectStudentNParentMessage">
	  	<br>
	  		<div>
		  		<input type="checkbox" id="sendToStudent" name="sendTo" value="student"><label for="sendToStudent">Send to student</label>
		  		&nbsp;&nbsp;
		  		<input type="checkbox" id="sendToParent" name="sendTo" value="parent"><label for="sendToParent">Send to parent</label>
	  		</div>
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
				<div>
	  			<Select name="batchSelect" id="batchSelect" class="form-control" width="100px">
	  			<option value="-1">Select Batch</option>
	  			</Select>
	  			</div>
	  		</div>
	  		</div>
	  		<div>
	  			<textarea placeholder="Message.." class="form-control" id="message" required="require" name="stuNParMessage"></textarea>
	  		</div>	
		  	<br>
		  		<div>
				  	<input type="checkbox" id="emailtostdnparent" name="messageTypeTOST" value="email">
				  	<label for="emailtostdnparent">EMAIL</label>
				  	&nbsp;&nbsp;
				  	<input type="checkbox" id="smstostdnparent" name="messageTypeTOST" value="sms">
				  	<label for="smstostdnparent">SMS</label>
				  	&nbsp;&nbsp;
				  	<input type="checkbox" id="pushmessagetostdnparent" name="messageTypeTOST"  value="pushmessage">
				  	<label for="pushmessagetostdnparent">Push message</label>
			  	</div>
		  	<br>
		  	<input type="button" value="Send" class="btn btn-success" id="sendMessageToStuNPar">
		</form>  	
	  </div>
	  <div class="tab-pane" id="sendAlert">
	  	<jsp:include page="sendAlert.jsp"></jsp:include>
	  </div>
	</div>
</body>
</html>