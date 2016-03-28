var TABLE = "#viewNEditFeeStruct";
var DISTRIBUTION_TABLE = "#distributionTable";
var ADD_DISTRIBUTION = "#addDistribution";
var DISTRIBUTION_NAME = "#distributionName";
var DISTRIBUTION_TABLE_ID = "#distributionTable";
var SAVE_FEE_STRUCTURE = "#saveFeeStructure";
var DISTRIBUTION_ITEMS = ".distributionItems";
var FEE_STRUCT_NAME = "#feeStructName";
var FEE_DISTRIBUTION_WRAPPER = ".feeDistributionWrapper";
var FEE_TABLE_CONTAINER = "#viewNEditFeeStructWrapper";
var LINK_BATCH_CONTAINER = "#linkBatchContainer";
var LINK_FEE_STRUCT = "#linkFeeStructure";

var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var FEE_STRUCT_SELECT = "#feeStructSelect";

var FEE_DIST_AMMOUNT = ".feeDistAmount";
var FEE_DIST_SAVE = "#distributionSave";
var BATCH_N_FEE_LIST_TABLE = "#BatchNFeeListContainerTable";
var LOAD_BATCH_FEE_TABLE = "#batchNFeeTableLoad";
/**/
var getListUrl = "rest/feesservice/getAllFeeStructre";
var getDetailsUrl = "rest/feesservice/getFeeStructre/";
var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var saveFeeStructUrl = "rest/feesservice/saveBatchFeesDistribution";
var getBatchFeesDistributionUrl = "rest/feesservice/getBatchFeesDistribution/";
var updateBatch = "rest/feesservice/updateBatchFeesDistribution";

var dataTable;

$(document).ready(function(){
	linkFeeStructure();
	init();
	$("body").on("click",ADD_DISTRIBUTION,addDistribution)
		.on('input',FEE_DIST_AMMOUNT,calculateAmount)
		.on('click',FEE_DIST_SAVE,saveFee)
		.on('change',BATCH_SELECT,onBatchSelect)
		.on('change',FEE_STRUCT_SELECT,linkFeeStruct);
});

function init(){
	$(FEE_DISTRIBUTION_WRAPPER).hide();
	$(FEE_STRUCT_SELECT).attr("disabled","disabled");
	$(BATCH_SELECT).attr("disabled","disabled");
}

function loadbatchNFeeTable(){
	$(DISTRIBUTION_TABLE).empty();
	var handler = {};
	var division = $(DIVISION_SELECT).val();
	var batch = $(BATCH_SELECT).val();
	handler.success = function(data){
		loadFeeStructureSuccess(data,"update");
		$(FEE_STRUCT_SELECT).val(data.batchFees.fees_id);
		loadAmmount(data);
		calculateAmount();
	};
	handler.error = function(e){console.log(e)}
	rest.get(getBatchFeesDistributionUrl+division+"/"+batch,handler);
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

function loadFeeStructure(id,name){
	$(DISTRIBUTION_TABLE).empty();
	$(FEE_STRUCT_NAME).val(name);
	$(FEE_STRUCT_NAME).data("fees_id",id);
	var handler = {};
	handler.success = function(data){
		loadFeeStructureSuccess(data,"save");
		loadAmmount(data);
		calculateAmount();
	}
	handler.error = function(e){console.log(e)}
	rest.get(getDetailsUrl+id,handler);	
	//loadFeeStructureSuccess(data);
}

function loadFeeStructureSuccess(data,state){
	//$(FEE_STRUCT_NAME).val(data.structureName);
	if(state == "update"){
		$(DISTRIBUTION_TABLE).data("DIST_ID",data);
	}else{
		$(DISTRIBUTION_TABLE).data("DIST_ID",undefined);
	}
	
	if(data.batchFeesDistribution){
		$.each(data.batchFeesDistribution,function(index,val){
			if(data.batchFees){
				val.batch_fees_id = data.batchFees.batch_fees_id;
			}
		});
	}
	
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
	
	var buttonRemove = $('<input/>',{
		type:"number",
		placeholder:"Amount",
		class:"form-control feeDistAmount"
	}).appendTo(removeBtnTd);
	
	var iButtonRemove = $('<i/>',{
		class:"glyphicon glyphicon-minus-sign",
		style:"color:white"
	}).appendTo(buttonRemove);
	
	var itemEdit = $("<input/>",{
		value:distributionName,
		class:'form-control distributionItems'
	}).data("fees_structure_id",data.fees_structure_id)
	.data("batch_fees_id",data.batch_fees_id)
	.appendTo(fieldTd);
	
	$(DISTRIBUTION_TABLE).append(tr);
	$(FEE_DISTRIBUTION_WRAPPER).show();
}

function getBatches(){
	$(FEE_STRUCT_SELECT).attr("disabled","disabled");
	$(FEE_STRUCT_SELECT).val(-1);
	var handler = {}
	var division = $(this).val();
	handler.success = getBatchSuccess;
	handler.error = getBatchError;
	rest.get(getBatchListUrl+division,handler);
}

function getBatchSuccess(batches){
	$(BATCH_SELECT).removeAttr("disabled","disabled");
	$(BATCH_SELECT).find('option').not('option[value="-1"]').remove();
	$(BATCH_SELECT).find('optgroup').remove();
	var optionsHasLink = "";
	var optionsNoLink = "";
	var optGroupHasLink;
	var optGroupNoLink;
	$.each(batches,function(index,val){
		if(val.feesLinkStatus == "Yes"){
			optionsHasLink = optionsHasLink + "<option value='"+val.batch.batch_id+"'>"+val.batch.batch_name+"</option>";
		}else{
			optionsNoLink = optionsNoLink + "<option value='"+val.batch.batch_id+"'>"+val.batch.batch_name+"</option>";
		}
	});
	
	if(optionsHasLink && optionsHasLink.trim().length){
		optGroupHasLink = '<optgroup label="Linked" linked="yes">'+optionsHasLink+'</optgroup>'
	}
	
	if(optionsNoLink && optionsNoLink.trim().length){
		optGroupNoLink = '<optgroup label="No link" linked="no">'+optionsNoLink+'</optgroup>';
	}
	$(BATCH_SELECT).append(optGroupHasLink+optGroupNoLink).trigger('change');	
}

function getBatchError(error){
	$(BATCH_SELECT).attr("disabled","disabled");
	$(BATCH_SELECT).find('option').not('option[value="-1"]').remove();
}

function calculateAmount(){
	var feeDist = $(FEE_DIST_AMMOUNT);
	var total = 0;
	$.each(feeDist,function(index,val){
		var num = parseFloat($(val).val());
		if(!isNaN(num)){
			total = total + num;
		}
	});
	$('.total').text(total);
	$('.total').data('total',total);
}
function linkFeeStruct(){
	var batchSelect = $(BATCH_SELECT).val();
	var feeStruct = $(FEE_STRUCT_SELECT).val();
	loadFeeStructure(feeStruct,'rowData.fees_desc');
}
function saveFee(){
	var data = $(DISTRIBUTION_TABLE).data("DIST_ID");
	var saveBean = new SaveBean();
	var updateOrSave = "Save";
	if(data){
		updateOrSave = "Update";
		saveBean = $.extend(saveBean,data);
	}else{
		var feeId = $(FEE_STRUCT_SELECT).val();
		saveBean.batchFees.fees_id = feeId;
		saveBean.batchFees.div_id = $(DIVISION_SELECT).val();
		saveBean.batchFees.batch_id = $(BATCH_SELECT).val();
		saveBean.batchFees.batch_fees = $(".total").data('total');
	}
	
	var tableRow = $(DISTRIBUTION_TABLE).find('tr');
	var batchFeesDistributions = [];
	$.each(tableRow,function(){
		var batchFeesDistribution = new BatchFeesDistribution();
		var fees_structure_id = $(this).find(DISTRIBUTION_ITEMS).data("fees_structure_id");
		var batch_fees_id = $(this).find(DISTRIBUTION_ITEMS).data("batch_fees_id");
		var fees_amount = $(this).find(FEE_DIST_AMMOUNT).val();
		//This parameter is constantly same and mandatory and has to be set on save only
		if(updateOrSave == "Save"){
			saveBean.batchFees.batch_fees_id = batch_fees_id;
		}
		batchFeesDistribution.batch_fees_id = saveBean.batchFees.batch_fees_id;
		batchFeesDistribution.fees_structure_id = fees_structure_id;
		batchFeesDistribution.fees_amount = fees_amount;
		batchFeesDistribution.fees_id = saveBean.batchFees.fees_id;
		batchFeesDistributions.push(batchFeesDistribution);
	});
	
	//var saveBean = new SaveBean();
	//saveBean.batchFees = batchFees;
	saveBean.batchFeesDistribution = batchFeesDistributions;
	var handler = {};
	handler.success = function(e){console.log(e)};
	handler.error = function(e){console.log(e)};
	if(updateOrSave == "Update"){
		console.log(saveBean);
		rest.post(updateBatch,handler,JSON.stringify(saveBean));
	}else{
		rest.post(saveFeeStructUrl,handler,JSON.stringify(saveBean));
	}
}

function loadAmmount(data){
	var distItem  = $(DISTRIBUTION_ITEMS);
	var ammountData = data.batchFeesDistribution;
	
	$.each(distItem,function(){
		var fStrId = $(this).data("fees_structure_id");
		for(var i = 0; ammountData && i<ammountData.length;i++){
			if(ammountData[i].fees_structure_id == fStrId){
				$(this).closest('tr').find(".feeDistAmount").val(ammountData[i].fees_amount);
				break;
			}
		}	
	});
}

function onBatchSelect(){
	var selected = $(':selected',this);
	if(selected.val()==-1){
		$(FEE_STRUCT_SELECT).attr("disabled","disabled");
	}else{
		$(FEE_STRUCT_SELECT).val(-1);
		$(FEE_STRUCT_SELECT).removeAttr("disabled");
	}
	
	if(selected.closest('optgroup') && selected.closest('optgroup').attr('linked') && selected.closest('optgroup').attr('linked').toLowerCase() == 'yes'){
		loadbatchNFeeTable();
		//$(LOAD_BATCH_FEE_TABLE).show();
	}else{
		$(FEE_DISTRIBUTION_WRAPPER).hide();
	}
}

/*Rest */
function BatchFees(){
	this.fees_id;
	this.div_id;
	this.batch_id;
	this.batch_fees;
	this.batch_fees_id;
}

function BatchFeesDistribution(){
	this.batch_fees_id;
	this.fees_structure_id;
	this.fees_amount;
	this.fees_id
}

function SaveBean(){
	this.batchFees = {};
	this.batchFeesDistribution = {};
}	