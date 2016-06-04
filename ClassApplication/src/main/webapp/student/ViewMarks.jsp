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

/*Style for hovetooltip*/
.morris-hover{position:absolute;z-index:1000;}
.morris-hover.morris-default-style{border-radius:10px;padding:6px;color:#666;background:rgba(255, 255, 255, 0.8);border:solid 2px rgba(230, 230, 230, 0.8);font-family:sans-serif;font-size:12px;text-align:center;}
.morris-hover.morris-default-style .morris-hover-row-label{font-weight:bold;margin:0.25em 0;}
.morris-hover.morris-default-style .morris-hover-point{white-space:nowrap;margin:0.1em 0;}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
</head>
<body>
<div class="container">
	<div class="row">
	<div id="addModifyTimetableForm">
		<form id="scheduleForm" onsubmit="return false;">
		
		<div class="col-md-3">
		<label for="classSelect">Class select</label><br>
			<select class="btn btn-default" id="classSelect" style="width:100%;" name="classSelect">
				<option value="-1">Select Class</option>
			</select>
		</div>
		
		<div class="col-md-3">
		<label for="divisionSelect">Division select</label><br>
			<select class="btn btn-default" id="divisionSelect" style="width:100%;" name="divisionSelect">
				<option value="-1">Select Division</option>
			</select>
		</div>
		<div class="col-md-3">
		<label for="batchSelect">Select batch</label><br>
			<select class="btn btn-default" id="batchSelect" style="width:100%;" name="batchSelect">
				<option value="-1">Select batch</option>
			</select>
		</div>
		
		
		<div id="buttons" class="col-md-1">
		<label for="subjectSelect">&nbsp;</label><br>
			<div class="form-group">
                <button class="btn btn-success" id="view">View</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<div class="row">
	<div id="notesContainer" style="display:none">
	<div class="page-header">
		<div id="notes">
		<div id="marksGraph" style="height: 250px;"></div>
		<table id="notesTable" class="table"></table>
		</div>
		<div id="examMarksByExamTableWrap">
			<input type="button" id="showExamList" value="Back to list" class="btn btn-default"/>
			<div id="marksBySubjectGraph" style="height: 250px;"></div>
			<table id="examMarksByExamTable" class="table"></table>
			</div>
		</div>
	</div>
	</div>
	<div id="notesShowMessageContainer" class="col-md-9">
		<h4></h4>
	</div>
</div>

</body>
</html>