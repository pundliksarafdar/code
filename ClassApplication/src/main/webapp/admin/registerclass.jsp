<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
	function enableExactDate(){
		$('#cdatebtn').html('Exact Date <span class="caret"></span>');
		$('#exact').show();
		$('#between').hide();
	}

	function enableBetweenDate(){
		$('#cdatebtn').html('Between <span class="caret"></span>');
		$('#exact').hide();
		$('#between').show();
	}
	
	function clearDate(){
		$('#cdatebtn').html(' Choose <span class="caret"></span>');
		$('#exact').hide();
		$('#between').hide();
	}
</script>

<div class="ajax" style="position: relative; overflow: hidden">
            <h3>AJAX (via jQuery.load)</h3>
            <pre class="pre-scrollable prettyprint linenums" data-source="#ajax">
            </pre>
            <div class="text-center">
            <button class="demo btn btn-primary btn-lg" data-toggle="modal">View Demo</button>
            </div>
</div>
          
<div id="ajax-modal" class="modal fade" tabindex="-1" style="display: none;">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">Search</h4>
  </div>

<div class="container-fluid" style="padding-top: 10px; padding-bottom: 10px;">
  <div class="row-fluid">
    <div class="span5">

	<table>
	<tr><td> <button id="" class="btn btn-danger dropdown-toggle clear">X</button>
	<td>Name Initials<td><input type="text" name="cname" data-provide="typeahead"/>
	</td>
	</tr>
	
	<tr><td> <button onclick="clearDate();" id="" class="btn btn-danger dropdown-toggle clear">X</button>
	<td>Join Date<td>
	<div class="btn-group">
		<button id="cdatebtn" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"> Choose <span class="caret"></span></button>
  		<ul class="dropdown-menu">
    		<li><a href="javascript:enableExactDate();">Exact Date</a></li>
    		<li><a href="javascript:enableBetweenDate();">Between</a></li>
  		</ul>	
  	</div>
  	<td id="exact" style="display: none;">
  		<input type="text">
  	<td>
  	<td id="between" style="display: none;">
  		<input type="date" placeholder="Form"><span>&nbsp;</span><input type="date" placeholder="To">
  	<td>
	</td></tr>
	
	<tr><td> <button id="" class="btn btn-danger dropdown-toggle clear">X</button>
	<td>Duration<td><input type="text" name="cname" data-provide="typeahead"/> Month
	</td>
	</tr>
	</table>
	
</div>
<div class="span5">
	
	<table>
	<tr><td> <button id="" class="btn btn-danger dropdown-toggle clear">X</button>
	<td>State<td><input type="text" name="cstate" data-provide="typeahead"/>
	</td>
	</tr>
	
	<tr><td> <button id="" class="btn btn-danger dropdown-toggle clear">X</button>
	<td>City<td><input type="text" name="ccity" data-provide="typeahead"/>
	</td>
	</tr>
	
	<tr><td> <button id="" class="btn btn-danger dropdown-toggle clear">X</button>
	<td>Class Id<td><input type="text" name="cid" data-provide="typeahead"/>
	</td>
	</tr>
	</table>
	
</div>
</div>  

 <div class="row-fluid">
 <div class="span10" style="padding-top: 10px;">
 	
 </div>
 </div>          		
</div>

<!-- footer -->
  <div class="modal-footer">
    <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
    <button type="button" class="btn btn-info" onclick="allAjax.searchClass('100')">Search</button>
  </div>

</div>
<script id="ajax" type="text/javascript">

var $modal = $('#ajax-modal');

$('.ajax .demo').on('click', function(){
  // create the backdrop and wait for next modal to be triggered
  $('body').modalmanager('loading');

  setTimeout(function(){
     $modal.load('modal_ajax_test.html', '', function(){
      $modal.modal();
    });
  }, 1000);
});

$modal.on('click', '.update', function(){
  $modal.modal('loading');
  setTimeout(function(){
    $modal
      .modal('loading')
      .find('.modal-body')
        .prepend('<div class="alert alert-info fade in">' +
          'Updated!<button type="button" class="close" data-dismiss="alert">&times;</button>' +
        '</div>');
  }, 1000);
});

</script> 
</body>
</html>