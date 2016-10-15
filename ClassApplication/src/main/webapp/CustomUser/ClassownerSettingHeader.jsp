<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.customeUserClassOwnerSettings}"></c:out>"><a href="customeUserClassOwnerSettings">Notification</a></li>
		<li class="<c:out value="${param.customeUserManageSettings}"></c:out>"><a href="customeUserManageSettings" >Header/Image</a></li>
		<li class="<c:out value="${param.headerSetting}"></c:out>"><a href="headerSettingsAction" >Header settings</a></li>
		<li class="<c:out value="${param.customeUserStudentFormFieldAction}"></c:out>"><a href="customeUserStudentFormFieldAction" >Student form</a></li>		
	</ul>
