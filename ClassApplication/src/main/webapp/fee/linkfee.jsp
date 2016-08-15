<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
#distributionTable_length{
display: none;
}
#distributionTable_filter{
display: none;
}
#distributionTable_info{
display: none;
}
#distributionTable_paginate{
display: none;
}
</style>
</head>
<body>
	<jsp:include page="FeeHeader.jsp" >
		<jsp:param value="active" name="linkFeeStruct"/>
	</jsp:include>
	
	<div class="well">
		<div class="col-lg-12" id="linkBatchContainer" >
		<div class="row">
			<div class="col-lg-3">
				<select class="form-control" id="divisionSelect">
					<option value="-1">Select Division</option>
					<c:forEach var="division" items="${divisions}">
						<option value='<c:out value="${division.divId}"></c:out>'><c:out value="${division.divisionName}"></c:out> <c:out value="${division.stream}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-lg-3">
				<select class="form-control" id="batchSelect">
					<option value="-1">Select batch</option>
					<option></option>
					<option></option>
				</select>
			</div>
			<div class="col-lg-3">
				<select class="form-control" id="feeStructSelect">
					<option value="-1">Select fee structure</option>
				</select>
			</div>
		</div>
		</div>
	</div>
	<div class="container">	
		<div class="row">
		<div class="col-lg-12 feeDistributionWrapper">
			<table id="distributionTable" class="table table-striped distributionTable">
			<thead>
			<tr>
			<th style="width: 70%">Distribution Item</th>
			<th style="width: 30%">Amount</th>
			</tr>
			</thead>
			<tbody></tbody>
			</table>
			<table id="totalDistributionTable" class="table table-striped distributionTable">
			<tr><td style="text-align: right;width: 50%;">Total</td><td class="total" style="text-align: right;width: 50%;"></td></tr>
			</table>
			<select class="form-control" id="divisionSelect">
					<option value="-1">Select Header</option>
					<c:forEach var="header" items="${headers}">
						<option value='<c:out value="${header.header_id}"></c:out>'><c:out value="${header}"></c:out></option>
					</c:forEach>
				</select>
			
			<button id="distributionSave" value="Save" class="btn btn-success">Save</button>
		</div>
		</div>
	</div>
</body>
</html>