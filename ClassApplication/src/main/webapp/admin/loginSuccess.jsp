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
	
	function blockClass(regId,role,that){
		alert($(that).parents('td').find('#block').parent().html());
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "block",
					 regId:regId,
					 role:role
			   		},
			   type:"POST",
			   success:function(){
				   		$(that).parents('td').find('#block').addClass('hide');
				   		$(that).parents('td').find('#unblock').removeClass('hide');
				   		launchModal("Success","User Blocked!");	   		
			   }
			});
	}
	
	function unBlockClass(regId,role,that){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "unblock",
					 regId:regId,
					 role:role
			   		},
			   type:"POST",
			   success:function(){
				   	$(that).parents('td').find('#unblock').addClass('hide');
			   		$(that).parents('td').find('#block').removeClass('hide');			   		
			   		launchModal("Success","User UnBlocked!");	   		
			   }
			});
	}
	
	function launchModal(header,message){
		$("#myModalLabel").text(header);
		$("#mymodalmessage").text(message);
		$('#pageModal').modal('show');
	}
	
	
	
</script>

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
			<th class = "visible-lg">Owner</th>
			<th>End Date</th>
			<th>Accept</th>
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
			<td class = "visible-lg"><%=registerBean.getFname() %></td>
			<%if(null == daysLeft){%>
			<td><input type="text" placeholder="Duration" id="duration" style="width: 80px;"/></td>
			<td><button type="button" class="btn btn-warning"
					onclick="acceptClass('<%=registerBean.getRegId() %>',this)">Accept</button></td>
			<%}else{%>
			<td><%=miscFunction.dateFormater(registerBean.getEndDate())%></td>
			<td>
				<!-- 
				<button type="button" class="btn btn-danger"
					onclick="blockClass('<%=registerBean.getRegId() %>','<%=registerBean.getRole() %>',this)" style="width: 69px;">Block</button>
				-->
				<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown">
						Action <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li id="block"
						<%if(registerBean.getRole() > 9){%>
							class = "hide"
						<%} %>
						><a onclick="blockClass('<%=registerBean.getRegId() %>','<%=registerBean.getRole() %>',this)" href="#">Block</a></li>
						<li id="unblock"
						<%if(registerBean.getRole() < 10){%>
							class = "hide"
						<%} %>
						><a onclick="unBlockClass('<%=registerBean.getRegId() %>','<%=registerBean.getRole() %>',this)" href="#">UnBlock</a></li>
						<li><a href="#">Delete</a></li>
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

<div class="modal fade bs-example-modal-sm" id="pageModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
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
</html>