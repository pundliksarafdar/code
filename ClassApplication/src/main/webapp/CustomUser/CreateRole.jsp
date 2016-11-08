<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<style type="text/css">
	.well{
	background: white;
	padding-left: 3%
	}
	hr{
	border-top: 1px solid black;
	}
	
	</style>
	<script type="text/javascript">
	var optionSelect = {
			onText:"Allow",
			offText:"Deny",
			onColor:"success",
			offColor:"danger"
		};
	$(document).ready(function(){
		$("input[type=\"checkbox\"]").bootstrapSwitch(optionSelect);
		$("#lectureCheckbox").bootstrapSwitch("destroy");
		$('input[class="parentModule"]').on('switchChange.bootstrapSwitch',function(e){
			if(!$(this).is(":checked")){
				$($(this).closest(".module")).find(".childModuleDiv").find("input").bootstrapSwitch('state',false);
				$($(this).closest(".module")).find(".childModuleDiv").find("input").bootstrapSwitch('disabled',true);
			}else{
				$($(this).closest(".module")).find(".childModuleDiv").find("input").bootstrapSwitch('disabled',false);
			}
		});
		
		$("#create").click(function(){
			var parentModule = [];
			var childModule = [];
			var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9-_\s]{1,}$/;
			var validationFlag = true;
			var lecturer = false;
			$(".error").html("");
			var roledesc = $("#roledesc").val().trim();
			$. each($(".parentModule:checked"), function(){
				parentModule. push($(this). val());
				});
			$. each($("input[name='childModule']:checked"), function(){
				childModule. push($(this). val());
				});
			console.log(parentModule);
			console.log(childModule);
			if(roledesc == ""){
				$("#roledescError").html("Enter Role Description");
				validationFlag = false;
			}else if(!CHAR_AND_NUM_VALIDATION.test(roledesc)){
				$("#roledescError").html("Invalid Role Description.Only - and _ special character allowed.");
				validationFlag = false;
			}
			if($("input[name=lectureCheckbox]").prop("checked") == true){
				lecturer = true;
			}
			if(validationFlag){
			var inst_roll = {};
			inst_roll.roll_desc = roledesc;
			inst_roll.parent_mod_access = parentModule.join(",");
			inst_roll.child_mod_access = childModule.join(",");
			inst_roll.teacher = lecturer;
			var handlers = {};
			handlers.success = function(resp){
				if(resp){
				 $.notify({message: "Roll added successfuly"},{type: 'success'});
				}else{
					$("#roledescError").html("Role with same description already present,enter different description");	
				}
			}
			handlers.error = function(){
			}
			rest.post("rest/customuserservice/saveinstroll/",handlers,JSON.stringify(inst_roll));
			}
		});
	});
	</script>
	</head>
	<body>
	<jsp:include page="RoleHeaders.jsp" >
		<jsp:param value="active" name="customeUserCreaterole"/>
	</jsp:include>
	<div class="container" style="margin-top: 2%">
		<div class="row">
			<div class="col-md-2"><label for="roledesc">Role Description </label></div>
			<div class="col-md-3"><input type="text" class="form-control" id="roledesc" maxlength="50">
								 <span id="roledescError" class="error"></span></div>
		</div>
		
		<div class="row">
			<div class="col-md-2"><b><u>Role Access</u></b></div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Class | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="1"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Class</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="1"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="2"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="3"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Subject | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="2"/></div>
		</div><div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Subject</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="4"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="5"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="6"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Batch | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="3"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Batch</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="7"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="8"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="9"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Student | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="4"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Student</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="10"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="11"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="12"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Teacher | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="5"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Teacher</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="13"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="14"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="15"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Notes | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="6"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Notes</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="16"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="17"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="18"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Question | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="7"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Question</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="19"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="20"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="21"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Timetable | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="8"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Timetable</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="22"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="23"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="24"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Exam | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="9"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Question Paper Pattern</div>
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="25"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="26"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="27"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Set Question Paper</div>
		<div class="col-md-3">Set <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="28"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="29"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="30"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Exam</div>
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="31"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="32"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="33"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Online Exam </div>
		<div class="col-md-3">Solve <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="34"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Marks | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="10"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Marks</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="35"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="36"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="37"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Progress Card </div>
		<div class="col-md-3">Print <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="38"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Attendance | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="11"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Attendance</div>
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="39"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="40"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="41"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Fee | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="12"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Fee Structure</div>
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="42"/></div>
		<div class="col-md-3">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="43"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="44"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Link Fee Structure </div>
		<div class="col-md-3">Link <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="45"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Student Fees</div> 
		<div class="col-md-3">Add <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="46"/></div>
		<div class="col-md-3">Print receipt <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="47"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Alert System | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="13"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Certificate | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="14"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Certificate</div> 
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="48"/></div>
		<div class="col-md-2">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="49"/></div>
		<div class="col-md-2">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="50"/></div>
		<div class="col-md-2">Print <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="51"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Setting | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="15"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Role | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="16"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Role</div> 
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="52"/></div>
		<div class="col-md-2">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="53"/></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">User</div> 
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="54"/></div>
		<div class="col-md-2">Edit <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="55"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Notice | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="18"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Notice</div> 
		<div class="col-md-3">Create <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="58"/></div>
		<div class="col-md-3">Delete <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="59"/></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Syllabus Planner | <input type="checkbox" id="parentModule" name="parentModule" data-size="mini" class="parentModule" value="17"/></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Syllabus Planner</div> 
		<div class="col-md-3">Plan<input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="56"/></div>
		<div class="col-md-3">Approve <input type="checkbox" id="childModule" name="childModule" data-size="mini" disabled="disabled" value="57"/></div>
		</div>
		</div>
		
		<div class="row">
		<input type="checkbox" id="lectureCheckbox" name="lectureCheckbox" /> Lecturer
		</div>
		<div class="row">
		<button id="create" class="btn btn-sm btn-success">Save</button>
		</div>
	</div>
	</body>
	</html>