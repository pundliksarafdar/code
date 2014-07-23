<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<c:set var="user" value="${sessionScope.user}"></c:set>
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
	<input type="text" name="registerBean.fname" value='<c:out value="${user.firstname}"></c:out>'/>
</td></tr>
<tr><td>
	Middle Name
</td>
<td>
	<input type="text" name="registerBean.mname" value="<c:out value="${user.middlename}"></c:out>"/>
</td></tr>
<tr><td>
	Last Name
</td>
<td>
	<input type="text" name="registerBean.lname"  value="<c:out value="${user.lastname}"></c:out>"/>
</td></tr>
<tr><td>
	Date of Birth
</td>
<td>
	<input type="date" name="registerBean.dob"  value="<c:out value="${user.dob}"></c:out>"/>
</td></tr>
<tr><td>
	*Address1
</td>
<td>
	<input type="text" name="registerBean.addr1"  value="<c:out value="${user.addr1}"></c:out>"/>
</td></tr>
<tr><td>
	&nbsp;Address1
</td>
<td>
	<input type="text" name="registerBean.addr2" value="<c:out value="${user.addr2}"></c:out>"/>
</td></tr>
<tr><td>
	*City
</td>
<td>
	<input type="text" name="registerBean.city" value="<c:out value="${user.city}"></c:out>"/>
</td></tr>
<tr><td>
	*State
</td>
<td>
	<input type="text" name="registerBean.state" value="<c:out value="${user.state}"></c:out>"/>
</td></tr>
<tr><td>
	*Country
</td>
<td>
	<input type="text" name="registerBean.country"  value="<c:out value="${user.country}"></c:out>"/>
</td></tr>
<tr><td>
	*Phone 1
</td>
<td>
	<input type="text" name="registerBean.phone1"  value="<c:out value="${user.phone1}"></c:out>"/>
</td></tr>
<tr><td>
	&nbsp;Phone 2
</td>
<td>
	<input type="text" name="registerBean.phone2"  value="<c:out value="${user.phone2}"></c:out>"/>
</td></tr>

<tr><td>
	*Desired Login Name
</td>
<td>
	<input type="text" name="registerBean.loginName" id="loginname"  value="<c:out value="${user.loginBean.loginname}"></c:out>"/>
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
	<input type="hidden" name="userId" value="<c:out value="${user.loginBean.loginname}"></c:out>">
	<input type="submit" value="Delete"/>
</form>