<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<jsp:include page="SyllabusHeaders.jsp" >
		<jsp:param value="active" name="customeUserViewPlannedSyllabusAction"/>
	</jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div>
					<label>Teacher</label>
					<ul class="list-group" id="teachersFilter" data-filter="teacher"></ul>
				</div>
				<div>
					<label>Subject</label>
					<ul class="list-group" id="subjectsFilter" data-filter=subject></ul>
				</div>
				<div>
					<label>Division</label>
					<ul class="list-group" id="divisionsFilter" data-filter="division"></ul>
				</div>
				<!-- <div>
					<label>Batch</label>
					<ul class="list-group" id="batchFilter" data-filter="batchs">
						<li class="list-group-item">
						    <span><input type="checkbox" value=""></span>
						    <label></label>
						</li>
					</ul>
				</div> -->
			</div>
			<div class="col-md-9">
			<div>
				<div class='input-group date form-group' id='syllabusSearchTime' style="width: 40%;float: left;">
                   <input type='text' class="form-control" name="syllabusSetTime" required placeholder="Date"/>
                   <span class="input-group-addon">
                       <span class="glyphicon glyphicon-calendar"></span>
                   </span>
               </div>
               
               <div style="width: 10%;float: right;">
					<input type="button" class="btn btn-default" value="Export" id="printSyllabusPlanner">
				</div>
				
               <div class="btn-group" data-toggle="buttons"  style="width: 40%;float: right;" id="dateTimeRangeButton">
				  <label class="btn btn-primary active">
				    <input type="radio" name="options" id="day" checked="checked" value="day"> Day
				  </label>
				  <label class="btn btn-primary">
				    <input type="radio" name="options" id="month" value="month"> Month
				  </label>
				  <label class="btn btn-primary">
				    <input type="radio" name="options" id="year" value="year"> Year
				  </label>
				</div>
				
				</div>
               <table id="syllabusTable" class="table"/>
			</div>
		</div>
	</div>
	<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
</body>
</html>