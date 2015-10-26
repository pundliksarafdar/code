<%@page import="com.classapp.db.batch.division.Division"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
	$( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  minDate:((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()
	  }).data("DateTimePicker");
	
	$("#submit").click(function(){
		var message=$("#message").val();
		if($("#to").val()=='student'){
		var batch=$("#batch").val();
		var date=$("#date").val();
		var batchname=$( "#batch option:selected" ).text();
		if(message!="" && batch!="-1" && date!=""){
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "sendmessage",
				    	 message: message,
				    	 batch:batch,
				    	 date:date,
				    	 batchname:batchname
				   		},
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   modal.launchAlert("Success","Message has been sent");
				   },
					error:function(){
				   		modal.launchAlert("Error","Error");
				   	}
				   });
		}
		}else{
			if(message!=""){
				$.ajax({
					 
					   url: "classOwnerServlet",
					   data: {
					    	 methodToCall: "sendteachermessage",
					    	 message: message
					   		},
					   type:"POST",
					   success:function(data){
						   var resultJson = JSON.parse(data);
						   modal.launchAlert("Success","Message has been sent");
					   },
						error:function(){
					   		modal.launchAlert("Error","Error");
					   	}
					   });
			}
			
		}
	});
	});
	

</script>
</head>
<body>
<%List<Batch> batch=(List<Batch>)request.getAttribute("batch"); 
List<Division> division=(List<Division>)request.getAttribute("division"); 
		int i=0;%>
		<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;"><u>Send Notice/Message</u></div>
			</div>
<div class="form-group">
	<label for="message"  class="col-sm-4 control-label" align="right">Message :</label>
	<div class="col-sm-5" align="left">
	<textarea rows="2" cols="70" name="message" class="form-control" id="message" maxlength="140"></textarea>
	</div>
	<div class="col-sm-2" align="left">
	<span class="error" id="messageerror" name="messageerror"></span>
	</div>
	</div>
	<input type="hidden" name="to" id="to" value="<c:out value='${to}'></c:out>">
	<c:if test="${to eq 'student' }">
	<div class="form-group">
	<label for="batch"  class="col-sm-4 control-label" align="right">Select Batch :</label>
	<div class="col-sm-5" align="left">
	<select id="batch" class="form-control" width="100px">
	<option value="-1">Select Batch</option>
					<%
					if(batch!=null){
						String ids="";
						while(i<batch.size()){
							if(i==0){
								ids=batch.get(i).getBatch_id()+"";
							}else{
								ids=ids+","+batch.get(i).getBatch_id();
							}
							i++;
						}
						i=0;
						if(batch.size()>0){
							%>
							<option value="<%=ids%>">All Batches</option>
				<%
					while(i<batch.size()){
						for(int j=0;j<division.size();j++){
							if(division.get(j).getDivId()==batch.get(i).getDiv_id()){
					%>
					<option value="<%=batch.get(i).getBatch_id()%>_<%=batch.get(i).getDiv_id()%>"><%=division.get(j).getDivisionName()%> <%=division.get(j).getStream()%> <%=batch.get(i).getBatch_name() %></option>
					<%}
						}
					i++;} }}%>
	</select>
	</div>
	<div class="col-sm-2" align="left">
	<span class="error" id="batcherror" name="batcherror"></span>
	</div>
	</div>

	<div class="form-group">
	<label for="date"  class="col-sm-4 control-label" align="right">Select Date :</label>
	<div class="col-sm-5" align="left">
	<div id="datetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
	</div>
	<div class="col-sm-2" align="left">
	<span class="error" id="dateerror" name="dateerror"></span>
	</div>
	</div>
		</c:if>
	<div class="form-group">
	<div class="col-sm-4" align="left"></div>
	<div class="col-sm-5" align="left">
	<button type="submit" class="btn btn-info" id="submit">Submit</button>
	</div>
	
	</div>
</body>
</html>