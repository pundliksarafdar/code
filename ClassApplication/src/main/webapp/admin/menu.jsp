<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="cx" uri="../WEB-INF/customtaglib/switch.tld"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.signon.User"%>
<%
	UserBean userBean = (UserBean)session.getAttribute("user"); 	
%>

<div ng-app = "siteMapApp" ng-cloak>
<script>
var siteMapData = <%=session.getAttribute("sitemapdata")%>
console.log(siteMapData);

/******************Static data********************/
var MENU_NOTIFICATION_DATA = "#menuNotificationDetailsData";
var MENU_NOTIFICATION_DATA_RE_EVALUATION = MENU_NOTIFICATION_DATA+"Reevaluation";
var MENU_NOTIFICATION_DATA_RE_EVALUATION_COUNT = MENU_NOTIFICATION_DATA_RE_EVALUATION+" .badge";
var MENU_NOTIFICATION_BACK_BTN = "#menuNotificationBack";
/**************************************/
/***********Enums********************/
var NOTIFICATION_KEY = {REEVALUATION:"REEVALUATION"};
/**************************************/

/****Require vaiable********************/
var ease = 200;
/************************************/
function chunk(arr, size) {
  var newArr = [];
  for (var i=0; i<arr.length; i+=size) {
    newArr.push(arr.slice(i, i+size));
  }
  return newArr;
}

var siteMapApp = angular.module("siteMapApp",[]);
siteMapApp.controller("SiteMapController",function($scope){
	$scope.siteMapDatas = siteMapData;
	$scope.SiteMap = "Site Map";
	$scope.chunkedData = chunk(siteMapData, 3);
});

/* $(document).ready(function(){
	var timer = new RenderTimer(0,30);
	timer.startTimer();
	
	var popoverTitle = "<div style='color:black;'><button class='btn btn-warning btn-xs' id='menuNotificationBack'>&nbsp;<i class='glyphicon glyphicon-arrow-left'> </i></button>&nbsp;&nbsp;<b>Notifications</b></div>";
	var option = {content:$("#menuNotificationDetails").html(),html:true,title:popoverTitle};
	$('#notificationBtn').popover(option);
	$('#notificationBtn').on("shown.bs.popover",renderNotification);
});

function renderNotification(){
		$(this).parent().find(".popover-content #menuNotificationDetailsProgress").show();
		$(this).parent().find(".popover-content #menuNotificationDetailsData").hide();
		getAllNotifications($(this),displayNotifications);
		$(this).parent().off("click").on("click",MENU_NOTIFICATION_DATA_RE_EVALUATION,renderReevaluationNotification);
}

function renderReevaluationNotification(){
		$(this).parents(".popover").find(".popover-content #menuNotificationDetailsProgress").show();
		$(this).parents(".popover").find(".popover-content #menuNotificationDetailsData").hide();
		
		getAllNotifications($(this).parents(".popover").siblings('[data-toggle="popover"]'),displayReevaluationNotification);
		
		$(MENU_NOTIFICATION_BACK_BTN).show(10,function(){
			$(this).on("click",function(){
				$('#notificationBtn').parent().find(".popover-content #menuNotificationDetailsData").hide(ease,function(){
						$('#notificationBtn').parent().find(".popover-content #menuNotificationDetailsProgress").show(ease,function(){
							getAllNotifications($('#notificationBtn'),displayNotifications);
						});
				});
				
			});
		});
}

function getAllNotifications(targetElm,successCallback){
	
	$.ajax({
			url: "classOwnerServlet",
			global:false,
			data: {
				 methodToCall: "getgeneralnotification"
				},
				success:function(data){successCallback(data,targetElm)},
				error:function(error){
					
				}
		});
}

function displayNotifications(data,targetElm){
	$(MENU_NOTIFICATION_BACK_BTN).hide(10,function(){
			$(this).off("click");
	});
	targetElm.parent().find(".popover-content #menuNotificationDetailsData").empty();
	targetElm.parent().find(".popover-content #menuNotificationDetailsData").html($("#menuNotificationDetails").html());
	var displayData = JSON.parse(JSON.parse(data).notifications);
	$.each(displayData,function(index,val){
		if(val.keys == NOTIFICATION_KEY.REEVALUATION){
			$(MENU_NOTIFICATION_DATA_RE_EVALUATION_COUNT).html(val.notificationCount);
		}
	});
	
	$(targetElm).parent().find(".popover-content #menuNotificationDetailsProgress").hide(ease,function(){
			$(targetElm).parent().find(".popover-content #menuNotificationDetailsData").show(ease);
	});
	
	
}

function displayReevaluationNotification(data,targetElm){
	var dataJson = JSON.parse(data);
	var notificationDatas = JSON.parse(dataJson.notifications);
	targetElm.parent().find(".popover-content #menuNotificationDetailsData").empty();
	
	var ulItem = $("<ul>",{
			class:"nav nav-pills nav-stacked"
		});
		
	for(var index in notificationDatas){
		var title = notificationDatas[index].notificationTitle;
		var keys = notificationDatas[index].keys;
		var liItem = $("<li/>");
		
		var aItem = $("<a/>",{
			class:"btn btn-success btn-xs",
			text:title
		});
		
		var statusBtn = $("<a/>",{
			type:"button",
			class:"btn btn-danger btn-xs pull-right",
			html:"&nbsp;<i class='glyphicon glyphicon-unchecked'>"
		});
		
		aItem.append(statusBtn);
		liItem.append(aItem);
		ulItem.append(liItem);
	}
	targetElm.parent().find(".popover-content #menuNotificationDetailsData").html(ulItem);
	
	targetElm.parent().find(".popover-content #menuNotificationDetailsProgress").hide(ease,function(){
		targetElm.parent().find(".popover-content #menuNotificationDetailsData").show(ease);
	});
}

function RenderTimer(completedTime,totalTime){
	var ctx = document.getElementById("myChart").getContext("2d");
	var option = {
		segmentShowStroke : false,
		animationSteps :1,
		showTooltips:false
	};		
	
	this.startTimer = function(){
		var timeInterval = setInterval(function(){
			var totalSec = totalTime - completedTime;
			var hours = parseInt( totalSec / 3600 ) % 24;
			var minutes = parseInt( totalSec / 60 ) % 60;
			var seconds = parseInt(totalSec % 60);
			var result = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds  < 10 ? "0" + seconds : seconds);
			$("#timerRemainingTime").html(result);
			
			var complete = {value: completedTime,color:"white"};
			var incomplete = {value: (totalTime-completedTime),color:"red"}				
			var data = [complete,incomplete];
			var completed = data[0];
			var incompleted = data[1];
			completedTime +=1;
			completed.value = completedTime;
			incompleted.value = totalTime-completedTime;
			var completedPer = completedTime/totalTime*100;
			if(completedPer <50){
				incompleted.color = "white";
			$("#timerRemainingTime").css({"color":"white"});
			}else if(completedPer <90){
				incompleted.color = "yellow";
				$("#timerRemainingTime").css({"color":"yellow"});
			}else if(completedPer <100.0){
				incompleted.color = "red";
			$("#timerRemainingTime").css({"color":"red"});
			}else{
				clearInterval(timeInterval);
				$(window).trigger("timeroverflow");
			}			
			var myDoughnutChart = new Chart(ctx).Doughnut(data,option);
		},1000);
	}
}	 */
</script>
<style>
	.className:hover{
		color:white;
	}
	
	.notificationBlinker {
	    background-color: red;
	    -webkit-animation-name: blinker; /* Chrome, Safari, Opera */
	    -webkit-animation-duration: 1s; /* Chrome, Safari, Opera */
	    -webkit-animation-iteration-count: infinite; /* Chrome, Safari, Opera */
	    animation-name: blinker;
	    animation-duration: 1s;
	    animation-iteration-count: infinite;
	}
	
	/* Chrome, Safari, Opera */
	@-webkit-keyframes blinker {
		50%  {background-color:red;}
	}
	
	/* Standard syntax */
	@keyframes blinker {
	    50%  {background-color:red;}
	}
		
	.popover-content {
	  color:black;
	  max-height:300px;
	  width:200px;
	  overflow-y: auto;
	  overflow-x: hidden;
	 }
		
</style>
	<div id="menuNotificationDetails" style="display:none">
		<div id="menuNotificationDetailsData">
			<ul class="nav nav-pills nav-stacked">
			  <li id="menuNotificationDetailsDataReevaluation">
				<a href="#" class="btn btn-success btn-xs">
				  <span class="badge pull-right">5</span>
				  Re-evaluation
				</a>
			  </li>
			  <li>
				<a href="#" class="btn btn-success btn-xs">
				  <span class="badge pull-right">5</span>
				  General notification
				</a>
			  </li>
			</ul>
		</div>
		<div id="menuNotificationDetailsProgress">
			<div class="corex-loader">Please wait</div>
		</div>
	</div>	
	<div id="id0" style="display:none">
		<div>
		<table class="table">
		<tr id="id0">
			<td>Messages 1</td>
			<td><button id="examList0" class="btn btn-xs btn-danger pull-right" type="button">&nbsp;<i class="glyphicon glyphicon-unchecked"></i></button></td>
		</tr>
		<tr id="id1">
			<td>Messages 2</td>
			<td><button id="examList0" class="btn btn-xs btn-warning pull-right" type="button">&nbsp;<i class="glyphicon glyphicon-edit"></i></button></td>
		</tr>
		<tr><td></td><td></td></tr>
		</table>
		</div>
	</div>
	
	<div id="id1" style="display:none">
	
	</div>
<nav class="navbar navbar-apple-custom" role="navigation" ng-controller="SiteMapController">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" style="border-color: white;border-width: 1px;">
      <i class="glyphicon glyphicon-th-large"></i>
    </button>
    <a class="navbar-brand" href="login">
	<img src="images/cxlogo.jpg" alt="cxlogo" style="height: 20px;" class="img-rounded"/>
	CoreX</a>
  </div>

  <!-- Collect the nav links, forms, and other content for toggling -->
  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    <ul class="nav navbar-nav">
	<li><a href="#" id="menu_dashboard"><i class="glyphicon glyphicon-list"></i> Dashboard</a></li>
    <%if(userBean.getRole() == 0 || userBean.getRole() == 10) {%>
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Admin <b class="caret"></b></a>
      		<ul class="dropdown-menu">	
	            <li><a class="menuoptions" href="login">Admin</a></li>
	            <li><a class="menuoptions" href="getallpaths">URL Access</a></li>
	            <li><a class="menuoptions" href="uploadadd">Upload Advertise</a></li>
	            <li><a class="menuoptions" href="instituteupgrade">Institute Upgrade</a></li>
	            <li><a class="menuoptions" href="javascript:void(0)" data-toggle="modal" data-target="#ajax-modal">Class List</a></li>
          	</ul>
      </li>
    <%}if(userBean.getRole() < 2 || userBean.getRole() == 10){ %>  
      <!-- <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Class Owner <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="addsubject">Add Subject</a></li>
	         </ul>
      </li> -->
      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Manage <b class="caret"></b></a>
      		<ul class="dropdown-menu">
      			<li><a class="menuoptions" href="addsubject">Manage Subject</a></li>
      			<li><a class="menuoptions" href="manageclass">Manage Class</a></li>
	            <li><a class="menuoptions" href="managestudent">Manage Student</a></li>
	            <li><a class="menuoptions" href="manageteacher">Manage Teacher</a></li>
	            <li><a class="menuoptions" href="managebatch">Manage Batch</a></li>
	         </ul>
      </li>
      
      <cx:versionswitch switchId="3">
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Questions <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a class="menuoptions" href="addquestion">Add Questions</a></li>
	            <li><a class="menuoptions" href="searchQuestion">Search/Edit Questions</a></li>
	         </ul>
      </li>
      </cx:versionswitch>
      <cx:versionswitch switchId="3">
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Exam <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a class="menuoptions" href="choosesubject?forwardAction=generateexampreaction">Auto Generate Exam</a></li>
	            <li><a class="menuoptions" href="choosesubject?forwardAction=manualexam">Add Manual Exam</a></li>
	            <li><a class="menuoptions" href="/choosesubject?forwardAction=listExam&batchDefault=true">Search Exam</a></li>
	            <li><a class="menuoptions" href="choosesubject?forwardAction=attemptexamlist">Attempt Exam</a></li>
	            <li><a class="menuoptions" href="choosesubject?forwardAction=studentexammarks">Exam marks</a></li>
	            <li><a class="menuoptions" href="createExamPatten">Exam</a></li>
	         </ul>
      </li>
      </cx:versionswitch>
      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Time Table <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a class="menuoptions" href="createtimetable">Create Time Table</a></li>
	            <li><a class="menuoptions" href="updatetimetable">View & Update Time Table</a></li>
	             <li><a class="menuoptions" href="weeklytimetable">Weekly Time Table</a></li>
	            <!-- <li><a href="showtimetable">See Time Table</a></li>  -->
	         </ul>
      </li>
    <%}%>
<%--     <cx:versionswitch switchId="3"> --%>
    	<li><a href="#" class="dropdown-toggle" data-toggle="dropdown">Notes <b class="caret"></b></a>
    	<ul class="dropdown-menu">
	            <li><a class="menuoptions" href="choosesubject?forwardAction=addnotesoption">Add Notes</a></li>
	            <li><a class="menuoptions" href="/choosesubject?forwardAction=seenotes&batchDefault=true">See/Update All Notes</a></li>  
	         </ul>
    	
    	</li>
    <%-- </cx:versionswitch> --%>
    <li><a href="#" class="dropdown-toggle" data-toggle="dropdown">Send Notice/Message<b class="caret"></b></a>
    		<ul class="dropdown-menu">
	            <li><a class="menuoptions" href="sendmessage?to=student">Student</a></li>
	            <li><a class="menuoptions" href="sendmessage?to=teacher">Teacher</a></li>  
	         </ul>
    </li>
    </ul>
	<ul class="nav navbar-nav navbar-right">
      <li>
		<form class="navbar-form navbar-left" role="search">
			<div class="form-group">
			  <input type="text" class="form-control" ng-model="searchSiteMap.searchString" placeholder="Search">
			</div>
      	</form>	
	  </li>
	  <% if(userBean.getRole() == 1 ){ %>
	  <li>
	  <form class="navbar-form navbar-left">
			<div class="form-group">
	  			<a href="#" id="notificationBtn" data-toggle="popover" data-placement="bottom" class="btn btn-default className notificationBlinker" style="background: transparent;"><%=userBean.getClassName()%></a>
	  		</div>
	  	</form>
	  	</li>	
	  <%} %>
	  <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-phone-alt"></i> <b class="caret"></b></a>
        <ul class="dropdown-menu">
    	  <li><a href="#" data-toggle="modal" data-target="#aboutUsModal">About Us</a></li>
		  <li><a href="#" data-toggle="modal" data-target="#contactUsModal" id="contactuslink">Contact Us</a></li>
	    </ul>
      </li>
      
	  <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=userBean.getFirstname() %> <b class="caret"></b></a>
        <ul class="dropdown-menu">
          <li><a href="edit" class="menuoptions">Edit</a></li>
          <li><a href="logout" class="menuoptions">Logout</a></li>
        </ul>
      </li>
    </ul>
  </div><!-- /.navbar-collapse -->
  
  <div ng-show="searchSiteMap.searchString.length">
		<h3>{{SiteMap}}</h3>
	<div class="row" ng-repeat="siteMapDataRows in chunkedData">
		<div class="col-sm-4" ng-repeat="siteMapData in siteMapDataRows | filter:searchSiteMap">
			<a href="{{siteMapData.href}}" style="color: #428bca;" title="{{siteMapData.title}}" >{{siteMapData.linkName}}</a>
		</div>
	</div>
	<div ng-hide="(chunkedData| filter:searchSiteMap).length">
			<div style="color:red;">No Result</div>
	</div>	
	</div>
	

</nav>

<!-- Metro Tiles - START -->
<div class="modal fade" id="tileMenuModal">
  
<div class="container dynamicTile">
    <div class="row">
		<div class="col-xs-12">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>	
		</div>
	</div>
	<div class="row">
        <div class="col-sm-4 col-xs-8">
            <a href="addsubject">
			<div id="tile8" class="tile">
			      <div class="carousel slide" data-ride="carousel">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Classes, subject & batch
                            </div>
                            
                        </div>
						
                    </div>
					</div>
            </div>
			</a>
        </div>
        <div class="col-sm-2 col-xs-4">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Student
                            </div>
                            
                        </div>
                    </div>
            </div>
        </div>
		<div class="col-sm-2 col-xs-4">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Teacher
                            </div>
                            
                        </div>
                    </div>
            </div>
        </div>
		<div class="col-sm-2 col-xs-4">
            <a href="manageExam">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Exam
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
		<div class="col-sm-2 col-xs-4">
		 <a href="attendances">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Attendance
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
		<div class="col-sm-2 col-xs-4">
            <a href="fees">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Fees
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
		<div class="col-sm-2 col-xs-4">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Alert
                            </div>
                            
                        </div>
                    </div>
            </div>
        </div>
		<div class="col-sm-2 col-xs-4">
		<a href="fees">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Timetable
                            </div>
                            
                        </div>
                    </div>
            </div>
        </a>    
        </div>
		<div class="col-sm-2 col-xs-4">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Parent control
                            </div>
                            
                        </div>
                    </div>
            </div>
        </div>
		<div class="col-sm-2 col-xs-4">
            <div id="tile8" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Report
                            </div>
                            
                        </div>
                    </div>
            </div>
        </div>
		<!---->
        </div>
    </div>


</div><!-- /.modal -->

<style>
    .dynamicTile .col-sm-2.col-xs-4 {
        padding: 5px;
    }
	
	.dynamicTile .col-sm-4.col-xs-8 {
        padding: 5px;
    }

    .bigicon {
        font-size: 67px;
        color: white;
        margin-top: 20px;
        margin-bottom: 10px;
    }

    .icontext {
        color: white;
        font-size: 16px;
    }

    .bigicondark {
        font-size: 67px;
        color: black;
        margin-top: 20px;
        margin-bottom: 10px;
    }

    .icontextdark {
        color: black;
        font-size: 27px;
    }

    
    #tile1 {
        background: #7C91C7;
    }

    #tile2 {
        background: #3B579D;
    }

    #tile3 {
        background: #153178;
    }

    #tile4 {
        background: #EACF46;
    }

    #tile5 {
        background: #EACF46;
    }

    #tile6 {
        background: #FFED94;
    }

    #tile7 {
        background: white;
    }

    #tile8 {
        background: #7C91C7;
    }

    #tile9 {
        background: #EACF46;
    }

    #tile10 {
        background: #EACF46;
    }
	
	.modal {
		-webkit-transition: all 0.5s ease;
		transition: all 0.5s ease;
	}

</style>
<script type="text/javascript">
$(document).ready(function () {
    $(".tile").height($("#tile1").width());
    $(".carousel").height($("#tile1").width());
    $(".item").height($("#tile1").width());

    $(window).resize(function () {
        if (this.resizeTO) clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function () {
            $(this).trigger('resizeEnd');
        }, 10);
    });

    $(window).bind('resizeEnd', function () {
        $(".tile").height($("#tile1").width());
        $(".carousel").height($("#tile1").width());
        $(".item").height($("#tile1").width());
    });
	
	var $modal= $('#tileMenuModal');
	$('#menu_dashboard').on('click', function (e) {
		//$modal.css({top: e.clientY, left: e.clientX, transform: 'scale(0.2, 0.2)'});
		$modal.modal();
		//$modal.css({top: '', left: '', transform: ''});
	});

});
</script>


<!-- Metro Tiles - END -->
</div>

