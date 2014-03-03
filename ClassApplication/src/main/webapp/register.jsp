<%@taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
	$(document).ready(function(){
		$('#role').val("");
	});
	
	function validate(){
		if(validateBlank()){
			if(isPasswordValid()){
				return true;	
			}else{
				return false;	
			}
		}else{
			isPasswordValid();
			return false;			
		}		
	}
	
	function validateBlank(){
		$('#mandatoryerror ul').text("");
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
			$('#mandatoryerror ul').append("<li>Field marked with the * are mandatory</li>");
			$('#mandatoryerror').show();
		}
		return isValid;
	}
	
	function addRole(role){
		if(role==0){
			$('#rolebtn').html('Admin <span class="caret"></span>');
		}else if(role==1){
			$('#rolebtn').html('ClassOwner <span class="caret"></span>');
		}else if(role==2){
			$('#rolebtn').html('ClassTeacher <span class="caret"></span>');
		}else{
			$('#rolebtn').html('Student <span class="caret"></span>');
		}
		$('#role').val(role);
	}
	
	function go(){
		if(validate()){
			$('#regform').submit();
		}
	}

	function isPasswordValid(){
		var ispassvalid = false;
		if($('#loginpass').val()===$('#loginpassre').val()){
			ispassvalid = true;
		}else{
			$('#loginpass').val("");
			$('#loginpassre').val("");
			$('#loginpass').parents('.form-group').addClass('has-error');
			$('#mandatoryerror ul').append("<li>Reentered password should match</li>");
			$('#mandatoryerror').show();
			ispassvalid = false;
		}
			return ispassvalid;
	}
</script>

<form id="regform" action="/registeruser" method="post" role="form" class="form-horizontal">
<s:if test="hasActionErrors()">
   <div class="alert alert-danger" id="mandatoryerror" hidden="hidden">
	   <s:actionerror/>
   </div>
</s:if>
<fieldset>
	<div class="alert alert-danger" id="mandatoryerror" hidden="hidden">
		<ul>
			
		</ul>
	</div>
	<div class="form-group">
    	<label for="fname" class="col-sm-2 control-label">*First Name</label>
    	<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.fname" id="fname" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="mname" class="col-sm-2 control-label">Middle Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.mname" id="mname"/>
		</div>
	</div>
	<div class="form-group">
		<label for="lname" class="col-sm-2 control-label">*Last Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.lname" id="lname" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="dob" class="col-sm-2 control-label" >*Date of Birth</label>
		<div class="col-sm-10">
				<!-- 
				<div id="datetimepicker" class="input-append date">
					<input type="text"></input> <span class="add-on icon icon-calendar"> <i
						data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
					</span>
				</div>
				-->
				
				<div id="datetimepicker" class="input-group" style="width :250px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text"  name="registerBean.dob" required="required"/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				
			</div>
	</div>
	<div class="form-group">
		<label for="adr1" class="col-sm-2 control-label">*Address1</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.addr1"  id="adr1" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="adr2" class="col-sm-2 control-label">Address2</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.addr2"  id="adr2"/>
		</div>
	</div>
	<div class="form-group">
		<label for="city" class="col-sm-2 control-label">*City</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.city" id="city" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="state" class="col-sm-2 control-label">*State</label>
		<div class="col-sm-10">	
			<input type="text" class="form-control" name="registerBean.state" id="state"  required="required"/>
		</div>	
	</div>
	<div class="form-group">
		<label for="country" class="col-sm-2 control-label">*Country</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.country" id="country" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="phone1" class="col-sm-2 control-label">*Phone 1</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.phone1" id="phone1" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="phone2" class="col-sm-2 control-label">Phone 2</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.phone2" id="phone2"/>
		</div>	
	</div>
	<div class="form-group">
		<label for="classname" class="col-sm-2 control-label">*Class Name</label>
		<div class="col-sm-10">	
			<input type="text" class="form-control" name="registerBean.className" id="classname" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="loginname" class="col-sm-2 control-label">*Desired Login Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" name="registerBean.loginName" id="loginname" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="loginpass" class="col-sm-2 control-label">*Password</label>
		<div class="col-sm-10">
			<input type="password" class="form-control" name="registerBean.loginPass" id="loginpass" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="loginpassre" class="col-sm-2 control-label">*Re-Enter Password</label>
		<div class="col-sm-10">
			<input type="password" class="form-control" name="registerBean.loginPassRe" id="loginpassre" required="required" />
		</div>
	</div>
	<div class="form-group">
		<label for="role"  class="col-sm-2 control-label">Select your role</label>
		<div class="col-sm-10">
		<input type="hidden" class="form-control" name="registerBean.role" id="role" value=""  required="required"/>
		<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle"
					data-toggle="dropdown" id="rolebtn">
					Role <span class="caret"></span>
				</button>
				<ul class="dropdown-menu" role="menu">
					<li><a href="javascript:addRole('0')">Admin</a></li>
					<li><a href="javascript:addRole('1')">Class Owner</a></li>
					<li><a href="javascript:addRole('2')">Class Teacher</a></li>
					<li><a href="javascript:addRole('3')">Student</a></li>
				</ul>
		</div>
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
		<label for="submit"  class="col-sm-2 control-label"> </label>
		<div class="col-sm-10">
			<input type="button" onclick="go();" value="Submit" class="btn btn-default"/>
		</div>
	</div>
</fieldset>
</form>
