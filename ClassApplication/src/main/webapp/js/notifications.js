function Notification(){
	/*********Notifications Id*************/
	var NOTIFICATION_TOAST = ".corex-toast";
	
	/*********Notiification variables***/
	var DEFAULT_ON_TIME = 1000;
	
	/********All functions ************/
	this.showToast = function(option){
		var message = "No message to display";
		var onTime = DEFAULT_ON_TIME;
		if(typeof option == "object"){
			message = option.message;
			onTime = option.onTime;
		}else if(typeof option == "string"){
			message = option
		}
		
		$(NOTIFICATION_TOAST).html(message).fadeIn(400).delay(onTime).fadeOut(400);
	}
}