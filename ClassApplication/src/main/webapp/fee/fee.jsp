<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
table.distributionTable {border:1px solid #000}
.distributionTable td,th {border:none}
</style>
</head>
<body>
	<jsp:include page="FeeHeader.jsp" >
		<jsp:param value="active" name="createFeeStruct"/>
	</jsp:include>
	<div>&nbsp;</div>
	<div class="well">
	<div class="row">
	 <div class="col-lg-5">
		<form>
		<div class="input-group">
			<input type="text" class="form-control" placeholder="Fee name" id="feeStructName" required="required" maxlength="25">
		  <span class="input-group-btn">
			<button id="saveFeeStructure" class="btn btn-success" type="button"><i class="glyphicon glyphicon-floppy-disk"></i>&nbsp;Save</button>
		  </span>
		</div>
		</form>	
			<span id="feeStructureError" class="error"></span>
	  </div>
	  </div>
	  </div>
	   <div class="container" style="margin-left: 2%">
	  <div class="row">
	  <div class="col-lg-5">
	  	<span id="saveDistributionError" class="error"></span>
	  </div>
	  </div>
	  </div>
	  <div class="container" style="margin-left: 2%">
	  <div class="row">
	  <div class="col-lg-5">
		<div class="input-group">
		  <input type="text" class="form-control" placeholder="Distribution item" id="distributionName" maxlength="25">
		  <span class="input-group-btn">
			<button id="addDistribution" class="btn btn-primary" type="button"><i class="glyphicon glyphicon-plus-sign"></i>&nbsp;Add</button>
		  </span>
		</div><!-- /input-group -->
		<span id="addDistributionError" class="error"></span>
	  </div>
	  </div>
	   <div class="row">
	  <div class="col-lg-5">
		<table class="table table-striped distributionTable" border=1 id="distributionTable"></table>
	  </div>
	</div>  
	</div>  
</body>
</html>