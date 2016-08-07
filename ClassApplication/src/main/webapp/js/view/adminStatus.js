$(document).ready(function(){
	var handler = {};
	handler.success = function(e){console.log(e);};
	handler.error = function(){};
	rest.get("rest/admin/status/systemStatus",handler);
});