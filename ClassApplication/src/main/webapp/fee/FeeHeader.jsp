<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.createFeeStruct}"></c:out>"><a href="fees">Create Fee structure</a></li>
		<li class="<c:out value="${param.viewneditFeeStruct}"></c:out>"><a href="viewneditfee">View/Edit Fee structure</a></li>
		<li class="<c:out value="${param.linkFeeStruct}"></c:out>"><a href="linkfee">Link Fee structure</a></li>
		<li class="<c:out value="${param.studentFees}"></c:out>"><a href="studentFees">Student Fees</a></li>
	</ul>
