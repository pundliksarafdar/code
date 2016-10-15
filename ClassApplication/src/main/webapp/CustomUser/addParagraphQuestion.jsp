<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
#uploadParaQuestionImage{
	height:20px;
	width:20px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/view/addCustomUserParagraphQuestion.js"></script>
</head>
<body>
<div id="paragraphDiv" style="display: none" class="container">
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
			<div id="" class="col-md-10">
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
			</div>
		</div>
	</form>
</div>

<div id="parahiddenQuestion" class="hide">
	<div class="paraQuestionTmpl">
			<div class="input-group">
		      <input type="text" class="form-control">
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