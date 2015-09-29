<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
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
$(document).ready(function(){
	$("#topics_division").on("change",fetchTopic);
	$(".deletetopic").on("click",deletetopic);
	$("#addTopic").on("click",addTopic);
	$("#saveTopic").on("click",saveedittopic);
});

function addTopic(){
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	var topicname=$("#topicname").val();
	var flag=false;
	$("#topicnameerror").html("");
	if(topicname.trim()==""){
		$("#topicnameerror").html("Please Enter Topic Name");
		flag=true;
	}else{
	$.ajax({
		 
			url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "addTopic",
		    	 divisionID:divisionID,
		    	 subID:subID,
		    	 topicname:topicname
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson=JSON.parse(data);
			   var status=resultJson.topicexists;
			   if(status!=""){
				   flag= true;
				   $("#topicnameerror").html("Topic name already available!!!");
			   }else {
				   fetchTopic();
			   }
		   },
		   error:function(){
			   }
		   });
	}
	
}

/* function addTopicAfterCheck(divisionID,subID,topicname){

	$.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "addTopic",
		    	 divisionID:divisionID,
		    	 subID:subID,
		    	 topicname:topicname
		   		},
		   type:"POST",
		   success:function(data){
			   fetchTopic();
			  // var resultJson=JSON.parse(data);
		   },
		   error:function(){
			   }
		   });
	//
} */
var edittopicid="";
function edittopic(topicid){
	edittopicid=topicid;
	var topicname=$("#topic"+topicid).html();
	$("#edittopicname").val(topicname);
	$("#topicedit").modal('toggle');
}

function saveedittopic(){
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	var topicname=$("#edittopicname").val();
	if(topicname.trim()==""){
		$("#edittopicnameerror").html("Please Enter Topic Name");
		flag=true;
	}else{
	$.ajax({
		 
			url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "edittopic",
		    	 divisionID:divisionID,
		    	 subID:subID,
		    	 topicname:topicname,
		    	 edittopicid:edittopicid
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson=JSON.parse(data);
			   var status=resultJson.topicexists;
			   if(status!=""){
				   flag= true;
				   $("#edittopicnameerror").html("Topic name already available!!!");
			   }else {
				   fetchTopic();
				   $("#topicedit").modal('toggle');
			   }
		   },
		   error:function(){
			   }
		   });
	}
	 
}

function deletetopic(topicid){
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	 $.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "deleteTopics",
		    	 divisionID:divisionID,
		    	 subID:subID,
		    	 topicid:topicid
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson=JSON.parse(data);
			   fetchTopic();
		   },
		   error:function(){
			   }
		   });
	 
}
function fetchTopic(){
	$("#topictable tbody").empty();
	$("#topictable").hide();
	$("#notopicdiv").hide();
	$("#topicbasediv").hide();
	var divisionID=$("#topics_division").val();
	var subID=$("#subjectID").val();
	 if(divisionID!="-1"){
		 $("#topicname").prop("disabled",false);
		 $("#addTopic").prop("disabled",false);
	 }else{
		 $("#topicname").prop("disabled",true);
		 $("#addTopic").prop("disabled",true);
	 }
	 $.ajax({
		 
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getDivisionsTopics",
		    	 divisionID:divisionID,
		    	 subID:subID
		   		},
		   type:"POST",
		   success:function(data){
			   var resultJson=JSON.parse(data);
			   var topic_names=resultJson.topic_names.split(",");
			   var topic_ids=resultJson.topic_ids.split(",");
			   if(topic_ids[0]!=""){
				   for(var i=0;i<topic_ids.length;i++){
					   $("#topictable tbody").append("<tr><td>"+(i+1)+"</td><td id='topic"+topic_ids[i]+"'>"+topic_names[i]+"</td><td><button class='btn btn-primary' onclick='edittopic("+topic_ids[i]+")'>Edit</button></td><td><button id="+topic_ids[i]+" class='btn btn-danger deletetopic' onclick='deletetopic("+topic_ids[i]+")'>Delete</button></td></tr>");
				   }
				   $("#topictable").show();
			   }else{
				  // $("#topictable").hide();
					$("#notopicdiv").show();
			   }
			   $("#topicbasediv").show();
		   },
		   error:function(){
			   }
		   });
	
}

</script>
</head>
<body>
<input type="hidden" value="<c:out value="${subid}"> </c:out>" id="subjectID">
<div class="container" style="margin-bottom: 5px">
			<a type="button" class="btn btn-primary" href="addsubject?currentPage=<c:out value="${currentPage}"> </c:out>" ><span class="glyphicon glyphicon-circle-arrow-left"></span> Back To Manage Subjects</a>
</div>
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	<div align="center" style="font-size: larger;">Add Topics</div>
			<div class="row">
				<div class='col-sm-6 header' style="padding-bottom: 10px;">*
					Add Topics for subject here 
				</div>
			</div>
			
			<div class="row">
			<div class="col-md-4">
			<select class="form-control" id="topics_division">
			<option value="-1">Select Class</option>
			<c:if test="${ (divisionList != null) }">
			<c:forEach items="${divisionList}" var="item">
				<option value="<c:out value="${item.divId}"> </c:out>"> <c:out value="${item.divisionName}"> </c:out></option>
			</c:forEach>
			</c:if> 
			</select>
			</div>
			<div class="col-md-4">
			<input type="text" class="form-control" id="topicname" disabled="disabled">
			<span id="topicnameerror" class="error"></span>
			</div>
			<div class="col-md-4">
			<button id="addTopic" class="btn btn-primary">Add</button>
			</div>
			</div>
</div>
		<div class="container" id="topicbasediv" style="display: none;">
  <h2><font face="cursive">Topics</font> </h2>            
  <table class="table table-striped" id="topictable" style="display: none">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Topic Name</th>
        <th>Edit</th>
        <th>Delete</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
    </table>
    <div class="alert alert-info" align="center" id="notopicdiv" style="display: none">Topics are not available for selected criteria.</div>
    </div>	
    
    <div class="modal fade" id="topicedit" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Topic Edit
            </h4>
         </div>
         <div class="modal-body">
          <input type="text" id="edittopicname" class="form-control">
          <span id="edittopicnameerror" class="error"></span>
         </div>
         <div class="modal-footer">
         <button id="saveTopic" class="btn btn-primary">Save</button>
         </div>
         </div>
   </div>
</div>
    
</body>
</html>