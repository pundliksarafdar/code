<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/view/editTeacherSubjectiveQuestion.js"></script>
<script>
	var subId = <c:out value="${subject}"></c:out>;
	var queId = <c:out value="${questionNumber}"></c:out>;
	var divId = <c:out value="${division}"></c:out>;
	var topicId = <c:out value="${topicId}"></c:out>;
	var inst_id = <c:out value="${inst_id}"></c:out>;
	var getQuestionUrl = "/rest/teacher/question/"+inst_id+"/"+queId+"/"+subId+"/"+divId;
	var handler = {};
	handler.success = loadSubjectiveQuestion;
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getQuestionUrl,handler,true);
</script>
</head>
<body>
<div id="subjectiveDiv" class="container">
	<form action="addquestion" method="post" enctype="multipart/form-data" id="addSubjectiveQuestion" class="form-horizontal corex-form-container">
 			<div class="form-group">
 			<label for="examname" class="col-md-2 control-label">Question </label>
 			<div class="col-md-10">
 				<textarea rows="5" cols="100" required id="subjectiveQuestion" name="question" class="form-control"></textarea>
 				<span class="validation-message" id="subjectiveQuestionError"></span>
 			</div>
 			</div>
 			<div class="form-group">
				<label for="questionmarks" class="col-sm-2 control-label">Marks</label>
				<div class="col-sm-2">
					<input type="number" required class="form-control" id="questionmarks" name="questionmarks" maxlength="5" size="5" style="width: 50%;" min="1"/>
					<span class="validation-message" id="questionMarksError"></span>
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
						<input type="file" accept=".jpeg,.gif,.bmp,.jpg" id="addSubjectiveQuestionImage"/>
					</span>
					<span class="validation-message" id="questionMarksError"></span>
				</div>
			</div>
			
			<div class="form-group">
			<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
 			<div id="subjectiveImageRow">
 				
 			</div>
 			</div>
 			</div>
 			<div class="form-group">
			<label class="col-sm-2 control-label">&nbsp;</label>
				<div class="col-sm-10">
					<input type="button" class="btn btn-default" id="saveSubjectiveExam" value="Save"/>
					<input type="button" value="Cancel" class="btn btn-link" id="cancelEdit"/>					
				</div>
		</div>
 	</form>
 	
 	<form action="searchTeacherQuestion" method="post" id="searchQuestion">
		<input type="hidden" value="<c:out value="${division}"></c:out>" name="classId"/>
		<input type="hidden" value="<c:out value="${subject}"></c:out>" name="subId"/>
		<input type="hidden" value="<c:out value="${inst_id}"></c:out>" name="inst_id"/>
		<input type="hidden" value="1" name="questionType"/>
		
	</form>
 	</div>
</body>
</html>