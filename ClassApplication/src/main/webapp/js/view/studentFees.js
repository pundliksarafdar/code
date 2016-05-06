/*****All urls******/
var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var getAllBatchStudentsFeesUrl = "rest/feesservice/getAllBatchStudentsFees/";
var saveStudentBatchFee = "rest/feesservice/saveStudentBatchFeesTransaction/";
var updateStudentFeesAmt = "rest/feesservice/updateStudentFeesAmt/";
var printReceiptUrl = "rest/feesservice/getPrintDetail/";

/*************/
var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var STUDENT_FEES_TABLE = "#studentFeesTable";
var PAY_FEE_INPUT = ".payFeesInput";
var PAY_FEE = ".payFees";
var NOTE_TEXT = ".noteText";
var PRINT_RECEIPT = ".printReceipt";
var RECEIPT = "#printReceipt";
/*Static value*/
var feesDataTable;
var optionSelect = {
	onText:"%",
	offText:"&#x20b9;"
}
$(document).ready(function(){
	$("body").on("change",DIVISION_SELECT,getBatches)
		.on("change",BATCH_SELECT,loadStudentTable)
		.on("switchChange.bootstrapSwitch input",STUDENT_FEES_TABLE+" input",calculateFee)
		.on("click",PAY_FEE,payfee)
		.on("click",PRINT_RECEIPT,printFeeReceipt);
	
});

function printFeeReceipt(){
	var tableRow = $(this).closest('tr');
	var row = feesDataTable.row(tableRow);
	var data = row.data();
	
	var handler = {}
	handler.success = printFeesSuccess;//function(e){console.log(e)};
	handler.error = function(e){console.log(e)};
	rest.get(printReceiptUrl+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val()+"/"+data.student_id+"/"+1,handler);
}

function printFeesSuccess(data){
	
	console.log(data);
	
	$.get('tmpl/feereceipt.tmpl', function(template){
		var rendered = Mustache.render(template, data);
		$('#printableReceipt').html(rendered);
		var receiptData = $('#printableReceipt').html(); 
		newWin= window.open("");
		newWin.document.write(receiptData);
		newWin.print();
		newWin.close();
	  });
}
function payfee(){
	var payFeeBean = new PayFeeBean();
	var tableRow = $(this).closest('tr');
	var row = feesDataTable.row(tableRow);
	var data = row.data();
	
	payFeeBean.student_id = data.student_id;
	payFeeBean.div_id = $(DIVISION_SELECT).val();
	payFeeBean.batch_id = $(BATCH_SELECT).val();
	payFeeBean.amt_paid = tableRow.find(PAY_FEE_INPUT).val();
	payFeeBean.note = tableRow.next().find(NOTE_TEXT).val();
	payFeeBean.disType = tableRow.find('[type="checkbox"]').is(':checked')?"per":"amt";
	payFeeBean.discount = tableRow.find(".discount").val();
	console.log(JSON.stringify(payFeeBean));
	var handler = {};
	handler.success = function(e){console.log("success",e);$.notify({message: "Fee saved successfully"},{type: 'success'});}
	handler.error = function(e){console.log("error",e)}
	rest.post(saveStudentBatchFee,handler,JSON.stringify(payFeeBean));
}

function calculateFee(){
	var tableRow = $(this).closest('tr');
	var row = feesDataTable.row(tableRow);
	var data = row.data();
	var discount = tableRow.find('.discount').val();
	var paidFees = tableRow.find(PAY_FEE_INPUT).val();
	var totalFees = data.batch_fees;
	var percentage = tableRow.find('[type="checkbox"]').is(':checked');
	remainingFee = percentage?(totalFees - (totalFees*discount*0.01) - paidFees):(totalFees-discount - paidFees);
	console.log(discount,paidFees,totalFees,percentage,remainingFee);
	tableRow.find('.remaingFee').html(remainingFee+' &#x20b9;');
	
	if(paidFees < 0){
		row.child("<textarea class='form-control noteText' placeholder='Add you note for negative transaction if required'></textarea>").show();
	}else{
		if(row.child()){
			row.child().hide();
		}
	}
	
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
	$(BATCH_SELECT).append(optGroupHasLink+optGroupNoLink);	
}

function getBatchError(error){
}

function loadStudentTable(data){
	var batchId = $(BATCH_SELECT).val();
	var divId = $(DIVISION_SELECT).val();
	var handler = {};
	handler.success = loadStudentTableSuccess;
	handler.error = loadStudentTableError;
	rest.get(getAllBatchStudentsFeesUrl+divId+"/"+batchId,handler);
}

function loadStudentTableSuccess(data){
	console.log(data);
	feesDataTable = $(STUDENT_FEES_TABLE).DataTable({
			bDestroy:true,
			data: data,
			lengthChange: false,
			columns:[
			{
				title: "Name",data:null,sDefault:'&mdash;',render:function(data){return data.fname+" "+data.lname}
			},
			{
				title: "Total fee",data:"batch_fees"
			},
			{
				title: "Final fee",data:"final_fees_amt"
			},
			{
				title: "Discount",bSortable:false,data:"discount",render:function(data){return "<input type='text' class='form-control discount' value='"+data+"'/>"}
			},
			{
				title: "%/&#x20b9;",bSortable:false,data:"discount_type",render:function(disTyp){return "<input type='checkbox' data-size=\"mini\"/ class='percentage' "+((disTyp=='per')?'checked':'')+">"},width:'auto',bSortable: false
			},
			{
				title: "Paid fee",data:"fees_paid",render:function(feePaid){return "<div>"+feePaid+"</div>"}
			},
			{
				title: "Pay",bSortable:false,data:null,render:function(){return "<input type='text' class='form-control payFeesInput'/>"}
			},
			{
				title: "Remaining fee",data:"fees_due",render:function(feeDue){return "<div class='remaingFee' style='text-align:center;'>"+feeDue+" &#x20b9;</div>"}
			},
			{
				title: "",bSortable:false,data:null,render:function(){return "<input type='button' class='btn btn-default payFees' value='Pay'/>"}
			},
			{
				title: "",bSortable:false,data:null,render:function(){return "<input type='button' class='btn btn-default printReceipt' value='Print'/>"}
			}
			
			]
		});
		$(STUDENT_FEES_TABLE).find("input[type=\"checkbox\"]").bootstrapSwitch(optionSelect);
}

function loadStudentTableError(){
	
}

/**/
function PayFeeBean(){
	this.student_id;
	this.div_id;
	this.batch_id;
	this.amt_paid;
	this.note;
}