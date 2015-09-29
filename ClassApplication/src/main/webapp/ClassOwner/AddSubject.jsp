<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
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
        	/* modal.launchAlert("Success","Subject Deleted! Page will refresh in soon");
 		   setTimeout(function(){
 			   location.reload();
 		   },2*1000); */
        	$("#paginateform").submit();
        	
        }, error: function(){
            alert('ajax failed');
        }
});
}
	$(document).ready(function(){
		
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
		
		$("#subjectName").on("keyup",function(){
			var string = $(this).val();	
			$(this).val(string.charAt(0).toUpperCase() + string.slice(1));
		});
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
	                	$("#paginateform").submit();
	                	/* modal.launchAlert("Success","Subject Modified! Page will refresh in soon");
	         		   setTimeout(function(){
	         			   location.reload();
	         		   },2*1000); */
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

<div class="">
 <div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 10px;">
	<div align="center" style="font-size: larger;"><u>Manage Subject</u></div>
  <button type="button" class="btn btn-info" data-target="#addSubjectModal" data-toggle="modal"><i class="glyphicon glyphicon-plus"></i>&nbsp;Add Subject</button>
  </div>
 </div>
<br><br>
<%List<Subject> list=(List<Subject>)request.getAttribute("listOfSubjects");
int endIndex=(Integer)request.getAttribute("endIndex");
int currentPage=(Integer)request.getAttribute("currentPage");
int startIndex=(Integer)request.getAttribute("startIndex");
%>
<%
int counter=startIndex;
if(list!=null){
if(list.size()>0){
%>
<div>
<table class="table table-bordered table-hover" style="background-color: white;" data-toggle="table">

<tr style="background-color: rgb(0, 148, 255);">
<th>SR No.</th>
<th>Subject Name</th>
<th>Topics/Chapters</th>
<th>Edit</th>
<th>Delete</th>
</tr>
<%while(counter< endIndex){ %>
<tr>
<td><%=counter+1 %></td>
<td><%=list.get(counter).getSubjectName() %><input type="hidden" id="sub<%=list.get(counter).getSubjectId()%>" value="<%=list.get(counter).getSubjectName()%>"></td>
<td><a class="btn btn-primary" href="addtopics?actionname=initiate&subid=<%=list.get(counter).getSubjectId() %>&subname=<%=list.get(counter).getSubjectName()%>&currentPage=<%=currentPage %>" id="<%=list.get(counter).getSubjectId()%>">Add Subject Topics/Chapters</a></td>
<td><a href="#" class="btn btn-primary" id="<%=list.get(counter).getSubjectId()%>" onclick="getSubject(<%=list.get(counter).getSubjectId()%>)">Edit</a></td>
<td><a href="#" class="btn btn-danger" id="<%=list.get(counter).getSubjectId()%>" onclick="deleteSubject(<%=list.get(counter).getSubjectId()%>)">Delete</a></td>
</tr>
<%counter++;}
%>
</table>
</div>
<div>
 <form action="addsubject" id="paginateform">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname" value='<c:out value="${actionname}"></c:out>'>
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

<%}else{
	%>
	<span class="alert alert-info">No Subject added yet</span>
	<%
}
}%>

<div class="modal fade" id="addTopicModal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="myModalLabel">Small modal</h4>
        </div>
        <div class="modal-body" id="mymodalmessage">
          
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>
</div>
<form action="addsubject" id="addsub">
<input type="hidden" name="actionname" id="actionname" value='subjectadded'>
  <input type="hidden" name="currentPage" id="currentPage">
  <input type="hidden" name="totalPages" id="totalPages">
</form>
</body>
</html>