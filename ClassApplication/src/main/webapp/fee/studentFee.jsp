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
	<div class="container">
		<div class="row">
			<div class="col-lg-3">
				<select class="btn btn-default" id="divisionSelect">
					<option value="-1">Select Division</option>
					<c:forEach var="division" items="${divisions}">
						<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-lg-3">
				<select class="btn btn-default" id="batchSelect">
					<option value="-1">Select batch</option>
					<option></option>
					<option></option>
				</select>
			</div>
			<div class="col-lg-3">
				<input type="button" class="btn btn-default" value="Load" id="batchNFeeTableLoad"/>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<table id="studentFeesTable" class="table table-striped"></table>
			</div>
		</div>
	</div>
	<div id="printableReceipt" class="hide">
	
	</div>
</body>
</html>