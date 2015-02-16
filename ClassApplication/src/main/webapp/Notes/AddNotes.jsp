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
<link href="../css/bootstrap.min.css" rel="stylesheet">
 <link href="../css/admin.css" rel="stylesheet">
 <script src="../js/jquery-1.10.2.min.js"></script>
 <script src="../js/bootstrap.min.js"></script>
<script type="text/javascript">
function validate()
{
var file=document.getElementByID("myFile");

	return false;
}
$(document).ready(function(){
$(":file").filestyle({buttonBefore: true});

$('.selectpicker').selectpicker();
$('#form').bootstrapValidator({
    feedbackIcons: {
        valid: 'glyphicon glyphicon-ok',
        invalid: 'glyphicon glyphicon-remove',
        validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
    	notesname: {
            validators: {
                notEmpty: {
                    message: 'The username is required'
                }
            }
        },
        myFile: {
            validators: {
                notEmpty: {
                    message: 'The password is required'
                }
            }
        }
    }
});
});
</script>
</head>
<body>
<div align="center">
<table>

<form action="upload" method="post" enctype="multipart/form-data" role="form" id="form">
	<div class="form-group">
	<tr><td><label>Notes Name :</label></td>
	<td>
	<div class="form-group">
	<input type="text" name="notesname" class="form-control">
	</div>
	</td>
	</tr>
     <tr> 
     <td><label for="myFile">Upload your file</label></td>
      <td>
      <div>
      <input type="file" name="myFile" accept=".pdf" class="form-control"  size="100px"></td>
      </div>
	</td>
      </tr>
      <tr>
      <td>
     <label>Select Subject:</label> 
      </td>
      <td>
      <select class="form-control" name="subject">
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
      <label> 
      Enter Division :
      </label>
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
      </div>
   </form>
   </table>
   </div>
</body>
</html>