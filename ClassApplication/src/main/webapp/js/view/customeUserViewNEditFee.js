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
var FEE_TABLE_CONTAINER = "#viewNEditFeeStructWrapper";
var LINK_BATCH_CONTAINER = "#linkBatchContainer";
var LINK_FEE_STRUCT = "#linkFeeStructure";

var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var FEE_STRUCT_SELECT = "#feeStructSelect";
/**/
var editIcon = "<button class='btn btn-primary btn-xs tableIcon tableIconEdit'>Edit</button>";
var deleteIcon = "<button class='btn btn-danger btn-xs tableIcon tableIconDelete'>Delete</button>";
var buttons = editIcon+deleteIcon;
var BACKTOLIST =".backToList";
/**/
var getListUrl = "rest/customuserservice/getAllFeeStructre";
var getDetailsUrl = "rest/customuserservice/getFeeStructre/";
var updateUrl = "rest/customuserservice/updateFeeStructre";
var delelteUrl = "rest/customuserservice/deleteFeeStructre/";
var getBatchListUrl = "rest/classownerservice/getBatches/";
var child_mod_access  = [];
var dataTable;
$(document).ready(function(){
	child_mod_access  = $("#accessRights").val().split(",");
	buttons = "";
	if($.inArray( "43", child_mod_access) != "-1"){
	buttons = buttons + editIcon;
	}
	if($.inArray( "44", child_mod_access) != "-1"){
	buttons = buttons + deleteIcon;
	}
	loadFeeStructureTable();
	$("body").on("click",EDIT,editFeeStructure)
		.on("click",ADD_DISTRIBUTION,addDistribution)
		.on("click",DELETE,deleteFeeStrcture)
		.on("click",LINK,linkFeeStructure)
		.on('click',BUTTON_REMOVE,removeRow)
		.on('click',LINK_FEE_STRUCT,linkFeeStruct)
		.on("click",SAVE_FEE_STRUCTURE,saveFeeStructure)
		.on("click",BACKTOLIST,backToFeeList);
		
});

function loadFeeStructureTable(){
	$(EDIT_DISTRIBUTION_WRAPPER).hide();
	$(LINK_BATCH_CONTAINER).hide();
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
		title: "",data:null,bSortable:false,sWidth:"10%",render:function(){return buttons}
	}]});
}
function editFeeStructure(){	
	$(FEE_TABLE_CONTAINER).hide();
	$(EDIT_DISTRIBUTION_WRAPPER).show();
	var tRow = $(this).closest('tr');
	var rowData = dataTable.row(tRow).data();
	loadFeeStructure(rowData.fees_id,rowData.fees_desc);
}
function deleteFeeStrcture(){
	var tRow = $(this).closest('tr');
	var rowData = dataTable.row(tRow).data();
	console.log(rowData);
	modal.modalConfirm("Delete","Do you want to delete "+rowData.fees_desc+" ?","No","Delete",function(){
			deleteFeeStructureRest(rowData.fees_id);
	},[]);
	
}
function linkFeeStructure(){
	$(FEE_TABLE_CONTAINER).hide();
	$(LINK_BATCH_CONTAINER).show();
	$(DIVISION_SELECT).on("change",getBatches);
	$(BATCH_SELECT);
	
	var handler = {};
	handler.success = loadFeeStructList;
	handler.error = function(e){console.log(e)};
	rest.get(getListUrl,handler);
}

function loadFeeStructList(data){
	var optionText = "";
	$(FEE_STRUCT_SELECT).find("option").not('option[value="-1"]').remove();
	$.each(data,function(){
		optionText = optionText + "<option value='"+this.fees_id+"'>"+this.fees_desc+"</option>";
	});
	$(FEE_STRUCT_SELECT).append(optionText);
}
function deleteFeeStructureRest(id){
	var handler = {};
	handler.success = function(e){
		loadFeeStructureTable();
		$.notify({message: 'Fee deleted successfully'},{type: 'success'});
	}
	handler.error = function(e){console.log(e)}
	rest.deleteItem(delelteUrl+id,handler);
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
	$(".error").html("");
	var distributionName = "";
	var flag = true;
	if(data.fees_structure_desc){
		distributionName = data.fees_structure_desc;
	}else{
		distributionName = $(this).closest('.row').find(DISTRIBUTION_NAME).val();
		if(distributionName.trim() == ""){
			$("#addDistributionError").html("Enter Fee Distribtion Item Description ")
			flag = false;
		}else{
			var preDistributionItems = $(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS);
			if(preDistributionItems != undefined){
			for(var i=0;i<preDistributionItems.length;i++){
				if(distributionName.trim() == $($(DISTRIBUTION_TABLE).find(DISTRIBUTION_ITEMS)[i]).val().trim()){
					$("#addDistributionError").html("Duplicate Fee Distribtion Item Description ")
					flag = false;
				}
			}
			}
		}
	}
	if(flag == true ){
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
	}).data("fees_structure_id",data.fees_structure_id)
	.appendTo(fieldTd);
	
	$(DISTRIBUTION_TABLE).append(tr);
	}
}

/*From create fee structure*/
function saveFeeStructure(){
	var feesStructureList = [];
	var distribution = $(DISTRIBUTION_TABLE_ID).find(DISTRIBUTION_ITEMS);
	var duplicateExist = false;
	$.each(distribution,function(index){
		var value = $(this).val();
		var id = $(this).data("fees_structure_id");
		
		var field = {};
			field.fees_structure_desc = value;
		if(id){
			field.fees_structure_id = id;
		}
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
	feeStructureToSave.fees.fees_id = $(FEE_STRUCT_NAME).data("fees_id");
	validateFeeStructure(feeStructureToSave,saveFeeStructureInDb);	
}

function saveFeeStructureInDb(struct){
	var handler = {};
	handler.success = saveFeeStructureInDbSuccess;
	handler.error = saveFeeStructureInDbError;
	rest.post(updateUrl,handler,JSON.stringify(struct));
}

function saveFeeStructureInDbSuccess(e){
	if(!e){
		$.notify({message: 'Error fee with same name already exist'},{type: 'danger'});
		$(FEE_STRUCT_NAME).closest('form').validate().showErrors({
			"feeStructName":"Fee strutcture with same name already exist"
		});
	}else{
		$.notify({message: 'Fee structure saved'},{type: 'success'});
		$(FEE_TABLE_CONTAINER).show();
		$(EDIT_DISTRIBUTION_WRAPPER).hide();
		loadFeeStructureTable();	
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

function getBatches(){
	var handler = {}
	var division = $(this).val();
	handler.success = getBatchSuccess;
	handler.error = getBatchError;
	rest.get(getBatchListUrl+division,handler);
}

function getBatchSuccess(batches){
	$(BATCH_SELECT).find('option').not('option[value="-1"]').remove();
	var options = "";
	$.each(batches,function(index,val){
		options = options + "<option value='"+val.batch_id+"'>"+val.batch_name+"</option>";
	});
	$(BATCH_SELECT).append(options);	
}

function getBatchError(error){
}

function linkFeeStruct(){
	var batchSelect = $(BATCH_SELECT).val();
	var feeStruct = $(FEE_STRUCT_SELECT).val();
	console.log(batchSelect,feeStruct);
}

function backToFeeList(){
	$(FEE_TABLE_CONTAINER).show();
	$(EDIT_DISTRIBUTION_WRAPPER).hide();
}

/*REST objects*/
function FeeStructureToSave(){
	this.fees = {};
	this.feesStructureList;
	this.structureName;
}