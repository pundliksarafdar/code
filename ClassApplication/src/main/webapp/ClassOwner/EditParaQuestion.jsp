<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
#uploadParaQuestionImage{
	height:20px;
	width:20px;
}
</style>
<script type="text/javascript" src="js/view/editParagraphQuestion.js"></script>

<script>
	var subId = <c:out value="${subject}"></c:out>;
	var queId = <c:out value="${questionNumber}"></c:out>;
	var divId = <c:out value="${division}"></c:out>;
	var topicId = <c:out value="${topicId}"></c:out>;
	
	var getQuestionUrl = "/rest/classownerservice/examservice/paraquestion/"+queId+"/"+subId+"/"+divId;
	
	var handler = {};
	handler.success = function(data){loadParaquestion(data,queId)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	$(document).ready(function(){
		rest.get(getQuestionUrl,handler,true);
		$("#paragraph").addExpresssion();
	});
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<div id="paragraphDiv" class="container">
	<form id="paraQuestionForm" class="form-horizontal corex-form-container">
		<div class="form-group">
			<label class="col-md-2 control-label" for="paragraph">Paragraph</label>
			<div class="col-md-10">
			<textarea required id="paragraph" class="form-control"></textarea>
			</div>
		</div>
		<div class="form-group">
			<label for="totalMarks" class="col-sm-2 control-label">Total marks</label>
			<div class="col-sm-6">
			<input type="number" id="totalMarks" name="totalMarks" required class="form-control"  maxlength="5" size="5" style="width: 20%;" min="1"/>
			</div>
			<div class="col-md-offset-2 col-sm-2">
				<select class="form-control" id="topicSelect">
					<option value="-1">Select Topic</option>
					 <c:forEach items="${topicList}" var="topic" varStatus="counter">
					 <option value="<c:out value="${topic.topic_id}"></c:out>"><c:out value="${topic.topic_name}"></c:out></option>
					 </c:forEach>
				</select>
				</div>
		</div>
		<div class="form-group">
			<label for="questionmarks" class="col-sm-2 control-label"></label>
			<div class="col-sm-2">
				<span class="btn btn-success fileinput-button">
					<i class="glyphicon glyphicon-folder-open"></i>
					<span>Add images </span>
					<input type="file" accept=".jpg,.jpeg,.gif,.bmp" id="addParaQuestionImage">
				</span>
			</div>
		</div>
		<div class="form-group" id="paraQuestionImage">
			
		</div>
		<div class="form-group">
			<label class="col-md-2 control-label"></label>
			<div id="paraQuestion" class="col-md-10">
				<input type="button" id="addQuestions" class="btn btn-default" value="Add question"/>
				<br/>
			</div>	
		</div>
		<div class="form-group">
		<label class="col-md-2 control-label"></label>
			<div id="paraQuestion" class="col-md-10">
			
			</div>
		</div>
		<div class="form-group">
		<label class="col-md-2 control-label"></label>
			<div class="col-sm-10">
			<input type="button" id="saveParagraphQuestion" class="btn btn-default" value="Save"/>
			<input type="button" value="Cancel" class="btn btn-link" id="cancelEdit"/>
			</div>
		</div>
	</form>
	
	<form action="searchQuestion" method="post" id="searchQuestion">
		<input type="hidden" value="<c:out value="${division}"></c:out>" name="classId"/>
		<input type="hidden" value="<c:out value="${subject}"></c:out>" name="subId"/>
		<input type="hidden" value="1" name="questionType"/>
		
	</form>
</div>

<div id="parahiddenQuestion" class="hide">
	<div class="paraQuestionTmpl">
			<div class="input-group">
		      <input type="text" id="" class="form-control">
		      <span class="input-group-addon">
		        <span class="fileinput-button">
					<i class="glyphicon glyphicon-folder-open"></i>
					<input type="file" accept=".jpg,.jpeg,.gif,.bmp" id="uploadParaQuestionImage">
				</span>
		        <i class="deleteParaQuestion glyphicon glyphicon-trash" title="Delete option"></i>
		      </span>
		    </div>
		    <label for="totalMarks" class="col-sm-2 control-label">Question marks</label>
			<div class="col-sm-2">
				<input type="number" id="questionMarks" required class="form-control"  maxlength="5" size="5" style="width: 50%;" min="1"/>
		    </div>
		    <div id="paraQuestionImage" class="input-group row"></div>
	</div>
</div>
</body>
</html>