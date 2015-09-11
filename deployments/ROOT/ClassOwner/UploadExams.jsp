<div>
<h3>Upload Exam</h3>

<div class="container bs-callout bs-callout-danger">
	<form method="post" action="uploadexams">
	<input type="hidden" name="actionname" value="submitmarks"/>
		<div class="row">
			<div class="col-xs-4">
				<label for="examname">Exam Name</label>
				<input type="text" class="form-control" id="examname" name="examname" required/>
			</div>
			<!-- 
			<div class="col-xs-4" style="display:none">
				<label for="exammarks">Total Marks</label>
				<input type="number" class="form-control" id="exammarks" name="exammarks" required/>
			</div>
			 -->
			<div class="col-xs-4">
				<div>&nbsp;</div>
				<input type="submit" class="btn btn-default" value="Ok"/>
			</div>
		</div>
	</form>
</div>
</div>