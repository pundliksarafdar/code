<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
 <style type="text/css">
 .error{
     color: red;
    margin-left: 10px;
}      

 </style>
<script type="text/javascript">
function validate()
{
var file=document.getElementByID("myFile");
alert(file);
	return false;
}
var globalnotesid="";
var globaldivision="";
var globalsubject="";

function fetchnotes(){

	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchnotes",
		    	 division: globaldivision,
		    	 subject:globalsubject
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var notesname=resultJson.notesnames.split(",");
			   var notesids=resultJson.notesids.split(",");
			   var notespaths=resultJson.notespaths.split(",");
			   var table=document.getElementById("notestable");
				  var rowCount=table.rows.length;
				  for (var x=rowCount-1; x>0; x--) {
					  table.deleteRow(x);
				   }
				  var i=0;
				 
				  var notestable=$("#notestable");
				  if(notesids[0]!=""){
					  while(notesids.length>i){
						  notestable.append("<tr><td>"+(i+1)+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' onclick='editnotes("+notesids[i]+")'>Edit</button></td><td><button class='btn btn-info' id='edit' onclick='deletenotes("+notesids[i]+")'>Delete</button></td></tr>");
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
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "editnotesinformation",
		    	 notesid: notesid
		    	 
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
			   fetchnotes();
			   $("#deletenotesalert").modal('toggle');
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
}


$(document).ready(function(){
	$("#submit").click(function(){
		$("#subjecterror").html("");
		$("#divisionerror").html("");
		var subject=$("#subject").val();
		var division=$("#division").val();
		var flag=true;
		if(subject=="-1"){
			$("#subjecterror").html("Please select Subject");
			flag=false;
		}
		if(division=="-1"){
			$("#divisionerror").html("Please select Division");
			flag=false;
		}
		if(flag==true){
			globaldivision=division;
			globalsubject=subject;
			
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "fetchnotes",
			    	 division: division,
			    	 subject:subject
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var notesname=resultJson.notesnames.split(",");
				   var notesids=resultJson.notesids.split(",");
				   var notespaths=resultJson.notespaths.split(",");
				   var table=document.getElementById("notestable");
					  var rowCount=table.rows.length;
					  for (var x=rowCount-1; x>0; x--) {
						  table.deleteRow(x);
					   }
					  var i=0;
					 
					  var notestable=$("#notestable");
					  if(notesids[0]!=""){
						  while(notesids.length>i){
							  notestable.append("<tr><td>"+(i+1)+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td><td><button class='btn btn-info' id='edit' onclick='editnotes("+notesids[i]+")'>Edit</button></td><td><button class='btn btn-info' id='edit' onclick='deletenotes("+notesids[i]+")'>Delete</button></td></tr>");
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
				   var resultJson = JSON.parse(data);
				   var duplicate=resultJson.duplicate;
				   if(duplicate==null){
				   $("#editnotes").modal('hide');
				   fetchnotes();
				  $("#notesupdated").modal('toggle');
				   }else{
					   notesnameerror.html("Please enter different notes name");
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
<h3><font face="cursive">Search/Update Notes</font></h3>
<hr>
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
		<div class="row">
				<div class='col-sm-6 header' style="padding-bottom: 10px;" align="left">*
					Search/Edit Notes 
				</div>
			</div>
	<div class="row">
		<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
			 
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-3">
		<select name="subject" class="form-control" id="subject">
      <option value="-1">Select Subject</option>
      <%List<Subjects> list=(List<Subjects>)request.getAttribute("subjects"); 
      for(int i=0;i<list.size();i++)
      {
      %>
      <option value="<%=list.get(i).getSubjectId() %>"><%=list.get(i).getSubjectName()%></option>
      <%} %>
      </select>
		<span class="error" id="subjecterror" name="subjecterror"></span>
		</div>
		<div class="col-md-3">
		<select name="division" class="form-control" id="division">
      <option value="-1">Select Division</option>
      <%List<Division> divisions=(List<Division>)request.getAttribute("divisions"); 
      for(int i=0;i<divisions.size();i++)
      {
      %>
      <option value="<%=divisions.get(i).getDivId() %>"><%=divisions.get(i).getDivisionName()%>  <%=divisions.get(i).getStream() %></option>
      <%} %>
      </select>
		<span class="error" id="divisionerror" name="divisionerror"></span>
		</div>
		<div class="col-md-3">
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
   
   <div class="modal fade" id="notesnotavailable" tabindex="-1" role="dialog" 
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
           Notes not available for this subject
         </div>
         </div>
   </div>
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
		Notes Name :- <input type="text" id="notesname" class="form-control" maxlength="50"> <br>
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