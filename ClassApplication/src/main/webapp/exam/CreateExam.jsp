<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
.subjectDiv .row{
padding-right: 0px;
width: 100%
}
.form-control{
font-size: 12px;
padding: 2px 2px;
height: 26px
}
.subjectDiv .col-md-1,.subjectDiv .col-md-3,.subjectDiv .col-md-4{
padding-right: 2px;
padding-left: 2px;
}
</style>
<script>
$(document).ready(function(){
	$("#division").change(function(){
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		createQuestionPaperListTable(e);
		}
		handlers.error = function(e){console.log("Error",e)}
		var division = $("#division").val(); 
		rest.post("rest/classownerservice/getQuestionPaperList/"+division,handlers);
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getSubjectOfDivision",
		    	 divisionId: division
		   		},
		   type:"POST",
		   success:function(data){
			   data = JSON.parse(data);
			   if(data.status == "success"){
				   var subjectnames = data.subjectnames;
				   var subjectIds = data.subjectids;
				   var i = 0;
				   var subjectnameArray = subjectnames.split(",");
					var subjectidArray =  subjectIds.split(",");  
					subjectList = [];
					$(".subjectDiv").empty();
					while(i < subjectnameArray.length){
				   		$(".subjectDiv").append("<div class='row well examSubjectPapers'><div class='col-md-3'><input type='checkbox' value='"+subjectidArray[i]+"' name='subjectCheckbox' id='subjectCheckbox'>"+
				   				subjectnameArray[i]+"</div><div class='col-md-4'>"+
				   				"<button class='btn btn-primary btn-xs chooseQuestionPaper'>Choose Question Paper</button>"+
				   				"<span class='questionPaperName'></span><input type='hidden' class='form-control selectedQuestionPaperID'></div><div class='col-md-1'><input type='text' class='form-control marks'></div>"+
				   				"<div class='col-md-3'><div class='col-md-6'>Duration  : </div><div class='col-md-3'><input type='number' class='form-control examHour' placeholder='HH'></div><div class='col-md-3'><input type='number' class='form-control examMinute' placeholder='MM'></div></div>"+
				   				"<div class='col-md-1'><button class='btn btn-primary btn-xs'>Preview</button></div></div>");
				   		i++;
				   }
			   }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	});
	var that;
	$(".subjectDiv").on("click",".chooseQuestionPaper",function(){
		$("#questionPaperListModal").modal("toggle");
		that = $(this);
	});
	
	$("#questionPaperListModal").on("click",".confirmQuestionPaper",function(){
		that.closest("div").find(".questionPaperName").html($(this).closest("div").find(".paperDesc").val());
		that.closest("div").find(".selectedQuestionPaperID").val($(this).attr("id"));
		$("#questionPaperListModal").modal("toggle");
	});
	
	$("#saveExam").click(function(){
		var examName = ""
		var examID = "";
		if($("#examSelect").val() != "-1"){
			examID = $("#examSelect").val();
			examName = "-1";
		}else{
			examName = $("#newExamName").val();
		}
		var division = $("#division").val();
		var exam_paperList = [];
		var i = 0;
		for(i=0;i<$(".examSubjectPapers").length;i++){
			
			if($($(".examSubjectPapers")[i]).find("#subjectCheckbox").is(":checked")){
			var exam_paper = {};
			exam_paper.exam_id = examID;	
			exam_paper.div_id = $("#division").val(); 
			exam_paper.sub_id = $($(".examSubjectPapers")[i]).find("#subjectCheckbox").val();
			exam_paper.marks = $($(".examSubjectPapers")[i]).find(".marks").val();
			exam_paper.duration = $($(".examSubjectPapers")[i]).find(".examHour").val()+":"+$($(".examSubjectPapers")[i]).find(".examMinute").val();
			exam_paper.question_paper_id = $($(".examSubjectPapers")[i]).find(".selectedQuestionPaperID").val();
			exam_paper.header_id = $("#headerDesc").val();
			exam_paperList.push(exam_paper);
			}
		}
		console.log(exam_paperList);
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		}
		handlers.error = function(e){console.log("Error",e)}
		exam_paperList = JSON.stringify(exam_paperList);
		rest.post("rest/classownerservice/saveExamPaper/"+examName,handlers,exam_paperList);
		});
});

/* function Exam_Paper(){
	
}
 */

function createQuestionPaperListTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#questionPaperListTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Paper Description",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.paper_description+'</div>';
				return div;
			},sWidth:"40%"},
			{ title: "Marks",data:null,render:function(data,event,row){
				return row.marks;
			},sWidth:"20%"},
			{ title: "Choose",data:null,render:function(data,event,row){
				var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-sm btn-primary confirmQuestionPaper" value="Choose" id="'+row.paper_id+'">'+
					'<input type="hidden" value="'+row.paper_description+'" class="paperDesc">'+
				'</div>'
				return buttons;
			},sWidth:"10%"}
		]
	});
	
}
</script>
</head>
<body>
<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="createExam"/>
	</jsp:include>
<div class="container">
		<div class="row">
			<div class="col-md-3">
			<label>Select Exam</label>
			<select class="form-control" id="examSelect">
				<option value="-1">Select Exam</option>
				<c:forEach items="${examList}" var="exam">
						<option value="<c:out value="${exam.exam_id }"></c:out>">
							<c:out value="${exam.exam_name }"></c:out>
						</option>
					</c:forEach>
			</select>
			</div>
			<div class="col-md-1">
				<span class="badge" style="padding: 18%;border-radius:20px">OR</span>
			</div>
			<div class="col-md-3">
				<label>Create New Exam</label>
				<input type="text" class="form-control" id="newExamName">
			</div>
		</div>
		<div class="row">
			<div class="col-md-2">Select Class</div>
			<div class="col-md-3">
				<select id="division" name="division" class="form-control">
					<option value="-1">Select Class</option>
					<c:forEach items="${divisionList}" var="division">
						<option value="<c:out value="${division.divId }"></c:out>">
							<c:out value="${division.divisionName }"></c:out>
							<c:out value="${division.stream }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2">Select Header</div>
			<div class="col-md-3">
				<select id="headerDesc" name="headerDesc" class="form-control">
					<option value="-1">Select Header</option>
					<c:forEach items="${headerList}" var="headerX">
						<option value="<c:out value="${headerX.header_id}"></c:out>">
							<c:out value="${headerX.header_name}"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="row subjectDiv">
		
		</div>
		<div class="actionOption">
			<button class="btn btn-primary btn-sm" value="Save" id="saveExam">Save</button>
		</div>
</div>

<div class="modal fade" id="questionPaperListModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title" id="myModalLabel">Question Paper list</h4>
		  </div>
		  <div class="modal-body">
			<div id="questionPaperList">
				<table id="questionPaperListTable" style="width:100%;"></table>
			</div>
		  </div>
		</div>
	  </div>
	</div>
</body>
</html>