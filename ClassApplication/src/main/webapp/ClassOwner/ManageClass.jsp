<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/AddSubject.js"></script>
<script type="text/javascript">
function editclass(id){
	$('div#ModifyClassModal .error').hide();
	$("#editclassname").val($("#name"+id).val());
	$("#editstream").val($("#stream"+id).val());
	$("#hidclassid").val(id);
	$('#ModifyClassModal').modal('toggle');
	
}

function deleteclass(classid){
	var result=confirm("Are You Sure?");
	if(result){
	$.ajax({
        url: 'classOwnerServlet',
        type: 'post',
        data: {
	    	 methodToCall: "deleteclass",
	    	 classid:classid
        },
        success: function(data){
        	modal.launchAlert("Success","Class Deleted!");
  		   setTimeout(function(){
  			   location.reload();
  		   },2*1000);
        	
        },error:function(data){
        	
        }
	});
	}
}

$(document).ready(function(){
	$("#classname,#stream").on("keyup",function(){
		var string = $(this).val();	
		$(this).val(string.charAt(0).toUpperCase() + string.slice(1));
	});
	$('#saveclass').click(function(){
		var classname=$("#editclassname").val();
		var stream=$("#editstream").val();
		var classid=$("#hidclassid").val();
		if(classname.length>0){
		$('div#ModifyClassModal .progress').removeClass('hide');
		$('div#ModifyClassModal .saveclass').addClass('hide');
		 $.ajax({
                url: 'classOwnerServlet',
                type: 'post',
                data: {
			    	 methodToCall: "modifyclass",
			    	 classname:classname,
			    	 stream:stream,
			    	 classid:classid
                },
                success: function(data){
                	var resultJson = JSON.parse(data);
                	var result=resultJson.updated;
                	if(result=="false"){
                		$('div#ModifyClassModal .progress').addClass('hide');
                		$('div#ModifyClassModal .saveclass').removeClass('hide');
                		$('div#ModifyClassModal .error').show();
                  		$('div#ModifyClassModal .error').html('<strong>Error!</strong> Class Already Present');
                	}else{
                	$('#ModifyClassModal').modal('hide');
                	modal.launchAlert("Success","Class Modified!");
         		   setTimeout(function(){
         			   location.reload();
         		   },2*1000);
                	}
                }, error: function(data){
                    alert('ajax failed');
                }
	});
		}else{
			
			$('div#ModifyClassModal .error').show();
      		$('div#ModifyClassModal .error').html('<strong>Error!</strong> Class Name cannot be empty');
		}
	});
});
</script>
</head>
<body>
<div class="container bs-callout bs-callout-danger" style="margin-bottom: 10px;">
<button data-target="#addclassModal" type="button" class="btn btn-info" data-toggle="modal">Add Class</button>
</div>
<br>
<%List<Division> list=(List<Division>)request.getAttribute("classes"); 
if(list!=null)
{
if(list.size()>0)
{
%>

<div>
<table class="table table-bordered table-hover" style="background-color: white;" data-toggle="table">
<tr style="background-color: rgb(0, 148, 255);">
<th>Sr.</th>
<th>Class Name</th>
<th>Stream/Part</th>
<th>Edit</th>
<th>Delete</th>
</tr>
<%
int counter=0;
while(list.size()>counter)
{
%>
<tr>
<td><%=counter+1 %></td>
<td><%=list.get(counter).getDivisionName() %><input type="hidden" id="name<%=list.get(counter).getDivId()%>" value="<%=list.get(counter).getDivisionName()%>"></td>
<td><%=list.get(counter).getStream() %><input type="hidden" id="stream<%=list.get(counter).getDivId()%>" value="<%=list.get(counter).getStream()%>"></td>
<td><a href="#" onclick="editclass(<%=list.get(counter).getDivId()%>)">Edit</a></td>
<td><a href="#" onclick="deleteclass(<%=list.get(counter).getDivId()%>)">Delete</a></td>
</tr>
<%
counter++;
}
%>
</table>
</div>
<%}else{
%>
<span class="alert alert-info">No Class added yet</span>
<%	
}

} %>
</body>
</html>