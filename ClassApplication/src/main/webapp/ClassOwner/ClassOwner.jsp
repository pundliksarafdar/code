<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page import="com.classapp.db.notificationpkg.Notification"%>
<%@page import="java.util.List"%>
<html>
<head>
<!-- <link href="../assets/css/style.css" rel="stylesheet" /> -->
</head>
<div class="container">
	<%
int batchcount=(Integer)session.getAttribute("batchcount"); 
int studentcount=(Integer)session.getAttribute("studentcount");
int teachercount=(Integer)session.getAttribute("teachercount");
List<Notification> notifications =(List<Notification>)session.getAttribute("notifications");
String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December" };
%>
	<div class="row">
		<%-- <div class="col-md-3 col-sm-3 col-xs-6">
			<div class="panel panel-primary">
				<div class="panel-heading">NO OF BATCHES</div>
				<div class="panel-body">
					<ul class="plan">
						<li class="price"><strong><%=batchcount %></strong>&nbsp;&nbsp;&nbsp;</li>
					</ul>
				</div>
			</div>
		</div>


		<div class="col-md-3 col-sm-3 col-xs-6">
			<div class="panel panel-primary">
				<div class="panel-heading">NO OF TEACHERS</div>
				<div class="panel-body">

					<ul class="plan">
						<li class="price"><strong><%=teachercount %></strong>&nbsp;&nbsp;&nbsp;</li>
					</ul>
				</div>
			</div>
		</div> --%>
		
		<!-- 
		<div class="col-md-3">
			<div class="btn-group-vertical">
				<div class="btn-group">
					<button class="btn btn-primary" type="button">NO OF BATCHES</button>
					<button type="button" class="btn btn-danger" aria-expanded="false">
    					<span><%= batchcount%></span>
    				</button>
    			</div>
    			<div class="btn-group">
				<button class="btn btn-primary" type="button">  NO OF TEACHERS</button>
					<button type="button" class="btn btn-danger" aria-expanded="false">
    					<span><%= teachercount%></span>
    				</button>
				</div>
				<div class="btn-group">
				<button class="btn btn-primary" type="button">  NO OF STUDENTS</button>
					<button type="button" class="btn btn-danger" aria-expanded="false">
    					<span><%= studentcount%></span>
    				</button>
				</div>
			</div>
		</div>
		 -->
		
		<div class="col-md-3 well" style="height:248px">
		<div align="center" style="font-size: larger;"><u>Institute Statastics</u></div>
		<div class="input-group">
			<input
				type="text" disabled="disabled" class="form-control" placeholder="Username"
				aria-describedby="basic-addon1" value="NO OF BATCHES">
			<span class="input-group-addon" id="basic-addon1"><%= batchcount%></span>	
		</div>
		
		<div class="input-group">
			<input
				type="text" disabled="disabled" class="form-control" placeholder="Username"
				aria-describedby="basic-addon1" value="NO OF TEACHERS">
			<span class="input-group-addon" id="basic-addon1"><%= teachercount%></span> 	
		</div>
		
		<div class="input-group">
			<input
				type="text" disabled="disabled" class="form-control" placeholder="Username"
				aria-describedby="basic-addon1" value="NO OF STUDENTS">
				<span class="input-group-addon" id="basic-addon1"><%= studentcount%></span>
		</div>
		</div>
		<div class="col-md-6 well">
		<div align="center" style="font-size: larger;"><u>Storage Statastics</u></div>
		<div class="row">
		<div class="col-md-6  " >
	<div id="classownerStorageGraph">
		<canvas id="classownerStorageGraphCtx" width="150" height="150"></canvas>
	</div>
</div>

<div class="col-md-6 "  >
	<div class="panel panel-default">
		<table id="classownerStorageTable" class="table">
			<tr style="background-color:;">
				<td>Total Space</td>
				<td id="classownerStorageTotalSpace"></td>
			</tr>

			<tr style="background-color: #FDB45C;">
				<td>Exam</td>
				<td id="classownerStorageExamSpace"></td>
			</tr>

			<tr style="background-color: #46BFBD;">
				<td>Notes</td>
				<td id="classownerStorageNotesSpace"></td>
			</tr>

			<tr style="background-color: #009900;">
				<td>Space Left</td>
				<td id="classownerStorageSpaceLeftSpace"></td>
			</tr>
		</table>
	</div>
</div>
</div>
</div>

<div class="col-md-3"></div>

</div>
	</div>
	<div class="container">
	<div class="row">
	<div align="left" class="col-md-12">

		<hr>
		<%
if(notifications!=null){
	if(notifications.size()>0){
		boolean flag=false;
		%>
		<div class="alert alert-info">Notifications</div>
		<div>
	<marquee behavior="scroll" direction="up" height="100" width="350" onmouseover="this.stop();" onmouseout="this.start();">
			<ul>
				<%
			for(int j=0;j<notifications.size();j++){
		
			%>
				<li><%=monthName[notifications.get(j).getMsg_date().getMonth()]+" "+ notifications.get(j).getMsg_date().getDate()+" -> "+notifications.get(j).getBatch_name()+" -> "+notifications.get(j).getMessage() %></li>
				<Br>
				<%}%>
			</ul>
</marquee>
		</div>
		<%
		}else{
		%>
		<div class="alert alert-info">Notifications not available</div>
		<Br>
		<%
	}
}
%>

	</div>
</div>
</div>

<script>
		var remaingSpace = <c:out value="${sessionScope.user.userStatic.remainingSpace}"></c:out>;
		var notesSpace = <c:out value="${sessionScope.user.userStatic.notesSpace}"></c:out>;
		var examSpace = <c:out value="${sessionScope.user.userStatic.examSpace}"></c:out>;
		var totalSpace = <c:out value="${sessionScope.user.userStatic.totalStorage}"></c:out>;
		var doughnutData = [
				{
					value: remaingSpace.toFixed(2),
					color: "#009900",
					highlight: "#00AA00"
				},
				{
					value: notesSpace.toFixed(2),
					color: "#46BFBD",
					highlight: "#5AD3D1"
				},
				{
					value: examSpace.toFixed(2),
					color: "#FDB45C",
					highlight: "#FFC870"
				}
			];

			window.onload = function(){
				var ctx = document.getElementById("classownerStorageGraphCtx").getContext("2d");
				window.myDoughnut = new Chart(ctx).Doughnut(doughnutData);
			};


		$("td#classownerStorageTotalSpace").text(totalSpace.toFixed(2));
		$("td#classownerStorageExamSpace").text(examSpace.toFixed(2))
		$("td#classownerStorageNotesSpace").text(notesSpace.toFixed(2));
		$("td#classownerStorageSpaceLeftSpace").text(remaingSpace.toFixed(2));
		
		
	</script>