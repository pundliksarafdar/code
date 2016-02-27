<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
.dataTables_filter {
     display: none;
}

#GenerateQuestionPaperContainer-generatePattern *{
	padding:10px;
}

ul {
    list-style-type: none;
}
.modal-header{
background: white;
}

.col-md-*{
width: 100%;
margin : 0%;
padding :0%;}
.row{
width: 100%;
margin : 0%;
padding :0%;
}

.sectionBackground{
background: #A8D8D8;
padding-top: 1%;
padding-bottom: 1%;
margin-bottom: 1%;
}
.sectionUl{
padding: 0%;
}

.form-control{
	font-size:12px;
	height:25px;
    padding-top: 1px;
    padding-bottom: 1px;
    padding-left: 5px;
    padding-right: 5px;


}

.col-md-1,.col-md-2,.col-md-3{
padding-left: 10px;
padding-right: 10px;
}
.well .col-md-9,.well .col-md-1,.well .col-md-7{
margin: 1px;
padding: 0px;
}
.well{
padding: 0px;
margin-right: 2px
}

.patternError{
color: red;
font-size: 12px;
}

.noRegenerate{
	display:none;
}


</style>
</head>
<body>
<jsp:include page="../ExamHeader.jsp" >
		<jsp:param value="active" name="editQuestionPaper"/>
	</jsp:include>
<div class="container" style="padding: 2%; background: #eee">
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
				<span id="divisionError" class="patternError"></span>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchQuestionPaper">Search</button>
			</div>
		</div>		
	</div>	
	<div class="row" id="editQuestionPaperListContainer">
			<div class="col-md-12">
				<table id="editQuestionPaperList" class="table"></table>
			</div>
		</div>
		<div id="viewPatternData"></div>
		<div class="container well" id="saveSection" style="padding:1%;margin:1%;">
		<input type="button" value="Generate" class="btn btn-default" id="generateQuestionPaper"/>
		<div>&nbsp;</div>
		<div class="row" >
			<div class="col-xs-5"><input type="text" class="form-control" id="saveQuestionPaperName" placeholder="Question paper name"></div>
			<div class="col-xs-5"><input type="text" class="form-control" id="saveQuestionPaperDesc" placeholder="Question paper description"></div>
			<div class="col-xs-2"><input type="button" value="Save" class="btn btn-default" id="saveQuestionPaper"/></div>
		</div>
		</div>
		
		<div class="modal fade" id="questionListModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title" id="myModalLabel">Question list</h4>
		  </div>
		  <div class="modal-body">
			<div id="questionListLoading">
				Loading list
			</div>
			<div id="questionList">
				<table id="questionListTable" style="width:100%;"></table>
			</div>
		  </div>
		</div>
	  </div>
	</div>
</body>
</html>