<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.classapp.db.notificationpkg.Notification"%>
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

<div align="left" class="col-sm-12">

<%List<RegisterBean> registerBean =(List<RegisterBean>)session.getAttribute("classes");
List<Notification> notifications =(List<Notification>)session.getAttribute("notifications");
String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December" };
if(registerBean.size()>0){
%>
<div class="alert alert-info">You are linked with Following Classes</div>

<div class="btn-group-vertical" role="group" aria-label="...">
<%int counter=0;
while(counter<registerBean.size()){
%>
<button type="button" class="btn btn-default"><%=registerBean.get(counter).getClassName() %></button>
<%
counter++;
} %>
</div><Br>
<%}else{ %>
<div class="alert alert-info">You are not linked with any Class</div>
<%}%>
<hr>
<%
if(notifications!=null){
	if(notifications.size()>0){
		boolean flag=false;
		%>
		<div class="alert alert-info">Notifications</div>
		<div>
		<%
		for(int i=0;i<registerBean.size();i++){
			flag=false;
			String institutename=registerBean.get(i).getClassName();
			%>
			<p><b><%=institutename %> </b></p>
			<ul>
			<%
			for(int j=0;j<notifications.size();j++){
			if(registerBean.get(i).getRegId()==notifications.get(j).getInstitute_id())
			{
			flag=true;
			%>
			<li><%=monthName[notifications.get(j).getMsg_date().getMonth()]+" "+ notifications.get(j).getMsg_date().getDate()+" -> "+notifications.get(j).getBatch_name()+" -> "+notifications.get(j).getMessage() %></li><Br>
		<%}}
		if(flag==false)	{
			%>
			<li>No notifications available</li><Br>
			<%
		}%>
		</ul>
		<% 
		}
		%>
		</div>
		<%
		}else{
		%>
		<div class="alert alert-info">Notifications not available</div><Br>
		<%
	}
}
%>
</div>
<div  class="col-sm"> 
<img alt="temp" src="images/background.png" >
</div>
</body>
</html>