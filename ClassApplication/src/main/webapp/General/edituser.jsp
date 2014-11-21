<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<body>
<c:set var="user" value="${sessionScope.user}"></c:set>
<form id="frEditUser" class="form-horizontal" role="form" action="editconfirm" method="post">
	<s:if test="hasActionErrors()">
		<div class="errors">
			<s:actionerror/>
		</div>
	</s:if>

	 	<div class="form-group">
    		<label for="inputFirstName" class="col-sm-4 control-label">First Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputFirstName" name="registerBean.fname" value="<c:out value="${user.firstname}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputMiddleName" class="col-sm-4 control-label">Middle Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputMiddleName" name="registerBean.mname" value="<c:out value="${user.middlename}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputLastName" class="col-sm-4 control-label">Last Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputLastName" name="registerBean.lname"  value="<c:out value="${user.lastname}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputDBO" class="col-sm-4 control-label">Date of Birth</label>
    		<div class="col-sm-5">
				<input type="date" class="form-control" id="inputDBO" name="registerBean.dob"  value="<c:out value="${user.dob}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputAdd1" class="col-sm-4 control-label">Address1</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputAdd1" name="registerBean.addr1"  value="<c:out value="${user.addr1}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputAdd2" class="col-sm-4 control-label">Address2</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputAdd2" name="registerBean.addr2"  value="<c:out value="${user.addr2}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputCity" class="col-sm-4 control-label">City</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputCity" name="registerBean.city" value="<c:out value="${user.city}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputState" class="col-sm-4 control-label">State</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputState" name="registerBean.state" value="<c:out value="${user.state}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputCountry" class="col-sm-4 control-label">Country</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputCountry" name="registerBean.country"  value="<c:out value="${user.country}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputPhone1" class="col-sm-4 control-label">Phone 1</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputPhone1" name="registerBean.phone1"  value="<c:out value="${user.phone1}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputPhone2" class="col-sm-4 control-label">Phone 2</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputPhone2" name="registerBean.phone2"  value="<c:out value="${user.phone2}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputLoginName" class="col-sm-4 control-label">Desired Login Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="inputLoginName" name="registerBean.loginName" value="<c:out value="${user.loginBean.loginname}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="loginpass" class="col-sm-4 control-label">Password</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="loginpass" name="registerBean.loginPass"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="loginpassre" class="col-sm-4 control-label">Re-Enter Password</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="loginpassre" name="registerBean.loginPassRe"/>
			</div>
		</div>
		
		 <div class="form-group">
		    <div class="col-sm-offset-4 col-sm-5">		      
		      	<button type="submit" value= "submit" class="btn btn-default">Submit</button>				
			</div>
		</div>  		
	</form>
	<form id="frDeleteUser" class="form-horizontal" role="form" action="deleteuser" method="post">
		<!--  <form action="deleteuser">-->
		<input type="hidden" name="userId" value="<c:out value="${user.loginBean.loginname}"></c:out>">
		<div class="form-group">
			<div class="col-sm-offset-4 col-sm-5">
				<button type="submit" value= "Delete" class="btn btn-default">Delete</button>
				<!--  <input type="submit" value="Delete"/>-->
			 </div>
		</div> 
	</form>
</body>
</html>