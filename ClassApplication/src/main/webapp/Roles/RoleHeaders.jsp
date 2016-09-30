<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.createrole}"></c:out>"><a href="createrole">Create Role</a></li>
		<li class="<c:out value="${param.viewrole}"></c:out>"><a href="viewrole">View/Edit Role</a></li>
		<li class="<c:out value="${param.createuser}"></c:out>"><a href="createuser">Create User</a></li>
		<li class="<c:out value="${param.viewuser}"></c:out>"><a href="viewuser">View/Edit User</a></li>
	</ul>
