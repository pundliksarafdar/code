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
var globalnotesid="";
var globaldivision="";
var globalsubject="";
var globalclass="";

function fetchteachernotes(){
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchteachernotes",
		    	 division: globaldivision,
		    	 subject:globalsubject,
		    	 classes:globalclass
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var notesname=resultJson.notesnames.split(",");
			   var notesids=resultJson.notesids.split(",");
			   var notespaths=resultJson.notespaths.split(",");
			   var addedbyteacher=resultJson.addedbyteacher.split(",");
			   
			   var table=document.getElementById("notestable");
				  var rowCount=table.rows.length;
				  for (var x=rowCount-1; x>0; x--) {
					  table.deleteRow(x);
				   }
				  var i=0;
				 
				  var notestable=$("#notestable");
				  if(notesids[0]!=""){
					  var j=1;
					  while(notesids.length>i){
						  if(addedbyteacher[i]=="true")
							  {
						  notestable.append("<tr><td>"+j+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' onclick='editnotes("+notesids[i]+")'>Edit</button></td><td><button class='btn btn-info' id='edit' onclick='deletenotes("+notesids[i]+")'>Delete</button></td></tr>");
							 	j++;
							  }
						  i++;
					  }
					  i=0;
					  while(notesids.length>i){
						  if(addedbyteacher[i]=="false")
							  {
						  notestable.append("<tr><td>"+j+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' disabled>Edit</button></td><td><button class='btn btn-info' id='edit' disabled>Delete</button></td></tr>");
							  j++;
							  }
						  i++;
					  }
					  $("#notestable").show();
					  
				  }else{
					  $("#notesnotavailable").modal('toggle');
					  $("#notestable").hide();
				  }
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	
}

function editnotes(notesid){
	globalnotesid=notesid;
	var classes = $('#classnameselect').val();
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "editnotesinformation",
		    	 notesid: notesid,
		    	 classes:classes
		    	 
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var notesname=resultJson.notesname;
			   var notesbatch=resultJson.notesbatches.split(",");
			   var allbatchnames=resultJson.allbatchnames.split(",");
			   var allbatchids=resultJson.allbatchids.split(",");
			   $("#notesname").val(notesname);
			   $("#batchesdiv").empty();
			   var i=0;
			   while(i<allbatchids.length){
				   var flag=false;
				   var j=0;
				   while(j<notesbatch.length){
					   if(allbatchids[i]==notesbatch[j]){
						   flag=true; 
					   }
					   j++;
				   }
				   if(flag==true){
					   $("#batchesdiv").append("<input type='checkbox' name='batches' id='batches' value='"+allbatchids[i]+"' checked>"+allbatchnames[i]);
				   }else{
				   $("#batchesdiv").append("<input type='checkbox' name='batches' id='batches' value='"+allbatchids[i]+"'>"+allbatchnames[i]);
				   }
				   $("#batchesdiv").append("<br>");
				   i++;
			   }
			   $("#editnotes").modal('toggle');
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
}

function deletenotes(notesid){
	globalnotesid=notesid;
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "deletenotes",
		    	 notesid: notesid
		    	 
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   fetchteachernotes();
			   $("#deletenotesalert").modal('toggle');
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
}

$(document).ready(function() {
	
	$("#savenotes").click(function(){
		var notesname=$("#notesname").val();
		var notesnameerror=$("#notesnameerror");
		var batchidmap;
		var batchids="";
		var regex = /^[a-zA-Z0-9 ]*$/;
		var flag=true;
		notesnameerror.html("");
		if(notesname==""){
			notesnameerror.html("Please enter notes name");
			flag=false;
		}else if(!notesname.match(regex)){
			notesnameerror.html("Please enter valid notes name (No special characher allowed) ");
			flag=false;
		}
		batchidmap=$("input[name='batches']:checked").map(function() {
			return this.value;
		});
		
		var i=0;
		while(i<batchidmap.size())
			{
			if(i==0)
				{
				batchids=batchids+batchidmap[0]+"";
				}else{
					batchids=batchids+","+batchidmap[i];
				}
			i++;
			}
		if(flag==true){
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "updatenotes",
			    	 batchids: batchids,
			    	 notesid:globalnotesid,
			    	 notesname:notesname
			   		},
			   type:"POST",
			   success:function(data){
				   $("#editnotes").modal('hide');
				   fetchteachernotes();
				  $("#notesupdated").modal('toggle');
				   
			   },
			   error:function(){
			   		modal.launchAlert("Error","Error");
			   	}
			   });
			   
		}
	});
	
	
	$("#submit").click(function(){
		$("#subjecterror").html("");
		$("#divisionerror").html("");
		var subject=$("#subject").val();
		var division=$("#division").val();
		var classes = $('#classnameselect').val();
		var flag=true;
		if(subject=="-1"){
			$("#subjecterror").html("Please select Subject");
			flag=false;
		}
		if(division=="-1"){
			$("#divisionerror").html("Please select Division");
			flag=false;
		}
		if(classes=="-1"){
			$("#classnameerror").html("Please select Class");
			flag=false;
		}
		if(flag==true){
		
			globaldivision=division;
			globalsubject=subject;
			globalclass=classes;
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "fetchteachernotes",
			    	 division: division,
			    	 subject:subject,
			    	 classes:classes
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var notesname=resultJson.notesnames.split(",");
				   var notesids=resultJson.notesids.split(",");
				   var notespaths=resultJson.notespaths.split(",");
				   var addedbyteacher=resultJson.addedbyteacher.split(",");
				   
				   var table=document.getElementById("notestable");
					  var rowCount=table.rows.length;
					  for (var x=rowCount-1; x>0; x--) {
						  table.deleteRow(x);
					   }
					  var i=0;
					 
					  var notestable=$("#notestable");
					  if(notesids[0]!=""){
						  var j=1;
						  while(notesids.length>i){
							  if(addedbyteacher[i]=="true")
								  {
							  notestable.append("<tr><td>"+j+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' onclick='editnotes("+notesids[i]+")'>Edit</button></td><td><button class='btn btn-info' id='edit' onclick='deletenotes("+notesids[i]+")'>Delete</button></td></tr>");
								 j++;
								  }
							  i++;
						  }
						  i=0;
						  while(notesids.length>i){
							  if(addedbyteacher[i]=="false")
								  {
							  notestable.append("<tr><td>"+j+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' disabled>Edit</button></td><td><button class='btn btn-info' id='edit' disabled>Delete</button></td></tr>");
								  j++;
								  }
							  i++;
						  }
						  $("#notestable").show();
						  
					  }else{
						  $("#notesnotavailable").modal('toggle');
						  $("#notestable").hide();
					  }
			   },
				error:function(){
			   		modal.launchAlert("Error","Error");
			   	}
			   });
		}
	});
	
	$('#classnameselect').change(function(){
		var classes = $('#classnameselect').val();
		var flag=true;
		
		if(classes=="-1"){
			 var divisionselect=$('#division');
			   $("#divisionerror").html("");
			   divisionselect.empty();
			   divisionselect.append("<option value='-1'>Select Division</option>");
			   $("#subjecterror").html("");
			   var subjectselect=$('#subject');
			   subjectselect.empty();
			   subjectselect.append("<option value='-1'>Select Subject</option>");
			flag=false;
		}
		if(flag==true){
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getsubjectsanddivisions",
			    	 classes: classes
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var subjectnames=resultJson.subjectnames.split(",");
				   var subjectids=resultJson.subjectids.split(",");
				   var divisionnames=resultJson.divisionnames.split(",");
				   var divisionids=resultJson.divisionids.split(",");
				   $("#subjecterror").html("");
				   var subjectselect=$('#subject');
				   subjectselect.empty();
				   subjectselect.append("<option value='-1'>Select Subject</option>");
				   if(subjectids[0]!=""){
					   var i=0;
					   while(i<subjectids.length){
						   subjectselect.append("<option value="+subjectids[i]+">"+subjectnames[i]+"</option>");
						   i++;
					   }
					   
				   }else{
					   $("#subjecterror").html("No subjects assigned to you");
				   }
				   
				   var divisionselect=$('#division');
				   $("#divisionerror").html("");
				   divisionselect.empty();
				   divisionselect.append("<option value='-1'>Select Division</option>");
				   if(divisionids[0]!=""){
					   var i=0;
					   while(i<divisionids.length){
						   divisionselect.append("<option value="+divisionids[i]+">"+divisionnames[i]+"</option>");
						   i++;
					   }
					   
				   }else{
					   $("#divisionerror").html("No Division present");
				   }
				   
			   },
				error:function(){
			   		modal.launchAlert("Error","Error");
			   	}
			   });
		}
		
	});
	
	
});

</script>
</head>
<body>
	<%
		List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes");
	%>
	<div class="container">
	<div class="form-group">
		<label for="classnameselect" class="col-sm-4 control-label">Select
			Class :</label>
		<div class="col-sm-5" align="left">
			<select id="classnameselect" class='form-control'>
				<option value="-1">Select Class</option>
				<%
					int counter=0;
				if(list!=null){
				while(list.size()>counter){
				%>
				<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName()%></option>
				<%
					counter++;
				} }
				%>
			</select>
		</div>
		<div class="col-sm-2" align="left">
			<span class="error" id="classnameerror" name="classnameerror"></span>
		</div>
	</div>
	
	<div class="form-group">
	<label for="subject"  class="col-sm-4 control-label">Select Subject :</label>
      <div class="col-sm-5" align="left">
	<select name="subject" class="form-control" id="subject">
      <option value="-1">Select one</option>
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
      </select>
  	</div>
  	<div class="col-sm-2" align="left">
	<span class="error" id="divisionerror" name="divisionerror"></span>
	</div>
</div>

<div class="form-group">
  	  <label for="role"  class="col-sm-4 control-label"></label>
  	<div class="col-sm-5" align="left">
      <button type="submit" class="btn btn-info" id="submit">Submit</button>
      </div>
     </div>
     </div>
     <hr>
     <div id="notesdiv" class="container">
   <table id="notestable" class="table table-bordered table-hover" style="background-color: white; display:none;">
   <thead style="background-color: rgb(0, 148, 255);">
   	<tr>
   	<th>Sr No.</th>
   	<th>Name</th>
   	<th></th>
   	<th></th>
   	<th></th>
   	</tr>
   </thead>
   </table>
   </div>
   <div class="modal fade" id="notesupdated" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           Notes updated successfully..
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="deletenotesalert" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body">
           Notes deleted successfully..
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="editnotes" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Notes
            </h4>
         </div>
         <div class="modal-body" align="left">
		Notes Name :- <input type="text" id="notesname" class="form-control"> <br>
		<span id="notesnameerror" class="error"></span><br>
		This Notes is applicatble for following batches:- <br>
		<div id="batchesdiv"> 
		
		</div>         
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="savenotes">Save</button>
         </div>
         </div>
   </div>
    
</div>
</body>
</html>