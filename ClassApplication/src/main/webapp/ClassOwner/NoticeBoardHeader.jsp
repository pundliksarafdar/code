<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.noticeBoard}"></c:out>"><a href="noticeBoard">Student Notice</a></li>
		<li class="<c:out value="${param.staffNoticeBoard}"></c:out>"><a href="staffNoticeBoard" >Staff Notice</a></li>		
		<li class="<c:out value="${param.viewNoticeBoard}"></c:out>"><a href="viewNoticeBoard" >View Notice</a></li>
	</ul>
