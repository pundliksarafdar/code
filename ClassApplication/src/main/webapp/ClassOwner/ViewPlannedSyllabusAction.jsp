<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div>
					<label>Teacher</label>
					<ul class="list-group" id="teachersFilter" data-filter="teacher"></ul>
				</div>
				<div>
					<label>Subject</label>
					<ul class="list-group" id="subjectsFilter" data-filter=subject></ul>
				</div>
				<div>
					<label>Division</label>
					<ul class="list-group" id="divisionsFilter" data-filter="division"></ul>
				</div>
				<!-- <div>
					<label>Batch</label>
					<ul class="list-group" id="batchFilter" data-filter="batchs">
						<li class="list-group-item">
						    <span><input type="checkbox" value=""></span>
						    <label></label>
						</li>
					</ul>
				</div> -->
			</div>
			<div class="col-md-9">
				<div class='input-group date form-group' id='syllabusSearchTime' >
                   <input type='text' class="form-control" name="syllabusSetTime" required placeholder="Date"/>
                   <span class="input-group-addon">
                       <span class="glyphicon glyphicon-calendar"></span>
                   </span>
               </div>
               <table id="syllabusTable" class="table"/>
			</div>
		</div>
	</div>
</body>
</html>