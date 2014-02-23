<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	RegisterBean registerBean = (RegisterBean)session.getAttribute("registerbean");
%>
<form action="editsave" method="post">

<s:if test="hasActionErrors()">
   <div class="errors">
      <s:actionerror/>
   </div>
</s:if>
<table>
<tr><td>
	First Name
</td>
<td>
	<input type="hidden" name="registerBean.fname" value="<%=registerBean.getFname() %>"/>
	<%=registerBean.getFname() %>	
</td></tr>
<tr><td>
	Middle Name
</td>
<td>
	<input type="hidden" name="registerBean.mname" value="<%=registerBean.getMname() %>"/>
	<%=registerBean.getMname() %>
</td></tr>
<tr><td>
	Last Name
</td>
<td>
	<input type="hidden" name="registerBean.lname"  value="<%=registerBean.getLname() %>"/>
	<%=registerBean.getLname() %>
</td></tr>
<tr><td>
	Date of Birth
</td>
<td>
	<input type="date" name="registerBean.dob"  value="<%=registerBean.getDob() %>"/>
	<%=registerBean.getDob() %>
</td></tr>
<tr><td>
	*Address1
</td>
<td>
	<input type="hidden" name="registerBean.addr1"  value="<%=registerBean.getAddr1() %>"/>
	<%=registerBean.getAddr1() %>
</td></tr>
<tr><td>
	&nbsp;Address2
</td>
<td>
	<input type="hidden" name="registerBean.addr2" value="<%=registerBean.getAddr2() %>"/>
	<%=registerBean.getAddr2() %>
</td></tr>
<tr><td>
	*City
</td>
<td>
	<input type="hidden" name="registerBean.city" value="<%=registerBean.getCity() %>"/>
	<%=registerBean.getCity() %>
</td></tr>
<tr><td>
	*State
</td>
<td>
	<input type="hidden" name="registerBean.state" value="<%=registerBean.getState() %>"/>
	<%=registerBean.getState() %>
</td></tr>
<tr><td>
	*Country
</td>
<td>
	<input type="hidden" name="registerBean.country"  value="<%=registerBean.getCountry() %>"/>
	<%=registerBean.getCountry() %>
</td></tr>
<tr><td>
	*Phone 1
</td>
<td>
	<input type="hidden" name="registerBean.phone1"  value="<%=registerBean.getPhone1() %>"/>
	<%=registerBean.getPhone1() %>
</td></tr>
<tr><td>
	&nbsp;Phone 2
</td>
<td>
	<input type="hidden" name="registerBean.phone2"  value="<%=registerBean.getPhone2() %>"/>
	<%=registerBean.getPhone2() %>
</td></tr>

<tr><td>
	*Desired Login Name
</td>
<td>
	<input type="hidden" name="registerBean.loginName" id="loginname"  value="<%=registerBean.getLoginName() %>"/>
	<%=registerBean.getLoginName() %>
</td>
</tr>

<tr>
</tr>

<tr><td><input type="submit" value="Submit"></td></tr>
</table>
</form>