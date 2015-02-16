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
			var classid=$("#classname").val();
			
			var date=$("#date").val();
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getteacherschedule",
				    	 classid:classid,
				    	 date:date
				    	
				   		},
				   type:"POST",
				   success:function(data){
					   
					   var resultJson = JSON.parse(data);
					   var batch=resultJson.batch.split(",");
					   var starttime=resultJson.starttime.split(",");
					   var endtime=resultJson.endtime.split(",");
					   var subject=resultJson.subject.split(",");
					   var table=$(document.getElementById("scheduletable"));
					   var counter=0
					   var table1=document.getElementById("scheduletable");
						  var rowCount=table1.rows.length;
						  for (var x=rowCount-1; x>0; x--) {
							  table1.deleteRow(x);
						   }
						 if(batch[0]!="") {
					   $(table).border="1";
					   while(counter<batch.length)
						   {
					   $(table).append("<tr><td>"+batch[counter]+"</td><td>"+subject[counter]+"</td><td>"+starttime[counter]+
					"</td><td>"+endtime[counter]+"</td></tr>");
					   $(table).show();
					   counter++;
						   }
				   }else{
					   $('#lectureupdatemodal').modal('toggle');
				   }
					    
					   		   	   },
				   	error:function(){
				   		modal.launchAlert("Error","Error");
				   	}	
				});
		});
	  
	
})
</script>
</head>
<body>

<form role="form" class="form-inline">
<div class="container">
<div class="jumbotron">
<div align="left" class="container">
<div class="col-xs-2">
Select Class
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes"); %>
<select id="classname"  class='form-control'>
<option>Select Class</option>
<%
int counter=0;
if(list!=null){
while(list.size()>counter){ %>
<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName() %></option>
<%counter++;
} }%>
</select>
</div>

<div class="col-xs-2">
Select Date
<div id="datetimepicker" class="input-group" style="width :150px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date"/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
</div>
<div class="col-xs-2"><br>
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
<tr>
<th>Batch</th>
<th>Subject</th>
<th>Start Time</th>
<th>End Time </th>
 </tr>
</thead>
</table>
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
           Lectures Not Available..
         </div>
         </div>
   </div>
</div>
</body>
</html>