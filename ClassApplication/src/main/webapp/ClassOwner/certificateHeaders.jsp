<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li class="<c:out value="${param.certificate}"></c:out>"><a href="certificate">Create Certificate</a></li>
		<li class="<c:out value="${param.viewcertificate}"></c:out>"><a href="viewcertificate" >View/Edit Certificate</a></li>		
		<li class="<c:out value="${param.printStudentCertificate}"></c:out>"><a href="printStudentCertificate" >Print Student Certificate</a></li>
	</ul>
