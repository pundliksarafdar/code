var allAjax = {
	searchClass : function(form){
		$('#'+form).submit();
	},
	classSearch : function(){
		$('#divLoad').load('/ClassApplication/getClassList',function(response, status, xhr){
		});
	}
}