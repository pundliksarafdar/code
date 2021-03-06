<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
	$(document).ready(function() {
		
		$("#submitbtn").click(function(){
			var classid=$("#classnameselect").val();
			var batch = $("#batch").val();
			var subject = $("#subject").val();
			$("#classerror").html("");
			$("#batcherror").html("");
			$("#subjecterror").html("");
			var flag=true;
			if(classid=="-1"){
				$("#classerror").html("Please select class");
				flag=false;
			}
			if(batch=="-1"){
				$("#batcherror").html("Please select batch");
				flag=false;
			}
			if(subject=="-1"){
				$("#subjecterror").html("Please select subject");
				flag=false;
			}
			if(flag==true){
				$("#commonform")[0].submit();
			/* $.ajax({
				   url: "classOwnerServlet",
				    data: {
				    	 methodToCall: "getstudentnotes",
				    	 classid:classid,
				    	 batch:batch,
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
								  notestable.append("<tr><td>"+(i+1)+"</td><td>"+notesname[i]+"</td><td><a href='shownotes.action?notesid="+notesids[i]+"'>Click me</a></td></tr>");
								  i++;
							  }
							  $("#notestable").show();
							  
						  }else{
							  $("#notesnotavailable").modal('toggle');
							  $("#notestable").hide();
						  }
				   },
				   error:function(data){
					   alert("error");
				   }
			}); */
			}
				   
		});
		
		$("#classnameselect").on("change",function(){
			var classid=$("#classnameselect").val();
			
			$("#batch").empty();
			$("#batch").append("<option value='-1'>Select Batch</option>");
			if(classid!="-1"){
			$.ajax({
				   url: "classOwnerServlet",
				    data: {
				    	 methodToCall: "getstudentbatch",
				    	 classid:classid
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
						   	select.append("<option value="+batchids[counter]+">"+batchnames[counter]+"</option>")
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
		
		$("#batch").change(function(){
			var batchName = $("#batch").val();
			var batchdivision=$("#studentdivision").val();
			var institute=$("#classnameselect").val();
			$("#subject").empty();
			$("#subject").append("<option value='-1'>Select Subject</option>")
			  var reg;
			  $.ajax({
				   url: "classOwnerServlet",
				    data: {
				    	 methodToCall: "fetchBatchSubject",
				    		 batchName:batchName,
				    		 batchdivision:batchdivision,
				    		 institute:institute
				   		}, 
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   var subjectstatus=resultJson.subjectstatus;
					   if(subjectstatus==""){
					   var subjectnames= resultJson.Batchsubjects.split(",");
					   var subjectids= resultJson.BatchsubjectsIds.split(",");
					  
					   var select = $("#subject");
					   var counter=0;
					   while(counter<subjectids.length)
						   {
						   	select.append("<option value="+subjectids[counter]+">"+subjectnames[counter]+"</option>")
						   	counter++;
						   }
					   }else{
						  
							  $("#nosubjecterror").modal('toggle');
							  
					   }
		               
				   	},
				   error:function(data){
					   alert("error");
				   }
			});
		});
		
	});
</script>
</head>
<body>
	<form method="post" action="<c:out value="${forwardAction}" ></c:out>" id="commonform">
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	
		<div align="center" style="font-size: larger;margin-bottom: 15px"><u><c:out value="${lable}"></c:out></u></div>
		<div class="row">
		<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
			 
		</div>
		</div>
		<div class="row">
		<div class="col-md-3">
				<%
					List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes");
					int studentdivision=(Integer)request.getAttribute("studentdivision");
				%>
				<input type="hidden" id="studentdivision" name="division"  value="<%=studentdivision%>">
				<select id="classnameselect" class="form-control" name="institute">
					<option value="-1">Select Institute</option>
					<%
						int counter=0;
					while(list.size()>counter){
					%>
					<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName()%></option>
					<%
						counter++;
					}
					%>
				</select>
				<span class="error" id="classerror" name="classerror"></span>
			</div>
			
				
			
		<div class="col-md-3">
			
				<select id="batch" class="form-control" name="batch"><option value="-1">Select Batch</option></select>
		
				<span class="error" id="batcherror" name="batcherror"></span>
			
		</div>
		
		<div class="col-md-3">
			
				<select id="subject" class="form-control" name="subject"><option value="-1">Select Subject</option></select>
			
				<span class="error" id="subjecterror" name="subjecterror"></span>
			
		</div>
		<div class="col-md-3">
			
				<button type="button" class="btn btn-primary"
					data-loading-text="Loading..." id="submitbtn">Continue</button>
			
		</div>
	</div>
	</div>
	</form>
	<div id="notesdiv" class="container">
   <table id="notestable" class="table table-bordered table-hover" style="background-color: white; display:none;">
   <thead style="background-color: rgb(0, 148, 255);">
   	<tr>
   	<th>Sr No.</th>
   	<th>Name</th>
   	<th></th>
   	</tr>
   </thead>
   </table>
   </div>
	
	<div class="modal fade" id="notallocated" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">�
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
<div class="modal fade" id="nosubjecterror" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">�
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Subject
            </h4>
         </div>
         <div class="modal-body">
           Please add subjects in batch...
         </div>
         </div>
   </div>
</div>

<div class="modal fade" id="notesnotavailable" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">�
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
</body>
</html>