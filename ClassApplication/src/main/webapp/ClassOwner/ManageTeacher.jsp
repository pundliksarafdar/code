
<%@page import="com.classapp.db.Teacher.TeacherDetails"%>
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<script type="text/javascript" src="js/ManageTeacher.js"></script>
<script type="text/javascript" src="js/AddSubject.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.TEACHER_LIST); %>
<script>
var subjectIds;
var searchedteacherID;
function canceledit(){
	//alert(batchID+" "+pagenumber);
	 $("#teachertomodify").hide();
	 $("#modifyTeacherModalbody").empty();
	 $("#paginateform").show();
	 $("#baseteachertable").show();
	
}
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


function searchTeacherthroughtable(teacherLoginName) {
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
				   var teacherssubjectname= resultJson.teacherssubjectname;
				   var teacherssubjectIds= resultJson.teacherssubjectIds.split(",");
				   var teachersloginname= resultJson.teachersloginname;
				   var teacherssuffix= resultJson.teacherssuffix;
				   var allsubjectname= resultJson.allsubjectname.split(",");
				   var allsubjectIds= resultJson.allsubjectIds.split(",");
				   $("#teachertomodifybody").empty();
				   $("#modifyTeacherModalbody").empty();
				   $("#teachertomodifybody").append("<td>"+teachersloginname+"</td><td>"+firstname+" "+lastname+"</td><td>"+teacherssubjectname+"</td>"
						   +"<td><button type='button' class='btn btn-info' data-target='#modifyTeacherModal' data-toggle='modal'>Modify Teacher</button></td>"
						   +"<td> <button type='button' class='btn btn-info' data-target='#deleteTeacherModal' data-toggle='modal'>Delete Teacher</button></td>"
						   +"<td> <a onclick='canceledit()'><button type='button' class='btn btn-info'>Cancel</button></a></td>");
				   $("#modifysuffix").val(teacherssuffix);
				   if(allsubjectIds[0]!=""){
					   for(var i=0;i<allsubjectIds.length;i++){
						   var flag=false;
						   for(var j=0;j<teacherssubjectIds.length;j++){
							   if(teacherssubjectIds[j]==allsubjectIds[i]){
								   flag=true;
							   }
						   }
						   if(flag==true){
							   $("#modifyTeacherModalbody").append("<div class='input-group'>"
							  +"<span class='input-group-addon'>"
								+"<input type='checkbox' class='chkSubjectTeacher' name='subjectnameTeacher' id='subjectnameTeacher'  value='"+allsubjectIds[i]+"' checked/>"
								+"</span>"
								+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
							+"</div>");
						   }else{
							   $("#modifyTeacherModalbody").append("<div class='input-group'>"
										  +"<span class='input-group-addon'>"
											+"<input type='checkbox' class='chkSubjectTeacher' name='subjectnameTeacher' id='subjectnameTeacher'  value='"+allsubjectIds[i]+"'/>"
											+"</span>"
											+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
										+"</div>");
						   }
					   }
				   }
				   $("#teachertomodify").show();
				   $("#paginateform").hide();
				   $("#baseteachertable").hide();
				   searchedteacherID=teacherId;
				   //alert("Found "+firstname+" "+lastname+" with Student id ="+studentId+"!");
				
					//modal.launchAlert("Success","Found "+firstname+" "+lastname+" with teacher id ="+teacherId+"! Page will refresh in soon");
					 /* setTimeout(function(){
						   location.reload();
					   },2*1000); */	   
			      }else{
				 	 if(!resultJson.message){
				 		modal.launchAlert("Error","Error while searching teacher!");
			   	   	}else{
			   	   		modal.launchAlert("Error",""+resultJson.message);
			   	   	}
				 	/* setTimeout(function(){
				   		location.reload();
				   	},1000*3); */
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


function searchTeacher() {
	var teacherLoginName=document.getElementById("teacherLoginNameSearch").value;
	var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
	if(!teacherLoginName || teacherLoginName.trim()==""|| teacherLoginName.trim().length==0){
		 modal.launchAlert("Error","Error!</strong> &nbsp;Teacher login name cannot be blank");		
	}else if($("#teacherLoginNameSearch").val().length<5 || !$("#teacherLoginNameSearch").val().match(regloginname))
	{
		 modal.launchAlert("Error","<div style='color:red'><strong>Error!</strong>&nbsp;Invalid Teacher login name</div>");
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
				   var teacherssubjectname= resultJson.teacherssubjectname;
				   var teacherssubjectIds= resultJson.teacherssubjectIds.split(",");
				   var teachersloginname= resultJson.teachersloginname;
				   var teacherssuffix= resultJson.teacherssuffix;
				   var allsubjectname= resultJson.allsubjectname.split(",");
				   var allsubjectIds= resultJson.allsubjectIds.split(",");
				   $("#teachertomodifybody").empty();
				   $("#modifyTeacherModalbody").empty();
				   $("#teachertomodifybody").append("<td>"+teachersloginname+"</td><td>"+firstname+" "+lastname+"</td><td>"+teacherssubjectname+"</td>"
						   +"<td><button type='button' class='btn btn-info' data-target='#modifyTeacherModal' data-toggle='modal'>Modify Teacher</button></td>"
						   +"<td> <button type='button' class='btn btn-info' data-target='#deleteTeacherModal' data-toggle='modal'>Delete Teacher</button></td>"
						   +"<td> <a onclick='canceledit()'><button type='button' class='btn btn-info'>Cancel</button></a></td>");
				   $("#modifysuffix").val(teacherssuffix);
				   if(allsubjectIds[0]!=""){
					   for(var i=0;i<allsubjectIds.length;i++){
						   var flag=false;
						   for(var j=0;j<teacherssubjectIds.length;j++){
							   if(teacherssubjectIds[j]==allsubjectIds[i]){
								   flag=true;
							   }
						   }
						   if(flag==true){
							   $("#modifyTeacherModalbody").append("<div class='input-group'>"
							  +"<span class='input-group-addon'>"
								+"<input type='checkbox' class='chkSubjectTeacher' name='subjectnameTeacher' id='subjectnameTeacher'  value='"+allsubjectIds[i]+"' checked/>"
								+"</span>"
								+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
							+"</div>");
						   }else{
							   $("#modifyTeacherModalbody").append("<div class='input-group'>"
										  +"<span class='input-group-addon'>"
											+"<input type='checkbox' class='chkSubjectTeacher' name='subjectnameTeacher' id='subjectnameTeacher'  value='"+allsubjectIds[i]+"'/>"
											+"</span>"
											+"<input type='text' value='"+allsubjectname[i]+"' class='form-control' disabled='disabled'>"
										+"</div>");
						   }
					   }
				   }
				   $("#teachertomodify").show();
				   $("#paginateform").hide();
				   $("#baseteachertable").hide();
				   searchedteacherID=teacherId;   
			      }else{
				 	 if(!resultJson.message){
				 		modal.launchAlert("Error","Error while searching teacher!");
			   	   	}else{
			   	   		modal.launchAlert("Error",""+resultJson.message);
			   	   	}
				 	/* setTimeout(function(){
				   		location.reload();
				   	},1000*3); */
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
		$('div#modifyTeacherModal .error').html('');
		$('div#modifyTeacherModal .error').hide();
		
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
							   	   modal.launchAlert("Success","Teacher Added! Page will refresh in soon");
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
			
			var teacherId=searchedteacherID;
			
			subjectIds="";
			$('div#modifyTeacherModal .error').html('');
			$('div#modifyTeacherModal .error').hide();
			
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
			//getSelectedSubjectsForTeacher();
			console.log(subjectIds);	
			var suffixregex=/^[A-Za-z]*$/;
			var suffix=$("#modifysuffix").val();
			if(!subjectIds || subjectIds.trim()==""){
				$('div#modifyTeacherModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Please select a subject');
				$('div#modifyTeacherModal .error').show();
			}else if(!suffix.match(suffixregex)){
				$('div#modifyTeacherModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Invalid Suffix');
				$('div#modifyTeacherModal .error').show();
			}else{
				
				$.ajax({
					 url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "updateTeacher",
							 regId:'',
							 subIds:subjectIds,
							 teacherId:teacherId,
							 suffix:suffix
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
						   	 $("#teachertomodify").hide();
						   	   modal.launchAlert("Success","Teacher Updated! Page will refresh in soon");
						   	 $("#paginateform").submit();	   
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
			var teacherId=searchedteacherID;
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
						   	 $("#teachertomodify").hide();
						   	   modal.launchAlert("Success","Teacher Deleted! Page will refresh in soon");
						   	 $("#paginateform").submit();		   
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

	<div class="">	
		<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 10px;">
			<div align="center" style="font-size: larger;margin-bottom: 15px"><u>Manage teacher</u></div>
			<div class="row">
			<div class="col-md-4">
			<button type="button" class="btn btn-info" data-target="#addTeacherModal" data-toggle="modal"><i class="glyphicon glyphicon-user"></i>&nbsp;Add Teacher</button>
			</div>
			
			<div class="col-md-4">
			<div class="input-group">
				<input type="text" class="form-control" id="teacherLoginNameSearch" placeholder="Teacher Login Name" size="20"/>
				<span class="input-group-btn">
				<button type="button" class="btn btn-info" id="searchTeacher" onclick="searchTeacher()" ><i class="glyphicon glyphicon-search"></i>&nbsp;Search Teacher</button>
				</span>
			</div>
			</div>
		</div>
		</div>
		
		<%TeacherDetails teacherSearch=(TeacherDetails)request.getSession().getAttribute("teacherSearchResult");
		/* if(teacherSearch!=null){ */
		//System.out.println("studentSearch : "+studentSearch.getStudentUserBean().getLoginName());
		%> 
		<table id="teachertomodify" class="table table-bordered table-hover" style="background-color: white;display:none" border="1">
			<thead>
				<tr style="background-color: rgb(0, 148, 255);">
					<th>Teacher Login Name</th>
					<th>Teacher Name</th>
					<th>Subjects</th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody id="teachertomodifybody">
				<%-- <tr>
					<td> <INPUT TYPE="radio" NAME="radioTeacher" VALUE="<%=teacherSearch.getTeacherId() %>" CHECKED></td>
					<td><%=teacherSearch.getTeacherBean().getLoginName() %></td>
					<td><%=teacherSearch.getTeacherBean().getFname() %> </td>
					<td><%=teacherSearch.getSubjectNames() %></td>
					<td><button type="button" class="btn btn-info" data-target="#modifyTeacherModal" data-toggle="modal">Modify Teacher</button></td>
					<td> <button type="button" class="btn btn-info" data-target="#deleteTeacherModal" data-toggle="modal">Delete Teacher</button></td>
					<td> <a onclick="canceledit()"><button type="button" class="btn btn-info">Cancel</button></a></td>
				</tr> --%>
			</tbody>
		</table>
		
		<%
		/* request.getSession().setAttribute("teacherSearchResult",null);		
		} */
		%> 
		<!-- <br><br><br>
		  <button type="button" class="btn btn-info" data-target="#addTeacherModal" data-toggle="modal">Add Teacher</button>
		 <br><br> -->
	</div>
	<br/><br/>
	
	<s:choose>
	<s:when test="${teacherListSize gt 0}">
	<div class="panel-group" id="accordion">
		<table class="table table-bordered table-hover" style="background-color: white;" border="1" id="baseteachertable">
			<thead>
				<tr style="background-color: rgb(0, 148, 255);">
					<!--<td> <input type="checkbox" class="chk" name="selectAll" id="selectAll" data-label="selectAll">Select All</<input></td>  -->
					<th>Teacher Login Name</th>
					<th>Teacher Name</th>
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
				// Iterator iteratorList = list.iterator();
				 while(i<endIndex){
				 TeacherDetails teacher = (TeacherDetails)list.get(i);
				 //String timmingsTitle = "Start time :"+ batchDataClass.getTimmings().getStartTimming()+"<br>End Time :"+batchDataClass.getTimmings().getEndTimming(); 
					String timmingsTitle = "Start time :";					 		 
			%> 
			<tbody>
				<tr>
					<!-- <td>  <input type="checkbox" class="chk" name="teachername" id="teachername" data-label="<%=teacher.getTeacherBean().getLoginName() %>" value="<%=teacher.getTeacherId() %>"/></td> -->
					<td><%=teacher.getTeacherBean().getLoginName() %></td>
					<td><%=teacher.getTeacherBean().getFname() %> <%=teacher.getTeacherBean().getLname() %> </td>
					<td><%=teacher.getSubjectNames() %> </td>
					<td><button type="button" class="btn btn-info" onclick="searchTeacherthroughtable('<%=teacher.getTeacherBean().getLoginName() %>')" >Edit</button></td>
				</tr>
			<%	i++;}
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
	<div>
 <form action="manageteacher" id="paginateform">
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
		<span class="alert alert-info">No teacher added yet</span>
	</s:otherwise>
	</s:choose>
</body>
</html>