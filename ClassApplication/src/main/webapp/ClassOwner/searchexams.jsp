<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<s:if test="hasActionErrors()">
   <div class="alert alert-danger">
      <s:actionerror/>
   </div>
</s:if>

<jsp:include page="/ClassOwner/examSearchForm.jsp"></jsp:include>

<div class="modal fade" id="questionNavigationModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title">Question Navigation</h4>
      </div>
      <div class="modal-body">
		<c:choose>
			<c:when test="${not empty requestScope.examData}">
			<c:set var="examData" value="${requestScope.examData}"></c:set>
			<c:set var="count" value="1"></c:set>
			<c:forEach items="${examData.questionDatas }" var="question">
					<c:if test="${count%5 eq 0 }">
						<div class="row">
					</c:if>
					<c:choose>
						<c:when test="${question.solved }">
							<div class="col-md-2 btn btn-success btn-xs" style="margin: 2px;"><a href='viewonlyexam?methodToCall=getQuestion&questionNumber=<c:out value="${count}"></c:out>'>
								<c:out value="${count }"></c:out>
							</a></div>
						</c:when>
						<c:otherwise>
							<div class="col-md-2 btn btn-default btn-xs" style="margin: 2px;"><c:out value="${count }"></c:out></div>
						</c:otherwise>
					</c:choose>	
						
					
					<c:if test="${count%5 eq 0 }">
						</div>
					</c:if>
			<c:set var="count" value="${count+1 }"></c:set>		
			</c:forEach>	
		    </c:when>
		</c:choose>        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="examEvaluating">
  <div class="modal-dialog">
    	<div class="progress progress-striped active">
			<div class="progress-bar progress-bar-success" role="progressbar"
				aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"
				style="width: 100%">
				
				<strong>Evaluating!&nbsp;</strong>Please wait...
			</div>
		</div>
		</div>
</div><!-- /.modal -->


<c:set value="${sessionScope.examSearchResult}" var="exams"></c:set>
	
<c:choose>
	<c:when test="${not empty  requestScope.divisionId}">

		<c:choose>
			<c:when test="${not empty exams}">
				<table class="table table-striped" style="margin-top: 10px;">
					<tr>
						<th>Exam ID</th>
						<th>Subject</th>
						<th>Division</th>
						<th>Teacher</th>
						<th>Date</th>
						<th></th>
					</tr>
					<c:forEach items="${exams}" var="exam">
						<tr>
							<td id="examId"><c:out value="${exam.exam_id}"></c:out></td>
							<td><c:out value="${exam.subject_id}"></c:out></td>
							<td><c:out value="${exam.div_id}"></c:out></td>
							<td><c:out value="${exam.teacher_id}"></c:out></td>
							<td><c:out value="${exam.create_date}"></c:out></td>
							<td><input type="button" class="viewexam btn btn-info" value="View"/></td>
						</tr>
					</c:forEach>
				</table>
			</c:when>

			<c:otherwise>
	No result so search criteria
	</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
	
	</c:otherwise>
</c:choose>

<c:choose>

	<c:when test="${not empty requestScope.examData}">
	<c:set var="examData" value="${requestScope.examData}"></c:set>
		<div class="panel panel-default option_body">
			<div class="panel-heading">
				<c:out value="${examName}"></c:out>
				<div class="pull-right">
					Time:<c:out value="${examData.timeToSolve}"></c:out>
					Total Marks:<c:out value="${examData.totalMarks}"></c:out>
				</div>
			</div>
			<div class="panel-body">
				<div class="accordion" id="myAccordion">
				<c:set var="question" value="${requestScope.questionData}"></c:set>
					<div class="panel">
						<div class="alert alert-danger hide">
							Please select one option before submit.....
						</div>
						
						<div>
						<span class="badge"><c:out value="${questionNumber+1}"></c:out></span>
						<a data-toggle="collapse" data-target="#_option" data-parent="#myAccordion">
							<c:out value="${question.question.question}"></c:out>							
						</a>
						</div>
						<div class="badge pull-right">
							<c:out value="${question.marks}"></c:out>
						</div>
						<br>
						<div id="_option" class="collapse">
					        <p>
					        <c:choose>
					        	<c:when test="${question.multipleChoice }">
					        		<c:set var="questiontype" value="checkbox"></c:set>
					        	</c:when>
					        	<c:otherwise>
					        		<c:set var="questiontype" value="radio"></c:set>
					        	</c:otherwise>
					        </c:choose>
					        	<c:set var="optionNumber" value="0"></c:set>
					        	<c:forEach items="${question.options}" var="option">
					        		<br>
					        		<input type='<c:out value="${questiontype}"></c:out>' name="option" value='<c:out value="${optionNumber}"></c:out>'/>
					        		<c:out value="${option.options }"></c:out>
					        		<c:forEach items="${option.imageFiles }" var="image">
					        			<img src='data:image/jpeg;base64,<c:out value="${image}"></c:out>'/>
					        		</c:forEach>
					        		<br>
					        		<c:set var="optionNumber" value="${optionNumber+1 }"></c:set>
					        	</c:forEach>
					        </p>
					    </div>
    				</div>
				</div>
			</div>
			<div class="panel-footer">
				
				<div class="btn-toolbar" role="toolbar">
			      <div class="btn-group btn-group-sm">
			        <c:if test="${questionNumber gt 0}">
			        	<a href='viewonlyexam?methodToCall=getQuestion&questionNumber=<c:out value="${questionNumber}"></c:out>' class="btn btn-info"><span class="glyphicon glyphicon-backward"></span>&nbsp;Prev</a>
			        </c:if>
			        <button type="button" class="btn btn-default" id="submit"><span class="glyphicon glyphicon-floppy-disk"></span>&nbsp;Save</button>
			        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#questionNavigationModal"><span class="glyphicon glyphicon-th-list"></span>&nbsp;List</button>
			        <button type="button" class="btn btn-default btn-exam-eval"><span class="glyphicon glyphicon-upload"></span>&nbsp;Evaluate</button>
			        <c:if test="${questionNumber lt examDataLength-1}">
			        	<a href='viewonlyexam?methodToCall=getQuestion&questionNumber=<c:out value="${questionNumber+2}"></c:out>' class="btn btn-info"><span class="glyphicon glyphicon-forward"></span>Next</a>
			        </c:if>	
			      </div>
    </div>
			</div>
		</div>
	</c:when>
	
</c:choose>


<form method="post" action="viewonlyexam" id="viewExam">
	<input type='hidden' name="examId"/>
</form>

<form method="post" action="viewonlyexam" id="answerSubmit">
	<input type='hidden' name="examId"/>
	<input type="hidden" name="methodToCall" value="submitans"/>
	<input type="hidden" name="questionNumber" value='<c:out value="${questionNumber}"></c:out>'/>
	
</form>

<script>
$('.viewexam').click(function(){
	var examId = $(this).parents('tr').find('#examId').text();
	$('#viewExam').find('input').val(examId);
	$('#viewExam').submit();
});

$('#submit').click(function(){
	var that = $(this);
	
	if($(this).parents('.option_body').find('input:checked').length == 0){
		$(this).parents('.option_body').find('.alert').focus();
		$(this).parents('.option_body').find('.alert').removeClass('hide');
		$('body').animate({
	        scrollTop: (that.parents('.option_body').find('.alert').offset().top - 200)
	    }, 500);
	}else{
		var option = $("#_option").clone();
		$('#answerSubmit').append(option);
		$('#answerSubmit').submit();
	}
 });

 $(".btn-exam-eval").click(function(){
	 modal.modalConfirm("Submit","Do you want to evaluate exam? <br> you will not be able to modify again","No","Yes",function(){
		 $("#examEvaluating").modal("show");
		 $('#viewExam').submit();
	 },
	 []);
 });

</script>