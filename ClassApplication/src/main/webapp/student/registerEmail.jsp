<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="/js/REST.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#submit").on("click",function(){
		$("#emailError").html("");
		var email = $("#email").val();
		var atpos = email.indexOf("@");
	    var dotpos = email.lastIndexOf(".");
	    if(email == ""){
	    	  $("#emailError").html("Enter e-mail ID");
		        return false;
	    }else if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
	        $("#emailError").html("Not a valid e-mail ID");
	        return false;
	    }else{
	    	var handler = {};
	    	handler.success = function(e){
	    		if(e==true){
	    			$("#emailError").html("Email ID already registered,enter different email ID");
	    		}else{
	    			$("#successEmail").append("Activation code sent");
	    			$("#submit").hide();
	    			$("#resend").show();
	    			$("#next").show();
	    		}
	    	};
	    	handler.error = function(e){};
	    	rest.get("rest/classownerservice/validateEmail/"+email,handler);
	    }
	});
	
	$("#resend").on("click",function(){
		$("#successEmail").empty();
		$("#emailError").html("");
		var email = $("#email").val();
		var atpos = email.indexOf("@");
	    var dotpos = email.lastIndexOf(".");
	    if(email == ""){
	    	  $("#emailError").html("Enter e-mail ID");
		        return false;
	    }else if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
	        $("#emailError").html("Not a valid e-mail ID");
	        return false;
	    }else{
	    	var handler = {};
	    	handler.success = function(e){
	    		if(e==true){
	    			$("#emailError").html("Email ID already registered,enter different email ID");
	    		}else{
	    			$("#successEmail").append("Activation code resent");
	    		}
	    	};
	    	handler.error = function(e){};
	    	rest.get("rest/classownerservice/resendActivation/"+email,handler);
	    }
	});
	
	$("#activate").on("click",function(){
		$("#successEmail").empty();
		$("#emailError").html("");
		var email = $("#email").val();
		var code = $("#activationcode").val();
		var atpos = email.indexOf("@");
	    var dotpos = email.lastIndexOf(".");
	    if(email == ""){
	    	  $("#emailError").html("Enter e-mail ID");
		        return false;
	    }else if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
	        $("#emailError").html("Not a valid e-mail ID");
	        return false;
	    }else{
	    	var handler = {};
	    	handler.success = function(e){
	    		if(e==true){
	    			window.location="login.action";
	    		}else{
	    			$("#activationError").append("Invalid activation code");
	    		}
	    	};
	    	handler.error = function(e){};
	    	rest.get("rest/classownerservice/activation/"+email+"/"+code,handler);
	    }
	});
	
	$("#next").click(function(){
		$("#registerEmail").hide();
		$("#activation").show();
	});
	
	$("#back").click(function(){
		$("#registerEmail").show();
		$("#activation").hide();
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


<div class="container" id="registerEmail">
	<div class="row">
	
        <div class="col-md-6 col-md-offset-3" id="forgotdiv"> 
  <h4 style="border-bottom: 1px solid #c5c5c5;">
    <i class="glyphicon glyphicon-user">
    </i>
    Register Email
    <br>
    <div id="successEmail" style="padding: 10px;color: red;font-size: small" align="left"></div>
  <div style="padding: 10px;" id="form-olvidado">
      <fieldset>
        <div class="form-group">
          <input class="form-control" placeholder="Enter Email" id="email" type="text" required="" autofocus="" id="password">
          <span id="emailError" style="color: red;font-size: smaller"></span>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-block" id="submit">
            Submit
          </button>
          <button type="submit" class="btn btn-primary btn-block" id="resend" style="display: none;">
            resend
          </button>
           <button type="submit" class="btn btn-primary btn-block" id="next" style="display: none;">
            Next
          </button>
        </div>
      </fieldset>
  </div>
</div>
	</div>
</div>

<div class="container" id="activation" style="display: none;">
	<div class="row">
	
        <div class="col-md-6 col-md-offset-3" id="forgotdiv"> 
  <h4 style="border-bottom: 1px solid #c5c5c5;">
    <i class="glyphicon glyphicon-user">
    </i>
    Activation
    <br>
   Please Enter Your Activation Code which is sent to registered email id:
  <div style="padding: 20px;" id="form-olvidado">
      <fieldset>
        <div class="form-group input-group">
          <span class="input-group-addon">
            Code
          </span>
          <input class="form-control" placeholder="Activation Code" name="activation" type="text" required="" autofocus="" id="activationcode">
          <span id="activationError" style="color: red;font-size: smaller"></span>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-block" id="activate">
            Submit
          </button>
          <button type="submit" class="btn btn-primary btn-block" id="back">
            Back
          </button>
        </div>
      </fieldset>
  </div>
</div>
</div>
</div>
</body>
</html>