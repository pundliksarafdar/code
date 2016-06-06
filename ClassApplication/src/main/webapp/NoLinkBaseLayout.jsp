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
<link href="css/common.css" rel="stylesheet">
<link rel="icon" 
      type="image/jpeg" 
      href="images/cxlogowhite.jpg">
 
 <style>
 	
body{
    padding-right: 0px;
    padding-left: 0px;
    font-family: cursive;
}
 
 </style>
 <script type="text/x-mathjax-config">
  MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});
</script>

<script type="text/javascript" async
  src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=AM_CHTML">
</script>
 <script src="js/jquery-1.10.2.min.js"></script>
 <script src="js/bootstrap.min.js"></script>
 <script src="js/allAjax.js"></script>	
 <script src="js/modal/modal.js"></script>
 <script src="js/moment.min.js"></script>
 <!-- <script src="js/datepicker.js"></script> -->
 <script src="js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>
	<div class="modal fade" id="loaderModal">
		<div class="modal-dialog">
			
				<div class="progress progress-striped active">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Please wait....
					</div>
			</div>	
			
		</div>
	</div>
	<div id="outerDiv" align ="center">
		<div id = "innerDiv" >
			<div id="header" style="height: 100px;">
				<tiles:insertAttribute name="header" />
				<br/>							
			</div>
			<div id="body">
				<div style="padding: 5px;">
					<tiles:insertAttribute name="body" />
					<br />
				</div>	
			</div>
			<div id="footer">
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
	<script>
		$(document).ajaxStart(function(e) {
			$("#loaderModal").modal("show")
			console.log(e);
		});

		$(document).ajaxStop(function() {
			$("#loaderModal").modal("hide")
		});
	</script>
	
</body>
</html>