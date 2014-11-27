<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<script type="text/javascript">
	$(function () {
		$('[data-toggle="tooltip"]').attr("title",passwordCriteria); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
	})
	var passwordCriteria = "Password should be greater than 5 and less than 20 in length <br> Should contain special atleast on character [!@#$%]"
	$(document).ready(function(){
		$('#role').val("");
		$('#phone1,#phone2').keydown(function(e){
		    console.log("loggin :"+e.keyCode+":"+e.which);
		    if(((e.keyCode < 47 || e.keyCode > 57)) && !(e.keyCode == 8 || e.keyCode == 9 || e.keyCode == 46 || e.keyCode ==37 || e.keyCode ==39)){
		    	if(((e.keyCode < 97 || e.keyCode > 105)) && !(e.keyCode == 8 || e.keyCode == 46 || e.keyCode ==37 || e.keyCode ==39)){
			    	return false;
			    }
		    }else{
		    	return true;
		    }
		});
		addRole($('#roleValidation').val())
		if($("#dobhidden").val()>3){
			$("#dobfield").val($("#dobhidden").val().substring(0,4)+"-"+$("#dobhidden").val().substring(4,6)+"-"+$("#dobhidden").val().substring(6,8));
		}
		
		$("#phone1").on("blur",function(){
			allAjax.checkNumberExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#phone1").focus();
					   alert("Mobile Nuber already registered, Please contact administrator");						   					   	
				   	}else{
				   		
				   	}				
			});
		});
		
		$("#loginname").on("blur",function(){
			allAjax.checkUserNameExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#loginname").focus();
					   alert("User name already registered, Please contact administrator");						   					   	
				   	}else{
				   		
				   	}				
			});
		});
	});
	
	
	var state=1;
	function validate(){
		if(validateBlank()){
			if(isPasswordValid() && validateFields()){
				return true;	
			}else{
				return false;	
			}
		}else{
			isPasswordValid();
			validateFields();
			return false;			
		}		
	}
	
	function validateBlank(){
		$('#mandatoryerror ul').text("");
		var isValid = true;
		$('#mandatoryerror').hide();
		$('input').each(function(){
			$(this).parents('.form-group').removeClass('has-error');
			if($(this).attr('required')=='required'){
				if($(this).val().trim().length==0){
					//alert('field is required');form-control
					$(this).parents('.form-group').addClass('has-error');
					isValid = false;
				}
			}
		});
		
		if(!isValid){
			$('#mandatoryerror ul').append("<li>Field marked with the * are mandatory</li>");
			$('#mandatoryerror').show();
			$("#rolebtn").focus();
		}
		return isValid;
	}
	
	function addRole(role){
		if(role==0){
			$('#rolebtn').html('Admin <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else if(role==1){
			state=2
			$('#rolebtn').html('ClassOwner <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
		}else if(role==2){
			$('#rolebtn').html('ClassTeacher <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else if(role==3){
			$('#rolebtn').html('Student <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else{
			$('#rolebtn').html('Role <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}
		$('#role').val(role);
	}
	
	function go(){
		if(validate()){
			$('#regform').submit();
		}
	}

	function isPasswordValid(){
		var ispassvalid = false;
		if($('#loginpass').val()===$('#loginpassre').val()){
			ispassvalid = true;
		}else{
			$('#loginpass').val("");
			$('#loginpassre').val("");
			$('#loginpass').parents('.form-group').addClass('has-error');
			$('#mandatoryerror ul').append("<li>Reentered password should match</li>");
			$('#mandatoryerror').show();
			ispassvalid = false;
		}
			return ispassvalid;
	}
	
	function validateFields(){
		var isValidated = true;
		var regPhoneNumber = /^[0-9]+$/;
		var regStringExpr = /^[a-zA-Z]+$/;
		var regAddressExpr = /^[a-zA-Z]+$/;
		var regPasswordExpr = /^(?=[^\d_].*?\d)\w(\w|[!@#$%]){5,20}/;
		var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
			//(^[a-z0-9]+[@._]*[a-z0-9]$){5,20}
		if($("#loginname").val().length<5 || !$("#loginname").val().match(regloginname))
			{
			isValidated = false;
			$("#loginname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Username is invalid.</li>");
			}
		if(!$("#loginpass").val().match(regPasswordExpr)){
			isValidated = false;
			$("#loginpass").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Password is invalid</li>");
		}	
		if($("#loginpass").val()==""){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Please Enter Password</li>");
			
		}else if($("#loginpassre").val()==""){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Please Enter Re-Password</li>");	
		}else if(!$("#loginpass").val()==$("#loginpassre").val()){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Password is mismatched.</li>");
		}		
		if(!$("#phone1").val().match(regPhoneNumber)){
			isValidated = false;
			$("#phone1").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Phone number is invalid. Only Numbers are allowed.</li>");
		}
		if($("#phone2").val()!=""){
		if(!$("#phone2").val().match(regPhoneNumber)){
			isValidated = false;
			$("#phone12").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Phone2 number is invalid. Only Numbers are allowed.</li>");
		}
		}
		if(!$("#fname").val().match(regStringExpr)){
			isValidated = false;
			$("#fname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>First Name is invalid. Only A-Z characters are allowed. </li>");
		}
		
		if($("#mname").val()!=""){
		if(!$("#mname").val().match(regStringExpr)){
			isValidated = false;
			$("#mname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Middle Name is invalid. Only A-Z characters are allowed. </li>");
		}
		}
		if(!$("#lname").val().match(regStringExpr)){
			isValidated = false;
			$("#lname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Last Name is invalid. Only A-Z characters are allowed. </li>");
		}
		if(state==2){
		if(!$("#classname").val().match(regStringExpr)){
			isValidated = false;
			$("#classname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Class Name is invalid. Only A-Z characters are allowed. </li>");
		}
		}
		if(!$("#addr1").val().match(regAddressExpr)){
			isValidated = false;
			$("#addr1").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Address1 is invalid. Special characters are not allowed. </li>");
		}
		if($("#addr2").val()!=""){
		if(!$("#addr2").val().match(regAddressExpr)){
			isValidated = false;
			$("#addr2").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Address2 is invalid. Special characters are not allowed. </li>");
		}
		}
		$('#mandatoryerror').show();
		$("#rolebtn").focus();
		return isValidated;
	}
	
</script>
<body>
<br/><br/>
	 <form id="regform" action="/registeruser" method="post" role="form" class="form-horizontal"> 
		<%if(request.getAttribute(Constants.ERROR_MESSAGE) != null){%>
			<div class="alert alert-danger" id="mandatoryerror1">
				<%=request.getAttribute(Constants.ERROR_MESSAGE)%>
			</div>
			<script>
				$("#rolebtn").focus();
			</script>			
		<% }%> 			
	<fieldset>
		<div class="alert alert-danger" id="mandatoryerror" hidden="hidden">
			<ul>				
			</ul>
		</div>
		
		<div class="form-group">
			
			<label for="role"  class="col-sm-4 control-label">Select your role</label>
			<div class="col-sm-5" align="left">
			
			<input type="hidden" class="form-control" name="registerBean.role" id="role" required="required"/>
			<input type="hidden" id="roleValidation" value='<s:property value="registerBean.role" default="-1"/>' />
			<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" id="rolebtn">
						Role <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li><a href="javascript:addRole('0')">Admin</a></li>
						<li><a href="javascript:addRole('1')">Class Owner</a></li>
						<li><a href="javascript:addRole('2')">Class Teacher</a></li>
						<li><a href="javascript:addRole('3')">Student</a></li>
					</ul>
			</div>
			</div>
				<!--<select name="ROLE" id="role">
				  <option value="0">Admin</option>
				  <option value="1">Class Owner</option>
				  <option value="2">Class Teacher</option>
				  <option value="3">Student</option>
				</select>
				-->
		</div>
		
		<div class="form-group">
	    	<label for="fname" class="col-sm-4 control-label">*First Name</label>
	    	<div class="col-sm-5">
	    		<input type="text" class="form-control" name="registerBean.fname" id="fname" required="required" value='<s:property value="registerBean.fname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="mname" class="col-sm-4 control-label">Middle Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.mname" id="mname"  value='<s:property value="registerBean.mname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="lname" class="col-sm-4 control-label">*Last Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.lname" id="lname" required="required"  value='<s:property value="registerBean.lname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="dob" class="col-sm-4 control-label" >*Date of Birth</label>
			<input type="hidden" value='<s:property value="registerBean.dob" />' id="dobhidden">
			<div class="col-sm-5" align="left">
					<!-- 
					<div id="datetimepicker" class="input-append date">
						<input type="text"></input> <span class="add-on icon icon-calendar"> <i
							data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
						</span>
					</div>
					-->
					
					<div id="datetimepicker" class="input-group" style="width :250px;">
						<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
							type="text"  id="dobfield" name="registerBean.dob" required="required"  value='<s:property value="registerBean.dob" />'/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					
				</div>
		</div>
		<div class="form-group">
			<label for="adr1" class="col-sm-4 control-label">*Address1</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.addr1"  id="addr1" required="required"  value='<s:property value="registerBean.addr1" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="adr2" class="col-sm-4 control-label">Address2</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.addr2"  id="addr2"  value='<s:property value="registerBean.addr2" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="city" class="col-sm-4 control-label">*City</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.city" id="city" required="required"  value='<s:property value="registerBean.city" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="state" class="col-sm-4 control-label">*State</label>
			<div class="col-sm-5">	
				<input type="text" class="form-control" name="registerBean.state" id="state"  required="required"  value='<s:property value="registerBean.state" />'/>
			</div>	
		</div>
		<div class="form-group">
			<label for="country" class="col-sm-4 control-label">*Country</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.country" id="country" required="required"  value='<s:property value="registerBean.country" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="phone1" class="col-sm-4 control-label">*Phone 1</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.phone1" id="phone1" required="required"  value='<s:property value="registerBean.phone1" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="phone2" class="col-sm-4 control-label">Phone 2</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.phone2" id="phone2"  value='<s:property value="registerBean.phone2" />'/>
			</div>	
		</div>
		<div class="form-group" id="divClassname">
			<label for="classname" class="col-sm-4 control-label">*Class Name</label>
			<div class="col-sm-5">	
				<input type="text" class="form-control" name="registerBean.className" id="classname" required="required"  value='<s:property value="registerBean.className" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="loginname" class="col-sm-4 control-label">*Desired Login Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.loginName" id="loginname" required="required"  value='<s:property value="registerBean.loginName" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="loginpass" class="col-sm-4 control-label">
			<!-- <button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="left" title=""><i class="glyphicon glyphicon-info-sign"></i></button> -->	
			<i class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="bottom" title="" style="color: red;"></i>*Password</label>
			<div class="col-sm-5">
				<div>
					<input type="password" class="form-control" name="registerBean.loginPass" id="loginpass" required="required" />
				</div>	
				</div>
			</div>
			<!-- 
			<div class="col-sm-5">
				<button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="left" title=""><i class="glyphicon glyphicon-info-sign"></i></button>
			</div>
			 -->
		<div class="form-group">
			<label for="loginpassre" class="col-sm-4 control-label">*Re-Enter Password</label>
			<div class="col-sm-5">
				<input type="password" class="form-control" name="registerBean.loginPassRe" id="loginpassre" required="required" />
			</div>
		</div>	
		
		<div class="form-group">
			<label for="submit"  class="col-sm-4 control-label"> </label>
			<div class="col-sm-5">
				<input type="button" onclick="go();" value="Submit" class="btn btn-default"/>
			</div>
		</div>
	</fieldset>
	</form>
</body>
</html>