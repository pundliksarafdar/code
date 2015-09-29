<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script>

var ROOT = "#QuestionBankQuestionList";
var EXAM_MODAL = ROOT +"CreateExamhModal";
var EXAM_MODAL_CHOOSE_MARKS_ADD = ROOT+"ChooseMarksAdd";

$(function () {
		$('[data-toggle="tooltip"]').attr("title",$("#tooltipdata").html()); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
		
		$(EXAM_MODAL_CHOOSE_MARKS_ADD).on("click",selectMarks);
		
});

$(document).ready(function(){
	var addedIds=[];
	var removedIds=[];
	var addedIndex=0;
	var removedIndex=0;
	$("#uploadexams").on("click",function(){
		$("#uploadform").attr("action","uploadexams");
		$("#uploadform").submit();
	});
	
	$("#search").on("click",function(){
		$("#searchform #totalmarks").val($("#temptotalmarks").html());
		$("#searchform #addedIds").val(addedIds);
		$("#searchform #removedIds").val(removedIds);
		$("#searchform").submit();
	});
	
	$(".page").on("click",function(e){
		$("form#paginateform #currentPage").val($(this).text());
		$("#totalmarks").val($("#temptotalmarks").html());
		$("#addedIds").val(addedIds);
		$("#removedIds").val(removedIds);
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".start").on("click",function(e){
		$("form#paginateform #currentPage").val("1");
		$("#totalmarks").val($("#temptotalmarks").html());
		$("#addedIds").val(addedIds);
		$("#removedIds").val(removedIds);
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".end").on("click",function(e){
		$("form#paginateform #currentPage").val($("#totalPages").val());
		$("#totalmarks").val($("#temptotalmarks").html());
		$("#addedIds").val(addedIds);
		$("#removedIds").val(removedIds);
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".edit").on("click",function(){
		$("#questionNumber").val($(this).prop("id"));
		$("#actionform").attr("action","editquestion");
		$("#actionname").val("editquestion");
		$("#actionform").submit();
	});
	
	$(".delete").on("click",function(){
		$("#questionNumber").val($(this).prop("id"));
		$("#actionform").attr("action","deletequestion");
		$("#actionname").val("deletequestion");
		$("#actionform").submit();
	});
	
	$("#basetable").on("click",".addquestion",function(){
	//	$("#addedIds").val($("#addedIds").val()+","+$(this).prop("id"));
		addedIds.push($(this).prop("id"));
		removedIds.splice(removedIds.indexOf($(this).prop("id")), 1);
		$(this).find("button").text("Remove");
		$(this).find("button").prop("class","btn btn-danger");
		$("#temptotalmarks").html(parseInt($("#temptotalmarks").html()) + parseInt($("#mark"+$(this).prop("id")).html()));
		$(this).prop("class","removequestion");
	});
	$("#basetable").on("click",".removequestion",function(){
	//	$("#removedIds").val($("#removedIds").val()+","+$(this).prop("id"));
		removedIds.push($(this).prop("id"));
		addedIds.splice(addedIds.indexOf($(this).prop("id")), 1);
		$(this).find("button").text("Add");
		$(this).find("button").prop("class","btn btn-success");
		$("#temptotalmarks").html(parseInt($("#temptotalmarks").html()) - parseInt($("#mark"+$(this).prop("id")).html()));
		$(this).prop("class","addquestion");
	});
	$("#clearadvancesearch").on("click",function(){
		$("form#paginateform #currentPage").val("0");
		$("form#paginateform #totalmarks").val($("#temptotalmarks").html());
		$("form#paginateform #addedIds").val(addedIds);
		$("form#paginateform #removedIds").val(removedIds);
		$("form#paginateform #searchedMarks").val("-1");
		$("form#paginateform #searchedExam").val("-1");
		$("form#paginateform #searchedRep").val("-1");
		$("form#paginateform #actionname").val("");
		$("#paginateform").submit();    
		
	});
	$("#showaddedquestion").on("click",function(){
		$("form#paginateform #currentPage").val("0");
		$("form#paginateform #totalmarks").val($("#temptotalmarks").html());
		$("form#paginateform #addedIds").val(addedIds);
		$("form#paginateform #removedIds").val(removedIds);
		$("form#paginateform #searchedMarks").val("-1");
		$("form#paginateform #searchedExam").val("-1");
		$("form#paginateform #searchedRep").val("-1");
		$("form#paginateform #actionname").val("showaddedquestions");
		$("#paginateform").submit();    
		
	}); 
	$("#backtoquestions").on("click",function(){
		$("form#paginateform #currentPage").val("0");
		$("form#paginateform #totalmarks").val($("#temptotalmarks").html());
		$("form#paginateform #addedIds").val(addedIds);
		$("form#paginateform #removedIds").val(removedIds);
		$("form#paginateform #searchedMarks").val("-1");
		$("form#paginateform #searchedExam").val("-1");
		$("form#paginateform #searchedRep").val("-1");
		$("form#paginateform #actionname").val("");
		$("#paginateform").submit();    
		
	});
	
	$("#createexam").on("click",function(){
		$("form#paginateform #currentPage").val("0");
		$("form#paginateform #totalmarks").val($("#temptotalmarks").html());
		$("form#paginateform #addedIds").val(addedIds);
		$("form#paginateform #removedIds").val(removedIds);
		$("form#paginateform #searchedMarks").val("-1");
		$("form#paginateform #searchedExam").val("-1");
		$("form#paginateform #searchedRep").val("-1");
		$("form#paginateform #actionname").val("createexam");
		$("#paginateform").submit();    
		
	});
	
	$("#cancelEdit").on("click",function(){
		$("form#paginateform #actionname").val("viewcomplete");
		$("#paginateform").submit();
	});
	
	$(".menuoptions").on("click",function(e){
		var confirmresult=confirm("If you press OK your exam data will get lost.Do you want to continue?");
		if(confirmresult==false){
			e.preventDefault()
		}else{
			$("#autosubmitform #forwardhref").val($(this).prop("href"));
			$("#autosubmitform").submit();
		}
	});
});

var selectMarks = function(){
	
};
</script>
</head>
<body>
			
			<div class="container" style="margin-bottom: 5px">
			<c:choose>
			<c:when test="${institute ne null }">
			<a type="button" class="btn btn-primary" href="#" id="cancelEdit" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Back To Exam List</a>
			</c:when>
			<c:otherwise><a type="button" class="btn btn-primary" href="#" id="cancelEdit"><span class="glyphicon glyphicon-circle-arrow-left"></span> Back To Exam List</a></c:otherwise>
			</c:choose>
			</div>
			<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;"><u>View Exam</u></div>
			<div class="btn-group" role="group" aria-label="..." style="width:90%">
			<form action="" id="uploadform" method="post">
			<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
			<input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
			<input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
			<input type="hidden" name="examname" value="<c:out value="${examname}"></c:out>"/>
			<div  class="col-md-6" align="left">
			Exam Name:- <c:out value="${examname}"></c:out>	
			</div>
			<div class="col-md-6" align="right">
			Total Marks:-<span class="badge" id="temptotalmarks" name="temptotalmarks"><c:out value="${totalmarks}"></c:out></span><br>
			  </div>
			</form>
			</div>
			</div>
			<c:if test="${(questionDataList != null)}">
	<div class="container">
  <h4><font face="cursive">Exam Questions</font> </h4>            
  <table class="table table-striped" id="basetable">
    <tbody>
    <c:forEach items="${questionDataList}" var="item" varStatus="counter">
    <tr>
    	<c:if test="${currentPage eq 1}">
        <td  class="col-md-1"><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td  class="col-md-1"><c:out value="${counter.count + ((currentPage-1)*10)}"></c:out></td>
        </c:if>
        <td  class="col-md-10"><c:out value="${item.question}"></c:out></td>
        <td  class="col-md-1"><span class="badge" id='mark<c:out value="${item.questionNumber}"></c:out>'><c:out value="${item.marks}"></span></c:out></td>
      </tr>
      <tr>
      <td  class="col-md-1"></td>
      <td  class="col-md-10">
      <c:forEach items="${item.options}" var="option" varStatus="innercounter">
      <c:out value="${innercounter.count}"></c:out>.&nbsp; &nbsp;<c:out value="${option}"></c:out><br>
      </c:forEach>
      </td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <form action="viewexam" id="autosubmitform" method="post">
  <input type="hidden" name="forwardhref" id="forwardhref">
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
  <input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
  <input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
  <input type="hidden" name="actionname" id="actionname" value="autosubmit">
  </form>
  <form action="viewexam" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
  <input type="hidden" name="addedIds" id="addedIds" value="">
  <input type="hidden" name="removedIds" id="removedIds" value="">
  <input type="hidden" id="totalmarks" name="totalmarks">
   <input type="hidden" name="actionname" id="actionname">
   <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
   <input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
   <input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
   <input type="hidden" name="examname" value="<c:out value="${examname}"></c:out>"/>
   <c:if test="${(actionname ne 'showaddedquestions') && (actionname ne 'editexam')}">
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
</c:if>
</form>
</div>
	</c:if>	



</body>
</html>