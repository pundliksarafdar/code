<%@page import="com.classapp.servicetable.ServiceMap"%>
<%String unblockcontactno = ServiceMap.getSystemParam("2", "unblockcontactno"); %>

<div class="jumbotron">
  <h1>Blocked!</h1>
  <p>
  	Your access is blocked <br>
	Please call your on number <%=unblockcontactno %>
  </p>
  <p><a class="btn btn-primary btn-lg" role="button" href="logout">Logout</a></p>
</div>
