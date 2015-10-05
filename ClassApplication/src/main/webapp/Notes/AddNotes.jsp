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
function validate()
{
var file=document.getElementByID("myFile");

	return false;
}

$(document).ready(function(){
	$('input[type=radio][name=validforbatch]').change(function() {
        if (this.value == 'all') {
        	$( "#batch" ).prop( "disabled", true );
        	$("#batchdiv").fadeOut();
        }
        else if (this.value == 'specific') {
        	$( "#batch" ).prop( "disabled", false );
        	$("#batchdiv").fadeIn();
        }
    });
	
	
	$('#division').change(function(){
		var division = $('#division').val();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getbatches",
			    	 division: division
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var batchids=resultJson.batchids.split(",");
				   var batchnames=resultJson.batchnames.split(",");
				   $("#batcherror").html("");
				   var batchselect=$('#batch');
				   var fieldset=$('#fieldset');
				   fieldset.empty();
				   batchselect.empty();
				   $("#allbatches").val(batchids);
				   if(batchids[0]!=""){
					   var i=0;
					   while(i<batchids.length){
						//   batchselect.append("<option value="+batchids[i]+">"+batchnames[i]+"</option>");
						   fieldset.append("<input type='checkbox' name='batch' id='batch' value='"+batchids[i]+"' >"+batchnames[i]+"<br>");
						   i++;
					   }
					   
				   }else{
					   $("#batcherror").html("No Batch Present");
				   }
			   },
				error:function(){
			   		modal.launchAlert("Error","Error");
			   	}
			   });
	});
	
	$("#submit").click(function(event){
	//	event.preventDefault();
		var flag=true;
	var allnotesnames="";
	var allnotesrowid="";
	var i=0;
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
		var division=$("#division").val();
		var subject=$("#subject").val();
		var validforbatch=$("#validforbatch:checked").val();
		//var batch=$("#batch").val();
		var noteserror=$("#noteserror"+i);
		var regex = /^[a-zA-Z0-9 ]*$/;
		noteserror.html("");
		var batchidmap;
		var batch="";
		
		if(file==""){
			noteserror.html("Please select file");
			flag=false;
		}else{
			var filesize=document.getElementById("myfile"+i).files[0].size/1024/1024;
			if(filesize > 5){
				noteserror.html("File size should be less than 5 MB");
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
				    	 notesrowid:allnotesrowid
				   		},
				   		async: false,
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   var notesstatuts=resultJson.notesname;
					   var overlappedIds=resultJson.overlappedIds.split("/");
					   var notesnamestatus=resultJson.notesnamestatus.split(",");
					   if(overlappedIds[0]==""){
						   if(notesnamestatus[0]!=""){
					  for(var s=0;s<notesnamestatus.length;s++){
						  $("#noteserror"+notesnamestatus[s]).html("Notes name already exists,Please enter different names");
					  }
					  flag=false;
						   }
					   }else{
						   for(var k=0;k<overlappedIds.length;k++){
							   $("#noteserror"+overlappedIds[k].split(",")[0]).html("Please enter unique names");
							   $("#noteserror"+overlappedIds[k].split(",")[1]).html("Please enter unique names");
						   }
						   flag=false;
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
			event.preventDefault();
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
</script>
</head>
<body onload="showalert()">
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
<div class="container" style="margin-bottom: 5px">
	<a type="button" class="btn btn-primary" href="choosesubject?forwardAction=addnotesoption" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
</div>
<form action="upload" method="post" enctype="multipart/form-data" role="form" id="form">
<input type="hidden" id="batch" name="batch" value="<c:out value="${batch}" ></c:out>">
<input type="hidden" id="division" name="division" value="<c:out value="${division}" ></c:out>">
<input type="hidden" id="subject" name="subject" value="<c:out value="${subject}" ></c:out>">
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
		<div align="center" style="font-size: larger;"><u>Add Notes</u></div>
	<div class="row">
		<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
			 
		</div>
	</div>
</div>		
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
</body>
</html>