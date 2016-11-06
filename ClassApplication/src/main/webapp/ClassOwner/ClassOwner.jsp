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
		var usedSpace = <c:out value="${sessionScope.user.userStatic.usedSpace}"  default="0"></c:out>;
		/* var examSpace = <c:out value="${sessionScope.user.userStatic.examSpace}"  default="0"></c:out>; */
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
					    {label: "Used space", value: usedSpace.toFixed(0)}
					  ],
					  colors: [
						'#669900',
						'#3366FF'/* ,
						'#B2B2B2' */
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
		/* $("td#classownerStorageExamSpace").text(examSpace.toFixed(0)).css("background","#B2B2B2"); */
		$("td#classownerStorageusedSpace").text(usedSpace.toFixed(0)).css("background","#3366FF");
		$("td#classownerStorageSpaceLeftSpace").text(remaingSpace.toFixed(0)).css("background","#669900");
		$("td#userIdTotal").text(allocatedId).css("background","#FFD400");
		$("td#userIdUsed").text(usedId).css("background","#3366FF");
		$("td#userIdLeft").text(avialableId).css("background","#669900");
		/* if(emailAccess == true){
		$("#emailAccess").html("Yes");
		}else{
		$("#emailAccess").html("No");	
		} */
		if(smsAccess == true){
		$("#smsLeftPanel").show();	
		$("#smsLeft").html(smsLeft);
		}else{
		$("#smsLeftPanel").hide();
		$("#noSMSAccessPanel").show();
		}
		var allNames = getNames();
        $('#studentNameSearch').typeahead({
            minLength: 1,
            order: "asc",
            hint: true,
            offset : true,
            template: "{{display}}",
            source: allNames,
            debug: true
        });
	$("#searchStudentByName").click(function(){
		$(".error").html();
		if($("#studentNameSearch").val().trim() == "" ){
			$("#searchStudentByNameError").html("Enter Student Name")
			return false;
		}
	});
});
function getNames(){
	var names = {};
	  $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchNamesForSuggestion"						 
		   		},
		   type:"POST",
		   async : false,
		   success:function(e){
			   var data = JSON.parse(e);
			   names =  data.names;
		   },
		   error:function(e){
			   }
		   });
	return names;
}
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
									<td>Total</td><td>Remaining</td><td>Used</td><!-- <td>Exam</td> -->
								</tr>
								<tr>
									<td id="classownerStorageTotalSpace"></td>
									<td id="classownerStorageSpaceLeftSpace"></td>
									<td id="classownerStorageusedSpace"></td>
									<!-- <td  id="classownerStorageExamSpace"></td> -->
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
				<div class="col-md-4">
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
				<div class="col-md-4">
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
				<div class="col-md-4">
				<div class="panel panel-default panel-sticker" id="smsLeftPanel">
					  <div class="panel-heading">
						<table width="100%">
							<tr>
								<td><div id="smsLeft"></div></td>
								<td rowspan="2" align="right">
									<i class="glyphicon glyphicon-send"></i>
								</td>
							</tr>
							<tr>
								<td><h5>SMS Left</h5></td>
							</tr>
						</table>
					  </div>
					  <div class="panel-body">
						<a href="/classownerSettings">Configure</a>
						<i class="glyphicon glyphicon-circle-arrow-right pull-right blue"></i>
					  </div>
					</div>
					
					<div class="panel panel-default panel-sticker" style="display: none;" id="noSMSAccessPanel">
					  <div class="panel-heading">
						<table width="100%" >
							<tr>
								<td>No Sms Accees</td>
								<td rowspan="2" align="right">
									<i class="glyphicon glyphicon-send"></i>
								</td>
							</tr>
						</table>
					  </div>
					  <div class="panel-body">
						<a data-toggle="modal" data-target="#myModal">Get Access
						<i class="glyphicon glyphicon-info-sign pull-right"></i>
						</a>
					  </div>
					</div>	
				</div>
			</div>
		</div>
		
		<div class="col-md-6">
		<div class="row well" style="width:100%;">
		<div class="col-md-12">
		<form action="viewstudent">
        <div class="typeahead-container">
            <div class="typeahead-field">

            <span class="typeahead-query">
                <input id="studentNameSearch"
                       name="studentNameSearch"
                       type="search"
                       autofocus
                       autocomplete="off" Placeholder="Search Student" class="form-control" style="border-radius:4px 0 0 4px">
            </span>
            <span class="typeahead-button">
                <button type="submit" id="searchStudentByName" style="border-radius:0 4px 4px 0">
                    <span class="typeahead-search-icon" ></span>
                </button>
            </span>

            </div>
        </div>
    </form>
    <span id="searchStudentByNameError" class="error"></span>
		</div>
		</div>
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
				<h5><strong><u>Student Notice</u></strong></h5>
					<c:if test="${not empty studentNoticeList}">
					<ul>
					<c:forEach items="${studentNoticeList}" var="notice" varStatus="counter">
						<li><c:out value="${notice.notice }"></c:out></li>
					</c:forEach>
					</ul>
					</c:if>
					<c:if test="${empty studentNoticeList}">
					NA
					</c:if>
					<h5><strong><u>Staff Notice</u></strong></h5>
					<c:if test="${not empty staffNoticeList}">
					<ul>
					<c:forEach items="${staffNoticeList}" var="notice" varStatus="counter">
						<li><c:out value="${notice.notice }"></c:out></li>
					</c:forEach>
					</ul>
					</c:if>
					<c:if test="${empty staffNoticeList}">
					NA
					</c:if>
				</div>
		</div>
		</div>
		</div>
	</div>	
	
	</div>
</div>	
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">SMS</h4>
      </div>
      <div class="modal-body">
        <p>To get sms access contact 9766120685</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
</html>