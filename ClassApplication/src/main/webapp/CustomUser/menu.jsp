<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="cx" uri="../WEB-INF/customtaglib/switch.tld"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.signon.User"%>
<%
	UserBean userBean = (UserBean)session.getAttribute("user"); 	
    String[] parent_mod_access = (String[])session.getAttribute("parent_mod_access");
    String[] child_mod_access = (String[])session.getAttribute("child_mod_access");
%>

<div ng-app = "siteMapApp" ng-cloak>
<script>
var siteMapData = <%=session.getAttribute("sitemapdata")%>

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
	  <% if(userBean.getRole() == 1 ){ %>
	  <li>
	  <form class="navbar-form navbar-left">
			<div class="form-group">
	  			<a href="#" id="notificationBtn" data-toggle="popover" data-placement="bottom" class="btn btn-default className /*notificationBlinker*/" style="background: transparent;"><%=userBean.getClassName()%></a>
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
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="glyphicon glyphicon-remove-circle" style="color: red"></i></button>	
		</div>
	</div>
	
	<div class="row">
	<% if(ArrayUtils.contains(parent_mod_access,"1") || ArrayUtils.contains(parent_mod_access,"2") || ArrayUtils.contains(parent_mod_access,"3")){ %>
        <div class="col-sm-4 col-xs-8">
        	<%if(ArrayUtils.contains(parent_mod_access,"1")){%>
            <a href="customeUserManageclass">
            <%}else if(ArrayUtils.contains(parent_mod_access,"2")){ %>
             <a href="customUserManagesubject">
            <%}else if(ArrayUtils.contains(parent_mod_access,"3")){ %>
             <a href="customUserManageBatch">
            <%} %>
			<div id="tile2" class="tile">
			      <div class="carousel slide" data-ride="carousel">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/class.svg">
                            </div>
                            <div class="icontext">
                             <% int i = 0; if(ArrayUtils.contains(parent_mod_access,"1")){ %>Classes <%  i++;} %>
                                 <% if(ArrayUtils.contains(parent_mod_access,"2")){ %><% if(i>0){ %> , <%} %> Subject <%i++;} %>
                                  <% if(ArrayUtils.contains(parent_mod_access,"3")){ %><% if(i>0){ %> , <%} %> Batch <%} %>
                            </div>
                            
                        </div>
						
                    </div>
					</div>
            </div>
			</a>
        </div>
        <%} %>
        <% if(ArrayUtils.contains(parent_mod_access,"4")){ %>
        <div class="col-sm-2 col-xs-4">
        	<a href="customUserViewStudent">
            <div id="tile3" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Student.svg">
                            </div>
                            <div class="icontext">
                                Student
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
        <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"5")){ %>
		<div class="col-sm-2 col-xs-4">
			<a href="customUserViewTeacher">
            <div id="tile4" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Teacher.svg">
                            </div>
                            <div class="icontext">
                                Teacher
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
        <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"6")){ %>
        <div class="col-sm-2 col-xs-4">
            <a href="customeUserSeeNotes">
            <div id="tile5" class="tile">
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
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"9")){ %>
		<div class="col-sm-2 col-xs-4">
            <a href="customeUserViewnEditPattern">
            <div id="tile6" class="tile">
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
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"10")){ %>
        <div class="col-sm-2 col-xs-4">
            <a href="customeUserViewexamMarks">
            <div id="tile7" class="tile">
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
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"11")){ %>
		<div class="col-sm-2 col-xs-4">
		 <a href="customeUserViewAttendance">
            <div id="tile8" class="tile">
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
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"12")){ %>
		<div class="col-sm-2 col-xs-4">
            <a href="customeUserFees">
            <div id="tile9" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Fees.svg">
                            </div>
                            <div class="icontext">
                                Fees
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"13")){ %>
		<div class="col-sm-2 col-xs-4">
		<a href="customeUserAlertAction">
            <div id="tile10" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                            	<img src="/images/Alert.svg">
                            </div>
                            <div class="icontext">
                                Alert
                            </div>
                            
                        </div>
                    </div>
            </div>
        </a>
        </div>
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"8")){ %>
		<div class="col-sm-2 col-xs-4">
		<a href="customeUserTimeTableAll">
            <div id="tile11" class="tile">
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
         <%} %>
		<% if(ArrayUtils.contains(parent_mod_access,"15")){ %>
        <div class="col-sm-2 col-xs-4">
        	<a href="customeUserClassOwnerSettings">
        	<!-- This two settings need to merge -->
        	<!-- <a href="classownerSettings"> -->
            <div id="tile12" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/Setting.svg">
                            </div>
                            <div class="icontext">
                                Settings
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"7")){ %>
        <div class="col-sm-2 col-xs-4">
        	<a href="customeUserSearchQuestion">
        	<!-- This two settings need to merge -->
        	<!-- <a href="classownerSettings"> -->
            <div id="tile13" class="tile">
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
         <%} %>
         <% if(ArrayUtils.contains(parent_mod_access,"14")){ %>
		<div class="col-sm-2 col-xs-4">
			 <% if(ArrayUtils.contains(child_mod_access,"51")){ %>
        	<a href="customeUserPrintStudentCertificate">
        	 <%}else{ %>
        	 <a href="customeUserViewCertificate">
        	 <%} %>
        	<!-- This two settings need to merge -->
        	<!-- <a href="classownerSettings"> -->
            <div id="tile13" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <img src="/images/certificate.svg">
                            </div>
                            <div class="icontext">
                                Certificate
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
         <%} %>
        <%--  <% if(ArrayUtils.contains(parent_mod_access,"5")){ %>
        <div class="col-sm-2 col-xs-4">
        	<a href="createrole">
        	<!-- This two settings need to merge -->
        	<!-- <a href="classownerSettings"> -->
            <div id="tile13" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div class="markGlyphicon">
                                <i class="glyphicon glyphicon-user bigicon"></i>
                            </div>
                            <div class="icontext">
                                Role
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div> --%>
		<!---->
        </div>
    
    <% if(userBean.getRole() == 0 ){ %>
	<div class="row">
        <div class="col-sm-2 col-xs-4">
            <a href="manageClassFeature">
            <div id="tile7" class="tile">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Class
                            </div>
                            
                        </div>
                    </div>
            </div>
            </a>
        </div>
       </div>
    <%} %>
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
        background: #fe6700;
    }

    #tile10 {
        background: #8B008B;
    }
    
    #tile11 {
    	background: #00FF7F;	
    }
    
    #tile12{
    	background: #191970;
    }
    #tile13 {
    	background: #A0522D;
    }
	
	.modal {
		-webkit-transition: all 0.5s ease;
		transition: all 0.5s ease;
	}
	
	.tile:hover { 
    box-shadow: 10px 10px 5px #383838;
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
	
	$modal.on("keyDown",function(){
		$modal.modal("hide");
	});
	
	$("#tileMenuModal").hover(function() {
	    this.focus();
	    console.log("here");
	}, function() {
	    this.blur();
	    console.log("asdsadasd");
	}).keydown(function(e) {
	    console.log(e.keyCode);
	});
});
</script>


<!-- Metro Tiles - END -->
</div>

