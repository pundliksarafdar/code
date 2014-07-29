<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="com.classapp.db.subject.ClassSubjects"%>
<%@page import="com.datalayer.subject.Subject"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.config.Constants"%>
<%@page import="java.util.List"%>

	<!-- Search Modal  start-->
<div class="modal fade" id="ajax-modal">
<div class="modal-dialog">
<div class="modal-content">
<form action="searchclass" method="post" id="searchclass">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">Search</h4>
  </div>

<div class="container-fluid" style="padding: 10px;">
  	<table>
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Name Initials<td><input type="text" name="classSearchForm.cname" data-provide="typeahead" class="form-control"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true" onclick="clearDate();">&times;</button>
	<td>Join Date<td>
	<div class="btn-group">
		<button id="cdatebtn" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"> Choose <span class="caret"></span></button>
  		<ul class="dropdown-menu">
    		<li><a href="javascript:enableExactDate();">Exact Date</a></li>
    		<li><a href="javascript:enableBetweenDate();">Between</a></li>
  		</ul>	
  	</div>
  	<td id="exact" style="display: none;">
  		<input type="date" name="classSearchForm.cexactdate" class="form-control">
  	<td>
  	<td id="between" style="display: none;">
  		<input type="date" placeholder="Form" name="classSearchForm.cfrmdate" class="form-control"/>
  		<span>&nbsp;</span><input type="date" placeholder="To" name="classSearchForm.ctodate" class="form-control"/>
  	<td>
	</td></tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Duration<td><input type="text" name="classSearchForm.cduration" data-provide="typeahead" class="form-control"/> 
	</td>
	<td>Month</td>
	</tr>
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>State<td><input type="text" name="classSearchForm.cstate" data-provide="typeahead" class="form-control"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>City<td><input type="text" name="classSearchForm.ccity" data-provide="typeahead" class="form-control"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Class Id<td><input type="text" name="classSearchForm.cid" data-provide="typeahead" class="form-control"/>
	</td>
	</tr>
	</table>
</div>

<!-- footer -->
  <div class="modal-footer">
    <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
    <button type="button" class="btn btn-info" onclick="allAjax.searchClass('searchclass')">Search</button>
  </div>
</form>
</div>
</div>
</div>

	<!-- Search Modal  end-->
	<!-- Modal confirmation Box Start -->
	<div class="modal fade" id="messageModal">
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

<div class="modal fade" id="acceptModal">
<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Accept Class</h4>
        </div>
        <div class="modal-body" id="">
          	<div>Do you want to accept the class?</div>
          	<div>
          		<table>
          			<tr>
          				<td><input type="text" id="duration" data-provide="typeahead" class="form-control"/></td><td>&nbsp;&nbsp;&nbsp; Months</td>
          			</tr>
          		</table>
          	</div>
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-success" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>
</div>

<div class="modal fade" id="rejectModal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Reject Class</h4>
        </div>
        <div class="modal-body" id="">
          	<div>Do you want to reject the class?</div>
          	
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-danger" data-dismiss="modal">Yes</button>
      	</div>
    </div>
</div>	
</div>
<div class="modal fade" id="addBatchModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Add Batch</h4>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger"></div>
			<div class="form-group">
				<input type="text" class="form-control" id="batchName"/>
				<br>
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>
					<div class='input-group date' id='fromDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
					
					<div class="col-sm-6">
					<label for="">End Time</label>
					<div class='input-group date' id='toDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
				</div>
				</div>
				</div>
			</div>				
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="add">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="btn-add">Add</button>
	        </div>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>


<div class="modal fade" id="addSubjectModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Add Subject</h4>
        </div>
        <div class="modal-body" id="abc">
        	<div class="error alert alert-danger"></div>
			<div class="form-group" id="abc1">
				<input type="text"  id="subjectName" name="subjectName"/>
				<script>
				$("#subjectName").autocomplete("AutoComplete.jsp");
				</script>
				<br>
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>
					<div class='input-group date' id='fromDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
					
					<div class="col-sm-6">
					<label for="">End Time</label>
					<div class='input-group date' id='toDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
				</div>
				</div>
				</div>
			</div>				
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="add">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="btn-add">Add</button>
	        </div>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>

<div class="modal fade" data-backdrop="static" id="progressModal" style="padding-top: 20%">
    <div class="modal-dialog">
		<div class="progress progress-striped active">
			<div class="progress-bar progress-bar-success" role="progressbar"
				aria-valuenow="40" aria-valuemin="0" aria-valuemax="100"
				style="width: 100%">In progress please wait......</div>
		</div>
	</div>	
</div>

<%-- <div class="modal fade" id="addSubjectToBatchModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Add Subject</h4>
          Click on the batch to add subject to  
        </div>
        <div class="modal-body" id="">
         <div class="panel-group" id="accordion">
				
		  <%List list = (List)request.getAttribute(Constants.BATCH_LIST); 
		  int i = 0;
		  if(null != list){
			  Iterator iteratorList = list.iterator();
			  while(iteratorList.hasNext()){
			  BatchDataClass batchDataClass = (BatchDataClass)iteratorList.next();
		  %>
				<!-- <div class="batchData">
					<button type="button" class="btn btn-danger" data-toggle="collapse"
						data-target=".timingAndTeacher<%=i %>"><%=batchDataClass.getBatchName()%></button>
					<input type="hidden" id=""
						value="'<%=batchDataClass.getBatchCode()%>'" />
					<div class="timingAndTeacher<%=i %> collapse">Collapsible</div>
				</div>
				-->
				<div class="panel panel-default">
				<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#batchTime<%=i%>"><%=batchDataClass.getBatchName()%></a>
							</h4>
						</div>
						<div id="batchTime<%=i%>" class="panel-collapse collapse">
							<div class="panel-body">
								<input type="hidden" id="batchCode" value="'<%=batchDataClass.getBatchCode()%>'" />
								<div class="pull-left">
								<p class="text-warning">Click on add button to add the subject to batch</p>
								</div>
								<div class="pull-right">
								<button type="button" class="btn btn-info" onclick="javascript:addSub2batchDB(this);">Add</button>
								</div>
							</div>
						</div>
						</div>
				<%
				i++;
			  }} %>
				
				</div>
			
		</div>
    </div>
</div>	
</div> --%>



<div class="modal fade" id="addTeacherModal" data-backdrop="static" style="display:none;" >
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Enter Teacher ID</h4>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger"></div>
			<div class="form-group" id="">
				<input type="text"  id="teacherID" name="teacherID"/>
				
				<br>
				<%
				List<Subjects> str=(List<Subjects>)request.getAttribute("subjectList");
				if(str!= null)
				{
				if(str.size()>0){
					int j=0;
					for(j=0;j<str.size();j++){
				%>
				<input type="checkbox" name="subjectsname" value="<%=str.get(j).getSubjectCode()%>" class="chk"><%=str.get(j).getSubjectName() %>
				<%}}} %>
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>
					<div class='input-group date' id='fromDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
					
					<div class="col-sm-6">
					<label for="">End Time</label>
					<div class='input-group date' id='toDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
				</div>
				</div>
				</div>
			</div>				
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="add">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="btn-add">Add</button>
	        </div>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>

	<!-- Modal Box End -->
