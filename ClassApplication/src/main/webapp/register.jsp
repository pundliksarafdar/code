<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<style>
.danger{
	color:red;
}

.scrollable-menu {
    height: auto;
    max-height: 200px;
    overflow-x: hidden;
}
</style>

<script type="text/javascript">
	$(function () {
		$('[data-toggle="tooltip"]').attr("title",passwordCriteria); 
		$('[data-toggle="tooltip"]').tooltip({"html":true});
	})
	var passwordCriteria = "Password should be greater than 5 and less than 20 in length <br> Should contain atleast one special character [!@#$%], one lowercase letter, one uppercase letter and one digit<br> "
	$(document).ready(function(){
		
		/******************************************/
		$('#regform').validate({
        highlight: function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-group').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            if(element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        }
    });
		
		/*******************************************/
		$('#datetimepicker').datetimepicker({
			format : 'DD-MM-YYYY',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		});
		
		$("#fname,#mname,#lname").on("blur",function(){
			var string = $(this).val();	
			$(this).val(string.charAt(0).toUpperCase() + string.slice(1));
		});
		
		$('#roleValidation').val("");
		$('#phone1,#phone2').keydown(function(e){
		    console.log("loggin :"+e.keyCode+":"+e.which);
		    if(((e.keyCode < 47 || e.keyCode > 57)) && !(e.keyCode == 8 || e.keyCode == 9 || e.keyCode == 46 || e.keyCode ==37 || e.keyCode ==39)){
		    	if(((e.keyCode < 96 || e.keyCode > 105)) && !(e.keyCode == 8 || e.keyCode == 46 || e.keyCode ==37 || e.keyCode ==39)){
			    	return false;
			    }
		    }else{
		    	return true;
		    }
		});
		
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
		
		$("#email").on("blur",function(){
			allAjax.checkEmailExist($(this).val().trim(),function(e){
				var resultJson = JSON.parse(e);
				   if(resultJson.exists == true){
					   $("#emailError").show();						   					   	
				   	}else{
				   		
				   	}				
			});
		}).on("focus",function(){
			   $("#emailError").hide();
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
	
	function go(){
		$("input").parents(".form-group").find('.danger').remove();
		if($("#regform").valid()){
			$('#regform').submit();
		}else{
			$(document).scrollTop();
		}
	}

	
</script>
<body>

	 <form id="regform" action="/registeruser" method="post" role="form" class="form-horizontal"> 
		<%if(request.getAttribute(Constants.ERROR_MESSAGE) != null){%>
			<div class="alert alert-danger" id="mandatoryerror1">
				<%=request.getAttribute(Constants.ERROR_MESSAGE)%>
			</div>
			<script>
				$("#rolebtn").focus();
			</script>			
		<% }%> 			
	
		<div class="alert alert-danger" id="mandatoryerror" hidden="hidden">
			<ul>				
			</ul>
		</div>
		
		<div class="form-group">
			
			<label for="role"  class="col-sm-4 control-label">*Select your role</label>
			<div class="col-sm-5" align="left">
			<select name="registerBean.role" required="required" class="btn btn-default">
				<option value="1">Class owner</option>
				<option value="2">Class Teacher</option>
				<option value="3">Student</option>
			</select>
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
	    		<input type="text" pattern="[A-Za-z]{4,20}" class="form-control" name="registerBean.fname" id="fname" required="required" value='<s:property value="registerBean.fname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="mname" class="col-sm-4 control-label">Middle Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" maxlength="20" name="registerBean.mname" id="mname"  value='<s:property value="registerBean.mname" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="lname" class="col-sm-4 control-label">*Last Name</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" maxlength="20" name="registerBean.lname" id="lname" required="required"  value='<s:property value="registerBean.lname" />'/>
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
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="dobfield" name="registerBean.dob" required="required"  readonly value='<s:property value="registerBean.dob" />'/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					
				</div>
		</div>
		<div class="form-group">
			<label for="adr1" class="col-sm-4 control-label">*Address1</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.addr1"  id="addr1" required="required" maxlength="50" value='<s:property value="registerBean.addr1" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="adr2" class="col-sm-4 control-label">Address2</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.addr2"  id="addr2" maxlength="50" value='<s:property value="registerBean.addr2" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="city" class="col-sm-4 control-label">*City</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="registerBean.city" id="city" required="required" maxlength="20"  value='<s:property value="registerBean.city" />'/>
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
			
			<input type="hidden" class="form-control" name="registerBean.state" id="state" required="required" value='<s:property value="registerBean.state" default="-1"/>'/>
			<input type="hidden"  value='<s:property value="registerBean.state" default="-1"/>' />
			<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" id="statebtn">
						State <span class="caret"></span>
					</button>
					<ul class="dropdown-menu scrollable-menu" role="menu" >
						<li value="Andhra Pradesh">Andhra Pradesh</li>
						<li value="Arunachal Pradesh">Arunachal Pradesh<li>
						<li value="Assam">Assam</li>
						<li value="Bihar">Bihar</li>
						<li value="Chhattisgarh">Chhattisgarh</li>
						<li value="Goa">Goa</li>
						<li value="Gujarat">Gujarat</li>
						<li value="Haryana">Haryana</li>
						<li value="Himachal Pradesh">Himachal Pradesh</li>
						<li value="Jammu and Kashmir">Jammu and Kashmir</li>
						<li value="Jharkhand">Jharkhand</li>
						<li value="Karnataka">Karnataka</li>
						<li value="Kerala">Kerala</li>
						<li>Madhya Pradesh</li>
						<li>Maharashtra</li>
						<li>Manipur</li>
						<li>Meghalaya</li>
						<li>Mizoram</li>
						<li>Nagaland</li>
						<li>Orissa</li>
						<li>Punjab</li>
						<li>Rajasthan</li>
						<li>Sikkim</li>
						<li>Tamil Nadu</li>
						<li>Telangana</li>
						<li>Tripura</li>
						<li>Uttar Pradesh</li>
						<li>Uttarakhand</li>
						<li>West Bengal</li>
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
				<input type="tel" class="form-control" name="registerBean.phone1" id="phone1" required="required" maxlength="10" value='<s:property value="registerBean.phone1" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="phone2" class="col-sm-4 control-label">Phone 2</label>
			<div class="col-sm-5">
				<input type="tel" class="form-control" name="registerBean.phone2" id="phone2" maxlength="10" value='<s:property value="registerBean.phone2" />'/>
			</div>	
		</div>
		<div class="form-group" id="divClassname">
			<label for="classname" class="col-sm-4 control-label">*Class Name</label>
			<div class="col-sm-5">	
				<input type="text" maxlength="20" class="form-control" name="registerBean.className" id="classname" required="required"  value='<s:property value="registerBean.className" />'/>
			</div>
		</div>
		
		<div id="emailError" style="display: none;color: red;">This Email id is already registered</div>
		<div class="form-group" id="divemail">
			<label for="email" class="col-sm-4 control-label">*Email ID</label>
			<div class="col-sm-5">	
				<input type="email" maxlength="100" class="form-control" name="registerBean.email" id="email" required="required"  value='<s:property value="registerBean.email" />'/>
			</div>
		</div>
		
		<div id="lgnameError" style="display: none;color: red;">This login name is already registered</div>
		<div class="form-group">
			<label for="loginname" class="col-sm-4 control-label">*Desired Login Name</label>
			<div class="col-sm-5">
				<input type="text" maxlength="20" class="form-control" name="registerBean.loginName" id="loginname" required="required"  value='<s:property value="registerBean.loginName" />'/>
			</div>
		</div>
		<div class="form-group">
			<label for="loginpass" class="col-sm-4 control-label">
			<!-- <button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="left" title=""><i class="glyphicon glyphicon-info-sign"></i></button> -->	
			<i class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="bottom" title="" style="color: red;"></i>*Password</label>
			<div class="col-sm-5">
				<div>
					<input type="password" maxlength="20" class="form-control" name="registerBean.loginPass" id="loginpass" required="required" />
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
				<input type="password" maxlength="20" class="form-control" name="registerBean.loginPassRe" id="loginpassre" equalTo="#loginpass" required="required" />
			</div>
		</div>	
		
		<div class="form-group">
			<label for="submit"  class="col-sm-4 control-label"> </label>
			<div class="col-sm-5">
				<input type="button" onclick="go();" value="Submit" class="btn btn-default"/>
			</div>
		</div>
	
	</form>
</body>
</html>