<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
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
	});
	
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
			$('#rolebtn').html('ClassOwner <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
		}else if(role==2){
			$('#rolebtn').html('ClassTeacher <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else{
			$('#rolebtn').html('Student <span class="caret"></span>');
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
		var regPhoneNumber = new RegExp('^[0-9]+$');
		var regStringExpr = new RegExp('/^[a-zA-Z]+$/');
		var regAddressExpr = new RegExp('\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)');
		var regPasswordExpr = new RegExp('^(?=[^\d_].*?\d)\w(\w|[!@#$%]){5,20}');

		if(!regPasswordExpr.test($("#loginpass").val())){
			isValidated = false;
			$("#loginpass").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Password is invalid.</li>");
		}	
		if(!$("#loginpass").val()==$("#loginpassre").val()){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Password is mismatched.</li>");
		}		
		if(!regPhoneNumber.test($("#phone1").val())){
			isValidated = false;
			$("#phone1").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Phone number is invalid. Only Numbers are allowed.</li>");
		}
		if(!regPhoneNumber.test($("#phone2").val())){
			isValidated = false;
			$("#phone12").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Phone2 number is invalid. Only Numbers are allowed.</li>");
		}
		if(!regStringExpr.test($("#fname").val())){
			isValidated = false;
			$("#fname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>First Name is invalid. Only A-Z characters are allowed. </li>");
		}
		if(!regStringExpr.test($("#mname").val())){
			isValidated = false;
			$("#mname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Middle Name is invalid. Only A-Z characters are allowed. </li>");
		}
		if(!regStringExpr.test($("#lname").val())){
			isValidated = false;
			$("#lname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Last Name is invalid. Only A-Z characters are allowed. </li>");
		}
		if(!regStringExpr.test($("#classname").val())){
			isValidated = false;
			$("#classname").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Class Name is invalid. Only A-Z characters are allowed. </li>");
		}
		if(!regAddressExpr.test($("#addr1").val())){
			isValidated = false;
			$("#addr1").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Address1 is invalid. Special characters are not allowed. </li>");
		}
		if(!regAddressExpr.test($("#addr2").val())){
			isValidated = false;
			$("#addr2").addClass("has-error");
			$('#mandatoryerror ul').append("<li>Address2 is invalid. Special characters are not allowed. </li>");
		}
		
		$('#mandatoryerror').show();
		$("#rolebtn").focus();
		return isValidated;
	}
	
</script>

<form id="regform" action="/registeruser" method="post" role="form" class="form-horizontal">

	<%if(request.getAttribute(Constants.ERROR_MESSAGE) != null){%>
		<div class="alert alert-danger" id="mandatoryerror">
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
		<label for="role"  class="col-sm-2 control-label">Select your role</label>
		<div class="col-sm-10">
		<input type="hidden" class="form-control" name="registerBean.role" id="role" value=""  required="required"/>
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
    	<label for="fname" class="col-sm-2 control-label">*First Name</label>
    	<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.fname" id="fname" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="mname" class="col-sm-2 control-label">Middle Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.mname" id="mname"/>
		</div>
	</div>
	<div class="form-group">
		<label for="lname" class="col-sm-2 control-label">*Last Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.lname" id="lname" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="dob" class="col-sm-2 control-label" >*Date of Birth</label>
		<div class="col-sm-10">
				<!-- 
				<div id="datetimepicker" class="input-append date">
					<input type="text"></input> <span class="add-on icon icon-calendar"> <i
						data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
					</span>
				</div>
				-->
				
				<div id="datetimepicker" class="input-group" style="width :250px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text"  name="registerBean.dob" required="required"/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				
			</div>
	</div>
	<div class="form-group">
		<label for="adr1" class="col-sm-2 control-label">*Address1</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.addr1"  id="adr1" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="adr2" class="col-sm-2 control-label">Address2</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.addr2"  id="adr2"/>
		</div>
	</div>
	<div class="form-group">
		<label for="city" class="col-sm-2 control-label">*City</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.city" id="city" required="required" />
		</div>
	</div>
	
	<div class="form-group">
		<label for="country" class="col-sm-2 control-label">*Country</label>
		<div class="col-sm-10">
		<input type="text" class="form-control" name="registerBean.country" id="country" required="required" /> 
			<%-- <select id="country" name="registerBean.country" class="form-control bfh-countries" data-country="US" required="required"></select> --%>
		</div>
	</div>
	<div class="form-group">
		<label for="state" class="col-sm-2 control-label">*State</label>
		<div class="col-sm-10">	
			<input type="text" class="form-control" name="registerBean.state" id="state"  required="required"/>
			<%-- <select class="form-control bfh-states" data-country="countries_states1" name="registerBean.state" id="state" required="required"></select> --%>
		</div>	
	</div>
	<div class="form-group">
		<label for="phone1" class="col-sm-2 control-label">*Phone 1</label>
		<div class="col-sm-10">
		<%-- <select id="countries_phone1" class="form-control bfh-countries" data-country="US"></select> --%>
		<br><br>
		<!-- <input type="text" class="form-control bfh-phone" data-country="countries_phone1" name="registerBean.phone1" id="phone1" required="required"> -->
			<input type="text" class="form-control bfh-phone" name="registerBean.phone1" id="phone1" required="required" /> 
		</div>
	</div>
	<div class="form-group">
		<label for="phone2" class="col-sm-2 control-label">Phone 2</label>
		<div class="col-sm-10">
			<input type="text" class="form-control bfh-phone" name="registerBean.phone2" id="phone2"/>
		</div>	
	</div>
	<div class="form-group" id="divClassname">
		<label for="classname" class="col-sm-2 control-label">*Class Name</label>
		<div class="col-sm-10">	
			<input type="text" class="form-control" name="registerBean.className" id="classname" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="loginname" class="col-sm-2 control-label">*Desired Login Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.loginName" id="loginname" required="required" />
		</div>
	</div>
		<div class="form-group">
		<label for="passwordCriteria" class="col-sm-12 control-label">*Note :  The length of password must be of 6 to 20 aplhanumeric characters and select special characters. The password also can not start with a digit, underscore or special character and must contain at least one digit.</label>		
	</div>
	<div class="form-group">
		<label for="loginpass" class="col-sm-2 control-label">*Password</label>
		<div class="col-sm-10">
			<input type="password" class="form-control" name="registerBean.loginPass" id="loginpass" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="loginpassre" class="col-sm-2 control-label">*Re-Enter Password</label>
		<div class="col-sm-10">
			<input type="password" class="form-control" name="registerBean.loginPassRe" id="loginpassre" required="required" />
		</div>
	</div>
	
	
	<div class="form-group">
		<label for="submit"  class="col-sm-2 control-label"> </label>
		<div class="col-sm-10">
			<input type="button" onclick="go();" value="Submit" class="btn btn-default"/>
		</div>
	</div>
</fieldset>
</form>
