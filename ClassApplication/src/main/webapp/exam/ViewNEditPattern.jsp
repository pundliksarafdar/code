<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
.col-md-1,.col-md-2,.col-md-3{
padding-left: 10px;
padding-right: 10px;
}

.form-control{
	font-size:12px;
	height:25px;
    padding-top: 1px;
    padding-bottom: 1px;
    padding-left: 5px;
    padding-right: 5px;


}
</style>
<script type="text/javascript">
var division  = "";
$(document).ready(function(){
	$("#searchPattern").click(function(){
		division = $("#division").val();
		var patternType = $("#patternType").val();
		var obj = {};
		/* obj.division = division;
		obj.patternType = patternType; */
		var handlers = {};
		handlers.success = function(e){
		console.log("Success",e)
		$("#patternListTable").find("tbody").empty();
		if(e.length > 0)
		{
			for(var i=0; i<e.length; i++){
			$("#patternListTable").find("tbody").append("<tr><td>"+(i+1)+"</td><td>"+e[i].pattern_name+"</td><td>"+e[i].marks+"</td><td><button class='btn btn-primary btn-xs viewPattern' id='"+e[i].pattern_id+"'>View/Edit</button></td><td><button class='btn btn-danger btn-xs deletePattern' id='"+e[i].pattern_id+"'>Delete</button></td>");
		}
		}else{
			$("#patternListTable").find("tbody").append("<tr><td colspan='5' align='center'>Paterns not available for selected criteria</td></tr>");
		}	
		};
		handlers.error = function(e){console.log("Error",e)};
		rest.post("rest/classownerservice/searchQuestionPaperPattern/"+division+"/"+patternType,handlers,obj,false);
	});
	
	$("#patternListTable").on("click",".viewPattern",function(){
		$("#viewPatternDiv").show();
		$("#patternListTable").hide();
		var patternid = $(this).attr("id");
		var handlers = {};
		handlers.success = function(e){
		console.log("Success",e)
		recursiveView(e);
		};
		handlers.error = function(e){
		console.log("Error",e)}
		rest.post("rest/classownerservice/getQuestionPaperPattern/"+division+"/"+patternid,handlers);
	});
	
	$(".cancleView").click(function(){
		$("#viewPatternDiv").hide();
		$("#patternListTable").show();
		
		
	});
});

function recursiveView(data){
	
}
</script>
</head>
<body>
<ul class="nav nav-tabs" style="border-radius: 10px">
		<li><a href="createExamPatten">Create
				Exam Paper Pattern</a></li>
		<li class="active"><a href="#viewneditpattern">View/Edit Exam Pattern</a></li>
		<li><a href="viewstudent">Set Question Paper</a></li>
		<li><a href="viewstudent">View/Edit Question paper</a></li>
		<li><a href="viewstudent">Create Exam</a></li>
		<li><a href="viewstudent">View/Edit Exam</a></li>
	</ul>
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
			<div class="col-md-3">
				<select id="patternType" name="patternType" class="form-control">
					<option value="-1">Any Pattern Type</option>
					<option value="WS">Pattern With Section</option>
					<option value="WOS">Pattern Without Section</option>
				</select>
			</div>
			<div class="col-md-1">
				<button class="form-control btn btn-primary btn-sm" id="searchPattern">Search</button>
			</div>
		</div>
	</div>
	<div class="container">
		<table class="table" id="patternListTable">
		<thead>
			<tr>
				<th>Sr No.</th>
				<th>Pattern Description</th>
				<th>Marks</th>
				<th>View/Edit</th>
				<th>Delete</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
	</div>
	<div id="viewPatternDiv" style="display: none;" class="container">
		<div class="row">
			<div class="col-md-1"><button class="cancleView btn btn-primary btn-xs" >Back To List</button></div>
			<div class="col-md-1"><button class="editPattern btn btn-primary btn-xs" >Edit</button></div>
			<div class="col-md-10"></div>
		</div>
		<ul id="examPattern" style="padding: 0%"></ul>
	</div>
</body>
</html>