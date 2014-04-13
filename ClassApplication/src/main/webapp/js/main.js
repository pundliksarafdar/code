$(document).ready(function(){
	$('[data-toggle="modal"]').on('click',function(){
		$($(this).attr('data-target')).find('.error').text("");
	});
});

var commonfunctions={
		//Function to display the pageinator syntax paginator(current_page,Total_Pages,CntainerId)
		paginator:function(cPage,tResPerPages,totalResults,containerId){
			if(totalResults > tResPerPages){
				if(cPage>3){
						
				}
			}
		}
}