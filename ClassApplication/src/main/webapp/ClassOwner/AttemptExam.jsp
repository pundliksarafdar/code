<!-- Assumpation made
	Code all alternate question will come one after another
	Always will be objective question
 -->

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

.table td:FIRST-CHILD{
	width: 5%;
}
</style>
<script>
$(document).ready(function(){
	$("#examSubmit").click(function(){
		modal.modalConfirm("Submit","Do you want to Submit?","Cancel","Submit",SubmitExam,[]);
	});
	var milliSeconds = 100000;
	var socket = new WebSocket("ws://"+location.host+"/websocket/timer/"+milliSeconds);
	socket.onmessage = function(message){console.log(message.data)
		var dataObj = JSON.parse(message.data);
		if(dataObj.examEnded){
			SubmitExam();
		}
		$("#timeLeft").html(dataObj.timer);
	}
	showOr();
});

function showOr(){
	var alternateIds = $("[alternameId]");
	var OredIdElm = {};
	$.each(alternateIds,function(){
		var itemsid = $(this).attr("alternameId");
		OredIdElm[itemsid] = $("[alternameId='"+itemsid+"']");
	});
	
	
	$.each(OredIdElm,function(key,val){
		$.each($(val).closest("tr"),function(key,val){
			if(key){
				$(val).before("<tr><td></td><td align='center'>Or</td></tr>");	
			}
		});		
	});
}
function SubmitExam(){
	/*
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
		}else{
			if(count>0){
				
				answers=answers+" / "+"";
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
	*/
	var questionAndAns = {};
	$.each($("[question]"),function(key){
		var questionId = $(this).attr("question");
		$.each($('[question-id="'+questionId+'"]'),function(key,val){
			if(!questionAndAns[questionId]){
				questionAndAns[questionId] = [];
			}
			if($(val).is(":checked")){
				if(questionAndAns[questionId].indexOf($(val).val())==-1){questionAndAns[questionId].push($(val).val());}	
			}
			
		});
	});
	
	var handlers = {};
	handlers.success = function(e){
		$("#showScoreForm").find("[name='totalMarks']").val(e.totalMarks);
		$("#showScoreForm").find("[name='marks']").val(e.marks);
		$("#showScoreForm").submit();
	}
	handlers.error = function(e){console.log("Error",e)}
	var onlineexam = {};
	onlineexam.instId = $("#inst_id").val();
	onlineexam.subId = $("#subject").val();
	onlineexam.batchId = $("#batch").val();
	onlineexam.division = $("#division").val();
	onlineexam.examId = $("#exam").val();
	onlineexam.questionPaperId = $("#question_paper_id").val()
	
	onlineexam.examMap = questionAndAns;
	
	rest.post("rest/classownerservice/evaluateExam",handlers,JSON.stringify(onlineexam));
	console.log(questionAndAns);
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
				<div class="col-md-3">
					<div id="timeLeft"></div>
				</div>
				<div class="col-md-3">
					<strong><c:out value="${onlineExamPaper.paper_description }"></c:out></strong>
				</div>
				<div class="col-md-3">
					<strong>Marks:-<c:out value="${onlineExamPaper.marks }"></c:out></strong>
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
				
				<tr style="padding-top: 1%">
					<c:if test="${element.item_type eq 'Section' }">
						<td colspan="3" align="center" style="font-weight: bolder;" class="section">
							<c:out value="${element.item_description }" default="No section"></c:out>
						</td>
					</c:if>
					<c:if test="${element.item_type eq 'Instruction' }" >
						<td  style="font-weight: bold;" class="instruction">
							<c:out value="${element.item_no}"></c:out>
						</td>
						<td colspan="3"   style="font-weight: bold;">
							<c:out value="${element.item_description }" default="No instruction"></c:out>
						</td>
					</c:if>
					<c:if test="${element.item_type eq 'Question' }">
						<td class="question" alternameId="<c:out value='${element.alternate_value}'></c:out>">
							<c:out value="${element.item_no}"></c:out>
						</td>
						<td question="<c:out value='${element.questionbank.que_id}'></c:out>">
							<c:out value="${element.questionbank.que_text }" default="No question" ></c:out>
						</td>
						<td question="<c:out value='${element.questionbank.que_id}'></c:out>">
							<c:out value="${element.questionbank.marks }" default="No question" ></c:out>
						</td>
					</c:if>
				</tr>
				<tr>
					<td colspan="3">
					<c:forEach items="${element.questionbank.primaryImage}" var="imgsrc">
						<img src='/rest/commonservices/image/<c:out value="${imgsrc}"></c:out>' width="200px" height="200px"/>
					</c:forEach>
						
					</td>
				</tr>
				<c:if test="${element.questionbank.opt_1 ne null }">
					<tr>
					<td></td>
					<td>
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox"
									name='<c:out value="${varStat.index }"></c:out>' value="0" question-id="<c:out value='${element.questionbank.que_id}'></c:out>">
							
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value ="0" question-id="<c:out value='${element.questionbank.que_id}'></c:out>">
							
						</c:if>
						
							<c:out value="${element.questionbank.opt_1 }"></c:out>
						</td>
					</tr>
					<tr>
					<td colspan="3">
					<c:forEach items="${element.questionbank.secondaryImageStr['0']}" var="imgsrc">
						<img src='/rest/commonservices/image/<c:out value="${imgsrc}"></c:out>' width="200px" height="200px"/>
					</c:forEach>
					</td>
				</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_2 ne null }">
					<tr>
					<td></td>
					<td>
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox"  name='<c:out value="${varStat.index }"></c:out>' value="1" question-id="<c:out value='${element.questionbank.que_id}'></c:out>">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' value="1" question-id="<c:out value='${element.questionbank.que_id}'></c:out>">
						</c:if>
							<c:out value="${element.questionbank.opt_2 }"></c:out>
					</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_3 ne null }">
					<tr>
					<td></td>
					<td>
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="2">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="2">
						</c:if>
							<c:out value="${element.questionbank.opt_3 }"></c:out>
						</td>
						</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_4 ne null }">
					<tr>
					<td></td>
					<td>
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="3">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="3">
						</c:if>
							<c:out value="${element.questionbank.opt_4 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_5 ne null }">
					<tr>
					<td></td>
					<td>	
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="4">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="4">
						</c:if>
							<c:out value="${element.questionbank.opt_5 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_6 ne null }">
					<tr>
					<td></td>
					<td>	
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="5">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="5">
						</c:if>
							<c:out value="${element.questionbank.opt_6 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_7 ne null }">
					<tr>
					<td></td>
					<td>	
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="6">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="6">
						</c:if>
							<c:out value="${element.questionbank.opt_7 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_8 ne null }">
					<tr>
					<td></td>
					<td>	
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="7">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="7">
						</c:if>
							<c:out value="${element.questionbank.opt_8 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_9 ne null }">
					<tr>
					<td></td>
					<td>	
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="8">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="8">
						</c:if>
							<c:out value="${element.questionbank.opt_9 }"></c:out>
						</td>
					</tr>
				</c:if>
				<c:if test="${element.questionbank.opt_10 ne null }">
					<tr>
					<td></td>
					<td>
						<c:if test="${element.ans_type eq 'multiple' }">
								<input type="checkbox" name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="9">
						</c:if>
						<c:if test="${element.ans_type eq 'single' }">
								<input type="radio"
									name='<c:out value="${varStat.index }"></c:out>' question-id="<c:out value='${element.questionbank.que_id}'></c:out>" value="9">
						</c:if>
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
	<form id="showScoreForm" method="post" action="showScore">
		<input type="hidden" name="passingMarks"/>
		<input type="hidden" name="marks"/>
		<input type="hidden" name="totalMarks"/>
	</form>
</body>
</html>