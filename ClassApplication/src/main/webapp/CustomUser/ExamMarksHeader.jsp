<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.customeUserViewexamMarks}"></c:out>"><a href="customeUserViewexamMarks">View Exam Marks</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"35")){ %>
		<li class="<c:out value="${param.customeUserExamMarks}"></c:out>"><a href="customeUserExamMarks">Add Exam Marks</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"36")){ %>
		<li class="<c:out value="${param.customeUserEditExamMarks}"></c:out>"><a href="customeUserEditExamMarks">Update Exam Marks</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"38")){ %>
		<li class="<c:out value="${param.customeUserProgressCard}"></c:out>"><a href="customeUserProgressCard">Progress Card</a></li>
		<%} %>
	</ul>
