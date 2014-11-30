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
		$('#datetimepicker').datetimepicker({
			format : 'YYYY-MM-DD',
			pickTime : false,
			maxDate:((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()
		});
		
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
	
	function addState(state){
		if(state==0){
			$('#statebtn').html('Andhra Pradesh <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Andhra Pradesh');
		}else if(state==1){
			$('#statebtn').html('Arunachal Pradesh <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Arunachal Pradesh');
		}else if(state==2){
			$('#statebtn').html('Assam <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Assam');
		}else if(state==3){
			$('#statebtn').html('Bihar <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Bihar');
		}else if(state==4){
			$('#statebtn').html('Chhattisgarh <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Chhattisgarh');
		}else if(state==5){
			$('#statebtn').html('Goa <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Goa');
		}else if(state==6){
			$('#statebtn').html('Gujarat <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Gujarat');
		}else if(state==7){
			$('#statebtn').html('Haryana <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Haryana');
		}else if(state==8){
			$('#statebtn').html('Himachal Pradesh <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Himachal Pradesh');
		}else if(state==9){
			$('#statebtn').html('Jammu and Kashmir <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Jammu and Kashmir');
		}else if(state==10){
			$('#statebtn').html('Jharkhand <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Jharkhand');
		}else if(state==11){
			$('#statebtn').html('Karnataka <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Karnataka');
		}else if(state==12){
			$('#statebtn').html('Kerala <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Kerala');
		}else if(state==13){
			$('#statebtn').html('Madhya Pradesh <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Madhya Pradesh');
		}else if(state==14){
			$('#statebtn').html('Maharashtra <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Maharashtra');
		}else if(state==15){
			$('#statebtn').html('Manipur <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Manipur');
		}else if(state==16){
			$('#statebtn').html('Meghalaya <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Meghalaya');
		}else if(state==17){
			$('#statebtn').html('Mizoram <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Mizoram');
		}else if(state==18){
			$('#statebtn').html('Nagaland <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Nagaland');
		}else if(state==19){
			$('#statebtn').html('Orissa <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Orissa');
		}else if(state==20){
			$('#statebtn').html('Punjab <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Punjab');
		}else if(state==21){
			$('#statebtn').html('Rajasthan <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Rajasthan');
		}else if(state==22){
			$('#statebtn').html('Sikkim <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Sikkim');
		}else if(state==23){
			$('#statebtn').html('Tamil Nadu <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Tamil Nadu');
		}else if(state==24){
			$('#statebtn').html('Telangana <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Telangana');
		}else if(state==25){
			$('#statebtn').html('Tripura <span class="caret"></span>');
			$('#divClassname').show();
			$('#divClassname').find('input').attr("required","required");
			$('#state').val('Tripura');
		}else if(state==26){
			$('#statebtn').html('Uttar Pradesh <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Uttar Pradesh');
		}else if(state==27){
			$('#statebtn').html('Uttarakhand <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('Uttarakhand');
		}else if(state==28){
			$('#statebtn').html('West Bengal <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('West Bengal');
		}else{
			$('#statebtn').html('state <span class="caret"></span>');
			$('#divClassname').hide();
			$('#divClassname').find('input').removeAttr("required");
			$('#state').val('State');
		}
		
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
		$('#role').val(role);
	}
	
	function go(){
		if(validate()){
			$('#regform').submit();
		}else{
			$(document).scrollTop();
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
			$('#mandatoryerror').append("Reentered password should match<br>");
			$('#mandatoryerror').show();
			ispassvalid = false;
		}
			return ispassvalid;
	}
	
	function validateFields(){
		var isValidated = true;
		var regPhoneNumber = /^[0-9]+$/;
		var regStringExpr = /^[a-zA-Z]+$/;
		var regAddressExpr = /^[a-zA-Z0-9 ]+$/;
		var regPasswordExpr = /^(?=[^\d_].*?\d)\w(\w|[!@#$%]){5,20}/;
		var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
		var textonly=/^[a-zA-Z]+$/;
			//(^[a-z0-9]+[@._]*[a-z0-9]$){5,20}
		if($("#role").val()==-1){
			$("#role").parents(".form-group").addClass("has-error");
			$('#mandatoryerror').append("Please select role.<br>");
		}
		if($("#loginname").val().length<5 || !$("#loginname").val().match(regloginname))
			{
			isValidated = false;
			$("#loginname").addClass("has-error");
			$('#mandatoryerror').append("Username is invalid.<br>");
			}
		if(!$("#loginpass").val().match(regPasswordExpr)){
			isValidated = false;
			$("#loginpass").addClass("has-error");
			$('#mandatoryerror').append("Password is invalid<br>");
		}	
		if($("#loginpass").val()==""){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror').append("Please Enter Password<br>");
			
		}else if($("#loginpassre").val()==""){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror').append("Please Enter Re-Password<br>");	
		}else if(!$("#loginpass").val()==$("#loginpassre").val()){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror').append("Password is mismatched.<br>");
		}		
		if(!$("#phone1").val().match(regPhoneNumber)){
			isValidated = false;
			$("#phone1").addClass("has-error");
			$('#mandatoryerror').append("Phone number is invalid. Only Numbers are allowed.<br>");
		}
		if($("#phone2").val()!=""){
		if(!$("#phone2").val().match(regPhoneNumber)){
			isValidated = false;
			$("#phone12").addClass("has-error");
			$('#mandatoryerror').append("Phone2 number is invalid. Only Numbers are allowed.<br>");
		}
		}
		if(!$("#fname").val().match(regStringExpr)){
			isValidated = false;
			$("#fname").addClass("has-error");
			$('#mandatoryerror').append("First Name is invalid. Only A-Z characters are allowed. <br>");
		}
		
		if($("#mname").val()!=""){
		if(!$("#mname").val().match(regStringExpr)){
			isValidated = false;
			$("#mname").addClass("has-error");
			$('#mandatoryerror').append("Middle Name is invalid. Only A-Z characters are allowed. <br>");
		}
		}
		if(!$("#lname").val().match(regStringExpr)){
			isValidated = false;
			$("#lname").addClass("has-error");
			$('#mandatoryerror').append("Last Name is invalid. Only A-Z characters are allowed. <br>");
		}
		if(rolestate==2){
		if(!$("#classname").val().match(regStringExpr)){
			isValidated = false;
			$("#classname").addClass("has-error");
			$('#mandatoryerror').append("Class Name is invalid. Only A-Z characters are allowed. <br>");
		}
		}
		if(!$("#addr1").val().match(regAddressExpr)){
			isValidated = false;
			$("#addr1").addClass("has-error");
			$('#mandatoryerror').append("Address1 is invalid. Special characters are not allowed. <br>");
		}
		if($("#addr2").val()!=""){
		if(!$("#addr2").val().match(regAddressExpr)){
			isValidated = false;
			$("#addr2").addClass("has-error");
			$('#mandatoryerror').append("Address2 is invalid. Special characters are not allowed. <br>");
		}
		}
		
		/*Pundlik Validation for city,state and country*/
		if(!$("#city").val().match(textonly)){
			isValidated = false;
			$("#city").addClass("has-error");
			$('#mandatoryerror').append("City name is invalid. Only characters are not allowed. <br>");
		}
		alert($("#state").val());
		if($("#state").val()=="State" ){
			isValidated = false;
			$("#state").addClass("has-error");
			$('#mandatoryerror').append("State name is invalid. Only characters are not allowed. <br>");
		}
		alert($("#country").val());
		if(!$("#country").val().match(textonly)){
			isValidated = false;
			$("#country").addClass("has-error");
			$('#mandatoryerror').append("Country name is invalid. Only characters are not allowed. <br>");
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
							type="text"  id="dobfield" name="registerBean.dob" required="required"  readonly value='<s:property value="registerBean.dob" />'/> <span class="input-group-addon add-on"> <i
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
		<%-- <div class="form-group">
			<label for="state" class="col-sm-4 control-label">*State</label>
			<div class="col-sm-5">	
				<input type="text" class="form-control" name="registerBean.state" id="state"  required="required"  value='<s:property value="registerBean.state" />'/>
			</div>	
		</div> --%>
		
		<div class="form-group">
			
			<label for="state"  class="col-sm-4 control-label">*State</label>
			<div class="col-sm-5" align="left">
			
			<input type="hidden" class="form-control" name="registerBean.state" id="state" required="required"/>
			<input type="hidden"  value='<s:property value="registerBean.state" default="-1"/>' />
			<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" id="statebtn">
						State <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" >
						<li value="Andhra Pradesh"><a href="javascript:addState('0')">Andhra Pradesh</a></li>
						<li value="Arunachal Pradesh"><a href="javascript:addState('1')">Arunachal Pradesh</a><li>
						<li value="Assam"><a href="javascript:addState('2')">Assam</a></li>
						<li value="Bihar"><a href="javascript:addState('3')">Bihar</a></li>
						<li value="Chhattisgarh"><a href="javascript:addState('4')">Chhattisgarh</a></li>
						<li value="Goa"><a href="javascript:addState('5')">Goa</a></li>
						<li value="Gujarat"><a href="javascript:addState('6')">Gujarat</a></li>
						<li value="Haryana"><a href="javascript:addState('7')">Haryana</a></li>
						<li value="Himachal Pradesh"><a href="javascript:addState('8')">Himachal Pradesh</a></li>
						<li value="Jammu and Kashmir"><a href="javascript:addState('9')">Jammu and Kashmir</a></li>
						<li value="Jharkhand"><a href="javascript:addState('10')">Jharkhand</a></li>
						<li value="Karnataka"><a href="javascript:addState('11')">Karnataka</a></li>
						<li value="Kerala"><a href="javascript:addState('12')">Kerala</a></li>
						<li><a href="javascript:addState('13')">Madhya Pradesh</a></li>
						<li><a href="javascript:addState('14')">Maharashtra</a></li>
						<li><a href="javascript:addState('15')">Manipur</a></li>
						<li><a href="javascript:addState('16')">Meghalaya</a></li>
						<li><a href="javascript:addState('17')">Mizoram</a></li>
						<li><a href="javascript:addState('18')">Nagaland</a></li>
						<li><a href="javascript:addState('19')">Orissa</a></li>
						<li><a href="javascript:addState('20')">Punjab</a></li>
						<li><a href="javascript:addState('21')">Rajasthan</a></li>
						<li><a href="javascript:addState('22')">Sikkim</a></li>
						<li><a href="javascript:addState('23')">Tamil Nadu</a></li>
						<li><a href="javascript:addState('24')">Telangana</a></li>
						<li><a href="javascript:addState('25')">Tripura</a></li>
						<li><a href="javascript:addState('26')">Uttar Pradesh</a></li>
						<li><a href="javascript:addState('27')">Uttarakhand</a></li>
						<li><a href="javascript:addState('28')">West Bengal</a></li>
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
		
		<div id="lgnameError" style="display: none;color: red;">This login name is already registered</div>
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