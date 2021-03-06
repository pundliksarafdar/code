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
	
	$(".editQuestion").on("click",function(){
		$("#questionNumber").val($(this).prop("id"));
		$("#actionform").attr("action","editquestion");
		$("#actionname").val("editquestion");
		$("#actionform").submit();
	});
	
	$(".deleteQuestion").on("click",function(){
		$("#questionNumber").val($(this).prop("id"));
		var subject=$("form#actionform #subject").val();
		var division=$("form#actionform #division").val();
		var institute=$("form#actionform #institute").val();
		var questionNumber=$("form#actionform #questionNumber").val();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "isQuestionAvailableInExam",
			    	 questionNumber:questionNumber,
			    	 subject:subject,
			    	 division:division,
			    	 institute:institute
			   		},
			   		success:function(data){
			   			var resultJson = JSON.parse(data);
						   var quesstatus=resultJson.quesstatus;
						   if(quesstatus=="Y"){
							   $("#DeleteConfirmBody").empty();
							  var examnames=resultJson.examnames.split(",");
							   $("#DeleteConfirmBody").append("This Question is present in following exams-<br>")
							   for(var i=0;i<examnames.length;i++){
								   $("#DeleteConfirmBody").append((i+1)+"."+examnames[i]+"<br>");
							   }
							   $("#DeleteConfirmBody").append("Still you delete, this question will remain in exams but will not be available in search. Once you delete that exams this question will get deleted.<br>")
						   		$("#DeleteConfirmBody").append("Do you want to continue?");
							   $("#quesstatus").val("Y");
							   $("#DeleteConfirmModal").modal("toggle");
						   }else{
							   $("#DeleteConfirmBody").empty();
							   $("#DeleteConfirmBody").append("Are you sure?");
							   $("#quesstatus").val("");
							   $("#DeleteConfirmModal").modal("toggle");
						   }
			   		},
			   		error:function(error){
			   		
			   		}
		});
		/* $("#actionform").attr("action","deletequestion");
		$("#actionname").val("deletequestion");
		$("#actionform").submit(); */
	});
	
	$("#QuestionDeleteConfirm").on("click",function(){
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
		tableObj.append('<tr class="success"><td></td><td>Total</td><td>'+totalQuestion+'</td><td>'+totalMarks+'<input type="hidden" name="totalMarks" value='+totalMarks+'></td></tr>');
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
			<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=generateexampreaction" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
			</c:otherwise>
			</c:choose>
			</div>
    
    <div class="container" id="QuestionBankQuestionListCreateExamhModal">
 	
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
     <button type="button" class="btn btn-primary" id="QuestionBankQuestionListCreateExamhModalOk">Ok</button>
	</div>
	
</body>
</html>