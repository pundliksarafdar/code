<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page import="com.classapp.db.notificationpkg.Notification"%>
<%@page import="java.util.List"%>
<html>
<head>
<!-- <link href="../assets/css/style.css" rel="stylesheet" /> -->
<style>
.dashboard .panel-sticker .panel-heading{
	background:#428bca;
}

.dashboard .panel-sticker .panel-heading table h4,h5{
	margin:0px;
}

.dashboard .panel-sticker .panel-body{
	padding:5px;
}

.dashboard .panel-sticker .panel-body i.blue{
	color:#428bca;
}

.dashboard .panel-sticker .panel-heading .glyphicon{
	font-size: 35px;
}
.list-group-item.studentNotification,.btn.btn-default.studentNotification{
	background:beige;
}

.dashboard #notificationPanel{
	
}

.list-group-item.techerNotification,.btn.btn-default.techerNotification{
	background:bisque;
}

.panel-body{
	overflow-y: auto;
}
.corex-dashboard-panel-legend{
	padding:0px;
}
.corex-dashboard-panel-legend table{
	width: 100%;
	text-align: center;
}

.corex-dashboard-panel-legend table tr{
	border-collapse: collapse;
}

.corex-dashboard-panel-legend table tr td:first-child{
	 border-left: none;
}

.corex-dashboard-panel-legend table tr td{
	 border-left: solid 1px #ddd;
	 width:25%;
	 
}

.panel-body{
	
}

::-webkit-scrollbar {
    width: 5px;
}
 
::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3); 
    
}
 
::-webkit-scrollbar-thumb {
    border-radius: 0px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); 
}


</style>
<script>
$(document).ready(function(){
	
		/*
		var remaingSpace = <c:out value="${requestScope.instituteStats}"  default="0"></c:out>;
		var notesSpace = <c:out value="${requestScope.instituteStats}"  default="0"></c:out>;
		var examSpace = <c:out value="${requestScope.instituteStats}"  default="0"></c:out>;
		var totalSpace = <c:out value="${requestScope.instituteStats}"  default="0"></c:out>;
		*/
		
		var remaingSpace = <c:out value="${sessionScope.user.userStatic.remainingSpace}"  default="0"></c:out>;
		var notesSpace = <c:out value="${sessionScope.user.userStatic.notesSpace}"  default="0"></c:out>;
		var examSpace = <c:out value="${sessionScope.user.userStatic.examSpace}"  default="0"></c:out>;
		var totalSpace = <c:out value="${sessionScope.user.userStatic.totalStorage}"  default="0"></c:out>;
		
		var avialableId = <c:out value="${requestScope.instituteStats.avail_ids}" default="0"></c:out>
		var usedId = <c:out value="${requestScope.instituteStats.used_ids}" default="0"></c:out>
		var allocatedId = <c:out value="${requestScope.instituteStats.alloc_ids}" default="0"></c:out>
		var emailAccess = <c:out value="${requestScope.instituteStats.emailAccess}" default="0"></c:out>
		var smsAccess =  <c:out value="${requestScope.instituteStats.smsAccess}" default="0"></c:out>
		var smsLeft =  <c:out value="${requestScope.instituteStats.smsLeft}" default="0"></c:out>	
			
				Morris.Donut({
					  element: 'classownerStorageGraphCtx',
					  data: [
					    {label: "Remaining space", value: remaingSpace.toFixed(0)},
					    {label: "Notes space", value: notesSpace.toFixed(0)},
					    {label: "Exam space", value: examSpace.toFixed(0)}
					  ],
					  colors: [
						'#669900',
						'#3366FF',
						'#B2B2B2'
					  ],
					  formatter: function (x) { return x + "MB"}
					});
					
					Morris.Donut({
					  element: 'studentStatisticGraphCtx',
					  data: [
					    {label: "Remaining ids", value: avialableId},
					    {label: "Used ids", value: usedId},
					  ],
					  colors: [
						'#669900',
						'#3366FF'
					  ]
					});
					
		$("td#classownerStorageTotalSpace").text(totalSpace.toFixed(0)).css("background","#FFD400");
		$("td#classownerStorageExamSpace").text(examSpace.toFixed(0)).css("background","#B2B2B2");
		$("td#classownerStorageNotesSpace").text(notesSpace.toFixed(0)).css("background","#3366FF");
		$("td#classownerStorageSpaceLeftSpace").text(remaingSpace.toFixed(0)).css("background","#669900");
		$("td#userIdTotal").text(allocatedId).css("background","#FFD400");
		$("td#userIdUsed").text(usedId).css("background","#3366FF");
		$("td#userIdLeft").text(avialableId).css("background","#669900");
		if(emailAccess == true){
		$("#emailAccess").html("Yes");
		}else{
		$("#emailAccess").html("No");	
		}
		if(smsAccess == true){
		$("#smsAccess").html("Yes");	
		}else{
		$("#smsAccess").html("No");
		}
		$("#smsLeft").html(smsLeft);
	
});
</script>
</head>
	<%
int batchcount=(Integer)session.getAttribute("batchcount"); 
int studentcount=(Integer)session.getAttribute("studentcount");
int teachercount=(Integer)session.getAttribute("teachercount");
List<Notification> notifications =(List<Notification>)session.getAttribute("notifications");
String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December" };
%>
<div class="container dashboard">
	<div class="row" style="width:100%;">
		<div class="col-md-6">
			<div class="row" style="width:100%;">
			<c:choose>
				<c:when test="${empty requestScope.instituteStats}">
					<div class="alert alert-danger"><i class="glyphicon glyphicon-warning-sign"></i>&nbsp;&nbsp;Error! Error occurred</div>	
				</c:when>
				<c:otherwise>
					<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-body">
							<div id="classownerStorageGraphCtx" style="min-width:200px;min-height:200px;"></div>
						</div>
						<div class="panel-footer corex-dashboard-panel-legend">
							<table>
								<tr style="border-bottom:solid 1px #ddd;">
									<td colspan="4"> <h4>Storage</h4> </td>
								</tr>
								<tr>
									<td>Total</td><td>Remaining</td><td>Notes</td><td>Exam</td>
								</tr>
								<tr>
									<td id="classownerStorageTotalSpace"></td>
									<td id="classownerStorageSpaceLeftSpace"></td>
									<td id="classownerStorageNotesSpace"></td>
									<td  id="classownerStorageExamSpace"></td>
								</tr>
							</table>
						</div>	
					</div>	
				</div>
					<div class="col-md-6">
			<div class="panel panel-default">
				<div class="panel-body">
					<div id="studentStatisticGraphCtx" style="min-width:200px;min-height:200px;"></div>
				</div>
				<div class="panel-footer corex-dashboard-panel-legend">
						<table>
							<tr style="border-bottom:solid 1px #ddd;">
								<td colspan="4"> <h4>Student</h4> </td>
							</tr>
							<tr>
								<td>Total</td><td>Remaining</td><td>Used</td>
							</tr>
							<tr>
								<td id="userIdTotal"></td>
								<td id="userIdLeft"></td>
								<td id="userIdUsed"></td>
							</tr>
						</table>
					</div>
			</div>	
		</div>
				</c:otherwise>
			</c:choose>
			</div>
			<div class="row" style="width:100%;">
				<div class="col-md-6">
					<div class="panel panel-default panel-sticker">
					  <div class="panel-heading">
						<table width="100%">
							<tr>
								<td><h4><%=teachercount %></h4></td>
								<td rowspan="2" align="right">
									<i class="glyphicon glyphicon-user"></i>
								</td>
							</tr>
							<tr>
								<td><h5>Teacher</h5></td>
							</tr>
						</table>
					  </div>
					  <div class="panel-body">
						<a href="/manageteacher">View detail</a>
						<i class="glyphicon glyphicon-circle-arrow-right pull-right blue"></i>
					  </div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="panel panel-default panel-sticker">
					  <div class="panel-heading">
						<table width="100%">
							<tr>
								<td><h4><%=batchcount %></h4></td>
								<td rowspan="2" align="right">
									<i class="glyphicon glyphicon-user"></i>
								</td>
							</tr>
							<tr>
								<td><h5>Batch</h5></td>
							</tr>
						</table>
					  </div>
					  <div class="panel-body">
						<a href="/managebatch">View detail</a>
						<i class="glyphicon glyphicon-circle-arrow-right pull-right blue"></i>
					  </div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="col-md-6">
		<div class="row" style="width:100%;">
		<div class="col-md-12">
			<div class="panel panel-default" id="notificationPanel">
				<div class="panel-heading">
				Notification
				<span class="pull-right">
					<a href="#" class="btn btn-default btn-xs techerNotification">Teacher</a>
					<a href="#" class="btn btn-default btn-xs studentNotification">Student</a>
				</span>
				</div>	
				<div class="panel-body">
					<%
					if(notifications!=null){
						if(notifications.size()>0){
							boolean flag=false;
							%>
							
							<ul class="list-group">
									<%
								for(int j=0;j<notifications.size();j++){
							
								%>
									
											<span>
												<%if(notifications.get(j).getBatch_name().equalsIgnoreCase("Teachers")) {%>
												<li class="list-group-item techerNotification">
												<%}else{ %>
												<li class="list-group-item studentNotification">
												<%} %>
												<%= notifications.get(j).getMessage() %>
												<%= notifications.get(j).getBatch_name() %>
											</span>
											<span class="badge pull-right">
												<%= monthName[notifications.get(j).getMsg_date().getMonth()]%>
												<%= notifications.get(j).getMsg_date().getDate() %>
											</span>
											
											
									</li>
									
									<%}%>
								</ul>
							<%
							}else{
							%>
							<div class="alert alert-info">Notifications not available</div>
							<%
						}
					}
					%>
				</div>
		</div>
		</div>
		</div>
		<div class="row" style="width:100%;">
		<div class="col-md-4">
		<div class="panel panel-default">
				<div class="panel-heading" align="center">Email Access</div>
				<div class="panel-body"><div id="emailAccess" align="center"></div></div>
		</div>
				
		</div>
		<div class="col-md-4">
		<div class="panel panel-default">
				<div class="panel-heading" align="center">SMS Access</div>
				<div class="panel-body"><div id="smsAccess" align="center"></div></div>
		</div>
		</div>
		<div class="col-md-4" >
		<div class="panel panel-default">
				<div class="panel-heading" align="center">SMS Left</div>
				<div class="panel-body"><div id="smsLeft" align="center"></div></div>
		</div>
		</div>
		</div>
	</div>	
	
	</div>
</div>	
</html>