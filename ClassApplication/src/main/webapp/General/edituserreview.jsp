<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<c:set var="register" value="${sessionScope.registerbean}"></c:set>
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
	<input type="hidden" name="registerBean.fname" value="<c:out value="${register.fname}"></c:out>"/>
</td></tr>
<tr><td>
	Middle Name
</td>
<td>
	<input type="hidden" name="registerBean.mname" value="<c:out value="${register.mname}"></c:out>"/>

</td></tr>
<tr><td>
	Last Name
</td>
<td>
	<input type="hidden" name="registerBean.lname"  value="<c:out value="${register.lname}"></c:out>"/>
</td></tr>
<tr><td>
	Date of Birth
</td>
<td>
	<input type="date" name="registerBean.dob"  value="<c:out value="${register.dob}"></c:out>"/>
</td></tr>
<tr><td>
	*Address1
</td>
<td>
	<input type="hidden" name="registerBean.addr1"  value="<c:out value="${register.addr1}"></c:out>"/>
</td></tr>
<tr><td>
	&nbsp;Address2
</td>
<td>
	<input type="hidden" name="registerBean.addr2" value="<c:out value="${register.addr2}"></c:out>"/>
</td></tr>
<tr><td>
	*City
</td>
<td>
	<input type="hidden" name="registerBean.city" value="<c:out value="${register.city}"></c:out>"/>
</td></tr>
<tr><td>
	*State
</td>
<td>
	<input type="hidden" name="registerBean.state" value="<c:out value="${register.state}"></c:out>"/>
</td></tr>
<tr><td>
	*Country
</td>
<td>
	<input type="hidden" name="registerBean.country"  value="<c:out value="${register.country}"></c:out>"/>
</td></tr>
<tr><td>
	*Phone 1
</td>
<td>
	<input type="hidden" name="registerBean.phone1"  value="<c:out value="${register.phone1}"></c:out>"/>
</td></tr>
<tr><td>
	&nbsp;Phone 2
</td>
<td>
	<input type="hidden" name="registerBean.phone2"  value="<c:out value="${register.phone2}"></c:out>"/>
</td></tr>

<tr><td>
	*Desired Login Name
</td>
<td>
	<input type="hidden" name="registerBean.loginName" id="loginname"  value="<c:out value="${register.loginName}"></c:out>"/>
</td>
</tr>

<tr>
</tr>

<tr><td><input type="submit" value="Submit"></td></tr>
</table>
</form>