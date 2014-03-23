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
<script src="js/prettify.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootstrap-modalmanager.js"></script>
<script src="js/bootstrap-modal.js"></script>
<script src="js/bootstrap-paginator.min.js"></script>
<script src="js/datepicker.js"></script>
<script src="js/modal/modal.js"></script>
<script type="text/javascript" src="">

</script>
</head>
<body>
	<!-- Modal confirmation Box Start -->
	<div class="modal fade bs-example-modal-sm" id="messageModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
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

<div class="modal fade bs-example-modal-sm" id="acceptModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Accept Class</h4>
        </div>
        <div class="modal-body" id="">
          	<div>Do you want to accept the class?</div>
          	<div>
          		<table>
          			<tr>
          				<td><input type="text" id="duration" data-provide="typeahead" class="form-control"/></td><td>&nbsp;&nbsp;&nbsp; Months</td>
          			</tr>
          		</table>
          	</div>
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-success" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>

<div class="modal fade bs-example-modal-sm" id="rejectModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Reject Class</h4>
        </div>
        <div class="modal-body" id="">
          	<div>Do you want to reject the class?</div>
          	
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-danger" data-dismiss="modal">Yes</button>
      	</div>
    </div>
</div>	

<div class="modal fade bs-example-modal-sm" id="addBatSubModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id=""></h4>
        </div>
        <div class="modal-body" id="">
          	<div id="addMessage"></div>
          	<div class="error"></div>
          	<input type="text" id="name" data-provide="typeahead" class="form-control"/>
          	<input type="hidden" id="task"/>
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal">Add</button>
      	</div>
    </div>
</div>	

	<!-- Modal Box End -->
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