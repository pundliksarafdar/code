<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
.container-fluid .close{
	padding-right: 10px;
}

.container-fluid tr td:nth-child(3){
	padding-left: 10px;
	padding-top: 5px;
	padding-bottom: 5px;
}
</style>
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

<div id="ajax-modal" class="modal fade" tabindex="-1" style="display: none;">
<form action="searchclass" method="post" id="searchclass">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">Search</h4>
  </div>

<div class="container-fluid" style="padding: 10px;">
  	<table>
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Name Initials<td><input type="text" name="classSearchForm.cname" data-provide="typeahead"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true" onclick="clearDate();">&times;</button>
	<td>Join Date<td>
	<div class="btn-group">
		<button id="cdatebtn" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"> Choose <span class="caret"></span></button>
  		<ul class="dropdown-menu">
    		<li><a href="javascript:enableExactDate();">Exact Date</a></li>
    		<li><a href="javascript:enableBetweenDate();">Between</a></li>
  		</ul>	
  	</div>
  	<td id="exact" style="display: none;">
  		<input type="date" name="classSearchForm.cexactdate">
  	<td>
  	<td id="between" style="display: none;">
  		<input type="date" placeholder="Form" name="classSearchForm.cfrmdate"/><span>&nbsp;</span><input type="date" placeholder="To" name="classSearchForm.ctodate"/>
  	<td>
	</td></tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Duration<td><input type="text" name="classSearchForm.cduration" data-provide="typeahead"/> Month
	</td>
	</tr>
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>State<td><input type="text" name="classSearchForm.cstate" data-provide="typeahead"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>City<td><input type="text" name="classSearchForm.ccity" data-provide="typeahead"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Class Id<td><input type="text" name="classSearchForm.cid" data-provide="typeahead"/>
	</td>
	</tr>
	</table>
</div>

<!-- footer -->
  <div class="modal-footer">
    <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
    <button type="button" class="btn btn-info" onclick="allAjax.searchClass('searchclass')">Search</button>
  </div>
</form>
</div>
<%if(null==session.getAttribute("isSearched") || !session.getAttribute("isSearched").equals("true")) {%>
<script id="ajax" type="text/javascript">
setTimeout(function(){
		var $modal = $('#ajax-modal');
    	$modal.modal();
},10);    
</script> 
<%} %>
</body>
</html>