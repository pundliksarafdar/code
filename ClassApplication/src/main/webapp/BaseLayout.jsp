<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.css" rel="stylesheet" />
<link href="css/prettify.css" rel="stylesheet" />
<link href="css/bootstrap-modal-bs3patch.css" rel="stylesheet" />
<link href="css/bootstrap-modal.css" rel="stylesheet" />
<link href="css/datetimepicker.css" rel="stylesheet">
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
</style>
<script src="js/jquery-1.10.2.min.js"></script>
<script src="js/allAjax.js"></script>
<script src="js/main.js"></script>
<script src="js/prettify.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootstrap-modalmanager.js"></script>
<script src="js/bootstrap-modal.js"></script>
<script src="js/bootstrap-paginator.min.js"></script>
<script src="js/datepicker.js"></script>

<script type="text/javascript" src=""></script>
</head>
<body>
	<div id="container">
		<div id="header">
			<tiles:insertAttribute name="header" />
			<br />
			<hr size="2px" />
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