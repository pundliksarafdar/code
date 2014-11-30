<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<script>
	var isHidden = true;

	$(document).ready(function() {
		var currentPhone = $("#phone1").val();
		var currentLogin = 	$("#loginname").val();
		
		$('#datetimepicker').datetimepicker({
			format : 'YYYY-MM-DD',
			pickTime : false,
			maxDate:((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()
		});
		
		if($("#dobhidden").val()>3){
			$("#dobfield").val($("#dobhidden").val().substring(0,4)+"-"+$("#dobhidden").val().substring(4,6)+"-"+$("#dobhidden").val().substring(6,8));
		}
		
		/*Password related function*/
		
		$("#passwords").hide(10);
		$("#showpassbtn").click(function(){
			var $that = $(this);
			$("#passwords").toggle(function(){
				if($that.find("i").hasClass("glyphicon-circle-arrow-down")){
					$that.find("i").removeClass("glyphicon-circle-arrow-down");
					$that.find("i").addClass("glyphicon-circle-arrow-up");
					isHidden = false;
				}else{
					$("#loginpass").val("");
					$("#loginpassre").val("");
					$that.find("i").removeClass("glyphicon-circle-arrow-up");
					$that.find("i").addClass("glyphicon-circle-arrow-down");
					isHidden = true;
				}
			});	
		});
		
		$("#phone1").on("blur",function(){
			if(currentPhone != $(this).val().trim()){
			allAjax.checkNumberExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#phone1").focus();
					   $("#phoneError").show();		   					   	
				   	}else{
				   		
				   	}				
			});
			}
		});
		
		$("#loginname").on("blur",function(){
			if(currentLogin != $(this).val().trim()){
			allAjax.checkUserNameExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#loginname").focus();
					   $("#lgnameError").show();						   					   	
				   	}else{
				   		
				   	}				
			});
			}
		});
		
	});
	
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
	
	function go(){
		if(validate()){
			$('#frEditUser').submit();
		}
	}
	
	function isPasswordValid(){
		
		var ispassvalid = false;
		if(isHidden){
			return true;
		}
		
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
		var regAddressExpr = /^[a-zA-Z0-9]+$/;
		var regPasswordExpr = /^(?=[^\d_].*?\d)\w(\w|[!@#$%]){5,20}/;
		var textonly=/^[a-zA-Z]+$/;

		if(!isHidden && !$("#loginpass").val().match(regPasswordExpr)){
			isValidated = false;
			$("#loginpass").addClass("has-error");
			$('#mandatoryerror').append("Password is invalid<br>");
		}	
		if(!isHidden && $("#loginpass").val()==""){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror').append("Please Enter Password<br>");
			
		}else if(!isHidden && $("#loginpassre").val()==""){
			isValidated = false;
			$("#loginpassre").addClass("has-error");
			$('#mandatoryerror').append("Please Enter Re-Password<br>");	
		}else if(!isHidden && !$("#loginpass").val()==$("#loginpassre").val()){
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
		if(state==2){
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
		
		if(!$("#state").val().match(textonly)){
			isValidated = false;
			$("#state").addClass("has-error");
			$('#mandatoryerror').append("State name is invalid. Only characters are not allowed. <br>");
		}
		
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
<c:set var="user" value="${sessionScope.user}"></c:set>
<form id="frEditUser" class="form-horizontal" role="form" action="editconfirm" method="post">
	<s:if test="hasActionErrors()">
		<div class="errors">
			<s:actionerror/>
		</div>
	</s:if>
	
		<div class="alert alert-danger" id="mandatoryerror" hidden="hidden">
			
		</div>

	 	<div class="form-group">
    		<label for="inputFirstName" class="col-sm-4 control-label">First Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="fname" name="registerBean.fname" value="<c:out value="${user.firstname}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputMiddleName" class="col-sm-4 control-label">Middle Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="mname" name="registerBean.mname" value="<c:out value="${user.middlename}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputLastName" class="col-sm-4 control-label">Last Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="lname" name="registerBean.lname"  value="<c:out value="${user.lastname}"></c:out>"/>
			</div>
		</div>
		<!-- 
		<div class="form-group">
    		<label for="inputDBO" class="col-sm-4 control-label">Date of Birth</label>
    		<div class="col-sm-5">
				<input type="date" class="form-control" id="inputDBO" name="registerBean.dob"  value="<c:out value="${user.dob}"></c:out>"/>
			</div>
		</div>
		 -->
		<div class="form-group">
    		<label for="inputDBO" class="col-sm-4 control-label">Date of Birth</label>
    		<input type="hidden" value='<c:out value="${user.dob}"></c:out>' id="dobhidden"/>
    		<div class="col-sm-5">
			<div id="datetimepicker" class="input-group" style="width :250px;">
				<input class="form-control"
					type="text"  id="dobfield" name="registerBean.dob" required="required" readonly value='<c:out value="${user.dob}"></c:out>'/> <span class="input-group-addon add-on"> <i
					class="glyphicon glyphicon-calendar glyphicon-time"></i>
				</span>
			</div>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputAdd1" class="col-sm-4 control-label">Address1</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="addr1" name="registerBean.addr1"  value="<c:out value="${user.addr1}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputAdd2" class="col-sm-4 control-label">Address2</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="addr2" name="registerBean.addr2"  value="<c:out value="${user.addr2}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputCity" class="col-sm-4 control-label">City</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="city" name="registerBean.city" value="<c:out value="${user.city}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputState" class="col-sm-4 control-label">State</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="state" name="registerBean.state" value="<c:out value="${user.state}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputCountry" class="col-sm-4 control-label">Country</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="country" name="registerBean.country"  value="<c:out value="${user.country}"></c:out>"/>
			</div>
		</div>
		
		<div id="phoneError" style="display: none;color: red;">This Phone number is already registered</div>
		<div class="form-group">
    		<label for="inputPhone1" class="col-sm-4 control-label">Phone 1</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="phone1" name="registerBean.phone1"  value="<c:out value="${user.phone1}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputPhone2" class="col-sm-4 control-label">Phone 2</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="phone2" name="registerBean.phone2"  value="<c:out value="${user.phone2}"></c:out>"/>
			</div>
		</div>
		
		<div id="lgnameError" style="display: none;color: red;">This login name is already registered</div>
		<div class="form-group">
    		<label for="inputLoginName" class="col-sm-4 control-label">Desired Login Name</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="loginname" name="registerBean.loginName" value="<c:out value="${user.loginBean.loginname}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="loginpass" class="col-sm-4 control-label">Click here to change password</label>
    		<div class="col-sm-5">
				<div class="btn btn-default" id="showpassbtn">
					<i class="glyphicon glyphicon-circle-arrow-down"></i>
				</div>
			</div>
		</div>
		
		<div id="passwords">
		<div class="form-group">
    		<label for="loginpass" class="col-sm-4 control-label">Password</label>
    		<div class="col-sm-5">
				<input type="password" class="form-control" id="loginpass" name="registerBean.loginPass"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="loginpassre" class="col-sm-4 control-label">Re-Enter Password</label>
    		<div class="col-sm-5">
				<input type="password" class="form-control" id="loginpassre" name="registerBean.loginPassRe"/>
			</div>
		</div>
		</div>
		 <div class="form-group">
		    <div class="col-sm-offset-4 col-sm-5">		      
		      	<button type="button" value= "submit" class="btn btn-default" onclick="go();">Submit</button>
		      	<input type="button" value="Reload" class="btn btn-danger" onclick="javascript:location.reload();"/>				
			</div>
		</div>  		
	</form>
	
	<!-- 
	<form id="frDeleteUser" class="form-horizontal" role="form" action="deleteuser" method="post">
		<input type="hidden" name="userId" value="<c:out value="${user.loginBean.loginname}"></c:out>">
		<div class="form-group">
			<div class="col-sm-offset-4 col-sm-5">
				<button type="submit" value= "Delete" class="btn btn-default">Delete</button>
			 </div>
		</div> 
	</form>
	 -->
</body>
</html>