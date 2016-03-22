<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
.nav-tabs .active{
font-weight: bolder;
}
.nav-tabs a{
color: black;
background: #989C9C;
}

.nav-tabs span{
color: white;
}

.nav-tabs>li>a:hover, .nav-tabs>li>a:focus{
color: #0E0D0D;
}
.nav-tabs>li{
width: 30%;
text-align: center;
}
</style>
</head>
<body>
<ul class="nav nav-tabs" style="border-radius:10px">
  <li class="active"><a href="#classtab" data-toggle = "tab">Manage Class</a></li>
  <li><a href="#subjectstab" data-toggle = "tab">Manage Subject</a></li>
  <li><a href="#batchtab" data-toggle = "tab">Manage Batch</a></li>
  </ul>
  <div id = "myTabContent" class = "tab-content">
  <div id="subjectstab" class = "tab-pane fade in">
<jsp:include page="AddSubject.jsp"></jsp:include>
</div>

<div id="classtab" class = "tab-pane fade in active"><jsp:include page="ManageClass.jsp"></jsp:include></div>
<div id="batchtab" class = "tab-pane fade"><jsp:include page="ManageBatch.jsp"></jsp:include></div>
  </div>

</body>
</html>