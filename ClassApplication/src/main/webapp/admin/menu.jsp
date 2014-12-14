<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="cx" uri="../WEB-INF/customtaglib/switch.tld"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.signon.User"%>
<%
	UserBean userBean = (UserBean)session.getAttribute("user"); 	
%>
<nav class="navbar navbar-default" role="navigation">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="navbar-brand" href="login">CoreX</a>
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
	            <li><a href="updatetimetable">Update Time Table</a></li>
	            <li><a href="showtimetable">See Time Table</a></li>
	         </ul>
      </li>
    <%}%>
    <cx:versionswitch switchId="3">
    	<li><a href="notes">Notes</a></li>
    </cx:versionswitch>
    </ul>
    <ul class="nav navbar-nav navbar-right">
      <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=userBean.getFirstname() %> <b class="caret"></b></a>
        <ul class="dropdown-menu">
          <li><a href="edit">Edit</a></li>
          <li><a href="logout">Logout</a></li>
        </ul>
      </li>
    </ul>
  </div><!-- /.navbar-collapse -->
</nav>


