<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.classapp.schedule.Scheduledata"%>
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

<div align="left" class="row container">

<%List<RegisterBean> registerBean =(List<RegisterBean>)session.getAttribute("classes");
List<Notification> notifications =(List<Notification>)session.getAttribute("notifications");
Map<String, List<Scheduledata>> map =(Map<String, List<Scheduledata>>)session.getAttribute("todayslect");
String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December" };
%>
<div class="col-sm-6">
<div class="panel-group">
<div class="panel panel-primary">
      <div class="panel-heading"><b>Your classes</b></div>
      <%if(registerBean.size()>0){ %>
      <div class="panel-body">
      <ul>
      <%for(int i=0;i<registerBean.size();i++){ %>
     <li title="1"><%=registerBean.get(i).getClassName() %></li>
      <%} %>
      </ul>
      </div>
      <%}else{ %>
      <div class="panel-body">Yor are not connected to any class</div>
      <%} %>
    </div>
    </div>
	<div>&nbsp;</div>
	<div class="">
	 <div class="panel-group">
    <div class="panel panel-primary" >
      <div class="panel-heading">Todays Lectures</div>
      <div class="panel-body">
<%
DateFormat sdf = new SimpleDateFormat("kk:mm");
DateFormat f2 = new SimpleDateFormat("h:mma");
Iterator it = map.entrySet().iterator();
if(map.size()>0){
while (it.hasNext()) {
    Map.Entry pair = (Map.Entry)it.next();
    System.out.println(pair.getKey() + " = " + pair.getValue());
    %>
   
      <ul>
      <li><b><%=pair.getKey() %></b></li>
      <table class="table">
      <% List<Scheduledata> list=(List<Scheduledata>)pair.getValue();
      for(int i=0;i<list.size();i++){
      %>
      <tr>
      	<%if(list.get(i).getDivision_name()!=null) {%>
      	<td><%=list.get(i).getDivision_name() %></td>
      	<%} %>
      	<td><%=list.get(i).getBatch_name() %></td>
      	<td><%=list.get(i).getSubject_name() %></td>
      	<%
      	Date start=sdf.parse(list.get(i).getStart_time().toString());
      	Date end=sdf.parse(list.get(i).getEnd_time().toString());
      	%>
      	<td><%= f2.format(start).toUpperCase() %></td>
      	<td><%=f2.format(end).toUpperCase()%></td>
      </tr>
      <%} %>
      </table>
      </ul>
      
    <%}
}else{
%>
<div class="alert alert-info" align="center">No lectures today.</div>
<%} %>
</div>
      </div>
      </div>
</div>
	</div>
	
<div class="col-sm-6">	
<div class="panel-group">
    <div class="panel panel-default" >
      <div class="panel-heading" style="background-color:black;color: white;align:center" ><u>Notice Board</u></div>
      <%if(notifications.size()>0){ %>
      <div class="panel-body" style="background-color:black;height: 200px;color: white;">
		<ul>
		<%
		for(int j=0;j<registerBean.size();j++){
			boolean flag= false;
		for(int i=0;i<notifications.size();i++){ 
		if(registerBean.get(j).getRegId()==notifications.get(i).getInstitute_id()){
		%>
		
		<%
			if(flag==false){
		%>
			<u><%=registerBean.get(j).getClassName() %></u>
			<%
		flag=true;	
		} %>
			
			<li type="disc"><%=notifications.get(i).getMessage() %></li>
			
		<%
		}
		}
		}%>
		</ul>
	  </div>
	  <%
		
		} else{%>
	  <div class="panel-body" style="background-color:black;height: 200px;"></div>
	  <%} %>
	  <div align="center">
	  <img src=".\images\2.png" align="center">
		</div>
	</div>
  </div>
</div>
</div>

</body>
</html>