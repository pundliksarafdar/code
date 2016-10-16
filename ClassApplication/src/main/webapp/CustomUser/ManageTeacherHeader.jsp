<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
        <li class="<c:out value="${param.customUserViewTeacher}"></c:out>"><a href="customUserViewTeacher">View Teachers</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"13")){ %>
		<li class="<c:out value="${param.customUserManageTeacher}"></c:out>"><a href="customUserManageTeacher">Add Teacher</a></li>
		<%} %>
	</ul>
