<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<div class="panel panel-default">
	  <div class="panel-heading">
		<span>	<lable>Review exam</lable>  </span>
		<a href="generateexamsaveaction" class="btn btn-primary btn-xs" style="float: right;">Submit</a>	
	</div>
	  <div class="panel-body">
		<table class="table">
			<thead>
			<tr>
				<th>#</th>
				<th>Question</th>
				<th>Marks</th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${questionData}" var="question" varStatus="counter">
					<tr>
						<td><c:out value="${counter.count}"></c:out></td><td><c:out value="${question.question }"></c:out></td><td><c:out value="${question.marks }"></c:out></td>
					</tr>	
				</c:forEach>
			</tbody>
		</table>
			<a href="generateexamsaveaction" class="btn btn-primary btn-xs" style="float: right;">Submit</a>	
	</div>
</body>
</html>