<!DOCTYPE html>
 <%@page import="java.io.IOException"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.List"%>
<html>
 <head>

 <style type="text/css">
 body {
	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
	font-size: 14px;
}

</style>

  <script>
  var prev= "";
  var globcounter=0;
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
	  
	 
          $('.hasDatepicker').datetimepicker({
              pickDate: false
          });

	  $(function () {
          $('#datetimepicker4').datetimepicker({
              pickDate: false
          });
      });
	  
$('#batchname').change(function(){
	
	var batchName = $('div#timetable').find('#batchname').val();
	var conf=true;
	if(prev!="" && prev!='-1')
		{
	 conf=confirm('If you change Batch your previous data will get lost Are You sure to Change Batch?');
		}
	if(conf==true){
	
	var table1=document.getElementById("timetablediv");
		  var rowCount=table1.rows.length;
		  for (var x=rowCount-1; x>0; x--) {
			  table1.deleteRow(x);
		   }
		globcounter=0;
		if(batchName!='-1')
			{
	add();
			}
	prev=batchName;
		
	}else{
		 document.getElementById("batchname").value =prev;
	}
});

 $("#add").click(function(){
	 var batchID = $('div#timetable').find('#batchname').val();
	 if(batchID!='-1')
	 {
		 
	  add();
	 }else{
		 alert('Please Select valid Batch');
	 }
	  
  });
 
 $("#submit").click(function(){
	 var batchID = $('div#timetable').find('#batchname').val();
	 
	 if(batchID!='-1')
		 {
	  		validate();
		 }else{
			 alert('Please Select valid Batch');
		 }
	  
 });
 
 function validate(){
	 
	 
	 var subjects="";
	 var teachers="";
	 var starttimes="";
	 var endtimes="";
	 var dates="";
	 var state=0;
	 for(var i=0;i<globcounter;i++)
		 {
		 subjects=$("#sub"+i).val();
			teachers=$("#teacher"+i).val();
			starttimes=$("#starttime"+i).val();
			endtimes=$("#endtime"+i).val();
			dates=$("#date"+i).val();
		 	if(subjects=="-1" || teachers=="-1" || starttimes=="" || endtimes=="" || dates=="")
		 		{
		 		/* var td=$(document.getElementById("error"+i));
	   			$(td).empty();
	   			$(td).append('<span>Enter All Fields</span>');
				$(td).show(); */
				
					state=1;
		 		}
		 	/* 	 	var startsplit=starttimes.split(" ");
		 	var endsplit=endtimes.split(" ");
		 	var startspitagain=startsplit[0].split(":");
		 	var endsplitagain=endsplit[0].split(":");
		 	
		 	if(startspitagain[0]>endsplitagain[0] && startsplit[1]==endsplit[1] )
		 {
		 		state=1;
		 }else if(startspitagain[0]=endsplitagain[0] && startspitagain[1]>endsplitagain[1] && startsplit[1]==endsplit[1] )
			 {
			 state=1;
			 }else if( startsplit[1]=='PM' && endsplit[1]=='AM')
				 {
				 state=1;
				 } */
				 
				var starttimes=$("#starttime"+i).val();
				var	endtimes=$("#endtime"+i).val();
					
				var startDate = new Date("January 01, 2015 "+starttimes);
				var endDate = new Date("January 01, 2015 "+endtimes);
				
				if(startDate>=endDate){
					state=2;
					modal.launchAlert("Error","Start Time Should be less than End Time");
					$("#starttime"+i).parents("td").addClass("has-error");
					$("#endtime"+i).parents("td").addClass("has-error");
				}
					
		 	if(!'undefined'.match(dates)){
		 	var valid=validatedate(dates);
		 	if(valid==false)
		 		{
		 		state=1;
		 		}
		 	}
		 }
	 
	 if(state==0)
		 {
	 subjects="";
	 teachers="";
	 starttimes="";
	 endtimes="";
	 dates="";
	 
	 for(var i=0;i<globcounter;i++){
		 
		if(i==0)
			{
			subjects=$("#sub"+i).val();
			teachers=$("#teacher"+i).val();
			starttimes=$("#starttime"+i).val();
			endtimes=$("#endtime"+i).val();
			
			dates=$("#date"+i).val();
			}else{
				subjects=subjects+","+$("#sub"+i).val();
				teachers=teachers+","+$("#teacher"+i).val();
				starttimes=starttimes+","+$("#starttime"+i).val();
				endtimes=endtimes+","+$("#endtime"+i).val();
				dates=dates+","+$("#date"+i).val();
			}
		 
	 }
	 var regId;
	 var batchID = $('div#timetable').find('#batchname').val();
	 $.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "addLectures",
		    	 batchID:batchID,
				 regId:regId,
				 subjects:subjects,
				 teachers:teachers,
				 starttimes:starttimes,
				 endtimes:endtimes,
				 dates:dates
		   		},
		   type:"POST",
		   success:function(data){
			   		/* $('#block'+regId).addClass('hide');
			   		$('#unblock'+regId).removeClass('hide');
			   		modal.launchAlert("Success","User Blocked!");	 */   
			   var resultJson = JSON.parse(data);
			   		var teacher=resultJson.teacher.split(",");
			   		var lecture=resultJson.lecture.split(",");
			   		var time=resultJson.time;
			   		var flag=0;
			   		for(var z=0;z<globcounter;z++)
			   			{
			   			var td=$(document.getElementById("error"+z));
			   			$(td).hide();
			   			}
			   		if(time=="" || time=="undefined"){
			   		for(var i=0;i<globcounter;i++)
			   			{
			   			var td=$(document.getElementById("error"+i));
			   			$(td).empty();
			   			for(var j=0;j<teacher.length;j++){
			   					if(teacher[i]!="")
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
			   				if(lecture[i]!=""){
			   						if(lecture[k].split("/")[1]==i+"" && lecture[k].split("/")[0]=="lecture")
										{
			   							flag=1;
			   								$(td).append('<span>Lecture Already Exists</span>');
			   								$(td).show();
										}			   					
			   					}
			   			}
			   			}
			   		}else{
			   			flag=1;
			   			modal.launchAlert("Error","Some lectures timmings are overlapped OR Start time is after end time");
			   		}
			   		
			   		if(flag==0)
			   			{
			   		 var table1=document.getElementById("timetablediv");
  					  var rowCount=table1.rows.length;
  					  for (var x=rowCount-1; x>0; x--) {
  						  table1.deleteRow(x);
  					   }
  					globcounter=0;
  						$("#testdiv").addClass("hide");
  						$('#lectureaddedmodal').modal('toggle');
  						$(td).hide();
  					
			   			}
			   		/* if(status[0]!="")
			   			{
			   		var tr=$(document.getElementById("tr"+status[1]));
			   		if(status[0]=="teacher")
			   			{
			   		$(tr).append('<td id="error">Teacher is busy in other lectures</td>');
			   			}else{
			   				$(tr).append('<td id="error">Batch schedule is already present in this time slot</td>');
			   			}
			   			}
			   		alert(status);
 */		   },
		   	error:function(){
		   		modal.launchAlert("Error","Error");
		   	}	
		});
		 }else if(state==1){
			 $("#fielderror").modal('toggle');
		 }
 }
 
$(function(){
	/*$("[id^='starttime']").datetimepicker({
		pickDate: false
	}).data("DateTimePicker").show();
	*/
	$('#testdiv').on('DOMNodeInserted', "tr", function () {
      var $that = $(this);
	  //console.log($(this).find("[id^='endtime']").html());
			 
		$(this).find("input[id^='starttime']").datetimepicker({
     pickDate: false
		}).on("dp.change",function (e) {
				console.log("sss");
				var m = e;
               $that.find("input[id^='endtime']").data("DateTimePicker").setMinDate(e.date);
			   $that.find("input[id^='endtime']").data("DateTimePicker").setDate(e.date);
 });
  
		$(this).find("input[id^='endtime']").datetimepicker({
			pickDate: false
		});
		
		$(this).find("input[id^='date']").datetimepicker({
			  pickTime: false,
			  minDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		  });
	});
})
 
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
	               if(teacherid[0]!=""){
	               for(var i=0;i<limit;i++)
	            	   {
	            	   
	            	   sell1Options[i+1]= new Option(firstname[i]+" "+lastname[i], teacherid[i]);
	            	   }
	               }else{
	            	   modal.launchAlert("Success","Not Available For This Subject");
	               }
			   	},
			   error:function(data){
				   alert("Teacher Not Available");
			   }
		});
  }
  
  function add()
  {
	  $("#testdiv").removeClass("hide");
	  var div=document.getElementById("timetablediv");
	  var div=document.getElementById("timetablediv");
	  var subjectTR=document.createElement("tr");
	  subjectTR.id="tr"+globcounter;
	  var subjectTD=document.createElement("td");
	  var teacherTD=document.createElement("td");
	  var starttimeTD=document.createElement("td");
	  starttimeTD.id="starttimeTD"+globcounter;
	  var endtimeTD=document.createElement("td");
	  endtimeTD.id="endtimeTD"+globcounter;
	  var dateTD=document.createElement("td");
	  var errorTD=document.createElement("td");
	  errorTD.id="error"+globcounter;
	  errorTD.width="100px"
	  errorTD.style.display="none";
	  
	  var select = document.createElement("select");
	  var id=globcounter;
	  select.id="sub"+globcounter;
	  select.setAttribute("class", "form-control");
	  select.onchange = function(){fillTeacher(id);};;
	  subjectTD.appendChild(select);
	  subjectTR.appendChild(subjectTD);
	  var teacherselect= document.createElement("select");
	  teacherselect.setAttribute("class", "form-control");
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
	  starttimeTextBox.setAttribute("class", "form-control");
	  starttimeTextBox.width=100;

	  starttimeTD.appendChild(starttimeTextBox);
	  subjectTR.appendChild(starttimeTD);
	  
	  var endtimeTextBox=document.createElement("input");
	  endtimeTextBox.setAttribute("type", "text");
	  endtimeTextBox.id= "endtime"+globcounter;
	  endtimeTextBox.setAttribute("class", "form-control");
	  endtimeTD.appendChild(endtimeTextBox);
	  subjectTR.appendChild(endtimeTD);
	  
	  var dateTextBox=document.createElement("input");
	  dateTextBox.setAttribute("type", "text");
	  dateTextBox.id= "date"+globcounter;
	  dateTextBox.class="hasDatepicker";
	  dateTextBox.setAttribute("class","hasDatepicker");
	  dateTextBox.setAttribute("class", "form-control");
	  dateTD.appendChild(dateTextBox);
	  subjectTR.appendChild(dateTD);
	  
	   var deletelink=document.createElement("a");
	   deletelink.id="delete"+id;
	   deletelink.innerHTML="Delete"
		deletelink.href="#";
	   deletelink.onclick=function(){deleteLecture(id);};
	 
	  var deleteTD=document.createElement("td");
	  deleteTD.id="deleteTD"+id;
	  deleteTD.appendChild(deletelink);
	  subjectTR.appendChild(deleteTD);
	  subjectTR.appendChild(errorTD);
	  div.appendChild(subjectTR);
	  var button=document.getElementById("add");
	  button.disabled=false;
	  filldropdown(); 
	  
  }
  
  function deleteLecture(id)
  {
	  document.getElementById("tr"+id).remove();
	  if($("#testdiv").find("tr").length == 1){
	  	$("#testdiv").addClass("hide");
	  }
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
			   var subjectnames= resultJson.Batchsubjects.split(",");
			   var subjectids= resultJson.BatchsubjectsIds.split(",");
			   
			      var counter=1;
			   var sell1Select = $("#"+"sub"+globcounter);
               if(sell1Select.prop) { 
                  var sell1Options = sell1Select.prop("options"); 
                } else { 
                  var sell1Options = sell1Select.attr("options"); 
                       } 
               $("option", sell1Select).remove();  
               
               sell1Options[0]= new Option("Select Subject", "-1");
               
               var limit=subjectnames.length;
               for(var i=0;i<limit;i++)
            	   {
            	   
            	   sell1Options[i+1]= new Option(subjectnames[i], subjectids[i]);
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
	<div id="timetable" align="center" class="container">	
		<%List<Batch> batch=(List<Batch>)request.getAttribute("batch"); 
		int i=0;%>
		<div class="container bs-callout bs-callout-danger" style="margin-bottom: 5px;">
			<div class="row">
				<div class='col-sm-6 header' style="padding-bottom: 10px;">*
					Create time table for batch here 
				</div>
			</div>
			<div class="row">
				<div class="col-md-4">
					<select name="batchname" id="batchname" class="form-control" width="100px">
					<option>Select Batch</option>
					<%
					while(i<batch.size()){
					%>
					<option value="<%=batch.get(i).getBatch_id()%>"><%=batch.get(i).getBatch_name() %></option>
					<%i++;} %>
					</select>
				</div>
				<div class="col-md-4">
				<button id="add" type="button" class="btn btn-info" disabled="true">Add lecture</button>
				</div>
				<div class="col-md-4"></div>
			</div>
		</div>
	</div>

<div id="testdiv" align="center" class="hide container">
<table id="timetablediv" class="table table-bordered table-hover" style="background-color: white;" data-toggle="table" width="600px" border="1">
	<tr>
		<th width="100px">Select Subject</th>
		<th width="100px">Select Teacher</th>
		<th width="100px">Start Time</th>
		<th width="100px">End Time</th>
		<th width="100px">Date</th>
		<th width="100px"></th>
		<!-- <th width="100px" style="display: none;" id="errorth"></th> -->
	</tr>	
</table>
<a href="#" id="submit"> <button type="button" class="btn btn-info" align="center">Submit</button></a>
</div>

<div class="modal fade" id="lectureaddedmodal" tabindex="-1" role="dialog" 
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
           Lecture Added Successfully..
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