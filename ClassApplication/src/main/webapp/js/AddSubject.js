var batchName;
$(document).ready(function(){
	$('div#addBatchModal').on('click','button#btn-add',function(){
		batchName = "";
		$('div#addBatchModal .error').html('');
		$('div#addBatchModal .error').hide();
		var regId;
		batchName = $('div#addBatchModal').find('#batchName').val();
		if(!batchName || batchName.trim()==""){
			$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Batch name cannot be blank');
			$('div#addBatchModal .error').show();
		}else{
			$('div#addBatchModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addBatch('',batchName,successCallback,errorCallback);
		}
	});
	
	
	$('div#addSubjectModal').on('click','button#btn-add',function(){
		batchName = "";
		$('div#addSubjectModal .error').html('');
		$('div#addSubjectModal .error').hide();
		var regId;
		subjectName = $('div#addSubjectModal').find('#subjectName').val();
		if(!subjectName || subjectName.trim()==""){
			$('div#addSubjectModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Subject name cannot be blank');
			$('div#addSubjectModal .error').show();
		}else{
			$('div#addSubjectModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addSubject('',subjectName,successCallbackSubject,errorCallbackSubject);
		}
	});

	$('[data-target="#addBatchModal"]').on('click',function(){
		$('#addBatchModal').find('.error').hide();
		$('div#addBatchModal #classTimming').addClass('hide');
		$('div#addBatchModal .setTimming').addClass('hide');
		$('div#addBatchModal .add').removeClass('hide');
		$('div#addBatchModal .progress').addClass('hide');
	});
	
	$('[data-target="#addSubjectModal"]').on('click',function(){
		$('#addSubjectModal').find('.error').hide();
		$('div#addSubjectModal #classTimming').addClass('hide');
		$('div#addSubjectModal .setTimming').addClass('hide');
		$('div#addSubjectModal .add').removeClass('hide');
		$('div#addSubjectModal .progress').addClass('hide');

	});
	
	$('#fromDate,#toDate').datetimepicker({
		pickDate:false
	});
});

function deleteBatch(batchName){
	modal.modalConfirm("Delete Batch", "Do you want to delete batch?", "No", "Yes", deleteBatchConfirm, [batchName]);
}

function deleteBatchConfirm(batchName){
	$('#progressModal').modal('show');
	allAjax.deleteBatch("",batchName,successCallBackDeleteBatch,errorcallBackDeleteBacth);
}

function successCallBackDeleteBatch(){
	$('#progressModal').modal('hide');
	location.reload();
}

function errorcallBackDeleteBacth(){
	modal.launchAlert("Error", "Unable to delete batch");
}

function errorCallback(e){
		$('div#addBatchModal .progress').addClass('hide');
		$('div#addBatchModal .add').removeClass('hide');
		$('div#addBatchModal .error').show();
  		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallback(e){
	$('div#addBatchModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addBatchModal').modal('hide');
		   modal.launchAlert("Success","Batch Added! Page will refresh in 2 sec");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);		   
	   }else{
		   $('div#addBatchModal .add').removeClass('hide');
		   $('div#addBatchModal .error').show();
	   	if(!resultJson.message){
		   $('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
	   	}else{
	   		$('div#addBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
	   	}
}

function errorCallbackSubject(e){
	$('div#addSubjectModal .progress').addClass('hide');
	$('div#addSubjectModal .add').removeClass('hide');
	$('div#addSubjectModal .error').show();
		$('div#addSubjectModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallbackSubject(e){
$('div#addSubjectModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#addSubjectModal').modal('hide');
	   modal.launchAlert("Success","Subject Added! Page will refresh in 2 sec");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
	   $('div#addSubjectModal .add').removeClass('hide');
	   $('div#addSubjectModal .error').show();
   	if(!resultJson.message){
	   $('div#addSubjectModal .error').html('<strong>Error!</strong> Unable to add');
   	}else{
   		$('div#addSubjectModal .error').html('<strong>Error!</strong>'+resultJson.message);
   	}
   	}
}

function errorCallbackBatchTimming(e){
		$('div#addBatchModal .progress').addClass('hide');
		$('div#addBatchModal .setTimming').removeClass('hide');		   
		$('div#addBatchModal .error').show();
		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallbackBatchTimming(e){
	$('div#addBatchModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addBatchModal').modal('hide');
		   modal.launchAlert("Success","Batch Added! Page will refresh in 2 sec");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);
		   
	   }else{
		   $('div#addBatchModal .setTimming').removeClass('hide');
		   $('div#addBatchModal .error').show();
		   if(!resultJso.message){
			   $('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
		   	}else{
		   		$('div#addBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	   	}
}



/****************function called by AddSubject.jsp********************/

function removeClass(){
	modal.modalConfirm("Delete", "Do you want to delete batch", "No", "Delete", deleteBatch, "Deleted");
}
