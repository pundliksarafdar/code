<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.classownerSettings}"></c:out>"><a href="classownerSettings">Notification</a></li>
		<li class="<c:out value="${param.notificationSetting}"></c:out>"><a href="manageSettings" >Header/Image</a></li>		
	</ul>
