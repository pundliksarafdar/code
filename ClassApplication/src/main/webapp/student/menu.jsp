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
        font-size: 45px;
        color: white;
        margin-top: 20px;
        margin-bottom: 10px;
    }

    .icontext {
        color: white;
       /*  font-size: 16px; */
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
	
	 #tileMenuModal img{
	 	height:67px;
	 	width:67px;
	 	padding: 5px;
	 }
	#tileMenuModal .markGlyphicon{
		height:67px;
	}

</style>
<nav class="navbar navbar-apple-custom" role="navigation">
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
<li><a href="#">Students</a></li>
		<li><a href="#" id="menu_dashboard"><i class="glyphicon glyphicon-list"></i> Dashboard</a></li>    
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
			<div id="tile2" class="tile">
			      <div class="carousel slide" data-ride="carousel">
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
            </div>
			</a>
        </div>
        
        <div class="col-sm-2 col-xs-2">
            <a href="viewStudentsNotes">
			<div id="tile3" class="tile">
			      <div class="carousel slide" data-ride="carousel">
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
            </div>
			</a>
        </div>
        
        <div class="col-sm-2 col-xs-2">
            <a href="ViewExamListAction">
			<div id="tile4" class="tile">
			      <div class="carousel slide" data-ride="carousel">
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
            </div>
			</a>
        </div>
         <div class="col-sm-2 col-xs-2">
            <a href="ViewMarksAction">
			<div id="tile5" class="tile">
			      <div class="carousel slide" data-ride="carousel">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div class="markGlyphicon">
                                <span class="glyphicon glyphicon-signal bigicon"></span>
                            </div>
                            <div class="icontext">
                                Marks
                            </div>
                            
                        </div>
						
                    </div>
					</div>
            </div>
			</a>
        </div>
        <div class="col-sm-2 col-xs-2">
            <a href="viewStudentAttendance">
			<div id="tile6" class="tile">
			      <div class="carousel slide" data-ride="carousel">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active text-center">
                            <div class="markGlyphicon">
                                <img src="/images/Attendance.svg">
                            </div>
                            <div class="icontext">
                                Attendance
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
