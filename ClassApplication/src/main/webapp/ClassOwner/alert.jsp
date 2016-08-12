<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
	/* .select2-container {
	  min-width: 260px !important;
	} */
</style>
</head>
<body>
	<ul class="nav nav-tabs">
	  <li class="active"><a href=#teacher data-toggle="tab">Teacher</a></li>
	  <li><a href="#studentNParent" data-toggle="tab">Student and Parent</a></li>
	  <li><a href="#sendAlert" data-toggle="tab">Academic Alert</a></li>
	</ul>
	
	<div class="tab-content" style="padding: 1%">
	  <div class="tab-pane active" id="teacher">
	  	<div class="container">
	  	<form id="selectTeacherMessage">
	  		<div class="row">
	  		<div class="col-md-6">
		  	<Select id="teachers" multiple="multiple" name="teacher" required="required" style="width: 100%"></Select>
		  	<span id="teacherSelectError" class="error"></span>
		  	</div>
		  	</div>
		  	<div class="row">
	  		<div class="col-md-6"><textarea placeholder="Message.." class="form-control" id="message" name="teacherMessage" required="required" maxlength="320"></textarea>
	  		<span id="teacherMessageError" class="error"></span>
	  		</div>
		  	</div>
		  		<div class="row">
					<div class="col-md-2">
						<div class='input-group'>
      					<span class='input-group-addon'>
				  		<input type="checkbox" id="email" name="messageType" value="email">
				  		</span>
				  		<input type='text' class='form-control' readonly value='EMAIL' data-toggle='tooltip' title='EMAIL' style="cursor:default;">
				  		</div>
				  	</div>
				  	<div class="col-md-2">
						<div class='input-group'>
      					<span class='input-group-addon'>	
				  		<input type="checkbox" id="sms" name="messageType" value="sms">
				  		</span>
				  		<input type='text' class='form-control' readonly value='SMS' data-toggle='tooltip' title='SMS' style="cursor:default;">
				  	</div>
				  	</div>
				  	<div class="col-md-3">
						<div class='input-group'>
      					<span class='input-group-addon'>
				  		<input type="checkbox" id="pushmessage" name="messageType" value="pushmessage">
				  		</span>
				  		<input type='text' class='form-control' readonly value='Push message' data-toggle='tooltip' title='Push message' style="cursor:default;">
				  	</div>
				  	</div>  	
			  	</div>
			<div class="row">
					<div class="col-md-2">
					<span id="teacherMessageTypeError" class="error"></span>
					</div>
			</div>		
		  <div class="row">
				<div class="col-md-4">
		  	<input type="button" value="Send" class="btn btn-success" id="sendMessageToTeacher">
		  		</div>
		  </div>		
	  	</form>
	  	</div>
	  </div>
	  <div class="tab-pane" id="studentNParent">
	  <form id="selectStudentNParentMessage">
	  <div class="container">
	  		<div class="row">
					<div class="col-md-2">
					<div class='input-group'>
      				<span class='input-group-addon'>
		  			<input type="checkbox" id="sendToStudent" name="sendTo" value="student">
		  			</span>
		  			<input type='text' class='form-control' readonly value='Student' data-toggle='tooltip' title='Student' style="cursor:default;">
		  			</div>
		  			</div>
		  			<div class="col-md-2">
		  			<div class='input-group'>
      				<span class='input-group-addon'>
		  			<input type="checkbox" id="sendToParent" name="sendTo" value="parent">
		  			</span>
		  			<input type='text' class='form-control' readonly value='Parent' data-toggle='tooltip' title='Parent' style="cursor:default;">
		  			</div>
		  			</div>
	  		</div>
	  		<div class="row">
					<div class="col-md-2">
					<span id="sendToStudentORParentError" class="error"></span>
					</div>
			</div>
	  		<div class="row">
			  <div class="col-md-3">
		  		<select name="divisionSelect" id="divisionSelect" class="form-control">
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="sendToStudentORParentDivisionSelectError" class="error"></span>
			</div>
			<div class="col-md-3">
			
	  			<Select name="batchSelect" id="batchSelect" class="form-control" width="100px">
	  			<option value="-1">Select Batch</option>
	  			</Select>
	  			<span id="sendToStudentORParentBatchSelectError" class="error"></span>
	  		</div>
	  		</div>
	  		<div class="row">
			  <div class="col-md-6">
	  			<textarea placeholder="Message.." class="form-control" id="message" required="require" name="stuNParMessage" maxlength="320"></textarea>
	  			<span id="stuNParMessageError" class="error"></span>
	  		</div>	
	  		</div>	
		  		<div class="row">
			  	<div class="col-md-2">
			  		<div class='input-group'>
      				<span class='input-group-addon'>
				  	<input type="checkbox" id="emailtostdnparent" name="messageTypeTOST" value="email">
				  	</span>
				  	<input type='text' class='form-control' readonly value='EMAIL' data-toggle='tooltip' title='EMAIL' style="cursor:default;">
				  	</div>
				 </div>
				 <div class="col-md-2">
				 	<div class='input-group'>
      				<span class='input-group-addon'>
				  	<input type="checkbox" id="smstostdnparent" name="messageTypeTOST" value="sms">
				  	</span>
				  	<input type='text' class='form-control' readonly value='SMS' data-toggle='tooltip' title='SMS' style="cursor:default;">
				  	</div>
				  </div>
				  <div class="col-md-3">
				  	<div class='input-group'>
      				<span class='input-group-addon'>
				  	<input type="checkbox" id="pushmessagetostdnparent" name="messageTypeTOST"  value="pushmessage">
				  	</span>
				  	<input type='text' class='form-control' readonly value='Push message' data-toggle='tooltip' title='Push message' style="cursor:default;">
				  	</div>
				  </div>	
			  	</div>
			  	<div class="row">
					<div class="col-md-2">
					<span id="messageTypeTOSTError" class="error"></span>
					</div>
			</div>
		 	 <div class="row">
			  	<div class="col-md-2">
		  	<input type="button" value="Send" class="btn btn-success" id="sendMessageToStuNPar">
		  	   </div>
		  	   </div>
		  	</div>
		</form>  	
	  </div>
	  <div class="tab-pane" id="sendAlert">
	  	<jsp:include page="sendAlert.jsp"></jsp:include>
	  </div>
	</div>
</body>
</html>