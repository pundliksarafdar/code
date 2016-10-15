<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style type="text/css">
#batchTable .editEnabled .editable{
	display:block;
}

#batchTable .editEnabled .default{
	display:none;
}

#batchTable .editable{
	display:none;
}

#batchTable .default{
	display:show;
}

</style>
<script>
	/**/
	var selectSubject;
	var subjectData;
	var child_mod_access = [];
	/*
	function
	*/
	
	function filterBatchTable(){
		$('#batchTable').DataTable().search(
			$('#batchTableSearchCustom').val()
			).draw();
	}
	var that = "";
	function deleteBatchPrompt(){
		var batchIdToDelete = $(this).closest("tr").find("#batchId").val();
		var divisionId = $(this).closest("tr").find("#divisionId").val();
		var batchName = $(this).closest("tr").find(".defaultBatchName").html();
		that = $(this);
		deleteBatchConfirm(divisionId,batchIdToDelete,batchName);	
	}
	
	function deleteBatchConfirm(divisionId,batchIdToDelete,batchName){
		modal.modalConfirm("Delete","Do you want to delete "+batchName+"?All data related to this batch will get deleted.","Cancel","Delete",deleteBatch,[divisionId,batchIdToDelete]);
	}
	
	function deleteBatch(divisionId,batchIdToDelete){
				var handlers = {};
				handlers.success = function(){
					var table = $("#batchTable").DataTable();
					that.closest("tr").addClass('selected');
					table.row('.selected').remove().draw( false );
					$.notify({message: "Batch successfuly deleted"},{type: 'success'});
				}
				handlers.error = function(){
					$.notify({message: "Batch not deleted"},{type: 'danger'});
				}
				rest.deleteItem("rest/customuserservice/deleteBatch/"+divisionId+"/"+batchIdToDelete,handlers);
	}
	
	function enableEdit(){
		//Cancel other edit row if any
		$("#batchTable").find(".editEnabled").find(".btn-cancel").trigger("click");
		
		selectSubject = $(this).closest("tr").find(".selectSubject").val();
		var subjectName = $(this).closest("tr").find(".defaultBatchName").text().trim();
		$(this).closest("tr").find(".editBatchName").val(subjectName);
		$(this).closest("tr").addClass("editEnabled");
	}

	function cancelEdit(){
		$(this).closest("tr").find(".selectSubject").val(selectSubject).change();
		$(this).closest("tr").removeClass("editEnabled");
		$(this).closest("tr").find(".error").remove();
	}
	
	function validateElement(batchName,selectSubject,divisionId){
		$(batchName).closest("div").find(".error").remove();
		$(selectSubject).closest("div").find(".error").remove();
		var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9-_\s]{1,}$/;
		isValid = true;
		if(batchName.val().trim()==""){
			batchName.after("<div class='error'>Batch name cannot be blank</div>");
			isValid = false;
		}else if(!CHAR_AND_NUM_VALIDATION.test(batchName.val().trim())){
			batchName.after("<div class='error'>Only characters are allowed</div>");
			isValid = false;
		}
		if(selectSubject.val() == null || selectSubject.val().length < 1){
			selectSubject.closest("div").append("<div class='error'>Select Subject</div>");
			isValid = false;
		}
		if(divisionId == "-1"){
			$('#divisionError').html("Select Class");
			isValid = false;
		}
		return isValid;
	}
	
	function saveBatch(){
		var batchId = $(this).closest("tr").find("#batchId").val();
		var divisionId = $(this).closest("tr").find("#divisionId").val();
		var batchName = $(this).closest("tr").find(".editBatchName").val();
		var subjectIds = $(this).closest("tr").find(".selectSubject").val();
		
		var isValid = validateElement($(this).closest("tr").find(".editBatchName"),$(this).closest("tr").find(".selectSubject"),divisionId);
		if(isValid){
			saveBatchAjax(batchId,divisionId,batchName,subjectIds,$(this).closest("tr").find(".editBatchName"));
		}
		
	}
	
	function saveBatchAjax(batchId,batchdivisionid,batchName,subjectIds,that){
		if(subjectIds.length>0){
			subjectIds = subjectIds.join(',');
		}
		var batch = {};
		batch.batch_id = batchId
		batch.batch_name = batchName;
		batch.sub_id = subjectIds;
		batch.div_id = batchdivisionid;
		var handlers = {};
		handlers.success = function(data){
			 if(data != false){
					$.notify({message: "Batch successfuly added"},{type: 'success'});
					showAllBatch(data);		
				   }else{
					   that.after("<div class='error'>Batch with same name already exists</div>");
				   }
		}
		handlers.error = function(e){}
		rest.put("rest/customuserservice/updateBatch/",handlers,JSON.stringify(batch));
	}
	
	function getAllBatches(){
		var handlers = {};
		handlers.success = function(data){
			showAllBatch(data);
		}
		handlers.error = function(e){}
		rest.get("rest/customuserservice/getAllBatches/",handlers);
	}
	
	function showAllBatch(data){
		/* data = JSON.parse(data); */
		var dataTable = $('#batchTable').DataTable({
			bDestroy:true,
			data: data,
			lengthChange: false,
			columns: [{title:"#",data:null},
				{ title: "Name",data:"batch",render:function(data,event,row){
					var div = '<div class="default defaultBatchName">'+data.batch_name+'</div>';
					var input= '<input type="text" value="'+data.batch_name+'" class="form-control editable editBatchName">';
					var batchIdHidden = '<input type="hidden" value="'+data.batch_id+'" id="batchId">';
					var divisionIdHidden = '<input type="hidden" value="'+data.div_id+'" id="divisionId">';
					return div + input + batchIdHidden + divisionIdHidden;
				},sWidth:"20%"},
				{ title: "Division name",data:"division",render:function(data,event,row){
					return data.divisionName;
				},sWidth:"20%"},
				{ title: "Stream",data:"division",render:function(data,event,row){
					return data.stream;
				},sWidth:"20%"},
				{ title: "Subjects",data:"subjects",render:function(data,event,row){
					//subjectsPreSelected represents name of subject while in rowCallBack it represents id of subject
					var subjectsPreSelected = [];
					var subjectsStr = "";
					$.each(data,function(key,val){
						subjectsPreSelected.push(val.subjectName);
					});
					var selectTag = '<div class="editable"><select class="selectSubject" multiple="" style="width:100%"></select></div>';
					if(subjectsPreSelected.length>0){subjectsStr = subjectsPreSelected.join(",")}
					subjectsStr = '<div class="default">'+subjectsStr+'</div>';
					return subjectsStr + selectTag;
				},sWidth:"30%"},
				{ title: "Actions",data:null,render:function(data,event,row){
					var buttons = '<div class="default">'
						if($.inArray( "8", child_mod_access) != "-1"){
						buttons = buttons +'<input type="button" class="btn btn-xs btn-primary btn-batch-edit" value="Edit">&nbsp;';
						}if($.inArray( "9", child_mod_access) != "-1"){
						buttons = buttons + '<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete">';
						}
						buttons = buttons + '</div>'+
					'<div class="editable">'+
						'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>'
					console.log("butttons....");
					return buttons;
				},sWidth:"10%"}
			],rowCallback:function(row,data){
					var subjectsPreSelected = [];
					$.each(data.subjects,function(key,val){
						subjectsPreSelected.push(val.subjectId);
					});
					$(row).find(".selectSubject").select2({data:subjectData}).val(subjectsPreSelected).change();
				}
		});
		dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw(); 
	}
	
	function getAllSubjects(){
		 var subjectDataArray = [];
		var handlers = {};
		handlers.success = function(data){
			$.each(data,function(key,val){
				var data = {};
				data.id = val.subjectId;
				data.text = val.subjectName;
				subjectDataArray.push(data);
			});
			$("#addBatchSelectSubject").select2({data:subjectDataArray,placeholder:"Select subjects for batch"});
		}
		handlers.error = function(e){}
		rest.get("rest/customuserservice/allInstituteSubjects/",handlers);
		return subjectDataArray;
	}
	
	function addBatch(){
		$(".alert-add-batch-error").addClass("hide");
		var batchName = $("#addBatchBatchName").val();
		var	divisonNameT = $("#addBatchSelectDivision").val();
		var	subject = $("#addBatchSelectSubject").val();
		$('#divisionError').html("");
		if(subject){
			subject = subject.join(",");
		}else{
			subject = "";
		}
		
		var isValid = validateElement($("#addBatchBatchName"),$("#addBatchSelectSubject"),divisonNameT);
		if(isValid){
			addBatchConfirm(batchName,divisonNameT.toString(),subject);
		}
	}
	
	function addBatchConfirm(batchName,divisionName,subject){
			var batch = {};
			batch.batch_name = batchName;
			batch.sub_id = subject;
			batch.div_id = divisionName;
			var handlers = {};
			handlers.success = function(data){
				 if(data != false){
						$.notify({message: "Batch successfuly added"},{type: 'success'});
						showAllBatch(data);		
					   }else{
						   $("#addBatchBatchName").after("<div class='error'>Batch with same name already exists</div>");
					   }
			}
			handlers.error = function(e){}
			rest.post("rest/customuserservice/addBatch/",handlers,JSON.stringify(batch));
			
	}
	
	$(document).ready(function(){
		child_mod_access  = $("#accessRights").val().split(",");
		getAllBatches();
		subjectData = getAllSubjects();
		$(this).on("click",".btn-batch-delete",deleteBatchPrompt)
			.on("click",".btn-batch-edit",enableEdit)
			.on("click",".btn-cancel",cancelEdit)
			.on("click",".btn-save",saveBatch)
			.on("click",".btn-add-batch",addBatch);	
		
		$('#batchTableSearchCustom').on("keyup click",filterBatchTable);
		//$("#batchTable").DataTable({lengthChange: false});
	});
</script>
</head>
<body>
<jsp:include page="ClassSubjectNBatchHeaders.jsp" >
		<jsp:param value="active" name="customUserManageBatch"/>
	</jsp:include>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
 <% if(ArrayUtils.contains(child_mod_access,"7")){ %>
<div class="well">
  <div class="row">
  <div class="col-md-3">
	<input type="text" class="form-control" id="addBatchBatchName" placeholder="Batch name">
	<span id='batchNameError' class='error'></span>
	</div>
	 <div class="col-md-3">
	<select id="addBatchSelectDivision" class="form-control">
		<option value="-1">Select Class</option>
		<c:forEach items="${batcheids}" var="divId" varStatus="counter">
				<option value='<c:out value="${divId}"></c:out>'><c:out value="${divisionNames[counter.index]}"></c:out> &nbsp; <c:out value="${streams[counter.index]}"></c:out></option>
		</c:forEach>
	</select>
	<span id='divisionError' class='error'></span>
 	</div>
 	<div class="col-md-3">
		<select id="addBatchSelectSubject" multiple="multiple" style="width:100%"></select>
	</div>
	<div class="col-md-2">
		<input type="button" class="btn btn-primary btn-add-batch" value="Add"/>
	</div>
  </div>
  </div>
<%} %>
<div class="alert alert-danger alert-edit-batch-error hide"></div>
<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
<div>
<table id="batchTable" class="table table-striped" width="100%"></table>
</div>
  
</body>
</html>