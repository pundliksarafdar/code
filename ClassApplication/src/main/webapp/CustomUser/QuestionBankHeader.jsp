<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
       <li class="<c:out value="${param.customeUserSearchQuestion}"></c:out>"><a href="customeUserSearchQuestion">Search/Edit Question</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"19")){ %>
		<li class="<c:out value="${param.customeUserAddquestion}"></c:out>"><a href="customeUserAddquestion">Add Question</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"19")){ %>
		<li class="<c:out value="${param.bulkQuestionupload}"></c:out>"><a href="bulkQuestionupload">Add Questions Through File</a></li>
		<%} %>
	</ul>
