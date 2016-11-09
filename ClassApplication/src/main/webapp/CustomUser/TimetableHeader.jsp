<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.customeUserTimeTableAll}"></c:out>"><a href="customeUserTimeTableAll">Create Timetable</a></li>
		<li class="<c:out value="${param.customUserPrintTimetable}"></c:out>"><a href="customUserPrintTimetable" >Print Timetable</a></li>
	</ul>
