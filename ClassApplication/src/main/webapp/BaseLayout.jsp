<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-responsive.css" rel="stylesheet">
 <link href="css/admin.css" rel="stylesheet">
 <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet">
 <style>
.text-center {
	text-align: center;
}

.marketing h1 {
	font-size: 50px;
	font-weight: lighter;
	line-height: 1;
}

.marketing p {
	font-size: 18px;
}

body{
    padding-right: 0px;
    padding-left: 0px;
}
</style>
<link href="css/admin.css" rel="stylesheet">
<style type="text/css">
html,body {
	margin: 0;
	padding: 0;
	height: 100%;
}

#container {
	min-height: 100%;
	position: relative;
}

#header {
	
}

#body {
	padding-bottom: 20px; /* Height of the footer */
}

#footer {
 	position: fixed;
    bottom: 0;
    width: 100%;
}

.error{
	color: red;
}
</style>
<script src="js/jquery-1.10.2.min.js"></script>
<script src="js/allAjax.js"></script>
<script src="js/main.js"></script>
<script src="js/bootstrap.min.js""></script>
<script src="js/modal/modal.js"></script>
<script src="js/moment.min.js"></script>
<script src="js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>
<jsp:include page="allmodals.jsp"></jsp:include>
	<div id="container">
		<div id="header">
			<div class = "hidden-xs">
			<tiles:insertAttribute name="header" />
			</div>
			<br />
			<div class = "hidden-xs">
			<hr size="2px" />
			</div>
			<tiles:insertAttribute name="topnav" />
			<br />
			<hr size="2px" />
		</div>
		<div id="body">
			<div style="padding: 5px;">
				<tiles:insertAttribute name="body" />
				<br />
			</div>

		</div>
		<div id="footer" style="background: lavender;height: 25px;">
			<hr size="2px" />
			<tiles:insertAttribute name="footer" />
			<br />
		</div>
	</div>
</body>
</html>