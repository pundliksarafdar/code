<%@page import="com.datalayer.subject.Subject"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<script type="text/javascript" src="js/AddSubject.js"></script>
<script>
	$(document).ready(function(){
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
<div>
	<span class="btn btn-info pull-right addsubject2batch" data-toggle="popover" title="Drag and drop subject to respective batch"><i class="glyphicon glyphicon-plus"></i>&nbsp;Subjects</span>
</div>
<div class="btn-group btn-group-sm">
  <button type="button" class="btn btn-info" data-target="#addBatchModal"  data-toggle="modal">Add Batch</button>
  <button type="button" class="btn btn-info" data-target="#addSubjectModal" data-toggle="modal">Add Subject</button>
  <button type="button" class="btn btn-info" >Add Student</button>
</div>
<br><br>
<div class="panel-group" id="accordion">
  <%List list = (List)request.getAttribute(Constants.BATCH_LIST); 
  int i = 0;
  if(null != list){
	  Iterator iteratorList = list.iterator();
	  while(iteratorList.hasNext()){
	  BatchDataClass batchDataClass = (BatchDataClass)iteratorList.next();
	  //String timmingsTitle = "Start time :"+ batchDataClass.getTimmings().getStartTimming()+"<br>End Time :"+batchDataClass.getTimmings().getEndTimming(); 
  		String timmingsTitle = "Start time :";
  %>
  
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <i class="glyphicon glyphicon-trash" title="Delete Batch" onclick="deleteBatch('<%= batchDataClass.getBatchName()%>')"></i>&nbsp;
        <a class="batchName" data-toggle="collapse" data-parent="#accordion" href="#batchItem<%=i %>">
          <%= batchDataClass.getBatchName()%>
        </a>
      <span class="badge pull-right"><%=batchDataClass.getCandidatesInBatch() %></span>
      </h4>
    </div>
    <div id="batchItem<%=i %>" class="panel-collapse collapse">
      <div class="popoverContainer<%=batchDataClass.getCandidatesInBatch() %>"></div>
      <div class="panel-body">
      			<button type="button" class="btn btn-default" data-container="body" popovername="'<%=batchDataClass.getBatchName()%>'"
					data-toggle="popover" data-placement="right">
					Popover on right</button>
	  </div>
    </div>
  </div>
  <%
  i++;
	  } 		
 } %>
 
</div>

<div class="hide">
	<div id="allSubject">
	<%List<Subject> allSubjects = (List<Subject>)request.getAttribute(Constants.SUBJECT_LIST);
		if(null!=allSubjects){
			Iterator iteratorSubject = allSubjects.iterator();
		while(iteratorSubject.hasNext()){
			Subject subject = (Subject)iteratorSubject.next();
		%>
			<span class="btn btn-info" style="width: 100%;margin: 2px;"><%=subject.getSubjectName() %></span>
		<%}	  
		}
	%>
	
	</div>
</div>