<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<style type="text/css">
	 #headersEle span{display:inline}
	 
	 #headerothernav
	 {
	 	height: 100px;
	 	width : 50%;	 
	 }		 
</style>
<body>
	<div id="headersEle" align="left" style="float: left; width: 50%;">
		<span><a href="login"><img src="images/cxlogo.jpg" alt="cxlogo" style="height: 100px;" class="img-rounded"/></a></span>
		<!-- <span><b style="font-size: 40px;">CoreX - <tiles:insertAttribute name="title" ignore="true" /></b></span>-->
	</div>
	<div id = "headerothernav" style="float:right;">
		<div align="right" style = "height:50px; float:right; width:100%;">
		<ul class="nav nav-pills" style ="color: aqua;">
		  <li><a href="login">Home</a></li>
		  <li><a href="#" data-toggle="modal" data-target="#aboutUsModal">About Us</a></li>
		  <li><a href="#" data-toggle="modal" data-target="#contactUsModal">Contact Us</a></li>
		</ul>
		</div>
		<div align="right" style = "height:50px; float:right; width:100%;">
		<span class="label"></span>
		</div>
	</div>
</body>
</html>