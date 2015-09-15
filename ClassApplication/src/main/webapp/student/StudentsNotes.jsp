<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
 <style type="text/css">
 .error{
     color: red;
    margin-left: 10px;
}      

 </style>
<script type="text/javascript">
function validate()
{
var file=document.getElementByID("myFile");
alert(file);
	return false;
}
var globalnotesid="";
var globaldivision="";
var globalsubject="";






$(document).ready(function(){
	$(".shownotes").on("click",function(){
		$("form#actionform #notesid").val($(this).prop("id"));
		$("#actionform").prop("action","studentnotes");
		var formData = $("#actionform").serialize();
		console.log(formData);
		window.open("shownotes?"+formData,"","width=500, height=500"); 
	});
	
	
	$(".page").on("click",function(){
		$("form#paginateform #currentPage").val($(this).text());
	//	$("#totalmarks").val($("#temptotalmarks").html());
	//	$("#addedIds").val(addedIds);
	//	$("#removedIds").val(removedIds);
		$("#paginateform").submit();
	});
	
	$(".start").on("click",function(){
		$("form#paginateform #currentPage").val("1");
	//	$("#totalmarks").val($("#temptotalmarks").html());
	//	$("#addedIds").val(addedIds);
	//	$("#removedIds").val(removedIds);
		$("#paginateform").submit();
	});
	
	$(".end").on("click",function(){
		$("form#paginateform #currentPage").val($("#totalPage").val());
	//	$("#totalmarks").val($("#temptotalmarks").html());
	//	$("#addedIds").val(addedIds);
	//	$("#removedIds").val(removedIds);
		$("#paginateform").submit();
	});
	
	
});
</script>
</head>
<body>
<div class="container" style="margin-bottom: 5px">
			<a type="button" class="btn btn-primary" href="studentcommoncomponent?forwardAction=studentnotes" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
</div>
<h3><font face="cursive">Search Result</font></h3>
<hr>
	
 	<hr>
   <div id="notesdiv" class="container">
   <table id="notestable" class="table table-bordered table-hover" style="background-color: white;">
   <thead style="background-color: rgb(0, 148, 255);">
   	<tr>
   	<th>Sr No.</th>
   	<th>Name</th>
   	<th></th>
   	</tr>
   </thead>
   <tbody>
   <c:forEach items="${noteslist}" var="item" varStatus="counter">
   <tr>
   <td><c:out value="${counter.count}"></c:out></td>
   <td><c:out value="${item.name}"></c:out></td>
    <td><button class="btn btn-primary shownotes" id='<c:out value="${item.notesid}"></c:out>'>Open</button></td>
    </tr>
   </c:forEach>
   </tbody>
   </table>
   <form action="studentnotes" id="paginateform">
  <input type="hidden" name="division" id="division" value='<c:out value="${division}"></c:out>'>
   <input type="hidden" name="batch" id="batch" value='<c:out value="${batch}"></c:out>'>
   <input type="hidden" name="subject" id="subject" value='<c:out value="${subject}"></c:out>'>
   <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
   <input type="hidden" name="newbatch" id="newbatch">
   <input type="hidden" name="actionname" id="actionname" >
   <input type="hidden" name="notesname" id="notesname" >
   <input type="hidden" name="notesid" id="notesid" >
   <input type="hidden" name="totalPage" id="totalPage" value='<c:out value="${totalPage}"></c:out>'>
   <input type="hidden" name="currentPage" id="currentPage"  value='<c:out value="${currentPage}"></c:out>'>
  <ul class="pagination">
  <li><a class="start" >&laquo;</a></li>
  <c:forEach var="item" begin="1" end="${totalPage}">
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
   <form action="" id="actionform">
   <input type="hidden" name="division" id="division" value='<c:out value="${division}"></c:out>'>
   <input type="hidden" name="batch" id="batch" value='<c:out value="${batch}"></c:out>'>
   <input type="hidden" name="subject" id="subject" value='<c:out value="${subject}"></c:out>'>
   <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
   <input type="hidden" name="newbatch" id="newbatch">
   <input type="hidden" name="actionname" id="actionname" >
   <input type="hidden" name="notesname" id="notesname" >
   <input type="hidden" name="notesid" id="notesid" >
   <input type="hidden" name="totalPage" id="totalPage" value='<c:out value="${totalPage}"></c:out>'>
   <input type="hidden" name="currentPage" id="currentPage"  value='<c:out value="${currentPage}"></c:out>'>
   </form>
   
   <div class="modal fade" id="notesnotavailable" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           Notes not available for this subject
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="notesupdated" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           Notes updated successfully..
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="deletenotesalert" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           Notes deleted successfully..
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="editnotes" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body" align="left">
		Notes Name :- <input type="text" id="newnotesname" class="form-control" maxlength="50"> <br>
		<span id="notesnameerror" class="error"></span><br>
		This Notes is applicatble for following batches:- <br>
		<div id="batchesdiv"> 
		
		</div>         
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="savenotes">Save</button>
         </div>
         </div>
   </div>
    
</div>
</body>
</html>