<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
 <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css" type="text/css" />
  <script src="../js/bootstrap-datetimepicker.min.js"></script>
  
<script type="text/javascript">
function gettime(time,id){
	 var input; 
	 if(time=="date"){
		 input = $('#date'+id);
		 input.datetimepicker({
			    pickTime: false,
			    minDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()),
				  format: 'DD/MM/YYYY'
			});
	 }else{
	 if(time=="start")
		 {
input = $('#start'+id);
		 }else{
			 input = $('#end'+id);
		 }
input.datetimepicker({
    pickDate: false
});
	 }
input.data("DateTimePicker").show();
}


function validatedate(inputText)
{
	var dateformat = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
	if(inputText.match(dateformat)){
		
		var pdate = inputText.split('/');
		var mm  = parseInt(pdate[0]);  
		  var dd = parseInt(pdate[1]);  
		  var yy = parseInt(pdate[2]); 
		  var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31]; 
		  if (mm==1 || mm>2)  
		  {  
		  if (dd>ListofDays[mm-1])  
		  {  
		  alert('Invalid date format!');  
		  return false;  
		  }  
		  }  
		  if (mm==2)  
		  {  
		  var lyear = false;  
		  if ( (!(yy % 4) && yy % 100) || !(yy % 400))   
		  {  
		  lyear = true;  
		  }  
		  if ((lyear==false) && (dd>=29))  
		  {  
		  alert('Invalid date format!');  
		  return false;  
		  }  
		  if ((lyear==true) && (dd>29))  
		  {  
		  alert('Invalid date format!');  
		  return false;  
		  }  
		  }
		return true;
		
	}else  
	  {  
		  alert("Invalid date format!");  
		  return false;  
		  }  
		
}

$(document).ready(function(){
	
		  $( "#datetimepicker" ).datetimepicker({
			  pickTime: false,
			  minDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()),
			  format: 'DD/MM/YYYY'
		  }).data("DateTimePicker");
		  
		  $("#divisionID").change(function(){
				 var divisionID=$("#divisionID").val();
				 if(divisionID!="-1"){
					 $.ajax({ 
						   url: "classOwnerServlet",
						   data: {
						    	 methodToCall: "getDivisionBatches",
						    	 divisionID:divisionID
						   		},
						   type:"POST",
						   success:function(data){
							   var resultJson = JSON.parse(data);
							   var batchnames=resultJson.batchnames.split(",");
						   		var batchids=resultJson.batchids.split(",");
						   		$("#batchname").empty();
						   		if(batchids[0]!=""){
						   			$("#batchname").append("<option value='-1'>Select Batch</option>");
						   			for(var i=0;i<batchids.length;i++){
						   				$("#batchname").append("<option value='"+batchids[i]+"'>"+batchnames[i]+"</option>");
						   			}
						   			$("#batchname").prop("disabled",false);
						   		}else{
						   			modal.launchAlert("Batch","Batches are not available for selected class");
						   			$("#batchname").prop("disabled",true);
						   		}
							   		},
						   	error:function(){
						   		modal.launchAlert("Error","Error");
						   	}	
						}); 
				 }else{
					 $("#batchname").prop("disabled",true);
				 }
				
				  
			  });
	
		  
	$("#submit").click(function(){
		var batchname=$("#batchname").val();
		var batchdivision=$("#divisionID").val();
		var date=$("#date").val();
		var status=true;
		$("#scheduletable").empty();
		if(batchname=="-1"){
			status=false;
		alert("Please Enter Valid Batch")	
		}else{
		if(date!="")
		{
				status=validatedate(date);
			
		if(status==true){
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getweeklyschedule",
			    	 batchname:batchname,
			    	 date:date,
			    	 batchdivision:batchdivision
			    	
			   		},
			   type:"POST",
			   success:function(data){
				   
				   var resultJson = JSON.parse(data);
				   var subjects=resultJson.subjects.split(',');
				   var firstname=resultJson.firstname.split(',');
				   var lastname=resultJson.lastname.split(',');
				   var starttime=resultJson.starttime.split(',');
				   var endtime=resultJson.endtime.split(',');
				   var dates=resultJson.dates.split(',');
				  var alldates=resultJson.alldates.split(',');
				   var prefix = new Object();
				   
				   if(resultJson.prefix != null){
					   prefix=resultJson.prefix.split(',');
				   }
				   var table=$(document.getElementById("scheduletable"));
				   var counter=0
				   var table1=document.getElementById("scheduletable");
					  var rowCount=table1.rows.length;
					  for (var x=rowCount-1; x>0; x--) {
						  table1.deleteRow(x);
					   }
				  
				   var actiontr=$(document.getElementById("scheduletr"));
				   $(actiontr).hide();
				   if(subjects[0]!=""){
					   $(table).border="1";
					   var tableString="<tr>";
					   var outercounter=0;
					   while(outercounter<dates.length){
						   tableString= tableString+"<td class='col-md-1'><table class='table  table-bordered'><tr><td colspan='2' style='background-color:rgb(181,181,181)'><b><u>"+dates[outercounter]+"</u><b></td></tr>";
						   counter=0;
						   var scheduleflag=false;
						   while(counter<subjects.length)
					   {
					   var pre="";
					   if(prefix[counter]=="null" || prefix[counter] == undefined){
						   prefix[counter]="";
					   }
					   if(alldates[counter]==dates[outercounter]){
						   scheduleflag=true;
					   tableString=tableString+"<tr><td>"+starttime[counter]+
							"-"+endtime[counter]+"</td><td><table class='table table-condensed'><tr><td>"+subjects[counter]+"</td></tr><tr><td>"+firstname[counter]+"<br>"+lastname[counter]+"</td></tr></table></td></tr>"; 
					   }
				   counter++;
					   }
						   if(scheduleflag==false){
							   tableString=tableString+"<tr><td>Lecturs are not scheduled</td></tr>";
						   }
						   tableString=tableString+"</table></td>"
				   outercounter++;
					   }
					   tableString=tableString+"</tr>";
					   $(table).append(tableString);
				  /*  $("#edit").show();
				   $("#update").hide(); */
					   $(table).show();
				   }else{
					   rowCount=table1.rows.length;
						  for (var x=rowCount-1; x>0; x--) {
							  table1.deleteRow(x);
						   }
					   $("#edit").hide();
					   $("#lecturenotavailablemodal").modal('toggle');
					   
					   $(table).style="display:none"
				   }
				   		   	   },
			   	error:function(){
			   		modal.launchAlert("Error","Error");
			   	}	
			});}}else{
				alert("Please Enter Date");
			}
		}
	});
	
});

</script>
</head>
<body>

<%List<Batch> batch=(List<Batch>)request.getAttribute("batch"); 
List<Division> divisions=(List<Division>)request.getAttribute("divisions");
int i=0;%>
<form role="form" class="form-inline">
<div class="container">
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	<div align="center" style="font-size: larger;"><u>Weekly Time Table</u></div>
	<div class="row">
		<div class='col-sm-6 header' style="padding-bottom: 10px;">*
			Update time table for batch here</div>
	</div>
	<div align="left" class="row">
	<div class="col-md-4">
					<select name="divisionID" id="divisionID" class="form-control" width="100px">
					<option value="-1">Select Class</option>
					<%
					if(divisions!=null){
					while(i<divisions.size()){
					%>
					<option value="<%=divisions.get(i).getDivId()%>"><%=divisions.get(i).getDivisionName() %> <%=divisions.get(i).getStream() %></option>
					<%
					i++;
					}} %>
			</select>
			</div>
<div class="col-md-4">

<select name="batchname" id="batchname" class='form-control' disabled="disabled">
<option value="-1">Select Batch</option>
<%-- <%
while(i<batch.size()){
%>
<option value="<%=batch.get(i).getBatch_id()%>"><%=batch.get(i).getBatch_name() %></option>
<%i++;} %> --%>
</select><%-- 
<%for(int counter=0;counter<batch.size();counter++){ %>
<input type="hidden" id="division<%=batch.get(counter).getBatch_id() %>" value="<%=batch.get(counter).getDiv_id() %>">
<%} %> --%>
</div>
<!-- <div class="col-xs-2" align="right">
<label>Select Date</label>
</div> -->
<div class="col-md-2">
<div id="datetimepicker" class="input-group" style="width :150px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Start Date Of Week" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				</div>
<div class="col-md-2">
<input value="Submit" type="button" id="submit" class="btn btn-danger">
</div>
<div class="col-md-3">
<%if(i==0) {%>
<div class="header">No batch is added please add batch first </div>
<%} %>
</div>
</div>
</div>
</div>
</form>
<div class="" style="margin: 5px">
<table id="scheduletable" class="table table-bordered table-hover" style="background-color: white; display:none;">
<thead>

</table>
</div>
<div class="container">
<!-- <input value="Edit" type="button" id="edit" class="btn btn-danger" style="display: none" onclick="edit()">
<input value="Update" type="button" id="update" class="btn btn-danger" style="display: none;"> -->
</div>
<div class="modal fade" id="lectureupdatemodal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Lecture
            </h4>
         </div>
         <div class="modal-body">
           Lecture Updated Successfully..
         </div>
         </div>
   </div>
</div>
<div class="modal fade" id="lecturenotavailablemodal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Lecture
            </h4>
         </div>
         <div class="modal-body">
           batch is not schuduled for any lectures...
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="fielderror" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Lecture
            </h4>
         </div>
         <div class="modal-body">
           Enter All Fields Correctly..
         </div>
         </div>
   </div>
</div>
</body>
</html>