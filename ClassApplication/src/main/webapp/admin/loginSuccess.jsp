<%@page import="com.miscfunction.MiscFunction"%>
<%@page import="java.util.Date"%>
<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<style>
	.tooltip{
		width : 250px;
	}
</style>
<script type="text/javascript">

$(document).ready(function(){
	$('.pager li').on('click',function(){
		if('next' == $(this).attr('class')){
			$('.task').prop("value","next");
			$('#searchClass').submit();
		}else if('previous'== $(this).attr('class')){
			$('.task').prop("value","prev");
			$('#searchClass').submit();
		}
	});
	
	
	$('a').tooltip({'html':true});
	
	$("#sendMessage").click(function(){
		var messageToSend = $("#message").val();
		
		$.ajax({
			   url: "notificationServlet",
			   data: {
			    	 methodToCall: "notifyall",
			    	 message:messageToSend
			   		},
			   type:"POST",
			   success:function(e){
				   		console.log(e);
			   		}
			});		
	});
});

function acceptClass(regId,that){
	$('#acceptModal .btn-success').on('click',function(){
		var duration = $(this).parents('.modal-content').find('#duration').val()
		allAjax.acceptClass(regId,duration);
	});
	
	$('#acceptModal').modal('show');
	}
	
	function rejectClass(regId){
		modal.modalConfirm("Reject","Do you want to reject user","No","Yes",allAjax.deleteUser,[regId]);
	} 
	function blockUser(regId,role){
		modal.modalConfirm("Block","Do you want to block user?","No","Yes",allAjax.blockUser,[regId,role]);
	}
	
	function unBlockUser(regId,role){
		modal.modalConfirm("UnBlock","Do you want to unblock user?","No","Yes",allAjax.unBlockUser,[regId,role]);
	}
	
	function deleteUser(regId){
		modal.modalConfirm("Reject","Do you want to delete user","No","Yes",allAjax.deleteUser,[regId]);
	}
	
</script>

<div id='divLoad'>
</div>

<!-- Table for class list-->
	<%
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
			<th class = "hidden-xs">Class Name</th>
			<th >Owner</th>
			<th class = "hidden-xs">End Date</th>
			<th>Accept</th>
		</tr>
		<%
		String daysLeft = null;
		while(iterator.hasNext()){
			RegisterBean registerBean = (RegisterBean)iterator.next();
			daysLeft = registerBean.getDaysLeft();
			String toolTip = "<span class='hidden-lg'>Class Name: "+registerBean.getClassName()+"</span>Registration Date:"+MiscFunction.dateFormater(registerBean.getRegistrationDate())+"<br>";
			if(!"Error".equals(MiscFunction.dateFormater(registerBean.getStartDate()))){
				toolTip += "Start Date:"+MiscFunction.dateFormater(registerBean.getStartDate())+"<br>";
			}
			toolTip += "Role:"+MiscFunction.roleConverter(registerBean.getRole());
		%>
		<tr id="tableTr">
			<td id="regId"><%=registerBean.getRegId() %></td>
			<td class = "hidden-xs"><%=registerBean.getClassName() %></td>
			<td><a title="<%=toolTip %>" data-toggle="tooltip"><%=registerBean.getFname() %></a></td>
			<%if(null == daysLeft){%>
			<td class = "hidden-xs" id="duration">NA</td>
			<td>
				<div class="btn-group">
					<button type="button" class="btn btn-primary dropdown-toggle"
						data-toggle="dropdown">
						Action <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li id="accept"><a onclick="acceptClass('<%=registerBean.getRegId() %>',this)" href="#">Accept</a></li>
						<li id="unblock"><a onclick="rejectClass('<%=registerBean.getRegId() %>');" href="#">Reject</a></li>
					</ul>
				</div>
		
					
			</td>
			<%}else{%>
			<td class = "hidden-xs" id="duration"><%=MiscFunction.dateFormater(registerBean.getEndDate())%></td>
			<td>
				<!-- 
				<button type="button" class="btn btn-danger"
					onclick="blockClass('<%=registerBean.getRegId() %>','<%=registerBean.getRole() %>',this)" style="width: 69px;">Block</button>
				-->
				<div class="btn-group">
					<button type="button" class="btn btn-info dropdown-toggle"
						data-toggle="dropdown">
						Action <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li id="block<%=registerBean.getRegId() %>"
						<%if(registerBean.getRole() > 9){%>
							class = "hide"
						<%} %>
						><a onclick="blockUser('<%=registerBean.getRegId() %>','<%=registerBean.getRole() %>')" href="#">Block</a></li>
						<li id="unblock<%=registerBean.getRegId() %>"
						<%if(registerBean.getRole() < 10){%>
							class = "hide"
						<%} %>
						><a onclick="unBlockUser('<%=registerBean.getRegId() %>','<%=registerBean.getRole() %>')" href="#">UnBlock</a></li>
						<li><a href="#" onclick="deleteUser('<%=registerBean.getRegId() %>')">Delete</a></li>
					</ul>
				</div>
			</td>
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
		<li class="previous"><a href="javascript:void(0)">&larr; Older</a></li>
		<%} if(currentPage != totalPages){ %>
		<li class="next"><a href="javascript:void(0)">Newer &rarr;</a></li>
		<%} %>
	</ul>
	</div>
<%} %>
<%} %>

<form action = "searchclass" id = "searchClass">
	<input type="hidden" name="classSearchForm.currentPage" id="currentPage" value="<%=currentPage%>">
	<input type="hidden" name="classSearchForm.task" class="task" value="next">
</form>

<div>
<h3>Notify All</h3>
<input type="text" placeholder="Message" id="message"/>
<a href="#" class="btn btn-default" id="sendMessage">Notify</a>
</div>
<div>
<img alt="temp" src="../images/background.png" height="400px" width="800px">
</div>
</html>