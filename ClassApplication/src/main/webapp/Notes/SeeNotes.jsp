<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
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
	var institute=$("#institute").val();
	var division=$("#division").val();
	var subject=$("#subject").val();
	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "editnotesinformation",
		    	 notesid: notesid,
		    	 institute:institute,
		    	 division:division,
		    	 subject:subject
		    	 
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var notesname=resultJson.notesname;
			   var notesbatch=resultJson.notesbatches.split(",");
			   var allbatchnames=resultJson.allbatchnames.split(",");
			   var allbatchids=resultJson.allbatchids.split(",");
			   $("#newnotesname").val(notesname);
			   $("#batchesdiv").empty();
			   var i=0;
			   while(i<allbatchids.length){
				   var flag=false;
				   var j=0;
				   while(j<notesbatch.length){
					   if(allbatchids[i]==notesbatch[j].trim()){
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
	$("form#actionform #notesid").val(notesid);
	$("form#actionform #actionname").val("deletenotes");
	$("#actionform").prop("action","seenotes");
	$("#actionform").submit();
	/* $.ajax({
		 
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
		   }); */
}


$(document).ready(function(){
	$(".shownotes").on("click",function(){
		$("form#actionform #notesid").val($(this).prop("id"));
		$("#actionform").prop("action","shownotes");
		var formData = $("#actionform").serialize();
		console.log(formData);
		window.open("shownotes?"+formData,"","width=500, height=500"); 
	});
	
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
	
	$(".page").on("click",function(e){
		$("form#paginateform #currentPage").val($(this).text());
	//	$("#totalmarks").val($("#temptotalmarks").html());
	//	$("#addedIds").val(addedIds);
	//	$("#removedIds").val(removedIds);
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".start").on("click",function(e){
		$("form#paginateform #currentPage").val("1");
	//	$("#totalmarks").val($("#temptotalmarks").html());
	//	$("#addedIds").val(addedIds);
	//	$("#removedIds").val(removedIds);
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".end").on("click",function(e){
		$("form#paginateform #currentPage").val($("#totalPage").val());
	//	$("#totalmarks").val($("#temptotalmarks").html());
	//	$("#addedIds").val(addedIds);
	//	$("#removedIds").val(removedIds);
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$("#savenotes").click(function(){
		var notesname=$("#newnotesname").val();
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
			$("form#actionform #newbatch").val(batchids);
			$("form#actionform #actionname").val("editnames");
			 $("form#actionform #notesname").val( $("#newnotesname").val());
			$("form#actionform #notesid").val(globalnotesid);
			$("#actionform").prop("action","seenotes");
			$("#actionform").submit();
		/* $.ajax({
			 
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
			   }); */
			   
		}
	});
	
});
</script>
</head>
<body>
<div class="container" style="margin-bottom: 5px">
			<c:choose>
			<c:when test="${(institute ne null) && (institute ne '') }">
			<a type="button" class="btn btn-primary" href="teachercommoncomponent?forwardAction=seenotes" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a>
			</c:when>
			<c:otherwise><a type="button" class="btn btn-primary" href="choosesubject?forwardAction=seenotes" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Modify criteria</a></c:otherwise>
			</c:choose>
			</div>
			<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;"><u>Search Notes</u></div>
			</div>
<c:if test="${(totalPage!=0)}">
   <div id="notesdiv" class="container">
   <table id="notestable" class="table table-bordered table-hover" style="background-color: white;">
   <thead style="background-color: rgb(0, 148, 255);">
   	<tr>
   	<th>Sr No.</th>
   	<th>Name</th>
   	<th></th>
   	<th></th>
  <th></th> 	
   	</tr>
   </thead>
   <tbody>
   <c:forEach items="${noteslist}" var="item" varStatus="counter">
   <tr>
   <td><c:out value="${counter.count}"></c:out></td>
   <td><c:out value="${item.name}"></c:out></td>
    <td><button class="btn btn-primary shownotes" id='<c:out value="${item.notesid}"></c:out>'>Open</button></td>
    <c:choose>
    <c:when test="${role eq 2 }">
    	<c:choose>
    	<c:when test="${item.addedby eq user.regId }">
    		<td><button class="btn btn-primary" id="edit" onclick='editnotes("<c:out value="${item.notesid}"></c:out>")'>Edit</button></td>
   			<td><button class="btn btn-danger" onclick='deletenotes("<c:out value="${item.notesid}"></c:out>")'>Delete</button></td>
    	</c:when>
    	<c:otherwise>
    	 <td><button class="btn btn-primary" disabled="disabled">Edit</button></td>
   		 <td><button class="btn btn-danger" disabled="disabled">Delete</button></td> 
    	</c:otherwise>
    	</c:choose>
    </c:when>
    <c:otherwise>
   <td><button class="btn btn-primary" id="edit" onclick='editnotes("<c:out value="${item.notesid}"></c:out>")'>Edit</button></td>
   <td><button class="btn btn-danger" onclick='deletenotes("<c:out value="${item.notesid}"></c:out>")'>Delete</button></td> 
    </c:otherwise>
    </c:choose>
   
   </tr>
   </c:forEach>
   </tbody>
   </table>
   <form action="seenotes" id="paginateform">
  <input type="hidden" name="division" id="division" value='<c:out value="${division}"></c:out>'>
   <input type="hidden" name="batch" id="batch" value='<c:out value="${batch}"></c:out>'>
   <input type="hidden" name="subject" id="subject" value='<c:out value="${subject}"></c:out>'>
   <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
   <input type="hidden" name="newbatch" id="newbatch">
   <input type="hidden" name="actionname" id="actionname" >
   <input type="hidden" name="notesname" id="notesname" >
   <input type="hidden" name="notesid" id="notesid" >
   <input type="hidden" name="totalPage" id="totalPage" value='<c:out value="${totalPage}"></c:out>'>
   <input type="hidden" name="currentPage" id="currentPage"  value='<c:out value="${currentPage}"></c:out>'>
  <ul class="pagination">
  <li><a class="start" >&laquo;</a></li>
  <c:forEach var="item" begin="1" end="${totalPage}">
  <c:if test="${item eq currentPage}">
  <li class="active"><a href="#" class="page"><c:out value="${item}"></c:out></a></li>
  </c:if>
  <c:if test="${item ne currentPage}">
  <li><a href="#" class="page"><c:out value="${item}"></c:out></a></li>
  </c:if>
  </c:forEach>
  <li><a href="#" class="end">&raquo;</a></li>
</ul>
</form>
   </div>
   <form action="" id="actionform">
   <input type="hidden" name="division" id="division" value='<c:out value="${division}"></c:out>'>
   <input type="hidden" name="batch" id="batch" value='<c:out value="${batch}"></c:out>'>
   <input type="hidden" name="subject" id="subject" value='<c:out value="${subject}"></c:out>'>
   <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
   <input type="hidden" name="newbatch" id="newbatch">
   <input type="hidden" name="actionname" id="actionname" >
   <input type="hidden" name="notesname" id="notesname" >
   <input type="hidden" name="notesid" id="notesid" >
   <input type="hidden" name="totalPage" id="totalPage" value='<c:out value="${totalPage}"></c:out>'>
   <input type="hidden" name="currentPage" id="currentPage"  value='<c:out value="${currentPage}"></c:out>'>
   </form>
   
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
		Notes Name :- <input type="text" id="newnotesname" class="form-control" maxlength="50"> <br>
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
</c:if>
<c:if test="${(totalPage==0)}">
<div class="alert alert-info" align="center">Notes not available for selected criteria.</div>
</c:if>
</body>
</html>