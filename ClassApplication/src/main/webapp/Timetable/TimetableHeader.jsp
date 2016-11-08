<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.timetableall}"></c:out>"><a href="timetableall">Create Timetable</a></li>
		<li class="<c:out value="${param.printTimetable}"></c:out>"><a href="printTimetable" >Print Timetable</a></li>
	</ul>
