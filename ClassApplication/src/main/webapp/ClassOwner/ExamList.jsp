<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
.popover-content {
  height:300px;
  overflow-y: auto;
  overflow-x: hidden;
 }
</style>
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
	
	$("#examBaseDiv").on("click",".disableExam",function(e){
		examID=$(this).prop("id");
		subject=$("form#paginateform #subject").val();
		division=$("form#paginateform #division").val();
		that=$(this);	
		e.preventDefault();
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
			   			$(that).off();
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
		subject=$("form#paginateform #subject").val();
		division=$("form#paginateform #division").val();
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
			   			$(that).off();
			   			$(that).find("button").prop("class","btn btn-warning");
			   			$(that).prop("class","disableExam");
			   			$("#"+$(that).prop("id")+"edit").text("View");
			   			$("#"+$(that).prop("id")+"edit").parent().off();
			   			$("#"+$(that).prop("id")+"edit").parent().prop("class","viewExam");
			   		},
			   		error:function(error){
			   		
			   		}
		});
	});
	
	$("#enableexamOK").on("click",function(){
		$(that).find("button").text("Enabling...");
		var starttime=$("#starttime").val();
		var endtime=$("#endtime").val();
		subject=$("form#paginateform #subject").val();
		division=$("form#paginateform #division").val();
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
			   			$(that).off();
			   			$(that).find("button").prop("class","btn btn-warning");
			   			$(that).prop("class","disableExam");
			   			$("#"+$(that).prop("id")+"edit").text("View");
			   			$("#"+$(that).prop("id")+"edit").parent().off();
			   			$("#"+$(that).prop("id")+"edit").parent().prop("class","viewExam");
			   			$("#"+$(that).prop("id")+"status").val("Y");
			   		},
			   		error:function(error){
			   		
			   		}
		});
	});
	
$("#examBaseDiv").on("click",".enableExam",function(e){
	var exam_status=$("#"+$(this).prop("id")+"status").val();
	examID=$(this).prop("id");
	that=$(this); 
	var statusflag=false;
	if(exam_status=="E"){
		$("#enableexamcontinuemodal").modal("toggle");
	}else{
	$("#enableexammodal").modal({
	    backdrop: 'static',
	    keyboard: false
	});
	subject=$("form#paginateform #subject").val();
	division=$("form#paginateform #division").val();
	}
	e.preventDefault();
});

$("#enablecontinue").on("click",function(){
	$("#enableexammodal").modal({
	    backdrop: 'static',
	    keyboard: false
	});
	subject=$("form#paginateform #subject").val();
	division=$("form#paginateform #division").val();
});

$("#examBaseDiv").on("click",".editExam",function(e){
	$("form#ViewEditform #actionname").val("editexam");
	$("form#ViewEditform #examID").val($(this).prop("id"));
	$("#ViewEditform").prop("action","editexam")
	$("#ViewEditform").submit();
	e.preventDefault();
});

$("#examBaseDiv").on("click",".viewExam",function(e){
	$("form#ViewEditform #actionname").val("viewexam");
	$("form#ViewEditform #examID").val($(this).prop("id"));
	$("#ViewEditform").prop("action","viewexam")
	$("#ViewEditform").submit();
	e.preventDefault();
});

$(".deleteExam").on("click",function(e){
	$("#examID").val($(this).prop("id"));
	$("#actionform").submit();
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
var popoverTitle = "<div><b>Re-evaluate</b> <button class='btn btn-success btn-xs pull-right'>All<i class='glyphicon glyphicon-refresh'></i></button></div>";
var option = {content:$("#examListReevaluateExams").html(),html:true,title:popoverTitle};
$('#example').popover(option);
});
</script>
</head>
<body>
<div class="container" style="margin-bottom: 5px">
	 <c:if test="${(role eq 1 || role eq 0)}">
	<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=listExam" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
	</c:if>
	 <c:if test="${(role eq 2)}">
	 <a type="button" class="btn btn-primary" href="teachercommoncomponent?forwardAction=listExam" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
	 </c:if>
	</div>
	<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	<div align="center" style="font-size: larger;"><u>Exam Search</u></div>
	</div>
	<a id="example" href="#" class="btn btn-danger pull-right" data-toggle="popover" data-placement="bottom" 
		title=""  role="button" >Re-evaluate</a>
	<div id="examListReevaluateExams" style="display:none">
		<div>
		<table class="table">
		<tr>
			<td>Exammmmmmmmm need reevaluation</td>
			<td><button id="examList0" class="btn btn-xs btn-danger" type="button">&nbsp;<i class="glyphicon glyphicon-unchecked"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluating</td>
			<td><button id="examList0" class="btn btn-xs btn-warning" type="button">&nbsp;<i class="glyphicon glyphicon-edit"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluation done</td>
			<td><button id="examList0" class="btn btn-xs btn-success" type="button">&nbsp;<i class="glyphicon glyphicon-check"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm need reevaluation</td>
			<td><button id="examList0" class="btn btn-xs btn-danger" type="button">&nbsp;<i class="glyphicon glyphicon-unchecked"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluating</td>
			<td><button id="examList0" class="btn btn-xs btn-warning" type="button">&nbsp;<i class="glyphicon glyphicon-edit"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluation done</td>
			<td><button id="examList0" class="btn btn-xs btn-success" type="button">&nbsp;<i class="glyphicon glyphicon-check"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm need reevaluation</td>
			<td><button id="examList0" class="btn btn-xs btn-danger" type="button">&nbsp;<i class="glyphicon glyphicon-unchecked"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluating</td>
			<td><button id="examList0" class="btn btn-xs btn-warning" type="button">&nbsp;<i class="glyphicon glyphicon-edit"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluation done</td>
			<td><button id="examList0" class="btn btn-xs btn-success" type="button">&nbsp;<i class="glyphicon glyphicon-check"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm need reevaluation</td>
			<td><button id="examList0" class="btn btn-xs btn-danger" type="button">&nbsp;<i class="glyphicon glyphicon-unchecked"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluating</td>
			<td><button id="examList0" class="btn btn-xs btn-warning" type="button">&nbsp;<i class="glyphicon glyphicon-edit"></i></button></td>
		</tr>
		<tr>
			<td>Exammmmmmmmm2 reevaluation done</td>
			<td><button id="examList0" class="btn btn-xs btn-success" type="button">&nbsp;<i class="glyphicon glyphicon-check"></i></button></td>
		</tr>
		<tr><td></td><td></td></tr>
		</table>
		</div>
	</div>
	 <c:if test="${(examlist != null) && (totalPages!=0)}">
	<div class="container" id="examBaseDiv">
  <h2><font face="cursive">Search Result</font> </h2>
              
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Exam Name</th>
         <c:if test="${(role eq 1 || role eq 0)}">
        <th>Enable/Disable</th>
        </c:if>
        <th>Edit/View</th>
         <c:if test="${(role eq 1 || role eq 0)}">
        <th>Delete</th>
        </c:if>
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
        <c:if test="${(item.exam_status eq 'Y') &&  (role eq 1 || role eq 0) }">
        <td><a href="#" class="disableExam" id="<c:out value="${item.exam_id}" ></c:out>"><button class="btn btn-warning">Disable</button></a></td>
        </c:if>
        <c:if test="${(item.exam_status eq 'N' || item.exam_status eq 'E') &&  (role eq 1 || role eq 0)}">
        <td><a class="enableExam" href="#" id="<c:out value="${item.exam_id}"></c:out>"><button class="btn btn-success">Enable</button></a>
        	<input type="hidden" id="<c:out value="${item.exam_id}"></c:out>status" value="<c:out value="${item.exam_status}"></c:out>"> 
        </td>
        </c:if>
         <c:if test="${item.exam_status eq 'E' && (role eq 0 || role eq 1 || item.created_by eq user.regId)}">
        <td><a href="#" class="editExam" id="<c:out value="${item.exam_id}" ></c:out>"><button class="btn btn-primary" id="<c:out value="${item.exam_id}" ></c:out>edit">Edit</button></a></td>
        </c:if>
         <c:if test="${item.exam_status ne 'E' || (role ne 0 && role ne 1 && item.created_by ne user.regId)}">
        <td><a href="#" class="viewExam" id="<c:out value="${item.exam_id}" ></c:out>"><button class="btn btn-primary">View</button></a></td>
        </c:if>
        <c:if test="${(role eq 1 || role eq 0)}">
        <td><a class="deleteExam" href="#" id="<c:out value="${item.exam_id}"></c:out>"><button class="btn btn-danger">Delete</button></a></td>
      	</c:if>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <form action="deleteExam" id="actionform" method="post">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
  <input type="hidden" name="actionname" id="actionname">
  </form>
  
  <form action="" id="ViewEditform" method="post">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="searchcurrentPage" id="searchcurrentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="searchtotalPages" id="searchcurrentPage" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname">
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
  </form>
  
  <form action="listExam" id="paginateform">
  <input type="hidden" name="subject" id="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" id="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
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
 <div class="modal fade" id="enableexammodal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Enable Exam
            </h4>
         </div>
         <div class="modal-body">
          Do you want to enable the exam?
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-dismiss="modal" id="enableOK">Yes</button>
         </div>
         </div>
   </div>
</div> 

<div class="modal fade" id="enableexamcontinuemodal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Enable Exam
            </h4>
         </div>
         <div class="modal-body">
           Once you enable this exam you will not able edit it. Do you want to continue?
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-dismiss="modal" id="enableexamOK">Continue</button>
         </div>
         </div>
   </div>
</div>
	</c:if>	
	<c:if test="${(totalPages==0)}">
<div class="alert alert-info" align="center">Exams not available for selected criteria.</div>
</c:if>
</body>
</html>