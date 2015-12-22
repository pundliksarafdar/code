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
	/*
	function
	*/
	
	function filterBatchTable(){
		$('#batchTable').DataTable().search(
			$('#batchTableSearchCustom').val()
			).draw();
	}
	
	function deleteBatchPrompt(){
		var batchIdToDelete = $(this).closest("tr").find("#batchId").val();
		var divisionId = $(this).closest("tr").find("#divisionId").val();
		deleteBatchConfirm(divisionId,batchIdToDelete);	
	}
	
	function deleteBatchConfirm(divisionId,batchIdToDelete){
		modal.modalConfirm("Delete","Do you want to delete","Cancel","Delete",deleteBatch,[divisionId,batchIdToDelete]);
	}
	
	function deleteBatch(divisionId,batchIdToDelete){
				$.ajax({
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
			});
	}
	
	function enableEdit(){
		var subjectName = $(this).closest("tr").find(".defaultBatchName").text().trim();
		$(this).closest("tr").find(".editBatchName").val(subjectName);
		$(this).closest("tr").addClass("editEnabled");
	}

	function cancelEdit(){
		$(this).closest("tr").removeClass("editEnabled");
	}
	
	function saveBatch(){
		var batchId = $(this).closest("tr").find("#batchId").val();
		var divisionId = $(this).closest("tr").find("#divisionId").val();
		var batchName = $(this).closest("tr").find(".editBatchName").val();
		var subjectIds = $(this).closest("tr").find(".selectSubject").val();
		saveBatchAjax(batchId,divisionId,batchName,subjectIds);
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
		console.log(data);
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
					var subjectsStr = "";
					var selectTag = '<div class="editable"><select class="selectSubject" multiple="" style="width:100%">';
					var subjects;
					$.each(data,function(key,val){
						selectTag = selectTag + '<option selected="selected" value="'+val.subjectId+'">'+val.subjectName+'</option>';
						subjectsStr =  subjectsStr + ","+ val.subjectName;
					});
					subjectsStr = subjectsStr.replace(",","");
					subjectsStr = '<div class="default">'+subjectsStr+'</div>';
					selectTag = selectTag+"</select></div>";
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
					return buttons;
				},sWidth:"10%"}
			]
		});
		
		var subjectData = getAllSubjects();
		
		$(".selectSubject").select2({
			//data:subjectData
		});
		
		$("#addBatchSelectSubject").select2({
			data:subjectData,
			placeholder:"Search and add subject"
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
		var batchName = $("#addBatchBatchName").val();
		var	divisonNameT = $("#addBatchSelectDivision").val();
		var	subject = $("#addBatchSelectSubject").val().join(",");
		addBatchConfirm(batchName,divisonNameT.toString(),subject);
	}
	
	function addBatchConfirm(batchName,divisionName,subject){
			console.log(batchName,divisionName,subject);	
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
					$.notify({message: "Batch successfuly added"},{type: 'success'});
					getAllBatches();		
				},
			   error:function(e){
					$.notify({message: "Error"},{type: 'danger'});
				}
			});
			
	}
	
	$(document).ready(function(){
		getAllBatches();
		
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
<div class="container">
 <div class="row" style="background-color:#eee">
 <div class="col-lg-4">
	<input type="text" class="form-control" id="batchTableSearchCustom" placeholder="search">
 </div>
 <div class="col-lg-8">
	<div class="row">
	<div class="col-lg-3">
		<input type="text" class="form-control" id="addBatchBatchName" placeholder="Batch name">
	</div>
	<div class="col-lg-3">	
		<select id="addBatchSelectDivision" class="form-control">
			<!--Thought it is batch id written its division id only-->
			<c:forEach items="${batcheids}" var="divId" varStatus="counter">
					<option value='<c:out value="${divId}"></c:out>'><c:out value="${divisionNames[counter.index]}"></c:out> &nbsp; <c:out value="${streams[counter.index]}"></c:out></option>
			</c:forEach>
		</select>
	</div>	
	<div class="col-lg-5">	
		<select id="addBatchSelectSubject" multiple="multiple" style="width:100%">
			
		</select>
	</div>
	<div class="col-lg-1">
		<input type="button" class="btn btn-success btn-add-batch" value="Add"/>
	</div>	
	</div>
 </div>
  
  </div>
</div>
<!--
<table class="table" id="batchTable">
<thead>
	<tr>
		<th>Batch name</th><th>Division</th><th>Stream</th><th>Subjects</th><th></th>
	</tr>
</thead>
<tbody>
	<c:forEach items="${listOfBatches}" var="batches" varStatus="counter">
	<tr>
		<td>
			<div class="default defaultBatchName"><c:out value="${batches.batch.batch_name}"></c:out></div>
			<input type="text" value='<c:out value="${batches.batch.batch_name}"></c:out>' class="form-control editable editBatchName"/>
			
			<input type="hidden" value='<c:out value="${batches.batch.batch_id}"></c:out>' id="batchId"/>
			<input type="hidden" value='<c:out value="${batches.batch.div_id}"></c:out>' id="divisionId"/>
		</td>
		<td><c:out value="${batches.division.divisionName}"></c:out></td>
		<td><c:out value="${batches.division.stream}"></c:out></td>
		<td>
			<div class="default">
				<c:forEach items="${batches.subjects}" var="subjects" varStatus="counter">
					<c:out value="${subjects.subjectName}"></c:out>
				</c:forEach>
			</div>		
			<div class="editable">
				<select class="selectSubject" multiple="multiple" style="width:100%">
					<c:forEach items="${batches.subjects}" var="subjects" varStatus="counter">
						<option selected="selected" value='<c:out value="${subjects.subjectId}"></c:out>'><c:out value="${subjects.subjectName}"></c:out></option>
					</c:forEach>
				</select>
			</div>
		</td>
		<td>
			<div class="default">
				<input type="button" class="btn btn-xs btn-warning btn-batch-edit" value="Edit"/>
				<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete"/>
			</div>
			<div class="editable">
				<button class="btn btn-success btn-xs btn-save">Save</button>
				<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>
			</div>
		</td>
	</tr>
	</c:forEach>
</tbody>
</table>
-->
<table id="batchTable" class="table" width="100%">

</table>
</body>
</html>