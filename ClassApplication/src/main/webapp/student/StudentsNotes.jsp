<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
$(document).ready(function(){
	  $( "#datetimepicker" ).datetimepicker({
		  pickTime: false
	  }).data("DateTimePicker");
	  $("classname").change(function(){
		  alert("suraj");
		  
		  
		  
	  });
	  
});
</script>
</head>
<body>
<div>
<table>
<tr>
<td>Select Class</td>
<td>
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes"); %>
<select id="classname">
<option>Select Class</option>
<%
int counter=0;
while(list.size()>counter){ %>
<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName() %></option>
<%counter++;
} %>
</select>
</td>
</tr>
<tr>
<td>Select Date
</td>
<td>
<div id="datetimepicker" class="input-group" style="width :150px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date"/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div></td>
</tr>
<tr><td>Select Batch:</td><td><select id="batch"><option>select Batch</option></select></td></tr>
<tr>

<td>
   <button type="button" class="btn btn-primary" 
      data-loading-text="Loading..."  id="submit">Submit
   </button>

</td>
</tr>
</table>

</div>
</body>
</html>