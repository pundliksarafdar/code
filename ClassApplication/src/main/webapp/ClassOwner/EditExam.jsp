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
	
	var editExam = new EditExam();
	//editExam.getBatchFromDivisonNSubject();
	
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
	
	$("#basetable").on("click",".addquestion",function(e){
	//	$("#addedIds").val($("#addedIds").val()+","+$(this).prop("id"));
		addedIds.push($(this).prop("id"));
		removedIds.splice(removedIds.indexOf($(this).prop("id")), 1);
		$(this).find("button").text("Remove");
		$(this).find("button").prop("class","btn btn-danger");
		$("#temptotalmarks").html(parseInt($("#temptotalmarks").html()) + parseInt($("#mark"+$(this).prop("id")).html()));
		$(this).prop("class","removequestion");
		e.preventDefault();
	});
	$("#basetable").on("click",".removequestion",function(e){
	//	$("#removedIds").val($("#removedIds").val()+","+$(this).prop("id"));
		removedIds.push($(this).prop("id"));
		addedIds.splice(addedIds.indexOf($(this).prop("id")), 1);
		$(this).find("button").text("Add");
		$(this).find("button").prop("class","btn btn-success");
		$("#temptotalmarks").html(parseInt($("#temptotalmarks").html()) - parseInt($("#mark"+$(this).prop("id")).html()));
		$(this).prop("class","addquestion");
		e.preventDefault();
	});
	$("#clearadvancesearch").on("click",function(){
		$("form#paginateform #currentPage").val("0");
		$("form#paginateform #totalmarks").val($("#temptotalmarks").html());
		$("form#paginateform #addedIds").val(addedIds);
		$("form#paginateform #removedIds").val(removedIds);
		$("form#paginateform #searchedMarks").val("-1");
		$("form#paginateform #searchedExam").val("-1");
		$("form#paginateform #searchedRep").val("-1");
		$("form#paginateform #searchedTopic").val("-1");
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
		$("form#paginateform #searchedTopic").val("-1");
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
		$("form#paginateform #searchedTopic").val("-1");
		$("form#paginateform #actionname").val("");
		$("#paginateform").submit();    
		
	});
	
$("#createexam").on("click",function(){
		
		var noofquestions=$("#noofquestions").val();
		if(noofquestions=="0" && addedIds.length==0){
			alert("please add questions");
		}else{
			$("form#paginateform #currentPage").val("0");
			$("form#paginateform #totalmarks").val($("#temptotalmarks").html());
			$("form#paginateform #addedIds").val(addedIds);
			$("form#paginateform #removedIds").val(removedIds);
			$("form#paginateform #searchedMarks").val("-1");
			$("form#paginateform #searchedExam").val("-1");
			$("form#paginateform #searchedRep").val("-1");
			$("form#paginateform #searchedTopic").val("-1");
			$("form#paginateform #actionname").val("createexam");
		$("#paginateform").submit();    
		}
	});
	
	$("#cancelEdit").on("click",function(){
		$("form#paginateform #actionname").val("canceledit");
		$("#paginateform").submit();
	});
	
	$(".menuoptions").on("click",function(e){
		var confirmresult=confirm("If you press OK your exam data will get lost.Do you want to continue?");
		if(confirmresult==false){
			e.preventDefault()
		}else{
			$.ajax({
				 
				url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "removeaddedquestioninexam"
			   		},
			   type:"POST",
			   success:function(data){
				  
			   },
			   error:function(){
				   }
			   });
			$("#autosubmitform #forwardhref").val($(this).prop("href"));
			$("#autosubmitform").submit();
		}
	});
	
	$("#submitExam").on("click",function(e){
		var numberExpr = /^[0-9]+$/;
		var nameExpr = /^[a-zA-Z0-9 ]+$/;
		var flag=false;
		$("#examTimeError").hide();
		$("#passmarkserror").hide();
		$("#examnameerror").hide();
		if($("form#saveExamForm #newexamname").val().trim()==""){
			$("#examnameerror").html("Please enter exam name");
			$("#examnameerror").show();
			flag=true;
		}
		else if(!$("form#saveExamForm #newexamname").val().match(nameExpr)){
			$("#examnameerror").html("invalid exam name");
			$("#examnameerror").show();
			flag=true;
		}
		if($("form#saveExamForm #passmarks").val().trim()==""){
			$("#passmarkserror").html("Please enter passing marks!!");
			$("#passmarkserror").show();
			flag=true;
		}
		else if(!$("form#saveExamForm #passmarks").val().match(numberExpr)){
			$("#passmarkserror").html("On numbers allowded!!");
			$("#passmarkserror").show();
			flag=true;
		}else if(parseInt($("form#saveExamForm #passmarks").val()) > parseInt($("form#saveExamForm #totalmarks").val())){
				$("#passmarkserror").html("Pass marks cannot be greater than total marks!!");
			$("#passmarkserror").show();
			flag=true;

		}
		if($("form#saveExamForm #examMinute").val().trim()=="" || $("form#saveExamForm #examHour").val().trim()==""){
			$("#examTimeError").html("Please Enter Valid Time!!");
			$("#examTimeError").show();
			flag=true;
		}else if(!$("form#saveExamForm #examHour").val().match(numberExpr)){
			$("#examTimeError").html("Only numbers allowded!!");
			$("#examTimeError").show();
			flag=true;
		}else if(!$("form#saveExamForm #examMinute").val().match(numberExpr)){
			$("#examTimeError").html("Only numbers allowded!!");
			$("#examTimeError").show();
			flag=true;
		}else if(parseInt($("form#saveExamForm #examHour").val())>24 || parseInt($("form#saveExamForm #examHour").val())<0){
			$("#examTimeError").html("Exam hour should be in between 1 and 24!!");
			$("#examTimeError").show();
			flag=true;
		}else if(parseInt($("form#saveExamForm #examMinute").val())>60 || parseInt($("form#saveExamForm #examMinute").val())<0){
			$("#examTimeError").html("Exam Minute should be in between 1 and 60!!");
			$("#examTimeError").show();
			flag=true;
		}else if(parseInt($("form#saveExamForm #examHour").val())==0 && parseInt($("form#saveExamForm #examMinute").val())==0){
			$("#examTimeError").html("Exam Time can't be 0!!");
			$("#examTimeError").show();
			flag=true;
		}else if(parseInt($("form#saveExamForm #noofquestions").val())==0){
			$("#noofquestionsError").html("Please add questions!!");
			$("#noofquestionsError").show();
			flag=true;
		}
		var examname=$("form#saveExamForm #newexamname").val().trim();
		var institute=$("form#saveExamForm #institute").val();
		var examID=$("form#saveExamForm #examID").val();
	if(flag==false){	
		$.ajax({
			
			url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "validateexamname",
		    	 examname:examname,
		    	 institute:institute,
		    	 examID:examID
		   		},
		   	 async: false, 
		   type:"POST",
		   success:function(data){
			   var resultJson=JSON.parse(data);
			   var examstatus=resultJson.examavailable;
			   if(examstatus=="true"){
				   $("#examnameerror").html("Exam name already available.Please enter different name");
					$("#examnameerror").show();
					flag=true;
			   }
			   
		   },
		   error:function(){
			   }
		   });
	}
		if(flag==true){
			e.preventDefault();
		}else{
			$("form#saveExamForm #examname").val($("form#saveExamForm #newexamname").val());
		}
		
	});
	
	/* $("#submitExam").click("click",function(){
		
	}); */
	
	if($("#classownerUploadexamSelectBatchName").length){
		var editExam = new EditExam();
		var subject = <c:out value="${subject}" default='-1'></c:out>;
		var division = <c:out value="${division}" default='-1'></c:out>;
		
		if(division!=-1 && subject!=-1){
			editExam.getBatchFromDivisonNSubject(subject,division);
			$("#classownerUploadexamSelectBatchName").multiselect();
		}
	}
});

var selectMarks = function(){
	
};

function EditExam(){
	this.getBatchFromDivisonNSubject = function(subjectId,divisionId){
		
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getBatchesByDivisionNSubject",
			    	 divisionId: divisionId,
					 subjectId:subjectId
			   		},
			   type:"POST",
			   success:function(data){
				   console.log(data);
				   displayBatchFromSubjectNDivision(data);
			   },
				error:function(){
			   		modal.launchAlert("Error","Error");
			   	}
			   });
		}
	
	var displayBatchFromSubjectNDivision = function (data){
		var selectOptionDropdown = "#classownerUploadexamBatchName";
		var classownerUploadexamBatchNameMenu = "#classownerUploadexamBatchNameMenu";
		data = JSON.parse(data);
		data = data.batchlist;
		data = JSON.parse(data);
		$(classownerUploadexamBatchNameMenu).children().not(".staticMenu").remove();
		$(classownerUploadexamBatchNameMenu).find(".selectAllCheckbox").prop("checked",false);
		if(data.length !== 0){
		var index=0;
		$.each(data,function(index,subjectData){
			var batchName = subjectData.batch_name;
			var batchId = subjectData.batch_id;
			var optionMenu = '<li><a href="#"> <input id="checkButton'+index+'" type="checkbox" name="batch" value="'+batchId+'"><label for="checkButton'+index+'">'+batchName+'</label></a></li>'
			$(classownerUploadexamBatchNameMenu).append(optionMenu);
			index++;
		});
		$("#classownerUploadexamBatchName").prop("disabled",false);
		}else{
			$("#classownerUploadexamBatchName").prop("disabled",true);
			$(".alert-danger").text("Subjects for selected batch are not added.").show();
		}
	}
}
</script>
</head>
<body>
			
			<div class="container" style="margin-bottom: 5px">
			<c:choose>
			<c:when test="${institute ne null }">
			<a type="button" class="btn btn-primary" href="#" id="cancelEdit" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Cancel Edit</a>
			</c:when>
			<c:otherwise><a type="button" class="btn btn-primary" href="#" id="cancelEdit"><span class="glyphicon glyphicon-circle-arrow-left"></span> Cancel Edit</a></c:otherwise>
			</c:choose>
			</div>
			<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;margin-bottom: 15px"><u>Edit Exam</u></div>	
			<div class="btn-group" role="group" aria-label="..." style="width:90%">
			<form action="" id="uploadform" method="post">
			<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
			<input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
			<input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
			<input type="hidden" name="examname" value="<c:out value="${examname}"></c:out>"/>
			<input type="hidden" name="examID" value="<c:out value="${examID}"></c:out>"/>
			Exam Name:- <c:out value="${examname}"></c:out>
			  <c:if test="${(actionname ne 'editexam')}">
			  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#QuestionBankQuestionListQuestionSearchModal">Advance Search</button>
			   </c:if>
			   <c:if test="${(actionname ne 'createexam')}">
			  <button type="button" class="btn btn-default" id="createexam">Save Exam</button>
			  </c:if>
			  <c:if test="${(actionname ne 'showaddedquestions') && (actionname ne 'editexam')}">
			  <button type="button" class="btn btn-default" id="showaddedquestion">Show All Added Questions</button>
			 </c:if>
			 <c:if test="${(actionname eq 'showaddedquestions') || (actionname eq 'editexam') || (actionname eq 'createexam')}">
			  <button type="button" class="btn btn-default" id="backtoquestions">Add Other Questions</button>
			 </c:if>	
			  Total Marks:-<span class="badge" id="temptotalmarks" name="temptotalmarks"><c:out value="${totalmarks}"></c:out></span><br>
			  <c:if test='${actionname eq "advancesearch"}'>
			   <button type="button" class="btn btn-default" id="clearadvancesearch">Clear Advance Search Criteria <span class="badge">x</span></button>
			  </c:if>
			</form>
			</div>
			</div>
			<c:if test="${(questionDataList != null)}">
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
    	<c:if test="${(actionname eq 'showaddedquestions') || (actionname eq 'editexam')}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
    	<c:if test="${currentPage eq 1}">
        <td><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td><c:out value="${counter.count + ((currentPage-1)*50)}"></c:out></td>
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
          	<c:when test="${(actionname eq 'showaddedquestions') || (actionname eq 'editexam')}">
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
  
  <form action="editexam" id="autosubmitform" method="post">
  <input type="hidden" name="forwardhref" id="forwardhref">
  <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
  <input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
  <input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
  <input type="hidden" name="actionname" id="actionname" value="autosubmit">
  </form>
  <form action="editexam" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
  <input type="hidden" name="searchedTopic" id="searchedTopic" value="<c:out value="${searchedTopic}"></c:out>"/>
  <input type="hidden" name="addedIds" id="addedIds" value="">
  <input type="hidden" name="removedIds" id="removedIds" value="">
  <input type="hidden" id="totalmarks" name="totalmarks">
   <input type="hidden" name="actionname" id="actionname" value='<c:out value="${actionname}"></c:out>'>
   <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
   <input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
   <input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
   <input type="hidden" name="examname" value="<c:out value="${examname}"></c:out>"/>
   <input type="hidden" name="examID" value="<c:out value="${examID}"></c:out>"/>
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

	
	
<div class="modal fade" id="QuestionBankQuestionListQuestionSearchModal">
 	<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
          <h4 class="modal-title" id="myModalLabel">Search</h4>
        </div>
        <div class="modal-body" >
		<form action="editexam" id="searchform">
		<input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="actionname" value="advancesearch">
			 <input type="hidden" name="addedIds" id="addedIds" value="">
 			 <input type="hidden" name="removedIds" id="removedIds" value="">
 			 <input type="hidden" id="totalmarks" name="totalmarks">
 			 <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
 			 <input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
 			 <input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
 			 <input type="hidden" name="examname" value="<c:out value="${examname}"></c:out>"/>
 			 <input type="hidden" name="examID" value="<c:out value="${examID}"></c:out>"/>
		<%-- <div class="form-group">
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
		</div> --%>
		<input type="hidden" name="selectedExamID" value="-1">
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
		<label for="-1">Choose Topic</label>	
		  <select  class="btn btn-default" name="selectedTopic">
			<option value="-1">Select</option>
			<c:forEach items="${topics}" var="item">
    			<option value="<c:out value="${item.topic_id}"></c:out>"><c:out value="${item.topic_name}"></c:out></option>
			</c:forEach>
		  </select>
		</div>
		<input type="hidden" name="selectedRep" value="-1" >
		<%-- <div class="form-group">
		<label for="">Choose repetetion</label>	
		  <select class="btn btn-default" name="selectedRep">
			<option value="-1">Select</option>
			<c:forEach items="${repeatation}" var="item">
    			<option value="<c:out value="${item}"></c:out>"><c:out value="${item}"></c:out></option>
			</c:forEach>
		  </select>
		</div> --%>  
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
    <form role="form" action="editexam" id="saveExamForm" class="form-horizontal">
    <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
			<input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
			<input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
			<input type="hidden" name="actionname" value="submitexam">
			<input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
			<input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
			<input type="hidden" name="examname" id="examname" value="<c:out value="${examname}"></c:out>"/>
			<input type="hidden" name="examID" id="examID" value="<c:out value="${examID}"></c:out>"/>
			<input type="hidden" name="institute" id="institute" value="<c:out value="${institute}"></c:out>"/>
  <div class="form-group">
    <label for="newexamname"  class="control-label col-sm-2">Enter Exam Name:</label>
     <div class="col-sm-5">
    <input type="text" class="form-control" id="newexamname" name="newexamname" value="<c:out value="${examname}"></c:out>">
  	</div>
  	 <span class="col-sm-5" id="examnameerror" style="display:none;color: red"></span>
  </div>
  <div class="form-group">
    <label for="totalquestions" class="control-label col-sm-2">Total Questions:</label>
    <div class="col-sm-5">
    <input type="text" class="form-control" id="noofquestions" name="noofquestions" value=<c:out value="${noofquestions}"></c:out> disabled="disabled">
  	</div>
  	<span class="col-sm-5" id="noofquestionsError" style="display:none;color: red"></span>
  </div>
  <div class="form-group">
    <label for="totalmarks" class="control-label col-sm-2">Total Marks:</label>
    <div class="col-sm-5">
    <input type="text" class="form-control" id="totalmarks" name="totalmarks" value=<c:out value="${totalmarks}"></c:out> readonly="readonly">
  	</div>
  </div>
   <div class="form-group">
    <label for="totalmarks" class="control-label col-sm-2">Enter Passing Marks:</label>
     <div class="col-sm-5">
    <input type="text" class="form-control" id="passmarks" name="passmarks" value=<c:out value="${passmarks}"></c:out>>
  	</div>
  	<span class="col-sm-5" id="passmarkserror" style="display:none;color: red"></span>
  </div>
  <div class="form-group">
    <label for="examHour" class="control-label col-sm-2">Exam Time Duration:</label>
     <div class="col-sm-1">
    <input type="number" class="form-control" min="0" max="24" maxlength="2" id="examHour" name="examHour" placeholder="HH" value="<c:out value='${examHour}'></c:out>" >
     </div>
     <div class="col-sm-1">
    <input type="number" class="form-control" min="0" max="59" maxlength="2" id="examMinute" name="examMinute" placeholder="MM" value='<c:out value="${examMinute}"></c:out>'>
  	</div>
  	<span class="col-sm-5" id="examTimeError" style="display:none;color: red"></span>
  </div>
  <div class="form-group">
    <label for="totalmarks" class="control-label col-sm-2">Batch:</label>
    <div class="col-sm-5">
    	<div class="dropdown" id="classownerUploadexamSelectBatchName" >
		  <button class="btn btn-default dropdown-toggle" type="button" id="classownerUploadexamBatchName" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" disabled="disabled">
			<label>Select Batch</label>
			<span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" aria-labelledby="classownerUploadexamSubjectNameMenu" id="classownerUploadexamBatchNameMenu">
			<li class="staticMenu"><a href="#" id="selectAll"> <input type="checkbox" class="selectAllCheckbox" id="classownerUploadexamSubjectNameMenuselectAllRadio"><label for="classownerUploadexamSubjectNameMenuselectAllRadio">Select All<label></a></li>
			<li class="staticMenu divider" role="separator" class=""></li>
		  </ul>
		</div>
  	</div>
  </div>
   <div class="col-sm-offset-2 col-sm-5">
  <button type="submit" class="btn btn-default" id="submitExam">Submit</button>
	</div>
</form>
<form action="editexam" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
   <input type="hidden" name="searchedTopic" id="searchedTopic" value="<c:out value="${searchedTopic}"></c:out>"/>
  <input type="hidden" name="addedIds" id="addedIds" value="">
  <input type="hidden" name="removedIds" id="removedIds" value="">
  <input type="hidden" id="totalmarks" name="totalmarks">
   <input type="hidden" name="actionname" id="actionname" value='<c:out value="${actionname}"></c:out>'>
   <input type="hidden" name="institute" value="<c:out value="${institute}"></c:out>"/>
   <input type="hidden" name="searchcurrentPage" value="<c:out value="${searchcurrentPage}"></c:out>"/>
   <input type="hidden" name="searchtotalPages" value="<c:out value="${searchtotalPages}"></c:out>"/>
   <input type="hidden" name="examname" value="<c:out value="${examname}"></c:out>"/>
   <input type="hidden" name="examID" value="<c:out value="${examID}"></c:out>"/>
</form>
</c:if>
</body>
</html>