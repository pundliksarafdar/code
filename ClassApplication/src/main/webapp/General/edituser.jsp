<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
UserBean userBean = (UserBean)session.getAttribute("user");
%>
<form action="editconfirm" method="post">
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
	<input type="text" name="registerBean.fname" value="<%= userBean.getFirstname()%>"/>
</td></tr>
<tr><td>
	Middle Name
</td>
<td>
	<input type="text" name="registerBean.mname" value="<%= userBean.getMiddlename()%>"/>
</td></tr>
<tr><td>
	Last Name
</td>
<td>
	<input type="text" name="registerBean.lname"  value="<%= userBean.getLastname()%>"/>
</td></tr>
<tr><td>
	Date of Birth
</td>
<td>
	<input type="date" name="registerBean.dob"  value="<%= userBean.getDOB()%>"/>
</td></tr>
<tr><td>
	*Address1
</td>
<td>
	<input type="text" name="registerBean.addr1"  value="<%= userBean.getAddr1()%>"/>
</td></tr>
<tr><td>
	&nbsp;Address1
</td>
<td>
	<input type="text" name="registerBean.addr2" value="<%= userBean.getAddr2()%>"/>
</td></tr>
<tr><td>
	*City
</td>
<td>
	<input type="text" name="registerBean.city" value="<%= userBean.getCity()%>"/>
</td></tr>
<tr><td>
	*State
</td>
<td>
	<input type="text" name="registerBean.state" value="<%= userBean.getState()%>"/>
</td></tr>
<tr><td>
	*Country
</td>
<td>
	<input type="text" name="registerBean.country"  value="<%= userBean.getCountry()%>"/>
</td></tr>
<tr><td>
	*Phone 1
</td>
<td>
	<input type="text" name="registerBean.phone1"  value="<%= userBean.getPhone1()%>"/>
</td></tr>
<tr><td>
	&nbsp;Phone 2
</td>
<td>
	<input type="text" name="registerBean.phone2"  value="<%= userBean.getPhone2()%>"/>
</td></tr>

<tr><td>
	*Desired Login Name
</td>
<td>
	<input type="text" name="registerBean.loginName" id="loginname"  value="<%= userBean.getLoginBean().getLoginname()%>"/>
</td>
</tr>

<tr>
<td>
	*Password
</td>
<td>
	<input type="text" name="registerBean.loginPass" id="loginpass">
</td>
</tr>
<tr>
<td>*Re-Enter Password</td>
<td><input type="text" name="registerBean.loginPassRe" id="loginpassre"/>
</td>
</tr>

<tr><td><input type="submit" value="Submit"></td></tr>
</table>
</form>

<form action="deleteuser">
	<input type="hidden" name="userId" value="<%= userBean.getLoginBean().getLoginname()%>">
	<input type="submit" value="Delete"/>
</form>