<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style type="text/css">

.classTable .editEnabled .editable{
	display:block;
}

.classTable .editEnabled .default{
	display:none;
}

.classTable .editable{
	display:none;
}

.classTable .default{
	display:show;
}

.editclassnameerror, .editstreamerror, .addstreamerror, .addclassnameerror{
	color: red;
}
</style>
<!-- <script type="text/javascript" src="js/Addclass.js"></script> -->
<script>

var child_mod_access  = $("#accessRights").val(); 
var BUTTONS_MANAGE = '<div class="default">' +
						'<button class="btn btn-primary btn-xs btn-edit">Edit</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-delete">Delete</button>'+
					'</div>';
					
var BUTTONS_CANCEL = '<div class="editable">' +
						'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>';
					
var class_NAME = '<input type="hidden" class="hide editclassId" value="{{editclassIdValue}}">'+
					'<div class="default defaultclassName">{{defaultclassNameValue}}</div>'+
					'<input type="text" value="{{editclassNameValue}}" class="form-control editable editclassName" maxlength="100">'+
					'<div class="editclassnameerror"></div>';
					
var stream_NAME =	'<div class="default defaultstreamName">{{defaultstreamNameValue}}</div>'+
					'<input type="text" value="{{editstreamNameValue}}" class="form-control editable editstreamName" maxlength="100">'+
					'<div class="editstreamerror"></div>';

function validateInput(inputText){
	var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9]{1,}$/;
	var isValidInput = CHAR_AND_NUM_VALIDATION.test(inputText);
	return isValidInput;
}
var classid="";
function getclass(subid){
	classid=subid;
	$("#editclass").val($("#sub"+subid).val());
	$('div#ModifyclassModal .error').hide();
	$('#ModifyclassModal').modal('toggle');
}

/*
New function
*/
function deleteclass(classIdToDelete){
	var handlers = {};
	handlers.success = function(){
		var table = $("#classTable").DataTable();
		$("#classTable").find(".editclassId[value='"+classIdToDelete+"']").closest("tr").addClass('selected');;
		table.row('.selected').remove().draw( false );
		$.notify({message: "Class successfuly deleted"},{type: 'success'});
	}
	handlers.error = function(){
		$.notify({message: "Class successfuly deleted"},{type: 'success'});
	}
	rest.deleteItem("rest/customuserservice/deleteDivision/"+classIdToDelete,handlers);
}

/*this function will prompt to delete class*/
function deleteclassPrompt(){
	var classIdToDelete = $(this).closest("tr").find(".editclassId").val();
	var className = $(this).closest("tr").find(".defaultclassName").html();
	var streamName =$(this).closest("tr").find(".defaultstreamName").html();
	deleteConfirm(classIdToDelete,className,streamName);
}

function deleteConfirm(classIdToDelete,className,streamName){
	modal.modalConfirm("Delete","Do you want to delete "+className+" "+streamName+"? All data related to this class will get deleted.","Cancel","Delete",deleteclass,[classIdToDelete]);
}

function enableEdit(){
	$("#classTable").find(".editEnabled").find(".btn-cancel").trigger("click");
	var className = $(this).closest("tr").find(".defaultclassName").text().trim();
	var streamname = $(this).closest("tr").find(".defaultstreamName").text().trim();
	$(this).closest("tr").find(".editclassName").val(className);
	$(this).closest("tr").find(".editstreamName").val(streamname);
	$(this).closest("tr").addClass("editEnabled");
	//$(this).closest("tr").find(".editclassName").val("");
}

function cancelEdit(){
	$(this).closest("tr").find(".editclassnameerror").empty();
	$(this).closest("tr").find(".editstreamerror").empty();
	$(this).closest("tr").removeClass("editEnabled");
}

function manageLink(){
	var classIdTo = $(this).closest("tr").find(".editclassId").val();
	var classname = $(this).closest("tr").find(".defaultclassName").text();
	var link = "addtopics?actionname=initiate&subid="+classIdTo+"&subname="+classname;
	location.href = link;
}

function saveNewclassName(){
	var that = $(this);
	$(this).closest("tr").find(".editclassnameerror").empty();
	$(this).closest("tr").find(".editstreamerror").empty();
	var classNameToEdit = $(this).closest("tr").find(".editclassName").val();
	var classIdToEdit = $(this).closest("tr").find(".editclassId").val();
	var stream = $(this).closest("tr").find(".editstreamName").val();
	var flag=false;
	if(!classNameToEdit || classNameToEdit.trim()==""){
		$(this).closest("tr").find(".editclassnameerror").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> class name cannot be blank');
		flag=true;
	}else if(!validateInput(classNameToEdit)){
		$(this).closest("tr").find(".editclassnameerror").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Only character and numbers are allowed');
		flag=true;
	}
	
	if(!stream.trim()==""){
		if(!validateInput(stream)){
			$(this).closest("tr").find(".editstreamerror").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Only character and numbers are allowed');
			flag=true;
		}
		}
	
	if(flag==false){
		$(this).closest("tr").find(".editclassnameerror").empty();
		
		var division = {};
		division.divisionName = classNameToEdit;
		division.stream = stream;
		division.divId = classIdToEdit;
		var handlers = {};
		handlers.success = function(resp){
			if(resp == false){
			that.closest("tr").find(".editclassnameerror").html('Class already exists!!');	
			}else{
			$.notify({message: "Class successfuly updated"},{type: 'success'});
			that.closest("tr").find(".defaultclassName").text(classNameToEdit);		
			that.closest("tr").find(".defaultstreamName").text(stream);
			cancelEdit.apply(that);
			}
		}
		handlers.error = function(){
			$.notify({message: "Class not updated"},{type: 'danger'});
		}
		rest.post("rest/customuserservice/editClass",handlers,JSON.stringify(division));
	}

}
/*functions ends here*/

	var dataTable;
	var that;
	function filterGlobal() {
		$('#classTable').DataTable().search(
			$('#tableSearchCustom').val()
			).draw();
	}

	$(document).ready(function(){
		child_mod_access  = $("#accessRights").val().split(",");
		BUTTONS_MANAGE = '<div class="default">';
		if($.inArray( "2", child_mod_access) != "-1"){
			BUTTONS_MANAGE = BUTTONS_MANAGE +	'<button class="btn btn-primary btn-xs btn-edit">Edit</button>&nbsp;';
		}
		if($.inArray( "3", child_mod_access) != "-1" ){
			BUTTONS_MANAGE = BUTTONS_MANAGE +	'<button class="btn btn-danger btn-xs btn-delete">Delete</button>';
		}
		BUTTONS_MANAGE = BUTTONS_MANAGE +'</div>';
		$('body').on("click",".btn-edit",enableEdit)
			.on("click",".btn-cancel",cancelEdit)
			.on("click",".btn-save",saveNewclassName)
			.on("click",".btn-delete",deleteclassPrompt)
			.on("click",".btn-manage",manageLink);
			
		$('#tableSearchCustom').on("keyup click",filterGlobal);
		
		dataTable = $("#classTable").DataTable({oClasses:{"lengthChange": false},"columnDefs": [
            {
                "render": function ( data, type, row ) {
                    console.log(data);
					return "data";
                }
            },
        ]});
		
		dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw(); 
				
		$('#manageclassAddclass').on('click',function(){
			that=$(this);
		var className = $('#className').val();
		var streamName = $('#streamName').val();
		$(this).closest('.addclassContainer').find(".addclassnameerror").empty();
		$(this).closest('.addclassContainer').find(".addstreamerror").empty();
		var flag= false;
		if(!className || className.trim()==""){
			$(this).closest('.addclassContainer').find(".addclassnameerror").html('class name cannot be blank');
			flag=true;
		}else if(!validateInput(className)){
			$(this).closest('.addclassContainer').find(".addclassnameerror").html('Only character and numbers are allowed');
			flag=true;
		}
		if(!streamName.trim()==""){
		if(!validateInput(streamName)){
			$(this).closest('.addclassContainer').find(".addstreamerror").html('Only character and numbers are allowed');
			flag=true;
		}
		}
		if(flag==false){
			var division = {};
			division.divisionName = className;
			division.stream = streamName;
			var handlers = {};
			handlers.success = function(resp){
				successCallbackclass(resp)
			}
			handlers.error = function(){
				$.notify({message: "Class not added"},{type: 'danger'});
			}
			rest.post("rest/customuserservice/addClass",handlers,JSON.stringify(division));
		}
	});
		
	});
	
	function errorCallbackclass(e){
		/*
		$('div#addclassModal .progress').addClass('hide');
		$('div#addclassModal .add').removeClass('hide');
		$('div#addclassModal .error').show();
		$('div#addclassModal .error').html('<strong>Error!</strong> Unable to add');
		*/
	}

	function successCallbackclass(data){
		if(data != false){
		$.notify({message: "Class successfuly added"},{type: 'success'});
		dataTable = $('#classTable').DataTable({
			bDestroy:true,
			data: data,
			columns: [
				{title:"#",data:null},
				{ title: "Class Name",data:"divisionName",render:function(data,event,row){
					console.log(row);
					var modifiedObj = class_NAME.replace("{{defaultclassNameValue}}",row.divisionName);
					modifiedObj = modifiedObj.replace("{{editclassNameValue}}",row.divisionName);
					modifiedObj = modifiedObj.replace("{{editclassIdValue}}",row.divId);
					return modifiedObj;
				}},
				{ title: "Stream",data:"stream",render:function(data,event,row){
					console.log(row);
					var modifiedObj = stream_NAME.replace("{{defaultstreamNameValue}}",row.stream);
					modifiedObj = modifiedObj.replace("{{editstreamNameValue}}",row.stream);
					return modifiedObj;
				}},
				{ title: "Actions",data:null,render:function(data){
					return BUTTONS_MANAGE+BUTTONS_CANCEL;
					}}
			]
		});
		
	 dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); 
		}else{
			that.closest('.addclassContainer').find(".addclassnameerror").html('Class already exists!!');
		}
	}
	
	
</script>
</head>
<body>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<jsp:include page="ClassSubjectNBatchHeaders.jsp" >
		<jsp:param value="active" name="customeUserManageclass"/>
	</jsp:include>
  <% if(ArrayUtils.contains(child_mod_access,"1")){ %>
 <div class="well">
 <div class="row addclassContainer" >
    <div class="col-md-3">
      <input type="text" class="form-control" id="className" maxlength="25" placeholder="Enter Class">
      <div class="addclassnameerror"></div>
    </div>
    <div class="col-md-3">
       <input type="text" class="form-control" id="streamName" maxlength="25" placeholder="Enter Stream/Part(Optional)">
       <div class="addstreamerror"></div>
    </div>
    <!-- /input-group -->
    <div class="col-md-3">
        <button id="manageclassAddclass" class="btn btn-primary" type="button">Add Class</button>
      </div>
  </div>
</div>
 <%} %>
  <input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
 
<div class="container" style="padding-top: 2%">
 <table class="table table-striped classTable" id="classTable">
	<thead>
		<th>#</th><th>Class name</th><th>Stream/Part</th><th>Actions</th>
	</thead>
 <c:forEach items="${classes}" var="class" varStatus="counter">
 	<tr>
 		<td><%-- <c:out value="${counter.count}"></c:out> --%></td>
 		<td>
			<input type="hidden" class="hide editclassId" value='<c:out value="${class.divId}"></c:out>'/>
			<div class="default defaultclassName"><c:out value="${class.divisionName}"></c:out></div>
			<input type="text" value='<c:out value="${class.divisionName}"></c:out>' class="form-control editable editclassName" maxlength="100"/>
			<div class="editclassnameerror"></div>
		</td>
		<td>
			<div class="default defaultstreamName"><c:out value="${class.stream}"></c:out></div>
			<input type="text" value='<c:out value="${class.stream}"></c:out>' class="form-control editable editstreamName" maxlength="100"/>
			<div class="editstreamerror"></div>
		</td>
 		<td>
			<div class="default">
			 <% if(ArrayUtils.contains(child_mod_access,"2")){ %>
				<button class="btn btn-primary btn-xs btn-edit">Edit</button>
			<%} %>	
			 <% if(ArrayUtils.contains(child_mod_access,"3")){ %>	
				<button class="btn btn-danger btn-xs btn-delete">Delete</button>
			<%} %>
			</div>
			<div class="editable">
				<button class="btn btn-success btn-xs btn-save">Save</button>
				<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>
			</div>
		</td>
 		
 	</tr>
 </c:forEach>
 </table>
 </div>
</body>
</html>