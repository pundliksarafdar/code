var allAjax = {
	searchClass : function(form){
		$('#'+form).submit();
	},
	classSearch : function(){
		$('#divLoad').load('/ClassApplication/getClassList',function(response, status, xhr){
		});
	},
	addSubject :function(){
		location.href = "addsubject";
	},
	blockUser :function (regId,role){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "block",
					 regId:regId,
					 role:role
			   		},
			   type:"POST",
			   success:function(){
				   		$('#block'+regId).addClass('hide');
				   		$('#unblock'+regId).removeClass('hide');
				   		modal.launchAlert("Success","User Blocked!");	   		
			   },
			   	error:function(){
			   		modal.launchAlert("Error","Error occured while blocking the user");
			   	}	
			});
	},
	unBlockUser :function(regId,role){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "unblock",
					 regId:regId,
					 role:role
			   		},
			   type:"POST",
			   success:function(){
				   	$('#unblock'+regId).addClass('hide');
			   		$('#block'+regId).removeClass('hide');			   		
			   		modal.launchAlert("Success","User UnBlocked!");	   		
			   },
			   error:function(){
				   modal.launchAlert("Error","Error occured while unblocking user");	   		
			   }
			});
	}
	

}