<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
	  $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format:"DD/MM/YYYY"
	  }).data("DateTimePicker");
	  
	  $("#submit").click(function(){
			var batchname=$("#batch").val();
			var classid=$("#classnameSelect").val();
			var batchdivision=$("#studentdivision").val();
			var date=$("#date").val();
			$("#scheduletable").empty();
			if(batchname!="-1" && classid!="-1" && date!="")
				{
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getweeklyschedule",
				    	 batchname:batchname,
				    	 date:date,
				    	 classid:classid,
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
						   tableString=tableString+"<tr><td>"+starttime[counter]+
								"-"+endtime[counter]+"</td><td><table class='table table-condensed'><tr><td>"+subjects[counter]+"</td></tr><tr><td>"+firstname[counter]+"<br>"+lastname[counter]+"</td></tr></table></td></tr>"; 
						   scheduleflag=true;
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
				});
				}else{
					alert("Please Select Valid Class/Batch");
				}
		});
	  
	
	$("#classnameSelect").on("change",function(){
		var classid=$("#classnameSelect").val();
		var date=$("#date").val();
		$("#batch").empty();
		$("#batch").append("<option value='-1'>Select Batch</option>")
		if(classid!="-1"){
		$.ajax({
			   url: "classOwnerServlet",
			    data: {
			    	 methodToCall: "getstudentbatch",
			    	 classid:classid,
			    	 date:date
			   		}, 
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var nobatch=resultJson.nobatch;
				   if(nobatch==""){
				   var batchnames=resultJson.batchnames.split(',');
				   var batchids=resultJson.batchids.split(',');
				   var select=$(document.getElementById("batch"));
				   var counter=0;
				   while(counter<batchnames.length)
					   {
					   	$(select).append("<option value="+batchids[counter]+">"+batchnames[counter]+"</option>")
					   	counter++;
					   }
				   }else{
					   $("#notallocated").modal('toggle');
					   
				   }
			   	},
			   error:function(data){
				   alert("error");
			   }
		});
		}
	});
	
	
	
})
</script>
</head>
<body>
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;margin-bottom: 15px"><u>Weekly Time Table</u></div>
<form role="form" class="form-inline">
<div class="container">
<div align="left" class="container">
<div class="col-md-3">
<select id="classnameSelect" class='form-control'>
<option value="-1">Select Institute</option>
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes");
int studentdivision=(Integer)request.getAttribute("studentdivision");
%>
<%
int counter=0;
while(list.size()>counter){ %>
<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName() %></option>
<%counter++;
} %>
</select>
</div>
<div class="col-md-3">
		<select id="batch" class='form-control'><option value="-1">Select Batch</option></select>
</div>
<div class="col-md-3">
<div id="datetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
	</div>
				

<div class="col-md-3">	
   <button type="button" class="btn btn-danger"
      data-loading-text="Loading..."  id="submit">Submit
   </button>

</div>
<input type="hidden" id="studentdivision" value="<%=studentdivision%>">
</div>
</div>
</form> 
</div>


<div class="" style="margin: 5px">
<table id="scheduletable" class="table table-bordered table-hover" style="background-color: white; display:none;">
<thead>

</table>
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
           Lectures are not scheduled in selected week.
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="notallocated" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Batch
            </h4>
         </div>
         <div class="modal-body">
           You are not allocated in any batch of this class...
         </div>
         </div>
   </div>
</div>
</body>
</html>