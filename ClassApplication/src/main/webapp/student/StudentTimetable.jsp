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
		  pickTime: false
	  }).data("DateTimePicker");
	  
	  $("#submit").click(function(){
			var batchname=$("#batch").val();
			var classid=$("#classnameSelect").val();
			var date=$("#date").val();
			if(batchname!="-1" && classid!="-1" && date!="")
				{
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getschedule",
				    	 batchname:batchname,
				    	 date:date,
				    	 classid:classid
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
					   var prefix=resultJson.prefix.split(',');
					   var table=$(document.getElementById("scheduletable"));
					   var counter=0
					   var table1=document.getElementById("scheduletable");
						  var rowCount=table1.rows.length;
						  for (var x=rowCount-1; x>0; x--) {
							  table1.deleteRow(x);
						   }
					   $(table).border="1";
					   if(subjects[0]!=""){
					   while(counter<subjects.length)
						   {
						   if(prefix[counter]=="null"){
							   prefix[counter]="";
						   }
					   $(table).append("<tr><td>"+subjects[counter]+"</td><td>"+firstname[counter]+" "+lastname[counter]+" "+prefix[counter]+"</td><td>"+starttime[counter]+
					"</td><td>"+endtime[counter]+"</td><td>"+dates[counter]+"</td></tr>");
					   $(table).show();
					   counter++;
						   }
					   }else{
						   $("#lecturenotavailablemodal").modal('toggle');
					   }
					   $("#edit").show();
					   
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
<h3><font face="cursive">Timetable</font></h3>
<hr>
<form role="form" class="form-inline">
<div class="container">
<div class="jumbotron">
<div align="left" class="container">
<div class="col-xs-2">
Select Class
<select id="classnameSelect" class='form-control'>
<option value="-1">Select Class</option>
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes"); %>
<%
int counter=0;
while(list.size()>counter){ %>
<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName() %></option>
<%counter++;
} %>
</select>
</div>
<div class="col-xs-2">
				Select Batch:<select id="batch" class='form-control'><option value="-1">Select Batch</option></select>
</div>
<div class="col-xs-2">
Select Date
<div id="datetimepicker" class="input-group" style="width :150px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
	</div>
				

<div class="col-xs-2">	<br>			
   <button type="button" class="btn btn-danger"
      data-loading-text="Loading..."  id="submit">Submit
   </button>

</div>
</div>
</div>
</div>
</form>

<div class="container">
<table id="scheduletable" border="1" style="display:none" class="table table-bordered">
<thead>
<tr style="background-color: rgb(0, 148, 255);">
<th>Subject</th>
<th>Teacher</th>
<th>Start Time</th>
<th>End Time </th>
<th>Date</th>
 </tr>
</thead>
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
           Schedule Not Available...
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