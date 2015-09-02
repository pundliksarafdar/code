<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
	$("#topics_division").on("change",function(){
		var divisionID=$("#topics_division").val();
		var subID=$("#subjectID").val();
		 $.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getDivisionsTopics",
			    	 divisionID:divisionID,
			    	 subID:subID
			   		},
			   type:"POST",
			   success:function(data){
				   alert("Its Working...");
			   },
			   error:function(){
				   }
			   });
	});
	
});

</script>
</head>
<body>
<input type="hidden" value="<c:out value="${subid}"> </c:out>" id="subjectID">
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
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
			</div>
</div>
		<div class="container">
  <h2><font face="cursive">Topics</font> </h2>            
  <table class="table table-striped" id="topictable">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Topic Name</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
    </table>
    </div>	
</body>
</html>