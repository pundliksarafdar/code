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

 </style>
<script type="text/javascript">
var allbatches="";
var globalnotesrowcounter=0;
var noofrows=0;
var deletedrows=[];
var SUBJECT_DROPDOWN = ".subjectDropDown";	
var BATCH_DROPDOWN = ".batchDropDown";
var ADD_BUTTON = "#classownerUploadexamAddExam";
var teacherSubjectArray = [];
var divisionTempData = {};
divisionTempData.id = "-1";
divisionTempData.text = "Select Class";
var subjectTempData = {};
subjectTempData.id = "-1";
subjectTempData.text = "Select Subject";
var batchTempData = {};
batchTempData.id = "-1";
batchTempData.text = "Select Batch";

function validate()
{
var file=document.getElementByID("myFile");

	return false;
}

$(document).ready(function(){
	
	$("#instituteSelect").change(function(){
		var divisionArray = [];
		var subjectArray = [];
		var batchArray = [];
		divisionArray.push(divisionTempData);
		subjectArray.push(subjectTempData);
		batchArray.push(batchTempData);
		$("#divisionSelect").empty();
		$("#subjectSelect").empty();
		$("#batch").empty();
		 $("#divisionSelect").select2({data:divisionArray,placeholder:"Type Class Name"});
		$("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
		 $("#batch").select2({data:null,placeholder:"Type Batch Name"});
		 
		var inst_id = $(this).val();
		if(inst_id != "-1"){
		$("#instituteError").html("");
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		var divisionArray = [];
		if(e.divisionList.length > 0){
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 		teacherSubjectArray = e.subjectList;
	    $("#divisionSelect").select2({data:divisionArray,placeholder:"Type Topic Name"});
		}else{
			$("#divisionSelect").empty();
 	 		$("#divisionSelect").select2({data:"",placeholder:"Class not available"});	
		}
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}
	});
	
	$("select").on("change",function(e){
		$(".alert-danger").hide();
	});
	
	 $("#divisionSelect").on("change",function(e){
		 var subjectArray = [];
			var batchArray = [];
			subjectArray.push(subjectTempData);
			batchArray.push(batchTempData);
			$("#subjectSelect").empty();
			$("#batch").empty();
			$("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
			$("#batch").select2({data:null,placeholder:"Type Batch Name"});
		if($(this).val()!=-1){
			var uploadExam = new UploadExam();
			uploadExam.getSubjectsInDivision($(this).val());
			$("#divisionError").html("");
			
		}
	}); 
	
	$("#subjectSelect").on("change",function(e){
		var batchArray = [];
		batchArray.push(batchTempData);
		$("#batch").empty();
		$("#batch").select2({data:null,placeholder:"Type Batch Name"});
		var uploadExam = new UploadExam();
		var subjectId = $(this).val();
		var divisionId = $("#divisionSelect").val();
		var inst_id = $("#instituteSelect").val();
		if(subjectId!="-1"){
			$("#subjectError").html("");
			uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId,inst_id);	
		}else{
			$("#batchError").html("");
			$("#batch").select2({data:batchDataArray,placeholder:"type batch name"});
			$("#batch").select2("val", "");
			$("#batch").prop("disabled",true);
			
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
	var inst_id = $("#instituteSelect").val();
	$("#divisionError").html("");
	$("#subjectError").html("");
	$("#batchError").html("");
	var division = $("#divisionSelect").val();
	var subject = $("#subjectSelect").val();
	var batch = $("#batch").val();
	var flag=true;
	var allnotesnames="";
	var allnotesrowid="";
	var i=0;
	var filesize=0;
	if(division == "-1" || division == "" || division == null){
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
	if(inst_id == "-1"){
		$("#instituteError").html("Select Institute");
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
				    	 methodToCall: "validatenotesname",
				    	 notes: allnotesnames,
				    	 notesrowid:allnotesrowid,
				    	 filesize:filesize,
				    	 division:division,
				    	 subject:subject,
				    	 institute:inst_id
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
						   
							var notesCount = 0;
							var formData = new FormData();
							for(i=0;i<=globalnotesrowcounter;i++){
								internalflag=false;
								for(var j=0;j<deletedrows.length;j++){
									if(deletedrows[j]==i){
										internalflag=true;	
									}
								}
								if(internalflag==false){
							var file=$("#myfile"+i).prop('files')[0];
							var notesname = $("#notesname"+i).val();
								formData.append("uploadedFile"+i,file);
								formData.append("notesname"+i,notesname);
								notesCount++;
								}
							}
							var uri = "rest/commonservices/uploadTeacherNotes/"+notesCount+"/"+inst_id+"/"+division+"/"+subject+"/"+batch;
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
	
	$("#batch").change(function(e){
	if($("#batch").val() != "" && $("#batch").val() != null){
		$("#batchError").html("");
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

		var inst_id = $("#instituteSelect").val();
		var handler = {};
		handler.success = function(e){console.log("Success",e);
		$("#subjectSelect").empty();
		var subjectArray = [];
		var tempData = {};
		if(teacherSubjectArray.length > 0){
 		tempData.id = "-1";
 		tempData.text = "Select Subject";
 		subjectArray.push(tempData);
 		$.each(e,function(key,val){
 	 		$.each(teacherSubjectArray,function(innerKey,innerVal){
 	 			if(val.subjectId == innerVal.subjectId){
 				var data = {};
 				data.id = val.subjectId;
 				data.text = val.subjectName;
 				subjectArray.push(data);
 	 			return false;
 	 			}
 	 		});
 			});
 		if(subjectArray.length > 1){
	    $("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
	    $("#subjectSelect").prop("disabled",false);
 		}else{
 	 		subjectArray = [];
 	 		$("#subjectSelect").select2({data:subjectArray,placeholder:"Subjects not available"});
 		    $("#subjectSelect").prop("disabled",false);
 	 	}
		}else{
 	 		subjectArray = [];
 	 		$("#subjectSelect").select2({data:subjectArray,placeholder:"Subjects not available"});
 		    $("#subjectSelect").prop("disabled",false);
 	 	}
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getSubjectOfDivision/"+inst_id+"/"+division,handler);
	}
	
	
	var getBatchFromDivisonNSubject = function(subjectId,divisionId,institute){
	
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
		var selectOptionDropdown = "#subjectSelect";
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
			batchDataArray = [];
			$("#batchError").html("");
			$("#batch").select2({data:batchDataArray,placeholder:"type batch name"});
			$("#batch").select2("val", "");
			$("#batch").prop("disabled",true);
			
		}else{
			$("#subjectSelect").prop("disabled",true);
			batchDataArray = [];
			$("#batchError").html("");
			$("#batch").select2({data:batchDataArray,placeholder:"type batch name"});
			$("#batch").select2("val", "");
			$("#batch").prop("disabled",true);
		}
		
	}
	
	var displayBatchFromSubjectNDivision = function (data){
		var selectOptionDropdown = "#batch";
		var batchMenu = "#batchMenu";
		data = JSON.parse(data);
		data = data.batchlist;
		data = JSON.parse(data);
		$(batchMenu).children().not(".staticMenu").remove();
		$(batchMenu).find(".selectAllCheckbox").prop("checked",false);
		if(data.length !== 0){
		var batchDataArray = [];
		
		var index=0;
		$.each(data,function(index,subjectData){
			/* var batchName = subjectData.batch_name;
			var batchId = subjectData.batch_id;
			var optionMenu = '<li><a href="#"> <input id="checkButton'+index+'" type="checkbox" name="batch" value="'+batchId+'"><label for="checkButton'+index+'">'+batchName+'</label></a></li>'
			$(batchMenu).append(optionMenu);
			index++; */
			var batchDataObject = {};
			batchDataObject.id = subjectData.batch_id;
			batchDataObject.text = subjectData.batch_name;
			batchDataArray.push(batchDataObject);
		});
		$("#batch").empty();
		$("#batch").select2({data:batchDataArray,placeholder:"type batch name",multiple: true});
		$("#batch").prop("disabled",false);
		}else{
			$("#batch").prop("disabled",true);
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
<ul class="nav nav-tabs" style="border-radius:10px">
  <li class="active"><a href="#addnotestab" data-toggle = "tab">Add Notes</a></li>
  <li><a href="seeteachernotes">View Notes</a></li>
</ul>

<div id="addnotestab">
<div class="container" style="padding: 2%;background: #eee">
<div class="row">
			<div class="col-md-3">
				<select name="instituteSelect" id="instituteSelect" class="form-control" width="100px">
					<option value="-1">Select Institute</option>
					<c:forEach items="${requestScope.registerBeanList}" var="institute">
						<option value="<c:out value="${institute.regId}"></c:out>"><c:out value="${institute.className}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="instituteError" class="error"></span>
			</div>
			<div class="col-md-3">
				<select name="division" id="divisionSelect" class="form-control" width="100px">
					<option value="-1">Select Class</option>						
				</select>
				<span id="divisionError" class="error"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="subjectSelect" class="form-control" width="100px">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectError" class="error"></span>
			</div>
			<div class="col-md-3 batchDropDown">
				<select name="batch" id="batch" class="form-control" width="100px">
					<option value="-1">Select Batch</option>
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
		<td><input type="text" name="notesname" class="form-control" id="notesname0" maxlength="50"></td>
		<td><input type="file" name="myFile" accept=".pdf" class="form-control"  size="100px" id="myfile0"></td>
		<td><button class="btn btn-info removenotesrow" id="notesdelete_0" disabled="disabled"><i class="glyphicon glyphicon-trash"></i></button></td>
		<td><span class="error" id="noteserror0" name="noteserror"></span></td>
	</tr>
	</tbody>
	</table>
	
</div>
<div class="container">
<div class="row">
	<div class="col-md-3">
	 <button class="btn btn-info" id="addrow" style="margin-top: 22px"><i class="glyphicon glyphicon-plus"></i>&nbsp;Add Row</button>
      </div>
      <div class="col-md-3">
      <button type="submit" class="btn btn-info" id="submit" style="margin-top: 22px">Submit</button>
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