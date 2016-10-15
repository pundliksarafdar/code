<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<ul class="nav nav-tabs" style="border-radius: 10px">
    <li class="<c:out value="${param.customeUserViewnEditPattern}"></c:out>"><a href="customeUserViewnEditPattern">View/Edit Exam Pattern</a></li>
	<% if(ArrayUtils.contains(child_mod_access,"25")){ %>
		<li class="<c:out value="${param.customeUserCreateExamPatten}"></c:out>"><a href="customeUserCreateExamPatten">Create
				Exam Paper Pattern</a></li>
		<%} %>
		<% if(ArrayUtils.contains(child_mod_access,"28")){ %>
		<li class="<c:out value="${param.customeUserGenerateQuestionPaper}"></c:out>"><a href="customeUserGenerateQuestionPaper">Set Question Paper</a></li>
		<%} %>
		<li class="<c:out value="${param.customeUserEditQuestionPaper}"></c:out>"><a href="customeUserEditQuestionPaper">View/Edit Question paper</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"31")){ %>
		<li class="<c:out value="${param.customeUserCreateExam}"></c:out>"><a href="customeUserCreateExam">Create Exam</a></li>
		<%} %>
		<li class="<c:out value="${param.customeUserEditExam}"></c:out>"><a href="customeUserEditExam">View/Edit Exam</a></li>
		<% if(ArrayUtils.contains(child_mod_access,"34")){ %>
		<li class="<c:out value="${param.customeUserOnlineExamList}"></c:out>"><a href="customeUserOnlineExamList">Online Exam</a></li>
		<%} %>
	</ul>
