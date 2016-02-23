function REST(){
	this.get =function(uri,handlers,global){
		$.ajax({
			   url: uri,
			   type:"GET",
			   global:global,
			   success:handlers.success,
			   error:handlers.error
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
}

var rest = new REST(); 