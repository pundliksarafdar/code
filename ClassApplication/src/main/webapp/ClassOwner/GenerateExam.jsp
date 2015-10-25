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

var examValidationDeferred = $.Deferred();
var examNameValidationDeferred = $.Deferred();
var isNameIsAvailable = false;
$(function () {
		$('[data-toggle="tooltip"]').attr("title",$("#tooltipdata").html()); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
		
		$(EXAM_MODAL_CHOOSE_MARKS_ADD).on("click",selectMarks);
		
});

$(document).ready(function(){
	$("input,select").on("click",function(){
		$(this).parents(".col-sm-2").find(".validation-message").empty();	
	});
	
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
		console.log(isNameIsAvailable);
		if(isNameIsAvailable){
			$("#actionform").attr("action","deletequestion");
			$("#actionname").val("deletequestion");
			$("#actionform").submit();
		}
	});
	
	$("#QuestionBankQuestionListCreateExamhModalChooseMarksAdd").on("click",addQuestionCriteria);
	$(EXAM_MODAL_OK).on("click",function(){
		$("#QuestionBankQuestionListCreateExamhModalErrorBox").empty();
		if(!$("#autogenerateexamform").valid()){
			
		}else if(!isNameIsAvailable){
			$("#QuestionBankQuestionListCreateExamhModalExamName").prev(".validation-message").html("Exam name already available.Please enter different name").removeClass("hide");
		}else if($.isEmptyObject(marksObj)){
			$("#QuestionBankQuestionListCreateExamhModalErrorBox").html("<div class='alert alert-danger'>Please add atleast one question</div>").focus();
		}else{	
			var passingmarks = $(EXAM_MODAL_EXAM_PASSING_MARKS).val();
			var examName = $(EXAM_MODAL_EXAM_NAME).val();
			var examHour = $("#autogenerateexamform [name='examHour']").val();
			var examMinute = $("#autogenerateexamform [name='examMinute']").val();
			$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").find("[name='passingmarks']").val(passingmarks);
			$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").find("[name='examname']").val(examName);
			$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").find("[name='examHour']").val(examHour);
			$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").find("[name='examMinute']").val(examMinute);
			$("#QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria").submit();
		}
	});
	
	$("#QuestionBankQuestionListCreateExamhModalExamName").on("blur",function(){
		var instituteId = $("#institute").val();
		var examname = $("#QuestionBankQuestionListCreateExamhModalExamName").val().trim();
		isNameIsAvailable = checkExamNameValidation(instituteId,examname,function(){});
	}).on("click",function(){
		$("#QuestionBankQuestionListCreateExamhModalExamName").prev(".validation-message").empty().addClass("hide");
	});
	
	var validationOption = {rules: {
		passingmarks: {
			max:function(elm){
				var totalMarks = 0;
				for(var key in marksObj){
					totalMarks = totalMarks + marksObj[key]*key;
				}
				console.log(totalMarks );
				return totalMarks;
			}
		}
	  }
	};
	$("#autogenerateexamform").validate(validationOption);
	$("[data-target='#QuestionBankQuestionListCreateExamhModal']").on("click",resetForm);
});

//Object is storing the value of the table
var marksObj = {};
var addQuestionCriteria = function(){
	$(".alert").alert('close');
	var examMarks = $(this).parents("form").find("#QuestionBankQuestionListCreateExamhModalMarks").val();
	var numberOfQuestions = $(this).parents("form").find("#QuestionBankQuestionListCreateExamhModalNoOfQuestion").val();
	var sub_id = $("[name='subject']").val();
	var div_id = $("[name='division']").val();
	
	if(examMarks == "Select"){
		$("#QuestionBankQuestionListCreateExamhModalMarks").parents(".col-sm-2").find(".validation-message").text("Please select marks").removeClass("hide");
		return;
	}
	if(numberOfQuestions.trim().length==0 || numberOfQuestions.trim() == "0"){
		$("#QuestionBankQuestionListCreateExamhModalNoOfQuestion").parents(".col-sm-2").find(".validation-message").text("Please number of question").removeClass("hide");
		return;
	}
	
	var available = checkAvailibility(sub_id,div_id,examMarks,numberOfQuestions,-1);
	if(available){
		if(marksObj[examMarks]){
			showError("Question for "+examMarks+" marks are already added, Click ok to continue or cancel to cancel",examMarks,numberOfQuestions);
		}else{
			marksObj[examMarks] = numberOfQuestions;
			var tableObj = displayTable("#QuestionBankQuestionListCreateExamhModalTable",marksObj);
			$("#QuestionBankQuestionListCreateExamhModalTable").on("click","tr .remove",function(){
						var dataId = $(this).attr("data-id").trim();
						$(this).parents("tr").remove();
						delete marksObj[dataId];
						tableObj = displayTable("#QuestionBankQuestionListCreateExamhModalTable",marksObj);
						if(!$.isEmptyObject(marksObj)){
							$("#QuestionBankQuestionListCreateExamhModalTable").removeClass("hide");
						}else{
							$("#QuestionBankQuestionListCreateExamhModalTable").addClass("hide");
						}
			});
		}
	}else{
		var message = numberOfQuestions+" question of "+examMarks+" marks are not available";
		modal.launchAlert("Error",message);
	}
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
				
				if(!$.isEmptyObject(marksObj)){
					$("#QuestionBankQuestionListCreateExamhModalTable").removeClass("hide");
				}else{
					$("#QuestionBankQuestionListCreateExamhModalTable").addClass("hide");
				}
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
		if(!$.isEmptyObject(marksObj)){
			$("#QuestionBankQuestionListCreateExamhModalTable").removeClass("hide");
		}else{
			$("#QuestionBankQuestionListCreateExamhModalTable").addClass("hide");
		}
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

function checkAvailibility(sub_id,div_id,marks,count,maximumRepeatation){
	var isAvailable = true;
	$.ajax({
        url: 'classOwnerServlet',
        type: 'post',
		async:false,
        data: {
	    	 methodToCall: "validateQuestionAvailibility",
	    	 sub_id:sub_id,
			 div_id:div_id,
			 marks:marks,
			 count:count,
			 maximumRepeatation:maximumRepeatation
        },
        success: function(e){
			var responseJSON = JSON.parse(e);
			if(responseJSON.available){
				isAvailable = true;
			}else{
				isAvailable = false;
			}
        }, error: function(){
            alert('ajax failed');
        }
	});
	return isAvailable;
}

function checkExamNameValidation(institute,examname,successfunction){
	var isAvailable = false;
	$.ajax({
		
		url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "validateexamname",
	    	 examname:examname,
	    	 institute:institute
	   		},
	   async: false, 
	   type:"POST",
	   success:function(data){
		   console.log(data);
		   var resultJson=JSON.parse(data);
		   var examstatus=resultJson.examavailable;
		   if(examstatus=="true"){
				isAvailable = false;
				$("#QuestionBankQuestionListCreateExamhModalExamName").prev(".validation-message").html("Exam name already available.Please enter different name").removeClass("hide");
		   }else{
			   isAvailable = true;
		   }
	   },
	   error:function(e){
		   console.log(e);
		   isNameIsAvailable = false;
		   }
	   });
	   return isAvailable;
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
		<form class="form-horizontal corex-form-container" id="autogenerateexamform">
		<label class="control-label">
				<h4>Add Question</h4>
			</label>
		<hr/>	
		<div class="form-group">
		<label class="col-sm-2 control-label" for="QuestionBankQuestionListCreateExamhModalExamName">Exam name</label>
			<div class="col-sm-4">
				<div class="validation-message hide"></div>
				<input type="text" id="QuestionBankQuestionListCreateExamhModalExamName" class="form-control" required>
			</div>	
		
		<label class="col-sm-2 control-label" for="QuestionBankQuestionListCreateExamhModalPassingMarks1">Passing marks</label>
			<div class="col-sm-4">
				<input type="number" id="QuestionBankQuestionListCreateExamhModalPassingMarks" name="passingmarks" class="form-control" required min="0">
			</div>	
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label" for="QuestionBankQuestionListCreateExamhModalMarks">Marks</label>
				<div class="col-sm-2">
				<div class="validation-message hide"></div>
				  <select class="btn btn-default" id="QuestionBankQuestionListCreateExamhModalMarks">
					<option>Select</option>
					<c:forEach items="${marks}" var="item">
						<option value="<c:out value="${item}"></c:out>"><c:out value="${item}"></c:out></option>
					</c:forEach>
				  </select>
				</div> 
			
			
			<label class="col-sm-2 control-label" for="QuestionBankQuestionListCreateExamhModalNoOfQuestion">Number of question</label>
				<div class="col-sm-2">
					<div class="validation-message hide"></div>	
					<input type="number" id="QuestionBankQuestionListCreateExamhModalNoOfQuestion" class="form-control">
				</div>	
			
			<div class="col-sm-4">
				<div class="validation-message hide"></div>
					<input type="button" class="btn btn-default" value="Add" id="QuestionBankQuestionListCreateExamhModalChooseMarksAdd"/>
			</div>	
			
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label" for="QuestionBankQuestionListCreateExamhModalMarks">Exam Time Duration</label>
				<div class="col-sm-2">
				<div class="validation-message hide"></div>
				  <input type="number" class="form-control" placeholder="hh" name="examHour" required min="0" max="23"/>
				</div> 
			
			
				<div class="col-sm-2">
					<div class="validation-message hide"></div>	
					 <input type="number" class="form-control" placeholder="mm" name="examMinute" required min="0" max="59"/>
				</div>	
			
			
		</div>
		</form>
		<form id="QuestionBankQuestionListCreateExamhModalRandomQuestionGenerateCriteria" action="generateexamaction">
			<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
			<input type="hidden" name="passingmarks"/>
			<input type="hidden" name="examname">
			<input type="hidden" name="examHour">
			<input type="hidden" name="examMinute">
			
		<table id="QuestionBankQuestionListCreateExamhModalTable" class="table table-hover hide">
			
			<thead>
				<tr><th>&nbsp;</th><th>Marks</th><th>Count</th><th>Total</th></tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
		</form>
     <input type="button" class="btn btn-primary" id="QuestionBankQuestionListCreateExamhModalOk" value="Ok">
	</div>
	
</body>
</html>