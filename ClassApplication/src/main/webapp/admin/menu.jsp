<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.signon.User"%>
<%
	UserBean userBean = (UserBean)session.getAttribute("user"); 	
%>
<nav class="navbar navbar-default" role="navigation" style="margin-top: -20px; margin-bottom: -40px">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="navbar-brand" href="#">CoreX</a>
  </div>

  <!-- Collect the nav links, forms, and other content for toggling -->
  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    <ul class="nav navbar-nav">
    <%if(userBean.getRole() == 0) {%>
      <li class="active"><a href="admin">Admin</a></li>
    <%}if(userBean.getRole() < 1){ %>  
      <li><a href="#">Class Owner</a></li>
    <%}if(userBean.getRole() < 2){ %>  
      <li><a href="#">Class Teacher</a></li>  
    <%}if(userBean.getRole() == 3 ||userBean.getRole() == 0){ %>  
      <li><a href="#">Students</a></li>
    <%}%>      
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