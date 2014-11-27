
<%@page import="com.classapp.db.batch.BatchDetails"%>
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>

<html>
<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.BATCHES_LIST); %>
<script>
var subjectIds;
function getSelectedSubjectsForBatch(){
	var subjects;
	subjects=$(".chkSubjectBatch:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<subjects.size()){
		if(i==0){
			subjectIds=subjectIds+subjects[0]+"";
		}else{
			subjectIds=subjectIds+","+subjects[i];
		}
		i++;
	}
}

var subjectsname;
function getSelectedCheckbox()
{
	var subjects;
	subjects=$(".chkSubject:checked").map(function() {
		return this.value;
	});
var i=0;
while(i<subjects.size())
	{
	if(i==0)
		{
	subjectsname=subjectsname+subjects[0]+"";
		}else{
			subjectsname=subjectsname+","+subjects[i];
		}
	i++;
	}

}

function searchBatch() {
	var batchName=document.getElementById("batchNameSearch").value;
	var regbatchname=/^[a-zA-z0-9 ]+$/;
	if(!batchName || batchName.trim()==""||batchName.lenght==0 ){
		 modal.launchAlert("Error","Error!</strong> Batch name cannot be blank");		
	}else if(!$("#batchNameSearch").val().match(regbatchname)){
		 modal.launchAlert("Error","Error!</strong>Invalid Batchname");
	}
	else{
	$.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 methodToCall: "searchBatch",
		    		 batchName:batchName
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);   
			   // var firstname= resultJson.studentFname;
			  // var lastname= resultJson.studentLname;
			  // var studentId= resultJson.studentId;
			   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
			    if(resultJson.status != 'error'){
				modal.launchAlert("Success","Found Batch! Page will refresh in 2 sec");
						   setTimeout(function(){
							   location.reload();
						   },2*1000);
			    }else{
			    	 modal.launchAlert("Error","Batch with batch name : "+batchName+" not found!");
					   	setTimeout(function(){
					   		location.reload();
					   	},1000*3);
					}		   
		   	},
		   error:function(data){
			   modal.launchAlert("Error","Batch with batch name : "+batchName+" not found!");
			   	setTimeout(function(){
			   		location.reload();
			   	},1000*3);
		   }
			   
	});
	}
}

$(document).ready(function(){
	$('div#addBatchModal .error').hide();
	$('div#addBatchModal').on('click','button#btn-addBatch',function(){
		var batchName = "";
		subjectsname="";
		$('div#addBatchModal .error').html('');
		$('div#addBatchModal .error').hide();
		var regId;
		var regbatchname=/^[a-zA-z0-9 ]+$/;
		batchName = $('div#addBatchModal').find('#batchName').val();
		getSelectedCheckbox();
		if(!batchName || batchName.trim()==""){
			$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Batch name cannot be blank');
			$('div#addBatchModal .error').show();
		}else if(!$("#batchName").val().match(regbatchname) || $("#batchName").val().length<1){
			$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Invalid Batch name');
			$('div#addBatchModal .error').show();
	}else if($("#divisionName").val()=='-1'){
		$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please Select Division');
		$('div#addBatchModal .error').show();
	}else if(subjectsname==""){
		$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please Select Atlease One Subject');
		$('div#addBatchModal .error').show();
	}else{
			$('div#addBatchModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			
			$.ajax({
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "addBatch",
						 regId:'',
						 batchName:batchName,
						 divisionName: $('div#addBatchModal').find('#divisionName').val(),
						 subjectname:subjectsname
				   		},
				   type:"POST",
				   success:function(e){
						$('div#addBatchModal .progress').addClass('hide');
						var resultJson = JSON.parse(e);
						   if(resultJson.status != 'error'){
							   $('div#addBatchModal').modal('hide');
							   modal.launchAlert("Success","Batch Added! Page will refresh in 2 sec");
							   setTimeout(function(){
								   location.reload();
							   },2*1000);		   
						   }else{
							   $('div#addBatchModal .add').removeClass('hide');
							   $('div#addBatchModal .error').show();
							   	if(!resultJson.message){
								   $('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
							   	}else{
							   		$('div#addBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
							   	}
						   	}
				   	},
				   error:function(e){
					   $('div#addSubjectModal .progress').addClass('hide');
						$('div#addSubjectModal .add').removeClass('hide');
						$('div#addSubjectModal .error').show();
						$('div#addSubjectModal .error').html('<strong>Error!</strong> Unable to add');

				   }
			});
		}
		$(".chkSubject:checked").removeAttr('checked');
	});
	

	$('div#modifyBatchModal').on('click','button#btn-updateBatch',function(){
		
		var batchId=document.getElementsByName("radioBatch")[0].value;
		
		subjectIds="";
		$('div#modifyBatchModal .error').html('');
		$('div#modifyBatchModal .error').hide();
		getSelectedSubjectsForBatch();
		
		if(!subjectIds || subjectIds.trim()==""){
			$('div#modifyBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Subject name cannot be blank');
			$('div#modifyBatchModal .error').show();
		}else{
			
			$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "updateBatch",
						 regId:'',
						 subIds:subjectIds,
						 batchId:batchId
				   		},
				   type:"POST",
				   success:function(data){					  
					    $('div#modifyBatchModal .progress').addClass('hide');
					   var resultJson = JSON.parse(data);
					      if(resultJson.status != 'error'){
					       $('div#modifyBatchModal').modal('hide');
					   	   modal.launchAlert("Success","Batch Updated! Page will refresh in 2 sec");
					   	   setTimeout(function(){
					   		   location.reload();
					   	   },2*1000);		   
					     }
				   	},
				   error:function(data){
					  /*  modal.launchAlert("Error","Error occured while updating the teacher!");
					   	setTimeout(function(){
					   		location.reload();
					   	},1000*3); */
					   //	errorCallbackTeacherModify(data);
					   $('div#modifyBatchModal .progress').addClass('hide');
						$('div#modifyBatchModal .add').removeClass('hide');
						$('div#modifyBatchModal .error').show();
						var resultJson = JSON.parse(data);
						if(!resultJson.message){
							   $('div#modifyBatchModal .error').html('<strong>Error!</strong> Unable to update');
						   	}else{
						   		$('div#modifyBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
						   	}
				   }
			});
			$('div#modifyBatchModal .progress').removeClass('hide');
			$('.add').addClass('hide');
		}
	$(".chkSubjectBatch:checked").removeAttr('checked');
});

	$('div#deleteBatchModal').on('click','button#btn-deleteBatch',function(){
	
		var batchId=document.getElementsByName("radioBatch")[0].value;
		$('div#deleteBatchModal .error').html('');
		$('div#deleteBatchModal .error').hide();
		
		$('div#deleteBatchModal .progress').removeClass('hide');
		$('.add').addClass('hide');
				$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "deleteBatch",
						 regId:'',
						 batchId:batchId
				   		},
				   type:"POST",
				   success:function(data){
					  /*  var resultJson = JSON.parse(data);
					   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
						modal.launchAlert("Success", "Teacher with teacher id ="+teacherId+" is deleted from class!");
					   	setTimeout(function(){
					   		location.reload();
					   	},1000*3); */
					   //	successCallbackTeacherDelete(data);
					   $('div#deleteBatchModal .progress').addClass('hide');
					   var resultJson = JSON.parse(data);
					      if(resultJson.status != 'error'){
					   	   $('div#deleteBatchModal').modal('hide');
					   	   modal.launchAlert("Success","Batch Deleted! Page will refresh in 2 sec");
					   	   setTimeout(function(){
					   		   location.reload();
					   	   },2*1000);		   
					      }else{
					   		   $('div#deleteBatchModal .add').removeClass('hide');
					   		   $('div#deleteBatchModal .error').show();
					   	   	if(!resultJson.message){
					   		   $('div#deleteBatchModal .error').html('<strong>Error!</strong> Unable to delete');
					   	   	}else{
					   	   		$('div#deleteBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
					   	   	}
					      	}
				   	},
				   error:function(data){
					    $('div#deleteBatchModal .progress').addClass('hide');
						$('div#deleteBatchModal .add').removeClass('hide');
						$('div#deleteBatchModal .error').show();
						var resultJson = JSON.parse(e);
						if(!resultJson.message){
							   $('div#deleteBatchModal .error').html('<strong>Error!</strong> Unable to delete');
						   	}else{
						   		$('div#deleteBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
						   	}
				   }
			});			
	});

});

</script>
<body>
	<br/><br/>
	<div class="btn-group btn-group-sm">
		<table class="table">
			<thead>
				<tr>
					<td><button type="button" class="btn btn-info" data-target="#addBatchModal" data-toggle="modal">Add Batch</button></td>
					<!--<td><button type="button" class="btn btn-info" data-target="#deleteSelectedBatchModal" data-toggle="modal">Delete batch/es</button></td>-->
					<td></td>
					<td></td>
					<td><input type="text" class="form-control" id="batchNameSearch" size="20"/></td>			
					<td><button type="button" class="btn btn-info" id="searchBatch" onclick="searchBatch()" >Search Batch</button></td>
				</tr>
			</thead>			
		</table>
	
	<%BatchDetails batchSearch=(BatchDetails)request.getSession().getAttribute("batchSearchResult");
	if(batchSearch!=null){
		//System.out.println("studentSearch : "+studentSearch.getStudentUserBean().getLoginName());
	%>
		<table class="table table-bordered table-hover" style="background-color: white;" border="1">
			<thead>
				<tr>
					<th></th>
					<th>Batch Name</th>
					<th>Division</th>
					<th>Stream</th>
					<th>Subjects</th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>	
				<tr>
					<td> <INPUT TYPE="radio" NAME="radioBatch" VALUE="<%=batchSearch.getBatch().getBatch_id() %>" CHECKED></td>
					<td><%=batchSearch.getBatch().getBatch_name() %></td>
					<td> <%= batchSearch.getDivision().getDivisionName()%> </td>
					<td> <%= batchSearch.getDivision().getStream()%> </td>
					<td> <%= batchSearch.getSubjectNames()%>  </td>
					<td><button type="button" class="btn btn-info" data-target="#modifyBatchModal" data-toggle="modal">Modify Batch Subjects</button></td>
					<td> <button type="button" class="btn btn-info" data-target="#deleteBatchModal" data-toggle="modal">Delete Batch</button></td>
				</tr>
			</tbody>
		</table>
	
	<%
	request.getSession().setAttribute("batchSearchResult",null);
	}
	%>
	</div>	
	<!--  <br/><br/><br/>
	<div>
		<button type="button" class="btn btn-info" data-target="#addBatchModal" data-toggle="modal">Add Batch</button>
		<button type="button" class="btn btn-info" data-target="#deleteSelectedBatchModal" data-toggle="modal">Delete batch/es</button>
	</div> -->
	<br/><br/>
	<div class="panel-group" id="accordion">
		<table class="table table-bordered table-hover" style="background-color: white;" border="1">
			<thead>
				<tr>
					<!--<td> <input type="checkbox" class="chk" name="selectAll" id="selectAll" data-label="selectAll">Select All</<input></td>  -->
					<th>Batch Name</th>
					<th>Division</th>
					<th>Stream</th>
					<th>Subjects</th>
				</tr>
			</thead>
		
				 <%
				 int i = 0;
				 if(null != list){
				  Iterator iteratorList = list.iterator();
				  while(iteratorList.hasNext()){
				  BatchDetails batchDetails = (BatchDetails)iteratorList.next();
				  //String timmingsTitle = "Start time :"+ batchDataClass.getTimmings().getStartTimming()+"<br>End Time :"+batchDataClass.getTimmings().getEndTimming(); 
				 		String timmingsTitle = "Start time :";
				 %>
			<tbody>
				  <tr>
					  <!--<td><input type="checkbox" class="chkBatch" name="batchname" id="batchname" data-label="<%=batchDetails.getBatch().getBatch_name() %>" value="<%=batchDetails.getBatch().getBatch_id()%>"/></td>  -->
					  <td><%=batchDetails.getBatch().getBatch_name() %></td>
					  <td> <%= batchDetails.getDivision().getDivisionName()%> </td>
					  <td> <%= batchDetails.getDivision().getStream()%> </td>
					  <td> <%= batchDetails.getSubjectNames()%>  </td>
				  </tr>
				<%
		  			i++;		  
			  		} 		
		 		} %>
		 	</tbody>
		 </table>
	</div>
</body>
</html>