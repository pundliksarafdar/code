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
<h3>You are linked with Following Classes:-</h3><Br>
<ul>
<%int counter=0;
while(counter<registerBean.size()){
%>

<li><%=registerBean.get(counter).getClassName() %></li>
<%
counter++;
} %>
</ul>
<%}else{ %>
<h3>You are not linked with any Class</h3><Br>
<%} %>
</div>
<div>
<img alt="temp" src="images/background.png" height="400px" width="800px">
</div>
</body>
</html>