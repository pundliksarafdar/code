<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.studentattendance}"></c:out>"><a href="studentattendance" data-toggle="tab">Add Attendance</a></li>
		<li class="<c:out value="${param.viewneditFeeStruct}"></c:out>"><a href="viewneditfee">View/Edit Fee Attendance</a></li>
	</ul>
