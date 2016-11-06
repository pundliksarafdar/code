<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style>

/*This style is to add css*/
.corex-edit{
	display: none;
}

.corex-delete{
	display: none;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
</head>
<body>
<h4>Notes</h4>
<div class="well">
	<div class="row">
	<div id="addModifyTimetableForm">
		<form id="scheduleForm" onsubmit="return false;">
		
		<div class="col-md-3">
			<select class="btn btn-default" id="classSelect" style="width:100%;" name="classSelect">
				<option value="-1">Select Institute</option>
			</select>
		</div>
		<div class="col-md-3">
			<select class="btn btn-default" id="batchSelect" style="width:100%;" name="batchSelect">
				<option value="-1">Select batch</option>
			</select>
		</div>
		<div class="col-md-2">
			<select class="btn btn-default" id="subjectSelect" style="width:100%;" name="subjectSelect">
				<option value="-1">Select subject</option>
			</select>
		</div>
		</form>
	</div>
	</div>
</div>
<div class="container">
	<div class="row">
	<div id="notesContainer" style="display:none">
	<div class="page-header">
		<div id="notes">
		<table id="notesTable" class="table"></table>
		</div>
	</div>
	</div>
	<div id="notesShowMessageContainer" class="col-md-9">
	</div>
</div>
</div>

</body>
</html>