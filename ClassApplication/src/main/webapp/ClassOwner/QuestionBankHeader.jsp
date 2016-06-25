<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.addquestion}"></c:out>"><a href="addquestion">Add Question</a></li>
		<li class="<c:out value="${param.bulkQuestionupload}"></c:out>"><a href="bulkQuestionupload">Add Questions Through File</a></li>
		<li class="<c:out value="${param.searchQuestion}"></c:out>"><a href="searchQuestion">Search/Edit Question</a></li>
	</ul>
