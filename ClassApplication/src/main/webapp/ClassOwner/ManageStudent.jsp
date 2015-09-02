<meta http-equiv="cache-control" content="max-age=0" />
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.STUDENT_LIST); %>
<script>
var batchIds;
var selectedStudentIds;
var noofpages=0; 
var pno="";
function canceledit(batchID,pagenumber){
	//alert(batchID+" "+pagenumber);
	$('#studentDetailTable').remove(); 
	   if(batchID!=null){
	   $('#batchselected').val(batchID);
	   getstudentsrelatedtobatch(pagenumber);
	   }
}
function getstudentsrelatedtobatch(pagenumber){
	
	var batchname=$("#batchselected").val();
	if(batchname!="-1"){
		$("#students").hide();
		$("#pagination").hide();
		$("#studentDetailTable").remove();
		$("#noStudentError").hide();
		pno=pagenumber;
	$.ajax({
		url:"classOwnerServlet",
		data:{
			batchname:batchname,
			methodToCall:"getstudentsrelatedtobatch",
			pagenumber:pagenumber
		},
		type:"post",
		success:function(data){
			var resultJson = JSON.parse(data);
			var table=document.getElementById("students");
			  var rowCount=table.rows.length;
			  for (var x=rowCount-1; x>0; x--) {
				  table.deleteRow(x);
			   }
			var studentnames=resultJson.studentnames.split(',');
			var studentids=resultJson.studentids.split(',');
			var count=resultJson.count;
			var remain=resultJson.remain;
			var pages=resultJson.pages;
			
			if(studentids[0]!=""){
				var counter=0;
			while(counter<studentids.length){
				$("#students").append("<tr><td>"+studentids[counter]+"</td><td>"+studentnames[counter]+"</td><td><a><button type='button' class='btn btn-info' onclick=searchStudentthroughtable('"+studentids[counter]+"') >Edit</button></a></td></tr>");
				counter++;
			}
			$("#students").show();
			counter=0;
			$("#pagest").remove();
			if(noofpages!=0){
				while(counter<noofpages){
					$("#page"+counter).remove();
					counter++;
				}
			}
			
			counter=0;
			if(pagenumber==1)
			{
			$("#pagination").append("<li class='disabled' id=pagest><span>&laquo;</span></li>");
			}else{
				$("#pagination").append("<li><a href='#' id=pagest onClick=getstudentsrelatedtobatch("+(pagenumber-1)+")><span>&laquo;</span></li>");
			}
			while(counter<pages){
				var temp=counter+1;
				
				if(temp==pagenumber){
					$("#pagination").append("<li class='active' id=page"+counter+"><span>"+temp+"</span></li>");
				}else{
				$("#pagination").append("<li><a href='#' id=page"+counter+" onClick=getstudentsrelatedtobatch("+temp+")>"+temp+"</a></li>");
				}
				counter++;
			}
			
			noofpages=pages;
			/* $("#pagination").append("<li><a href='#'>&raquo;</a></li>"): */
			}else{
				$("#noStudentError").show();
			}
			if(noofpages>1){
				$("#pagination").show();
			}
			
		},
		error:function(e){
			//modal.launchalert("error","Yo2");
		}
		
	});
	}else{
		$("#noStudentError").hide();
	}
}
function getSelectedBatchesForStudent(){
	var batches;
	batchIds="";
	batches=$(".chkBatch:checked").map(function(){
	return this.value;
	});
	$(".chkBatch:checked").removeAttr('checked');
	var i=0;
	while(i<batches.size()){
		if(i==0){
			batchIds=batches[0]+"";
		}else{
			batchIds=batchIds+","+batches[i];
		}
		i++;
	}
}

function getSelectedStudentsToDelete(){
	var studentIds;
	studentIds=$(".chkStudent:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<studentIds.size()){
		if(i==0){
			selectedStudentIds=studentIds[0]+"";
		}else{
			selectedStudentIds=selectedStudentIds+","+studentIds[i];
		}
		i++;
	}
}


function searchStudent() {
	var studentLoginName=document.getElementById("studentLoginNameSearch").value;
	var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
	if(!studentLoginName || studentLoginName.trim()==""||studentLoginName.lenght==0 ){
		 modal.launchAlert("Error","Error!</strong> Student login name cannot be blank");		
	}else if($("#studentLoginNameSearch").val().length<5 || !$("#studentLoginNameSearch").val().match(regloginname))
	{
		 modal.launchAlert("Error","Error!</strong>Invalid Student login name");
	}else if($("#studentLoginNameSearch").val().length<5 || !$("#studentLoginNameSearch").val().match(regloginname))
	{
		modal.launchAlert("Error","Error!</strong>Invalid Student login name");
	}else{
		$("#studentDetailTable").remove();
	$.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 methodToCall: "searchStudent",
		    	 studentLgName:studentLoginName
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);   

			   if(resultJson.status != 'error'){
			    var firstname= resultJson.studentFname;
			   var lastname= resultJson.studentLname;
			   var studentId= resultJson.studentId;
			   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
				modal.launchAlert("Success","Found "+firstname+" "+lastname+"! Page will refresh in soon");
						   setTimeout(function(){
							   location.reload();
						   },2*1000);
			   }else{
				 	 if(!resultJson.message){
				 		modal.launchAlert("Error","Error while searching student!");
			   	   	}else{
			   	   		modal.launchAlert("Error",resultJson.message);
			   	   		
			   	   	}
				 	setTimeout(function(){
				   		location.reload();
				   	},1000*3);
			      }			   
		   	},
		   error:function(data){
			   modal.launchAlert("Error","Student with login name : "+studentLoginName+" not found!");
			   	setTimeout(function(){
			   		location.reload();
			   	},1000*3);
		   }
			   
	});
	}
}

function searchStudentthroughtable(studentLoginName) {
		var batchname=$("#batchselected").val();
		$("#studentDetailTable").remove();
	$.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 methodToCall: "searchStudent",
		    	 studentLgName:studentLoginName,
		    	 pagenumber:pno,
		    	 batchID:batchname
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);   

			   if(resultJson.status != 'error'){
			    var firstname= resultJson.studentFname;
			   var lastname= resultJson.studentLname;
			   var studentId= resultJson.studentId;
						   setTimeout(function(){
							   location.reload();
						   },2*1000);
			   }else{
				 	 if(!resultJson.message){
				 		modal.launchAlert("Error","Error while searching student!");
			   	   	}else{
			   	   		modal.launchAlert("Error",resultJson.message);
			   	   		
			   	   	}
				 	setTimeout(function(){
				   		location.reload();
				   	},1000*3);
			      }			   
		   	},
		   error:function(data){
			   modal.launchAlert("Error","Student with login name : "+studentLoginName+" not found!");
			   	setTimeout(function(){
			   		location.reload();
			   	},1000*3);
		   }
			   
	});
	
}

function getSelectedStudentsToDelete(){
	var studentIds;
	studentIds=$(".chkStudent:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<studentIds.size()){
		if(i==0){
			selectedStudentIds=selectedStudentIds+studentIds[0]+"";
		}else{
			selectedStudentIds=selectedStudentIds+","+studentIds[i];
		}
		i++;
	}
}

	$(document).ready(function(){
		
		
	/* 	$('.batchName').tooltip({'placement':'right','html':'true'}).on('click',function(){
			$(this).tooltip('hide');
		});
		
		$('.addsubject2batch').tooltip();

		$('.addsubject2batch').popover({'placement':'bottom','content':$('#allSubject').html(),'html':true}); */
		
		$('div#addStudentModal').on('click','button#btn-addStudent',function(){
			batchIds = "";
			
			$('div#addStudentModal .error').html('');
			$('div#addStudentModal .error').hide();
			var studentLgName;
			var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
			studentLgName = $('div#addStudentModal').find('#studentLoginName').val();
			var divisionName ="";
			divisionId = $('div#addStudentModal').find('#divisionName').val();

			getSelectedBatchesForStudent();		
			if(!studentLgName || studentLgName.trim()==""){
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Student login name cannot be blank');
				$('div#addStudentModal .error').show();
			}else if($("#studentLoginName").val().length<5 || !$("#studentLoginName").val().match(regloginname))
			{
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Invalid Student login');
				$('div#addStudentModal .error').show();
			}else if(!divisionId || divisionId.trim()=="" || divisionId == -1){
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Please select division');
				$('div#addStudentModal .error').show();
			}else if(batchIds==""){
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>please select atleast one batch');
				$('div#addStudentModal .error').show();
			}else{
				$('div#addStudentModal .progress').removeClass('hide');
				$('.add').addClass('hide');
			$.ajax({
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "addStudent",
						 regId:'',
						 studentLgName:studentLgName,
						 batchIds:batchIds,	
						 divisionId:divisionId,				 
				   		},
				   type:"POST",
				   success:function(e){
					   $('div#addStudentModal .progress').addClass('hide');
					   var resultJson = JSON.parse(e);
					      if(resultJson.status != 'error'){
					   	   $('div#addStudentModal').modal('hide');
					   	   modal.launchAlert("Success","Student Added! Page will refresh in soon");
					   	   setTimeout(function(){
					   		   location.reload();
					   	   },2*1000);		   
					      }else{
					   		   $('div#addStudentModal .add').removeClass('hide');
					   		   $('div#addStudentModal .error').show();
					   	   	if(!resultJson.message){
					   		   $('div#addStudentModal .error').html('<strong>Error!</strong> Unable to add');
					   	   	}else{
					   	   		$('div#addStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
					   	   	}
					      	}
				   	},
				   error:function(e){
					   $('div#addStudentModal .progress').addClass('hide');
						$('div#addStudentModal .add').removeClass('hide');
						$('div#addStudentModal .error').show();
						var resultJson = JSON.parse(e);
						if(!resultJson.message){
							   $('div#addStudentModal .error').html('<strong>Error!</strong> Unable to update');
						   	}else{
						   		$('div#addStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
						   	}
				   }
				   
			});
			}	
			$(".chkBatch:checked").removeAttr('checked');
		});
		
		$('div#modifyStudentModal').on('click','button#btn-update',function(){
			var studentId=document.getElementsByName("radioStudent")[0].value;
			batchIds="";
			$('div#modifyStudentModal .error').html('');
			$('div#modifyStudentModal .error').hide();
			getSelectedBatchesForStudent();
			var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
			if(!batchIds || batchIds.trim()==""){
				$('div#modifyStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Batch name cannot be blank');
				$('div#modifyStudentModal .error').show();
			}else{
				
				$.ajax({
					 url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "updateStudent",
							 regId:'',
							 batchIds:batchIds,
							 studentId:studentId
					   		},
					   type:"POST",
					   success:function(data){
						   $('div#modifyStudentModal .progress').addClass('hide');
							var resultJson = JSON.parse(data);
							var pagenumber=resultJson.pagenumber;
							var batchID=resultJson.batchID;
							   if(resultJson.status != 'error'){
								   $('div#modifyStudentModal').modal('hide');
								   $('#studentDetailTable').remove(); 
								   if(batchID!=null){
								   $('#batchselected').val(batchID);
								   getstudentsrelatedtobatch(pagenumber);
								   }
								   modal.launchAlert("Success","Student Updated successfully!");
								   /* modal.launchAlert("Success","Student Updated! Page will refresh in soon");
								   setTimeout(function(){
									   location.reload();
								   },2*1000); */		   
							   }else{
									   $('div#modifyStudentModal .add').removeClass('hide');
									   $('div#modifyStudentModal .error').show();
								   	if(!resultJson.message){
									   $('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
								   	}else{
								   		$('div#modifyStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
								   	}
							   	}
						  // successCallbackStudentModify(data);
						   //var resultJson = JSON.parse(data);
						   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
							//modal.launchAlert("Success","Student is updated with Student id ="+studentId+"!");
							/* setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   	
					   	},
					   error:function(data){
						   /* $('div#modifyStudentModal .progress').addClass('hide');
							$('div#modifyStudentModal .add').removeClass('hide');
							$('div#modifyStudentModal .error').show();
							$('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
						   modal.launchAlert("Error","Error occured while updating the student!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   // errorCallbackStudentModify(data);
						   $('div#modifyStudentModal .progress').addClass('hide');
							$('div#modifyStudentModal .add').removeClass('hide');
							$('div#modifyStudentModal .error').show();
							var resultJson = JSON.parse(data);
							
							if(!resultJson.message){
								   $('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
							   	}else{
							   		$('div#modifyStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
							   	}
					   }
				});
				$('div#modifyStudentModal .progress').removeClass('hide');
				$('.add').addClass('hide');
			}
			$(".chkBatch:checked").removeAttr('checked');
		});

		$('div#deleteStudentModal').on('click','button#btn-delete',function(){
			var studentId=document.getElementsByName("radioStudent")[0].value;
			$('div#deleteStudentModal .error').html('');
			$('div#deleteStudentModal .error').hide();
			$('div#deleteStudentModal .progress').removeClass('hide');
			$('.add').addClass('hide');
					$.ajax({
					 url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "deleteStudent",
							 regId:'',
							 deleteStudentId:studentId
					   		},
					   type:"POST",
					   success:function(e){
						 /*   var resultJson = JSON.parse(data);
						   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
							modal.launchAlert("Success", "Student with Student id ="+studentId+" is deleted from class!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   	//successCallbackStudentDelete(e);  
						   $('div#deleteStudentModal .progress').addClass('hide');
							var resultJson = JSON.parse(e);
							   if(resultJson.status != 'error'){
								   $('div#deleteStudentModal').modal('hide');
								   modal.launchAlert("Success","Student Deleted! Page will refresh in soon");
								   setTimeout(function(){
									   location.reload();
								   },2*1000);		   
							   }else{
									   $('div#deleteStudentModal .add').removeClass('hide');
									   $('div#deleteStudentModal .error').show();
								   	if(!resultJson.message){
									   $('div#deleteStudentModal .error').html('<strong>Error!</strong> Unable to delete');
								   	}else{
								   		$('div#deleteStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
								   	}
							   	}
					   	},
					   error:function(e){
						  /*  modal.launchAlert("Error","Error occured while deleting student!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   	//errorCallbackStudentDelete(e);
						   $('div#deleteStudentModal .progress').addClass('hide');
							$('div#deleteStudentModal .add').removeClass('hide');
							$('div#deleteStudentModal .error').show();
							var resultJson = JSON.parse(e);
							if(!resultJson.message){
								   $('div#deleteStudentModal .error').html('<strong>Error!</strong> Unable to delete');
							   	}else{
							   		$('div#deleteStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
							   	}
					   }
				});
				
				
			
		});

		$('div#deleteSelectedStudentModal').on('click','button#btn-deleteMultiple',function(){
			selectedStudentIds="";
			getSelectedStudentsToDelete();
			if(!selectedStudentIds || selectedStudentIds.trim()==""||selectedStudentIds.lenght==0 ){
				 modal.launchAlert("Error","Error!</strong> Select atleast one student to delete.");		
			}
			$('div#deleteSelectedStudentModal .error').html('');
			$('div#deleteSelectedStudentModal .error').hide();
			$('div#deleteSelectedStudentModal .progress').removeClass('hide');
			$('.add').addClass('hide');
					$.ajax({
					 url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "deleteMultipleStudents",
							 regId:'',
							 deleteStudentId:studentId
					   		},
					   type:"POST",
					   success:function(e){
						 /*   var resultJson = JSON.parse(data);
						   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
							modal.launchAlert("Success", "Student with Student id ="+studentId+" is deleted from class!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   	successCallbackStudentDelete(e);  	
					   	},
					   error:function(e){
						  /*  modal.launchAlert("Error","Error occured while deleting student!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   	errorCallbackStudentDelete(e);
					   }
				});
				
				$(".chkStudent:checked").removeAttr('checked');
			
		});
		
		$("#batchselected").change(function(){
			getstudentsrelatedtobatch(1);
		});
	});

	function errorCallbackStudent(e){
		$('div#addStudentModal .progress').addClass('hide');
		$('div#addStudentModal .add').removeClass('hide');
		$('div#addStudentModal .error').show();
			$('div#addStudentModal .error').html('<strong>Error!</strong> Unable to add');
	}

	function successCallbackStudent(e){
	$('div#addStudentModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addStudentModal').modal('hide');
		   modal.launchAlert("Success","Student Added! Page will refresh in soon");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);		   
	   }else{
			   $('div#addStudentModal .add').removeClass('hide');
			   $('div#addStudentModal .error').show();
		   	if(!resultJson.message){
			   $('div#addStudentModal .error').html('<strong>Error!</strong> Unable to add');
		   	}else{
		   		$('div#addStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	   	}
	}


	function errorCallbackStudentModify(e){
		$('div#modifyStudentModal .progress').addClass('hide');
		$('div#modifyStudentModal .add').removeClass('hide');
		$('div#modifyStudentModal .error').show();
		var resultJson = JSON.parse(e);
		if(!resultJson.message){
			   $('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
		   	}else{
		   		$('div#modifyStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	}

	function successCallbackStudentModify(e){
	$('div#modifyStudentModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#modifyStudentModal').modal('hide');
		   modal.launchAlert("Success","Student Updated! Page will refresh in soon");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);		   
	   }else{
			   $('div#modifyStudentModal .add').removeClass('hide');
			   $('div#modifyStudentModal .error').show();
		   	if(!resultJson.message){
			   $('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
		   	}else{
		   		$('div#modifyStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	   	}
	}

	function errorCallbackStudentDelete(e){
		$('div#deleteStudentModal .progress').addClass('hide');
		$('div#deleteStudentModal .add').removeClass('hide');
		$('div#deleteStudentModal .error').show();
		var resultJson = JSON.parse(e);
		if(!resultJson.message){
			   $('div#deleteStudentModal .error').html('<strong>Error!</strong> Unable to delete');
		   	}else{
		   		$('div#deleteStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	}

	function successCallbackStudentDelete(e){
	$('div#deleteStudentModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#deleteStudentModal').modal('hide');
		   modal.launchAlert("Success","Student Deleted! Page will refresh in soon");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);		   
	   }else{
			   $('div#deleteStudentModal .add').removeClass('hide');
			   $('div#deleteStudentModal .error').show();
		   	if(!resultJson.message){
			   $('div#deleteStudentModal .error').html('<strong>Error!</strong> Unable to delete');
		   	}else{
		   		$('div#deleteStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	   	}
	}
	
	function getBatchesOfDivision(){
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = $('div#addStudentModal').find('#divisionName').val();

		if(!divisionId || divisionId.trim()=="" || divisionId == -1){
			$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please select a division');
			$('div#addStudentModal .error').show();
		}else{		
		  $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchBatchesForDivision",
				 regId:'',
				 divisionId:divisionId,						 
		   		},
		   type:"POST",
		   success:function(e){		 
			   
			    var resultJson = JSON.parse(e);
			    
			      if(resultJson.status != 'error'){
			   	  	var batchnames=resultJson.batchNames.split(',');
			   		var batchids=resultJson.batchIds.split(',');
			   		var i=0;
			   		while(i<batchnames.length){
				   		addCheckbox(batchnames[i],batchids[i]);
			   			i++;
					   }
				   } else{
					   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> '+resultJson.message+' <a href="managebatch">Click here</a> to add bacth');
						$('div#addStudentModal .error').show();
				}
		   	},
		   error:function(e){
			   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
				$('div#addStudentModal .error').show();
		   }
		   
	});
		
	}
	}

	function addCheckbox(batchname,batchid) {
		   var container =$('#checkboxes');
		   var inputGroupDiv ='<div class="input-group" id="'+batchid+'"><div>';
		   var inputGroupSpan = '<span class="input-group-addon" id="'+batchid+'"></span>';
		   var label = '<input type="text" class="form-control" disabled="disabled">';
		   container.append(inputGroupDiv);
		   container.find("div#"+batchid).html(inputGroupSpan);
		   var inputGroupSpan = container.find("div#"+batchid).find("span");
		   $('<input />', { type: 'checkbox', id: batchid, value: batchid, class: "chkBatch" }).appendTo(inputGroupSpan);
		   var inputGroupLable = container.find("div#"+batchid)
		   $('<input />', { type: 'text', value: batchname,disabled:'disabled',class:'form-control' }).appendTo(inputGroupLable);
		}
</script>
<h3><font face="cursive">Manage Student</font></h3>
<hr>
<div class="modal fade" id="addStudentModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
          <strong>Add Student</strong>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger"></div>
			<div class="form-group">
				<s:set value="${requestScope.divisionNames}" var="divisionNames"></s:set>
				<s:set value="${requestScope.divisionStream}" var="divisionStream"></s:set>
				<s:set value="${requestScope.divisionId}" var="divisionId"></s:set>
				<s:set value="${requestScope.divisions}" var="divisions"></s:set>
				<s:choose>
				<s:when test="${divisionSize gt 0}">
				Student Login Name <input type="text" class="form-control" id="studentLoginName"/> 
				<br>
				<select class="btn btn-default" name="divisionName" id="divisionName">
					
						<option value="-1">Select Division</option>
						<s:forEach items="${divisions}" var="division">
							<option value='<s:out value="${division.divId}"></s:out>'><s:out value="${division.divisionName}"></s:out> &nbsp; <s:out value="${division.stream}"></s:out></option>
						</s:forEach>
					</select>
				<button type="button" class="btn btn-primary btn-getBatchesForStudent" id="getBatchesForStudent" onclick="getBatchesOfDivision()">Get available batches</button>
				</s:when>
				<s:otherwise>
					<div style="color: red;">No Divisions <a href="addsubject">Click Here</a> to add division</div>	
				</s:otherwise>
				</s:choose>
				<br>
				<div id="checkboxes">
				
				</div>
														
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>
					<div class='input-group date' id='fromDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
					
					<div class="col-sm-6">
					<label for="">End Time</label>
					<div class='input-group date' id='toDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
					
					
				</div>
				</div>
				</div>				
			</div>				
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
			<div class="add">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-addStudent" id="btn-addStudent">Add</button>
	        </div>
	     
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>
	
		<div class="container bs-callout bs-callout-danger" style="margin-bottom: 10px;">
		<div class="row">
			<div class="col-md-4">
				<button type="button" class="btn btn-info" data-target="#addStudentModal" data-toggle="modal"><i class="glyphicon glyphicon-user"></i>&nbsp;Add Student</button>
			</div>
			
			<div class="col-md-4">
			Search Student By Login ID
			<div class="input-group">
				<input type="text" class="form-control" id="studentLoginNameSearch" placeholder="Student Login Id" size="20"/>
				<span class="input-group-btn">
					<button type="button" class="btn btn-info" id="searchStudent" onclick="searchStudent()" ><i class="glyphicon glyphicon-search"></i>&nbsp;Search</button>
				</span>
			</div>
			</div>
			
			<div class="col-md-4">
				<%List<Batch> batches=(List<Batch>)request.getAttribute("batches"); %>
				<%if(null != batches && batches.size()!=0) {%>
					Search Student By Batch
					<select class="form-control btn btn-default" id="batchselected" >
					<option value="-1">Select Batch</option>
					<%for(int counter=0;counter<batches.size();counter++){ %>
					<option value="<%=batches.get(counter).getBatch_id() %>"><%=batches.get(counter).getBatch_name() %></option>
					<%} %>
					</select>
				<%}else{ %>
					<div style="color:red">
						No batches are added for this class <br>
						<a href="managebatch">Click here</a> to add batch
					</div>
				<%} %>
				
			</div>
		</div>
		</div>
		
	<div class="btn-group btn-group-sm">
			<div id="noStudentError" class="alert alert-danger" style="display:none;">
				No student for this batch
			</div>
			<div>
			<table id="students" class="table table-bordered searchTable" style="background-color: white;display: none;">
			<thead>
			<tr style="background-color: rgb(0, 148, 255);">
			<th>Student ID</th>
			<th>Student Name</th>
			<th>Edit</th>
			</tr>
			</thead>
			</table>
			<ul class="pagination" id="pagination">	</ul>
			
			</div>
		
			<%StudentDetails studentSearch=(StudentDetails)request.getSession().getAttribute("studentSearchResult");
			String batchID=(String)request.getSession().getAttribute("batchID");
			String pagenumber=(String)request.getSession().getAttribute("pagenumber");
			if(studentSearch!=null){
				//System.out.println("studentSearch : "+studentSearch.getStudentUserBean().getLoginName());
			%>
			
			<table class="hidden-xs hidden-sm table table-bordered table-hover" id="studentDetailTable" style="background-color: white;" border="1">
				<thead>
					<tr style="background-color: rgb(0, 148, 255);">
						
						<th>Student Login Name</th>
						<th>Student Name</th>
						<th>Division</th>
						<th>Batches</th>
						<th></th>
						<th></th>
						<th></th>
					 </tr>
				</thead>
				<tbody>	
					<tr>
						<INPUT TYPE="hidden" NAME="radioStudent" VALUE="<%=studentSearch.getStudentId() %>" CHECKED>
						<td><%=studentSearch.getStudentUserBean().getLoginName() %></td>
						<td><%= studentSearch.getStudentUserBean().getFname()%> <%= studentSearch.getStudentUserBean().getLname()%></td>
						<td><%= studentSearch.getDivision().getDivisionName()%>  </td>
						<td><%= request.getSession().getAttribute("studentBatch")%></td>
						<td><button type="button" class="btn btn-info" data-target="#modifyStudentModal" data-toggle="modal">Modify Student Batch</button></td>
						<td><button type="button" class="btn btn-info" data-target="#deleteStudentModal" data-toggle="modal">Delete Student</button></td>
						<td><a onclick="canceledit(<%=batchID%>,<%=pagenumber%>)"><button type="button" class="btn btn-info">Cancel</button></a></td>
					</tr>
				</tbody>
			</table>

			<table class="visible-xs visible-sm table table-bordered table-hover" id="studentDetailTable" style="background-color: white;" border="1">
					<tr>
						<td>Student Login Name</td>
						<td><%=studentSearch.getStudentUserBean().getLoginName() %></td>
					</tr>
					<tr>		
						<td>Student Name</td>
						<td><%= studentSearch.getStudentUserBean().getFname()%> <%= studentSearch.getStudentUserBean().getLname()%></td>
					</tr>
					<tr>		
						<td>Division</td>
						<td><%= studentSearch.getDivision().getDivisionName()%>  </td>
					</tr>
					<tr>		
						<td>Batches</td>
						<td><%= request.getSession().getAttribute("studentBatch")%></td>
					</tr>		
					<tr>
						<td></td>
						<td>
							<button type="button" class="btn btn-info" data-target="#modifyStudentModal" data-toggle="modal">Modify Student Batch</button>
							<button type="button" class="btn btn-info" data-target="#deleteStudentModal" data-toggle="modal">Delete Student</button>
						</td>
					</tr>
			</table>			
			<%
			request.getSession().setAttribute("studentSearchResult",null);	
			}
			%>
			<!--<br><br><br>
				<button type="button" class="btn btn-info" data-target="#addStudentModal" data-toggle="modal">Add Student</button>
			 	<button type="button" class="btn btn-info" data-target="#deleteSelectedStudentModal" data-toggle="modal">Delete Student</button>
			<br><br> -->
		</div>
		<br/><br/>
		<div class="panel-group hide" id="accordion" >
			<table class="table table-bordered table-hover " id="" style="background-color: white;" border="1">
				<thead>
					<tr style="background-color: rgb(0, 148, 255);">
						<!--<td> <input type="checkbox" class="chk" name="selectAll" id="selectAll" data-label="selectAll">Select All</<input></td>  -->
						<th>Student Login Name</th>
						<th>Student Name</th>
						<th>Division</th>
					</tr>
				</thead>
				<%
					int i = 0;
					if(null != list){
					 Iterator iteratorList = list.iterator();
					 while(iteratorList.hasNext()){
					 StudentDetails student = (StudentDetails)iteratorList.next();
					 //String timmingsTitle = "Start time :"+ batchDataClass.getTimmings().getStartTimming()+"<br>End Time :"+batchDataClass.getTimmings().getEndTimming(); 
					 String timmingsTitle = "Start time :";
				%>
				<tbody>	
					<tr>
						<!--<td>  <input type="checkbox" class="chkStudent" name="studentname" id="studentname" data-label="<%=student.getStudentUserBean().getLoginName() %>" value="<%=student.getStudentUserBean().getLoginName() %>"/></td>  -->
						<td><%=student.getStudentUserBean().getLoginName()%></td>
						<td> <%= student.getStudentUserBean().getFname()%> </td>
						<td> <%= student.getDivision().getDivisionName()%> </td>
					</tr>
				<%
					 i++;
					  } 		
					}
				%>
				</tbody>
			</table>
		</div>
		
