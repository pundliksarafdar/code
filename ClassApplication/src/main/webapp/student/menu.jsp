<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="cx" uri="../WEB-INF/customtaglib/switch.tld"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.signon.User"%>
<%
	UserBean userBean = (UserBean)session.getAttribute("user"); 	
%>
<script>
	$(document).ready(function(){
		var $modal= $('#tileMenuModal');
		$('#menu_dashboard').on('click', function (e) {
			$modal.modal();
		});	
	});
	
</script>
<style>
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
<nav class="navbar navbar-apple-custom" role="navigation">
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
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Class Owner <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="login">Class Owner</a></li>
	            <li><a href="addsubject">Add Subject</a></li>
	            <li><a href="timetable">Time Table</a></li>
 				<li><a href="managestudent">Manage Student</a></li>
	            <li><a href="manageteacher">Manage Teacher</a></li>
	            <li><a href="managebatch">Manage Batch</a></li>
          	</ul>
      </li>
    <%}if(userBean.getRole() == 2 || userBean.getRole() == 10){ %>  
      <li><a href="#">Class Teacher</a></li>  
    <%}if(userBean.getRole() == 3 || userBean.getRole() == 10){ %>  
		<li><a href="#">Students</a></li>
		<li><a href="#" id="menu_dashboard"><i class="glyphicon glyphicon-list"></i> Dashboard</a></li>
    <%}%>      
     <%-- <cx:versionswitch switchId="3"> --%>
    <li><a href="studentcommoncomponent?forwardAction=studentnotes">Notes</a></li>
    <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Exam<b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="studentcommoncomponent?forwardAction=attemptexamlist">Attempt Exam</a></li>
	            <li><a href="studentcommoncomponent?forwardAction=examMarks">Exam Marks</a></li>
          	</ul>
      </li>
    <%-- </cx:versionswitch> --%>
    </ul>
    <ul class="nav navbar-nav navbar-right">
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
</nav>

<div class="modal fade" id="tileMenuModal">
  
<div class="container dynamicTile">
    <div class="row">
		<div class="col-xs-12">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>	
		</div>
	</div>
	<div class="row">
        <div class="col-sm-2 col-xs-2">
            <a href="studenttimetable">
			<div id="tile8" class="tile">
			      <div class="carousel slide" data-ride="carousel">
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
            </div>
			</a>
        </div>
        
        <div class="col-sm-2 col-xs-2">
            <a href="viewStudentsNotes">
			<div id="tile8" class="tile">
			      <div class="carousel slide" data-ride="carousel">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div>
                                <span class="fa fa-spinner bigicon"></span>
                            </div>
                            <div class="icontext">
                                Notes
                            </div>
                            
                        </div>
						
                    </div>
					</div>
            </div>
			</a>
        </div>
		</div>
	</div>
</div>
