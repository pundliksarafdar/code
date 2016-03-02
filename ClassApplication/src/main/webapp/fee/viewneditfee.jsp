<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
.tableIcon{
	margin:3px;
	cursor:pointer
}

.tableIcon:hover{
	color: blue;
}
</style>
</head>
<body>
	<jsp:include page="FeeHeader.jsp" >
		<jsp:param value="active" name="viewneditFeeStruct"/>
	</jsp:include>
	<div class="container">
	<div class="row">
	<div class="col-lg-12">
	<div id="viewNEditFeeStructWrapper">
		<table id="viewNEditFeeStruct"></table>
	</div>
	</div>
	  <div class="col-lg-12 editDistributionWrapper">
		<div class="input-group">
		  <input type="text" class="form-control" placeholder="Distribution item" id="distributionName">
		  <span class="input-group-btn">
			<button id="addDistribution" class="btn btn-default" type="button"><i class="glyphicon glyphicon-plus-sign"></i>&nbsp;Add</button>
		  </span>
		</div><!-- /input-group -->
	  </div>
	  <div class="col-lg-12 editDistributionWrapper">
			<table id="distributionTable" class="table table-striped distributionTable"></table>
			  </div>
	  <div class="col-lg-12 editDistributionWrapper" id="">
		<form>
		<div class="input-group">
			<input type="text" class="form-control" placeholder="Fee name" id="feeStructName" required="required">
		  <span class="input-group-btn">
			<button id="saveFeeStructure" class="btn btn-default" type="button"><i class="glyphicon glyphicon-floppy-disk"></i>&nbsp;Save</button>
		  </span>
		</div>
		</form>	
	  </div>
	</div>
		</div>
	
</body>
</html>