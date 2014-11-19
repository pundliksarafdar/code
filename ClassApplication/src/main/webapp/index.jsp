
<html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="WEB-INF/customtaglib/switch.tld"%>
<body>
<h2>Hello World!</h2>
<c:Switch switchId="23">
	<div>I am</div>
</c:Switch>
<s:form action="login">
	<s:if test="hasActionErrors()">
   	<div class="errors">
     	 <s:actionerror/>
   	</div>
	</s:if>
	  <s:textfield name="loginBean.loginname" label="Login name" />
 	  <s:password  name="loginBean.loginpass" label="Password" />
	  <s:submit value="Login" ></s:submit>
</s:form>
	<a href="register">Register</a>
</body>
</html>
