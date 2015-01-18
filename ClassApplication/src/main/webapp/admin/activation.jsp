<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
function validatecode(code){
	var filter= /^[a-zA-Z0-9 ]+$/;
	if (filter.test(code)) {
		return true;
		}
		else {
		return false;
		}
}
$(document).ready(function() {
	$("#submit").on("click",function(){
		code=$("#activation").val();
		if(validatecode(code)){
		$.ajax({
			url:"classOwnerServlet",
			data:{
				methodToCall:"activation",
				code:code
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
		}else{
			alert("Invalid Activation Code");
		}
	});
	
});
</script>
</head>
<body>
<!-- Please Enter Your Activation Code:
<input type="text" id="activation" maxlength="100">
<button id="submit">Submit</button>
 -->
<div class="container">
	<div class="row">
	
        <div class="col-md-6 col-md-offset-3" id="forgotdiv"> 
  <h4 style="border-bottom: 1px solid #c5c5c5;">
    <i class="glyphicon glyphicon-user">
    </i>
    Activation
    <br>
   Please Enter Your Activation Code:
  <div style="padding: 20px;" id="form-olvidado">
      <fieldset>
        <div class="form-group input-group">
          <span class="input-group-addon">
            Code
          </span>
          <input class="form-control" placeholder="Activation Code" name="activation" type="text" required="" autofocus="" id="activation">
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