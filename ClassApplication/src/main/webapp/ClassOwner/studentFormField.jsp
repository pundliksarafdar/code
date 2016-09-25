<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="ClassownerSettingHeader.jsp" >
		<jsp:param value="active" name="studentFormFieldAction"/>
	</jsp:include>
	<section>
	<form role="form">
	<div id="formField">
	
	</div>
	</form>
	<div>
		<input type="button" value="Add" class="btn btn-success" id="addField"/>
		<input type="button" value="Save" class="btn btn-success" id="saveField"/>
	
	</div>
	</section>
	
	<section class="hide" id="fieldGroup">
		<div class="form-group">
		<div class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-minus-sign removeItem" style="color:red;"></i></span>
		  <input type="text" class="form-control" placeholder="Enter new field">
		</div>
		</div>
	</section>
</body>
</html>