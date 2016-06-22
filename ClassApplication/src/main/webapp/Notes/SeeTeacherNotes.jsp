<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
 <style type="text/css">
 .error{
     color: red;
    margin-left: 10px;
}      

 </style>
<script type="text/javascript">
var divisionTempData = {};
divisionTempData.id = "-1";
divisionTempData.text = "Select Class";
var subjectTempData = {};
subjectTempData.id = "-1";
subjectTempData.text = "Select Subject";
var teacherSubjectArray = [];
var subject="";
var division="";
var batchDataArray = [];
		/* Formatting function for row details - modify as you need */
		function format ( d ) {
		    // `d` is the original data object for the row
		    console.log("Row Data"+d);
		    var selectedBatches = d.batch.split(",");
		    var htmlString = '<table class="table editTable" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
		        '<tr>'+
		            '<td>Notes name:</td>'+
		            '<td><input type="text" value='+d.name+' id="newnotesname" class="form-control"><span id="notesnameerror" class="error"></span></td>';
		        htmlString = htmlString + '<td><select multiple class="form-control" id="batches"></select><br><span id="batcherror" class="error"></span></td>';
		        htmlString = htmlString + ''+
		            '<td><input type="button" class="btn btn-success save" value="save" id='+d.notesid+'></td>'+
		        '</tr>'+
		    '</table>';
		    return htmlString;
		}

		
function validate()
{
var file=document.getElementByID("myFile");
alert(file);
	return false;
}
var globalnotesid="";
var globaldivision="";
var globalsubject="";
var SUBJECT_DROPDOWN = ".subjectDropDown";	
var BATCH_DROPDOWN = ".batchDropDown";
var ADD_BUTTON = "#classownerUploadexamAddExam";
function fetchnotes(){

	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchnotes",
		    	 division: globaldivision,
		    	 subject:globalsubject
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var notesname=resultJson.notesnames.split(",");
			   var notesids=resultJson.notesids.split(",");
			   var notespaths=resultJson.notespaths.split(",");
			   var table=document.getElementById("notestable");
				  var rowCount=table.rows.length;
				  for (var x=rowCount-1; x>0; x--) {
					  table.deleteRow(x);
				   }
				  var i=0;
				 
				  var notestable=$("#notestable");
				  if(notesids[0]!=""){
					  while(notesids.length>i){
						  notestable.append("<tr><td>"+(i+1)+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' onclick='editnotes("+notesids[i]+")'>Edit</button></td><td><button class='btn btn-info deletenotes' id='"+notesids[i]+"'>Delete</button></td></tr>");
						  i++;
					  }
					  $("#notestable").show();
					  
				  }else{
					  $("#notesnotavailable").modal('toggle');
					  $("#notestable").hide();
				  }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	
}

function editnotes(notesid){
	globalnotesid=notesid;
	var institute=$("#institute").val();
	var division=$("#division").val();
	var subject=$("#subject").val();
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "editnotesinformation",
		    	 notesid: notesid,
		    	 institute:institute,
		    	 division:division,
		    	 subject:subject
		    	 
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var notesname=resultJson.notesname;
			   var notesbatch=resultJson.notesbatches.split(",");
			   var allbatchnames=resultJson.allbatchnames.split(",");
			   var allbatchids=resultJson.allbatchids.split(",");
			   $("#newnotesname").val(notesname);
			   $("#batchesdiv").empty();
			   var i=0;
			   while(i<allbatchids.length){
				   var flag=false;
				   var j=0;
				   while(j<notesbatch.length){
					   if(allbatchids[i]==notesbatch[j].trim()){
						   flag=true; 
					   }
					   j++;
				   }
				   if(flag==true){
					   $("#batchesdiv").append("<input type='checkbox' name='batches' id='batches' value='"+allbatchids[i]+"' checked>"+allbatchnames[i]);
				   }else{
				   $("#batchesdiv").append("<input type='checkbox' name='batches' id='batches' value='"+allbatchids[i]+"'>"+allbatchnames[i]);
				   }
				   $("#batchesdiv").append("<br>");
				   i++;
			   }
			   $("#editnotes").modal('toggle');
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
}

function deletenotes(notesid){
	var subject=$("#subject").val();
	var division=$("#division").val();
	var notes = {};
	notes.notesid = notesid;
	notes.divid = division;
	notes.subid = subject;
	var handlers = {};
	handlers.success = function(data){
		
	};
	handlers.error = function(){};
	rest.post("rest/classownerservice/deleteNotes",handlers,JSON.stringify(notes));
}


$(document).ready(function(){
	$("#instituteSelect").change(function(){
		$("#notesdiv").hide();
		var divisionArray = [];
		var subjectArray = [];
		divisionArray.push(divisionTempData);
		subjectArray.push(subjectTempData);
		$("#division").empty();
		$("#subject").empty();
		 $("#division").select2({data:divisionArray,placeholder:"Type Class Name"});
		$("#subject").select2({data:subjectArray,placeholder:"Type Subject Name"});
		 
		var inst_id = $(this).val();
		if(inst_id != "-1"){
		$("#instituteError").html("");
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		if(e.divisionList.length > 0){
		var divisionArray = [];
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 		teacherSubjectArray = e.subjectList;
	    $("#division").select2({data:divisionArray,placeholder:"Type Topic Name"});
		}else{
			$("#division").empty();
 	 		$("#division").select2({data:"",placeholder:"Class not available"});	
		}
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}
	});
	
	     
	    // Add event listener for opening and closing details
	    $('#notestable').on('click', '.edit', function () {
	        var tr = $(this).closest('tr');
	        var table = $('#notestable').DataTable();
	        var row = table.row( tr );
	 
	        if ( row.child.isShown() ) {
	            // This row is already open - close it
	            row.child.hide();
	            tr.removeClass('shown');
	        }
	        else {
	            // Open this row
	            row.child( format(row.data()) ).show();
	            tr.addClass('shown');
	            var selectedbatch = row.data().batch.split(",");
	            $(tr.next("tr")).find("select").select2({data:batchDataArray,placeholder:"Select Batches"}).val(selectedbatch).change();
	        }
	    } );
	$("#division").on("change",function(e){
		$("#notesdiv").hide();
		var subjectArray = [];
		subjectArray.push(subjectTempData);
		$("#subject").empty();
		$("#subject").select2({data:subjectArray,placeholder:"Type Subject Name"});
		if($(this).val()!=-1){
			var uploadExam = new UploadExam();
			uploadExam.getSubjectsInDivision($(this).val());
			$("#divisionError").html("");
		}
	});
	
	$("#subject").on("change",function(e){
		$("#notesdiv").hide();
		var uploadExam = new UploadExam();
		var subjectId = $(this).val();
		var divisionId = $("#division").val();
		var inst_id = $("#instituteSelect").val();
		if(subjectId!="-1"){
			$("#subjectError").html("");
			uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId,inst_id);	
		}
	});
	
	$("#classownerUploadexamSubjectName").on("change",function(e){
		var uploadExam = new UploadExam();
		$(ADD_BUTTON).hide();
		if($(this).val()!=-1){
			$(ADD_BUTTON).show();
		}
	});
	
	$("#notesdeleteconfirm").click(function(){
		$("#actionform").submit();
	});
	
	$("#notestable").on("click",".shownotes",function(){
		/* var division=$("#division").val();
		var subject=$("#subject").val(); */
		var inst_id = $("#instituteSelect").val();
		$("form#actionform #division").val(division);
		$("form#actionform #subject").val(subject);
		$("form#actionform #notesid").val($(this).prop("id"));
		$("form#actionform #institute").val(inst_id);
		$("#actionform").prop("action","shownotes");
		var formData = $("#actionform").serialize();
		console.log(formData);
		window.open("shownotes?"+formData,"","width=500, height=500"); 
	});
	
	$("#submit").click(function(){
		$("#subjecterror").html("");
		$("#divisionerror").html("");
		var inst_id = $("#instituteSelect").val();
		 subject=$("#subject").val();
		 division=$("#division").val();
		var flag=true;
		if(inst_id == "-1"){
			$("#instituteError").html("Select Institute");
			flag= false;
		}
		if(subject=="-1" || subject == "" || subject == null){
			$("#subjectError").html("Select Subject");
			flag=false;
		}
		if(division=="-1" || division == "" || division == null){
			$("#divisionError").html("Select Class");
			flag=false;
		}
		if(flag==true){
			globaldivision=division;
			globalsubject=subject;
			
			var handlers = {};
			handlers.success = function(data){
				var dataTable = $('#notestable').DataTable({
					autoWidth:false,
					bDestroy:true,
					data: data,
					lengthChange: false,
					columns: [
						{title:"#",data:null},
						{ title: "Name",data:null,render:function(data,event,row){
							return "<div class='defaultName'>"+row.name+"</div>";
						},sWidth:"70%"},
						{ title: "Action",data:null,render:function(data,event,row){
							return "<input type='hidden' class='notesname' value='"+row.name+"'>"+
									"<input type='hidden' class='batch' value='"+row.batch+"'>"+
									"<button class='btn btn-primary btn-xs shownotes' id='"+row.notesid+"'>Open</button>&nbsp;"+
									"<button class='btn btn-info btn-xs edit' id='"+row.notesid+"'>Edit</button>&nbsp;"+
									"<button class='btn btn-danger btn-xs deletenotes' id="+row.notesid+">Delete</button>";
							},sWidth:"20%"}
					]
					
				});
				
				 dataTable.on( 'order.dt search.dt', function () {
		        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
		            cell.innerHTML = i+1;
					});
				}).draw();
				 $("#notesdiv").show();
			}
			handlers.error = function(){
				
			}
			rest.post("rest/teacher/getNotes/"+inst_id+"/"+division+"/"+subject,handlers);
		}
	});
	
	$("#notestable").on("click",".save",function(){
		var notesid = $(this).prop("id");
		var inst_id = $("#instituteSelect").val();
		/* var subject=$("#subject").val();
		var division=$("#division").val(); */
		var notesname=$(this.closest("table")).find("#newnotesname").val();
		var notesnameerror=$(this.closest("table")).find("#notesnameerror");
		var batcherror=$(this.closest("table")).find("#batcherror");
		var batchidmap;
		var batchids=$(this.closest("table")).find("#batches").val();
		var regex = /^[a-zA-Z0-9 ]*$/;
		var flag=true;
		var that = this;
		notesnameerror.html("");
		batcherror.html("");
		if(notesname==""){
			notesnameerror.html("Please enter notes name");
			flag=false;
		}else if(!notesname.match(regex)){
			notesnameerror.html("Please enter valid notes name (No special characher allowed) ");
			flag=false;
		}
		
		if(batchids == null){
			batcherror.html("Please select batch");
			flag=false;
		}
		
		if(flag==true){
			var notes = {};
			notes.notesid = notesid;
			notes.name = notesname;
			notes.divid = division;
			notes.subid = subject;
			notes.inst_id = inst_id;
			notes.batch = batchids.join(",");
			var handlers = {};
			handlers.success = function(data){
				if(data == true){
					notesnameerror.html("Notes name already exists,Please enter different names");
				}else{
					$.notify({message: 'Notes updated successfully'},{type: 'success'});
					$($($(that.closest("table")).closest("tr")).prev("tr")).find(".defaultName").html(notesname);
				}
			};
			handlers.error = function(){};
			rest.post("rest/teacher/updateNotes",handlers,JSON.stringify(notes));
			
			   
		}
	});
	
	$("#notestable").on("click",".deletenotes",function(){
		/* var subject=$("#subject").val();
		var division=$("#division").val(); */
		var inst_id = $("#instituteSelect").val();
		var notes = {};
		notes.notesid = $(this).prop("id");
		notes.divid = division;
		notes.subid = subject;
		notes.inst_id = inst_id;
		var that = this;
		var handlers = {};
		handlers.success = function(data){
			var table = $("#notestable").DataTable();
			var editTable = $($(that.closest("tr")).next("tr")).find(".editTable")
			if(editTable.length > 0){
				$($(that.closest("tr")).next("tr")).addClass('removeRow');
			}
			$(that.closest("tr")).addClass('removeRow');
			table.row('.removeRow').remove().draw( false );
			$.notify({message: 'Notes deleted successfully'},{type: 'success'});
		};
		handlers.error = function(){
			console.log("error");
		};
		rest.post("rest/teacher/deleteNotes",handlers,JSON.stringify(notes));
	});
	
	
});

function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	
		var inst_id = $("#instituteSelect").val();
		var handler = {};
		handler.success = function(e){console.log("Success",e);
		 $("#subject").empty();
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
	    $("#subject").select2({data:subjectArray,placeholder:"Type Topic Name"});
	    $("#subject").prop("disabled",false);
 		}else{
 	 		subjectArray = [];
 	 		$("#subject").select2({data:subjectArray,placeholder:"Subjects not available"});
 		    $("#subject").prop("disabled",false);
 	 	}
		}else{
 	 		subjectArray = [];
 	 		$("#subject").select2({data:subjectArray,placeholder:"Subjects not available"});
 		    $("#subject").prop("disabled",false);
 	 	}
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getSubjectOfDivision/"+inst_id+"/"+division,handler);
	}
	
	
	var getBatchFromDivisonNSubject = function(subjectId,divisionId,inst_id){
	
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getBatchesByDivisionNSubject",
		    	 divisionId: divisionId,
				 subjectId:subjectId,
				 institute:inst_id
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
		var selectOptionDropdown = "#subject";
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
			$("#subject").prop("disabled",true);
			$("#classownerUploadexamBatchName").prop("disabled",true);
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
		 batchDataArray = [];
		
		var index=0;
		$.each(data,function(index,subjectData){
			/* var batchName = subjectData.batch_name;
			var batchId = subjectData.batch_id;
			var optionMenu = '<li><a href="#"> <input id="checkButton'+index+'" type="checkbox" name="batch" value="'+batchId+'"><label for="checkButton'+index+'">'+batchName+'</label></a></li>'
			$(classownerUploadexamBatchNameMenu).append(optionMenu);
			index++; */
			var batchDataObject = {};
			batchDataObject.id = subjectData.batch_id;
			batchDataObject.text = subjectData.batch_name;
			batchDataArray.push(batchDataObject);
		});
		$("#classownerUploadexamSelectBatchName").select2({data:batchDataArray,placeholder:"type batch name"});
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
<ul class="nav nav-tabs" style="border-radius:10px">
  <li><a href="addteachernotesoption">Add Notes</a></li>
  <li class="active"><a data-toggle = "tab">View Notes</a></li>
</ul>
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
				<select name="division" id="division" class="form-control" width="100px">
					<option value="-1">Select Class</option>							
				</select>
				<span id="divisionError" class="error"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="subject" class="form-control" width="100px">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjectError" class="error"></span>
			</div>
			<!-- <div class="col-md-3 batchDropDown">
				<select name="batch" id="classownerUploadexamSelectBatchName" class="form-control" width="100px" disabled="disabled">
					<option value="-1">Select Batch</option>
				</select>
			</div> -->
			<div class="col-md-3 ">
			<button class="btn btn-primary" id="submit">Submit</button>
			</div>
		</div>
		</div>

   <div id="notesdiv" class="container" style="width: 100%">
   <table id="notestable" class="table table-bordered table-hover" style="background-color: white;width: 100%">
  
   </table>
 
   </div>
   
   <div class="modal fade" id="notesnotavailable" tabindex="-1" role="dialog" 
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
           Notes not available for this subject
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="notesupdated" tabindex="-1" role="dialog" 
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
           Notes updated successfully..
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="deletenotesalert" tabindex="-1" role="dialog" 
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
           Notes deleted successfully..
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="editnotes" tabindex="-1" role="dialog" 
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
         <div class="modal-body" align="left">
		Notes Name :- <input type="text" id="newnotesname" class="form-control" maxlength="50"> <br>
		<span id="notesnameerror" class="error"></span><br>
		This Notes is applicatble for following batches:- <br>
		<div id="batchesdiv"> 
		
		</div>         
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="savenotes">Save</button>
         </div>
         </div>
   </div>
    
</div>

<div class="modal fade" id="notesdeleteconfirmmodal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="myModalLabel">Delete Notes</h4>
        </div>
        <div class="modal-body" id="mymodalmessage">
          Are you sure?
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancle</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="notesdeleteconfirm">Yes</button>
      	</div>
    </div>
</div>
</div>

<form action="" id="actionform">
   <input type="hidden" name="division" id="division" value='<c:out value="${division}"></c:out>'>
   <input type="hidden" name="batch" id="batch" value='<c:out value="${batch}"></c:out>'>
   <input type="hidden" name="subject" id="subject" value='<c:out value="${subject}"></c:out>'>
   <input type="hidden" name="notesid" id="notesid" >
   <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
   </form>
</body>
</html>