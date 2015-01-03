<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<html>
<head>
<script type="text/javascript" src="js/AddSubject.js"></script>
<script>
var subjectid="";
function getSubject(subid){
	subjectid=subid;
	$("#editsubject").val($("#sub"+subid).val());
	$('div#ModifysubjectModal .error').hide();
	$('#ModifysubjectModal').modal('toggle');
	
}

function deleteSubject(subid){
	$.ajax({
        url: 'classOwnerServlet',
        type: 'post',
        data: {
	    	 methodToCall: "deletesubject",
	    	 subjectid:subid
        },
        success: function(){
        	modal.launchAlert("Success","Subject Deleted! Page will refresh in soon");
 		   setTimeout(function(){
 			   location.reload();
 		   },2*1000);
        	
        }, error: function(){
            alert('ajax failed');
        }
});
}
	$(document).ready(function(){
		$('#savesubject').click(function(){
			var subjectname=$("#editsubject").val();
			if(subjectname.length>0){
			$('div#ModifysubjectModal .progress').removeClass('hide');
			$('div#ModifysubjectModal .savesubject').addClass('hide');
			 $.ajax({
	                url: 'classOwnerServlet',
	                type: 'post',
	                data: {
				    	 methodToCall: "modifysubject",
				    	 subjectname:subjectname,
				    	 subjectid:subjectid
	                },
	                success: function(data){
	                	var resultJson = JSON.parse(data);
	                	var result=resultJson.added;
	                	if(result=="false"){
	                		$('div#ModifysubjectModal .progress').addClass('hide');
	                		$('div#ModifysubjectModal .savesubject').removeClass('hide');
	                		$('div#ModifysubjectModal .error').show();
	                  		$('div#ModifysubjectModal .error').html('<strong>Error!</strong> Subject Already Present');
	                	}else{
	                	$('#ModifysubjectModal').modal('hide');
	                	modal.launchAlert("Success","Subject Modified! Page will refresh in soon");
	         		   setTimeout(function(){
	         			   location.reload();
	         		   },2*1000);
	                	}
	                }, error: function(data){
	                    alert('ajax failed');
	                }
		});
			}else{
				
				$('div#ModifysubjectModal .error').show();
          		$('div#ModifysubjectModal .error').html('<strong>Error!</strong> Subject Name cannot be empty');
			}
		});
		
		/* $("#addteacher").click(function() 
				{  	alert("Yo Yo1");
		             $.ajax({
		                url: 'classOwnerServlet',
		                type: 'post',
		                data: {
					    	 methodToCall: "fetchsubjects",
					   		},
		                success: function(){
		                	$('#addTeacherModal').modal({show:true});
		                }, error: function(){
		                    alert('ajax failed');
		                }
		            }); 
		            
		        }); */
		
		$('.batchName').tooltip({'placement':'right','html':'true'}).on('click',function(){
			$(this).tooltip('hide');
		});
		
		$('.addsubject2batch').tooltip();
/*		$('[data-toggle="popover"]').popover({container: '.popoverContainer'+$(this).attr('popovername')});
*/	
/*
		$('[data-toggle="popover"]').on('click',function(){
			$(this).popover('show');
		});
		*/
		
		$('.addsubject2batch').popover({'placement':'bottom','content':$('#allSubject').html(),'html':true});

	});
	
	
	
</script>
</head>
<body>
<div class="btn-group btn-group-sm">
 
  <button type="button" class="btn btn-info" data-target="#addSubjectModal" data-toggle="modal">Add Subject</button>
  
 </div>
<br><br>
<%List<Subject> list=(List<Subject>)request.getAttribute("listOfSubjects"); %>
<table class="table table-bordered table-hover" style="background-color: white;" data-toggle="table">
<%
int counter=0;
if(list!=null){%>
<tr>
<th>SR No.</th>
<th>Subject Name</th>
<th>Edit</th>
<th>Delete</th>
</tr>
<%while(counter<list.size()){ %>
<tr>
<td><%=counter+1 %></td>
<td><%=list.get(counter).getSubjectName() %><input type="hidden" id="sub<%=list.get(counter).getSubjectId()%>" value="<%=list.get(counter).getSubjectName()%>"></td>
<td><a href="#" id="<%=list.get(counter).getSubjectId()%>" onclick="getSubject(<%=list.get(counter).getSubjectId()%>)">Edit</a></td>
<td><a href="#" id="<%=list.get(counter).getSubjectId()%>" onclick="deleteSubject(<%=list.get(counter).getSubjectId()%>)">Delete</a></td>
</tr>
<%counter++;}}else{ %>
<tr>
<th>Till You Havn't Added any subject</th>
</tr>
<%} %>
</table>

</body>
</html>