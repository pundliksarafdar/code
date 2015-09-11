<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(function () {
	$('[data-toggle="tooltip"]').attr("title",passwordCriteria); 
	$('[data-toggle="tooltip"]').tooltip({"html":true});
})
var passwordCriteria = "Password should be greater than 5 and less than 20 in length <br> Should contain atleast one special character [!@#$%], one lowercase letter, one uppercase letter and one digit<br> "

function validatepass(){
	var regPasswordExpr = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{5,20}/;
	if(!$("#password").val().match(regPasswordExpr)){
		alert("Invalid Password");
		return false;
	}	
	
	if($("#password").val()!=$("#repass").val()){
		alert("Password Mismatch");
		return false;
	}
	return true;
}

$(document).ready(function(){
	$("#submit").on("click",function(){
		
		if(validatepass()){
			$.ajax({
				url:"classOwnerServlet",
				data:{
					methodToCall:"resetpassword",
					password:$("#password").val()
				},
				type:"POST",
				success:function(data){
					var result=JSON.parse(data);
					if(result.status=="success"){
						window.location="login.action";
					}else{
						alert("Invalid Activation Code");
					}
				},
				error:function(){
					
				}
			});
		}
	});
});
</script>
</head>
<body>
<!-- Reset Password....
<div>
Enter New Password:-<input type="password" id="password">
Re-enter Password:-<input type="password" id="repass">
<button id="submit">Submit</button>
</div> -->


<div class="container">
	<div class="row">
	
        <div class="col-md-6 col-md-offset-3" id="forgotdiv"> 
  <h4 style="border-bottom: 1px solid #c5c5c5;">
    <i class="glyphicon glyphicon-user">
    </i>
    Reset Password
    <br>
  <div style="padding: 20px;" id="form-olvidado">
      <fieldset>
        <div class="form-group">
          <input class="form-control" placeholder="Enter New Password" name="password" type="password" required="" autofocus="" id="password">
        </div>

        <div class="form-group">
          <input class="form-control" placeholder="Re-enter Password" name="repass" type="password" value="" required="" id="repass">
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-block" id="submit">
            Submit
          </button>
        </div>
      </fieldset>
  </div>
</div>
	</div>
</div>
</body>
</html>