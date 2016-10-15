<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<% if(ArrayUtils.contains(child_mod_access,"51")){ %>		
		<li class="<c:out value="${param.customeUserPrintStudentCertificate}"></c:out>"><a href="customeUserPrintStudentCertificate" >Print Student Certificate</a></li>
		<%} %>
		<li class="<c:out value="${param.customeUserViewCertificate}"></c:out>"><a href="customeUserViewCertificate" >View/Edit Certificate</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"48")){ %>
		<li class="<c:out value="${param.customeUserCertificate}"></c:out>"><a href="customeUserCertificate">Create Certificate</a></li>
		<%} %>
	</ul>
