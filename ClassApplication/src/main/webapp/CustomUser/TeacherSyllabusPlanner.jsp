<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
<jsp:include page="SyllabusHeaders.jsp" >
		<jsp:param value="active" name="customeUserTeacherSetSyllabusAction"/>
	</jsp:include>
	<div class="well">
<div class="row">
			<div class="col-md-3">
				<select name="division" id="divisionSelect" class="form-control filterSelect" width="100px">
					<option value="-1">Select Class</option>
					<c:forEach items="${divisionList}" var="division">
					<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>						
				</select>
				<span id="divisionError" class="error"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="subjectSelect" class="form-control filterSelect" width="100px">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectError" class="error"></span>
			</div>
			<div class="col-md-3 batchDropDown">
				<select name="batch" id="batch" class="form-control filterSelect" width="100px">
					<option value="-1">Select Batch</option>
				</select>
				<span id="batchError" class="error"></span>
			</div>
		</div>
		</div>
		<div class="container" id="addSyllabusContainer" style="width: 100%;">
		<div>
		<h4>Add syllabus</h4>
		<div>
	  	       <div class='input-group date form-group' id='syllabusSetTime' style="width: 500px;">
                   <input type='text' class="form-control" name="syllabusSetTime" required placeholder="Date"/>
                   <span class="input-group-addon">
                       <span class="glyphicon glyphicon-calendar"></span>
                   </span>
               </div>
           </div>
		  <div>
		    <textarea rows="" cols="" placeholder="Syllabus" id="syllabusToSet" class="form-control form-group"></textarea>
		  </div>
		  <div>
		  	<button type="button" class="btn btn-success form-group" id="saveNewSyllabus">Save</button>
		  </div>
		   </div>
		   <hr/>
		    <div>	
				<h4>View syllabus</h4>
			<div id="searches" class="row" style="width: 100%;">
				
				<div class="col-md-5">
					<div class='input-group date' id='syllabusSearchMonth' >
	                   <input type='text' class="form-control" name="syllabusSearchMonth" required placeholder="Month"/>
	                   <span class="input-group-addon">
	                       <span class="glyphicon glyphicon-calendar"></span>
	                   </span>
	               	</div>
				</div>
				<div class="col-md-6">
					<input type="text" class="form-control" placeholder="Search" id="syllabusDatatableSearch">
				</div>
			</div>
			<div>
				<table id="teacher-syllabus-planner-table" class="table scrollable-table">
					
				</table>
			</div>
		</div>
		</div>
		
</body>
</html>