<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
$(document).ready(function(){
	var examID;
	var subject;
	var division;
	var that;
	$('.hasDatepicker').datetimepicker({
		inline: true,
        sideBySide: true
	});
	
	$('.hasTimepicker').datetimepicker({
		pickDate:false
	});
	
	$(".disableExam").on("click",function(){
		examID=$(this).prop("id");
		subject=$("#subject").val();
		division=$("#division").val();
		that=$(this);	
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "disableExam",
			    	 examID:examID,
			    	 subject:subject,
			    	 division:division
			   		},
			   		success:function(data){
			   			 $(that).find("button").text("Enable");
			   			$(that).find("button").prop("class","btn btn-success");
			   			 $(that).prop("class","enableExam"); 
			   		},
			   		error:function(error){
			   		
			   		}
		});
		
	});
	
	$("#enableOK").on("click",function(){
		$(that).find("button").text("Enabling...");
		var starttime=$("#starttime").val();
		var endtime=$("#endtime").val();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "publishExam",
			    	 examID:examID,
			    	 subject:subject,
			    	 division:division,
			    	 starttime:starttime,
			    	 endtime:endtime
			   		},
			   		success:function(data){
			   			$(that).find("button").text("Disable");
			   			$(that).find("button").prop("class","btn btn-warning");
			   			$(that).prop("class","disableExam");
			   		},
			   		error:function(error){
			   		
			   		}
		});
	});
	
$(".enableExam").on("click",function(){
	$("#enableexammodal").modal({
	    backdrop: 'static',
	    keyboard: false
	});
	//$("#enableexammodal").modal('toggle');
	examID=$(this).prop("id");
	subject=$("#subject").val();
	division=$("#division").val();
	that=$(this);
	//$(this).find("button").text("Enabling...");
});

$(".viewmarks").on("click",function(e){
	$("form#actionform #examID").val($(this).prop("id"));
	$("form#actionform #currentPage").val(0);
	$("form#actionform #totalPages").val(0);
	$("form#actionform #examlistcurrentPage").val($("form#paginateform #currentPage").val());
	$("form#actionform #examlisttotalPages").val($("form#paginateform #totalPages").val());
	$("form#actionform #actionname").val("viewstudentmarks");
	$("#actionform").submit();
	e.preventDefault();
	
});

$("#backtoexamlist").on("click",function(){
	$("form#actionform #currentPage").val($("form#actionform #examlistcurrentPage").val());
	$("form#actionform #totalPages").val($("form#actionform #examlisttotalPages").val());
	$("form#actionform #actionname").val("");
	$("#actionform").submit();
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

});
</script>
</head>
<body>
<div class="container" style="margin-bottom: 5px">
<c:if test="${actionname ne 'viewstudentmarks' }">  
			<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=studentexammarks" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
</c:if>
<c:if test="${actionname eq 'viewstudentmarks' }">  
	<a type="button" class="btn btn-primary" href="#" id="backtoexamlist" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Back To Exam List</a>
</c:if>
</div>
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	<div align="center" style="font-size: larger;"><u>Exam Marks</u></div>
	</div>
	 <c:if test="${totalPages gt 0}">
	<div class="container">
  <h2><font face="cursive">Search Result</font> </h2>    
  <c:if test="${actionname ne 'viewstudentmarks' }">        
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Exam</th>
        <th>Attempt</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${examlist}" var="item" varStatus="counter">
    <tr>
    	<c:if test="${currentPage eq 1}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td><c:out value="${counter.count + ((currentPage-1)*10)}"></c:out></td>
        </c:if>
        <td><c:out value="${item.exam_name}"></c:out></td>
        <c:choose>
        <c:when test="${(role eq 0) || (role eq 1) || (role eq 2)}">
        <td><a href="#" class="viewmarks" id="<c:out value="${item.exam_id}" ></c:out>"><button class="btn btn-warning">View Marks</button></a></td>
        </c:when>
       <%--  <c:when test="${role eq 3 }">
        <td><a class="attemptExam" href="#" id="<c:out value="${item.exam_id}"></c:out>"><button class="btn btn-success">Enable</button></a></td>
        </c:when> --%>
        </c:choose>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  </c:if>
  <c:if test="${actionname eq 'viewstudentmarks' }">        
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Student Name</th>
        <th>Marks</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${examData}" var="item" varStatus="counter">
    <tr>
    	<c:if test="${currentPage eq 1}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td><c:out value="${counter.count + ((currentPage-1)*10)}"></c:out></td>
        </c:if>
        <td><c:out value="${item.student_name}"></c:out></td>
        <td><c:out value="${item.marks}"></c:out></td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  </c:if>
  
  <form action="studentexammarks" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
    <input type="hidden" name="examlistcurrentPage" id="examlistcurrentPage" value='<c:out value="${examlistcurrentPage}"></c:out>'>
  <input type="hidden" name="examlisttotalPages" id="examlisttotalPages" value='<c:out value="${examlisttotalPages}"></c:out>'>
   
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
	</c:if>
	<form action="studentexammarks" id="actionform" method="post">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="examlistcurrentPage" id="examlistcurrentPage" value='<c:out value="${examlistcurrentPage}"></c:out>'>
  <input type="hidden" name="examlisttotalPages" id="examlisttotalPages" value='<c:out value="${examlisttotalPages}"></c:out>'>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname">
  </form>	
	<c:if test="${(totalPages eq 0) && (actionname eq 'viewstudentmarks')}">
	<div class="alert alert-info">Marks Not Avaialble..</div>
	</c:if>
	<c:if test="${(totalPages eq 0) && (actionname ne 'viewstudentmarks')}">
	<div class="alert alert-info" align="center">Exams not available for selected criteria.</div>
	</c:if>
</body>
</html>