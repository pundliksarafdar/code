<%@taglib prefix="s" uri="/struts-tags"%>
<form action="registeruser" method="post">
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
	<input type="text" name="registerBean.fname"/>
</td></tr>
<tr><td>
	Middle Name
</td>
<td>
	<input type="text" name="registerBean.mname"/>
</td></tr>
<tr><td>
	Last Name
</td>
<td>
	<input type="text" name="registerBean.lname"/>
</td></tr>
<tr><td>
	Date of Birth
</td>
<td>
	<input type="date" name="registerBean.dob"/>
</td></tr>
<tr><td>
	*Address1
</td>
<td>
	<input type="text" name="registerBean.addr1"/>
</td></tr>
<tr><td>
	&nbsp;Address1
</td>
<td>
	<input type="text" name="registerBean.addr2"/>
</td></tr>
<tr><td>
	*City
</td>
<td>
	<input type="text" name="registerBean.city"/>
</td></tr>
<tr><td>
	*State
</td>
<td>
	<input type="text" name="registerBean.state"/>
</td></tr>
<tr><td>
	*Country
</td>
<td>
	<input type="text" name="registerBean.country"/>
</td></tr>
<tr><td>
	*Phone 1
</td>
<td>
	<input type="text" name="registerBean.phone1"/>
</td></tr>
<tr><td>
	&nbsp;Phone 2
</td>
<td>
	<input type="text" name="registerBean.phone2"/>
</td></tr>

<tr><td>
	*Class Name
</td>
<td>
	<input type="text" name="registerBean.className" id="loginname"/>
</td>
</tr>

<tr><td>
	*Desired Login Name
</td>
<td>
	<input type="text" name="registerBean.loginName" id="loginname"/>
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
<tr>
<td>
<select name="ROLE">
  <option value="0">Admin</option>
  <option value="1">Class Owner</option>
  <option value="2">Class Teacher</option>
  <option value="3">Student</option>
</select>
</td>
</tr>
<tr><td><input type="submit" value="Submit"></td></tr>
</table>
</form>