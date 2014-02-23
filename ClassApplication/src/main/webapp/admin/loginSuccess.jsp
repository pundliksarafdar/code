<%@page import="com.miscfunction.MiscFunction"%>
<%@page import="java.util.Date"%>
<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<script type="text/javascript">

$(document).ready(function(){
	$('#pager li').bind('click',function(){
		if('next' == $(this).attr('class')){
			$('#task').val("next");
			$('#searchClass').submit();
		}else if('previous'== $(this).attr('class')){
			$('#task').val("prev");
			$('#searchClass').submit();
		}
	});
});

function loadClassList(){

	$('#divLoad').load('/ClassApplication/getClassList',function(response, status, xhr){		
		
	});
}

function acceptClass(regId,that){
	$.ajax({
		   url: "admajxsrvlt",
		   data: {
		    	 methodToCall: "reg",
				 regId:$(that).parents('#tableTr').find('#regId').text(),
				 duration:$(that).parents('#tableTr').find('#duration').val()
		   		},
		   type:"POST",
		   success:function(){
			   		alert('Success');
		   		}
		});
	}
</script>

<div class="btn-group">
<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">USER <span class="caret"></span></button>
  <ul class="dropdown-menu">
    <li><a href="javascript:allAjax.classSearch();">Class List</a></li>
  </ul>
</div>
<div id='divLoad'>
</div>

<!-- Table for class list-->
	<%
	MiscFunction miscFunction = new MiscFunction();
	List list = (List)request.getAttribute("searchresult");
	int totalPages = 0;
	int currentPage = 0;
	
	if(null != request.getAttribute("totalPages")){
		totalPages = (Integer) request.getAttribute("totalPages");
	}
	if(null != request.getAttribute("currentPage")){
		currentPage = Integer.parseInt(request.getAttribute("currentPage").toString());
	}
	if(null!=list){
	Iterator iterator = list.iterator();
	%>
	
	<%if(list.size()==0) {%>
	<div style="color: red;margin-top: 20px;margin-left: 20px;">No result found</div>
	<%}else{ %>
<div class="panel panel-default" style="margin: 10px;">
	<!-- Default panel contents -->
	<div class="panel-heading">
		<b>Search</b>
	</div>
	<!-- Table -->
	<table class="table table-striped">
		<tr>
			<th>RegId</th>
			<th>Class Name</th>
			<th>Owner</th>
			<th>Start Date</th>
			<th>Days left</th>
		</tr>
		<%
		String daysLeft = null;
		while(iterator.hasNext()){
			RegisterBean registerBean = (RegisterBean)iterator.next();
			daysLeft = registerBean.getDaysLeft(); 
		%>
		<tr id="tableTr">
			<td id="regId"><%=registerBean.getRegId() %></td>
			<td><%=registerBean.getClassName() %></td>
			<td><%=registerBean.getFname() %></td>
			<%if(null == daysLeft){%>
			<td><input type="text" placeholder="Duration" id="duration" /></td>
			<td><button type="button" class="btn btn-warning"
					onclick="acceptClass('<%=registerBean.getRegId() %>',this)">Accept</button></td>
			<%}else{%>
			<td><%=miscFunction.dateFormater(registerBean.getRegistrationDate())%></td>
			<td><%=daysLeft %></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<div style="width: 50%; float: right"></div>
	<ul class="pager" id="pager" style="margin:10px 10px;">
	<%if(currentPage != 0){ %>
		<li class="previous"><a href="#">&larr; Older</a></li>
		<%} if(currentPage != totalPages){ %>
		<li class="next"><a href="#">Newer &rarr;</a></li>
		<%} %>
	</ul>
	</div>
<%} %>
<%} %>

<form action = "searchclass" id = "searchClass">
	<input type="hidden" name="classSearchForm.currentPage" id="currentPage" value="<%=currentPage%>">
	<input type="hidden" name="classSearchForm.task" id="task">
</form>
</html>