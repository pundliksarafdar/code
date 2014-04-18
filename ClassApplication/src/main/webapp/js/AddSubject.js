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
			allAjax.addBatch('12',batchName,successCallback,errorCallback);
		}
	});
	
	$('div#addBatchModal').on('click','button#btn-setTimming',function(){
		$('div#addBatchModal .error').html('');
		$('div#addBatchModal .error').hide();
		var regId;
		var toTime = $('div#addBatchModal').find('#toDate').find('input[type="text"]').val();
		var fromTime = $('div#addBatchModal').find('#fromDate').find('input[type="text"]').val();
		
		if(batchName){
			if(toTime.trim()=="" || fromTime.trim()==""){
				$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Start time and end time must be present<br> if you dont want to give time now then you can click on not now and add latter');
				$('div#addBatchModal .error').show();
			}else{
				$('div#addBatchModal .setTimming').addClass('hide');
				$('div#addBatchModal .progress').removeClass('hide');
				allAjax.addBatchTimming('12',batchName,successCallbackBatchTimming,errorCallbackBatchTimming);
			}
		}else{
			$('div#addBatchModal .progress').addClass('hide');
			$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Something unexpected occured,Please refresh page');
			$('div#addBatchModal .error').show();
		
		}
	});
	
	$('[data-target="#addBatchModal"]').on('click',function(){
		$('#addBatchModal').find('.error').hide();
		$('div#addBatchModal #classTimming').addClass('hide');
		$('div#addBatchModal .setTimming').addClass('hide');
		$('div#addBatchModal .add').removeClass('hide');
	});
	
	$('#fromDate,#toDate').datetimepicker({
		pickDate:false
	});
});

function errorCallback(e){
	   $('div#addBatchModal .error').show();
  		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallback(e){
	$('div#addBatchModal .progress').addClass('hide');
	
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addBatchModal #classTimming').removeClass('hide');
		   $('.setTimming').removeClass('hide');
		   $('.add').addClass('hide');
		   
	   }else{
		   $('div#addBatchModal .add').removeClass('hide');
		   $('div#addBatchModal .error').show();
	   		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
	   	}
}

function errorCallbackBatchTimming(e){
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
	   		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
	   	}
}