<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<style>
.danger{
	color:red;
}

.scrollable-menu {
    height: auto;
    max-height: 200px;
    overflow-x: hidden;
}
</style>

<script type="text/javascript">
	$(function () {
		$('[data-toggle="tooltip"]').attr("title",passwordCriteria); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
	})
	var passwordCriteria = "Password should be greater than 5 and less than 20 in length <br> Should contain atleast one special character [!@#$%], one lowercase letter, one uppercase letter and one digit<br> "
	$(document).ready(function(){
		$('#datetimepicker').datetimepicker({
			format : 'YYYY-MM-DD',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		});
		
		$("#fname,#mname,#lname").on("keyup",function(){
			var string = $(this).val();	
			$(this).val(string.charAt(0).toUpperCase() + string.slice(1));
		});
		
		$('#roleValidation').val("");
		$('#phone1,#phone2').keydown(function(e){
		    console.log("loggin :"+e.keyCode+":"+e.which);
		    if(((e.keyCode < 47 || e.keyCode > 57)) && !(e.keyCode == 8 || e.keyCode == 9 || e.keyCode == 46 || e.keyCode ==37 || e.keyCode ==39)){
		    	if(((e.keyCode < 96 || e.keyCode > 105)) && !(e.keyCode == 8 || e.keyCode == 46 || e.keyCode ==37 || e.keyCode ==39)){
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
					   $("#phoneError").show();
				   	}else{
				   		
				   	}				
			});
		}).on("focus",function(){
			   $("#phoneError").hide();
		});
		
		$("#loginname").on("blur",function(){
			allAjax.checkUserNameExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#lgnameError").show();						   					   	
				   	}else{
				   		
				   	}				
			});
		}).on("focus",function(){
			   $("#lgnameError").hide();
		});
		
		$("#email").on("blur",function(){
			allAjax.checkEmailExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#emailError").show();						   					   	
				   	}else{
				   		
				   	}				
			});
		}).on("focus",function(){
			   $("#emailError").hide();
		});
		
		$("input").keyup(function(){
			$(this).parents(".form-group").find('.danger').remove();
			$(this).parents(".form-group").removeClass("has-error");
		});
		
		$("#statebtn").parents(".btn-group").find("li").on("mouseup",function(){
			$("#statebtn").parents(".form-group").find('.danger').remove();
			$('#statebtn').html($(this).text()+'&nbsp;<span class="caret"></span>');
			$('#state').val($(this).text());
			$("#statebtn").focus();
		});
		
		var stateName = $("#state").val();
		if(stateName != "-1"){
			$('#statebtn').html(stateName+'&nbsp;<span class="caret"></span>');
		}
	});
	
	
	var rolestate=1;
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
		$('#mandatoryerror').text("");
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
			$('#mandatoryerror').append("Field marked with the * are mandatory<br>");
			$('#mandatoryerror').show();
			$("#rolebtn").focus();
		}
		return isValid;
	}
	
	
	function addRole(role){
		if(role==0){
			rolestate=1;
			$('#rolebtn').html('Admin <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else if(role==1){
			rolestate=2;
			$('#rolebtn').html('ClassOwner <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
		}else if(role==2){
			rolestate=1;
			$('#rolebtn').html('ClassTeacher <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else if(role==3){
			rolestate=1;
			$('#rolebtn').html('Student <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}else{
			$('#rolebtn').html('Role <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
		}
		$('#roleValidation').val(role);
	}
	
	function go(){
		$("input").parents(".form-group").find('.danger').remove();
		if(validate()){
			$('#regform').submit();
		}else{
			$(document).scrollTop();
		}
	}

	function isPasswordValid(){
		/*
		var ispassvalid = false;
		if($('#loginpass').val()===$('#loginpassre').val()){
			ispassvalid = true;
		}else{
			$('#loginpass').val("");
			$('#loginpassre').val("");
			$('#loginpass').parents('.form-group').addClass('has-error');
			$('#mandatoryerror').append("Reentered password should match<br>");
			$('#mandatoryerror').show();
			ispassvalid = false;
		}*/
			return true;
	}
	
	function validateEmail(sEmail) {
		var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		if (filter.test(sEmail)) {
		return true;
		}
		else {
		return false;
		}
		}
	
	function validateFields(){
		var isValidated = true;
		var regPhoneNumber = /^[0-9]+$/;
		var regStringExpr = /^[a-zA-Z]+$/;
		var regAddressExpr = /^[a-zA-Z0-9 ]+$/;
		var regPasswordExpr = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{5,20}/;
		var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
		var textonly=/^[a-zA-Z]+$/;
			//(^[a-z0-9]+[@._]*[a-z0-9]$){5,20}
		if($('#roleValidation').val()==-1){
			isValidated = false;
			$("#rolebtn").parents(".form-group").prepend("<p class='danger' >Please select role</p>");
			$("#rolebtn").parents(".form-group").addClass("has-error");
			}
		if($("#loginname").val().length<5 || !$("#loginname").val().match(regloginname))
			{
			$("#loginname").parents(".form-group").prepend("<p class='danger' >Username is invalid should be more than 5 character</p>");
			isValidated = false;
			$("#loginname").addClass("has-error");
			}
		if(!$("#loginpass").val().match(regPasswordExpr)){
			$("#loginpass").parents(".form-group").prepend("<p class='danger' >Password is invalid please see popup on left for criteria</p>");
			$('[data-toggle="tooltip"]').tooltip('show');
			isValidated = false;
			$("#loginpass").addClass("has-error");
			
		}	
		
		if($("#loginpass").val()!=$("#loginpassre").val()){
			$("#loginpassre").parents(".form-group").prepend("<p class='danger' >Password is mismatched.</p>");
			isValidated = false;
			$("#loginpassre").addClass("has-error");
		}		
		if(!$("#phone1").val().match(regPhoneNumber)){
			$("#phone1").parents(".form-group").prepend("<p class='danger' >Phone number is invalid. Only Numbers are allowed.</p>");
			isValidated = false;
			$("#phone1").addClass("has-error");
		}
		if($("#phone2").val()!=""){
		if(!$("#phone2").val().match(regPhoneNumber)){
			$("#phone2").parents(".form-group").prepend("<p class='danger' >Phone2 number is invalid. Only Numbers are allowed</p>");
			isValidated = false;
			$("#phone2").addClass("has-error");
		}
		}
		if(!$("#fname").val().match(regStringExpr)){
			$("#fname").parents(".form-group").prepend("<p class='danger' >First Name is invalid. Only A-Z characters are allowed.</p>");
			isValidated = false;
			$("#fname").addClass("has-error");
		}
		if($("#mname").val()!=""){
		if(!$("#mname").val().match(regStringExpr)){
			$("#mname").parents(".form-group").prepend("<p class='danger' >Middle Name is invalid. Only A-Z characters are allowed</p>");
			isValidated = false;
			$("#mname").addClass("has-error");			
		}
		}
		if(!$("#lname").val().match(regStringExpr)){
			$("#lname").parents(".form-group").prepend("<p class='danger' >Last Name is invalid. Only A-Z characters are allowed</p>");
			isValidated = false;
			$("#lname").addClass("has-error");
		}
		if(rolestate==2){
		if($("#classname").val().trim().length < 5 && $("#classname").val().trim().length>20){
			$("#classname").parents(".form-group").prepend("<p class='danger' >Class Name can have minimum 5 and maximum 20 letters</p>");
			isValidated = false;
			$("#classname").addClass("has-error");
		}
		}
		
		
		/*Pundlik Validation for city,state and country*/
		if(!$("#city").val().match(textonly)){
			$("#city").parents(".form-group").prepend("<p class='danger' >City name is invalid. Only A-Z characters are allowed</p>");
			isValidated = false;	
			$("#city").addClass("has-error");
		}
		
		if($("#state").val()=="-1" ){
			$("#state").parents(".form-group").prepend("<p class='danger' >Please Select State</p>");
			isValidated = false;
			$("#state").addClass("has-error");
		}
		
		if(!$("#country").val().match(textonly)){
			$("#country").parents(".form-group").prepend("<p class='danger'>Country name is invalid. Only characters are not allowed. </p>");
			isValidated = false;
			$("#country").addClass("has-error");
		}
		
		if(!validateEmail($("#email").val())){
			$("#email").parents(".form-group").prepend("<p class='danger'>Invalid Email ID. </p>");
			isValidated = false;
			$("#email").addClass("has-error");
		}
		
		$("#rolebtn").focus();
		return isValidated;
	}
	
</script>
<body>

	 <form id="regform" action="/registeruser" method="post" role="form" class="form-horizontal"> 
		<%if(request.getAttribute(Constants.ERROR_MESSAGE) != null){%>
			<div class="alert alert-danger" id="mandatoryerror1">
				<%=request.getAttribute(Constants.ERROR_MESSAGE)%>
			</div>
			<script>
				$("#rolebtn").focus();
			</script>			
		<% }%> 			
	
		<div class="alert alert-danger" id="mandatoryerror" hidden="hidden">
			<ul>				
			</ul>
		</div>
		
		<div class="form-group">
			
			<label for="role"  class="col-sm-4 control-label">Select your role</label>
			<div class="col-sm-5" align="left">
			
			<!-- <input type="hidden" class="form-control" name="registerBean.role" id="role" required="required"/> -->
			<input type="hidden" id="roleValidation" value='<s:property value="registerBean.role" default="-1"/>' required="required" name="registerBean.role" />
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
	    		<input type="text" class="form-control" maxlength="20" name="registerBean.fname" id="fname" required="required" value='<s:property value="registerBean.fname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="mname" class="col-sm-4 control-label">Middle Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" maxlength="20" name="registerBean.mname" id="mname"  value='<s:property value="registerBean.mname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="lname" class="col-sm-4 control-label">*Last Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" maxlength="20" name="registerBean.lname" id="lname" required="required"  value='<s:property value="registerBean.lname" />'/>
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
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="dobfield" name="registerBean.dob" required="required"  readonly value='<s:property value="registerBean.dob" />'/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					
				</div>
		</div>
		<div class="form-group">
			<label for="adr1" class="col-sm-4 control-label">*Address1</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.addr1"  id="addr1" required="required" maxlength="50" value='<s:property value="registerBean.addr1" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="adr2" class="col-sm-4 control-label">Address2</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.addr2"  id="addr2" maxlength="50" value='<s:property value="registerBean.addr2" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="city" class="col-sm-4 control-label">*City</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.city" id="city" required="required" maxlength="20"  value='<s:property value="registerBean.city" />'/>
			</div>
		</div>
		<%-- <div class="form-group">
			<label for="state" class="col-sm-4 control-label">*State</label>
			<div class="col-sm-5">	
				<input type="text" class="form-control" name="registerBean.state" id="state"  required="required"  value='<s:property value="registerBean.state" />'/>
			</div>	
		</div> --%>
		
		<div class="form-group">
			
			<label for="state"  class="col-sm-4 control-label">*State</label>
			<div class="col-sm-5" align="left">
			
			<input type="hidden" class="form-control" name="registerBean.state" id="state" required="required" value='<s:property value="registerBean.state" default="-1"/>'/>
			<input type="hidden"  value='<s:property value="registerBean.state" default="-1"/>' />
			<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" id="statebtn">
						State <span class="caret"></span>
					</button>
					<ul class="dropdown-menu scrollable-menu" role="menu" >
						<li value="Andhra Pradesh"><a href="#">Andhra Pradesh</a></li>
						<li value="Arunachal Pradesh"><a href="#">Arunachal Pradesh</a><li>
						<li value="Assam"><a href="#">Assam</a></li>
						<li value="Bihar"><a href="#">Bihar</a></li>
						<li value="Chhattisgarh"><a href="#">Chhattisgarh</a></li>
						<li value="Goa"><a href="#">Goa</a></li>
						<li value="Gujarat"><a href="#">Gujarat</a></li>
						<li value="Haryana"><a href="#">Haryana</a></li>
						<li value="Himachal Pradesh"><a href="#">Himachal Pradesh</a></li>
						<li value="Jammu and Kashmir"><a href="#">Jammu and Kashmir</a></li>
						<li value="Jharkhand"><a href="#">Jharkhand</a></li>
						<li value="Karnataka"><a href="#">Karnataka</a></li>
						<li value="Kerala"><a href="#">Kerala</a></li>
						<li><a href="#">Madhya Pradesh</a></li>
						<li><a href="#">Maharashtra</a></li>
						<li><a href="#">Manipur</a></li>
						<li><a href="#">Meghalaya</a></li>
						<li><a href="#">Mizoram</a></li>
						<li><a href="#">Nagaland</a></li>
						<li><a href="#">Orissa</a></li>
						<li><a href="#">Punjab</a></li>
						<li><a href="#">Rajasthan</a></li>
						<li><a href="#">Sikkim</a></li>
						<li><a href="#">Tamil Nadu</a></li>
						<li><a href="#">Telangana</a></li>
						<li><a href="#">Tripura</a></li>
						<li><a href="#">Uttar Pradesh</a></li>
						<li><a href="#">Uttarakhand</a></li>
						<li><a href="#">West Bengal</a></li>
					</ul>
			</div>
			</div>
		</div>
		<div class="form-group">
			<label for="country" class="col-sm-4 control-label">*Country</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.country" id="country" required="required"  value='India' readonly="readonly"/>
			</div>
		</div>
		
		<div id="phoneError" style="display: none;color: red;">This Phone number is already registered</div>
		<div class="form-group">
			<label for="phone1" class="col-sm-4 control-label">*Phone 1</label>
			<div class="col-sm-5">
				<input type="tel" class="form-control" name="registerBean.phone1" id="phone1" required="required" maxlength="10" value='<s:property value="registerBean.phone1" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="phone2" class="col-sm-4 control-label">Phone 2</label>
			<div class="col-sm-5">
				<input type="tel" class="form-control" name="registerBean.phone2" id="phone2" maxlength="10" value='<s:property value="registerBean.phone2" />'/>
			</div>	
		</div>
		<div class="form-group" id="divClassname">
			<label for="classname" class="col-sm-4 control-label">*Class Name</label>
			<div class="col-sm-5">	
				<input type="text" maxlength="20" class="form-control" name="registerBean.className" id="classname" required="required"  value='<s:property value="registerBean.className" />'/>
			</div>
		</div>
		
		<div id="emailError" style="display: none;color: red;">This Email id is already registered</div>
		<div class="form-group" id="divemail">
			<label for="email" class="col-sm-4 control-label">*Email ID</label>
			<div class="col-sm-5">	
				<input type="text" maxlength="100" class="form-control" name="registerBean.email" id="email" required="required"  value='<s:property value="registerBean.email" />'/>
			</div>
		</div>
		
		<div id="lgnameError" style="display: none;color: red;">This login name is already registered</div>
		<div class="form-group">
			<label for="loginname" class="col-sm-4 control-label">*Desired Login Name</label>
			<div class="col-sm-5">
				<input type="text" maxlength="20" class="form-control" name="registerBean.loginName" id="loginname" required="required"  value='<s:property value="registerBean.loginName" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="loginpass" class="col-sm-4 control-label">
			<!-- <button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="left" title=""><i class="glyphicon glyphicon-info-sign"></i></button> -->	
			<i class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="bottom" title="" style="color: red;"></i>*Password</label>
			<div class="col-sm-5">
				<div>
					<input type="password" maxlength="20" class="form-control" name="registerBean.loginPass" id="loginpass" required="required" />
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
				<input type="password" maxlength="20" class="form-control" name="registerBean.loginPassRe" id="loginpassre" required="required" />
			</div>
		</div>	
		
		<div class="form-group">
			<label for="submit"  class="col-sm-4 control-label"> </label>
			<div class="col-sm-5">
				<input type="button" onclick="go();" value="Submit" class="btn btn-default"/>
			</div>
		</div>
	
	</form>
</body>
</html>