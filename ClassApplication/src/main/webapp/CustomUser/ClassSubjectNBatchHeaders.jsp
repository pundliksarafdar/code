<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] parent_mod_access = (String[])session.getAttribute("parent_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
	<% if(ArrayUtils.contains(parent_mod_access,"1")){ %>
		<li class="<c:out value="${param.customeUserManageclass}"></c:out>"><a href="customeUserManageclass">Manage Class</a></li>
	<%} %>
	<% if(ArrayUtils.contains(parent_mod_access,"2")){ %>
		<li class="<c:out value="${param.customUserManagesubject}"></c:out>"><a href="customUserManagesubject">Manage Subject</a></li>
	<%} %>
	<% if(ArrayUtils.contains(parent_mod_access,"3")){ %>
		<li class="<c:out value="${param.customUserManageBatch}"></c:out>"><a href="customUserManageBatch">Manage Batch</a></li>
	<%} %>
	</ul>
