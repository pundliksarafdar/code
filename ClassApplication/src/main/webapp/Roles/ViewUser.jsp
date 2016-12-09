<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var user_id = "";
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
	var dataTable = $("#customUserTable").DataTable();
	 dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw(); 
	$('.datetimepicker').datetimepicker({
		format : 'DD-MM-YYYY',
		pickTime : false,
		maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
	});
	
	$(".joiningdatetimepicker").datetimepicker({
		pickTime: false,
		format:"DD-MM-YYYY",
		maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
	}).data("DateTimePicker").setDate(new Date());
	
$("#customUserTable").DataTable();
$("#customUserTableDiv").on("click",".edit",function(){
	user_id = $(this).prop("id");
	$(".error").html("");
	$("#editcustomUser").show();
	$("#customUserTableDiv").hide();
	$("#role").select2();
	$("#state").select2();
	var handlers = {};
		handlers.success = function(resp){
			if(resp != null){
				$("#fname").val(resp.registerBean.fname);
				$("#lname").val(resp.registerBean.lname);
				$("#dobfield").val(resp.registerBean.dob.substring(6)+"-"+resp.registerBean.dob.substring(4,6)+"-"+resp.registerBean.dob.substring(0,4));
				$("#address").val(resp.registerBean.addr1);
				$("#city").val(resp.registerBean.city);
				$("#state").select2().val(resp.registerBean.state).change();
				$("#email").val(resp.registerBean.email);
				$("#mobile").val(resp.registerBean.phone1);
			    $("#role").select2().val(resp.registerBean.inst_roll).change();
			    $("#education").val(resp.inst_user.education);
				$("#joiningDate").val(resp.inst_user.joining_date.substring(8)+"-"+resp.inst_user.joining_date.substring(5,7)+"-"+resp.inst_user.joining_date.substring(0,4));
			}
		}
		handlers.error = function(){
		}
		rest.get("rest/classownerservice/getinstuser/"+$(this).prop("id"),handlers);
});

$("#back").click(function(){
	$("#editcustomUser").hide();
	$("#customUserTableDiv").show();
});

$("#viewBack").click(function(){
	$("#viewcustomUser").hide();
	$("#customUserTableDiv").show();
});

$("#customUserTableDiv").on("click",".view",function(){
	$("#customUserTableDiv").hide();
	$("#viewcustomUser").show();
	var handlers = {};
	handlers.success = function(resp){
		if(resp != null){
			$("#viewfname").html(resp.registerBean.fname);
			$("#viewlname").html(resp.registerBean.lname);
			$("#viewDob").html(resp.registerBean.dob.substring(6)+"-"+resp.registerBean.dob.substring(4,6)+"-"+resp.registerBean.dob.substring(0,4));
			$("#viewAddress").html(resp.registerBean.addr1);
			$("#viewCity").html(resp.registerBean.city);
			$("#viewState").html(resp.registerBean.state);
			$("#viewEmail").html(resp.registerBean.email);
			$("#viewMobile").html(resp.registerBean.phone1);
			$("#role").select2().val(resp.registerBean.inst_roll).change();
			$("#viewRole").html($("#role").select2('data')[0].text);
		    $("#viewEducation").html(resp.inst_user.education);
			$("#viewJoiningDate").html(resp.inst_user.joining_date.substring(8)+"-"+resp.inst_user.joining_date.substring(5,7)+"-"+resp.inst_user.joining_date.substring(0,4));
		}
	}
	handlers.error = function(){
	}
	rest.get("rest/classownerservice/getinstuser/"+$(this).prop("id"),handlers);
});

$("#customUserTableDiv").on("click",".delete",function(){
	var id = $(this).prop("id");
	var handlers = {};
	handlers.success = function(resp){
		var table = $("#customUserTable").DataTable();
		$("#customUserTable").find(".delete[id='"+id+"']").closest("tr").addClass('selected');;
		table.row('.selected').remove().draw( false );
		 $.notify({message: "User deleted successfuly"},{type: 'success'});
	}
	handlers.error = function(){
	}
	rest.get("rest/classownerservice/deleteUser/"+$(this).prop("id"),handlers);
});

$("#submit").click(function(){
	$(".error").html("");
	var validFlag = true;
	var education = $("#education").val().trim();
	var joiningDate = $("#joiningDate").val().split("-").reverse().join("-");
	var register = {};
	register.regId = user_id;
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
	}else{
		register.dob = register.dob.split("-").reverse().join("-")
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
		 $.notify({message: "User updated successfuly"},{type: 'success'});
		 $("#back").click();
	}
	handlers.error = function(){
	}
	rest.put("rest/classownerservice/updateUser/"+joiningDate+"/"+education,handlers,JSON.stringify(register));
	}
});

});
</script>
</head>
<body>
<jsp:include page="RoleHeaders.jsp" >
		<jsp:param value="active" name="viewuser"/>
</jsp:include>
<div class="container" style="margin-top: 2%" id="customUserTableDiv">
<table class="table table-striped" id="customUserTable">
<thead>
	<tr>
		<th>#</th>
		<th>Name</th>
		<th>Actions</th>
	</tr>
</thead>
<tbody>
	<c:forEach items="${customUserList}" var="user" varStatus="counter">
	<tr>
		<td><%-- <c:out value="${counter.count}"></c:out> --%></td>
		<td><div class="tableRoleDesc"><c:out value="${user.fname}"></c:out> <c:out value="${user.lname}"></c:out></div></td>
		<td>
			<button class="btn btn-xs btn-info view" id="<c:out value="${user.reg_id}"></c:out>">View</button>
			<button class="btn btn-xs btn-primary edit" id="<c:out value="${user.reg_id}"></c:out>">Edit</button>
			<button class="btn btn-xs btn-danger delete" id="<c:out value="${user.reg_id}"></c:out>">Delete</button>
		</td>
	</tr>
	</c:forEach>
</tbody>
</table>
</div>

<div class="container" style="margin-top: 2%;display: none;" id="editcustomUser"> 
<div class="row">
<button id="back" class="btn btn-primary btn-sm">Back</button>
</div>
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
<div class="col-md-3"><button id="submit" class="btn btn-sm btn-primary">Update</button></div>
</div>
</div>
</div> 

<div class="container" style="margin-top: 2%;display: none;" id="viewcustomUser"> 
<div class="row">
<button id="viewBack" class="btn btn-primary btn-sm">Back</button>
</div>
<div class="container" style="margin-top: 2%"> 
<div class="row">
	<div class="col-md-3"><label for="role"  class="control-label">*Select role</label></div>
	<div class="col-md-3"><span id="viewRole"></span></div>	
</div>
<div class="row">
	<div class="col-md-3"><label for="fname"  class="control-label">*First Name</label></div>
	<div class="col-md-3"><span id="viewfname"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="lname"  class="control-label">*Last Name</label></div>
	<div class="col-md-3"><span id="viewlname"></span></div>
</div>	
<div class="row">
			<label for="dob" class="col-sm-3 control-label" >*Date of Birth</label>
			<div class="col-sm-3"><span id="viewDob"></span></div>
</div>
<div class="row">
			<label for="dob" class="col-sm-3 control-label" >*Institute joining date</label>
			<div class="col-sm-3"><span id="viewJoiningDate"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="mobile"  class="control-label">Education</label></div>
	<div class="col-md-3"><span id="viewEducation"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="address"  class="control-label">*Address</label></div>
	<div class="col-md-3"><span id="viewAddress"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="city"  class="control-label">*City</label></div>
	<div class="col-md-3"><span id="viewCity"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="state"  class="control-label">*State</label></div>
	<div class="col-md-3"><span id="viewState"></span></div>
</div>
<div class="row">
	<div class="col-md-3"><label for="mobile"  class="control-label">*Mobile</label></div>
	<div class="col-md-3"><span id="viewMobile"></span></div>
</div>	
<div class="row">
	<div class="col-md-3"><label for="email"  class="control-label">*Email</label></div>
	<div class="col-md-3"><span id="viewEmail"></span></div>
</div>	
</div>
</div> 
</body>
</html>