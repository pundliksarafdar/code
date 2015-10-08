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
			var classid=$("#teacherTimeTableClassnameDropDown").val();
			var date=$("#date").val();
			$("#scheduletable").empty();
			if(classid!="-1" && date!="")
				{
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getteacherweeklyschedule",
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
					   var dates=resultJson.dates.split(',');
					   var alldates=resultJson.alldates.split(',');
					   var division=resultJson.division.split(',');
					   var table=$(document.getElementById("scheduletable"));
					   var counter=0
					   
						 if(batch[0]!="") {
							 $(table).border="1";
							   var tableString="<tr>";
							   var outercounter=0;
							   while(outercounter<dates.length){
								   tableString= tableString+"<td class='col-md-1'><table class='table  table-bordered'><tr><td colspan='2' style='background-color:rgb(181,181,181)'><b><u>"+dates[outercounter]+"</u><b></td></tr>";
								   counter=0;
								   var scheduleflag=false;
								   while(counter<subject.length)
							   {
							   
							   if(alldates[counter]==dates[outercounter]){
							   tableString=tableString+"<tr><td>"+starttime[counter]+
									"-"+endtime[counter]+"</td><td><table class='table table-condensed'><tr><td>"+subject[counter]+"</td></tr><tr><td>"+batch[counter]+"</td></tr><tr><td>"+division[counter]+"</td></tr></table></td></tr>"; 
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
							   $("#scheduletable").show();
				   }else{
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
			<div align="center" style="font-size: larger;"><u>Weekly Time Table</u></div>
<form role="form" class="form-inline">
<div class="container">
<div align="left" class="container">
<div class="col-md-3">
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes"); %>
<select id="teacherTimeTableClassnameDropDown"  class='form-control'>
<option value="-1">Select Institute</option>
<%
int counter=0;
if(list!=null){
while(list.size()>counter){ %>
<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName() %></option>
<%counter++;
} }%>
</select>
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
</div>
</div>
</form>
</div>

<div class="container">
<table id="scheduletable" border="1" style="display:none" class="table table-bordered">


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
           Lectures are not scheduled in selected week..
         </div>
         </div>
   </div>
</div>
</body>
</html>