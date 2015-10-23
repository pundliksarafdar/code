<html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cx" uri="WEB-INF/customtaglib/switch.tld"%>
<body>
	<style>
.form-group {
	padding: 15px;
}

ul{
	list-style-type: none;
}
</style>
	<script>
		$(document).ready(function() {
			$("input.submit").on("click", function(e) {
				$(".frontenderror").addClass("hide");
				$(".frontenderror").html("");
				
				var hasError = false;
				if($("#lgname").val().trim().length == 0){
					$("#lgname").parents("form-group").addClass("has-error");
					$(".frontenderror").append("Login name can not be empty")
					hasError = true;
				}
				
				if($("#lgpass").val().trim().length == 0){
					$("#lgpass").parents("form-group").addClass("has-error");
					$(".frontenderror").append("<br>Password cannot be empty")
					hasError = true;
				}
				
				if(!hasError){
					$("form").submit();
				}else{
					e.preventDefault();
					$(".frontenderror").removeClass("hide");
				}
			});
			
			$("#showPass").on("mousedown",function(){
				$(this).parents(".input-group").find("input").attr("type","text");
			});
			
			$("#showPass").on("mouseleave",function(){
				$(this).parents(".input-group").find("input").attr("type","password");
			});
		});
	</script>

	<div class="panel panel-default">
		<div class="panel-body">

			<div class="page-header">
				<h1>Class Application</h1>
			</div>
			<s:property value="getText('global.name')" />			
			<div class="row">
				<div class="col-md-4">
					<img src="images/cxlogowhite.jpg" alt="cxlogo" style="height: 100%;width: 100%" class="img-rounded"/>
				</div>

				<div class="col-md-8">
					<div class="well well-lg bs-callout bs-callout-danger">
						<form role="form" action="login" method="post">
							<div class="hide alert alert-danger frontenderror">
									
							</div>
							<s:set var="varMsg" value="%{sessionMessageError}" />
							<s:if test="%{#varMsg!=null}">
							<div class="alert alert-danger">
							<s:property value="varMsg" />
							</div>
							</s:if>
							
							<s:set value="loginBean.loginname" name="usename"></s:set>	
							<s:if test="%{#usename!=null}">
							<s:if test="hasActionErrors()">
								<div class="alert alert-danger">
									<s:actionerror />
								</div>
							</s:if>
							</s:if>
							<input type="hidden" name="loginBean.lastLogin" value="<%=new java.util.Date().getTime()%>"/>
							<s:token></s:token>					
							<div class="form-group">
								<label for="lgname" class="col-sm-4 control-label">*Login
									Name</label>
								<div class="col-sm-8">
									<input name="loginBean.loginname" type="text"
										class="form-control" id="lgname" placeholder="Login name" />
								</div>
							</div>
							<div class="form-group">
								<label for="lgpass" class="col-sm-4 control-label">*Password</label>
								<div class="col-sm-8">
								<div class="input-group">
									<input name="loginBean.loginpass" type="password"
										class="form-control" id="lgpass" placeholder="Password" />
									<span class="input-group-addon" id="showPass"><i class="glyphicon glyphicon-hand-left"></i></span>	
								</div>
								</div>
							</div>

							<div class="btn-group form-group" role="group">
								<input type="submit" class="btn btn-default submit" value="Login" /> <a
									href="register" class="btn btn-default">Register</a>
							</div>
							<div>
								<a href="forgot">Forgot Password</a>
							</div>
						</form>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>
