<style>
#iosbtn {
	background: -moz-linear-gradient(-90deg, #da7d83, #da7d83 50%, #ca444e 50%, #ca444e);
	background: -ms-linear-gradient(-90deg, #da7d83, #da7d83 50%, #ca444e 50%, #ca444e);
	background: -o-linear-gradient(-90deg, #da7d83, #da7d83 50%, #ca444e 50%, #ca444e);
	background: -webkit-gradient(linear, left top, left bottom, from(#da7d83), color-stop(0.5, #da7d83), color-stop(0.5, #ca444e), to(#ca444e));
	background: linear-gradient(-90deg, #da7d83, #da7d83 50%, #ca444e 50%, #ca444e);
	border: 3px solid #fff;
	-moz-border-radius: 30px;
	-ms-border-radius: 30px;
	-o-border-radius: 30px;
	-webkit-border-radius: 30px;
	border-radius: 30px;
	-moz-box-shadow: 0 2px 2px rgba(0,0,0, 1);
	-ms-box-shadow: 0 2px 2px rgba(0,0,0, 1);
	-o-box-shadow: 0 2px 2px rgba(0,0,0, 1);
	-webkit-box-shadow: 0 2px 2px rgba(0,0,0, 1);
	box-shadow: 0 2px 2px rgba(0,0,0, 1);
	color: #fff;
	font: bold 32px/24px Arial;
	height: 30px;
	text-align: center;
	text-shadow: 0 -1px #b30000, 0px 2px #fff;
	width: 30px;
}

</style>

	<!-- Search Modal  start-->
<div class="modal fade" id="ajax-modal">
<div class="modal-dialog">
<div class="modal-content">
<form action="searchclass" method="post" id="searchclass">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">Search</h4>
  </div>

<div class="container-fluid" style="padding: 10px;">
  	<table>
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Name Initials<td><input type="text" name="classSearchForm.cname" data-provide="typeahead" class="form-control"/>
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
  		<input type="date" name="classSearchForm.cexactdate" class="form-control">
  	<td>
  	<td id="between" style="display: none;">
  		<input type="date" placeholder="Form" name="classSearchForm.cfrmdate" class="form-control"/>
  		<span>&nbsp;</span><input type="date" placeholder="To" name="classSearchForm.ctodate" class="form-control"/>
  	<td>
	</td></tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Duration<td><input type="text" name="classSearchForm.cduration" data-provide="typeahead" class="form-control"/> 
	</td>
	<td>Month</td>
	</tr>
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>State<td><input type="text" name="classSearchForm.cstate" data-provide="typeahead" class="form-control"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>City<td><input type="text" name="classSearchForm.ccity" data-provide="typeahead" class="form-control"/>
	</td>
	</tr>
	
	<tr><td><button type="button" class="close" aria-hidden="true">&times;</button>
	<td>Class Id<td><input type="text" name="classSearchForm.cid" data-provide="typeahead" class="form-control"/>
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
</div>
</div>

	<!-- Search Modal  end-->
	<!-- Modal confirmation Box Start -->
	<div class="modal fade" id="messageModal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="myModalLabel">Small modal</h4>
        </div>
        <div class="modal-body" id="mymodalmessage">
          
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>
</div>

<div class="modal fade" id="acceptModal">
<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Accept Class</h4>
        </div>
        <div class="modal-body" id="">
          	<div>Do you want to accept the class?</div>
          	<div>
          		<table>
          			<tr>
          				<td><input type="text" id="duration" data-provide="typeahead" class="form-control"/></td><td>&nbsp;&nbsp;&nbsp; Months</td>
          			</tr>
          		</table>
          	</div>
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-success" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>
</div>

<div class="modal fade" id="rejectModal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Reject Class</h4>
        </div>
        <div class="modal-body" id="">
          	<div>Do you want to reject the class?</div>
          	
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-danger" data-dismiss="modal">Yes</button>
      	</div>
    </div>
</div>	
</div>
<div class="modal fade" id="addBatchModal">
  <div class="modal-dialog">
      <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Add Batch</h4>
        </div>
        <div class="modal-body" id="">
        	<div class="error alert alert-danger"></div>
			<div class="form-group">
				<input type="text" class="form-control" id="batchName"/>
				<br>
				<div id="classTimming" class="hide">
				<div class="container-fluid">
  				<div class="row">
  					
					<div class="col-sm-6">
					<label for="">Start Time</label>
					<div class='input-group date' id='fromDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
					
					<div class="col-sm-6">
					<label for="">End Time</label>
					<div class='input-group date' id='toDate' data-date-format="hh:mm A" style="width: 150px;">
						<input type='text' class="form-control"/> <span
							class="input-group-addon"><span
							class="glyphicon glyphicon-calendar"></span> </span>
					</div>
					</div>
				</div>
				</div>
				</div>
			</div>
		</div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary btn-add" id="btn-add">Add</button>
      	</div>
    </div>
</div>	
</div>

<div class="modal fade" id="addSubjectModal">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="">Add Subject</h4>
        </div>
        <div class="modal-body" id="">
          	<div id="addMessage"></div>
          	<div class="error"></div>
          	<label for="subjectname">
          		Enter Subject Name
 			</label>
          	<input type="text" id="subjectname" data-provide="typeahead" class="form-control"/>
          	<input type="hidden" id="task"/>
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	        <button type="button" class="btn btn-primary" >Add</button>
      	</div>
    </div>
</div>	

	<!-- Modal Box End -->
