<html>
<div>
<%
int batchcount=(Integer)session.getAttribute("batchcount"); 
int studentcount=(Integer)session.getAttribute("studentcount");
int teachercount=(Integer)session.getAttribute("teachercount");
%>
<div align="left">

<div class="btn-group-vertical" role="group" aria-label="...">
  <button type="button" class="btn btn-default">No Of batches <span class="badge"><%=batchcount %></span></button>
  <button type="button" class="btn btn-default">No Of Teachers <span class="badge"><%=teachercount %></span></button>
  <button type="button" class="btn btn-default">No Of Student <span class="badge"><%=studentcount %></span></button>
</div>
</div>
<div>
<img alt="temp" src="../images/background.png" height="100%" width="100%">
</div>
</div>
</html>