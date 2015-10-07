<%@page import="java.util.Map"%>
<%@page import="com.user.UserBean"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Expires", "0");
%>
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
 <link href="css/jquery.autocomplete.css" rel="stylesheet">
<link href="css/common.css" rel="stylesheet">	
<link href="assets/css/style.css" rel="stylesheet" /> 
<link href="assets/css/font-awesome.min.css" rel="stylesheet" />
<link rel="icon" 
      type="image/jpeg" 
      href="images/cxlogowhite.jpg">
 
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
    font-family: cursive;
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

	<%
		UserBean userBean = (UserBean) session.getAttribute("user");
		/*if(userBean.getRole()<2){*/
		if (true) {
	%>

<jsp:include page="allmodals.jsp"></jsp:include>

<%} %>

<%
		Map<String, String> map = (Map<String, String>)request.getAttribute("param");
		String ignoresession = map.get("ignoresession");
		if(!"true".equals(ignoresession)){
			%>
			<script>
			var timeout = <%=request.getSession().getMaxInactiveInterval()%>;
				
				setMessage();
				function setMessage(){
					setTimeout(function(){
						timeout = timeout-1;
						if(timeout!=0){
							setMessage();
						}
						$(".sessionTimeoutModal #seconds").text(timeout);
						
						if(timeout == 60){
							var lastActivity = (new Date()-eventOccuredSience)/1000;
							if(lastActivity<60){
								reloadSession();
								timeout = <%=request.getSession().getMaxInactiveInterval()%>;
							}else{
								modal.sessionTimeout("Session Timeout", "Your session is about to expire in <span id='seconds'></span> seconds", "Continue", "Logout"
								,reloadSession
								,["paramsCancel"],function(){
									location.href="logout";	
								},["paramsOk"]);	
				}
							}
							
						if(timeout == 0){
							location.href="logout";
						}		
					},1000);
				}
				
				var reloadSession = function(){
							$.ajax({
								url:"sessionreload",
								type:"POST",
								success:function(data){
									timeout = <%=request.getSession().getMaxInactiveInterval()%>;
								},
								error:function(){
									
								}
							});
					}
			</script>
			<%
		}

%>
	<div id="outerDiv">
		<div id="innerDiv">
			<div id="header">
				<div>				
					<tiles:insertAttribute name="topnav" />				
				</div>
					<br/>							
			</div>
			<div id="body" style="margin: 10px;background-color: rgb(221, 225, 216)" >
				<div>
					<tiles:insertAttribute name="body" />
					<br />
				</div>	
			</div>
			<div id="footer">
				<tiles:insertAttribute name="footer" />		
				
			</div>
		</div>
	</div>
</body>
</html>