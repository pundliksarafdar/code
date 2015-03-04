<meta http-equiv="cache-control" content="max-age=0" />
<%@page import="com.classapp.db.batch.BatchDetails"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%-- <%@page import="com.classapp.db.subject.ClassSubjects"%> --%>
<%@page import="com.classapp.db.Teacher.TeacherDetails"%>
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.helper.SubjectHelperBean"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.config.Constants"%>
<%@page import="java.util.List"%>
<%@page import="com.classapp.db.subject.Subject" %>
<%@page import="com.classapp.db.batch.Batch" %>
<%@page import="com.classapp.db.batch.division.Division" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://java.sun.com/jstl/core"%>

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
				<%
					UserBean user=(UserBean) request.getSession().getAttribute("user");
				%>
				<jsp:useBean id="subjectHelperBean" class="com.helper.SubjectHelperBean">
				<jsp:setProperty name="subjectHelperBean" property="class_id" value="<%=user.getRegId() %>"/>
				
				<%List<Subject> listOfSubject=subjectHelperBean.getSubjects();
				
				if(listOfSubject.size()>0){
				%>
				Batch Name <input type="text" class="form-control" id="batchName"/>
				<br>
				Division<!-- <input type="text" class="form-control" id="divisionName" name="divisionName"/> -->
				<select class="btn btn-default" name="divisionName" id="divisionName">
					
						<option value="-1">Select Division</option>
						<%
						List<String> divs=(List<String>) request.getAttribute("divisionNames");
						List<String> streams=(List<String>) request.getAttribute("streams");
						List<String> ids=(List<String>) request.getAttribute("batcheids");
						
						int count=0;
						if(divs!=null && streams!=null && ids!=null ){
							for(count=0;count<divs.size();count++){
						%>
										<option value="<%=ids.get(count)%>"><%=divs.get(count)+" "+streams.get(count) %></option>
						<%}} %>
				</select>
				<br/>
				<br/>
				<div class="row">
				<%
				pageContext.setAttribute("sublist", listOfSubject);
				if(listOfSubject!=null){
					for(int i=0;i<listOfSubject.size();i++){
						Subject subject=listOfSubject.get(i);
						%>
						 <div class="col-lg-12">
						<div class="input-group">
      					<span class="input-group-addon">
							<input type="checkbox" class="chkSubject" name="subjectname" id="subjectname" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>"/>
						</span>
							<input type="text" value="<%=subject.getSubjectName()%>" class="form-control" disabled="disabled"/>
						</div>			
						</div>
					<%}}
				}else{%>
				
					<p style="color: red;">Please add subjects first<a href="addsubject">click here</a> to add subject</p> 
				<%	
				}
				%>
				 </div>
				</jsp:useBean>
  					
				</div>				
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
			<%List<Subject> ls=(List<Subject>)pageContext.getAttribute("sublist"); 
			if(ls!=null){
			%>
	        <div class="add">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-addBatch" id="btn-addBatch">Add</button>
	        </div>
	        <%}else{ %>
	       <font color="RED" ><b>Please Add Subjects First</b></font>
	        <%} %>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>

<div class="modal fade" id="modifyStudentModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
       		
        </div>
        <div class="modal-body" id="">
       	<div class="error alert alert-danger"></div>
       	<jsp:useBean id="batchHelperBean" class="com.helper.BatchHelperBean">
		<jsp:setProperty name="batchHelperBean" property="class_id" value="<%=user.getRegId() %>"/>
        	<%
        	StudentDetails studentDetails=(StudentDetails)request.getSession().getAttribute("studentSearchResultBatch");
        	if(studentDetails!=null){
        	
        	List<Batch> currentBatches=studentDetails.getBatches();
        	Batch selectedBatch=currentBatches.get(0);
        	List<Batch> listOfBatches=batchHelperBean.getAllRelatedBatches(selectedBatch.getDiv_id());
			if(listOfBatches!=null){    	
        	
        	listOfBatches.removeAll(currentBatches);
        	for(int i=0;i<listOfBatches.size();i++){
						Batch batch=listOfBatches.get(i);
											
						%>
					<input type="checkbox" class="chkBatch" name="batchId" data-label="<%=batch.getBatch_name() %>" value="<%=batch.getBatch_id() %>"/><%=batch.getBatch_name()%>		
					<%}}%>
					
			
			<%		
			if(currentBatches!=null){
			for(Batch batch: currentBatches){
						%>
						<input type="checkbox" class="chkBatch" name="batchId" data-label="<%=batch.getBatch_name() %>" value="<%=batch.getBatch_id() %>" checked="checked"/><%=batch.getBatch_name()%>		
				
						<%
					}
        	}
        	}
				%>
			</jsp:useBean>
			<div class="form-group">
																			
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
	        <div class="search">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-update" id="btn-update" >Update</button>
	        </div>
	       
	       
      	</div>
      	
    </div>
</div>	
</div>

<div class="modal fade" id="deleteStudentModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
       		
        </div>
        <div class="modal-body" id="">
        	<div class="form-group">Are you sure you want to delete this student from class?</div>
									
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="search">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-delete" id="btn-delete" >Delete</button>
	        </div>
	       
	       
      	</div>
      	
    </div>
</div>	
</div>

<div class="modal fade" id="deleteSelectedStudentModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
       		
        </div>
        <div class="modal-body" id="">
        	<div class="form-group">Are you sure you want to delete selected students from class?</div>
									
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="search">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-delete" id="btn-delete" >Delete</button>
	        </div>
	       
	       
      	</div>
      	
    </div>
</div>	
</div>

<div class="modal fade" id="modifyTeacherModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
 		 </div>
        <div class="modal-body" id="">  
       
        	<jsp:setProperty name="subjectHelperBean" property="class_id" value="<%=user.getRegId() %>"/>
				<% 
				TeacherDetails teacherSearch=(TeacherDetails)request.getSession().getAttribute("teacherSearchResultSubjects");
				
				if(teacherSearch!=null){
				List<Subject> currentAssignedSubjects=teacherSearch.getSubjects();
				List<Subject> listOfSubject=subjectHelperBean.getSubjects();
				if(listOfSubject!=null){
				listOfSubject.removeAll(currentAssignedSubjects);
					for(int i=0;i<listOfSubject.size();i++){
						Subject subject=listOfSubject.get(i);						
						%>
					<input type="checkbox" class="chkSubjectTeacher" name="subjectnameTeacher" id="subjectnameTeacher" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>"/><%=subject.getSubjectName()%>		
					<%}
				}if(currentAssignedSubjects!=null){
					for(int i=0;i<currentAssignedSubjects.size();i++){
						Subject subject=currentAssignedSubjects.get(i);
				%>
				<input type="checkbox" class="chkSubjectTeacher" name="subjectnameTeacher" id="subjectnameTeacher" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>" checked="checked"/><%=subject.getSubjectName()%>		
					<%}
					}
					
					}%>
        	
			<div class="form-group">
																	
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
	        <div class="search">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-update" id="btn-update">Update</button>
	        </div>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>
<div class="modal fade" id="deleteTeacherModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
       		
        </div>
        <div class="modal-body" id="">
        	<div class="form-group">Are you sure you want to delete this teacher from class?</div>
									
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="delete">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-deleteTeacher" id="btn-deleteTeacher" >Delete</button>
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
			<input type="text" class="form-control" id="subjectName" name="subjectName" placeholder="Enter Subject Name" maxlength="100">
				<!-- <input type="text"  id="subjectName" name="subjectName"/> -->
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
		<%-- 		
		  <%List list = (List)request.getAttribute(Constants.BATCH_LIST); 
		  int i = 0;
		  if(null != list){
			  Iterator iteratorList = list.iterator();
			  while(iteratorList.hasNext()){
			  BatchDataClass batchDataClass = (BatchDataClass)iteratorList.next();
		  %> 
		  <c:set var="i">0</c:set>
		  	<c:forEach items="'${requestScope.batchList}'" var="list">
   				<c:set var="batchDataClass">${list}</c:set>				
				<div class="panel panel-default">
				<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#batchTime'${i}'">${batchDataClass.batchName}</a>
							</h4>
						</div>
						<div id="batchTime'${i}'" class="panel-collapse collapse">
							<div class="panel-body">
								<input type="hidden" id="batchCode" value="'${batchDataClass.batchCode}'" />
								<div class="pull-left">
								<p class="text-warning">Click on add button to add the subject to batch</p>
								</div>
								<div class="pull-right">
								<button type="button" class="btn btn-info" onclick="javascript:addSub2batchDB(this);">Add</button>
								</div>
							</div>
						</div>
						</div>
						<c:set var="i" value="${i+1}"></c:set>
				</c:forEach>
				</div>
			
		</div>
    </div>
</div>	
</div>
<div class="modal fade" id="addTeacherModal" data-backdrop="static" style="display:none;" >
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Enter Teacher Login name</h4>
        </div>
        <div class="modal-body" id="">  
        	<div class="error alert alert-danger"></div>      
			<div class="form-group" id="">
				<input type="text"  id="teacherLoginName" name="teacherLoginName"/>
				
				<br>
				<%-- <%
				List<Subjects> str=(List<Subjects>)request.getAttribute("subjectList");
				if(str!= null)
				{
				if(str.size()>0){
					int j=0;
					for(j=0;j<str.size();j++){
				%>
				<input type="checkbox" name="subjectsname" value="<%=str.get(j).getSubjectCode()%>" class="chk"><%=str.get(j).getSubjectName() %>
				<%}}} %> 
				<% List<Subject> listOfSubject=subjectHelperBean.getSubjects();
				
					for(int i=0;i<listOfSubject.size();i++){
						Subject subject=listOfSubject.get(i);						
						%>
					<input type="checkbox" class="chkSubjectAddTeacher" name="subjectnameAddTeacher" id="subjectnameAddTeacher" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>"/><%=subject.getSubjectName()%>		
					<%}%>
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
	        <button type="button" class="btn btn-primary btn-addTeacher" id="btn-addTeacher">Add</button>
	        </div>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>--%>
<div class="modal fade" id="modifyBatchModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
       		
        </div>
        <div class="modal-body" id="">
             	
        	<%BatchDetails batchDetails=(BatchDetails)request.getSession().getAttribute("batchSearchResultBatch");
        	if(batchDetails!=null){
        	List<Subject> listOfSubjects=subjectHelperBean.getSubjects();
        	List<Subject> currentSubjects=batchDetails.getSubjects();
        	if(listOfSubjects!=null){
        	listOfSubjects.removeAll(currentSubjects);
        	for(int i=0;i<listOfSubjects.size();i++){
						Subject subject=listOfSubjects.get(i);
											
						%>
					<input type="checkbox" class="chkSubjectBatch" name="subjectBatchId" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>"/><%= subject.getSubjectName()%>		
				<%	}
					}%>
					
			
			<%	if(currentSubjects!=null){	
			for(Subject subject: currentSubjects){
						%>
						<input type="checkbox" class="chkSubjectBatch" name="subjectBatchId" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>" checked="checked"/><%=subject.getSubjectName()%>		
				
						<%
					}
			}
        	}
				%>
			
			<div class="form-group">
																			
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
	        <div class="search">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-updateBatch" id="btn-updateBatch" >Update</button>
	        </div>
	       
	       
      	</div>
      	
    </div>
</div>	
</div>
<div class="modal fade" id="deleteBatchModal" data-backdrop="static">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
       		
        </div>
        <div class="modal-body" id="">
        	<div class="form-group">Are you sure you want to delete this batch from class?</div>
									
			</div>
      	<div class="modal-footer">
	        <div class="progress progress-striped active hide">
					<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="45"
						aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						Processing your request Please wait
					</div>
			</div>
	        <div class="delete">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-deleteBatch" id="btn-deleteBatch" >Delete</button>
	        </div>
      	</div>
    </div>
</div>	
</div>

<div class="modal fade" id="addclassModal" data-backdrop="static" style="display:none;" >
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Add Class</h4>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger"></div>
			<div class="form-group" id="">
				Enter Class:- <input type="text" class="form-control" id="classname" placeholder="Enter Class Name" name="classname" maxlength="50">
				<!-- <input type="text"  id="classname" name="classname"/></br> -->
				Enter Stream/Part:-<input type="text" class="form-control" id="stream" placeholder="Enter Stream/Part" name="stream" maxlength="50">
				<!-- <input type="text"  id="stream" name="stream"/> -->
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
			 <input type="tel" class="form-control" id="teacherID" placeholder="Enter Teacher ID" name="teacherID"><br>
			 <input type="text" class="form-control" id="suffix" placeholder="Suffix" name="suffix" maxlength="5" rel="tooltip" title="This is optional field.You can use this field when you have more than one teachers with same name. eg Amitab Bachhan JR. ">
				<br>
				
				<jsp:setProperty name="subjectHelperBean" property="class_id" value="<%=user.getRegId() %>"/>
				<%List<Subject> listOfSubject=subjectHelperBean.getSubjects();
				if(listOfSubject!=null)
				{
					for(int i=0;i<listOfSubject.size();i++){
						Subject subject=listOfSubject.get(i);
						%>
					<input type="checkbox" class="chk" name="subjectname" id="subjectname" data-label="<%=subject.getSubjectName() %>" value="<%=subject.getSubjectId() %>"/><%=subject.getSubjectName()%>		
					<%}
				}
				%>
				 
				
				
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
			<%if(ls!=null){ %>
	        <div class="add">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="btn-add">Add</button>
	        </div>
	        <%}else{ %>
	        <font color="RED" ><b>Please Add Subjects First</b></font>
	        <%} %>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>

<div class="modal fade" id="ModifysubjectModal" data-backdrop="static" style="display:none;" >
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Edit Subject</h4>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger" hidden="true"></div>
			<div class="form-group" id="">
		
			 <input class="form-control" type="text" id="editsubject" maxlength="100">
				<input type="hidden" id="hidsubid">
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>`
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
	        <button type="button" class="btn btn-primary btn-add" id="savesubject">Save</button>
	        </div>
	        <div class="setTimming hide">
	        <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Not Now</button>
	        <button type="button" class="btn btn-primary btn-setTimming" id="btn-setTimming">Done</button>
	        </div>
      	</div>
    </div>
</div>	
</div>
<div class="modal fade" id="progressGifModal" data-backdrop="static"
	style="display: none;">
	<div class="modal-dialog">
		<div class="modal-content">
			<img src="images/ajax-loader.gif">
		</div>
	</div>
</div>

<div class="modal fade" id="aboutUsModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">x</button>
				<h4 class="modal-title" id="myModalLabel">About Us</h4>
			</div>
			<div class="modal-body">
								
					<ul id="myTab" class="nav nav-tabs  nav-justified" role="tablist">
						<li role="presentation" class="active"><a href="#home"
							id="home-tab" role="tab" data-toggle="tab" aria-controls="home"
							aria-expanded="true">CoreX</a></li>
						<li role="presentation" class=""><a href="#teachers"
							role="tab" id="profile-tab" data-toggle="tab"
							aria-controls="profile" aria-expanded="false">Teacher</a></li>
						<li role="presentation" class=""><a href="#student"
							role="tab" id="profile-tab" data-toggle="tab"
							aria-controls="profile" aria-expanded="false">Students</a></li>
						
					</ul>
					<div id="myTabContent" class="tab-content">
						<div role="tabpanel" class="tab-pane fade active in" id="home"
							aria-labelledby="home-tab">
							<br>
							Corex...<br> Right to success<br>

							<p>Our mission is to provide a complete interactive and
								innovative package to play around notes, homework, exam for the
								first time on the planet. We at Corex provide best platform to
								study online with ur smartphones or PC. Corex will change the
								future of learning with the technology & will bring about a
								complete transformation to tuition system. Our goal is to
								provide various program and services to students, teachers,
								administrators and parents under one roof. Corex help teachers
								and students to communicate more conveniently. Our purpose is to
								create more opportunities to learn to improve academic
								performance of the students.</p>
							
						</div>
						<div role="tabpanel" class="tab-pane fade" id="teachers"
							aria-labelledby="profile-tab">
							<br>
							<p>For teachers/ class owners: Corex help you to reach
								students all over the world. You can create course, set
								syllabus, share or upload notes, set a practice test in a very
								short time with ease. Teachers can upload notes in document
								format, presentations, audio notes, videos, animation for better
								understanding of concepts. Set timetable of complete course or
								year, send alerts/ notifications / change in schedule with
								calender. Corex also enables teachers to instantly assess and
								evaluate the students performance.</p>

							Features:
							<ul>
								<li>Manage access of the all students across batches and
									tuituions</li>
								<li>Manage access to exams notes and time table for each
									batch</li>
								<li>Upload notes in PDF, doc or ppt format</li>
								<li>Upload Exams in Excel format</li>
								<li>View the exams, and slive it.</li>
								<li>View the students result and send message to there
									registered device</li>
								<li>Manage timetable for batch</li>
								<li>Display your special events to students across the
									batches</li>
								<li>Dispaly ads to targeted students depend on there ages,
									education, region and gender</li>
							</ul>

						</div>
						<div role="tabpanel" class="tab-pane fade" id="student"
							aria-labelledby="dropdown1-tab">
							<br>
							<p>Learn with fun...for students</p>
							<br>
							<p>With Corex students can find unlimited options of classes
								with top names in market. Corex is one of the easy way to get
								expert created notes. Students can attend any class of their
								choise from any place. Each class with there unique features,
								study material, notes and practice tests to help students crack
								or excel in exam.</p>
							<br>
							<p>Corex will help you to create your timetable managing your
								school, college or ofice. Students can add reminder or task to
								their timetable.Corex will keep track of your progress and help
								you to work on weak points. You can share you results with
								social connectivity.</p>
							<br> Features:
							<ul>
								<li>Register to class with easy steps</li>
								<li>One time log in</li>
								<li>Manage your class items like notes exam and result</li>
								<li>Able to add own notes over provided one</li>
								<li>Apply for exams</li>
								<li>Reappear for exam and get the answers for the solved
									one</li>
								<li>Students can sync timetable across multiple devices</li>
								<li>Access your tuition over desktop or mobile</li>
								<li>View notes offline</li>
								<li>View results in graphical format</li>
								<li>Apply for special event through mobile app</li>
							</ul>
						</div>
						
					</div>
				
			</div>
		</div>
	</div>
</div>

<!-- Pundlik Contact us modal -->
<div class="modal fade" id="contactUsModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">x</button>
				<h4 class="modal-title" id="myModalLabel">Contact Us</h4>
			</div>
			<div class="modal-body">
			<div class="container">
			<div class="col-sm-5">
			<blockquote>Thank you for your interest in us. We look forward to your suggestions and feedback. If you have any query you can contact us.
			<br><img alt="" src="/images/contact.jpg" width="50px">  9766120685</blockquote>
			</div>
			
			</div>
			<div class="container">
			<h5><strong>Suggestion/Feedback:</strong></h5><br>
			<form class="form-horizontal" role="form" action="javascript:void(0);" id="form">
		<div class="form-group has-error">
			<label for="Name" class="col-sm-2 control-label">Name : </label>
			<div class="col-sm-3">
				<input type="text" class="form-control" name="Name" placeholder="Enter your Name" id="feedbackname"/><br>
				<span name="namespan" id="namespan"></span>
			</div>
		</div>
		<div class="form-group has-error">
			<label for="Email" class="col-sm-2 control-label">Email : </label>
			<div class="col-sm-3">
				<input type="text" class="form-control" name="Email" placeholder="Enter your Email" id="feedbackemail"/><br>
				<span id="emailspan"></span>
			</div>
		</div>
		<div class="form-group has-error">
			<label for="comments" class="col-sm-2 control-label">Comments : </label>
			<div class="col-sm-3">
				<textarea type="text" class="form-control" name="comments" placeholder="Enter comments here" maxlength="100" id="comment"></textarea>
				<span id="commentspan"></span>
			</div>
		</div>
		<div class="col-sm-10 col-sm-offset-2">
			<input type="submit" class="btn btn-success" value="Submit" id="submitfeedback"/>
		</div>
	</form>
	<div id="feedbacksuccess" style="display: none;">Thanks For Your Valuable Suggestion/Feedback.</div>
			</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="ModifyClassModal" data-backdrop="static" style="display:none;" >
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Edit Class</h4>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger" hidden="true"></div>
			<div class="form-group" id="">
		
			 <input class="form-control" type="text" id="editclassname" maxlength="50">
			 <br>
			 <input class="form-control" type="text" id="editstream" maxlength="50">
				<input type="hidden" id="hidclassid">
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>`
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
	        <button type="button" class="btn btn-primary btn-add" id="saveclass">Save</button>
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

