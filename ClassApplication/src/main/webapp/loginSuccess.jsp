<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Loged In</title>
</head>
<body>

<div align="left" >

<%List<RegisterBean> registerBean =(List<RegisterBean>)session.getAttribute("classes"); 
if(registerBean.size()>0){
%>
<div class="alert alert-info">You are linked with Following Classes</div>><Br>

<div class="btn-group-vertical" role="group" aria-label="...">
<%int counter=0;
while(counter<registerBean.size()){
%>
<button type="button" class="btn btn-default"><%=registerBean.get(counter).getClassName() %></button>
<%
counter++;
} %>
</div>
<%}else{ %>
<div class="alert alert-error">You are not linked with any Class</div><Br>
<%} %>
</div>
<div>
<img alt="temp" src="images/background.png" height="100%" width="100%">
</div>
</body>
</html>