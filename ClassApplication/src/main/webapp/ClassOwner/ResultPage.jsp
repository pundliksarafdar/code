<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).on("keydown", function (e) {
    if (e.which === 8 && !$(e.target).is("input, textarea")) {
        e.preventDefault();
    }
});
</script>
</head>
<body style="margin: 10px">
<div class="col-md-3">
<c:if test="${flag eq 'Y' }">
<img alt="Congrats" src="/images/congrats.png"><br>
Congratulation.. You have passed.
You got <c:out value="${Total_Marks }"></c:out> out of <c:out value="${TotalExam_Marks }"></c:out>
</c:if>
<c:if test="${flag eq 'N' }">
<img alt="Sorry" src="/images/fail.png"><br>
Sorry. You have Failed
You got <c:out value="${Total_Marks }"></c:out> out of <c:out value="${TotalExam_Marks }"></c:out>

</c:if>
</div>
</body>
</html>