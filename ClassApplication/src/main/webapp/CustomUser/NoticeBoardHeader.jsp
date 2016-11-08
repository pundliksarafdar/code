<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.customUserNoticeBoard}"></c:out>"><a href="customUserNoticeBoard">Student Notice</a></li>
		<li class="<c:out value="${param.customUserStaffNoticeBoard}"></c:out>"><a href="customUserStaffNoticeBoard" >Staff Notice</a></li>		
		<li class="<c:out value="${param.customUserViewNoticeBoard}"></c:out>"><a href="customUserViewNoticeBoard" >View Notice</a></li>
	</ul>
