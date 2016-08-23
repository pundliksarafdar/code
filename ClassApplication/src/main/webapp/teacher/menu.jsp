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
	 
	 #tileMenuModal img{
	 	height:67px;
	 	width:67px;
	 	padding: 5px;
	 }
	#tileMenuModal .markGlyphicon{
		height:67px;
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
   <a class="navbar-brand" href="login" style="padding: 5px 15px;">
	<img src="images/logo.gif" alt="cxlogo" style="height: 40px;" class="img-rounded"/>
	Classfloor</a>
  </div>

  <!-- Collect the nav links, forms, and other content for toggling -->
  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    <ul class="nav navbar-nav">
	<li><a href="#" id="menu_dashboard"><i class="glyphicon glyphicon-list"></i> Dashboard</a></li>
    </ul>
	<ul class="nav navbar-nav navbar-right">
      <li>
		<form class="navbar-form navbar-left" role="search">
			<div class="form-group">
			  <input type="text" class="form-control" ng-model="searchSiteMap.searchString" placeholder="Search">
			</div>
      	</form>	
	  </li>
	 
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
	<div class="col-sm-2 col-xs-4">
            <a href="addteacherquestion">
            <div id="tile2" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                               <img src="/images/Question.svg">
                            </div>
                            <div class="icontext">
                                Question
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
		<div class="col-sm-2 col-xs-4">
            <a href="createTeacherExamPatten">
            <div id="tile3" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Exam.svg">
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
            <a href="teacherExamMarks">
            <div id="tile4" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div class="markGlyphicon">
                                <span class="glyphicon glyphicon-signal bigicon "></span>
                            </div>
                            <div class="icontext">
                                Marks
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
		<div class="col-sm-2 col-xs-4">
		 <a href="teacherAttendance">
            <div id="tile5" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                               <img src="/images/Attendance.svg">
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
		 <a href="addteachernotesoption">
            <div id="tile6" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Notes.svg">
                            </div>
                            <div class="icontext">
                                Notes
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
		<div class="col-sm-2 col-xs-4">
		<a href="teachertimetable">
            <div id="tile7" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Timetable.svg">
                            </div>
                            <div class="icontext">
                                Timetable
                            </div>
                            
                        </div>
                    </div>
            </div>
        </a>    
        </div>
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
        font-size: 45px;
        color: white;
        margin-top: 20px;
        margin-bottom: 10px;
    }

    .icontext {
        color: white;
        /* font-size: 16px; */
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
        background: #FF7D28;
    }

    #tile2 {
        background: #87CEFA;
    }

    #tile3 {
        background: #DC2878;
    }

    #tile4 {
        background: #2cc822;
    }

    #tile5 {
        background: #FFD602;
    }

    #tile6 {
        background: #ff0a00;
    }

    #tile7 {
        background: #EACF46;
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

