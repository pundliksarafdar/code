<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.manageclass}"></c:out>"><a href="manageclass">Manage Class</a></li>
		<li class="<c:out value="${param.addsubject}"></c:out>"><a href="addsubject">Manage Subject</a></li>
		<li class="<c:out value="${param.managebatch}"></c:out>"><a href="managebatch">Manage Batch</a></li>
	</ul>
