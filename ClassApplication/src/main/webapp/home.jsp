<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript">
function login(){
	location.href="/login";
}

function systemreq(){
	location.href="/sysreq";
}
</script>
</head>
<body>
	<input type="submit" onclick="javascript:login()" value="Login" autofocus/>
	<input type="button" onclick="javascript:systemreq()" value="System Requirement"/>
</body>
</html>