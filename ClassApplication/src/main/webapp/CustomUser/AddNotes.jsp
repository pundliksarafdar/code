<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- <link href="../css/bootstrap.min.css" rel="stylesheet">
 <link href="../css/admin.css" rel="stylesheet">
 <script src="../js/jquery-1.10.2.min.js"></script>
 <script src="../js/bootstrap.min.js"></script> -->
 
 <style type="text/css">
 .error{
     color: red;
    margin-left: 10px;
}

.div {
    border-radius: 25px;
    border: 2px solid ;
    padding: 20px; 
   
}      
#basenotestable_paginate{
display: none;
}

#basenotestable_info{
display: none;
}

#basenotestable_length{
display: none;
}

#basenotestable_filter{
display: none;
}
 </style>
<script type="text/javascript">
var allbatches="";
var globalnotesrowcounter=0;
var noofrows=0;
var deletedrows=[];
var SUBJECT_DROPDOWN = ".subjectDropDown";	
var BATCH_DROPDOWN = ".batchDropDown";
var ADD_BUTTON = "#classownerUploadexamAddExam";
function validate()
{
var file=document.getElementByID("myFile");

	return false;
}

$(document).ready(function(){
	$("#basenotestable").DataTable();
	$("#classownerUploadexamSelectBatchName").select2({data:"",placeholder:"Select Batch"});
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
			$("#divisionError").html("");
			
		}else{
			$("#classownerUploadexamSubjectNameSelect").select2("val", "");
			$("#classownerUploadexamSubjectNameSelect").html("");
			$("#classownerUploadexamSubjectNameSelect").select2({data:"",placeholder:"Select Subject"});
			$("#batchError").html("");
			$("#classownerUploadexamSelectBatchName").select2("val", "");
			$("#classownerUploadexamSelectBatchName").html("");
			$("#classownerUploadexamSelectBatchName").select2({data:"",placeholder:"Select Batch"});
		}
	});
	
	$("#classownerUploadexamSubjectNameSelect").on("change",function(e){
		var uploadExam = new UploadExam();
		var subjectId = $(this).val();
		var divisionId = $("#classownerUploadexamDivisionName").val();
		if(subjectId!="-1" && subjectId != "" && subjectId != null){
			$("#subjectError").html("");
			uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId);	
		}else{
			$("#batchError").html("");
			$("#classownerUploadexamSelectBatchName").select2("val", "");
			$("#classownerUploadexamSelectBatchName").html("");
			$("#classownerUploadexamSelectBatchName").select2({data:"",placeholder:"Select Batch"});
			
		}
	});
	
	$("#classownerUploadexamSubjectName").on("change",function(e){
		var uploadExam = new UploadExam();
		$(ADD_BUTTON).hide();
		if($(this).val()!=-1){
			$(ADD_BUTTON).show();
		}
	});
	
	
	
	$("#submit").click(function(event){
	//	event.preventDefault();
	$("#divisionError").html("");
	$("#subjectError").html("");
	$("#batchError").html("");
	var division = $("#classownerUploadexamDivisionName").val();
	var subject = $("#classownerUploadexamSubjectNameSelect").val();
	var batch = $("#classownerUploadexamSelectBatchName").val();
	var flag=true;
	var allnotesnames="";
	var allnotesrowid="";
	var i=0;
	var filesize=0;
	if(division == "-1"){
		$("#divisionError").html("Select Class");
		flag= false;
	}
	if(subject == "-1" || subject == "" || subject == null){
		$("#subjectError").html("Select Subject");
		flag= false;
	}
	if(batch == "" || batch == null){
		$("#batchError").html("Select Batch");
		flag= false;
	}
	for(i=0;i<=globalnotesrowcounter;i++){
		var internalflag=false;
		for(var j=0;j<deletedrows.length;j++){
			if(deletedrows[j]==i){
				internalflag=true;	
			}
		}
		if(internalflag==false){
		var file=document.getElementById("myfile"+i).value;
		var notesname=$("#notesname"+i).val();
		if(""==allnotesnames){
		allnotesnames=notesname;
		allnotesrowid=i;
		}else{
			allnotesnames=allnotesnames+','+notesname;
			allnotesrowid=allnotesrowid+','+i;
		}
		var validforbatch=$("#validforbatch:checked").val();
		//var batch=$("#batch").val();
		var noteserror=$("#noteserror"+i);
		var regex = /^[a-zA-Z0-9 ]*$/;
		noteserror.html("");
		var batchidmap;
		
		if(file==""){
			noteserror.html("Please select file");
			flag=false;
		}else{
			var filesizetemp=document.getElementById("myfile"+i).files[0].size/1024/1024;
			filesize=filesize+filesizetemp;
			if(filesize > 5){
				/* noteserror.html("File size should be less than 5 MB"); */
				flag=false;
			}
		}
		if(notesname==""){
			noteserror.html("Please enter notes name");
			flag=false;
		}else if(!notesname.match(regex)){
			noteserror.html("Please enter valid notes name (No special characher allowed) ");
			flag=false;
		}
		}
	}
		if(flag==true){
			var  notesflag=false;
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "customUserValidateNotesName",
				    	 notes: allnotesnames,
				    	 notesrowid:allnotesrowid,
				    	 filesize:filesize,
				    	 division:division,
				    	 subject:subject
				   		},
				   		async: false,
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   var notesstatuts=resultJson.notesname;
					   var overlappedIds=resultJson.overlappedIds.split("/");
					   var notesnamestatus=resultJson.notesnamestatus.split(",");
					   if(overlappedIds[0]==""){
						   var allocmemory=resultJson.allocmemory;
						   if(allocmemory==""){
						   if(notesnamestatus[0]!=""){
					  for(var s=0;s<notesnamestatus.length;s++){
						  $("#noteserror"+notesnamestatus[s]).html("Notes name already exists,Please enter different names");
					  }
					  flag=false;
						   }
						   }else{
							   flag=false;
							   $("#memorylimitalert").modal("toggle");
						   }
					   }else{
						   for(var k=0;k<overlappedIds.length;k++){
							   $("#noteserror"+overlappedIds[k].split(",")[0]).html("Please enter unique names");
							   $("#noteserror"+overlappedIds[k].split(",")[1]).html("Please enter unique names");
						   }
						   flag=false;
					   }
					   
					   if(flag==true){
						   
							
							var formData = new FormData();
							for(i=0;i<=globalnotesrowcounter;i++){
							var file=$("#myfile"+i).prop('files')[0];
							var notesname = $("#notesname"+i).val();
								formData.append("uploadedFile"+i,file);
								formData.append("notesname"+i,notesname);
							}
							var uri = "rest/commonservices/customUseruploadNotes/"+globalnotesrowcounter+"/"+division+"/"+subject+"/"+batch;
							var handlers = {};
							handlers.success = function(e){
								$.notify({message: 'Notes saved'},{type: 'success'});
							};
							handlers.error = function(e){};
							rest.uploadNotes(uri,formData,handlers);
							console.log(form);
					   }
					  
				   },
					error:function(){
				   		modal.launchAlert("Error","Error");
				   	}
				   });
	/* 		if(notesflag==true){
				noteserror.html("Please enter different notes name");
				flag=false;
			} */
		}
		
		if(flag==false){
			if(filesize>5){
			$("#noteslimitalert").modal("toggle");
			}
			event.preventDefault();
		}else{
			
			
		}
			
	
	});
	
	
$("#addrow").click(function(event){
	event.preventDefault();
	if(noofrows==0){
		$(".removenotesrow").prop('disabled',false);
	}
	globalnotesrowcounter++;
	noofrows++;
	$("#basenotestablebody").append('<tr id="notesTR'+globalnotesrowcounter+'">'
	+'<td><input type="text" name="notesname" class="form-control" id="notesname'+globalnotesrowcounter+'" maxlength="50"></td>'
	+'<td><input type="file" name="myFile" accept=".pdf" class="form-control"  size="100px" id="myfile'+globalnotesrowcounter+'"></td>'
	+'<td><button class="btn btn-info removenotesrow" id="notesdelete_'+globalnotesrowcounter+'"><i class="glyphicon glyphicon-trash"></i></button></td>'
	+'<td><span class="error" id="noteserror'+globalnotesrowcounter+'" name="noteserror"></span></td>'
	+'</tr>');
	
});

$("#basenotestablebody").on("click",".removenotesrow",function(event){
	event.preventDefault();
	var id=$(this).prop("id").split("_");
	$("#notesTR"+id[1]).remove();
	deletedrows.push(id[1]);
	noofrows--;
	if(noofrows==0){
		$(".removenotesrow").prop('disabled',true);
	}
});

});

function showalert(){
	
	$("#notesaddedalert").modal('toggle');
}
function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	var handlers = {};
	handlers.success=function(data){
		 $("#classownerUploadexamSubjectNameSelect").removeAttr('disabled');
		   displaySubjectDropDown(data);
	};   
	handlers.error=function(){
		$.notify({message: "Error"},{type: 'danger'});
	};   
	
	rest.get("rest/customuserservice/getSubjectOfDivision/"+division,handlers);
	}
	
	
	var getBatchFromDivisonNSubject = function(subjectId,divisionId){
	
	/* $.ajax({
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
		   }); */
	var handlers = {};
	handlers.success=function(data){
		displayBatchFromSubjectNDivision(data);
	};   
	handlers.error=function(){
		$.notify({message: "Student not deleted"},{type: 'danger'});
	};   
	
	rest.get("rest/customuserservice/getBatchesByDivisionNSubject/"+divisionId+"/"+subjectId,handlers);
	}
	
	var displaySubjectDropDown = function (data){
		var selectOptionDropdown = "#classownerUploadexamSubjectNameSelect";
		$(selectOptionDropdown).empty();
		if(data.length > 0){
		$("<option/>",{
			value:"-1",
			text:"Select Subject"
		}).appendTo(selectOptionDropdown);
			for(subjectNameIndex in data){
				$("<option/>",{
					value:data[subjectNameIndex].subjectId,
					text:data[subjectNameIndex].subjectName
				}).appendTo(selectOptionDropdown);
			}
			$(selectOptionDropdown).select2().val("-1").change();
			batchDataArray = [];
			$("#batchError").html("");
			$("#classownerUploadexamSelectBatchName").html("");
			$("#classownerUploadexamSelectBatchName").select2({data:batchDataArray,placeholder:"Select Batch"});
			$("#classownerUploadexamSelectBatchName").select2("val", "");
			
		}else{
			$(selectOptionDropdown).select2({data:"",placeholder:"Subjects not available"});
			$("#batchError").html("");
			$("#classownerUploadexamSelectBatchName").select2("val", "");
			$("#classownerUploadexamSelectBatchName").html("");
			$("#classownerUploadexamSelectBatchName").select2({data:"",placeholder:"Select Batch"});
		}
		
	}
	
	var displayBatchFromSubjectNDivision = function (data){
		var selectOptionDropdown = "#classownerUploadexamSelectBatchName";
		var classownerUploadexamSelectBatchNameMenu = "#classownerUploadexamSelectBatchNameMenu";
		/* data = JSON.parse(data);
		data = data.batchlist;
		data = JSON.parse(data); */
		$(classownerUploadexamSelectBatchNameMenu).children().not(".staticMenu").remove();
		$(classownerUploadexamSelectBatchNameMenu).find(".selectAllCheckbox").prop("checked",false);
		if(data.length !== 0){
		var batchDataArray = [];
		
		var index=0;
		$.each(data,function(index,subjectData){
			/* var batchName = subjectData.batch_name;
			var batchId = subjectData.batch_id;
			var optionMenu = '<li><a href="#"> <input id="checkButton'+index+'" type="checkbox" name="batch" value="'+batchId+'"><label for="checkButton'+index+'">'+batchName+'</label></a></li>'
			$(classownerUploadexamSelectBatchNameMenu).append(optionMenu);
			index++; */
			var batchDataObject = {};
			batchDataObject.id = subjectData.batch_id;
			batchDataObject.text = subjectData.batch_name;
			batchDataArray.push(batchDataObject);
		});
		$("#classownerUploadexamSelectBatchName").empty();
		$("#classownerUploadexamSelectBatchName").select2({data:batchDataArray,placeholder:"Select Batch"});
		$("#classownerUploadexamSelectBatchName").prop("disabled",false);
		}else{
			$("#classownerUploadexamSelectBatchName").prop("disabled",true);
			$(".alert-danger").text("Subjects for selected batch are not added.").show();
		}
	}
	
	
	this.getSubjectsInDivision = getSubjectsInDivision;
	this.displaySubjectDropDown = displaySubjectDropDown;
	this.getBatchFromDivisonNSubject = getBatchFromDivisonNSubject;
	this.displayBatchFromSubjectNDivision = displayBatchFromSubjectNDivision;
	}
</script>
</head>
<body>
<div>
<%
String notes=(String)request.getAttribute("notes");
if(notes!=null){
	request.setAttribute("notes", null);
%>
<div class="modal fade" id="notesaddedalert" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           Notes Added successfully
         </div>
         </div>
   </div>
</div>

</div>
<%} %>
<jsp:include page="notesHeader.jsp" >
		<jsp:param value="active" name="customUserAddNotes"/>
	</jsp:include>

<div id="addnotestab">
<div class="well" >
<div class="row">
			<div class="col-md-3">
				<select name="division" id="classownerUploadexamDivisionName" class="form-control" width="100px">
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="divisionError" class="error"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="classownerUploadexamSubjectNameSelect" class="form-control" width="100px" >
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectError" class="error"></span>
			</div>
			<div class="col-md-3 batchDropDown">
				<select name="batch" id="classownerUploadexamSelectBatchName" class="form-control" width="100px" multiple="multiple">
				</select>
				<span id="batchError" class="error"></span>
			</div>
		</div>
		</div>
<form action="javascript:void(0)" method="post" enctype="multipart/form-data" role="form" id="form">
<input type="hidden" id="batch" name="batch" value="<c:out value="${batch}" ></c:out>">
<input type="hidden" id="division" name="division" value="<c:out value="${division}" ></c:out>">
<input type="hidden" id="subject" name="subject" value="<c:out value="${subject}" ></c:out>">		
<div class="container" id="basenotesdiv">
	<table id="basenotestable" class="table table-striped">
	<thead>
	<tr>
	<th class='col-md-3'>Name</th>
	<th class='col-md-3'>File</th>
	<th class='col-md-3'>Delete</th>
	<th class='col-md-3'></th>
	</tr>
	</thead>
	<tbody id="basenotestablebody">
	<tr id="notesTR0">
		<td style="width: 25%"><input type="text" name="notesname" class="form-control" id="notesname0" maxlength="50"></td>
		<td style="width: 30%"><input type="file" name="myFile" accept=".pdf" class="form-control"  size="100px" id="myfile0"></td>
		<td style="width: 5%"><button class="btn btn-info removenotesrow" id="notesdelete_0" disabled="disabled"><i class="glyphicon glyphicon-trash"></i></button></td>
		<td style="width: 40%"><span class="error" id="noteserror0" name="noteserror"></span></td>
	</tr>
	</tbody>
	</table>
	
</div>
<div class="container">
<div class="row">
	<div class="col-md-2">
	 <button class="btn btn-info btn-sm" id="addrow" style="margin-top: 22px"><i class="glyphicon glyphicon-plus"></i>&nbsp;Add Row</button>
      </div>
      <div class="col-md-2">
      <button type="submit" class="btn btn-info btn-sm" id="submit" style="margin-top: 22px">Submit</button>
  		</div>
  </div>
  </div>
</form>
</div>
<div class="modal fade" id="noteslimitalert" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           You Cannot add more than 5 MB at a time.
         </div>
         </div>
   </div>
</div>	
<div class="modal fade" id="memorylimitalert" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           You dont have enough space to add notes.
         </div>
         </div>
   </div>
</div>		
</body>
</html>