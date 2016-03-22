	<!-- URL : choosesubject?forwardAction=generateexampreaction&hideBatch=true
		forwardAction is next action on submit button
		hideBatch set to true if you want to ignore the batch selection if it not given or true then you can see batch dropdown 
	 -->
	
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
	var hideBatch = <c:out value="${hideBatch}" ></c:out>;
	var batchDefault = <c:out value="${batchDefault}" ></c:out>;
	
	$(document).ready(function(){
		//$(SUBJECT_DROPDOWN).hide();
		//$(BATCH_DROPDOWN).hide();
		//$(ADD_BUTTON).hide();
		
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
			
			if(hideBatch == true){
				$("#classownerUploadexamAddExam").prop("disabled",false);
			}else{
				uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId);	
			}
			
			//If default batch is enabled then enable submit button and fill the batch dropdown also
			if(batchDefault == true){
				$("#classownerUploadexamAddExam").prop("disabled",false);
				uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId);	
			}
			}else{
				if(hideBatch == true || batchDefault == true){
					$("#classownerUploadexamAddExam").prop("disabled",true);
				}else{
					$("#classownerUploadexamBatchName").prop("disabled",true);
				}
			}
		});
		
		$("#classownerUploadexamSubjectName").on("change",function(e){
			var uploadExam = new UploadExam();
			$(ADD_BUTTON).hide();
			if($(this).val()!=-1){
				$(ADD_BUTTON).show();
			}
		});
		
		if(hideBatch != true){
			$("#classownerUploadexamSelectBatchName").multiselect().on("allDeslected",function(){
				$("#classownerUploadexamAddExam").prop("disabled",true);
			}).
			on("selected",function(){$("#classownerUploadexamAddExam").prop("disabled",false);});
		}else{
			$("#classownerUploadexamAddExam").prop("disabled",true);
		}
	});
	
	function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getSubjectOfDivision",
		    	 divisionId: division
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
	
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getBatchesByDivisionNSubject",
		    	 divisionId: divisionId,
				 subjectId:subjectId
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
			$("#classownerUploadexamSubjectNameSelect").prop("disabled",true);
			$("#classownerUploadexamBatchName").prop("disabled",true);
			if(!hideBatch){
				$(".alert-danger").text("Batch for selected division are not added.").show();
			}
		}
		
	}
	
	var displayBatchFromSubjectNDivision = function (data){
		var selectOptionDropdown = "#classownerUploadexamBatchName";
		var classownerUploadexamBatchNameMenu = "#classownerUploadexamBatchNameMenu";
		data = JSON.parse(data);
		data = data.batchlist;
		data = JSON.parse(data);
		$(classownerUploadexamBatchNameMenu).children().not(".staticMenu").remove();
		$(classownerUploadexamBatchNameMenu).find(".selectAllCheckbox").prop("checked",false);
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
		
		<div align="center" style="font-size: larger;margin-bottom: 15px"><u><c:out value="${lable}"></c:out></u></div>
		<div class="row">
			<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
				 
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				<select name="division" id="classownerUploadexamDivisionName" class="form-control" width="100px">
					<option value="-1">Select Class</option>
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
			<c:if test="${hideBatch ne 'true'}">
			<div class="col-md-3 batchDropDown">
				<div class="dropdown" id="classownerUploadexamSelectBatchName" >
				  <button class="btn btn-default dropdown-toggle" type="button" id="classownerUploadexamBatchName" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" disabled="disabled">
					<label>Select Batch</label>
					<span class="caret"></span>
				  </button>
				  <ul class="dropdown-menu" aria-labelledby="classownerUploadexamSubjectNameMenu" id="classownerUploadexamBatchNameMenu">
					<li class="staticMenu"><a href="#" id="selectAll"> <input type="checkbox" class="selectAllCheckbox" id="classownerUploadexamSubjectNameMenuselectAllRadio"><label for="classownerUploadexamSubjectNameMenuselectAllRadio">Select All<label></a></li>
					<li class="staticMenu divider" role="separator" class=""></li>
				  </ul>
				</div>
				
			</div>
			</c:if>
			
			<div class="col-md-3">
				<button id="classownerUploadexamAddExam" type="submit" class="btn btn-info" disabled>Continue</button>
			</div>
			<div class="col-md-3">
			
			</div>
		</div>
		<div class="row">
			
		</div>
	</div>
	</form>
	</body>