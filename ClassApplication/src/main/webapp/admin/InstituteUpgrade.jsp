<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
$(document).ready(function(){
	var globalinstituteID="";
	$("#upgradediv").on("click","#addids",function(e){
		$("#noofiderror").html("");
		var numberExpr = /^[0-9]+$/;
		var noofids=$("#noofids").val().trim();
		if(noofids==""){
			$("#noofiderror").html("Please enter no of ID's");
		}
		else if(!noofids.match(numberExpr)){
			$("#noofiderror").html("Invalid no of ID's");
		}else{
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "addids",
				    	 noofids:noofids,
				    	 instituteID:globalinstituteID
				   		},
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   institutesearch(globalinstituteID);
					   $("#idsaddedmodal").modal("toggle");
					   },
				   	error:function(){
				   		modal.launchAlert("Error","Error");
				   	}	
				});
		}
		
	});
	
	$("#upgradediv").on("click","#addmemory",function(e){
		$("#memoryspaceerror").html("");
		var numberExpr = /^[0-9]+$/;
		var memoryspace=$("#memoryspace").val().trim();
		if(memoryspace==""){
			$("#memoryspaceerror").html("Please enter memory size");
		}else if(!memoryspace.match(numberExpr)){
			$("#memoryspaceerror").html("Invalid memory size");
		}else{
			$.ajax({
				 
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "addmemory",
				    	 memoryspace:memoryspace,
				    	 instituteID:globalinstituteID
				   		},
				   type:"POST",
				   success:function(data){
					   var resultJson = JSON.parse(data);
					   institutesearch(globalinstituteID);
					   $("#memmoryaddedmodal").modal("toggle");
				   },
				   	error:function(){
				   		modal.launchAlert("Error","Error");
				   	}	
				});
		}
	});
	
	$("#institutesearch").click(function(){
		$("#instituteIDerror").html("");
		$("#institutestattable").hide();
		   $("#invalidIDerror").hide();
		   $("#upgradediv").hide();
		var instituteID=$("#instituteID").val().trim();
		if(instituteID!=""){
			institutesearch(instituteID);
		}else{
			$("#instituteIDerror").html("Please enter institute ID");
		}
	});
	
	function institutesearch(instituteID){
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getInstituteStats",
			    	 instituteID:instituteID
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var found=resultJson.found;
				   if(found==""){
					   globalinstituteID=instituteID;
					   var classname=resultJson.classname;
					   var classowner=resultJson.classowner;
					   var noofstudentIds=resultJson.noofstudentIds;
					   var memoryspace=resultJson.memoryspace;
					   var noofstudentIdsused=resultJson.noofstudentIdsused;
					   var memoryspaceused=resultJson.memoryspaceused;
					   var inststatus=resultJson.inststatus;
					   $("#institutestattablebody").empty();
					   $("#institutestattablebody").append("<tr><td>"+classname+"</td><td>"+classowner+"</td><td>"+noofstudentIds+"</td><td>"+memoryspace+"</td>"
					   +"<td>"+noofstudentIdsused+"</td><td>"+memoryspaceused+"</td><td>"+inststatus+"</td></tr>");
					   $("#institutestattable").show();
					   $("#invalidIDerror").hide();
					   $("#upgradediv").show();
				   }else{
					   $("#upgradediv").hide();
					   $("#institutestattable").hide();
					   $("#invalidIDerror").show();
				   }
				   		},
			   	error:function(){
			   		modal.launchAlert("Error","Error");
			   	}	
			}); 
	}
});
</script>
</head>
<body>
<div class="container">
<div class="row" align="center">
<u>Institute Upgrade</u>
<hr>
</div>
</div>
<div class="container">
<div class="row">
<div class="form-group">
<label class="col-md-4" >Enter Institute ID:</label>
<div class="col-md-4">
<input type="text" class="form-control" id="instituteID">
<span id="instituteIDerror" style="color: red"></span>
</div>
<button class="col-md-2 btn btn-primary" id="institutesearch">Search</button>
</div>
</div>
</div>
<div class="container">
<table style="display: none" id="institutestattable" class="table">
<thead>
<tr>
<th>Class Name</th>
<th>Class Owner</th>
<th>Student Ids Allocated</th>
<th>Memory Allcated</th>
<th>Student Ids Used</th>
<th>Memory Allcated Used</th>
<th>Institute Status</th>
</tr>
</thead>
<tbody id="institutestattablebody"></tbody>
</table>
<div class="container alert alert-info" align="center"  style="display: none" id="invalidIDerror">
<span >Invalid Institute Id.</span>
</div>
</div>
<div class="container" id="upgradediv" style="display: none;">
<div class='row'>
<div class='col-md-3'>
<input type='number' id='noofids' class='form-control'>
<span id='noofiderror' style='color:red'></span>
</div>
<div class='col-md-2'><button class='btn btn-primary' id='addids'>Add IDs</button></div>
<div class="col-md-4">
<div class="row"><div class="col-md-10"><input type="number" id="memoryspace" class="form-control"></div><div class="col-md-2" style="margin-top: 10px;padding-left: 0px;">
<span class="badge">MB</span></div></div>
<span id="memoryspaceerror" style="color:red">Please enter memory size</span>
</div>
<div class='col-md-2'><button class='btn btn-primary' id='addmemory'>Add Memory</button></div></div>

</div>
<div class="modal fade" id="idsaddedmodal">
 	<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
          <h4 class="modal-title" id="myModalLabel">Institue Upgrade</h4>
        </div>
        <div class="modal-body" >
		No of Student ID's upgraded successfully.
        </div>

      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Ok</button>
      	</div>

    </div>
</div>
</div>

<div class="modal fade" id="memmoryaddedmodal">
 	<div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
          <h4 class="modal-title" id="myModalLabel">Institue Upgrade</h4>
        </div>
        <div class="modal-body" >
		Memory space upgraded successfully.
        </div>

      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Ok</button>
      	</div>

    </div>
</div>
</div>
</body>
</html>