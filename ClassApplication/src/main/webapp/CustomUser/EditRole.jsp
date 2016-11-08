<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var optionSelect = {
		onText:"Allow",
		offText:"Deny",
		onColor:"success",
		offColor:"danger"
	};
var child_mod_access  = [];
var inst_roll = {};
$(document).ready(function(){
	child_mod_access  = $("#accessRights").val().split(",");
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
	
	$("#roleTable").DataTable();
	$(".edit").click(function(){
		$(".error").html("");
		inst_roll.roll_id = $(this).prop("id");
		var handlers = {};
		handlers.success = function(resp){
		$("#editRoleDiv").show();
		$("#roleTableDiv").hide();
		var parentMod = [];
		parentMod = resp.parent_mod_access.split(",");
		var childMod = [];
		childMod = resp.child_mod_access.split(",");
		$. each(parentMod, function(index, value){
			$(".parentModule[value='"+value+"']").bootstrapSwitch('state',true);
			});
		$. each(childMod, function(index, value){
			$("#childModule[value='"+value+"']").bootstrapSwitch('state',true);
			});
		$("#roledesc").val(resp.roll_desc);
		if(resp.teacher == true){
			$("input[name=lectureCheckbox]").prop("checked","true");
		}
		}
		handlers.error = function(){
		}
		rest.get("rest/customuserservice/getroll/"+$(this).prop("id"),handlers);
	});
	
	$("#editBack").click(function(){
		$("input[name=lectureCheckbox]").prop("checked",false);
		$(".parentModule").bootstrapSwitch('state',false);
		$('input[name="childModule"]').bootstrapSwitch('state',false);
		$("#editRoleDiv").hide();
		$("#roleTableDiv").show();	
	});
	
	$(".view").click(function(){
		$(".viewParentModule").css("color","red");
		$(".viewChildModule").css("color","red");
		$(".viewLecturer").css("color","red");
		$("#roleTableDiv").hide();
		$("#viewModuleDiv").show();
		var handlers = {};
		handlers.success = function(resp){
		var parentMod = [];
		parentMod = resp.parent_mod_access.split(",");
		var childMod = [];
		childMod = resp.child_mod_access.split(",");
		$. each(parentMod, function(index, value){
			$(".viewParentModule[value='"+value+"']").css("color","lime");
			});
		$. each(childMod, function(index, value){
			$(".viewChildModule[value='"+value+"']").css("color","lime");
			});
		$("#viewroledesc").html(resp.roll_desc);
		if(resp.teacher == true){
			$(".viewLecturer").css("color","lime");
		}
		}
		handlers.error = function(){
		}
		rest.get("rest/customuserservice/getroll/"+$(this).prop("id"),handlers);
	});
	
	$("#viewBack").click(function(){
		$("#roleTableDiv").show();
		$("#viewModuleDiv").hide();
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
		inst_roll.roll_desc = roledesc;
		inst_roll.parent_mod_access = parentModule.join(",");
		inst_roll.child_mod_access = childModule.join(",");
		inst_roll.teacher = lecturer;
		var handlers = {};
		handlers.success = function(resp){
			if(resp){
			$($(".edit[id='"+inst_roll.roll_id+"']").closest("tr")).find(".tableRoleDesc").html(inst_roll.roll_desc);
			$("#editBack").click();
			 $.notify({message: "Roll updated successfuly"},{type: 'success'});
			}else{
				$("#roledescError").html("Role with same description already present,enter different description");	
			}
		}
		handlers.error = function(){
		}
		rest.put("rest/customuserservice/updateinstroll/",handlers,JSON.stringify(inst_roll));
		}
	});
});
</script>
</head>
<body>
<jsp:include page="RoleHeaders.jsp" >
		<jsp:param value="active" name="customeUserViewRole"/>
</jsp:include>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<div class="container" style="margin-top: 2%" id="roleTableDiv">
<table class="table table-striped" id="roleTable">
<thead>
	<tr>
		<th>#</th>
		<th>Role</th>
		<th>Actions</th>
	</tr>
</thead>
<tbody>
	<c:forEach items="${roleList}" var="role" varStatus="counter">
	<tr>
		<td><c:out value="${counter.count}"></c:out></td>
		<td><div class="tableRoleDesc"><c:out value="${role.roll_desc}"></c:out></div></td>
		<td>
			<button class="btn btn-xs btn-info view" id="<c:out value="${role.roll_id}"></c:out>">View</button>
			<% if(ArrayUtils.contains(child_mod_access,"53")){ %>
			<button class="btn btn-xs btn-primary edit" id="<c:out value="${role.roll_id}"></c:out>">Edit</button>
			<%} %>
		</td>
	</tr>
	</c:forEach>
</tbody>
</table>
</div>
<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
<div class="container" style="margin-top: 2%;display: none;" id="editRoleDiv">
	<div class="row">
	<button class="btn btn-xs btn-primary back" id="editBack">Back</button>
	</div>
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
	</div>
	
	
	<div id="viewModuleDiv" style="margin-top: 2%;display: none;" >
	<div class="container" style="margin-top: 2%">
		<div class="row">
		<button id="viewBack" class="btn btn-sm btn-primary">Back</button>
		</div>
		</div>
		<div class="container" style="margin-top: 2%">
		<div class="row">
			<div class="col-md-2"><label for="roledesc">Role Description </label></div>
			<div class="col-md-3"><span id="viewroledesc"></span></div>
		</div>
		
		<div class="row">
			<div class="col-md-2"><b><u>Role Access</u></b></div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Class | <span class="viewParentModule"  value="1"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Class</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="1"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="2"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="3"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Subject | <span class="viewParentModule"  value="2"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div><div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Subject</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="4"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="5"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="6"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Batch | <span class="viewParentModule"  value="3"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Batch</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="7"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="8"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="9"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Student | <span class="viewParentModule"  value="4"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Student</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="10"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="11"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="12"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Teacher | <span class="viewParentModule"  value="5"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Teacher</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="13"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="14"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="15"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Notes | <span class="viewParentModule"  value="6"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Notes</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="16"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="17"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="18"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Question | <span class="viewParentModule"  value="7"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Question</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="19"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="20"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="21"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Timetable | <span class="viewParentModule"  value="8"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Timetable</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="22"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="23"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="24"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Exam | <span class="viewParentModule"  value="9"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Question Paper Pattern</div>
		<div class="col-md-3">Create <span class="viewChildModule"  value="25"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="26"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="27"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Set Question Paper</div>
		<div class="col-md-3">Set <span class="viewChildModule"  value="28"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="29"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="30"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Exam</div>
		<div class="col-md-3">Create <span class="viewChildModule"  value="31"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="32"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="33"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Online Exam </div>
		<div class="col-md-3">Solve <span class="viewChildModule"  value="34"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Marks | <span class="viewParentModule"  value="10"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Marks</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="35"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="36"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="37"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Progress Card </div>
		<div class="col-md-3">Print <span class="viewChildModule"  value="38"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Attendance | <span class="viewParentModule"  value="11"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Attendance</div>
		<div class="col-md-3">Add <span class="viewChildModule"  value="39"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="40"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="41"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Fee | <span class="viewParentModule"  value="12"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Fee Structure</div>
		<div class="col-md-3">Create <span class="viewChildModule"  value="42"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Edit <span class="viewChildModule"  value="43"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="44"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Link Fee Structure </div>
		<div class="col-md-3">Link <span class="viewChildModule"  value="45"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Student Fees</div> 
		<div class="col-md-3">Add <span class="viewChildModule"  value="46"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Print receipt <span class="viewChildModule"  value="47"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Alert System | <span class="viewParentModule"  value="13"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Certificate | <span class="viewParentModule"  value="14"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Certificate</div> 
		<div class="col-md-3">Create <span class="viewChildModule"  value="48"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-2">Edit <span class="viewChildModule"  value="49"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-2">Delete <span class="viewChildModule"  value="50"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-2">Print <span class="viewChildModule"  value="51"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Setting | <span class="viewParentModule"  value="15"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Role | <span class="viewParentModule"  value="16"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Role</div> 
		<div class="col-md-3">Create <span class="viewChildModule"  value="52"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-2">Edit <span class="viewChildModule"  value="53"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">User</div> 
		<div class="col-md-3">Create <span class="viewChildModule"  value="54"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-2">Edit <span class="viewChildModule"  value="55"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Notice | <span class="viewParentModule"  value="18"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Notice</div> 
		<div class="col-md-3">Create <span class="viewChildModule"  value="58"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Delete <span class="viewChildModule"  value="59"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="well module">
		<div class="row">
			<div class="col-md-3">Syllabus Planner | <span class="viewParentModule"  value="17"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		<div class="row">
		<div class="col-md-12"><hr></div>
		</div>
		<div class="row childModuleDiv">
		<div class="col-md-3">Syllabus Planner</div> 
		<div class="col-md-3">Plan<span class="viewChildModule"  value="56"><span class="glyphicon  glyphicon-record"></span></span></div>
		<div class="col-md-3">Approve<span class="viewChildModule"  value="57"><span class="glyphicon  glyphicon-record"></span></span></div>
		</div>
		</div>
		
		<div class="row">
		<span class="viewLecturer"><span class="glyphicon  glyphicon-record"></span></span> Lecturer
		</div>
		</div>
	</div>
</body>
</html>