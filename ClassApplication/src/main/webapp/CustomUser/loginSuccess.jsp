<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.miscfunction.MiscFunction"%>
<%@page import="java.util.Date"%>
<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<style>
	.tooltip{
		width : 250px;
	}
</style>
<body>
<div class="col-sm-6">	
<div class="panel-group">
    <div class="panel panel-primary" >
      <div class="panel-heading">
      	<strong>Notice Board</strong>
      </div>
    
      <div class="panel-body">
       <c:if test="${not empty noticeList}">
	  <c:forEach  items="${noticeList}" var="list">
 		 <ul>
 		 <li> <c:out value="${list.notice}"/></li>
 		 </ul>
 		 </c:forEach>
 		 </c:if>
 		 <c:if test="${empty noticeList}">
 		 NA
 		 </c:if>
		
	</div>
  </div>
</div>
</div>
<%
DateFormat sdf = new SimpleDateFormat("kk:mm");
DateFormat f2 = new SimpleDateFormat("h:mma");
%>
<div class="col-sm-6">	
<div class="panel-group">
    <div class="panel panel-primary" >
      <div class="panel-heading">
      	<strong>Todays Lectures</strong>
      </div>
    
      <div class="panel-body">
       <c:if test="${not empty todayslect}">
       <table class="table">
	  <c:forEach  items="${todayslect}" var="lecture">
	  <c:set var="start_time" value='${lecture.start_time}' />
	  <c:set var="end_time" value='${lecture.end_time}' />
	  <%
	  String start_time = (String)pageContext.getAttribute("start_time").toString();
	  String end_time = (String)pageContext.getAttribute("end_time").toString();
	  Date start=sdf.parse(start_time);
      Date end=sdf.parse(end_time);
      pageContext.setAttribute("start_time", f2.format(start).toUpperCase());
      pageContext.setAttribute("end_time", f2.format(end).toUpperCase());
	  %>
	  <tr>
	  	<td><c:out value="${lecture.division_name}"/></td>
 		 <td><c:out value="${lecture.batch_name}"/></td>
 		 <td><c:out value="${lecture.subject_name}"/></td>
 		 <td> <c:out value="${start_time}"/></td>
 		 <td><c:out value="${end_time}"/></td>
 	 </tr>	 
 		 </c:forEach>
 		 </table>	
 		 </c:if>
 		 <c:if test="${empty todayslect}">
 		 NA
 		 </c:if>
		
	</div>
  </div>
</div>
</div>
</body>
</html>