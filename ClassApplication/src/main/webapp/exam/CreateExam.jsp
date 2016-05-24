<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
.actionOption .row{
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
</head>
<body>
<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="createExam"/>
	</jsp:include>
<div class="container">
<form id="examDataForm">
		<div class="row well">
		<div class="row">
		<div class="col-md-8 errorDiv error">
		</div>
		</div>
			<div class="row">
			<div class="col-md-3">
				<input type="text" class="form-control" id="newExamName" placeholder="Exam name" required>
			</div>
			<div class="col-md-1">
				<span class="badge" style="padding: 18%;border-radius:20px">OR</span>
			</div>
			<div class="col-md-3">
			<select class="form-control" id="examSelect">
				<option value="-1">Select Exam</option>
				<c:forEach items="${examList}" var="exam">
						<option value="<c:out value="${exam.exam_id }"></c:out>">
							<c:out value="${exam.exam_name }"></c:out>
						</option>
					</c:forEach>
			</select>
			</div>
			</div>
			<div class="row">
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
			
			<div class="col-md-3">
				<select class="form-control" id="batchSelect" >
					<option value="-1">Select Batch</option>
				</select>
			</div>
			<div class="col-md-3" >
				<select id="headerDesc" name="headerDesc" class="form-control" required>
					<option value="">Select Header</option>
					<c:forEach items="${headerList}" var="headerX">
						<option value="<c:out value="${headerX.header_id}"></c:out>">
							<c:out value="${headerX.header_name}"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
		</div>
		</form>
		<div class="actionOption" style="display: none">
		<div class="subjectError error">
		</div>
		<div class="row subjectDiv">
		
		</div>
		<div >
			<button class="btn btn-primary btn-sm" value="Save" id="saveExam">Save</button>
		</div>
		<div id="preview_page"></div>
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
	
	<div class="modal fade" id="examPreviewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title" id="myModalLabel">Question Paper list</h4>
		  </div>
		  <div class="modal-body">
		  
		  	<div id="preview_pageHeader">
				
			</div>
			<div id="meta">
				Marks:-<span id="marks"></span>
			</div>
			<hr/>
			<div id="preview_pageContent">
				
			</div>
		  </div>
		</div>
	  </div>
	</div>
</body>
</html>