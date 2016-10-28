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

#notestable .editEnabled .editable{
	display:block;
}

#notestable .editEnabled .default{
	display:none;
}

#notestable .editable{
	display:none;
}

#notestable .default{
	display:show;
}


 </style>
<script type="text/javascript">
var subject="";
var division="";
var batchDataArray = [];
var child_mod_access = [];
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
		            '<td><input type="button" class="btn btn-success btn-sm save" value="save" id='+d.notesid+'></td>'+
		            '<td><input type="button" class="btn btn-danger btn-sm cancel" value="cancel" id='+d.notesid+'></td>'+
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

/* function deletenotes(notesid){
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
} */


$(document).ready(function(){
	
	child_mod_access  = $("#accessRights").val().split(",");
	    // Add event listener for opening and closing details
	    $('#notestable').on('click', '.edit', function () {
	        var tr = $(this).closest('tr');
	        var table = $('#notestable').DataTable();
	        tr.find(".error").html("");
	        tr.find(".editBatches").select2({data:batchDataArray,placeholder:"Select Batch"});
	        if(tr.find(".preBatches").val() != ""){
	        	 tr.find(".editBatches").select2().val(tr.find(".preBatches").val().split(",")).change();
	        }
	        tr.find(".editNotesName").val(tr.find(".defaultNotesName").html());
	       tr.addClass("editEnabled");
	       /*  var row = table.row( tr );
	 
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
	        }*/
	    } );
	$("#division").on("change",function(e){
		/* $("#notestable").empty(); */
		$("#notesdiv").hide();
		if($(this).val()!=-1){
			$("#divisionerror").html("");
			var uploadExam = new UploadExam();
			uploadExam.getSubjectsInDivision($(this).val());
		}else{
			$("#subject").select2().val("-1").change();
			$("#subject").find('option:gt(0)').remove();
			
		}
	});
	
	$("#subject").on("change",function(e){
		$("#notesdiv").hide();
		var uploadExam = new UploadExam();
		var subjectId = $(this).val();
		var divisionId = $("#division").val();
		if(subjectId!="-1"){
			$("#subjecterror").html("");
			uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId);	
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
		$("form#actionform #division").val(division);
		$("form#actionform #subject").val(subject);
		$("form#actionform #notesid").val($(this).prop("id"));
		$("#actionform").prop("action","shownotes");
		var formData = $("#actionform").serialize();
		console.log(formData);
		window.open("customeUserShowNotes?"+formData,"","width=500, height=500"); 
	});
	
	$("#submit").click(function(){
		$("#notesdiv").hide();
		$("#subjecterror").html("");
		$("#divisionerror").html("");
		 subject=$("#subject").val();
		 division=$("#division").val();
		var flag=true;
		if(subject=="-1"){
			$("#subjecterror").html("Please select Subject");
			flag=false;
		}
		if(division=="-1"){
			$("#divisionerror").html("Please select class");
			flag=false;
		}
		if(flag==true){
			globaldivision=division;
			globalsubject=subject;
			
			var handlers = {};
			handlers.success = function(data){
				var dataTable = $('#notestable').DataTable({
					autoWidth: false,
					bDestroy:true,
					data: data,
					columns: [
						{title:"#",data:null,width:"10%"},
						{ title: "Name",data:null,render:function(data,event,row){
							var defaultName = "<div class='defaultNotesName default'>"+row.name+"</div>";
							var editName = "<div class=' editable'><input type='text' value='"+row.name+"' class='form-control editNotesName' ><span class='notesnameerror error'></span></div>";
							return defaultName + editName ;
						},width:"30%"},
						{ title: "Batches",data:null,render:function(data,event,row){
							var batchString = "";
							var notesBatchArray = row.batch.split(",");
							for(var i =0;i<notesBatchArray.length;i++){
								for(var j=0;j<batchDataArray.length;j++){
									if(notesBatchArray[i] == batchDataArray[j].id){
										batchString = batchString + batchDataArray[j].text+",";
									}
								}
							}
							batchString = batchString.slice(0,batchString.length - 1);
							var defaultBatches= "<div class='defaultBatches default'>"+batchString+"</div>"
							var editBatches = "<div class='editable'><select class='form-control editBatches' style='width:100%' multiple></select><span class='batcherror error'></span></div>"
							var preBatches = "<input type='hidden' value='"+row.batch+"' class='preBatches'>";
							return defaultBatches+editBatches+preBatches;
						},width:"30%"},
						{ title: "Action",data:null,render:function(data,event,row){
							var defaultButtons = "<div class='default'><input type='hidden' class='notesname' value='"+row.name+"'>"+
							"<input type='hidden' class='batch' value='"+row.batch+"'>"+
							"<button class='btn btn-info btn-xs shownotes' id='"+row.notesid+"'>Open</button>&nbsp;";
							if($.inArray( "17", child_mod_access) != "-1"){
								defaultButtons = defaultButtons + "<button class='btn btn-primary btn-xs edit' id='"+row.notesid+"'>Edit</button>&nbsp;";
							}if($.inArray( "18", child_mod_access) != "-1"){
								defaultButtons = defaultButtons + "<button class='btn btn-danger btn-xs deletenotes' id="+row.notesid+">Delete</button>";
							}
							defaultButtons = defaultButtons + "</div>";
							var editButtons = "<div class='editable'><button class='btn btn-success btn-xs save' id='"+row.notesid+"'>Save</button>&nbsp;"+
							"<button class='btn btn-danger btn-xs cancel' id="+row.notesid+">Cancel</button></div>";
							return defaultButtons+ editButtons;
							},width:"20%"}
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
			rest.get("rest/customuserservice/getNotes/"+division+"/"+subject,handlers);
		}
	});
	
	$("#notestable").on("click",".cancel",function(){
		/* $($($($(this).closest("table")).closest("tr")).prev("tr")).find(".edit").click(); */
		$($(this).closest("tr")).removeClass("editEnabled");
	});
	
	$("#notestable").on("click",".save",function(){
		var notesid = $(this).prop("id");
		/* var subject=$("#subject").val();
		var division=$("#division").val(); */
		var notesname=$(this.closest("tr")).find(".editNotesName").val();
		var notesnameerror=$(this.closest("tr")).find(".notesnameerror");
		var batcherror=$(this.closest("tr")).find(".batcherror");
		var batchidmap;
		var batchids=$(this.closest("tr")).find(".editBatches").val();
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
			notes.batch = batchids.join(",");
			var handlers = {};
			handlers.success = function(data){
				if(data == true){
					notesnameerror.html("Notes name already exists,Please enter different names");
				}else{
					$.notify({message: 'Notes updated successfully'},{type: 'success'});
					$(that.closest("tr")).find(".defaultNotesName").html(notesname);
					var batchString = "";
					for(var i =0;i<batchids.length;i++){
						for(var j=0;j<batchDataArray.length;j++){
							if(batchids[i] == batchDataArray[j].id){
								batchString = batchString + batchDataArray[j].text+",";
							}
						}
					}
					batchString = batchString.slice(0,batchString.length - 1);
					$(that.closest("tr")).find(".defaultBatches").html(batchString);
					$(that.closest("tr")).find(".preBatches").val(batchids.join(","));
					$(that.closest("tr")).find(".cancel").click();
				}
			};
			handlers.error = function(){};
			rest.put("rest/customuserservice/updateNotes",handlers,JSON.stringify(notes));
			
			   
		} 
	});
	
	$("#notestable").on("click",".deletenotes",function(){
		var notes = {};
		notes.notesid = $(this).prop("id");
		notes.divid = division;
		notes.subid = subject;
		notes.name = $($(this).closest("tr")).find(".defaultNotesName").html();
		var that = this;
		deleteNotesConfirm(notes,that);
	});
	
	
});

function deleteNotesConfirm(notes,that){
	modal.modalConfirm("Delete","Do you want to delete "+notes.name+"?","Cancel","Delete",deleteNotes,[notes,that]);
}

function deleteNotes(notes,that){
	var handlers = {};
	handlers.success = function(data){
		var table = $("#notestable").DataTable();
		var editTable = $($(that.closest("tr")).next("tr")).find(".editTable")
		if(editTable.length > 0){
			$($(that.closest("tr")).next("tr")).addClass('removeRow');
		}
		$(that.closest("tr")).addClass('removeRow');
		table.row('.removeRow').remove().draw( false );
		$.notify({message: "Notes successfuly deleted"},{type: 'success'});
	};
	handlers.error = function(){
		console.log("error");
	};
	rest.post("rest/customuserservice/deleteNotes",handlers,JSON.stringify(notes));
}

function UploadExam(){
	
	var getSubjectsInDivision = function(division){
	
		var handlers = {};
		handlers.success=function(data){
			 $("#subject").removeAttr('disabled');
			   displaySubjectDropDown(data);
		};   
		handlers.error=function(){
			$.notify({message: "Error"},{type: 'danger'});
		};   
		
		rest.get("rest/customuserservice/getSubjectOfDivision/"+division,handlers);
	}
	
	
	var getBatchFromDivisonNSubject = function(subjectId,divisionId){
	
		var handlers = {};
		handlers.success=function(data){
			displayBatchFromSubjectNDivision(data);
		};   
		handlers.error=function(){
			$.notify({message: "Error"},{type: 'danger'});
		};   
		
		rest.get("rest/customuserservice/getBatchesByDivisionNSubject/"+divisionId+"/"+subjectId,handlers);
	}
	
	var displaySubjectDropDown = function (data){
		var selectOptionDropdown = "#subject";
		$(selectOptionDropdown).select2().val("-1").change();
		$(selectOptionDropdown).find("option:not(:first)").remove();
		if(data.length > 0){
			for(subjectNameIndex in data){
				$("<option/>",{
					value:data[subjectNameIndex].subjectId,
					text:data[subjectNameIndex].subjectName
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
		/* data = JSON.parse(data);
		data = data.batchlist;
		data = JSON.parse(data); */
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
<jsp:include page="notesHeader.jsp" >
		<jsp:param value="active" name="customeUserSeeNotes"/>
	</jsp:include>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<div class="well">
<div class="row">
			<div class="col-md-3">
				<select name="division" id="division" class="form-control" width="100px">
					<option value="-1">Select Class</option>
					<c:forEach items="${requestScope.divisions}" var="division">
						<option value="<c:out value="${division.divId}"></c:out>"><c:out value="${division.divisionName}"></c:out>&nbsp;<c:out value="${division.stream}"></c:out></option>
					</c:forEach>							
				</select>
				<span id="divisionerror" class="validation-message"></span>
			</div>
			<div class="col-md-3 subjectDropDown">
				<select name="subject" id="subject" class="form-control" width="100px">
					<option value="-1">Select Subject</option>
				</select>
				<span id="subjecterror" class="validation-message"></span>
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
<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
   <div id="notesdiv" class="" style="width: 100%">
   <table id="notestable" class="table table-striped" style="width: 100%">
  
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
   </form>
</body>
</html>