<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/AddSubject.js"></script>
<script type="text/javascript">
var globalclassid="";
function editclass(id){
	$('div#ModifyClassModal .error').hide();
	$("#editclassname").val($("#name"+id).val());
	$("#editstream").val($("#stream"+id).val());
	$("#hidclassid").val(id);
	$('#ModifyClassModal').modal('toggle');
	
}

function deleteclass(classid){
	globalclassid=classid;
	/* var result=confirm("Are You Sure?");
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
  		  /*  setTimeout(function(){
  			   location.reload();
  		   },2*1000); 
        	$("#paginateform").submit();
        },error:function(data){
        	
        }
	});
	} */
	$("#classdeleteconfirmmodal").modal("toggle");
}

$(document).ready(function(){
	$("#classdeleteconfirm").click(function(){
		$.ajax({
	        url: 'classOwnerServlet',
	        type: 'post',
	        data: {
		    	 methodToCall: "deleteclass",
		    	 classid:globalclassid
	        },
	        success: function(data){
	        	modal.launchAlert("Success","Class Deleted!");
	  		  /*  setTimeout(function(){
	  			   location.reload();
	  		   },2*1000); */
	        	$("#paginateform").submit();
	        },error:function(data){
	        	
	        }
		});
	});
	
	$("#classname,#stream").on("keyup",function(){
		var string = $(this).val();	
		$(this).val(string.charAt(0).toUpperCase() + string.slice(1));
	});
	$(".page").on("click",function(e){
		$("form#paginateform #currentPage").val($(this).text());
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".start").on("click",function(e){
		$("form#paginateform #currentPage").val("1");
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".end").on("click",function(e){
		$("form#paginateform #currentPage").val($("#totalPages").val());
		$("#paginateform").submit();
		e.preventDefault();
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
         		  /*  setTimeout(function(){
         			   location.reload();
         		   },2*1000); */
                	$("#paginateform").submit();
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
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 10px;">
<div align="center" style="font-size: larger;"><u>Manage Class</u></div>
<button data-target="#addclassModal" type="button" class="btn btn-info" data-toggle="modal"><i class="glyphicon glyphicon-plus"></i>&nbsp;Add Class</button>
</div>
<br>
<%List<Division> list=(List<Division>)request.getAttribute("classes"); 
int endIndex=(Integer)request.getAttribute("endIndex");
int currentPage=(Integer)request.getAttribute("currentPage");
int startIndex=(Integer)request.getAttribute("startIndex");
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
int counter=startIndex;
while(endIndex>counter)
{
%>
<tr>
<td><%=counter+1 %></td>
<td><%=list.get(counter).getDivisionName() %><input type="hidden" id="name<%=list.get(counter).getDivId()%>" value="<%=list.get(counter).getDivisionName()%>"></td>
<td><%=list.get(counter).getStream() %><input type="hidden" id="stream<%=list.get(counter).getDivId()%>" value="<%=list.get(counter).getStream()%>"></td>
<td><a href="#" class="btn btn-primary" onclick="editclass(<%=list.get(counter).getDivId()%>)">Edit</a></td>
<td><a href="#" class="btn btn-danger" onclick="deleteclass(<%=list.get(counter).getDivId()%>)">Delete</a></td>
</tr>
<%
counter++;
}
%>
</table>
</div>
<div>
 <form action="manageclass" id="paginateform">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <ul class="pagination">
  <li><a class="start" >&laquo;</a></li>
  <c:forEach var="item" begin="1" end="${totalPages}">
  <c:if test="${item eq currentPage}">
  <li class="active"><a href="#" class="page"><c:out value="${item}"></c:out></a></li>
  </c:if>
  <c:if test="${item ne currentPage}">
  <li><a href="#" class="page"><c:out value="${item}"></c:out></a></li>
  </c:if>
  </c:forEach>
  <li><a href="#" class="end">&raquo;</a></li>
</ul>
</form>
</div>
<%}else{
%>
<span class="alert alert-info">No Class added yet</span>
<%	
}

} %>

<div class="modal fade" id="classdeleteconfirmmodal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="myModalLabel">Delete Class</h4>
        </div>
        <div class="modal-body" id="mymodalmessage">
          Are you sure?
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancle</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="classdeleteconfirm">Yes</button>
      	</div>
    </div>
</div>
</div>
</body>
</html>