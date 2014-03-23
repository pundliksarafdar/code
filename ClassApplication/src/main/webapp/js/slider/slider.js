$('document').ready(function(){
/*
$('#button').toggle( 
    function() {
        $('#right').animate({ left: 150 }, 'slow', function() {
            $('#button').html('Close');
        });
    }, 
    function() {
        $('#right').animate({ left: 0 }, 'slow', function() {
            $('#button').html('Menu');
        });
    }
);
*/
	
	$('#button').click(function(){
		console.log($('#right').css('left').indexOf("150"));
		if($('#right').css('left').indexOf("150") == -1){
		$('#right').animate({ left: 150 }, 'slow', function() {
            $('#button').html('Close');
        });
		}else{
		$('#right').animate({ left: 0 }, 'slow', function() {
	            $('#button').html('Close');
	        });

		}
	});
});
