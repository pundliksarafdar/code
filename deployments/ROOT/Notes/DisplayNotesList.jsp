<%@page import="com.classapp.db.Notes.Notes"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%List<Notes> notes= (List<Notes>) request.getAttribute("noteslist"); %>
<div align="center">
<table border="1" class="table table-hover">
<tr>
<th>Sr.</th>
<th>Name</th>
<th></th>
</tr>
<%for(int i=0;i<notes.size();i++){ %>
<tr>
<td><%=i+1 %></td>
<td><%=notes.get(i).getName() %></td>
<td><a href="shownotes.action?notesid=<%=notes.get(i).getNotesid()%>">Click Here</a></td>
</tr>
<%} %>
</table>
</div> 
</body>
</html>