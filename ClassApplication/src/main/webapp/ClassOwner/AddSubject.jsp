<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<script>
	$(document).ready(function(){
		$('.batchName').tooltip({'placement':'right','html':'true'}).on('click',function(){
			$(this).tooltip('hide');
		});
		
	});
</script>
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