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
</style>

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
    <%if(userBean.getRole() == 0 || userBean.getRole() == 10) {%>
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Admin <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="login">Admin</a></li>
	            <li><a href="javascript:void(0)" data-toggle="modal" data-target="#ajax-modal">Class List</a></li>
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
      			<li><a href="addsubject">Manage Subject</a></li>
      			<li><a href="manageclass">Manage Class</a></li>
	            <li><a href="managestudent">Manage Student</a></li>
	            <li><a href="manageteacher">Manage Teacher</a></li>
	            <li><a href="managebatch">Manage Batch</a></li>
	         </ul>
      </li>
      
      <cx:versionswitch switchId="3">
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Exam <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="manageexams">Add Exam</a></li>
	            <li><a href="searchexamnonstudent">Attempt Exam</a></li>
	         </ul>
      </li>
      </cx:versionswitch>
      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Time Table <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="createtimetable">Create Time Table</a></li>
	            <li><a href="updatetimetable">View & Update Time Table</a></li>
	            <!-- <li><a href="showtimetable">See Time Table</a></li>  -->
	         </ul>
      </li>
    <%}%>
    <cx:versionswitch switchId="3">
    	<li><a href="notes">Notes</a></li>
    </cx:versionswitch>
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
	  			<a href="#" class="btn btn-default className" style="background: transparent;"><%=userBean.getClassName()%></a>
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
          <li><a href="edit">Edit</a></li>
          <li><a href="logout">Logout</a></li>
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
</div>


