<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style type="text/css">
.dataTables_filter {
     display: none;
}

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
		that = $(this);
		deleteBatchConfirm(divisionId,batchIdToDelete);	
	}
	
	function deleteBatchConfirm(divisionId,batchIdToDelete){
		modal.modalConfirm("Delete","Do you want to delete","Cancel","Delete",deleteBatch,[divisionId,batchIdToDelete]);
	}
	
	function deleteBatch(divisionId,batchIdToDelete){
				/* $.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "deleteBatch",
						 regId:'',
						 batchdivisionid:divisionId,
						 batchId:batchIdToDelete
				   		},
				   type:"POST",
				   success:function(data){
					   $.notify({message: "Batch successfuly deleted"},{type: 'success'});
					   $('input[type="hidden"]#batchId[value="'+batchIdToDelete+'"]').closest("tr").remove();
				   	},
				   error:function(data){
				
				   }
			}); */
				var handlers = {};
				handlers.success = function(){
					that.closest("tr").remove();
					$.notify({message: "Batch successfuly deleted"},{type: 'success'});
				}
				handlers.error = function(){
					that.closest("tr").remove();
					$.notify({message: "Batch successfuly deleted"},{type: 'success'});
				}
				rest.deleteItem("rest/commonDelete/deleteBatch/"+divisionId+"/"+batchIdToDelete,handlers);
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
	
	function validateElement(batchName,selectSubject){
		$(batchName).closest("div").find(".error").remove();
		$(selectSubject).closest("div").find(".error").remove();
		var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9 -_]{1,}$/;
		isValid = true;
		if(batchName.val().trim()==""){
			batchName.after("<div class='error'>Field cannot be blank</div>");
			isValid = false;
		}else if(!CHAR_AND_NUM_VALIDATION.test(batchName.val().trim())){
			batchName.after("<div class='error'>Only characters are allowed</div>");
			isValid = false;
		}
		if(selectSubject.val() == null || selectSubject.val().length < 1){
			selectSubject.closest("div").append("<div class='error'>Field cannot be blank</div>");
			isValid = false;
		}
		return isValid;
	}
	
	function saveBatch(){
		var batchId = $(this).closest("tr").find("#batchId").val();
		var divisionId = $(this).closest("tr").find("#divisionId").val();
		var batchName = $(this).closest("tr").find(".editBatchName").val();
		var subjectIds = $(this).closest("tr").find(".selectSubject").val();
		
		var isValid = validateElement($(this).closest("tr").find(".editBatchName"),$(this).closest("tr").find(".selectSubject"));
		if(isValid){
			saveBatchAjax(batchId,divisionId,batchName,subjectIds);
		}
		
	}
	
	function saveBatchAjax(batchId,batchdivisionid,batchName,subjectIds){
		if(subjectIds.length>0){
			subjectIds = subjectIds.join(',');
		}
		$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "updateBatch",
						 regId:'',
						 subIds:subjectIds,
						 batchId:batchId,
						 batchName:batchName,
						 batchdivisionid:batchdivisionid
				   		},
				   type:"POST",
				   success:function(data){					  
						getAllBatches();
				   	},
				   error:function(data){
				   
				   }
			});
	}
	
	function getAllBatches(){
		$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getAllBatches"
				   		},
				   type:"POST",
				   success:function(data){					  
						showAllBatch(data);
				   	},
				   error:function(data){
				   
				   }
			});
	}
	
	function showAllBatch(data){
		data = JSON.parse(data);
		var dataTable = $('#batchTable').DataTable({
			bDestroy:true,
			data: data.instituteBatches,
			lengthChange: false,
			columns: [
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
				{ title: "Manage",data:null,render:function(data,event,row){
					var buttons = '<div class="default">'+
						'<input type="button" class="btn btn-xs btn-warning btn-batch-edit" value="Edit">'+
						'<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete">'+
					'</div>'+
					'<div class="editable">'+
						'<button class="btn btn-success btn-xs btn-save">Save</button>'+
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
		
	}
	
	function getAllSubjects(){
		var subjectDataArray = [];
		var subjectData = {};
		$.ajax({
				 url: "classOwnerServlet",
				async:false,
				   data: {
				    	 methodToCall: "getAllSubjects"
				   		},
				   type:"POST",
				   success:function(data){
						subjectData = JSON.parse(data);
				   	},
				   error:function(data){
				   
				   }
			});
			
			$.each(subjectData.instituteSubjects,function(key,val){
				var data = {};
				data.id = val.subjectId;
				data.text = val.subjectName;
				subjectDataArray.push(data);
			});
			
			return subjectDataArray;
	}
	
	function addBatch(){
		$(".alert-add-batch-error").addClass("hide");
		var batchName = $("#addBatchBatchName").val();
		var	divisonNameT = $("#addBatchSelectDivision").val();
		var	subject = $("#addBatchSelectSubject").val();
		
		if(subject){
			subject = subject.join(",");
		}else{
			subject = "";
		}
		
		var isValid = validateElement($("#addBatchBatchName"),$("#addBatchSelectSubject"));
		if(isValid){
			addBatchConfirm(batchName,divisonNameT.toString(),subject);
		}
	}
	
	function addBatchConfirm(batchName,divisionName,subject){
			$.ajax({
			   url: "classOwnerServlet",
			   data: {
					 methodToCall: "addBatch",
					 regId:'',
					 batchName:batchName,
					 divisionName:divisionName,
					 subjectname:subject
					},
			   type:"POST",
			   success:function(e){
				   e = JSON.parse(e);
				   if(e.status != "error"){
					$.notify({message: "Batch successfuly added"},{type: 'success'});
					getAllBatches();		
				   }else{
					   $("#addBatchBatchName").after("<div class='error'>Batch with same name already exists</div>");
				   }
				},
			   error:function(e){
					$.notify({message: "Error"},{type: 'danger'});
				}
			});
			
	}
	
	$(document).ready(function(){
		getAllBatches();
		subjectData = getAllSubjects();
		$("#addBatchSelectSubject").select2({data:subjectData,placeholder:"Select subjects for batch"});
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
		<jsp:param value="active" name="managebatch"/>
	</jsp:include>

<div class="well">
  <div class="row">
  <div class="col-md-3">
	<input type="text" class="form-control" id="addBatchBatchName" placeholder="Batch name">
	<span id='batchNameError' class='error'></span>
	</div>
	 <div class="col-md-3">
	<select id="addBatchSelectDivision" class="form-control">
		<!--Thought it is batch id written its division id only-->
		<c:forEach items="${batcheids}" var="divId" varStatus="counter">
				<option value='<c:out value="${divId}"></c:out>'><c:out value="${divisionNames[counter.index]}"></c:out> &nbsp; <c:out value="${streams[counter.index]}"></c:out></option>
		</c:forEach>
	</select>
 	</div>
 	<div class="col-md-3">
		<select id="addBatchSelectSubject" multiple="multiple" style="width:100%"></select>
	</div>
	<div class="col-md-2">
		<input type="button" class="btn btn-success btn-add-batch" value="Add"/>
	</div>
  </div>
  </div>
<div class="container">
 <div class="row">
  <div class="col-lg-offset-9 col-lg-3">
	<input type="text" class="form-control" id="batchTableSearchCustom" placeholder="search">
 </div>
 </div>
</div>


<div class="alert alert-danger alert-edit-batch-error hide"></div>

<div>
<table id="batchTable" class="table table-hover" width="100%"></table>
</div>
  
</body>
</html>