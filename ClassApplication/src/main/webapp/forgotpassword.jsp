<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
function validateEmail(sEmail) {
	var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	if (filter.test(sEmail)) {
	return true;
	}
	else {
	return false;
	}
	}
	
function validateMobile(mobile){
	var filter=/^[0-9]+$/;
	if (filter.test(mobile)) {
		return true;
		}
		else {
		return false;
		}
}
	
	function formvalidation(){
		var email=$("#email").val();
		var phone=$("#mobile").val();
		var status="";
		if(email=="" || phone=="")
		{
			alert("Enter All The Fields");
			return false;
		}else
		{	if(!validateEmail(email)){
			$("#emailspan").html("<p>Invalid Email ID</p>");
			status=1;
		}if(!validateMobile(phone)){
			$("#mobilespan").html("<p>Invalid Mobile No</p>");
			status=1;
		}
		if(status==1){
			return false;
		}
		}
		return true;
		
	}
	
$(document).ready(function(){
	$("#successdiv").hide();
	
$("#submit").click(function(){
	$("#emailspan").html("");
	$("#mobilespan").html("");
	var email=$("#email").val();
	var phone=$("#mobile").val();
	if(formvalidation()){
		$("#submit").attr("disabled","disable");
	$.ajax({
		url: "classOwnerServlet",
		data: {
	    	 methodToCall: "forgotpassword",
			 email:$("#email").val(),
			 mobile:$("#mobile").val()
	   		},
	   		type:"POST",
			   success:function(e){
				  var result=JSON.parse(e);
				  if(result.status=="success"){
					  $("#forgotdiv").hide();
					  $("#successdiv").show();
				  }else{
					  $("#submit").removeAttr("disabled");
					  alert("Invalid Email/Mobile No");
				  }
			   	},
			   error:function(e){
				   $("#submit").removeAttr("disabled");
				   
			   }
	});
	}
});
});
</script>
</head>
<body>
<div class="container">
	<div class="row">
	
        <div class="col-md-6 col-md-offset-3" id="forgotdiv"> 
  <h4 style="border-bottom: 1px solid #c5c5c5;">
    <i class="glyphicon glyphicon-user">
    </i>
    Forgot Password
    <br>
    Enter Your Registered Email ID and Mobile No.
  <div style="padding: 20px;" id="form-olvidado">
      <fieldset>
        <div class="form-group input-group">
          <span class="input-group-addon">
            @
          </span>
          <input class="form-control" placeholder="Email" name="email" type="text" required="" autofocus="" id="email">
        </div>
        <div>
        <span id="emailspan"></span>
        </div>
        <div class="form-group input-group">
          <span class="input-group-addon">
            <i class="glyphicon glyphicon-phone">
            </i>
          </span>
          <input class="form-control" placeholder="Mobile no" name="mobile" type="text" value="" required="" id="mobile">
        </div>
        <div>
        <span id="mobilespan"></span>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-block" id="submit">
            Submit
          </button>
        </div>
      </fieldset>
  </div>
</div>
<div id="successdiv" class="well">
Check Your Mail Box....
Click here to <a href="login.action">login</a>
</div>
	</div>
</div>
</body>
</html>