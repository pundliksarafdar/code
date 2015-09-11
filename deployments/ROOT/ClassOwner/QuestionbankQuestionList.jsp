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
var EXAM_MODAL_OK = EXAM_MODAL+"Ok";
var EXAM_MODAL_EXAM_NAME = EXAM_MODAL+"ExamName";
var EXAM_MODAL_EXAM_PASSING_MARKS = EXAM_MODAL + "PassingMarks";
$(function () {
		$('[data-toggle="tooltip"]').attr("title",$("#tooltipdata").html()); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
		
		$(EXAM_MODAL_CHOOSE_MARKS_ADD).on("click",selectMarks);
		
});

$(document).ready(function(){
	
	$("#uploadexams").on("click",function(){
		$("#uploadform").attr("action","uploadexams");
		$("#uploadform").submit();
	});
	
	$("#search").on("click",function(){
		$("#searchform").submit();
	});
	
	$(".page").on("click",function(){
		$("form#paginateform #currentPage").val($(this).text());
		$("#paginateform").submit();
	});
	
	$(".start").on("click",function(){
		$("form#paginateform #currentPage").val("1");
		$("#paginateform").submit();
	});
	
	$(".end").on("click",function(){
		$("form#paginateform #currentPage").val($("#totalPages").val());
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
	
	$("#QuestionBankQuestionListCreateExamhModalChooseMarksAdd").on("click",addQuestionCriteria);
	$(EXAM_MODAL_OK).on("click",function(){
		var passingmarks = $(EXAM_MODAL_EXAM_PASSING_MARKS).val();
		var examName = $(EXAM_MODAL_EXAM_NAME).val();
		$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").find("[name='passingmarks']").val(passingmarks);
		$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").find("[name='examname']").val(examName);
		$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").submit();
	});
	$("[data-target='#QuestionBankQuestionListCreateExamhModal']").on("click",resetForm);
});

//Object is storing the value of the table
var marksObj = {};
var addQuestionCriteria = function(){
	$(".alert").alert('close');
	var examMarks = $(this).parents("form").find("#QuestionBankQuestionListCreateExamhModalMarks").val();
	var numberOfQuestions = $(this).parents("form").find("#QuestionBankQuestionListCreateExamhModalNoOfQuestion").val();
	
	if(marksObj[examMarks]){
		showError("Question for "+examMarks+" marks are already added, Click ok to continue or cancel to cancel",examMarks,numberOfQuestions);
	}else{
		marksObj[examMarks] = numberOfQuestions;
	}	
	
	var tableObj = displayTable("#QuestionBankQuestionListCreateExamhModalTable",marksObj);
	tableObj.find("tr").find(".remove").off("click").on("click",function(){
				var dataId = $(this).attr("data-id").trim();
				$(this).parents("tr").remove();
				delete marksObj[dataId];
				tableObj = displayTable("#QuestionBankQuestionListCreateExamhModalTable",marksObj);
	});
}

function showError(message,examMarks,numberOfQuestions){
	errorMessage = $('<div class="alert alert-warning alert-dismissible fade in" role="alert">'+
		  '<div id="QuestionBankQuestionListCreateExamhModalAlertMessage"></div>'+
		  '<input type="button" class="btn btn-success btn-xs" value="Ok" data-dismiss="alert" />'+
		  '<input type="button" class="btn btn-default btn-xs" data-dismiss="alert" value="Cancel"/>'+
		  
		'</div>');	
		errorMessage.find("#QuestionBankQuestionListCreateExamhModalAlertMessage").html(message);
		
		$("#QuestionBankQuestionListCreateExamhModalErrorBox").html(errorMessage);
		errorMessage.find(".btn-success").off("click").on("click",function(){
			marksObj[examMarks]	= numberOfQuestions;
			var tableObj = displayTable("#QuestionBankQuestionListCreateExamhModalTable",marksObj);
			
			tableObj.find("tr").find(".remove").on("click",function(){
				var dataId = $(this).attr("data-id").trim();
				$(this).parents("tr").remove();
				delete marksObj[dataId];
				tableObj = displayTable("#QuestionBankQuestionListCreateExamhModalTable",marksObj);
			});
		});
}


function displayTable(tableId,rowObj){
	var tableObj = $(tableId).find("tbody");	
		tableObj.empty();
		var totalMarks = 0;
		var totalQuestion = 0;
		for(key in rowObj){
			tableObj.append('<tr><td class="remove glyphicon glyphicon-trash" data-id="'+key+'"><input type="hidden" value="'+rowObj[key]+'" name="questioncount"><input type="hidden" value="'+key+'" name="marks"></td><td>'+key+'</td><td>'+rowObj[key]+'</td><td>'+rowObj[key]*key+'</td></tr>');
			totalMarks += parseInt(key)*parseInt(rowObj[key]);
			totalQuestion += parseInt(rowObj[key]);
		}	
		tableObj.append('<tr class="success"><td></td><td>Total</td><td>'+totalQuestion+'</td><td>'+totalMarks+'</td></tr>');
		return tableObj;
}

var selectMarks = function(){
	
};

function resetForm(){
	//Reset marks object
	marksObj = {};
	$("#QuestionBankQuestionListCreateExamhModal").find("form")[0].reset();
	var tableObj = $("#QuestionBankQuestionListCreateExamhModalTable").find("tbody");	
		tableObj.empty();
}
</script>
</head>
<body>
			<div class="container" style="margin-bottom: 5px">
			<c:choose>
			<c:when test="${institute ne null }">
			<a type="button" class="btn btn-primary" href="teachercommoncomponent?forwardAction=searchQuestion" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
			</c:when>
			<c:otherwise>
			<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=listquestionbankquestionaction" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
			</c:otherwise>
			</c:choose>
			</div>
			<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div class="btn-group" role="group" aria-label="..." style="width:90%">
			<form action="" id="uploadform" method="post">
			<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			
			  <button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" >Summary</button>
			  <a type="button" class="btn btn-default" id="uploadexams">Add new Question</a>
			  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#QuestionBankQuestionListQuestionSearchModal">Search</button>
			  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#QuestionBankQuestionListCreateExamhModal">Create Exam</button>
			</form>
			</div>
			<c:if test="${(questionDataList != null) && (totalPages!=0)}">
	<div class="container">
  <h2><font face="cursive">Search Result</font> </h2>            
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Question</th>
        <th>Edit</th>
        <th>Delete</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${questionDataList}" var="item" varStatus="counter">
    <tr>
    	<c:if test="${currentPage eq 1}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td><c:out value="${counter.count + ((currentPage-1)*10)}"></c:out></td>
        </c:if>
        <td><c:out value="${item.question}"></c:out></td>
        <td><a class="edit" href="#" id="<c:out value="${item.questionNumber}"></c:out>"><button class="btn btn-primary">Edit</button></a></td>
        <td><a class="delete" href="#" id="<c:out value="${item.questionNumber}"></c:out>"><button class="btn btn-danger">Delete</button></a></td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <form action="" id="actionform" method="post">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
  <input type="hidden" name="questionNumber" id="questionNumber" value='<c:out value="${questionNumber}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname">
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
  </form>
  <form action="paginateQuestion" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
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
	</c:if>	

	
	
<div class="modal fade" id="QuestionBankQuestionListQuestionSearchModal">
 	<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
          <h4 class="modal-title" id="myModalLabel">Search</h4>
        </div>
        <div class="modal-body" >
		<form action="searchQuestion" id="searchform">
		<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
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
			<option>Select</option>
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

    
    <div class="modal fade" id="QuestionBankQuestionListCreateExamhModal">
 	<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
          <h4 class="modal-title" id="myModalLabel">Search</h4>
        </div>
        <div class="modal-body" >
		<div id="QuestionBankQuestionListCreateExamhModalErrorBox">
			
		</div>
		<form>
		<div class="form-group">
		<label for="QuestionBankQuestionListCreateExamhModalNoOfQuestion">Exam name</label>	<br/>
		  <input type="text" id="QuestionBankQuestionListCreateExamhModalExamName" class="form-control">
		</div>
		<div class="form-group">
		<label for="QuestionBankQuestionListCreateExamhModalNoOfQuestion">Passing marks</label>	<br/>
		  <input type="number" id="QuestionBankQuestionListCreateExamhModalPassingMarks" class="form-control">
		</div>
		
		<div class="row">
			<div class="col-xs-4">
			<div class="form-group">
			<label for="QuestionBankQuestionListCreateExamhModalMarks">Marks</label>	<br/>
			  <select class="btn btn-default" id="QuestionBankQuestionListCreateExamhModalMarks">
				<option>Select</option>
				<c:forEach items="${marks}" var="item">
					<option value="<c:out value="${item}"></c:out>"><c:out value="${item}"></c:out></option>
				</c:forEach>
			  </select>
			</div>
			</div>
			
			<div class="col-xs-4">
			<div class="form-group">
			<label for="QuestionBankQuestionListCreateExamhModalNoOfQuestion">Number of question</label>	<br/>
			  <input type="number" id="QuestionBankQuestionListCreateExamhModalNoOfQuestion" class="form-control">
			</div>
			</div>
			
			<div class="col-xs-4">
			<div class="form-group">
			<label for="QuestionBankQuestionListCreateExamhModalChooseMarksAdd">&nbsp;</label>	<br/>
			  <input type="button" class="btn btn-default" value="Add" id="QuestionBankQuestionListCreateExamhModalChooseMarksAdd"/>
			</div>
			</div>  
		</div>
		</form>
		<form id="QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria" action="generateexamaction">
			<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="passingmarks"/>
			<input type="hidden" name="examname">
		<table id="QuestionBankQuestionListCreateExamhModalTable" class="table table-hover">
			
			<thead>
				<tr><th></th><th>Marks</th><th>Count</th></tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
		</form>
        </div>

      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" id="QuestionBankQuestionListCreateExamhModalClose" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="QuestionBankQuestionListCreateExamhModalOk">Ok</button>
      	</div>

    </div>
	</div>
	</div>

</body>
</html>