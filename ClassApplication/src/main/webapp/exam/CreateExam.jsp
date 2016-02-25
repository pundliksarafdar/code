<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
				   		$(".subjectDiv").append("<div class='row well'><div class='col-md-3'><input type='checkbox' value='"+subjectidArray[i]+" name='subjectCheckbox' id='subjectCheckbox'>"+
				   				subjectnameArray[i]+"</div><div class='col-md-5'>"+
				   				"<button class='btn btn-primary btn-xs chooseQuestionPaper'>Choose Question Paper</button>"+
				   				"<span class='questionPaperName'></span></div><div class='col-md-1'><input type='text' class='form-control marks'></div>"+
				   				"</div>");
				   		i++;
				   }
			   }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	});
	
	$(".subjectDiv").on("click",".chooseQuestionPaper",function(){
		$("#questionPaperListModal").modal("toggle");
	});
});

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
					'<input type="button" class="btn btn-sm btn-primary viewPattern" value="Choose" id="'+row.paper_id+'">'+
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
			<select class="form-control">
				<option>Select Exam</option>
			</select>
			</div>
			<div class="col-md-1">
				<span class="badge" style="padding: 18%;border-radius:20px">OR</span>
			</div>
			<div class="col-md-3">
				<label>Create New Exam</label>
				<input type="text" class="form-control">
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
				<select id="header" name="header" class="form-control">
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