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
	
	$(".page").on("click",function(){
		$("form#paginateform #currentPage").val($(this).text());
		$("#totalmarks").val($("#temptotalmarks").html());
		$("#addedIds").val(addedIds);
		$("#removedIds").val(removedIds);
		$("#paginateform").submit();
	});
	
	$(".start").on("click",function(){
		$("form#paginateform #currentPage").val("1");
		$("#totalmarks").val($("#temptotalmarks").html());
		$("#addedIds").val(addedIds);
		$("#removedIds").val(removedIds);
		$("#paginateform").submit();
	});
	
	$(".end").on("click",function(){
		$("form#paginateform #currentPage").val($("#totalPages").val());
		$("#totalmarks").val($("#temptotalmarks").html());
		$("#addedIds").val(addedIds);
		$("#removedIds").val(removedIds);
		$("#paginateform").submit();
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
			<a type="button" class="btn btn-primary" href="teachercommoncomponent?forwardAction=manualexam" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
			</c:when>
			<c:otherwise><a type="button" class="btn btn-primary" href="choosesubject?forwardAction=manualexam" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a></c:otherwise>
			</c:choose>
			</div>
			<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div class="btn-group" role="group" aria-label="..." style="width:90%">
			<form action="" id="uploadform" method="post">
			<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
			  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#QuestionBankQuestionListQuestionSearchModal">Advance Search</button>
			  <button type="button" class="btn btn-default" id="createexam">Create Exam</button>
			  <c:if test="${actionname ne 'showaddedquestions'}">
			  <button type="button" class="btn btn-default" id="showaddedquestion">Show All Added Questions</button>
			 </c:if>
			 <c:if test="${(actionname eq 'showaddedquestions') || (actionname eq 'createexam') }">
			  <button type="button" class="btn btn-default" id="backtoquestions">Back To Questions</button>
			 </c:if>	
			  Total Marks:-<span class="badge" id="temptotalmarks" name="temptotalmarks"><c:out value="${totalmarks}"></c:out></span><br>
			  <c:if test='${actionname eq "advancesearch"}'>
			   <button type="button" class="btn btn-default" id="clearadvancesearch">Clear Advance Search Criteria <span class="badge">x</span></button>
			  </c:if>
			</form>
			</div>
			</div>
			<c:if test="${(questionDataList != null) && (totalPages!=0)}">
	<div class="container">
  <h2><font face="cursive">Search Result</font> </h2>            
  <table class="table table-striped" id="basetable">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Question</th>
        <th>Marks</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${questionDataList}" var="item" varStatus="counter">
    <tr>
    	<c:if test="${actionname eq 'showaddedquestions'}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
    	<c:if test="${currentPage eq 1}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td><c:out value="${counter.count + ((currentPage-1)*10)}"></c:out></td>
        </c:if>
        <td><c:out value="${item.question}"></c:out></td>
        <td><span class="badge" id='mark<c:out value="${item.questionNumber}"></c:out>'><c:out value="${item.marks}"></span></c:out></td>
        <c:choose>
        <c:when test="${(questionIds != null)}">
        <c:if test="${questionIds[(counter.count-1)] eq 1 }">
        <td><a class="removequestion" href="#" id="<c:out value="${item.questionNumber}"></c:out>"><button class="btn btn-danger">Remove</button></a></td>
        </c:if>
		<c:if test="${questionIds[(counter.count-1)] eq 0 }">
        <td><a class="addquestion" href="#" id="<c:out value="${item.questionNumber}"></c:out>"><button class="btn btn-success">Add</button></a></td>
        </c:if>
          	</c:when>
          	<c:when test="${actionname eq 'showaddedquestions'}">
          	<td><a class="removequestion" href="#" id="<c:out value="${item.questionNumber}"></c:out>"><button class="btn btn-danger">Remove</button></a></td>
          	</c:when>
      	<c:otherwise>
      	<td><a class="addquestion" href="#" id="<c:out value="${item.questionNumber}"></c:out>"><button class="btn btn-success">Add</button></a></td>
      	</c:otherwise>
      	</c:choose>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <form action="manualexam" id="autosubmitform" method="post">
  <input type="hidden" name="forwardhref" id="forwardhref">
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
  <input type="hidden" name="actionname" id="actionname" value="autosubmit">
  </form>
  <form action="manualexam" id="paginateform">
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
   <input type="hidden" name="actionname" id="actionname" value='<c:out value="${actionname}"></c:out>'>
   <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
   <c:if test="${actionname ne 'showaddedquestions'}">
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

	
	
<div class="modal fade" id="QuestionBankQuestionListQuestionSearchModal">
 	<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
          <h4 class="modal-title" id="myModalLabel">Search</h4>
        </div>
        <div class="modal-body" >
		<form action="manualexam" id="searchform">
		<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="actionname" value="advancesearch">
			 <input type="hidden" name="addedIds" id="addedIds" value="">
 			 <input type="hidden" name="removedIds" id="removedIds" value="">
 			 <input type="hidden" id="totalmarks" name="totalmarks">
 			 <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
		<div class="form-group">
		<label for="exampleInputEmail1">Search word</label>
          <input type="text" placeholder="Search word" class="form-control"/>
		</div>

		<div class="form-group">
		<label for="">Choose Question occured in</label>	
		<select class="btn btn-default" name="selectedExamID"> 
			<option value="-1">Select</option>
			<c:forEach items="${compExams}" var="item">
    			<option value="'<c:out value="${item.compexam_id}"></c:out>'"><c:out value="${item.compexam_name}"></c:out></option>
			</c:forEach>
		</select>
		</div>

		<div class="form-group">
		<label for="-1">Choose Marks</label>	
		  <select  class="btn btn-default" name="selectedMarks">
			<option value="-1">Select</option>
			<c:forEach items="${marks}" var="item">
    			<option value="<c:out value="${item}"></c:out>"><c:out value="${item}"></c:out></option>
			</c:forEach>
		  </select>
		</div>

		<div class="form-group">
		<label for="">Choose repetetion</label>	
		  <select class="btn btn-default" name="selectedRep">
			<option value="-1">Select</option>
			<c:forEach items="${repeatation}" var="item">
    			<option value="<c:out value="${item}"></c:out>"><c:out value="${item}"></c:out></option>
			</c:forEach>
		  </select>
		</div>  
		</form>
        </div>

      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="search">Ok</button>
      	</div>

    </div>
</div>
</div>

	
    <c:if test="${actionname eq 'createexam' }">
    <form role="form" action="manualexam">
    <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="actionname" value="submitexam">
  <div class="form-group">
    <label for="examname">Enter Exam Name:</label>
    <input type="text" class="form-control" id="examname" name="examname">
  </div>
  <div class="form-group">
    <label for="totalquestions">Total Questions:</label>
    <input type="text" class="form-control" id="noofquestions" name="noofquestions" value=<c:out value="${noofquestions}"></c:out> disabled="disabled">
  </div>
  <div class="form-group">
    <label for="totalmarks">Total Marks:</label>
    <input type="text" class="form-control" id="totalmarks" name="totalmarks" value=<c:out value="${totalmarks}"></c:out> disabled="disabled">
  </div>
   <div class="form-group">
    <label for="totalmarks">Enter Passing Marks:</label>
    <input type="text" class="form-control" id="passmarks" name="passmarks">
  </div>
  <button type="submit" class="btn btn-default">Submit</button>
</form>
<form action="manualexam" id="paginateform">
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
   <input type="hidden" name="actionname" id="actionname" value='<c:out value="${actionname}"></c:out>'>
   <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
</form>
</c:if>
</body>
</html>