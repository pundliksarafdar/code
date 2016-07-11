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
		
    </head>
    <body>
		<div class="container">
		<jsp:include page="home_head.html"></jsp:include>
		<div class="row">
		
		<div class="col-md-6" style="">
        <ul id="hexGrid">
            <li class="hex">
                <a class="hexIn" href="#" style="background: #8B008B;">
                    <img src="images/Alert.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: white;">
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>			
             <li class="hex">
                <a class="hexIn" href="#" style="background: #7C91C7;">
                    <img src="images/Attendance.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			 <li class="hex">
                <a class="hexIn" href="#" style="background: #87CEFA;">
                    <img src="images/class.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #ff0a00;">
                    <img src="images/Exam.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #fe6700;">
                    <img src="images/Fees.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #191970;">
                    <img src="images/Student.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: white;">
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #FFD602;">
                    <img src="images/Notes.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #A0522D;">
                    <img src="images/Question.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: white;">
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #191970;">
                    <img src="images/Setting.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #2cc822;">
                    <img src="images/Teacher.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			<li class="hex">
                <a class="hexIn" href="#" style="background: #00FF7F;">
                    <img src="images/Timetable.svg" alt="" />
                    <h1>This is a title</h1>
                    <p>Some sample text about the article this hexagon leads to</p>
                </a>
            </li>
			
        </ul>
		</div>
		<div class="col-md-6" style="padding:30px;">
			<!--
			<form role="form">
				<div class="form-group">
					<h3 style="color:white;text-align:center;">Class floor</h3>
				</div>
				<div class="form-group">
				<div class="input-group">
				  <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
				  <input type="text" class="form-control" placeholder="Username">
				</div>
				</div>
				
				<div class="form-group">
				<div class="input-group">
				  <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
				  <input type="password" class="form-control" placeholder="Password">
				</div>
				</div>
				
				<div class="form-group">
					<input type="button" class="btn btn-success" value="Login"/>
					<input type="button" class="btn btn-link" value="Sign in"/>
				</div>
			</form>
			-->
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
		         <section id="just-intro" style="padding-top: 50px;">
             <div class="container">
              
             <div class="row text-center pad-row">
            <div class="col-md-4  col-sm-4">
                 <i class="fa fa-desktop fa-5x"></i>
                            <h4> <strong>Contact Us</strong> </h4>
                            <p>
                                Email:-mycorex2015@gmail.com<br>
                                Phone:- +91 9766120685
                            </p>    
                </div>
             <div class="col-md-4  col-sm-4">
                 <i class="fa fa-flask  fa-5x"></i>
                            <h4> <strong>Our Location</strong> </h4>
                            <p>
                                Kasturinagar,Banglore - 560043,Karnataka
                            </p>
                </div>
            <div class="col-md-4  col-sm-4">
                  <i class="fa fa-pencil  fa-5x"></i>
                            <h4> <strong>About Company</strong> </h4>
                            <p>
                               Classfloor is a technology start-up founded in the heart of India's Silicon Valley - Bangalore by four musketeers.  Classfloor provides the excellent platform for institute management system.
							  <a href="aboutUs">Read more ...</a>

                            </p>    
                </div>
                    
            </div>
                 </div>
         </section>
		
	</body>
</html>
