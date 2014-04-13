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
		
	});
</script>
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
	  String timmingsTitle = "Start time :"+ batchDataClass.getTimmings().getStartTimming()+"<br>End Time :"+batchDataClass.getTimmings().getEndTimming(); 
  %>
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a title="<%=timmingsTitle %>" class="batchName" data-toggle="collapse" data-parent="#accordion" href="#batchItem<%=i %>">
          <%= batchDataClass.getBatchName()%>
        </a>
      <span class="badge pull-right"><%=batchDataClass.getCandidatesInBatch() %></span>
      </h4>
    </div>
    <div id="batchItem<%=i %>" class="panel-collapse collapse">
      <div class="panel-body">
        Subject
      </div>
    </div>
  </div>
  <%
  i++;
	  } 		
 } %>
</div>