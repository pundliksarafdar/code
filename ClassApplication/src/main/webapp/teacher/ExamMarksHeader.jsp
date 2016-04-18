<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.examMarks}"></c:out>"><a href="teacherExamMarks">Add Exam Marks</a></li>
		<li class="<c:out value="${param.editExamMarks}"></c:out>"><a href="teacherEditExamMarks">Update Exam Marks</a></li>
		<li class="<c:out value="${param.viewexamMarks}"></c:out>"><a href="teacherViewExamMarks">View Exam Marks</a></li>
	</ul>
