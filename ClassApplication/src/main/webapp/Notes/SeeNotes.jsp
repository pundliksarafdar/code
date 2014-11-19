<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
function validate()
{
var file=document.getElementByID("myFile");
alert(file);
	return false;
}
</script>
</head>
<body>
<div align="center">
<table>

<form action="fetchnotes" method="post" enctype="multipart/form-data" role="form">
      <td>
      Select Subject:
      </td>
      <td>
      <select name="subject" class="form-control">
      <option>Select one</option>
      <%List<Subjects> list=(List<Subjects>)request.getAttribute("subjects"); 
      for(int i=0;i<list.size();i++)
      {
      %>
      <option value="<%=list.get(i).getSubjectId() %>"><%=list.get(i).getSubjectName()%></option>
      <%} %>
      </select>
      </td>
      </tr>
      <tr>
      <td>
      Select Division :
      </td>
      <td>
      <select name="division" class="form-control">
      <option>Select one</option>
      <%List<Division> divisions=(List<Division>)request.getAttribute("divisions"); 
      for(int i=0;i<divisions.size();i++)
      {
      %>
      <option value="<%=divisions.get(i).getDivId() %>"><%=divisions.get(i).getDivisionName()%>  <%=divisions.get(i).getStream() %></option>
      <%} %>
      </select>
      </td>
      
      </tr>
      <tr>
      <td></td>
      <td>
      <button type="submit" class="btn btn-default">Submit</button>
      </td>
      </tr>
   </form>
   </table>
   </div>
</body>
</html>