<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
       <li class="<c:out value="${param.customeUserViewPlannedSyllabusAction}"></c:out>"><a href="customeUserViewPlannedSyllabusAction">View Syllabus</a></li>
       <% if(ArrayUtils.contains(child_mod_access,"56")){ %>
		<li class="<c:out value="${param.customeUserTeacherSetSyllabusAction}"></c:out>"><a href="customeUserTeacherSetSyllabusAction">Create/Edit Syllabus</a></li>
	<%} %>	 
	</ul>
