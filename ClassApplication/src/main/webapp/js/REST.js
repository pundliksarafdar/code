function REST(){
	this.get =function(uri,handlers,global,async){
		$.ajax({
			   url: uri,
			   type:"GET",
			   global:global,
			   async:async,
			   success:handlers.success,
			   error:handlers.error,
			   always:handlers.always,
			   cache:false
			   }); 
	}
	
	this.post =function(uri,handlers,data,global){
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:data,
			   processData: false,
			   contentType: "application/json",
			   global:global,
			   success:handlers.success,
			   error:handlers.error,
			   always:handlers.always
			   });
	}
	
	this.postString =function(uri,handlers,data,global){
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:data,
			   processData: false,
			   contentType: "text/plain",
			   global:global,
			   success:handlers.success,
			   error:handlers.error
			   });
	}

	this.deleteItem =function(uri,handlers,global){
		$.ajax({
			   url: uri,
			   type:"DELETE",
			   global:global,
			   success:handlers.success,
			   error:handlers.error
			   }); 
	}
	
	this.deleteItemData =function(uri,handlers,data,global){
		$.ajax({
			   url: uri,
			   type:"DELETE",
			   global:global,
			   data:data,
			   processData: false,
			   contentType: "application/json",
			   success:handlers.success,
			   error:handlers.error
			   }); 
	}
	
	this.put =function(uri,handlers,data,global){
		$.ajax({
			   url: uri,
			   type:"PUT",
			   data:data,
			   processData: false,
			   contentType: "application/json",
			   global:global,
			   success:handlers.success,
			   error:handlers.error
			   });
	}

	
	/*
	 * This is sample code submitquestionsuploadfile is file type id
	 * 
	 * 
	 *  var handler = {}
		handler.success=function(e){console.log(e)}
		handler.error=function(e){console.log("Error",e)}
		var submitDataFile = $("#submitquestionsuploadfile")[0]
		rest.uploadImageFile(submitDataFile ,handler,false);
	 * */
	this.uploadImageFile = function(file,handlers,global){
		var formData = new FormData();
		formData.append("uploadedFile",file.files[0]);
		var uri = "rest/commonservices/uploadImageTemp";
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:formData,
			   global:global,
			   processData: false,
			   contentType: false,
			   success:handlers.success,
			   error:handlers.error
			   });
	}
	
	this.uploadExcelFile = function(file,handlers,global){
		var formData = new FormData();
		formData.append("uploadedExcelFile",file.files[0]);
		var uri = "rest/commonservices/uploadExcelFile";
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:formData,
			   global:global,
			   processData: false,
			   contentType: false,
			   success:handlers.success,
			   error:handlers.error
			   });
	}
	
	this.uploadNotes = function(uri,formData,handlers,global){
		
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:formData,
			   global:global,
			   processData: false,
			   contentType: false,
			   success:handlers.success,
			   error:handlers.error
			   });
	}
	
	this.uploadTeacherImageFile = function(file,inst_id,handlers,global){
		var formData = new FormData();
		formData.append("uploadedFile",file.files[0]);
		var uri = "rest/commonservices/uploadTeacherImageTemp/"+inst_id;
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:formData,
			   global:global,
			   processData: false,
			   contentType: false,
			   success:handlers.success,
			   error:handlers.error
			   });
	}
	
	this.uploadCustomUserImageFile = function(file,handlers,global){
		var formData = new FormData();
		formData.append("uploadedFile",file.files[0]);
		var uri = "rest/commonservices/uploadCustomUserImageFile";
		$.ajax({
			   url: uri,
			   type:"POST",
			   data:formData,
			   global:global,
			   processData: false,
			   contentType: false,
			   success:handlers.success,
			   error:handlers.error
			   });
	}
}

var rest = new REST(); 