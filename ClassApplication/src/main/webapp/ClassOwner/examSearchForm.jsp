<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	
	<div class="container bs-callout bs-callout-danger" style="padding-top:0px;">
	<div class="row">
	<div class='col-sm-6 header' style="padding-bottom:10px;">
		<h4>Search Exam</h4>
	</div>
	<div class='col-sm-6 header' style="padding-bottom:10px;">
		* Leave the field unchanged or empty to exclude it from search
	</div>
	</div>
	<form action="searchExamAction" id="searchExam" method="post">
    <div class="row">
    <div class='col-sm-6'>
	<select class="btn btn-default" name="batchId">
	<c:set value="${requestScope.batchList}" var="batches"></c:set>
	<option value="-1">Select Batch</option>
	<c:forEach items="${batches}" var="batch">
		<option value="10"><c:out value="${batch}"></c:out></option>
	</c:forEach>
	</select>
	
	<select class="btn btn-default" name="teacherId">
	<c:set value="${requestScope.listOfteachers}" var="teachers"></c:set>
	<option value="-1">Select Teacher</option>
	<c:forEach items="${teachers}" var="teacher">
		<option value="10"><c:out value="${teacher}"></c:out></option>
	</c:forEach>
	</select>
	
	<select class="btn btn-default" name="subjectId">
	<option value="-1">Select Subject</option>
	<c:set value="${requestScope.subjectList}" var="subjectList"></c:set>
	
	<c:forEach items="${subjectList}" var="subject">
		<option value="10"><c:out value="${subject}"></c:out></option>
	</c:forEach>
	</select>
	</div>
    <div class='col-sm-5'>
               <div class='input-group date col-sm-6' id='fromdate'>
                   <input type='text' class="form-control"  placeholder="From Date" name="startDate" data-date-format="YYYY-MM-DD" readonly="readonly"/>
                   <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                   </span>
               </div>
               <div class='input-group date col-sm-6' id='todate'>
                   <input type='text' class="form-control" placeholder="To Date" name="endDate" data-date-format="YYYY-MM-DD" readonly="readonly"/>
                   <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                   </span>
               </div>
       </div>
       <div class='col-sm-1'>
       		<input type="button" class="btn btn-danger" value="Search" id="searchExamBtn"/>
       </div>
        <script type="text/javascript">
            
                $('#todate,#fromdate').datetimepicker({
                	pickTime: false,
                	format:"YYYY-MM-DD"
                });
                
                $('#searchExamBtn').click(function(){
                	$('form#searchExam').submit();
                });
            
        </script>
    </div>
    </form>
</div>
</body>
</html>