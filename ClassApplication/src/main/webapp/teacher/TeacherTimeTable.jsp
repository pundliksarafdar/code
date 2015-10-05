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
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	  
	  $("#submit").click(function(){
			var classid=$("#teacherTimeTableClassnameDropDown").val();
			var date=$("#date").val();
			if(classid!="-1" && date!="")
				{
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
					   var division=resultJson.division.split(",");
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
					   $(table).append("<tr><td>"+division[counter]+"</td><td>"+batch[counter]+"</td><td>"+subject[counter]+"</td><td>"+starttime[counter]+
					"</td><td>"+endtime[counter]+"</td></tr>");
					   $(table).show();
					   counter++;
						   }
				   }else{
					   $("#scheduletable").hide();
					   $('#lectureupdatemodal').modal('toggle');
				   }
					    
					   		   	   },
				   	error:function(){
				   		modal.launchAlert("Error","Error");
				   	}	
				});
				}else{
					alert("Select Valid Class/Date");
				}
		});
	  
	
})
</script>
</head>
<body>
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;"><u>Time Table</u></div>
<form role="form" class="form-inline">
<div class="container">
<div align="left" class="container">
<div class="col-xs-2">
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes"); %>
<select id="teacherTimeTableClassnameDropDown"  class='form-control'>
<option value="-1">Select Class</option>
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
<div id="datetimepicker" class="input-group" style="width :150px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
</div>
<div class="col-xs-2">
   <button type="button" class="btn btn-danger" 
      data-loading-text="Loading..."  id="submit">Submit
   </button>
</div>
</div>
</div>
</form>
</div>

<div class="container">
<table id="scheduletable" border="1" style="display:none;background-color: white;" class="table table-bordered">
<thead>
<tr style="background-color: rgb(0, 148, 255);">
<th>Class</th>
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