var modal = new Modal();

function Modal(){
	
}

Modal.prototype.modalConfirm = function(heading, question, cancelButtonTxt, okButtonTxt,callback,params) {
	
    var confirmModal = 
      $('<div class="modal fade">' +    
          '<div class="modal-header">' +
            '<a class="close" data-dismiss="modal" >&times;</a>' +
            '<h4>' + heading +'</h4>' +
          '</div>' +

          '<div class="modal-body">' +
            '<p>' + question + '</p>' +
          '</div>' +

          '<div class="modal-footer">' +
            '<a href="#" class="btn btn-default" data-dismiss="modal">' + 
              cancelButtonTxt + 
            '</a>' +
            '<a href="#" id="okButton" class="btn btn-primary">' + 
              okButtonTxt + 
            '</a>' +
          '</div>' +
        '</div>');

    	confirmModal.find('#okButton').click(function(event) {
      
    	callback.apply(undefined,params);
      confirmModal.modal('hide');
    });

    confirmModal.modal('show');
  }
	
	Modal.prototype.acceptClass = function(regId,that){
	$.ajax({
		   url: "admajxsrvlt",
		   data: {
		    	 methodToCall: "reg",
				 regId:$(that).parents('#tableTr').find('#regId').text(),
				 duration:$(that).parents('#tableTr').find('#duration').val()
		   		},
		   type:"POST",
		   success:function(){
			   		alert('Success');
		   		}
		});
  }
  
	Modal.prototype.launchAlert = function(header,message){
			$("#myModalLabel").text(header);
			$("#mymodalmessage").html(message);
			$('#messageModal').modal('show');		
	}