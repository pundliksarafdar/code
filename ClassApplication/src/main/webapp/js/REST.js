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
			   global:global,
			   success:handlers.success,
			   error:handlers.error
			   });
	}
}

var rest = new REST(); 