<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
        <li class="<c:out value="${param.customUserViewStudent}"></c:out>"><a href="customUserViewStudent">View Student</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"10")){ %>
		<li class="<c:out value="${param.customUserManageStudent}"></c:out>"><a href="customUserManageStudent">Add Student</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"10")){ %>
		<li class="<c:out value="${param.customUserBulkStudentUpload}"></c:out>"><a href="customUserBulkStudentUpload">Bulk Student Through File</a></li>
		<%} %>
	</ul>
