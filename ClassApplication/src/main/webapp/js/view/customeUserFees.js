var ADD_DISTRIBUTION = "#addDistribution";
var DISTRIBUTION_TABLE = "#distributionTable";
var DISTRIBUTION_NAME = "#distributionName";
var BUTTON_REMOVE = ".buttonRemove";
var DISTRIBUTION_TABLE_ID = "#distributionTable";
var SAVE_FEE_STRUCTURE = "#saveFeeStructure";
var DISTRIBUTION_ITEMS = ".distributionItems";
var FEE_STRUCT_NAME = "#feeStructName";

var saveUrl = "rest/customuserservice/saveFeeStructre";
$("DOCUMENT").ready(function(){
	$("body").on("click",ADD_DISTRIBUTION,addDistribution).
		on("click",SAVE_FEE_STRUCTURE,saveFeeStructure).
		on('click',BUTTON_REMOVE,removeRow);
});

function addDistribution(){
	$(".error").html("");
	var distributionName = $(this).closest('.row').find(DISTRIBUTION_NAME).val();
	if(distributionName.trim() != ""){
	var tr = $("<tr/>",{});
	var preDistributionItems = $(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS);
	var flag = true;
	if(preDistributionItems != undefined){
	for(var i=0;i<preDistributionItems.length;i++){
		if(distributionName.trim() == $($(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS)[i]).val().trim()){
			$("#addDistributionError").html("Duplicate Fee Distribtion Item Description ")
			flag = false;
		}
	}
	}
	if(flag){
	var fieldTd = $("<td/>",{
	}).appendTo(tr);
	
	var removeBtnTd = $("<td/>",{
		style:"vertical-align: middle;",
		title:'Remove'
	}).appendTo(tr);
	
	var buttonRemove = $('<button/>',{
		type:"button",
		text:"Remove ",
		class:"btn btn-danger buttonRemove"
	}).appendTo(removeBtnTd);
	
	var iButtonRemove = $('<i/>',{
		class:"glyphicon glyphicon-minus-sign",
		style:"color:white"
	}).appendTo(buttonRemove);
	
	var itemEdit = $("<input/>",{
		value:distributionName,
		class:'form-control distributionItems'
	}).appendTo(fieldTd);
	
	
	$(DISTRIBUTION_TABLE).append(tr);
	}
	}else{
		$("#addDistributionError").html("Enter Fee Distribtion Item Description ")
	}
}

function saveFeeStructure(){
	var feesStructureList = [];
	var distribution = $(DISTRIBUTION_TABLE_ID).find(DISTRIBUTION_ITEMS);
	var duplicateExist = false;
	$.each(distribution,function(index){
		var value = $(this).val();
		var field = {};
		field.fees_structure_desc = value;
		feesStructureList.push(field);
		/*
		if(distributionList.indexOf(value)!=-1){
			duplicateExist = true;	
		}
		*/
	});
	var feeStructureToSave = new FeeStructureToSave();
	feeStructureToSave.feesStructureList = feesStructureList;
	feeStructureToSave.fees.fees_desc = $(FEE_STRUCT_NAME).val();
	validateFeeStructure(feeStructureToSave,saveFeeStructureInDb);
	
}

function saveFeeStructureInDb(struct){
	var handler = {};
	handler.success = saveFeeStructureInDbSuccess;
	handler.error = saveFeeStructureInDbError;
	console.log("Saving....",JSON.stringify(struct));
	rest.post(saveUrl,handler,JSON.stringify(struct));
}

function saveFeeStructureInDbSuccess(e){
	console.log(e);
	if(e==false){
		if($("#feeStructName").closest("form").find(".error").length == 0){
			$("#feeStructName").closest("form").append("<div class='error'>Fee Structure with same name already exists.</div>");
			$.notify({message: 'Fee Structure with same name already exists.'},{type: 'danger'});
		}else{
			$("#feeStructName").closest("form").find(".error").html("Fee Structure with same name already exists.");
			$.notify({message: 'Fee Structure with same name already exists.'},{type: 'danger'});
		}
	}else{
		$("#feeStructName").closest("div").find(".error").remove();
		$.notify({message: 'Fee Structure saved.'},{type: 'success'});
	}
}

function saveFeeStructureInDbError(e){
	console.log(e);
}

function validateFeeStructure(struct,saveFunction){
	$(".error").html("");
	var isValid = true;
	if($("#feeStructName").val().trim() == ""){
		$("#feeStructureError").html("Enter Fee Description");
		isValid = false;
	}else if(!$("#feeStructName").val().trim().match(/^[A-Za-z0-9\s-]*$/)){
		$("#feeStructureError").html("Invalid Fee Description");
		isValid = false;
	}
	var preDistributionItems = $(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS);
	if(preDistributionItems != undefined){
	for(var i=0;i<preDistributionItems.length;i++){
		for(var j=i;j<preDistributionItems.length;j++){
		if($($(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS)[j]).val().trim() == $($(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS)[i]).val().trim() && i!=j){
			$("#saveDistributionError").html("Duplicate Fee Distribtion Item Description ")
			isValid = false;
		}
		}
	}
	}
	if(isValid){
	if(!struct.feesStructureList || !struct.feesStructureList.length){
		modal.modalConfirm("Error","Distribution not exist","Add Distribution","Continue anyway",function(){
			saveFunction(struct);
		},[]);
	}else{
		saveFunction(struct);
	}
	}
	return isValid;
}

function removeRow(){
	$(this).closest('tr').remove();
}

/*REST objects*/
function FeeStructureToSave(){
	this.fees = {};
	this.feesStructureList;
	this.structureName;
}