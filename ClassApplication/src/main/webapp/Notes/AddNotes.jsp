<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s" %>
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

 </style>
<script type="text/javascript">
var allbatches="";

function validate()
{
var file=document.getElementByID("myFile");

	return false;
}

$(document).ready(function(){
	$('input[type=radio][name=validforbatch]').change(function() {
        if (this.value == 'all') {
        	$( "#batch" ).prop( "disabled", true );
        }
        else if (this.value == 'specific') {
        	$( "#batch" ).prop( "disabled", false );
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
				   batchselect.empty();
				   $("#allbatches").val(batchids);
				   if(batchids[0]!=""){
					   var i=0;
					   while(i<batchids.length){
						   batchselect.append("<option value="+batchids[i]+">"+batchnames[i]+"</option>");
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
	
	$("#submit").click(function(){
		
		var file=document.getElementById("myfile").value;
		var notesname=$("#notesname").val();
		var division=$("#division").val();
		var subject=$("#subject").val();
		var validforbatch=$("#validforbatch:checked").val();
		var batch=$("#batch").val();
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
		var flag=true;
		if(file==""){
			fileerror.html("Please select file");
			flag=false;
		}else{
			var filesize=document.getElementById("myfile").files[0].size/1024/1024;
			if(filesize > 2){
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
				    	 notes: notesname
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
			divisionerror.html("Please select division");
			flag=false;
		}
		if(subject=="-1"){
			subjecterror.html("Please select subject");
			flag=false;
		}
		var allbatch=$("#allbatches").val();
		if(allbatch==""){
			 $("#batcherror").html("No Batch Present");
			 flag=false;
		}
		
		if(validforbatch=="specific"){
			if(batch==null){
				batcherror.html("Please select atleast one batch");
				flag=false;
			}
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
               aria-hidden="true">�
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

<form action="upload" method="post" enctype="multipart/form-data" role="form" id="form">
	<div class="form-group">
	<label for="notesname"  class="col-sm-4 control-label">Notes Name :</label>
	<div class="col-sm-5" align="left">
	<input type="text" name="notesname" class="form-control" id="notesname" maxlength="50">
	</div>
	<div class="col-sm-2" align="left">
	<span class="error" id="notesnameerror" name="notesnameerror"></span>
	</div>
	</div>
	
	<div class="form-group">
	<label for="myFile"  class="col-sm-4 control-label">Upload your file</label>
	<div class="col-sm-5" align="left">
      <input type="file" name="myFile" accept=".pdf" class="form-control"  size="100px" id="myfile">
      </div>
      <div class="col-sm-2" align="left">
      <span id="fileerror" class="error"></span>
      </div>
      </div>
      
      <div class="form-group">
	<label for="subject"  class="col-sm-4 control-label">Select Subject:</label> 
    <div class="col-sm-5" align="left">
      <select class="form-control" name="subject" id="subject">
      <option value="-1">Select one</option>
      <%List<Subjects> list=(List<Subjects>)request.getAttribute("subjects"); 
      if(list!=null){
      for(int i=0;i<list.size();i++)
      {
      %>
      <option value="<%=list.get(i).getSubjectId() %>"><%=list.get(i).getSubjectName()%></option>
      <%} }%>
      </select>
      </div>
      <div class="col-sm-2" align="left">
	<span class="error" id="subjecterror" name="subjecterror"></span>
	</div>
      </div>
      
      <div class="form-group">
	<label for="division"  class="col-sm-4 control-label">Select Division :</label>
      <div class="col-sm-5" align="left">
	<select name="division" class="form-control" id="division">
      <option value="-1">Select one</option>
      <%List<Division> divisions=(List<Division>)request.getAttribute("divisions");
      if(divisions!=null){
      for(int i=0;i<divisions.size();i++)
      {
      %>
      <option value="<%=divisions.get(i).getDivId() %>"><%=divisions.get(i).getDivisionName()%>  <%=divisions.get(i).getStream() %></option>
      <%} }%>
      </select>
  	</div>
  	<div class="col-sm-2" align="left">
	<span class="error" id="divisionerror" name="divisionerror"></span>
	</div>
  	</div>
  	<br>
  	<div class="form-group">
  	<div class="col-sm-5" align="right">
  	 <input type="radio" name="validforbatch" value="all" checked="checked" id="validforbatch">All batches
  	 <input type="hidden" name="allbatches" id="allbatches"> 
  	 </div>
  	 <div class="col-sm-6" align="left">
  	  <input type="radio" name="validforbatch" value="specific" id="validforbatch">If you want to restrict the access of this notes to specific batches then use this multi-select
     </div>
     </div>
  	<br>  	
  	 <div class="form-group">
  	  <label for="role"  class="col-sm-4 control-label">Select Batch</label>
  	<div class="col-sm-5" align="left">
      <select id="batch" name="batch" class="form-control" multiple="multiple" disabled="disabled">
      </select>
      </div>
      <div class="col-sm-2" align="left">
	<span class="error" id="batcherror" name="batcherror"></span>
	</div>
     </div>
  	  <div class="form-group">
  	  <label for="role"  class="col-sm-4 control-label"></label>
  	<div class="col-sm-5" align="left">
      <button type="submit" class="btn btn-info" id="submit">Submit</button>
      </div>
     </div>
     </form>
   </div>
</body>
</html>