<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
        <li class="<c:out value="${param.customeUserSeeNotes}"></c:out>"><a href="customeUserSeeNotes">View Notes</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"16")){ %>
		<li class="<c:out value="${param.customUserAddNotes}"></c:out>"><a href="customUserAddNotes">Add Notes</a></li>
		<%} %>
	</ul>
