var TABLE = "#viewNEditFeeStruct";
var EDIT = ".tableIconEdit";
var DELETE = ".tableIconDelete";
var LINK = ".tableIconLink";
var FEE_CONTENT_DIV = "#viewNEditFeeStructContent";
var FEE_CONTENT_DIV_NAME = FEE_CONTENT_DIV+"Name";
var FEE_CONTENT_DIV_DATA = FEE_CONTENT_DIV+"Data";
var DISTRIBUTION_TABLE = "#distributionTable";
var BUTTON_REMOVE = ".buttonRemove";

var ADD_DISTRIBUTION = "#addDistribution";
var DISTRIBUTION_NAME = "#distributionName";
var DISTRIBUTION_TABLE_ID = "#distributionTable";
var SAVE_FEE_STRUCTURE = "#saveFeeStructure";
var DISTRIBUTION_ITEMS = ".distributionItems";
var FEE_STRUCT_NAME = "#feeStructName";
var EDIT_DISTRIBUTION_WRAPPER = ".editDistributionWrapper";

/**/
var editIcon = "<i class='glyphicon glyphicon-edit tableIcon tableIconEdit' title='Edit'></i>";
var deleteIcon = "<i class='glyphicon glyphicon-trash tableIcon tableIconDelete' title='Delete'></i>";
var linkIcon = "<i class='glyphicon glyphicon-hand-right tableIcon tableIconLink' title='Link'></i>";
var buttons = editIcon+deleteIcon+linkIcon;

/**/
var getListUrl = "rest/feesservice/getAllFeeStructre";
var getDetailsUrl = "rest/feesservice/getFeeStructre/";
var updateUrl = "rest/feesservice/updateFeeStructre";
var dataTable;
$(document).ready(function(){
	loadFeeStructureTable();
	$("body").on("click",EDIT,editFeeStructure)
		.on("click",ADD_DISTRIBUTION,addDistribution)
		.on("click",DELETE,deleteFeeStrcture)
		.on("click",LINK,linkFeeStructure)
		.on('click',BUTTON_REMOVE,removeRow)
		.on("click",SAVE_FEE_STRUCTURE,saveFeeStructure);
		
});

function loadFeeStructureTable(){
	$(EDIT_DISTRIBUTION_WRAPPER).hide();
	var handler = {};
	handler.success = onGetAllFeeStructureSuccess;
	handler.error = function(e){console.log(e)};
	rest.get(getListUrl,handler);
	//onGetAllFeeStructureSuccess(data);
}
function onGetAllFeeStructureSuccess(data){
	dataTable = $(TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns:[
	{
		title: "Fee structure",data:'fees_desc',sWidth:"70%"
	},
	{
		title: "",data:null,sWidth:"10%",render:function(){return buttons}
	}]});
}
function editFeeStructure(){	
	$(EDIT_DISTRIBUTION_WRAPPER).show();
	var tRow = $(this).closest('tr');
	var rowData = dataTable.row(tRow).data();
	loadFeeStructure(rowData.fees_id,rowData.fees_desc);
}
function deleteFeeStrcture(){
	var tRow = $(this).closest('tr');
	var rowData = dataTable.row(tRow).data();
	modal.modalConfirm("Delete","Do you want to delete "+rowData.fee_struct_id+" ?","No","Delete",function(){
			deleteFeeStructureRest(rowData.fee_struct_id);
	},[]);
	
}
function linkFeeStructure(){
	
}
function deleteFeeStructureRest(id){
	console.log("Deleting "+id);
}

function deleteFeeStructureSuccess(){}
function deleteFeeStructureError(){}

function loadFeeStructure(id,name){
	$(DISTRIBUTION_TABLE).empty();
	$(FEE_STRUCT_NAME).val(name);
	$(FEE_STRUCT_NAME).data("fees_id",id);
	var handler = {};
	handler.success = loadFeeStructureSuccess;
	handler.error = function(e){console.log(e)}
	rest.get(getDetailsUrl+id,handler);	
	//loadFeeStructureSuccess(data);
}

function loadFeeStructureSuccess(data){
	//$(FEE_STRUCT_NAME).val(data.structureName);
	
	$.each(data.feesStructureList,function(index,val){
		addDistribution(val);
	});
}

function addDistribution(data){
	var distributionName = "";
	if(data.fees_structure_desc){
		distributionName = data.fees_structure_desc;
	}else{
		distributionName = $(this).closest('.row').find(DISTRIBUTION_NAME).val();
	}
	var tr = $("<tr/>",{});
	
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
	}).data("item_id",data.fees_structure_id)
	.appendTo(fieldTd);
	
	$(DISTRIBUTION_TABLE).append(tr);
}

/*From create fee structure*/
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
	var id = $(FEE_STRUCT_NAME).data("fees_id");
	feeStructureToSave.fees.fees_id = id;
	validateFeeStructure(feeStructureToSave,saveFeeStructureInDb);	
}

function saveFeeStructureInDb(struct){
	var handler = {};
	handler.success = saveFeeStructureInDbSuccess;
	handler.error = saveFeeStructureInDbError;
	console.log("Saving....",JSON.stringify(struct));
	rest.post(updateUrl,handler,JSON.stringify(struct));
}

function saveFeeStructureInDbSuccess(e){
	console.log(e);
}

function saveFeeStructureInDbError(e){
	console.log(e);
}

function validateFeeStructure(struct,saveFunction){
	var isValid = true;
	if(!$(FEE_STRUCT_NAME).closest('form').valid()){
		isValid = false;
	}else if(!struct.feesStructureList || !struct.feesStructureList.length){
		modal.modalConfirm("Error","Distribution not exist","Add Distribution","Continue anyway",function(){
			saveFunction(struct);
		},[]);
	}else{
		saveFunction(struct);
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