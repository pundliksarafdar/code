<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<script>

var SUBJECT_DROPDOWN = ".subjectDropDown";	
var BATCH_DROPDOWN = ".batchDropDown";
var ADD_BUTTON = "#classownerUploadexamAddExam";
$(document).ready(function(){
	//$(SUBJECT_DROPDOWN).hide();
	//$(BATCH_DROPDOWN).hide();
	//$(ADD_BUTTON).hide();
	$('#classes').change(function(){
		var classes = $('#classes').val();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getsubjectsanddivisions",
			    	 classes: classes
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var subjectnames=resultJson.subjectnames.split(",");
				   var subjectids=resultJson.subjectids.split(",");
				   var divisionnames=resultJson.divisionnames.split(",");
				   var divisionids=resultJson.divisionids.split(",");
				   
				   var divisionselect=$('#classownerUploadexamDivisionName');
				   $("#divisionerror").html("");
				   divisionselect.empty();
				   divisionselect.append("<option value='-1'>Select Division</option>");
				   if(divisionids[0]!=""){
					   var i=0;
					   while(i<divisionids.length){
						   divisionselect.append("<option value="+divisionids[i]+">"+divisionnames[i]+"</option>");
						   i++;
					   }
					   
				   }else{
					   $("#divisionerror").html("No Division present");
				   }
				   
			   },
				error:function(){
			   		modal.launchAlert("Error","Error");
			   	}
			   });
		
	});
	
	$("select").on("change",function(e){
		$(".alert-danger").hide();
	});
	
	$("#classownerUploadexamDivisionName").on("change",function(e){
		//$(SUBJECT_DROPDOWN).hide();
		//$(BATCH_DROPDOWN).hide();
		//$(ADD_BUTTON).hide();
		if($(this).val()!=-1){
			var uploadExam = new UploadExam();
			uploadExam.getSubjectsInDivision($(this).val());
		}else{
			$("#classownerUploadexamSubjectNameSelect").prop("disabled",true);
			$("#classownerUploadexamBatchName").prop("disabled",true);
			$("#classownerUploadexamAddExam").prop("disabled",true);
		}
	});
	
	$("#classownerUploadexamSubjectNameSelect").on("change",function(e){
		var uploadExam = new UploadExam();
		var subjectId = $(this).val();
		var divisionId = $("#classownerUploadexamDivisionName").val();
		if(subjectId!="-1"){
		uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId);
		}else{
			$("#classownerUploadexamBatchName").prop("disabled",true);
		}
	});
	
	$("#classownerUploadexamSubjectName").on("change",function(e){
		var uploadExam = new UploadExam();
		$(ADD_BUTTON).hide();
		if($(this).val()!=-1){
			$(ADD_BUTTON).show();
		}
	});
	
		
	$("#classownerUploadexamSelectBatchName").multiselect().on("allDeslected",function(){$("#classownerUploadexamAddExam").prop("disabled",true);}).
	on("selected",function(){$("#classownerUploadexamAddExam").prop("disabled",false);});
	
});

function UploadExam(){

var getSubjectsInDivision = function(division){
	var institute=$("#classes").val();
$.ajax({
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "getSubjectOfDivision",
	    	 divisionId: division,
	    	 institute:institute
	   		},
	   type:"POST",
	   success:function(data){
		   $("#classownerUploadexamSubjectNameSelect").removeAttr('disabled');
		   displaySubjectDropDown(data);
	   },
		error:function(){
	   		modal.launchAlert("Error","Error");
	   	}
	   });
}


var getBatchFromDivisonNSubject = function(subjectId,divisionId){
	var institute=$("#classes").val();
$.ajax({
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "getBatchesByDivisionNSubject",
	    	 divisionId: divisionId,
			 subjectId:subjectId,
			 institute:institute
	   		},
	   type:"POST",
	   success:function(data){
		   console.log(data);
		   displayBatchFromSubjectNDivision(data);
	   },
		error:function(){
	   		modal.launchAlert("Error","Error");
	   	}
	   });
}

var displaySubjectDropDown = function (data){
	var selectOptionDropdown = "#classownerUploadexamSubjectNameSelect";
	$(selectOptionDropdown).find("option:not(:first)").remove();
	data = JSON.parse(data);
	if(data.subjectnames.trim()!=="" || data.subjectids.trim()!==""){
	var subjectnames = data.subjectnames.split(",");
	var subjectids = data.subjectids.split(",");
		for(subjectNameIndex in subjectnames){
			console.log(subjectnames[subjectNameIndex]);
			console.log(subjectids[subjectNameIndex]);
			$("<option/>",{
				value:subjectids[subjectNameIndex],
				text:subjectnames[subjectNameIndex]
			}).appendTo(selectOptionDropdown);
		}
	}else{
		$(SUBJECT_DROPDOWN).hide();
		$(".alert-danger").text("Batch for selected division are not added.").show();
	}
	
}

var displayBatchFromSubjectNDivision = function (data){
	var selectOptionDropdown = "#classownerUploadexamBatchName";
	var classownerUploadexamBatchNameMenu = "#classownerUploadexamBatchNameMenu";
	data = JSON.parse(data);
	data = data.batchlist;
	data = JSON.parse(data);
	console.log(data);
	
	//$(classownerUploadexamBatchNameMenu).children().not(".staticMenu").remove();
	if(data.length !== 0){
	var index=0;
	$.each(data,function(index,subjectData){
		var batchName = subjectData.batch_name;
		var batchId = subjectData.batch_id;
		var optionMenu = '<li><a href="#"> <input id="checkButton'+index+'" type="checkbox" name="batch" value="'+batchId+'"><label for="checkButton'+index+'">'+batchName+'</label></a></li>'
		$(classownerUploadexamBatchNameMenu).append(optionMenu);
		index++;
	});
	$("#classownerUploadexamBatchName").prop("disabled",false);
	}else{
		$("#classownerUploadexamBatchName").prop("disabled",true);
		$(".alert-danger").text("Subjects for selected batch are not added.").show();
	}
}


this.getSubjectsInDivision = getSubjectsInDivision;
this.displaySubjectDropDown = displaySubjectDropDown;
this.getBatchFromDivisonNSubject = getBatchFromDivisonNSubject;
this.displayBatchFromSubjectNDivision = displayBatchFromSubjectNDivision;
}
</script>

<form method="post" action="<c:out value="${forwardAction}" ></c:out>">
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	<div class="row">
		<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
			 
		</div>
	</div>
	<div class="row">
	<div class="col-md-3">
	     <select class="form-control" name="institute" id="classes">
      <option value="-1">Select Institute</option>
      <%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("classes"); 
      if(list!=null){
      for(int i=0;i<list.size();i++)
      {
      %>
      <option value="<%=list.get(i).getRegId()%>"><%=list.get(i).getClassName()%></option>
      <%} }%>
      </select>
      
	</div>
		<div class="col-md-3">
			<select name="division" id="classownerUploadexamDivisionName" class="form-control" width="100px">
				<option value="-1">Select Division</option>
				<c:forEach items="${requestScope.divisions}" var="division">
					<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
				</c:forEach>							
			</select>
		</div>
		<div class="col-md-3 subjectDropDown">
			<select name="subject" id="classownerUploadexamSubjectNameSelect" class="form-control" width="100px" disabled="disabled">
				<option value="-1">Select Subject</option>
			</select>
		</div>
		
		<div class="col-md-3 batchDropDown">
			<div class="dropdown" id="classownerUploadexamSelectBatchName" >
			  <button class="btn btn-default dropdown-toggle" type="button" id="classownerUploadexamBatchName" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" disabled="disabled">
				Select Batch
				<span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu" aria-labelledby="classownerUploadexamSubjectNameMenu" id="classownerUploadexamBatchNameMenu">
				<li class="staticMenu"><a href="#" id="selectAll"> <input type="checkbox" class="selectAllCheckbox" id="classownerUploadexamSubjectNameMenuselectAllRadio"><label for="classownerUploadexamSubjectNameMenuselectAllRadio">Select All<label></a></li>
				<li class="staticMenu divider" role="separator" class=""></li>
				
			  </ul>
			</div>
			
		</div>
		
		<div class="col-md-3">
			<button id="classownerUploadexamAddExam" type="submit" class="btn btn-info" disabled>Add</button>
		</div>
		<div class="col-md-3">
		
		</div>
	</div>
	<div class="row">
		
	</div>
</div>
</form>
</body>