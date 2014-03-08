<%@page import="com.classapp.servicetable.ServiceMap"%>
<%String regcallcontact = ServiceMap.getSystemParam("2", "regcontactno"); %>

<div class="jumbotron">
  <h1>Registered! But not accepted</h1>
  <p>
  	You are successfully registered but yor profile is not accepted <br>
	Please call your on number <%=regcallcontact %>
  </p>
  <p><a class="btn btn-primary btn-lg" role="button" href="logout">Logout</a></p>
</div>
