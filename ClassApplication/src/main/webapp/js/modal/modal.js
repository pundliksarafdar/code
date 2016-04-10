var modal = new Modal();

function Modal(){
	
}

Modal.prototype.modalConfirm = function(heading, question, cancelButtonTxt, okButtonTxt,callback,params) {
	
    var confirmModal = 
      $('<div class="modal fade"><div class="modal-dialog"><div class="modal-content">' +    
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
            '<a href="#" id="okButton" class="btn btn-primary" data-dismiss="modal">' + 
              okButtonTxt + 
            '</a>' +
          '</div>' +
        '</div></div></div>');

    	confirmModal.find('#okButton').click(function(event) {
      
    	callback.apply(this,params);
      confirmModal.modal('hide');
    });

    confirmModal.modal('show');
  }

Modal.prototype.modalYesAndNo = function(heading, question, noButtonTxt,noBtnCallBack, okButtonTxt,yesBtnCallback) {
	
    var confirmModal = 
      $('<div class="modal fade"><div class="modal-dialog"><div class="modal-content">' +    
          '<div class="modal-header">' +
            '<a class="close" data-dismiss="modal" >&times;</a>' +
            '<h4>' + heading +'</h4>' +
          '</div>' +

          '<div class="modal-body">' +
            '<p>' + question + '</p>' +
          '</div>' +

          '<div class="modal-footer">' +
            '<a href="#" id="noButton" class="btn btn-default" data-dismiss="modal">' + 
            	noButtonTxt + 
            '</a>' +
            '<a href="#" id="okButton" class="btn btn-primary" data-dismiss="modal">' + 
              okButtonTxt + 
            '</a>' +
            '<a href="#" id="cancelButton" class="btn btn-primary" data-dismiss="modal">' + 
            "Cancel" + 
          '</a>' +
          '</div>' +
        '</div></div></div>');

    	confirmModal.find('#okButton').click(function(event) {
    		yesBtnCallback();
    		confirmModal.modal('hide');
    	});
    	
    	confirmModal.find('#noButton').click(function(event) {
    		if(noBtnCallback ){noBtnCallback();}
    		confirmModal.modal('hide');
    	});
    	confirmModal.find('#cancelButton').click(function(event) {
    		noBtnCallback();
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
	
	
	var ModalVar = {
			addBatch : function(){
				$('#addBatSubModal').find(".modal-title").text("Add New Batch");
				$('#addBatSubModal').find("#addMessage").text("Enter Batch Name");
				$('#addBatSubModal').find("#task").val("addbatch");
				$('#addBatSubModal').modal('show');
				$('#addBatSubModal .btn-primary').unbind('click');
				$('#addBatSubModal .btn-primary').on('click',function(){
					console.log($(this).parents('#addBatSubModal').find('#name').val().length);
					if($(this).parents('#addBatSubModal').find('#name').val().length !== 0){
						
					}else{
						$(this).parents('#addBatSubModal').find('#error').text("Please enter the subject name");
						$('#addBatSubModal').modal('show');
					}
				});
			},
			addSubject : function(){
				$('#addBatSubModal').find(".modal-title").text("Add New Subject");
				$('#addBatSubModal').find("#addMessage").text("Enter Subject Name");
				$('#addBatSubModal').find("#task").val("addsubject");
				$('#addBatSubModal').modal('show');
				
				$('#addBatSubModal .btn-primary').unbind('click');
				$('#addBatSubModal .btn-primary').on('click',function(){
					if($(this).parents('#addBatSubModal').find('#name').val().length !== 0){
						
					}else{
						$(this).parents('#addBatSubModal').find('#error').text("Please enter the subject name");
					}
				});
			}
	}
	
	Modal.prototype.sessionTimeout = function(heading, question, cancelButtonTxt, okButtonTxt,callbackCancel,paramsCancel,callBackOk,paramsOk) {
		
	    var sessionTimeoutModal = 
	      $('<div class="modal fade sessionTimeoutModal"><div class="modal-dialog"><div class="modal-content">' +    
	          '<div class="modal-header">' +
	            '<h4>' + heading +'</h4>' +
	          '</div>' +

	          '<div class="modal-body">' +
	            '<p>' + question + '</p>' +
	          '</div>' +

	          '<div class="modal-footer">' +
	            '<a href="#" id="cancelButton" class="btn btn-default" data-dismiss="modal">' + 
	              cancelButtonTxt + 
	            '</a>' +
	            '<a href="#" id="okButton" class="btn btn-primary" data-dismiss="modal">' + 
	              okButtonTxt + 
	            '</a>' +
	          '</div>' +
	        '</div></div></div>');

	    	sessionTimeoutModal.find('#okButton').click(function(event) {
	    		callBackOk.apply(undefined,paramsOk);
		    	sessionTimeoutModal.modal('hide');
	    	});
	    	
	    	sessionTimeoutModal.find('#cancelButton').click(function(event) {
	  	      	callbackCancel.apply(undefined,paramsCancel);
		    	sessionTimeoutModal.modal('hide');
		    });

	    	sessionTimeoutModal.modal('show');
	  }
	