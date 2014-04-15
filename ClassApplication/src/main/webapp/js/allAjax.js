var allAjax = {
	searchClass : function(form){
		$('#'+form).submit();
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
	},
	acceptClass :function(regId,duration){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "reg",
					 regId:regId,
					 duration:duration
			   		},
			   type:"POST",
			   success:function(e){
				   var resultJson = JSON.parse(e);
				   if(resultJson.status != 'error'){
					   	modal.launchAlert("Success","User Accepted till "+resultJson.endDate+"<br> Page will refresh");
					   	//$("#regId"+regId).parents('#tableTr').find('#duration').text(resultJson.endDate);
					   	setTimeout(function(){
					   		location.reload();
					   	},1000*3);
				   	}else{
				   		modal.launchAlert("Error","Error occured");
				   	}
			   		},
			   error:function(){
				   modal.launchAlert("Error","Error occured while accepting the user!");
			   }
			});
	},
	
	deleteUser :function(regId){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "deleteuser",
					 regId:regId
			   		},
			   type:"POST",
			   success:function(e){
				   var resultJson = JSON.parse(e);
				   if(resultJson.status != 'error'){
					   	modal.launchAlert("Success","User Deleted");
					   	setTimeout(function(){
					   		location.reload();
					   	},1000*3);
				   }else{
				   		modal.launchAlert("Error","Error occured");
				   	}
			   		},
			   error:function(){
				   modal.launchAlert("Error","Error occured while deleting the user!");
			   }
		});
	},
	addBatch :function(regId,batchName,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addBatch",
					 regId:regId,
					 batchName:batchName
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	}

}