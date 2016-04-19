<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.attendances}"></c:out>"><a href="teacherAttendance">Add Attendance</a></li>
		<li class="<c:out value="${param.editAttendance}"></c:out>"><a href="teacherEditAttendance">Edit Attendance</a></li>
		<li class="<c:out value="${param.viewAttendance}"></c:out>"><a href="teacherViewAttendance">View Attendance</a></li>
	</ul>
