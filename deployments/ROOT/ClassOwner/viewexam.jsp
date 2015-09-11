<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<c:forEach items="'${requestScope.listOfMCQs}'" var="listOfMCQ">
<c:set var="mcqs" value="'${listOfMCQ}'"></c:set>
	<c:out value="'${mcqs.subject_id}'" default="0"></c:out>
</c:forEach>