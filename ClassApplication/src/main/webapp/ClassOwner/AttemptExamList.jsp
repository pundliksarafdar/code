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

$(".attemptExam").on("click",function(e){
	$("#examID").val($(this).prop("id"));
	/* $("#actionform").submit(); */
	var formData = $("#actionform").serialize();
	console.log(formData);
	window.open("attemptExam?"+formData,"","width=500, height=500"); 
	e.preventDefault();
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
			<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=attemptexamlist" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
	</div>
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;"><u>Attempt Exam List</u></div>
			</div>
	 <c:if test="${(examlist != null) && (totalPages gt 0)}">
	<div class="container">
  <h2><font face="cursive">Search Result</font> </h2>            
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
        <td><a href="#" class="attemptExam" id="<c:out value="${item.exam_id}" ></c:out>"><button class="btn btn-warning">Attempt</button></a></td>
        </c:when>
       <%--  <c:when test="${role eq 3 }">
        <td><a class="attemptExam" href="#" id="<c:out value="${item.exam_id}"></c:out>"><button class="btn btn-success">Enable</button></a></td>
        </c:when> --%>
        </c:choose>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <form action="attemptExam" id="actionform" method="post" target="blank">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <%-- <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'> --%>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname">
  </form>
  <form action="attemptexamlist" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
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
	<c:if test="${totalPages eq 0}">
	<div class="alert alert-info">Exams not available..</div>
	</c:if>
</body>
</html>