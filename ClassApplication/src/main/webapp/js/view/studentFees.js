/*****All urls******/
var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var getAllBatchStudentsFeesUrl = "rest/feesservice/getAllBatchStudentsFees/";
var saveStudentBatchFee = "rest/feesservice/saveStudentBatchFeesTransaction/";
var updateStudentFeesAmt = "rest/feesservice/updateStudentFeesAmt/";
var printReceiptUrl = "rest/feesservice/getPrintDetail/";
var sendDueAlertURL ="rest/notification/sendFeesDue"
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
		.on("click",PRINT_RECEIPT,printFeeReceipt)
		.on("click",".sendDue",sendDue)
		.on("click","#sendFeesDue",sendMultipleDue);
	
});
function sendDue(){
	$("#notificationSummary").hide();
	$("#typeError").html("");
	$("#whomeError").html("");
	var flag = true;
	var sms = $("#actionButtons #sms").is(":checked");
	var email = $("#actionButtons #email").is(":checked");
	var parent = $("#actionButtons #parent").is(":checked");
	var student = $("#actionButtons #student").is(":checked");
	var tableRow = $(this).closest('tr');
	var row = feesDataTable.row(tableRow);
	var data = row.data();
	var studentIDArray = [];
	studentIDArray.push(data.student_id)
	if(sms == false && email == false){
		flag = false;
		$("#typeError").html("Select method");
	}
	if(parent == false && student ==false){
		flag = false;
		$("#whomeError").html("Select receipent");
	}
	if(flag){
	var handler = {}
	handler.success = function(response){
		$("#notificationSummary").empty();
		$("#notificationSummary").show();
		if(response.status == null || response.status == ""){
			var totalSMSSent = 0;
			if($("#actionButtons #sms").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					totalSMSSent = totalSMSSent + (response.criteriaStudents-response.studentsWithoutPhone);
				}
				if( $("#actionButtons #parent").is(":checked")){
					totalSMSSent = totalSMSSent + (response.criteriaStudents-response.parentsWithoutPhone);
				}
			}	
			var totalEmailSent = 0;
			if($("#actionButtons #email").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					totalEmailSent = totalEmailSent + (response.criteriaStudents-response.studentsWithoutEmail);
				}
				if( $("#actionButtons #parent").is(":checked")){
					totalEmailSent = totalEmailSent + (response.criteriaStudents-response.parentsWithoutEmail);
				}
			}	
			$("#notificationSummary").append("<div class='row'><div class='col-md-3'>No of students in batch :"+response.totalStudents+"</div>" +
					"<div class='col-md-3'>"+response.criteriaMsg+"</div><div class='col-md-2'>Total SMS Sent:"+totalSMSSent+"</div>" +
					"<div class='col-md-2'>Total Email Sent:"+totalEmailSent+"</div>" +
					"<a data-toggle='collapse' data-target='.a' style='cursor:pointer'>[ view details ]</a></div>");
			if($("#actionButtons #sms").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of students :"+(response.criteriaStudents-response.studentsWithoutPhone)+"</div>" +
							"<div class='col-md-4'>Students without phone no :"+response.studentsWithoutPhone+"</div></div>")
				}
				if( $("#actionButtons #parent").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of parents :"+(response.criteriaStudents-response.parentsWithoutPhone)+"</div>" +
							"<div class='col-md-4'>Parents without phone no :"+response.parentsWithoutPhone+"</div></div>")
				}
			}
			if($("#actionButtons #email").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of students :"+(response.criteriaStudents-response.studentsWithoutEmail)+"</div>" +
							"<div class='col-md-4'>Students without Email ID :"+response.studentsWithoutEmail+"</div></div>")
				}
				if( $("#actionButtons #parent").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of parents :"+(response.criteriaStudents-response.parentsWithoutEmail)+"</div>" +
							"<div class='col-md-4'>Parents without Email ID :"+response.parentsWithoutEmail+"</div></div>")
				}
			}
			/*$.notify({message: "Notification sent successfully"},{type: 'success'});*/
		}else{
			$.notify({message: response.status},{type: 'danger'});
		}
	};//function(e){console.log(e)};
	handler.error = function(e){console.log(e)};
	rest.post(sendDueAlertURL+"/"+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val()+"/"+sms+"/"+email+"/"+parent+"/"+student,handler,studentIDArray.join(","));
	}
}

function sendMultipleDue(){
	$("#notificationSummary").hide();
	var sms = $("#actionButtons #sms").is(":checked");
	var email = $("#actionButtons #email").is(":checked");
	var parent = $("#actionButtons #parent").is(":checked");
	var student = $("#actionButtons #student").is(":checked");
	var studentIDArray = [];
	$('input[name="studentCheckbox"]:checked').each(function() {
		var tableRow = $(this).closest('tr');
		var row = feesDataTable.row(tableRow);
		var data = row.data();
		studentIDArray.push(data.student_id)
		});
	if(studentIDArray.length > 0){
	var handler = {}
	handler.success = function(response){
		$("#notificationSummary").empty();
		$("#notificationSummary").show();
		if(response.status == null || response.status == ""){
			var totalSMSSent = 0;
			if($("#actionButtons #sms").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					totalSMSSent = totalSMSSent + (response.criteriaStudents-response.studentsWithoutPhone);
				}
				if( $("#actionButtons #parent").is(":checked")){
					totalSMSSent = totalSMSSent + (response.criteriaStudents-response.parentsWithoutPhone);
				}
			}	
			var totalEmailSent = 0;
			if($("#actionButtons #email").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					totalEmailSent = totalEmailSent + (response.criteriaStudents-response.studentsWithoutEmail);
				}
				if( $("#actionButtons #parent").is(":checked")){
					totalEmailSent = totalEmailSent + (response.criteriaStudents-response.parentsWithoutEmail);
				}
			}	
			$("#notificationSummary").append("<div class='row'><div class='col-md-3'>No of students in batch :"+response.totalStudents+"</div>" +
					"<div class='col-md-3'>"+response.criteriaMsg+"</div><div class='col-md-2'>Total SMS Sent:"+totalSMSSent+"</div>" +
					"<div class='col-md-2'>Total Email Sent:"+totalEmailSent+"</div>" +
					"<a data-toggle='collapse' data-target='.a' style='cursor:pointer'>[ view details ]</a></div>");
			if($("#actionButtons #sms").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of students :"+(response.criteriaStudents-response.studentsWithoutPhone)+"</div>" +
							"<div class='col-md-4'>Students without phone no :"+response.studentsWithoutPhone+"</div></div>")
				}
				if( $("#actionButtons #parent").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of parents :"+(response.criteriaStudents-response.parentsWithoutPhone)+"</div>" +
							"<div class='col-md-4'>Parents without phone no :"+response.parentsWithoutPhone+"</div></div>")
				}
			}
			if($("#actionButtons #email").is(":checked")){
				if($("#actionButtons #student").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of students :"+(response.criteriaStudents-response.studentsWithoutEmail)+"</div>" +
							"<div class='col-md-4'>Students without Email ID :"+response.studentsWithoutEmail+"</div></div>")
				}
				if( $("#actionButtons #parent").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of parents :"+(response.criteriaStudents-response.parentsWithoutEmail)+"</div>" +
							"<div class='col-md-4'>Parents without Email ID :"+response.parentsWithoutEmail+"</div></div>")
				}
			}
			/*$.notify({message: "Notification sent successfully"},{type: 'success'});*/
		}else{
			$.notify({message: response.status},{type: 'danger'});
		}
	};//function(e){console.log(e)};
	handler.error = function(e){console.log(e)};
	rest.post(sendDueAlertURL+"/"+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val()+"/"+sms+"/"+email+"/"+parent+"/"+student,handler,studentIDArray.join(","));
	}else{
		$.notify({message: "Select students"},{type: 'success'})	
	}
}

function printFeeReceipt(){
	$("#notificationSummary").hide();
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
	$("#notificationSummary").hide();
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
	handler.success = function(e){console.log("success",e);$.notify({message: "Fee saved successfully"},{type: 'success'});
	tableRow.find('.feePaid').html(parseInt(tableRow.find('.feePaid').html())+ parseInt(tableRow.find(PAY_FEE_INPUT).val()));
	}
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
	var feesPaidBefore = tableRow.find('.feePaid').html();
	remainingFee = percentage?(totalFees - (totalFees*discount*0.01) - paidFees - feesPaidBefore):(totalFees-discount - paidFees - feesPaidBefore);
	console.log(discount,paidFees,totalFees,percentage,remainingFee);
	tableRow.find('.remaingFee').html(remainingFee+' &#x20b9;');
	var finalFees = percentage?(totalFees - (totalFees*discount*0.01)):(totalFees-discount);
	tableRow.find('.finalFees').html(finalFees);
	if(paidFees < 0){
		row.child("<textarea class='form-control noteText' placeholder='Add you note for negative transaction if required'></textarea>").show();
	}else{
		if(row.child()){
			row.child().hide();
		}
	}

}
	
function getBatches(){
	$("#notificationSummary").hide();
	$("#studentFeesTableDiv").hide();
	$(BATCH_SELECT).find('option').not('option[value="-1"]').remove();
	$(BATCH_SELECT).find('optgroup').remove();
	var handler = {}
	var division = $(this).val();
	if(division != "-1"){
	handler.success = getBatchSuccess;
	handler.error = getBatchError;
	rest.get(getBatchListUrl+division,handler);
	}else{
		$("#batchSelect").empty();
		 $("#batchSelect").select2().val("").change();
		 $("#batchSelect").select2({data:"",placeholder:"Select Batch"});
	}
}

function getBatchSuccess(batches){
	$(BATCH_SELECT).find('option').not('option[value="-1"]').remove();
	$(BATCH_SELECT).find('optgroup').remove();
	$("#batchSelect").empty();
	var optionsHasLink = "";
	var optionsNoLink = "";
	var optGroupHasLink;
	var optGroupNoLink;
	if(batches.length > 0){
		$(BATCH_SELECT).append("<option value='-1'>Select Batch</option>");
		  $(BATCH_SELECT).select2().val("-1").change();
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
	}else{
    	$("#batchSelect").empty();
		 $("#batchSelect").select2().val("").change();
		 $("#batchSelect").select2({data:"",placeholder:"Batch not available"});
   }
}

function getBatchError(error){
}

function loadStudentTable(data){
	$("#notificationSummary").hide();
	var batchId = $(BATCH_SELECT).val();
	var divId = $(DIVISION_SELECT).val();
	$("#studentFeesTableDiv").hide();
	if(batchId != "-1"){
	var handler = {};
	handler.success = loadStudentTableSuccess;
	handler.error = loadStudentTableError;
	rest.get(getAllBatchStudentsFeesUrl+divId+"/"+batchId,handler);
	}
}

function loadStudentTableSuccess(data){
	console.log(data);
	feesDataTable = $(STUDENT_FEES_TABLE).DataTable({
			autoWidth:false,
			bDestroy:true,
			data: data,
			lengthChange: false,
			columns:[
			{
				title: "",data:null,sDefault:'&mdash;',render:function(data){return "<input type='checkbox' name='studentCheckbox'>"}
			},
			{
				title: "Name",data:null,sDefault:'&mdash;',render:function(data){return data.fname+" "+data.lname}
			},
			{
				title: "Total fee",data:"batch_fees"
			},
			{
				title: "Final fee",data:"final_fees_amt",render:function(finalFees){return "<div class='finalFees'>"+finalFees+"</div>"}
			},
			{
				title: "Discount",bSortable:false,data:"discount",render:function(data){return "<input type='number' class='form-control discount' value='"+data+"'/>"}
			},
			{
				title: "%/&#x20b9;",bSortable:false,data:"discount_type",render:function(disTyp){return "<input type='checkbox' data-size=\"mini\"/ class='percentage' "+((disTyp=='per')?'checked':'')+">"},width:'auto',bSortable: false
			},
			{
				title: "Paid fee",data:"fees_paid",render:function(feePaid){return "<div class='feePaid'>"+feePaid+"</div>"}
			},
			{
				title: "Paying fee",bSortable:false,data:null,render:function(){return "<input type='number' class='form-control payFeesInput'/>"}
			},
			{
				title: "Remaining fee",data:"fees_due",render:function(feeDue){return "<div class='remaingFee' style='text-align:center;'>"+feeDue+" &#x20b9;</div>"}
			},
			{
				title: "",bSortable:false,data:null,render:function(){return "<input type='button' class='btn btn-default payFees' value='Save'/>"}
			},
			{
				title: "",bSortable:false,data:null,render:function(){return "<input type='button' class='btn btn-default printReceipt' value='Print'/>"}
			}
			,
			{
				title: "",bSortable:false,data:null,render:function(){return "<input type='button' class='btn btn-default sendDue' value='Send Due'/>"}
			}
			],
			rowCallback:function(row, data, displayIndex, displayIndexFull){
				$(row).find("input[class=\"percentage\"]").bootstrapSwitch(optionSelect);
			}
		});
	$("#studentFeesTableDiv").show();
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