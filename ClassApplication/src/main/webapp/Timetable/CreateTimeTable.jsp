<!DOCTYPE html>
 <%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.List"%>
<html>
 <head>
 <!-- <link rel="stylesheet" href="../js/jquery-ui/jquery-ui.css" type="text/css" /> -->
 <!-- <link rel="stylesheet" href="../css/jquery-clockpicker.min.css" type="text/css" /> -->
 <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css" type="text/css" />
 
 <script src="../js/jquery-1.10.2.min.js"></script>
 <!-- <script src="../js/jquery-ui/jquery-ui.js"></script> -->
 <!-- <script src="../js/jquery-clockpicker.min.js"></script> -->
 <script src="../js/bootstrap-datetimepicker.min.js"></script>
 
 <style type="text/css">
 body {
	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
	font-size: 14px;
}
.container {
    padding: 20px;
}
</style>

  <script>
  var globcounter=0;
  
  $(document).ready(function(){
	  
	  $(function () {
          $('#datetimepicker4').datetimepicker({
              pickDate: false
          });
      });
	  
	  function getDate(id) {
		  $( "#date"+id ).datetimepicker({
			  pickTime: false
		  });
		  }
$('#batchname').change(function(){
	
	var batchName = $('div#timetable').find('#batchname').val();
	add();
	
});

 $("#add").click(function(){
	  add();
	  
	  
  });
 
 $("#submit").click(function(){
	  validate();
	  
 });
 
 function validate(){
	 
	 alert("hmmm");
 }
 
 function gettime(time,id){
	 var input; 
	 if(time=="starttime")
		 {
input = $('#starttime'+id);
		 }else{
			 input = $('#endtime'+id);
		 }
 input.datetimepicker({
     pickDate: false
 });
 
 input.data("DateTimePicker").show();
 }

  
  function fillTeacher(id)
  {
	  var teacherid=document.getElementById("teacher"+id);
	  teacherid.disabled=false;
	  var subname = $('div#testdiv').find('#sub'+id).val();
		  var reg;
		  $.ajax({
			   url: "classOwnerServlet",
			    data: {
			    	 methodToCall: "fetchSubjectTeacher",
			    	 subname:subname
			   		}, 
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var firstname= resultJson.firstname.split(",");
				   var lastname= resultJson.lastname.split(",");
				   var teacherid= resultJson.teacherid.split(",");
				      var counter=1;
				   var sell1Select = $("#teacher"+id);
	               if(sell1Select.prop) { 
	                  var sell1Options = sell1Select.prop("options"); 
	                } else { 
	                  var sell1Options = sell1Select.attr("options"); 
	                       } 
	               $("option", sell1Select).remove();  
	               
	               sell1Options[0]= new Option("Select Teacher", "-1");
	               
	               var limit=firstname.length;
	               for(var i=0;i<limit;i++)
	            	   {
	            	   
	            	   sell1Options[i+1]= new Option(firstname[i]+" "+lastname[i], teacherid[i]);
	            	   }
			   	},
			   error:function(data){
				   alert("error");
			   }
		});
  }
  
  function add()
  {
	  var div=document.getElementById("timetablediv");
	  var div=document.getElementById("timetablediv");
	  var subjectTR=document.createElement("tr");
	  var subjectTD=document.createElement("td");
	  var teacherTD=document.createElement("td");
	  var starttimeTD=document.createElement("td");
	  var endtimeTD=document.createElement("td");
	  var dateTD=document.createElement("td");
	  var select = document.createElement("select");
	  var id=globcounter;
	  select.id="sub"+globcounter;
	  select.onchange = function(){fillTeacher(id);};;
	  subjectTD.appendChild(select);
	  subjectTR.appendChild(subjectTD);
	  var teacherselect= document.createElement("select");
	  var teacheroption=new Option();
	  teacheroption.text="Select Teacher";
	  teacherselect.id="teacher"+globcounter;
	  teacherselect.options.add(teacheroption);
	  teacherselect.disabled="true";
	  teacherTD.appendChild(teacherselect);
	  subjectTR.appendChild(teacherTD);
	  
	  var starttimeTextBox=document.createElement("input");
	  starttimeTextBox.setAttribute("type", "text");
	  starttimeTextBox.id= "starttime"+globcounter;
	  starttimeTextBox.onclick=function(){gettime("starttime",id);};;
	  starttimeTextBox.width=100;

	  starttimeTD.appendChild(starttimeTextBox);
	  subjectTR.appendChild(starttimeTD);
	  
	  var endtimeTextBox=document.createElement("input");
	  endtimeTextBox.setAttribute("type", "text");
	  endtimeTextBox.id= "endtime"+globcounter;
	  endtimeTextBox.onclick=function(){gettime("endtime",id);};;
	  endtimeTD.appendChild(endtimeTextBox);
	  subjectTR.appendChild(endtimeTD);
	  
	  var dateTextBox=document.createElement("input");
	  dateTextBox.setAttribute("type", "text");
	  dateTextBox.id= "date"+globcounter;
	  dateTextBox.class="hasDatepicker";
	  dateTextBox.onclick=function(){getDate(id);};;
	  dateTD.appendChild(dateTextBox);
	  subjectTR.appendChild(dateTD);
	  
	  div.appendChild(subjectTR);
	  var button=document.getElementById("add");
	  button.disabled=false;
	  filldropdown(); 
	  
  }
  
  function filldropdown()
  {
	  var batchName = $('div#timetable').find('#batchname').val();
	  var reg;
	  $.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 methodToCall: "fetchBatchSubject",
		    		 batchName:batchName
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var parsedallDropdownJsonObject= resultJson.Batchsubjects.split(",");
			   
			      var counter=1;
			   var sell1Select = $("#"+"sub"+globcounter);
               if(sell1Select.prop) { 
                  var sell1Options = sell1Select.prop("options"); 
                } else { 
                  var sell1Options = sell1Select.attr("options"); 
                       } 
               $("option", sell1Select).remove();  
               
               sell1Options[0]= new Option("Select Subject", "-1");
               
               var limit=parsedallDropdownJsonObject.length;
               for(var i=0;i<limit;i++)
            	   {
            	   
            	   sell1Options[i+1]= new Option(parsedallDropdownJsonObject[i], parsedallDropdownJsonObject[i]);
            	   }
               globcounter++;
		   	},
		   error:function(data){
			   alert("error");
		   }
	});

	  
  }
  });
 </script> 
 </head>
 <body>
 <div id="timetable">

<%List<Batch> batch=(List<Batch>)request.getAttribute("batch"); 
int i=0;%>
<select name="batchname" id="batchname">
<option>Select Batch</option>
<%
while(i<batch.size()){
%>
<option value="<%=batch.get(i).getBatch_id()%>"><%=batch.get(i).getBatch_name() %></option>
<%i++;} %>
</select>
<button id="add" type="button" class="btn btn-info" onclick="add()" disabled="true">Add One more lecture</button>
</div>

<div id="testdiv">
<table id="timetablediv" border="1">
<tr>
<td>Select Subject</td>
<td>Select Teacher</td>
<td>Start Time</td>
<td>End Time</td>
<td>Date</td>
</tr>
</table>
</div>
<a href="#" id="submit"> <button type="button" class="btn btn-info">Submit</button></a>
<input type='text' id='datetimepicker4' />
</body>
 </html>