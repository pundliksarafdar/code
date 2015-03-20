<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
	<body>
	<style>
		input {
			border: 0px;
			background: white;
		}
	</style>	
		<c:set var="register" value="${sessionScope.registerbean}"></c:set>
		<form action="editsave" method="post">		
			<s:if test="hasActionErrors()">
			   <div class="errors">
			      <s:actionerror/>
			   </div>
			</s:if>
			
			<c:if test="${not empty registerBean.loginPass}">
				<div class="alert alert-info" role="alert"><i class="glyphicon glyphicon-paperclip"></i>&nbsp;Password Changed by you, On Submit it will changed</div>
				<input type="hidden" name="registerBean.loginPass" value='<s:property value="registerBean.loginPass" />'/>
			</c:if>
			<table class="table" style="background-color: white;" border="0">
				<tbody>
					<tr>
						<td>First Name</td>
						<td><input type="text" name="registerBean.fname" value='<s:property value="registerBean.fname" />' readonly="readonly"/></td>
						
					</tr>
					<tr>
						<td>Middle Name</td>
						<td><input type="text" name="registerBean.mname" value='<s:property value="registerBean.mname" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="registerBean.lname"  value='<s:property value="registerBean.lname" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>Date of Birth</td>
						<td><input type="text" name="registerBean.dob"  value='<s:property value="registerBean.dob" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>*Address1</td>
						<td><input type="text" name="registerBean.addr1"  value='<s:property value="registerBean.addr1" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>&nbsp;Address2</td>
						<td><input type="text" name="registerBean.addr2"  value='<s:property value="registerBean.addr2" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>*City</td>
						<td><input type="text" name="registerBean.city"  value='<s:property value="registerBean.city" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>*State</td>
						<td><input type="text" name="registerBean.state"  value='<s:property value="registerBean.state" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>*Country</td>
						<td><input type="text" name="registerBean.country"  value='<s:property value="registerBean.country" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>*Phone 1</td>
						<td><input type="text" name="registerBean.phone1"   value='<s:property value="registerBean.phone1" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td>Phone 2</td>
						<td><input type="text" name="registerBean.phone2"   value='<s:property value="registerBean.phone2" />' readonly="readonly"></td>
					</tr>
					
					<c:if test="${not empty registerBean.className && registerBean.role eq 1}">
					<tr>
						<td>Class Name</td>
						<td><input type="text" name="registerBean.className"   value='<s:property value="registerBean.className" />' readonly="readonly"></td>
					</tr>
					</c:if>
					<tr>
						<td>*Desired Login Name</td>
						<td><input type="text" name="registerBean.loginName" id="loginname"  value='<s:property value="registerBean.loginName" />' readonly="readonly"/></td>
					</tr>
					<tr>
						<td><button type="submit" value= "submit" class="btn btn-default">Submit</button></td>
					</tr>
				</tbody>
			</table>
		</form>
	</body>
</html>