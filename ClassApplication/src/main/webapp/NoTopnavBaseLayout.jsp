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
 <link href="css/datetimepicker.css" rel="stylesheet">
 <style>
 	
body{
    padding-right: 0px;
    padding-left: 0px;
}
 
 </style>
 <script src="js/jquery-1.10.2.min.js"></script>
 <script src="js/bootstrap.min.js"></script>
 <script src="js/allAjax.js"></script>	
 <script src="js/datepicker.js"></script>
</head>
<body>
	<div id="outerDiv" align ="center">
		<div id = "innerDiv" >
			<div id="header">
				<tiles:insertAttribute name="header" />
				<br/>							
			</div>
			<div id="body">
				<div style="padding: 5px;">
					<tiles:insertAttribute name="body" />
					<br />
				</div>	
			</div>
			<div id="footer" style="background: red;">
				<tiles:insertAttribute name="footer" />				
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
		$('#datetimepicker').datetimepicker({
		 format: 'yyyy-MM-dd',	
		 pickTime: false
		});
		});
	</script>
</body>
</html>