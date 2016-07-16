<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
</head>
<body>
	<div class="alert alert-warning">
		Score :- <c:out value="${marks}"></c:out>/<c:out value="${totalMarks}"></c:out>
	</div>
	
</body>
</html>