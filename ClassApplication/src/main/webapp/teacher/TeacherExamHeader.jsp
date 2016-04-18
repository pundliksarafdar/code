<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.createExamPatten}"></c:out>"><a href="createTeacherExamPatten">Create
				Exam Paper Pattern</a></li>
		<li class="<c:out value="${param.viewneditpattern}"></c:out>"><a href="viewneditteacherpattern">View/Edit Exam Pattern</a></li>
		<li class="<c:out value="${param.generateQuestionPaper}"></c:out>"><a href="generateteacherQuestionPaper">Set Question Paper</a></li>
		<li class="<c:out value="${param.editQuestionPaper}"></c:out>"><a href="editteacherQuestionPaper">View/Edit Question paper</a></li>
		<li class="<c:out value="${param.onlineExamList}"></c:out>"><a href="onlineExamList">Online Exam</a></li>
	</ul>
