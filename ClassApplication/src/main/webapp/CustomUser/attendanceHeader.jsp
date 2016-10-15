<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
        <li class="<c:out value="${param.customeUserViewAttendance}"></c:out>"><a href="customeUserViewAttendance">View Attendance</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"39")){ %>
		<li class="<c:out value="${param.customeUserAttendances}"></c:out>"><a href="customeUserAttendances">Add Attendance</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"40")){ %>
		<li class="<c:out value="${param.customeUserEditAttendance}"></c:out>"><a href="customeUserEditAttendance">Edit Attendance</a></li>
		<%} %>
	</ul>
