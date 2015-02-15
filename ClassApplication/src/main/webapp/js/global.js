$(document).ready(function(){
	$("#submitfeedback").click(function(){
		$("#namespan").html("");
		   $("#emailspan").html("");
		   $("#commentspan").html("");
		var regStringExpr = /^[a-zA-Z]+$/;
		var regPasswordExpr = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{5,20}/;
		var name=$("#feedbackname").val();
		var email=$("#feedbackemail").val();
		var comment=$("#comment").val();
		var status="";
		if(name==""){
			$("#namespan").html("<p style='color:red;'>Please enter name<p>");
			status="1";
		}else if(!name.match(regStringExpr)){
			$("#namespan").html("<p style='color:red;'>Please enter name valid<p>");
			status="1";
		}
		if(email.match(regPasswordExpr)){
		$("#emailspan").html("<p style='color:red;'>Enter Valid email<p>");
		status="1";
		}else if(email==""){
			$("#emailspan").html("<p style='color:red;'>Enter email<p>");
			status="1";
		}
		if(comment==""){
			$("#commentspan").html("<p style='color:red;'>Enter comment<p>");
			status="1";
		}
		if(status==""){
			$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addfeedback",
			    	 name:name,
			    	 email:email,
			    	 comment:comment
			    	
			   		},
			   type:"POST",
			   success:function(data){
				   $("#namespan").html("");
				   $("#emailspan").html("");
				   $("#commentspan").html("");
				   $("#feedbacksuccess").show();
				   $("#form").hide();
			   },
			   error:function(){
				   
			   }
			   });
		}
	});
	
	$("#contactuslink").click(function(){
					$("#namespan").html("");
				   $("#emailspan").html("");
				   $("#commentspan").html("");
				   $("#feedbacksuccess").hide();
				   $("#feedbackname").val("");
					$("#feedbackemail").val("");
					$("#comment").val("");
				   $("#form").show();
	});
});