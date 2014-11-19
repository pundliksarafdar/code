
<%@page import="com.classapp.db.Teacher.TeacherDetails"%>
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<html>
<script type="text/javascript" src="js/ManageTeacher.js"></script>
<script type="text/javascript" src="js/AddSubject.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.TEACHER_LIST); %>
<script>
var subjectIds;
function getSelectedSubjectsForAddTeacher(){
	var subjects=$(".chkSubjectAddTeacher:checked").map(function(){
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

function getSelectedSubjectsForTeacher(){
	var subjects=$(".chkSubjectTeacher:checked").map(function(){
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

function searchTeacher() {
	var teacherLoginName=document.getElementById("teacherLoginNameSearch").value;
	if(!teacherLoginName || teacherLoginName.trim()==""|| teacherLoginName.trim().length==0){
		 modal.launchAlert("Error","Error!</strong> Teacher login name cannot be blank");		
	}else{
	$.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 methodToCall: "searchTeacher",
		    	 teacherLgName:teacherLoginName
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);

			   if(resultJson.status != 'error'){
				   var firstname= resultJson.teacherFname;
				   var lastname= resultJson.teacherLname;
				   var teacherId= resultJson.teacherId;
				   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
				
					modal.launchAlert("Success","Found "+firstname+" "+lastname+" with teacher id ="+teacherId+"! Page will refresh in 2 sec");
					 setTimeout(function(){
						   location.reload();
					   },2*1000);	   
			      }else{
				 	 if(!resultJson.message){
				 		modal.launchAlert("Error","Error while searching teacher!");
			   	   	}else{
			   	   		modal.launchAlert("Error","Error while searching teacher!"+resultJson.message);
			   	   	}
				 	setTimeout(function(){
				   		location.reload();
				   	},1000*3);
			      }				   
		   	},
		   error:function(data){
			   var resultJson = JSON.parse(data);
			   modal.launchAlert("Error","Teacher with login name : "+teacherLoginName+" not found!");
			   	setTimeout(function(){
			   		location.reload();
			   	},2*1000);
		   }
	});
	}
}

	$(document).ready(function(){
		$('.batchName').tooltip({'placement':'right','html':'true'}).on('click',function(){
			$(this).tooltip('hide');
		});
		$('div#addTeacherModal').on('click','button#btn-addTeacher',function(){
			
			var teacherLoginName=document.getElementById("teacherLoginName").value;
			subjectIds="";
			getSelectedSubjectsForAddTeacher();
			if(!teacherLoginName || teacherLoginName.trim()==""|| teacherLoginName.trim().length==0){
				 modal.launchAlert("Error","Error!</strong> Teacher login name cannot be blank");		
			}else {
			   $.ajax({
		                url: 'classOwnerServlet',
		                data:{
					    	 methodToCall: "addTeacher",
					    	 regId:'',
					    	 subjects:subjectIds,
					    	 teacherLoginName:teacherLoginName,
					   		}, 
					   	type:"POST", 
					   	 success:function(e){
						   	   $('div#addTeacherModal .progress').addClass('hide');
							   var resultJson = JSON.parse(e);
							      if(resultJson.status != 'error'){
							   	   $('div#addTeacherModal').modal('hide');
							   	   modal.launchAlert("Success","Teacher Added! Page will refresh in 2 sec");
							   	   setTimeout(function(){
							   		   location.reload();
							   	   },2*1000);		   
							      }else{
								 	   $('div#addTeacherModal .add').removeClass('hide');
							   		   $('div#addTeacherModal .error').show();
							   	   	if(!resultJson.message){
							   	   	  $('div#addTeacherModal .error').html('<strong>Error!</strong> Unable to add');
							   	   	}else{
							   	  		$('div#addTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
							   	   	}
							      }
						   	},
						   error:function(e){
							   $('div#addTeacherModal .progress').addClass('hide');
								$('div#addTeacherModal .add').removeClass('hide');
								$('div#addTeacherModal .error').show();
								var resultJson = JSON.parse(e);
								if(!resultJson.message){
									   $('div#addTeacherModal .error').html('<strong>Error!</strong> Unable to update');
								   	}else{
								   		$('div#addTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
								   	}
						   }
		            }); 
				}
				$(".chkSubjectAddTeacher:checked").removeAttr('checked');
		     });
		$('.addsubject2batch').tooltip();

		$('.addsubject2batch').popover({'placement':'bottom','content':$('#allSubject').html(),'html':true});

		$('div#modifyTeacherModal').on('click','button#btn-update',function(){
			
			var teacherId=document.getElementsByName("radioTeacher")[0].value;
			
			subjectIds="";
			$('div#modifyTeacherModal .error').html('');
			$('div#modifyTeacherModal .error').hide();
			getSelectedSubjectsForTeacher();
			
			if(!subjectIds || subjectIds.trim()==""){
				$('div#modifyTeacherModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Subject name cannot be blank');
				$('div#modifyTeacherModal .error').show();
			}else{
				
				$.ajax({
					 url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "updateTeacher",
							 regId:'',
							 subIds:subjectIds,
							 teacherId:teacherId
					   		},
					   type:"POST",
					   success:function(data){
						  // successCallbackTeacherModify(data);
						   /* var resultJson = JSON.parse(data);
						   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
							modal.launchAlert("Success","Teacher is updated with teacher id ="+teacherId+"!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   $('div#modifyTeacherModal .progress').addClass('hide');
						   var resultJson = JSON.parse(data);
						      if(resultJson.status != 'error'){
						   	   $('div#modifyTeacherModal').modal('hide');
						   	   modal.launchAlert("Success","Teacher Updated! Page will refresh in 2 sec");
						   	   setTimeout(function(){
						   		   location.reload();
						   	   },2*1000);		   
						      }else{
						   		   $('div#modifyTeacherModal .add').removeClass('hide');
						   		   $('div#modifyTeacherModal .error').show();
						   	   	if(!resultJson.message){
						   		   $('div#modifyTeacherModal .error').html('<strong>Error!</strong> Unable to update');
						   	   	}else{
						   	   		$('div#modifyTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
						   	   	}
						      	}
					   	},
					   error:function(data){
						  /*  modal.launchAlert("Error","Error occured while updating the teacher!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   //	errorCallbackTeacherModify(data);
						   $('div#modifyTeacherModal .progress').addClass('hide');
							$('div#modifyTeacherModal .add').removeClass('hide');
							$('div#modifyTeacherModal .error').show();
							var resultJson = JSON.parse(data);
							if(!resultJson.message){
								   $('div#modifyTeacherModal .error').html('<strong>Error!</strong> Unable to update');
							   	}else{
							   		$('div#modifyTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
							   	}
					   }
				});
				$('div#modifyTeacherModal .progress').removeClass('hide');
				$('.add').addClass('hide');
			}
		});

		$('div#deleteTeacherModal').on('click','button#btn-deleteTeacher',function(){
			var teacherId=document.getElementsByName("radioTeacher")[0].value;
			$('div#deleteTeacherModal .error').html('');
			$('div#deleteTeacherModal .error').hide();
			
			$('div#deleteTeacherModal .progress').removeClass('hide');
			$('.add').addClass('hide');
					$.ajax({
					 url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "deleteTeacher",
							 regId:'',
							 teacherId:teacherId
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
						   $('div#deleteTeacherModal .progress').addClass('hide');
						   var resultJson = JSON.parse(data);
						      if(resultJson.status != 'error'){
						   	   $('div#deleteTeacherModal').modal('hide');
						   	   modal.launchAlert("Success","Teacher Deleted! Page will refresh in 2 sec");
						   	   setTimeout(function(){
						   		   location.reload();
						   	   },2*1000);		   
						      }else{
						   		   $('div#deleteTeacherModal .add').removeClass('hide');
						   		   $('div#deleteTeacherModal .error').show();
						   	   	if(!resultJson.message){
						   		   $('div#deleteTeacherModal .error').html('<strong>Error!</strong> Unable to delete');
						   	   	}else{
						   	   		$('div#deleteTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
						   	   	}
						      	}
					   	},
					   error:function(data){
						  /*  modal.launchAlert("Error","Error occured while deleting teacher!");
						   	setTimeout(function(){
						   		location.reload();
						   	},1000*3); */
						   	//errorCallbackTeacherDelete(data);
						   $('div#deleteTeacherModal .progress').addClass('hide');
							$('div#deleteTeacherModal .add').removeClass('hide');
							$('div#deleteTeacherModal .error').show();
							var resultJson = JSON.parse(data);
							if(!resultJson.message){
								   $('div#deleteTeacherModal .error').html('<strong>Error!</strong> Unable to delete');
							   	}else{
							   		$('div#deleteTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
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
					<td><button type="button" class="btn btn-info" data-target="#addTeacherModal" data-toggle="modal">Add Teacher</button></td>
					<td><input type="text" class="form-control" id="teacherLoginNameSearch" size="20"/></td>					
					<td><button type="button" class="btn btn-info" id="searchTeacher" onclick="searchTeacher()" >Search Teacher</button></td>
				</tr>
				<tr></tr>
				<tr></tr>
			</thead>
		</table>
		
		<%TeacherDetails teacherSearch=(TeacherDetails)request.getSession().getAttribute("teacherSearchResult");
		if(teacherSearch!=null){
		//System.out.println("studentSearch : "+studentSearch.getStudentUserBean().getLoginName());
		%> 
		<table class="table table-bordered table-hover" style="background-color: white;" border="1">
			<thead>
				<tr>
					<td></td>
					<td>Teacher Login Name</td>
					<td>Student Name</td>
					<td>Subjects</td>
					<td></td>
					<td></td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td> <INPUT TYPE="radio" NAME="radioTeacher" VALUE="<%=teacherSearch.getTeacherId() %>" CHECKED></td>
					<td><%=teacherSearch.getTeacherBean().getLoginName() %></td>
					<td><%=teacherSearch.getTeacherBean().getFname() %> </td>
					<td><%=teacherSearch.getSubjectNames() %></td>
					<td><button type="button" class="btn btn-info" data-target="#modifyTeacherModal" data-toggle="modal">Modify Teacher Subjects</button></td>
					<td> <button type="button" class="btn btn-info" data-target="#deleteTeacherModal" data-toggle="modal">Delete Teacher</button></td>
				</tr>
			</tbody>
		</table>
		
		<%
		request.getSession().setAttribute("teacherSearchResult",null);		
		}
		%> 
		<!-- <br><br><br>
		  <button type="button" class="btn btn-info" data-target="#addTeacherModal" data-toggle="modal">Add Teacher</button>
		 <br><br> -->
	</div>
	<br/><br/>
	<div class="panel-group" id="accordion">
		<table class="table table-bordered table-hover" style="background-color: white;" border="1">
			<thead>
				<tr>
					<!--<td> <input type="checkbox" class="chk" name="selectAll" id="selectAll" data-label="selectAll">Select All</<input></td>  -->
					<td>Teacher Login Name</td>
					<td>Teacher Name</td>
					<td>Subjects</td>
				</tr>
			</thead>
			
			 <%			
				if(null != list){
				 Iterator iteratorList = list.iterator();
				 while(iteratorList.hasNext()){
				 TeacherDetails teacher = (TeacherDetails)iteratorList.next();
				 //String timmingsTitle = "Start time :"+ batchDataClass.getTimmings().getStartTimming()+"<br>End Time :"+batchDataClass.getTimmings().getEndTimming(); 
					String timmingsTitle = "Start time :";					 		 
			%> 
			<tbody>
				<tr>
					<!-- <td>  <input type="checkbox" class="chk" name="teachername" id="teachername" data-label="<%=teacher.getTeacherBean().getLoginName() %>" value="<%=teacher.getTeacherId() %>"/></td> -->
					<td><%=teacher.getTeacherBean().getLoginName() %></td>
					<td><%=teacher.getTeacherBean().getFname() %> </td>
					<td><%=teacher.getSubjectNames() %> </td>
				</tr>
			<%	}
			}else{
			%>
				<tr>
					<td colspan="4"> No teachers available in class </td>
				</tr>
			 <%} 
			 %>
			 </tbody>
		</table>
	</div>
</body>
</html>