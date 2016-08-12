<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<jsp:include page="FeeHeader.jsp" >
		<jsp:param value="active" name="studentFees"/>
	</jsp:include>
	<div class="well">
		<div class="row">
			<div class="col-lg-3">
				<select class="form-control" id="divisionSelect">
					<option value="-1">Select Class</option>
					<c:forEach var="division" items="${divisions}">
						<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out> <c:out value="${division.stream}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-lg-3">
				<select class="form-control" id="batchSelect">
					<option value="-1">Select Batch</option>
					<option></option>
					<option></option>
				</select>
			</div>
		</div>
		</div>
		<div class="container" id="actionButtons">
		<div class="row">
		<div class="col-md-4">
		<div class="row">
			<div class="col-md-6">
				<div class='input-group'>
      					<span class='input-group-addon'>
						<input type="checkbox" value="sms" id="sms" name="type" required>
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
					<!-- <input type="checkbox" value="sms" id="sms" name="type" required><label for="sms">SMS</label>
					<input type="checkbox" value="email" id="email" name="type" required><label for="email">Email</label> -->
			</div>
			</div>	
			<div id="typeError" style="color: red"></div>
			</div>
			<div class="col-md-4">
				<div class="row">
					<div class="col-md-6">
						<div class='input-group'>
      					<span class='input-group-addon'>
						<input type="checkbox" value="parent" id="parent" name="sendTo" required>
						</span>
						<input type='text' class='form-control' readonly value='Parent' data-toggle='tooltip' title='Parent' style="cursor:default;">
					</div>
					</div>
					<div class="col-md-6">		
					<div class='input-group'>
      					<span class='input-group-addon'>
						<input type="checkbox" value="student" id="student" name="sendTo" required>
						</span>
						<input type='text' class='form-control' readonly value='Student' data-toggle='tooltip' title='Student' style="cursor:default;">
					</div>
					</div>
				</div>	
				<div id="whomeError" style="color: red"></div>
			</div>
			<div class="col-md-2">
				<button class="btn btn-primary" id="sendFeesDue">Send fees due alert to Selected Students</button>
				<span id="selectStudentError" class="error"></span>
			</div>
			</div>
		</div>
		<div class="well alert alert-success" id="notificationSummary" style="display: none;"></div>
		<div class="container" id="studentFeesTableDiv" style="width: 100%">
				<table id="studentFeesTable" class="table table-striped" style="width: 100%"></table>
		</div>
	<div id="printableReceipt" class="hide">
	
	</div>
</body>
</html>