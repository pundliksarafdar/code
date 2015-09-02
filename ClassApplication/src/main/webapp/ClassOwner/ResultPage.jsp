<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<c:if test="${flag eq 'Y' }">
Congratulation.. You have passed. 
</c:if>
<c:if test="${flag eq 'N' }">
Sorry. You have Failed
</c:if>
You got <c:out value="${Total_Marks }"></c:out> out of <c:out value="${TotalExam_Marks }"></c:out>
</body>
</html>