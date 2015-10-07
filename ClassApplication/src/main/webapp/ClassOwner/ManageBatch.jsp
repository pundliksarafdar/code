
<%@page import="com.classapp.db.batch.BatchDetails"%>
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.BATCHES_LIST); %>
<script>
var subjectIds;
var batchID="";
var batchdivisionIDs="";
function canceledit(){
	 $("#modifyBatchModalbody").empty();
	$("#batchtomodify").hide();
	$("#basebatchtable").show();
	$("#paginateform").show();
}
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


function searchbatchthroughtable(batchName) {
	$.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 methodToCall: "searchBatch",
		    		 batchName:batchName
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);   
			    var batchId= resultJson.batchId;
			   var batchName= resultJson.batchName;
			   var batchdivisionname= resultJson.batchdivisionname;
			   var batchdivisionID= resultJson.batchdivisionID;
			   var batchdivisionstream= resultJson.batchdivisionstream;
			   var batchsubjectIds= resultJson.batchsubjectIds.split(",");
			   var allsubjectname= resultJson.allsubjectname.split(",");
			   var allsubjectIds= resultJson.allsubjectIds.split(",");
			   var batchsubjectnames= resultJson.batchsubjectnames;
			   $("#batchtomodifybody").empty();
			   $("#batchtomodify").hide();
			  $("#batchtomodifybody").append("<tr><td>"+batchName+"</td><td>"+batchdivisionname+"</td><td>"+batchdivisionstream+"</td><td>"+batchsubjectnames+"</td>"
			    +"<td><button type='button' class='btn btn-info' data-target='#modifyBatchModal' data-toggle='modal'>Modify Batch Subjects</button></td>"
			    +"<td> <button type='button' class='btn btn-info' data-target='#deleteBatchModal' data-toggle='modal'>Delete Batch</button></td>"
				+"<td><button type='button' class='btn btn-info' onclick='canceledit()' >Cancel</button></td></tr>");
			  if(allsubjectIds[0]!=""){
				   for(var i=0;i<allsubjectIds.length;i++){
					   var flag=false;
					   for(var j=0;j<batchsubjectIds.length;j++){
						   if(batchsubjectIds[j]==allsubjectIds[i]){
							   flag=true;
						   }
					   }
					   if(flag==true){
						   $("#modifyBatchModalbody").append("<div class='input-group'>"
						  +"<span class='input-group-addon'>"
							+"<input type='checkbox' class='chkSubjectBatch' name='subjectBatchId' id='subjectBatchId'  value='"+allsubjectIds[i]+"' checked/>"
							+"</span>"
							+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
						+"</div>");
					   }else{
						   $("#modifyBatchModalbody").append("<div class='input-group'>"
									  +"<span class='input-group-addon'>"
										+"<input type='checkbox' class='chkSubjectBatch' name='subjectBatchId' id='subjectBatchId'  value='"+allsubjectIds[i]+"'/>"
										+"</span>"
										+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
									+"</div>");
					   }
				   }
			   }
			   batchID=batchId;
			   batchdivisionIDs=batchdivisionID;
			   $("#batchtomodify").show();
			   $("#basebatchtable").hide();
			   $("#paginateform").hide();
			//   alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
			    if(resultJson.status != 'error'){
				//modal.launchAlert("Success","Found Batch! Page will refresh in soon");
				
						/*    setTimeout(function(){
							   location.reload();
						   },2*1000); */
			    }else{
			    	 modal.launchAlert("Error","Batch not found!");
					   	/* setTimeout(function(){
					   		location.reload();
					   	},1000*3); */
					}		   
		   	},
		   error:function(data){
			   modal.launchAlert("Error","Batch not found!");
			   	setTimeout(function(){
			   		location.reload();
			   	},1000*3);
		   }
			   
	});
	
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
			    	 var batchId= resultJson.batchId;
					   var batchName= resultJson.batchName;
					   var batchdivisionname= resultJson.batchdivisionname;
					   var batchdivisionID= resultJson.batchdivisionID;
					   var batchdivisionstream= resultJson.batchdivisionstream;
					   var batchsubjectIds= resultJson.batchsubjectIds.split(",");
					   var allsubjectname= resultJson.allsubjectname.split(",");
					   var allsubjectIds= resultJson.allsubjectIds.split(",");
					   var batchsubjectnames= resultJson.batchsubjectnames;
					   $("#batchtomodifybody").empty();
					   $("#batchtomodify").hide();
					  $("#batchtomodifybody").append("<tr><td>"+batchName+"</td><td>"+batchdivisionname+"</td><td>"+batchdivisionstream+"</td><td>"+batchsubjectnames+"</td>"
					    +"<td><button type='button' class='btn btn-info' data-target='#modifyBatchModal' data-toggle='modal'>Modify Batch Subjects</button></td>"
					    +"<td> <button type='button' class='btn btn-info' data-target='#deleteBatchModal' data-toggle='modal'>Delete Batch</button></td>"
						+"<td><button type='button' class='btn btn-info' onclick='canceledit()' >Cancel</button></td></tr>");
					  if(allsubjectIds[0]!=""){
						   for(var i=0;i<allsubjectIds.length;i++){
							   var flag=false;
							   for(var j=0;j<batchsubjectIds.length;j++){
								   if(batchsubjectIds[j]==allsubjectIds[i]){
									   flag=true;
								   }
							   }
							   if(flag==true){
								   $("#modifyBatchModalbody").append("<div class='input-group'>"
								  +"<span class='input-group-addon'>"
									+"<input type='checkbox' class='chkSubjectBatch' name='subjectBatchId' id='subjectBatchId'  value='"+allsubjectIds[i]+"' checked/>"
									+"</span>"
									+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
								+"</div>");
							   }else{
								   $("#modifyBatchModalbody").append("<div class='input-group'>"
											  +"<span class='input-group-addon'>"
												+"<input type='checkbox' class='chkSubjectBatch' name='subjectBatchId' id='subjectBatchId'  value='"+allsubjectIds[i]+"'/>"
												+"</span>"
												+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
											+"</div>");
							   }
						   }
					   }
					 batchID=batchId;
					   batchdivisionIDs=batchdivisionID;
					   $("#batchtomodify").show();
					   $("#basebatchtable").hide();
					   $("#paginateform").hide();
					   /* setTimeout(function(){
							   location.reload();
						   },2*1000); */
			    }else{
			    	 modal.launchAlert("Error","Batch not found!");
					   	/* setTimeout(function(){
					   		location.reload();
					   	},1000*3); */
					}		   
		   	},
		   error:function(data){
			   modal.launchAlert("Error","Batch not found!");
			   	setTimeout(function(){
			   		location.reload();
			   	},1000*3);
		   }
			   
	});
	}
}

$(document).ready(function(){
	$('div#modifyBatchModal .error').html('');
	$('div#modifyBatchModal .error').hide();
	
	$(".page").on("click",function(e){
		$("form#paginateform #currentPage").val($(this).text());
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".start").on("click",function(e){
		$("form#paginateform #currentPage").val("1");
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$(".end").on("click",function(e){
		$("form#paginateform #currentPage").val($("#totalPages").val());
		$("#paginateform").submit();
		e.preventDefault();
	});
	
	$('div#addBatchModal .error').hide();
	$("#batchName").on("keyup",function(){
		var string = $(this).val();	
		$(this).val(string.charAt(0).toUpperCase() + string.slice(1));
	});
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
		$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please Select Class');
		$('div#addBatchModal .error').show();
	}else if(subjectsname==""){
		$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please Select Atleast One Subject');
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
							   modal.launchAlert("Success","Batch Added! Page will refresh in soon");
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
		 
		var batchId=batchID;
		var batchdivisionid=batchdivisionIDs;
		subjectIds="";
		$('div#modifyBatchModal .error').html('');
		$('div#modifyBatchModal .error').hide();
		getSelectedSubjectsForBatch();
		
		if(!subjectIds || subjectIds.trim()==""){
			$('div#modifyBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Please Select Atleast One Subject');
			$('div#modifyBatchModal .error').show();
		}else{
			
			$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "updateBatch",
						 regId:'',
						 subIds:subjectIds,
						 batchId:batchId,
						 batchdivisionid:batchdivisionid
				   		},
				   type:"POST",
				   success:function(data){					  
					    $('div#modifyBatchModal .progress').addClass('hide');
					   var resultJson = JSON.parse(data);
					      if(resultJson.status != 'error'){
					       $('div#modifyBatchModal').modal('hide');
					   	   modal.launchAlert("Success","Batch Updated! Page will refresh in soon");
					   	 $("#paginateform").submit();	   
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
		
		var batchId=batchID;
		var batchdivisionid=batchdivisionIDs;
		$('div#deleteBatchModal .error').html('');
		$('div#deleteBatchModal .error').hide();
		
		$('div#deleteBatchModal .progress').removeClass('hide');
		$('.add').addClass('hide');
				$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "deleteBatch",
						 regId:'',
						 batchId:batchId,
						 batchdivisionid:batchdivisionid
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
					   	   modal.launchAlert("Success","Batch Deleted! Page will refresh in soon");
					   	 $("#paginateform").submit();		   
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
	<div class="">
		<div class="container bs-callout bs-callout-danger white-back"  style="margin-bottom: 10px;">
			<div align="center" style="font-size: larger;margin-bottom: 15px"><u>Manage Batch</u></div>
			<div class="row">
			<div class="col-md-4">
				<button type="button" class="btn btn-info" data-target="#addBatchModal" data-toggle="modal"><i class="glyphicon glyphicon-plus"></i>&nbsp;Add Batch</button>
			</div>
		
			<div class="col-md-4">
			<div class="input-group">
				<input type="text" class="form-control" id="batchNameSearch" size="20"/>
				<span class="input-group-btn">
					<button type="button" class="btn btn-info" id="searchBatch" onclick="searchBatch()" ><i class="glyphicon glyphicon-search"></i>&nbsp;Search Batch</button>
				</span>
			</div>
			</div>
			</div>		
		</div>
	<%BatchDetails batchSearch=(BatchDetails)request.getSession().getAttribute("batchSearchResult");
	/* if(batchSearch!=null){
	 */	//AppLogger.logger("studentSearch : "+studentSearch.getStudentUserBean().getLoginName());
	%>
		<table class="table table-bordered table-hover" style="background-color: white;display:none" border="1" id="batchtomodify">
			<thead>
				<tr style="background-color: rgb(0, 148, 255);">
					<!-- <th></th> -->
					<th>Batch Name</th>
					<th>Division</th>
					<th>Stream</th>
					<th>Subjects</th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody id="batchtomodifybody">	
				<%-- <tr>
					<td> <INPUT TYPE="radio" NAME="radioBatch" VALUE="<%=batchSearch.getBatch().getBatch_id() %>" CHECKED>
						 <input type="hidden" id="batchdivisionid" VALUE="<%=batchSearch.getBatch().getDiv_id() %>"> 
					</td>
					<td><%=batchSearch.getBatch().getBatch_name() %></td>
					<td> <%= batchSearch.getDivision().getDivisionName()%> </td>
					<td> <%= batchSearch.getDivision().getStream()%> </td>
					<td> <%= batchSearch.getSubjectNames()%>  </td>
					<td><button type="button" class="btn btn-info" data-target="#modifyBatchModal" data-toggle="modal">Modify Batch Subjects</button></td>
					<td> <button type="button" class="btn btn-info" data-target="#deleteBatchModal" data-toggle="modal">Delete Batch</button></td>
					<td><button type="button" class="btn btn-info" onclick="canceledit()" >Cancel</button></td>
				</tr> --%>
			</tbody>
		</table>
	
	<%-- <%
	request.getSession().setAttribute("batchSearchResult",null);
	}
	%> --%>
	</div>	
	<!--  <br/><br/><br/>
	<div>
		<button type="button" class="btn btn-info" data-target="#addBatchModal" data-toggle="modal">Add Batch</button>
		<button type="button" class="btn btn-info" data-target="#deleteSelectedBatchModal" data-toggle="modal">Delete batch/es</button>
	</div> -->
	<br/><br/>
	
	<s:choose>
	<s:when test="${batchSize gt 0}">
	<div class="panel-group" id="accordion">
		<table class="table table-bordered table-hover" style="background-color: white;" border="1" id="basebatchtable">
			<thead>
				<tr style="background-color: rgb(0, 148, 255);">
					<!--<td> <input type="checkbox" class="chk" name="selectAll" id="selectAll" data-label="selectAll">Select All</<input></td>  -->
					<th>Batch Name</th>
					<th>Division</th>
					<th>Stream</th>
					<th>Subjects</th>
					<th>Edit</th>
				</tr>
			</thead>
		
				 <%
				 int endIndex=(Integer)request.getAttribute("endIndex");
				 int currentPage=(Integer)request.getAttribute("currentPage");
				 int startIndex=(Integer)request.getAttribute("startIndex");
				 int i = startIndex;
				 if(null != list){
				//  Iterator iteratorList = list.iterator();
				  while(i<endIndex){
				  BatchDetails batchDetails = (BatchDetails)list.get(i);
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
					  <td><button type="button" class="btn btn-info" onclick="searchbatchthroughtable('<%=batchDetails.getBatch().getBatch_name()%>')" >Edit</button></td>
				  </tr>
				<%
		  			i++;		  
			  		} 		
		 		} %>
		 	</tbody>
		 </table>
	</div>
	<div>
 <form action="managebatch" id="paginateform">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <ul class="pagination">
  <li><a class="start" >&laquo;</a></li>
  <c:forEach var="item" begin="1" end="${totalPages}">
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
	</s:when>
	<s:otherwise>
		<span class="alert alert-info">No Batches added yet</span>
	</s:otherwise>
	</s:choose>
</body>
</html>