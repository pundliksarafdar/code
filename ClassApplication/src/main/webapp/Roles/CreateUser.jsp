<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<style>
.error{
color: red;
}
</style>
<script type="text/javascript">
$("#role").select2();
function validateEmail(sEmail) {
	var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	if (filter.test(sEmail)) {
	return true;
	}
	else {
	return false;
	}
	}
$(document).ready(function(){
	$('.datetimepicker').datetimepicker({
		format : 'YYYY-MM-DD',
		pickTime : false
	});
	
	$(".joiningdatetimepicker").datetimepicker({
		pickTime: false,
		format:"YYYY-MM-DD"
	}).data("DateTimePicker").setDate(new Date());
	
	$("#submit").click(function(){
		$(".error").html("");
		var validFlag = true;
		var education = $("#education").val().trim();
		var joiningDate = $("#joiningDate").val();
		var register = {};
		register.fname = $("#fname").val().trim();
		register.lname = $("#lname").val().trim();
		register.dob = $("#dobfield").val();
		register.addr1 = $("#address").val().trim();
		register.city = $("#city").val().trim();
		register.state = $("#state").val();
		register.email = $("#email").val().trim();
		register.phone1 = $("#mobile").val().trim();
		register.inst_roll = $("#role").val();
		var regPhoneNumber = /^[0-9]+$/;
		var regStringExpr = /^[a-zA-Z]+$/;
		var regAddressExpr = /^[a-zA-Z0-9 ]+$/;
		var regcityExpr = /^[a-zA-Z ]+$/;
		if(education == ""){
			education = "NA";
		}
		if(register.fname == ""){
			$("#fnameError").html("Enter first name")
			validFlag = false;
		}else if(!register.fname.match(regStringExpr)){
			$("#fnameError").html("Invalid first name,only alphabets allowed.")
			validFlag = false;
		}
		if(register.lname == ""){
			$("#lnameError").html("Enter last name")
			validFlag = false;
		}else if(!register.lname.match(regStringExpr)){
			$("#lnameError").html("Invalid last name,only alphabets allowed.")
			validFlag = false;
		}
		if(register.dob == ""){
			$("#dobError").html("Select DOB")
			validFlag = false;
		}
		if(register.addr1 == ""){
			$("#addressError").html("Enter Address")
			validFlag = false;
		}else if(!register.addr1.match(regAddressExpr)){
			$("#addressError").html("Invalid Address,Special character not allowed.")
			validFlag = false;
		}
		if(register.city == ""){
			$("#cityError").html("Enter City")
			validFlag = false;
		}else if(!register.city.match(regcityExpr)){
			$("#cityError").html("Invalid City,only alphabets allowed.")
			validFlag = false;
		}
		if(register.phone1 == ""){
			$("#mobileError").html("Enter mobile")
			validFlag = false;
		}else if(!register.phone1.match(regPhoneNumber) || register.phone1.length != 10){
			$("#mobileError").html("Invalid mobile.")
			validFlag = false;
		}
		if(register.email == ""){
			$("#emailError").html("Enter email")
			validFlag = false;
		}else if(!validateEmail(register.email)){
			$("#emailError").html("Invalid email.")
			validFlag = false;
		}
		if(register.state == "-1"){
			$("#stateError").html("Select State")
			validFlag = false;
		}
		if($("#role").val() == "-1"){
			$("#roleError").html("Select role")
			validFlag = false;
		}
		if(validFlag){
		var handlers = {};
		handlers.success = function(resp){
			 $.notify({message: "User added successfuly"},{type: 'success'});
		}
		handlers.error = function(){
		}
		rest.post("rest/classownerservice/createUser/"+joiningDate+"/"+education,handlers,JSON.stringify(register));
		}
	});
});
</script>
<body>
<jsp:include page="RoleHeaders.jsp" >
		<jsp:param value="active" name="createuser"/>
</jsp:include>

<div class="container" style="margin-top: 2%"> 
<div class="row">
	<div class="col-md-3"><label for="role"  class="control-label">*Select role</label></div>
	<div class="col-md-3">
		<select id="role" class="form-control">
			<option value="-1">Select Role</option>
			<c:forEach items="${roleList}" var="role" varStatus="counter">
			<option value="<c:out value="${role.roll_id}"></c:out>"><c:out value="${role.roll_desc}"></c:out></option>
			</c:forEach>
		</select>
		<span id="roleError" class="error"></span>
	</div>	
</div>
<div class="row">
	<div class="col-md-3"><label for="fname"  class="control-label">*First Name</label></div>
	<div class="col-md-3"><input type="text" class="form-control" id="fname">
	<span id="fnameError" class="error"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="lname"  class="control-label">*Last Name</label></div>
	<div class="col-md-3"><input type="text" class="form-control" id="lname">
	<span id="lnameError" class="error"></span></div>
</div>	
<div class="row">
			<label for="dob" class="col-sm-3 control-label" >*Date of Birth</label>
			<div class="col-sm-3">					
					<div class="input-group datetimepicker" style="width :250px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="dobfield" name="registerBean.dob" required="required"  readonly/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					<span id="dobError" class="error"></span>
				</div>
</div>
<div class="row">
			<label for="dob" class="col-sm-3 control-label" >*Institute joining date</label>
			<div class="col-sm-3">					
					<div class="input-group joiningdatetimepicker" style="width :250px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="joiningDate" name="registerBean.dob" required="required"  readonly/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					<span id="joiningDateError" class="error"></span>
				</div>
</div>
<div class="row">
	<div class="col-md-3"><label for="mobile"  class="control-label">Education</label></div>
	<div class="col-md-3"><input type="text" class="form-control" id="education">
	<span id="educationError" class="error"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="address"  class="control-label">*Address</label></div>
	<div class="col-md-3"><textarea class="form-control" id="address"></textarea>
	<span id="addressError" class="error"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="city"  class="control-label">*City</label></div>
	<div class="col-md-3"><input type="text" class="form-control" id="city">
	<span id="cityError" class="error"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="state"  class="control-label">*State</label></div>
	<div class="col-md-3">
	<select id="state" class="form-control">
			<option value="-1">Select State</option>
			<option value="Andhra Pradesh">Andhra Pradesh</option>
			<option value="Arunachal Pradesh">Arunachal Pradesh</option>
			<option value="Assam">Assam</option>
			<option value="Bihar">Bihar</option>
			<option value="Chhattisgarh">Chhattisgarh</option>
			<option value="Goa">Goa</option>
			<option value="Gujarat">Gujarat</option>
			<option value="Haryana">Haryana</option>
			<option value="Himachal Pradesh">Himachal Pradesh</option>
			<option value="Jammu and Kashmir">Jammu and Kashmir</option>
			<option value="Jharkhand">Jharkhand</option>
			<option value="Karnataka">Karnataka</option>
			<option value="Kerala">Kerala</option>
			<option>Madhya Pradesh</option>
			<option>Maharashtra</option>
			<option>Manipur</option>
			<option>Meghalaya</option>
			<option>Mizoram</option>
			<option>Nagaland</option>
			<option>Orissa</option>
			<option>Punjab</option>
			<option>Rajasthan</option>
			<option>Sikkim</option>
			<option>Tamil Nadu</option>
			<option>Telangana</option>
			<option>Tripura</option>
			<option>Uttar Pradesh</option>
			<option>Uttarakhand</option>
			<option>West Bengal</option>
		</select>
		<span id="stateError" class="error"></span>
		</div>
</div>
<div class="row">
	<div class="col-md-3"><label for="mobile"  class="control-label">*Mobile</label></div>
	<div class="col-md-3"><input type="text" class="form-control" id="mobile">
	<span id="mobileError" class="error"></span></div>
</div>	
<div class="row">
	<div class="col-md-3"><label for="email"  class="control-label">*Email</label></div>
	<div class="col-md-3"><input type="text" class="form-control" id="email">
	<span id="emailError" class="error"></span></div>
</div>	
<div class="row">
<div class="col-md-3"><button id="submit" class="btn btn-sm btn-primary">Submit</button></div>
</div>
</div> 
</body>
</html>