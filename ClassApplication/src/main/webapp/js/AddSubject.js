$(document).ready(function(){
	$('div#addBatchModal').on('click','button#btn-add',function(){
		
		var regId;
		var batchName = $('div#addBatchModal').find('#batchname').val();;
		allAjax.addBatch('12','qwe');
	});
	
    $('#datetimepicker').datetimepicker({
  	  pickDate: false,
  	  pick12HourFormat: true
    });

});