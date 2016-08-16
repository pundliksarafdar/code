<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cx" uri="WEB-INF/customtaglib/switch.tld"%>

    <head>
        <meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
        <title></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/hexagon.css">
        <style>
        	#imagePlaceholder{
        		width: 130px;height: 130px;BORDER-RADIUS: 100%;padding: 20px;margin: auto;
        	}
        	.tile{
        		border-radius:10px;
        		padding:5px;
        		margin:10px 0px;
        	}
        	
        	
        	#featureDetails{
		        text-overflow: ellipsis;
			    overflow: hidden;
			    white-space: nowrap;
			    cursor: pointer;
			    color:blue;
    		}
    		
        </style>
        <script>
			$(document).ready(function(){
				$("#home-features-tab").makeTabs();	
			});
		</script>
    </head>
    <body>
		<jsp:include page="home_head.html"></jsp:include>
		<div style="min-height: 80vh;" id="login">
		<div class="container">
		<div class="row">
		
		<div class="col-md-6" style="">
        <ul id="hexGrid">
            <li class="hex">
                <a class="hexIn" href="#" style="background: #8B008B;">
                    <img src="images/Alert.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: white;">
                </a>
            </li>			
             <li class="hex">
                <a class="hexIn" href="#" style="background: #7C91C7;">
                    <img src="images/Attendance.svg" alt="" />
                </a>
            </li>
			 <li class="hex">
                <a class="hexIn" href="#" style="background: #87CEFA;">
                    <img src="images/class.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #ff0a00;">
                    <img src="images/Exam.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #fe6700;">
                    <img src="images/Fees.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #191970;">
                    <img src="images/Student.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: white;">                    
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #FFD602;">
                    <img src="images/Notes.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #A0522D;">
                    <img src="images/Question.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: white;">
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #191970;">
                    <img src="images/Setting.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #2cc822;">
                    <img src="images/Teacher.svg" alt="" />
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #00FF7F;">
                    <img src="images/Timetable.svg" alt="" />
                </a>
            </li>
			
        </ul>
		</div>
		<div class="col-md-6" style="padding:30px;">
			<form role="form" action="login" method="post">
							<div class="hide alert alert-danger frontenderror">
									
							</div>
							<s:set var="varMsg" value="%{sessionMessageError}" />
							<s:if test="%{#varMsg!=null}">
							<div class="alert alert-danger">
							<s:property value="varMsg" />
							</div>
							</s:if>
							
							<s:set value="loginBean.loginname" name="usename"></s:set>	
							<s:if test="%{#usename!=null}">
							<s:if test="hasActionErrors()">
								<div class="alert alert-danger">
									<s:actionerror />
								</div>
							</s:if>
							</s:if>
							<input type="hidden" name="loginBean.lastLogin" value="<%=new java.util.Date().getTime()%>"/>
							<!-- <div class="form-group">
								<label for="lgname" class="col-sm-4 control-label">*Login
									Name</label>
								<div class="col-sm-8">
									<input name="loginBean.loginname" type="text"
										class="form-control" id="lgname" placeholder="Login name" />
								</div>
							</div> -->
							<div class="form-group">
								<div class="input-group">
								  <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
								  <input name="loginBean.loginname" type="text" class="form-control" placeholder="Username">
								</div>
							</div>
								
							<!-- <div class="form-group">
								<label for="lgpass" class="col-sm-4 control-label">*Password</label>
								<div class="col-sm-8">
								<div class="input-group">
									<input name="loginBean.loginpass" type="password"
										class="form-control" id="lgpass" placeholder="Password" />
									<span class="input-group-addon" id="showPass"><i class="glyphicon glyphicon-hand-left"></i></span>	
								</div>
								</div>
							</div>
							 -->
							<div class="form-group">
								<div class="input-group">
								  <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
								  <input name="loginBean.loginpass" type="password" class="form-control" placeholder="Password">
								</div>
							</div>

							<div class="btn-group form-group" role="group">
								<input type="submit" class="btn btn-default submit" value="Login" /> <a
									href="register" class="btn btn-default">Register</a>
							</div>
							<div>
								<a href="forgot">Forgot Password</a>
							</div>
						</form>
			
		</div>
		</div>
		</div>
		</div>
		<section style="margin-top:20px;" id="features">
			<div id="home-features-tab" class="container"></div>
		</section>	
		         <section id="just-intro" style="padding-top: 50px;">
             <div class="container">
              
             <div class="row text-center pad-row">
            <div class="col-md-4  col-sm-4">
                 <img src="images/1470441381_Customer_Support.svg" height="100px" width="100px"/>
                            <h4> <strong>Contact Us</strong> </h4>
                            <p>
                                Email:-mycorex2015@gmail.com<br>
                                Phone:- +91 9766120685
                            </p>    
                </div>
             <div class="col-md-4  col-sm-4">
                 <img src="images/1470441476_logo_social_media_location.svg" height="100px" width="100px"/>
                            <h4> <strong>Our Location</strong> </h4>
                            <p>
                                Kasturinagar,Banglore - 560043,Karnataka
                            </p>
                </div>
            <div class="col-md-4  col-sm-4">
                  			<img src="images/1470444730_Korawan_M_business08.svg" height="100px" width="100px"/>
                            <h4> <strong>About Company</strong> </h4>
                            <p>
                               Classfloor is a technology start-up founded in the heart of India's Silicon Valley - Bangalore by four musketeers.  Classfloor provides the excellent platform for institute management system.
							  <a href="aboutUs">Read more ...</a>

                            </p>    
                </div>
                    
            </div>
                 </div>
         </section>
		
		
		
		<div class="modal fade" id="messageModal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="myModalLabel">Small modal</h4>
        </div>
        <div class="modal-body" id="mymodalmessage">
          
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>
</div>
	</body>
</html>
