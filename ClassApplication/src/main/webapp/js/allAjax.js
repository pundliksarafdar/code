var allAjax = {
	searchClass : function(form){
		$('#'+form).submit();
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
	addBatch :function(regId,batchName,divisionName,streamName,subjectname,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addBatch",
					 regId:regId,
					 batchName:batchName,
					 divisionName:divisionName,
					 streamName:streamName,
					 subjectname:subjectname
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	},
	addBatchTimming :function(regId,batchName,fromTime,toTime,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addBatchTimming",
					 regId:regId,
					 batchName:batchName,
					 fromTime:fromTime,
					 toTime:toTime
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	},
	addSubject :function(regId,subjectName,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addSubject",
					 regId:regId,
					 subjectName:subjectName
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	},
	addTeacher :function(regId,subjects,teacherID,suffix,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addTeacher",
					 regId:regId,
					 teacherID:teacherID,
					 subjects:subjects,
					 suffix:suffix
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	},
	addclass :function(regId,classname,stream,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addclass",
					 regId:regId,
					 classname:classname,
					 stream:stream
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	},
	deleteBatch:function(regId,batchName,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "deleteBatch",
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
	},
	addStudent :function(regId,studentLgName,batchIds,successCallback,errorCallback){
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addStudent",
					 regId:regId,
					 studentLgName:studentLgName,
					 batchIds:batchIds,					 
			   		},
			   type:"POST",
			   success:function(e){
				   successCallback(e);
			   	},
			   error:function(e){
				   errorCallback(e);
			   }
		});
	},
	checkNumberExist:function(mobileNum,success){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "checkuphonenumber",
			    	 mobileNumber:mobileNum
			   		},
			   type:"POST",
			   success:function(e){
				   success(e);
				   
			   		},
			   error:function(){
				   modal.launchAlert("Error","Error occured while accepting the user!");
			   }
			});
	},
	checkUserNameExist:function(loginname,success){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "checkusername",
			    	 userName:loginname
			   		},
			   type:"POST",
			   success:function(e){
				   success(e);
				   
			   		},
			   error:function(){
				   modal.launchAlert("Error","Error occured while accepting the user!");
			   }
			});
	},
	checkEmailExist:function(email,success){
		$.ajax({
			   url: "admajxsrvlt",
			   data: {
			    	 methodToCall: "checkemail",
			    	 emailId:email
			   		},
			   type:"POST",
			   success:function(e){
				   success(e);
				   
			   		},
			   error:function(){
				   modal.launchAlert("Error","Error occured");
			   }
			});
	}
}