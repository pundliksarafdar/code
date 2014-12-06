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
			    minDate:((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()
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

function edit(){
	var batchname=$("#batchname").val();
	var date=$("#date").val();
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "geteditschedule",
		    	 batchname:batchname,
		    	 date:date
		    	
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
			   var subjectname=resultJson.Batchsubjects.split(',');
			   var teacherids=resultJson.teacherids.split(',');
			   var scheduleids=resultJson.scheduleids.split(',');
			   
			   var allteachersfirstname=resultJson.allteachersfirstname.split('/');
			   var allteacherlastname=resultJson.allteacherlastname.split('/');
			   var allteacherids=resultJson.allteacherids.split('/');
			   var subids=resultJson.BatchsubjectsIds.split(',');
			   var table=$(document.getElementById("scheduletable"));
			   var table1=document.getElementById("scheduletable");
			  var rowCount=table1.rows.length;
			  for (var x=rowCount-1; x>0; x--) {
				  table1.deleteRow(x);
			   }
			   var counter=0
			   
			   if(scheduleids[0]!=""){
			   $(table).border="1";
			   if(subjects.length>0)
				   {
				   var actiontr=$(document.getElementById("scheduletr"));
				   $(actiontr).show();
				   }
			   while(counter<subjects.length)
				   {
				   var innercounter=0;
				   var ids=allteacherids[counter].split(',');
			   $(table).append("<tr id="+counter+"><td><select id=subject"+counter+" class='form-control'></select></td><td><select id=teacher"+counter+" class='form-control'></select></td><td><input type='text' id=start"+counter+" class='form-control' onload=settime(); onclick=gettime('start',"+counter+");></td><td><input type='text' id=end"+counter+" class='form-control' onload=settime(); onclick=gettime('end',"+counter+");></td><td><input type='text' id=date"+counter+" class='form-control' onload=settime(); onclick=gettime('date',"+counter+");></td><td><a id="+scheduleids[counter]+" onclick=deleteschedule("+scheduleids[counter]+");>Delete</a> <input type='hidden' value='"+scheduleids[counter]+"' id='schrduleid"+counter+"'></td><td id=error"+counter+" style='display: none'></td></tr>");
			   var id=counter;
			   var start=document.getElementById("start"+id);
			   var end=document.getElementById("end"+id);
			   var date=document.getElementById("date"+id);
			   start.value=starttime[counter];
			   end.value=endtime[counter];
			   date.value=dates[counter];
			   //start.onclick=function(){gettime("start",counter);};;
			   var select=$(document.getElementById("subject"+counter));
			   var teacherselect=$(document.getElementById("teacher"+counter));
			   while(innercounter<subjectname.length)
				   {
				   $(select).append("<option value="+subids[innercounter]+">"+subjectname[innercounter]+"</option>");
				   innercounter++;
				   }
			   innercounter=0;
				   var subcounter=0;
				   while(subcounter<subjectname.length){
					   if(subjects[counter]==subjectname[subcounter])
						   {
						   $(select).get(0).selectedIndex=subcounter;
						   subcounter="Y";
						   break;
						   }
					   subcounter++;
				   }
				   subcounter=0;
			
					   var fnames=allteachersfirstname[counter].split(',');
					   var lnames=allteacherlastname[counter].split(',');
					   var subinnercounter=0;
					   while(fnames.length>subinnercounter)
						   {
					   $(teacherselect).append("<option value="+ids[subinnercounter]+">"+fnames[subinnercounter]+" "+lnames[subinnercounter]+"</option>");
					   subinnercounter++;
						   }
					   subcounter=0;
					   while(subcounter<fnames.length){
						   if(teacherids[counter]==ids[subcounter]){
							   $(teacherselect).get(0).selectedIndex=subcounter;
							   break;
						   }
						   subcounter++;
					   }
					  
				   
				   
			   $(table).show();
			   counter++;
				   }
			   $("#update").show();
			   }else{
				   $("#update").hide();
				   $("#edit").hide();
			   }
			   		   	   },
		   	error:function(){
		   		modal.launchAlert("Error","Error");
		   	}	
		});

}

function deleteschedule(scheduleid)
{
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "deleteschedule",
		    	 scheduleid:scheduleid
		    	
		   		},
		   type:"POST",
		   success:function(data){
			   
			   var resultJson = JSON.parse(data);
			   edit();
			   		   	   },
		   	error:function(){
		   		modal.launchAlert("Error","Error");
		   	}	
		});

}

function validatedate(inputText)
{
	var dateformat = /^(0?[1-9]|1[012])[\/](0?[1-9]|[12][0-9]|3[01])[\/]\d{4}$/;
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
			  minDate:((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()
		  }).data("DateTimePicker");
		  

	
	$("#submit").click(function(){
		var batchname=$("#batchname").val();
		
		var date=$("#date").val();
	
		if(date!="")
		{
			var status=validatedate(date);
		if(status==true){
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getschedule",
			    	 batchname:batchname,
			    	 date:date
			    	
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
				   while(counter<subjects.length)
					   {
				   $(table).append("<tr><td>"+subjects[counter]+"</td><td>"+firstname[counter]+" "+lastname[counter]+"</td><td>"+starttime[counter]+
				"</td><td>"+endtime[counter]+"</td><td>"+dates[counter]+"</td></tr>");
				   $(table).show();
				   counter++;
					   }
				   $("#edit").show();
				   $("#update").hide();
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
	});
	
	
	
/* function editschedule(){
		var batchname=$("#batchname").val();
		var date=$("#date").val();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "geteditschedule",
			    	 batchname:batchname,
			    	 date:date
			    	
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
				   var subjectname=resultJson.Batchsubjects.split(',');
				   var teacherids=resultJson.teacherids.split(',');
				   var scheduleids=resultJson.scheduleids.split(',');
				   
				   var allteachersfirstname=resultJson.allteachersfirstname.split('/');
				   var allteacherlastname=resultJson.allteacherlastname.split('/');
				   var allteacherids=resultJson.allteacherids.split('/');
				   var subids=resultJson.BatchsubjectsIds.split(',');
				   var table=$(document.getElementById("scheduletable"));
				   var table1=document.getElementById("scheduletable");
				  var rowCount=table1.rows.length;
				  for (var x=rowCount-1; x>0; x--) {
					  table1.deleteRow(x);
				   }
				   var counter=0
				   $(table).border="1";
				   while(counter<subjects.length)
					   {
					   var innercounter=0;
					   var ids=allteacherids[counter].split(',');
				   $(table).append("<tr id="+counter+"><td><select id=subject"+counter+" class='form-control'></select></td><td><select id=teacher"+counter+" class='form-control'></select></td><td><input type='text' id=start"+counter+" class='form-control' onload=settime(); onclick=gettime('start',"+counter+");></td><td><input type='text' id=end"+counter+" class='form-control' onload=settime(); onclick=gettime('end',"+counter+");></td><td><input type='text' id=date"+counter+" class='form-control' onload=settime(); onclick=gettime('date',"+counter+");></td><td><a id="+scheduleids[counter]+" onclick=deleteschedule("+scheduleids[counter]+");>Delete</a> </td><td id=error"+counter+"></td></tr>");
				   var id=counter;
				   var start=document.getElementById("start"+id);
				   var end=document.getElementById("end"+id);
				   var date=document.getElementById("date"+id);
				   start.value=starttime[counter];
				   end.value=endtime[counter];
				   date.value=dates[counter];
				   //start.onclick=function(){gettime("start",counter);};;
				   var select=$(document.getElementById("subject"+counter));
				   var teacherselect=$(document.getElementById("teacher"+counter));
				   while(innercounter<subjectname.length)
					   {
					   $(select).append("<option value="+subids[innercounter]+">"+subjectname[innercounter]+"</option>");
					   innercounter++;
					   }
				   innercounter=0;
					   var subcounter=0;
					   while(subcounter<subjectname.length){
						   if(subjects[counter]==subjectname[subcounter])
							   {
							   $(select).get(0).selectedIndex=subcounter;
							   subcounter="Y";
							   break;
							   }
						   subcounter++;
					   }
					   subcounter=0;
				
						   var fnames=allteachersfirstname[counter].split(',');
						   var lnames=allteacherlastname[counter].split(',');
						   var subinnercounter=0;
						   while(fnames.length>subinnercounter)
							   {
						   $(teacherselect).append("<option value="+ids[subinnercounter]+">"+fnames[subinnercounter]+" "+lnames[subinnercounter]+"</option>");
						   subinnercounter++;
							   }
						  
					   
					   
				   $(table).show();
				   counter++;
					   }
				   $("#update").show();
				   		   	   },
			   	error:function(){
			   		modal.launchAlert("Error","Error");
			   	}	
			});
	} */
	
	 $("#update").click(function(){
		 var table=document.getElementById("scheduletable");
		 var globcounter=table.rows.length-1;
		
		 var subjects="";
		 var teachers="";
		 var starttimes="";
		 var endtimes="";
		 var dates="";
		 var state=0;
		 for(var i=0;i<globcounter;i++)
			 {
			 subjects=$("#subject"+i).val();
				teachers=$("#teacher"+i).val();
				starttimes=$("#start"+i).val();
				endtimes=$("#end"+i).val();
				dates=$("#date"+i).val();
			 	if(subjects=="-1" || teachers=="-1" || starttimes=="" || endtimes=="" || dates=="")
			 		{
			 		var td=$(document.getElementById("error"+i));
		   			$(td).empty();
		   			$(td).append('<span>Enter All Fields</span>');
					$(td).show();
						state=1;
			 		}
			 	
			 	var startsplit=starttimes.split(" ");
			 	var endsplit=endtimes.split(" ");
			 	var startspitagain=startsplit[0].split(":");
			 	var endsplitagain=endsplit[0].split(":");
			 	
			 	/* if(startspitagain[0]>endsplitagain[0] && startsplit[1]==endsplit[1] )
			 {
			 		state=1;
			 }else if(startspitagain[0]=endsplitagain[0] && startspitagain[1]>endsplitagain[1] && startsplit[1]==endsplit[1] )
				 {
				 state=1;
				 }else if( startsplit[1]=='PM' && endsplit[1]=='AM')
					 {
					 state=1;
					 } */
			 	validatedate(dates);
			 }
		 if(state!=1)
			 {
		 subjects="";
		 teachers="";
		 starttimes="";
		 endtimes="";
		 dates="";
		 schrduleid="";
		 for(var i=0;i<globcounter;i++){
			 
			if(i==0)
				{
				subjects=$("#subject"+i).val();
				teachers=$("#teacher"+i).val();
				starttimes=$("#start"+i).val();
				endtimes=$("#end"+i).val();
				dates=$("#date"+i).val();
				schrduleid=$("#schrduleid"+i).val();
				}else{
					subjects=subjects+","+$("#subject"+i).val();
					teachers=teachers+","+$("#teacher"+i).val();
					starttimes=starttimes+","+$("#start"+i).val();
					endtimes=endtimes+","+$("#end"+i).val();
					dates=dates+","+$("#date"+i).val();
					schrduleid=schrduleid+","+$("#schrduleid"+i).val();
				}
			 
		 }
		 var regId;
		 var batchID = $('#batchname').val();
		 $.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "updateLectures",
			    	 batchID:batchID,
					 regId:regId,
					 subjects:subjects,
					 teachers:teachers,
					 starttimes:starttimes,
					 endtimes:endtimes,
					 dates:dates,
					 batchID:batchID,
					 schrduleid:schrduleid
			   		},
			   type:"POST",
			   success:function(data){
				   		/*  $('#block'+regId).addClass('hide');
				   		$('#unblock'+regId).removeClass('hide');
				   		modal.launchAlert("Success","User Blocked!"); */	 
				   var resultJson = JSON.parse(data);
				   		var teacher=resultJson.teacher.split(",");
				   		var lecture=resultJson.lecture.split(",");
				   		var time=resultJson.time;
				   		var flag=0;
				   		var i=0;
				   		for(i=0;i<globcounter;i++){
				   			var td=$(document.getElementById("error"+i));
				   			$(td).hide();
				   		}
				   		
				   		for(i=0;i<globcounter;i++)
				   			{
				   			var td=$(document.getElementById("error"+i));
				   			$(td).empty();
				   				for(var j=0;j<teacher.length;j++){
				   					if(teacher[j]!="")
				   						{
				   						if(teacher[j].split("/")[1]==i+"" && teacher[j].split("/")[0]=="teacher")
				   							{
				   							flag=1;
				   							$(td).append('<span>Teacher is busy</span>');
				   							$(td).show();
				   							}
				   						}
				   				}
				   				for(var k=0;k<lecture.length;k++){
				   				if(lecture[k]!=""){
				   						if(lecture[k].split("/")[1]==i+"" && lecture[k].split("/")[0]=="lecture")
											{
				   							flag=1;
				   								$(td).append('<span>Lecture Already Exists</span>');
				   								$(td).show();
											}			   					
				   					}
				   					} 
				   			}
				   		if(time!=""){
				   			modal.launchAlert("Success","Some lectures timmings are overlapped OR Start time is after end time");
				   			flag=1;
				   		}
				   		
				   					if(flag==0){
				   						var table1=document.getElementById("scheduletable");
				   					  var rowCount=table1.rows.length;
				   					  for (var x=rowCount-1; x>0; x--) {
				   						  table1.deleteRow(x);
				   					   }
				   						$('#lectureupdatemodal').modal('toggle');
				   						$("#scheduletable").hide();
				   						$("#edit").hide();
				   						$("#update").hide();
				   						$(td).hide();
				   					}
				   			
				   			
			   }
				 ,
			   	error:function(){
			   		modal.launchAlert("Error","Error");
			   	}	
			});
			 }else{
				 $("#fielderror").modal('toggle');
			 }
	 });
});

</script>
</head>
<body>

<%List<Batch> batch=(List<Batch>)request.getAttribute("batch"); 
int i=0;%>
<form role="form" class="form-inline">
<div class="container">
<div class="jumbotron">
<div align="left" class="container">
<!-- <div class="col-xs-2" align="right">
<label>Select Batch</label>
</div> -->
<div class="col-md-4">
Select Batch
<select name="batchname" id="batchname" class='form-control'>
<option>Select Batch</option>
<%
while(i<batch.size()){
%>
<option value="<%=batch.get(i).getBatch_id()%>"><%=batch.get(i).getBatch_name() %></option>
<%i++;} %>
</select>
</div>
<!-- <div class="col-xs-2" align="right">
<label>Select Date</label>
</div> -->
<div class="col-md-2">
Select Date
<div id="datetimepicker" class="input-group" style="width :150px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date"/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				</div>
<div class="col-md-6"><br>
<input value="Submit" type="button" id="submit" class="btn btn-danger">
</div>
</div>
</div>
</div>
</form>
<div class="container">
<table id="scheduletable" class="table table-bordered table-hover" style="background-color: white; display:none;">
<thead>
<tr >
<th>Subject</th>
<th>Teacher</th>
<th>Start Time</th>
<th>End Time </th>
<th>Date</th>
<th id="scheduletr" style="display:none">Action</th>
 </tr>
</thead>
</table>
</div>
<div class="container">
<input value="Edit" type="button" id="edit" class="btn btn-danger" style="display: none" onclick="edit()">
<input value="Update" type="button" id="update" class="btn btn-danger" style="display: none;">
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
           Schedule Not Available...
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