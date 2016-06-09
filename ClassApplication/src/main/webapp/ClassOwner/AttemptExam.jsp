<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet" />
<script src="js/jquery.dataTables.js"></script>
<script src="js/REST.js"></script>
<style type="text/css">
.table>thead>tr>th, .table>tbody>tr>th, .table>tfoot>tr>th, .table>thead>tr>td, .table>tbody>tr>td, .table>tfoot>tr>td{
border-top : 0px solid #ddd
}
</style>
<script>
$(document).ready(function(){
	/* $('#questionTable').DataTable(); */
	
	$("#examSubmit").click(function(){
		modal.modalConfirm("Submit","Do you want to Submit?","Cancel","Submit",SubmitExam,[]);
		
	});
});
function SubmitExam(){
	var answers="";
	var count=0;
	while(count<$("#questionPaperSize").val()){
		if($("input[name='"+count+"']").val() != undefined){
		var checkedanswers = [];
		//var name="answers"+count;
			$.each($("input[name='"+count+"']:checked"), function(){            
				checkedanswers.push($(this).val());
    });
		if(count==0){
			
			answers=checkedanswers+"";
		}else{
			answers=answers+" / "+checkedanswers;
		}
		}
		count++;
	}
	console.log(answers);
	var handlers = {};
	handlers.success = function(e){console.log("Success",e);
	$("#examDiv").empty();
	$("#examMarksDiv").append("Marks	:	"+e.marks);
	}
	handlers.error = function(e){console.log("Error",e)}
	var onlineexam = {};
	onlineexam.div_id = $("#division").val();
	onlineexam.batch_id = $("#batch").val()
	onlineexam.sub_id = $("#subject").val()
	onlineexam.exam_id = $("#exam").val()
	onlineexam.inst_id = $("#inst_id").val()
	onlineexam.answers = answers ; 
	onlineexam.paper_id = $("#question_paper_id").val()
	console.log(onlineexam);
	onlineexam = JSON.stringify(onlineexam);
	rest.post("rest/classownerservice/getOnlineExamPaperMarks",handlers,onlineexam);
}
</script>
</head>
<body>
<div id="examBodyDiv">
	<c:if test="${onlineExamPaper ne null }">
	<input type="hidden" id="questionPaperSize" value="<c:out value="${questionPaperSize}"></c:out>">
	<input type="hidden" id="division" value="<c:out value="${division}"></c:out>">
	<input type="hidden" id="question_paper_id" value="<c:out value="${question_paper_id}"></c:out>">
	<input type="hidden" id="subject" value="<c:out value="${subject}"></c:out>">
	<input type="hidden" id="exam" value="<c:out value="${exam}"></c:out>">
	<input type="hidden" id="batch" value="<c:out value="${batch}"></c:out>">
	<input type="hidden" id="inst_id" value="<c:out value="${inst_id}"></c:out>">
		<div class="container" id="examDiv">
			<div class="row">
				<div class="col-md-offset-4 col-md-3">
					<c:out value="${onlineExamPaper.paper_description }"></c:out>
				</div>
			</div>
			<table id="questionTable" width="100%" class="table">
			<!-- <thead>
				<tr>
					<th>Q No</th>
					<th>Question</th>
				</tr>
			</thead> -->
			<tbody>
			<c:forEach items="${onlineExamPaper.onlineExamPaperElementList }"
				var="element" varStatus="varStat">
				<tr style="background: #eee;padding-top: 1%">
					<c:if test="${element.item_type eq 'Section' }">
						<td colspan="3" align="center">
							<c:out value="${element.item_description }"></c:out>
						</td>
					</c:if>
					<c:if test="${element.item_type eq 'Question' }">
						<td>
							<c:out value="${element.item_no}"></c:out>
						</td>
						<td>
							<c:out value="${element.questionbank.que_text }"></c:out>
						</td>
					</c:if>
				</tr>
				<c:if test="${element.questionbank.opt_1 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox"
									name='<c:out value="${varStat.index }"></c:out>' value="0">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value ="0">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_1 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_2 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox"  name='<c:out value="${varStat.index }"></c:out>' value="1">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="1">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_2 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_3 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="2">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="2">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_3 }"></c:out>
						</td>
						</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_4 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>							
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="3">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="3">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_4 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_5 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="4">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="4">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_5 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_6 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="5">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="5">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_6 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_7 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="6">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="6">
							</td>
						</c:if>
						<td>						
							<c:out value="${element.questionbank.opt_7 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_8 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="7">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="7">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_8 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_9 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="8">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="8">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_9 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_10 ne null }">
					<tr>
						<c:if test="${element.ans_type eq 'multiple' }">
							<td>
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' value="9">
							</td>
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
							<td>
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="9">
							</td>
						</c:if>
						<td>
							<c:out value="${element.questionbank.opt_10 }"></c:out>
						</td>
					</tr>
				</c:if>
			</c:forEach>
			</tbody>
			</table>
			<div class="row"> <div id="examSubmit""col-md-offset-10 col-md-2"><button class="btn btn-primary btn-sm" id="examSubmit">Submit</button></div>
			</div>
		</div>
	</c:if>
	<div class="container" align="center" id="examMarksDiv">
	
	</div>
	</div>
</body>
</html>