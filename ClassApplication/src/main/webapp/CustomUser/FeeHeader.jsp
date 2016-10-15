<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
	<% if(ArrayUtils.contains(child_mod_access,"42")){ %>
		<li class="<c:out value="${param.customeUserFees}"></c:out>"><a href="customeUserFees">Create Fee structure</a></li>
		<%} %>
		<li class="<c:out value="${param.customeUserViewNEditFee}"></c:out>"><a href="customeUserViewNEditFee">View/Edit Fee structure</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"45")){ %>
		<li class="<c:out value="${param.customeUserLinkfee}"></c:out>"><a href="customeUserLinkfee">Link Fee structure</a></li>
		<%} %>
		<li class="<c:out value="${param.customeUserStudentFees}"></c:out>"><a href="customeUserStudentFees">Student Fees</a></li>
	</ul>
