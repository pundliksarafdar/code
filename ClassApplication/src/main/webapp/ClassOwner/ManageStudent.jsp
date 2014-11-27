<meta http-equiv="cache-control" content="max-age=0" />
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>

<html>
<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.STUDENT_LIST); %>
<script>
var batchIds;
var selectedStudentIds;

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
				modal.launchAlert("Success","Found "+firstname+" "+lastname+" with Student id ="+studentId+"! Page will refresh in 2 sec");
						   setTimeout(function(){
							   location.reload();
						   },2*1000);
			   }else{
				 	 if(!resultJson.message){
				 		modal.launchAlert("Error","Error while searching student!");
			   	   	}else{
			   	   		modal.launchAlert("Error","Error while searching student!"+resultJson.message);
			   	   		
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
			divisionName = $('div#addStudentModal').find('#divisionName').val();

			getSelectedBatchesForStudent();		
			if(!studentLgName || studentLgName.trim()==""){
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Student login name cannot be blank');
				$('div#addStudentModal .error').show();
			}else if($("#studentLoginName").val().length<5 || !$("#studentLoginName").val().match(regloginname))
			{
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Invalid Student login');
				$('div#addStudentModal .error').show();
			}else if(!divisionName || divisionName.trim()==""){
				$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Division name cannot be blank');
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
						 divisionName:divisionName,				 
				   		},
				   type:"POST",
				   success:function(e){
					   $('div#addStudentModal .progress').addClass('hide');
					   var resultJson = JSON.parse(e);
					      if(resultJson.status != 'error'){
					   	   $('div#addStudentModal').modal('hide');
					   	   modal.launchAlert("Success","Student Added! Page will refresh in 2 sec");
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
							   if(resultJson.status != 'error'){
								   $('div#modifyStudentModal').modal('hide');
								   modal.launchAlert("Success","Student Updated! Page will refresh in 2 sec");
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
								   modal.launchAlert("Success","Student Deleted! Page will refresh in 2 sec");
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
		   modal.launchAlert("Success","Student Added! Page will refresh in 2 sec");
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
		   modal.launchAlert("Success","Student Updated! Page will refresh in 2 sec");
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
		   modal.launchAlert("Success","Student Deleted! Page will refresh in 2 sec");
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
</script>
	<body>
		<br/><br/>
		<div class="btn-group btn-group-sm">
			<table class="table">
				<thead>
					<tr>
						<td><button type="button" class="btn btn-info" data-target="#addStudentModal" data-toggle="modal">Add Student</button></td>
				 		<!--<td><button type="button" class="btn btn-info" data-target="#deleteSelectedStudentModal" data-toggle="modal">Delete Student</button></td>  -->
						<td><input type="text" class="form-control" id="studentLoginNameSearch" size="20"/></td>		
						<td><button type="button" class="btn btn-info" id="searchStudent" onclick="searchStudent()" >Search Student</button></td>
					</tr>
				</thead>
			</table>
		
			<%StudentDetails studentSearch=(StudentDetails)request.getSession().getAttribute("studentSearchResult");
			if(studentSearch!=null){
				//System.out.println("studentSearch : "+studentSearch.getStudentUserBean().getLoginName());
			%>
			<table class="table table-bordered table-hover" style="background-color: white;" border="1">
				<thead>
					<tr>
						<th></th>
						<th>Student Login Name</th>
						<th>Student Name</th>
						<th>Division</th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tbody>	
					<tr>
						<td><INPUT TYPE="radio" NAME="radioStudent" VALUE="<%=studentSearch.getStudentId() %>" CHECKED></td>
						<td><%=studentSearch.getStudentUserBean().getLoginName() %></td>
						<td><%= studentSearch.getStudentUserBean().getFname()%> <%= studentSearch.getStudentUserBean().getLname()%></td>
						<td><%= studentSearch.getDivision().getDivisionName()%>  </td>
						<td><button type="button" class="btn btn-info" data-target="#modifyStudentModal" data-toggle="modal">Modify Student Batch</button></td>
						<td><button type="button" class="btn btn-info" data-target="#deleteStudentModal" data-toggle="modal">Delete Student</button></td>
					</tr>
				</tbody>
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
		<div class="panel-group" id="accordion">
			<table class="table table-bordered table-hover" style="background-color: white;" border="1">
				<thead>
					<tr>
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
	</body>
</html>