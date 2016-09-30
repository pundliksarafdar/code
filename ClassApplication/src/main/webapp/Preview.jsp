<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Class Owner</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-responsive.css" rel="stylesheet">
 <link href="css/admin.css" rel="stylesheet">
 <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet">
 <link href="css/jquery.autocomplete.css" rel="stylesheet">
<link href="css/common.css" rel="stylesheet">	
<link href="assets/css/style.css" rel="stylesheet" /> 
<link href="assets/css/font-awesome.min.css" rel="stylesheet" />
<link rel="icon" 
      type="image/jpeg" 
      href="images/bluelogo.gif">
 
<!-- DataTables, TableTools and Editor CSS 
<link rel="stylesheet" type="text/css" href="/media/css/jquery.dataTables.css">
<link rel="stylesheet" type="text/css" href="/extensions/TableTools/css/dataTables.tableTools.css">
<link rel="stylesheet" type="text/css" href="/extensions/Editor-1.3.2/css/dataTables.editor.css">-->
 
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
    padding-right: 5px;
    padding-left: 5px;
    font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif;
    min-height: 525px;
   /* background-color: rgb(141,141,253);*/
     background: linear-gradient(to right, red , blue);
    background-size: 100% 100%;
}

.white-back{
background-color: rgb(142,142,142);
color: white;
}

/* #footer-sec{
padding-bottom: 20px;
padding-top: 10px;
} */
</style>
<style type="text/css">
html,body {
	margin: 0;
	padding: 0;
		}
</style>
<script src="js/jquery-1.10.2.min.js"></script>
<script src="js/allAjax.js"></script>
<script src="js/main.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/modal/modal.js"></script>
<script src="js/moment.min.js"></script>
<script src="js/bootstrap-datetimepicker.min.js"></script>
<script src="js/jquery.autocomplete.js"></script>
<script src="js/global.js"></script>
<script src="js/angular.min.js"></script>
<script type="text/javascript" src="js/Chart.min.js"></script>
<script src="js/plugins.js"></script>
<script src="js/validator.js"></script>
<script>
$( document ).ajaxStart(function() {
	  $("#loaderModal").modal("show")
});

$( document ).ajaxStop(function() {
	  $("#loaderModal").modal("hide")
});


</script>
</head>
<body>

	<div class="modal fade" id="loaderModal">
		<div class="modal-dialog">
			
				<div class="corex-loader">
					
			</div>	
			
		</div>
	</div>

	

<meta http-equiv="cache-control" content="max-age=0" />

<style>
	.className:hover{
		color:white;
	}
	
	.notificationBlinker {
	    background-color: red;
	    -webkit-animation-name: blinker; /* Chrome, Safari, Opera */
	    -webkit-animation-duration: 1s; /* Chrome, Safari, Opera */
	    -webkit-animation-iteration-count: infinite; /* Chrome, Safari, Opera */
	    animation-name: blinker;
	    animation-duration: 1s;
	    animation-iteration-count: infinite;
	}
	
	/* Chrome, Safari, Opera */
	@-webkit-keyframes blinker {
		50%  {background-color:red;}
	}
	
	/* Standard syntax */
	@keyframes blinker {
	    50%  {background-color:red;}
	}
		
	.popover-content {
	  color:black;
	  max-height:300px;
	  width:200px;
	  overflow-y: auto;
	  overflow-x: hidden;
	 }
		
</style>
	<div id="menuNotificationDetails" style="display:none">
		<div id="menuNotificationDetailsData">
			<ul class="nav nav-pills nav-stacked">
			  <li id="menuNotificationDetailsDataReevaluation">
				<a href="#" class="btn btn-success btn-xs">
				  <span class="badge pull-right">5</span>
				  Re-evaluation
				</a>
			  </li>
			  <li>
				<a href="#" class="btn btn-success btn-xs">
				  <span class="badge pull-right">5</span>
				  General notification
				</a>
			  </li>
			</ul>
		</div>
		<div id="menuNotificationDetailsProgress">
			<div class="corex-loader">Please wait</div>
		</div>
	</div>	
	<div id="id0" style="display:none">
		<div>
		<table class="table">
		<tr id="id0">
			<td>Messages 1</td>
			<td><button id="examList0" class="btn btn-xs btn-danger pull-right" type="button">&nbsp;<i class="glyphicon glyphicon-unchecked"></i></button></td>
		</tr>
		<tr id="id1">
			<td>Messages 2</td>
			<td><button id="examList0" class="btn btn-xs btn-warning pull-right" type="button">&nbsp;<i class="glyphicon glyphicon-edit"></i></button></td>
		</tr>
		<tr><td></td><td></td></tr>
		</table>
		</div>
	</div>
	
	<div id="id1" style="display:none">
	
	</div>
<nav class="navbar navbar-apple-custom" role="navigation" ng-controller="SiteMapController">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" style="border-color: white;border-width: 1px;">
      <i class="glyphicon glyphicon-th-large"></i>
    </button>
    <a class="navbar-brand" href="#">
	<img src="images/cxlogo.jpg" alt="cxlogo" style="height: 20px;" class="img-rounded"/>
	CoreX</a>
  </div>

  <!-- Collect the nav links, forms, and other content for toggling -->
  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    <ul class="nav navbar-nav">
      
      <!-- <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Class Owner <b class="caret"></b></a>
      		<ul class="dropdown-menu">
	            <li><a href="addsubject">Add Subject</a></li>
	         </ul>
      </li> -->
      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Manage <b class="caret"></b></a>
      </li>
      
      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Questions <b class="caret"></b></a>
      </li>
      

      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Exam <b class="caret"></b></a>
      </li>
      

      
      <li>
      	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Time Table <b class="caret"></b></a>
      </li>
    

    	<li><a href="#" class="dropdown-toggle" data-toggle="dropdown">Notes <b class="caret"></b></a>
    	</li>
    
    <li><a href="#" class="dropdown-toggle" data-toggle="dropdown">Send Notice/Message<b class="caret"></b></a>
    </li>
    </ul>
	<ul class="nav navbar-nav navbar-right">
      <li>
		<form class="navbar-form navbar-left" role="search">
			<div class="form-group">
			  <input type="text" class="form-control" ng-model="searchSiteMap.searchString" placeholder="Search">
			</div>
      	</form>	
	  </li>
	  
	  <li>
	  <form class="navbar-form navbar-left">
			<div class="form-group">
	  			<a href="#" id="notificationBtn" data-toggle="popover" data-placement="bottom" class="btn btn-default className notificationBlinker" style="background: transparent;">classone</a>
	  		</div>
	  	</form>
	  	</li>	
	  
	  <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-phone-alt"></i> <b class="caret"></b></a>
 
      </li>
      
	  <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Class Name <b class="caret"></b></a>
      </li>
    </ul>
  </div><!-- /.navbar-collapse -->
</nav>
</div>

				
				</div>
					<br/>							
			</div>
			<div id="body" style="margin: 10px;background-color: white" >
				<div>
					


<html>
<head>
<!-- <link href="../assets/css/style.css" rel="stylesheet" /> -->
</head>
<div class="container" style="
    margin-right: 0px;
    margin-left: 100px;
">
<div class="col-md-10">
<div >
	
	<div class="row">
		<div class="col-md-5 well" style="height:240px">
		<div align="center" style="font-size: larger;"><u>Institute Statastics</u></div>
		<div class="input-group">
			<input
				type="text" disabled="disabled" class="form-control" placeholder="Username"
				aria-describedby="basic-addon1" value="NO OF BATCHES">
			<span class="input-group-addon" id="basic-addon1">11</span>	
		</div>
		
		<div class="input-group">
			<input
				type="text" disabled="disabled" class="form-control" placeholder="Username"
				aria-describedby="basic-addon1" value="NO OF TEACHERS">
			<span class="input-group-addon" id="basic-addon1">8</span> 	
		</div>
		
		<div class="input-group">
			<input
				type="text" disabled="disabled" class="form-control" placeholder="Username"
				aria-describedby="basic-addon1" value="NO OF STUDENTS">
				<span class="input-group-addon" id="basic-addon1">3</span>
		</div>
		</div>
		<div class="col-md-1"></div>
		<div class="col-md-6 well">
		<div align="center" style="font-size: larger;"><u>Storage Statastics</u></div>
		<div class="row">
		<div class="col-md-6  " >
	<div id="classownerStorageGraph" align="center">
		<canvas id="classownerStorageGraphCtx" width="150" height="150"></canvas>
	</div>
</div>

<div class="col-md-6 "  >
	<div class="panel panel-default">
		<table id="classownerStorageTable" class="table">
			<tr style="background-color:;">
				<td>Total Space</td>
				<td id="classownerStorageTotalSpace"></td>
			</tr>

			<tr style="background-color: #FDB45C;">
				<td>Exam</td>
				<td id="classownerStorageExamSpace"></td>
			</tr>

			<tr style="background-color: #46BFBD;">
				<td>Notes</td>
				<td id="classownerStorageNotesSpace"></td>
			</tr>

			<tr style="background-color: #009900;">
				<td>Space Left</td>
				<td id="classownerStorageSpaceLeftSpace"></td>
			</tr>
		</table>
	</div>
</div>
</div>
</div>

</div>
	</div>
	<div>
	<div class="row">
	<div align="left" class="col-md-12">

		<hr>
		
		<div class="alert alert-info">Notifications</div>
		<div>
	<marquee behavior="scroll" direction="up" height="100" width="350" onmouseover="this.stop();" onmouseout="this.start();">
			<ul>
				
				<li>September 24 -> All Batches -> Notice</li>
				<Br>
				
				<li>September 24 -> Morning -> Notice</li>
				<Br>
				
			
			</ul>
</marquee>
		</div>
		

	</div>
</div>
</div>
</div>
<div class="col-md-2"><img src="/images/1949023.jpg" style="height: 400px;width: 200px" alt="Advertisement 1" id="adv1"></div>
</div>
<div class="container">
<div class="row">
<div class="col-md-12"><img src="/images/1949023.jpg" style="height: 200px;width: 600px" alt="Advertisement 2" id="adv2"></div>
</div>
</div>

<script>
		var remaingSpace = 142.83749771118164;
		var notesSpace = 6.363703727722168;
		var examSpace = 0.7987985610961914;
		var totalSpace = 150.0;
		var doughnutData = [
				{
					value: remaingSpace.toFixed(2),
					color: "#009900",
					highlight: "#00AA00"
				},
				{
					value: notesSpace.toFixed(2),
					color: "#46BFBD",
					highlight: "#5AD3D1"
				},
				{
					value: examSpace.toFixed(2),
					color: "#FDB45C",
					highlight: "#FFC870"
				}
			];

			window.onload = function(){
				var ctx = document.getElementById("classownerStorageGraphCtx").getContext("2d");
				window.myDoughnut = new Chart(ctx).Doughnut(doughnutData);
			};


		$("td#classownerStorageTotalSpace").text(totalSpace.toFixed(2));
		$("td#classownerStorageExamSpace").text(examSpace.toFixed(2))
		$("td#classownerStorageNotesSpace").text(notesSpace.toFixed(2));
		$("td#classownerStorageSpaceLeftSpace").text(remaingSpace.toFixed(2));
		
		
	</script>
	</html>
					<br />
				</div>	
			</div>
			<div id="footer" style="background-color: black;color: white;" align="center">
						
				© 2015 Corex. All rights reserved
			</div>
		</div>
	</div>
</body>
</html>