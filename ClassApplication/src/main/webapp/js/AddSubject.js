$(document).ready(function(){
	$('div#addBatchModal').on('click','button#btn-add',function(){
		var regId;
		var batchName = $('div#addBatchModal').find('#batchname').val();;
		allAjax.addBatch('12','qwe',successCallback,errorCallback);
	});
	  
	$('[data-target="#addBatchModal"]').on('click',function(){
		$('#addBatchModal').find('.error').hide();
		$('div#addBatchModal #classTimming').addClass('hide');
	});
	
	$('#fromDate,#toDate').datetimepicker({
		pickDate:false
	});
});

function errorCallback(e){
	modal.launchAlert("Error","Error occured while deleting the user!");
}

function successCallback(e){
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addBatchModal #classTimming').removeClass('hide');
		   $('#fromDate,#toDate').datetimepicker({
				pickDate:false
			});
	   }else{
		   $('div#addBatchModal .error').show();
	   		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
	   	}
}