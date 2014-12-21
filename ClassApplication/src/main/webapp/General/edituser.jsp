<%@page import="com.user.UserBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<style>
.danger{
	color:red;
}
</style>

<script>
	var isHidden = true;
	$(function () {
		$('[data-toggle="tooltip"]').attr("title",passwordCriteria); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
	})
	var passwordCriteria = "Password should be greater than 5 and less than 20 in length <br> Should contain atleast one special character [!@#$%], one lowercase letter, one uppercase letter and one digit<br> "
	
	$(document).ready(function() {
		var currentPhone = $("#phone1").val();
		var currentLogin = 	$("#loginname").val();
		
		$('#datetimepicker').datetimepicker({
			format : 'YYYY-MM-DD',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
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
					$("#loginpassre,#oldpass").val("");
					$that.find("i").removeClass("glyphicon-circle-arrow-up");
					$that.find("i").addClass("glyphicon-circle-arrow-down");
					$("#oldpass,#loginpass,#loginpassre").parents(".form-group").removeClass("has-error");
					$("#oldpass,#loginpass,#loginpassre").parents(".form-group").find(".danger").remove();
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
			if(validateFields()){
				return true;	
			}else{
				return false;	
			}
		}else{
			
			validateFields();
			return false;			
		}		
	}
	
	function go(){
		if(validate()){
			$('#frEditUser').submit();
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
			
		if(!isHidden && $("#oldpass").val()==""){
			isValidated = false;
			$("#oldpass").parents(".form-group").addClass("has-error");
			$('#oldpass').parents(".form-group").prepend("<p class='danger' >Please Enter Password</p>");
		}else if(!isHidden && $("#loginpass").val()==""){
			isValidated = false;
			$("#loginpass").parents(".form-group").addClass("has-error");
			$('#loginpass').parents(".form-group").prepend("<p class='danger' >Please Enter Password</p>");
		}else if(!isHidden && $("#loginpassre").val()==""){
			isValidated = false;
			$("#loginpassre").parents(".form-group").addClass("has-error");
			$('#loginpassre').parents(".form-group").prepend("<p class='danger' >Please Enter Re-Password</p>");	
		}else if(!isHidden && !$("#loginpass").val()==$("#loginpassre").val()){
			isValidated = false;
			$("#loginpassre").parents(".form-group").addClass("has-error");
			$('#loginpassre').parents(".form-group").prepend("<p class='danger' >Password is mismatched.</p>");
		}else if(!isHidden && !$("#loginpass").val().match(regPasswordExpr)){
			isValidated = false;
			$("#loginpass").parents(".form-group").addClass("has-error");
			$('#loginpass').parents(".form-group").prepend("<p class='danger' >Password is invalid, Please see flyout for criteria</p>");
			$('[data-toggle="tooltip"]').tooltip('show');
		}
		
		if($("#loginname").val().length<5 || !$("#loginname").val().match(regloginname))
			{
			$("#loginname").parents(".form-group").prepend("<p class='danger' >Username is invalid should be more than 5 character</p>");
			isValidated = false;
			$("#loginname").addClass("has-error");
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
		$("#rolebtn").focus();
		return isValidated;
	}

</script>
<body>
<c:set var="user" value="${sessionScope.user}"></c:set>
<form id="frEditUser" class="form-horizontal" role="form" action="editconfirm" method="post">
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger">
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
		
		<!-- 
		<div class="form-group">
    		<label for="inputState" class="col-sm-4 control-label">State</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="state" name="registerBean.state" value="<c:out value="${user.state}"></c:out>"/>
			</div>
		</div>
		 -->
		 
		<div class="form-group">
			
			<label for="state"  class="col-sm-4 control-label">*State</label>
			<div class="col-sm-5" align="left">
			
			<input type="hidden" class="form-control" name="registerBean.state" id="state" required="required" value='<c:out value="${user.state}"></c:out>'/>
			<input type="hidden"  value='<s:property value="registerBean.state" default="-1"/>' />
			<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" id="statebtn">
						State <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" >
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
    		<label for="inputCountry" class="col-sm-4 control-label">Country</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="country" disabled="disabled" name="registerBean.country"  value="India"/>
			</div>
		</div>
		
		<div id="phoneError" style="display: none;color: red;">This Phone number is already registered</div>
		<div class="form-group">
    		<label for="inputPhone1" class="col-sm-4 control-label">Phone 1</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="phone1" maxlength="10" name="registerBean.phone1"  value="<c:out value="${user.phone1}"></c:out>"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="inputPhone2" class="col-sm-4 control-label">Phone 2</label>
    		<div class="col-sm-5">
				<input type="text" class="form-control" id="phone2" maxlength="10" name="registerBean.phone2"  value="<c:out value="${user.phone2}"></c:out>"/>
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
    		<label for="loginpass" class="col-sm-4 control-label">*Old Password</label>
    		<div class="col-sm-5">
				<input type="password" class="form-control" id="oldpass" name="oldPassword"/>
			</div>
		</div>
		
		<div class="form-group">
    		<label for="loginpass" class="col-sm-4 control-label">
    		<i class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="bottom" title="" style="color: red;"></i>*Password</label>
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