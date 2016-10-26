<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.customeUserViewRole}"></c:out>"><a href="customeUserViewRole">View/Edit Role</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"52")){ %>	
		<li class="<c:out value="${param.customeUserCreaterole}"></c:out>"><a href="customeUserCreaterole">Create Role</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"54")){ %>
		<li class="<c:out value="${param.customeUserCreateUser}"></c:out>"><a href="customeUserCreateUser">Create User</a></li>
		<%} %>
		<li class="<c:out value="${param.customeUserViewUser}"></c:out>"><a href="customeUserViewUser">View/Edit User</a></li>
	</ul>
