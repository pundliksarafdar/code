<html>
<div>
<%
int batchcount=(Integer)session.getAttribute("batchcount"); 
int studentcount=(Integer)session.getAttribute("studentcount");
int teachercount=(Integer)session.getAttribute("teachercount");
%>
<div align="left">
<ul>
<li>No Of batches :- <%=batchcount %></li>
<li>No Of Teachers :- <%=teachercount %></li>
<li>No Of Student :- <%=studentcount %></li>
</ul>
</div>
<img alt="temp" src="..\images\background.jpg" height="400px" width="800px">
</div>
</html>