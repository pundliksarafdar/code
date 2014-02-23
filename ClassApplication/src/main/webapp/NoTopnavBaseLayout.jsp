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
 <script src="js/jquery-1.10.2.min.js"></script>
 <script src="js/bootstrap.min.js"></script>
 <script src="js/allAjax.js"></script>	
</head>
<body>
  <tiles:insertAttribute name="header" /><br/>
   <hr size="2px"/>
   <tiles:insertAttribute name="body" /><br/>
   <hr size="2px"/>
   <tiles:insertAttribute name="footer" /><br/>
</body>
</html>