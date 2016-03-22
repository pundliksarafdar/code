<%@page import="com.classapp.db.register.RegisterBean"%>
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
var globalflag=false;

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
				   $("#subjecterror").html("");
				   var subjectselect=$('#subject');
				   subjectselect.empty();
				   subjectselect.append("<option value='-1'>Select Subject</option>");
				   if(subjectids[0]!=""){
					   var i=0;
					   while(i<subjectids.length){
						   subjectselect.append("<option value="+subjectids[i]+">"+subjectnames[i]+"</option>");
						   i++;
					   }
					   
				   }else{
					   $("#subjecterror").html("No subjects assigned to you");
				   }
				   
				   var divisionselect=$('#division');
				   $("#divisionerror").html("");
				   divisionselect.empty();
				   divisionselect.append("<option value='-1'>Select Class</option>");
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
	
	
	$('#division').change(function(){
		var division = $('#division').val();
		if(division!="-1"){
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
						  // batchselect.append("<option value="+batchids[i]+">"+batchnames[i]+"</option>");
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
		}else{
			$("#batcherror").html("");
			   var batchselect=$('#batch');
			   batchselect.empty();
		}
	});
	
	$("#submit").click(function(event){
		
		var file=document.getElementById("myfile").value;
		var notesname=$("#notesname").val();
		var division=$("#division").val();
		var subject=$("#subject").val();
		var validforbatch=$("#validforbatch:checked").val();
	//	var batch=$("#batch").val();
		var classes = $('#classes').val();
		var notesnameerror=$("#notesnameerror");
		var divisionerror=$("#divisionerror");
		var subjecterror=$("#subjecterror");
		var batcherror=$("#batcherror");
		var fileerror=$("#fileerror");
		var regex = /^[a-zA-Z0-9 ]*$/;
		batcherror.html("");
		subjecterror.html("");
		divisionerror.html("");
		notesnameerror.html("");
		fileerror.html("");
		var batchidmap;
		var batch="";
		var flag=true;
		batchidmap=$("input[name='batch']:checked").map(function() {
			return this.value;
		});
		
		var j=0;
		while(j<batchidmap.size())
			{
			if(j==0)
				{
				batch=batch+batchidmap[0]+"";
				}else{
					batch=batch+","+batchidmap[j];
				}
			j++;
			}
		
	
		if(file==""){
			fileerror.html("Please select file");
			flag=false;
		}else{
			var filesize=document.getElementById("myfile").files[0].size/1024/1024;
			if(filesize > 5){
				fileerror.html("File size should be less than 5 MB");
				flag=false;
			}
		}
		if(notesname==""){
			notesnameerror.html("Please enter notes name");
			flag=false;
		}else if(!notesname.match(regex)){
			notesnameerror.html("Please enter valid notes name (No special characher allowed) ");
			flag=false;
		}else{
			var  notesflag=false;
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "validatenotesname",
				    	 notes: notesname,
				    	 classes:classes
				   		},
				   		async: false,
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   var notesstatuts=resultJson.notesname;
					   if(notesstatuts=="available"){
						   notesflag=true; 
					   }else{
						   notesflag=false;
					   }
					  
				   },
					error:function(){
				   		modal.launchAlert("Error","Error");
				   	}
				   });
			if(notesflag==true){
				notesnameerror.html("Please enter different notes name");
				flag=false;
			}
		}
		if(division=="-1"){
			divisionerror.html("Please select class");
			flag=false;
		}
		if(subject=="-1"){
			subjecterror.html("Please select subject");
			flag=false;
		}
		if(validforbatch=="specific"){
			if(batch==null || batch==""){
				batcherror.html("Please select atleast one batch");
				flag=false;
			}
		}
		var allbatch=$("#allbatches").val();
		if(allbatch==""){
			 $("#batcherror").html("No Batch Present");
			 flag=false;
		}
		
		if(flag==false){
			event.preventDefault();
		}
			
		
	});
	
	
/* $(":file").filestyle({buttonBefore: true});
 */
/* $('.selectpicker').selectpicker();
$('#form').bootstrapValidator({
    feedbackIcons: {
        valid: 'glyphicon glyphicon-ok',
        invalid: 'glyphicon glyphicon-remove',
        validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
    	notesname: {
            validators: {
                notEmpty: {
                    message: 'The username is required'
                }
            }
        },
        myFile: {
            validators: {
                notEmpty: {
                    message: 'The password is required'
                }
            }
        }
    }
}); */

});

function showalert(){
	
	$("#notesaddedalert").modal('toggle');
}
</script>
</head>
<body onload="showalert()">
<div align="center">
<%
String notes=(String)request.getAttribute("notes");
if(notes!=null){
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
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;">Add notes</div>
		
<form action="upload" method="post" enctype="multipart/form-data" role="form" id="form">
	<input type="hidden" id="batch" name="batch" value='<c:out value="${batch}" ></c:out>'>
	<input type="hidden" id="division" name="division" value='<c:out value="${division}" ></c:out>'>
	<input type="hidden" id="subject" name="subject" value='<c:out value="${subject}" ></c:out>'>
	<input type="hidden" id="institute" name="institute" value='<c:out value="${institute}" ></c:out>'>
	<div class="row">
		<div class="col-md-3">
		<label for="notesname"  class="control-label" align="right">Notes Name :</label>
		<input type="text" name="notesname" class="form-control" id="notesname" maxlength="50">
		<span class="error" id="notesnameerror" name="notesnameerror"></span>
		</div>
		<div class="col-md-3">
		<label for="myFile"  class="control-label" align="right">Upload your file :</label>
		<input type="file" name="myFile" accept=".pdf" class="form-control"  size="100px" id="myfile">
		<span id="fileerror" class="error"></span>
		</div>
		<div class="col-md-3">
      <button type="submit" class="btn btn-info" id="submit" style="margin-top: 22px">Submit</button>
      </div>
		</div>
     </form>
   </div>
</body>
</html>