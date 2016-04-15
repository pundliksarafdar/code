<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.createExamPatten}"></c:out>"><a href="createExamPatten">Create
				Exam Paper Pattern</a></li>
		<li class="<c:out value="${param.viewneditpattern}"></c:out>"><a href="viewneditpattern">View/Edit Exam Pattern</a></li>
		<li class="<c:out value="${param.generateQuestionPaper}"></c:out>"><a href="generateQuestionPaper">Set Question Paper</a></li>
		<li class="<c:out value="${param.editQuestionPaper}"></c:out>"><a href="editQuestionPaper">View/Edit Question paper</a></li>
		<li class="<c:out value="${param.createExam}"></c:out>"><a href="createExam">Create Exam</a></li>
		<li class="<c:out value="${param.editExam}"></c:out>"><a href="editExam">View/Edit Exam</a></li>
		<li class="<c:out value="${param.onlineExamList}"></c:out>"><a href="onlineExamList">Online Exam</a></li>
	</ul>
