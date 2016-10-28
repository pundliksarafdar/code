var BATCH_SELECT = "#batches"; 
var DIV_SELECT = "#division";
var ADD_STUDENT = "#addStudent";
var ADDITIONAL_FORM_FIELD = "#additionalFormFields";
var ADDITIONAL_FORM_FIELD_TO_SAVE = "#additionalFormFieldsToSave";

var selectedBatchData = "";
var getBatchFeesUrl = "rest/feesservice/getBatchFees/";
var addStudentManuallyUrl = 'rest/classownerservice/addStudentByManually';
var addStudentByID = 'rest/classownerservice/addStudentByID';
var formFieldUrl = "rest/ClassownerSettings/formField";

$(document).ready(function(){
	$("body").on("change",BATCH_SELECT,selectBacth)
		.on("click",ADD_STUDENT,addStudent);
	showTable([]);
	loadFormField();
});

function loadFormField(){
	var handler = {};
	handler.success = loadFormFieldSuccess;
	handler.error = loadFormFieldError;
	rest.get(formFieldUrl,handler);
}

function loadFormFieldSuccess(e){
	var formField = JSON.parse(e.formField);
	if(formField){
		$.each(formField,function(key,val){
			var additionFormField = $(ADDITIONAL_FORM_FIELD).html();
			var $additionFormField = $(additionFormField);
			$additionFormField.find("#formFieldLabel").text(val);
			$additionFormField.find("#formFieldValue").attr("fieldId",key);
			$(ADDITIONAL_FORM_FIELD_TO_SAVE).append($additionFormField);
		});
	}else{
		$(ADDITIONAL_FORM_FIELD_TO_SAVE).html("<div class='alert alert-warning'>No additional field to save please add additional formfield from setting if applicable or <a href='studentFormFieldAction'>click here</a> to go to page</div>")
	}
}

function loadFormFieldError(){
	
}

function selectBacth(){
	var batch = $(this).val();
	var divisionId = $(DIV_SELECT).val();
	selectedBatchData = $(this).select2('data')
	var handler = {};
	handler.success = getBatchFeesSuccess;
	handler.error = getBatchFeesError;
	rest.post(getBatchFeesUrl+divisionId,handler,JSON.stringify(batch));
}

var getBatchFeesSuccess = function(data){
	showTable(data);
}

var getBatchFeesError = function(e){console.log(e);}

var showTable = function(data){
	feesDataTable = $("#dataTableForFees").DataTable({
			bDestroy:true,
			data: data,
			lengthChange: false,
			columns:[
			{
				title: "Batch",data:null,render:function(data,event,row){
					for(var i=0 ; i<selectedBatchData.length;i++){
						if(selectedBatchData[i].id == row.batch_id){
					return selectedBatchData[i].text;
						}
					}
				}
			},
			{
				title: "Total fee",data:"batch_fees"
			},
			{
				title: "Discount",data:null,render:function(){return "<input type='text' class='form-control discount'/>"}
			},
			{
				title: "%/&#x20b9;",data:null,render:function(){return "<input type='checkbox' data-size=\"mini\"/ class='percentage'>"},width:'auto',bSortable: false
			},
			{
				title: "Paid fee",data:null,render:function(){return "<input type='text' class='form-control paidFees'/>"}
			},
			{
				title: "Remaining fee",data:null,render:function(){return "<div class='remaingFee' style='text-align:center;'></div>"}
			}]
		});
		$("#dataTableForFees input[type=\"checkbox\"]").bootstrapSwitch(optionSelect);
		$('.dataTables_filter').find('input').attr("placeholder", "Search Fees");
}

var addStudent = function(){
	if(wayOfAddition=="byStudentID"){
		$(".error").empty();
		var flag=false;
		var regStringExpr = /^[a-zA-Z]+$/;
		var regPhoneNumber = /^[0-9]+$/;
		var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		var divisionId = $('#division').val();
		var batchIDs = $("#batches").val();
		var parentFname=$(".parentInfo").find("#fname").val().trim();
		var parentLname=$(".parentInfo").find("#lname").val().trim();
		var parentPhone=$(".parentInfo").find("#phone").val().trim();
		var parentEmail=$(".parentInfo").find("#email").val().trim();
		var joiningDate = $(".studentInfobyID").find("#joiningDate").val().trim();
		if($("#classfloorID").val() === ""){
			flag=true;
			$("#classfloorIDerror").html("Enter Classfloor ID");
		}
		if(divisionId=="-1"){
			flag=true;
			$("#divisionError").html("Please select class");
		}
		if(batchIDs=="" || batchIDs==null){
			flag=true;
			$("#batchError").html("Please select batch");
		}else{
			batchIDs = batchIDs.join(',')
		}
		
		if(parentFname==""){
			flag=true;
			$("#parentFnameError").html("Name Cannot be blank");
		}else if(!parentFname.match(regStringExpr)){
			flag=true;
			$("#parentFnameError").html("Only alphabets allowded!");
		}
		if(parentLname==""){
			flag=true;
			$("#parentLnameError").html("Name Cannot be blank");
		}else if(!parentLname.match(regStringExpr)){
			flag=true;
			$("#parentLnameError").html("Only alphabets allowded!");
		}
		
		if(parentPhone!=""){
			if(!parentPhone.match(regPhoneNumber)){
				flag=true;
				$(".parentInfo").find("#phoneError").html("Only numbers allowded!");
			}
		}
		
		if(parentEmail!=""){
			if(!filter.test(parentEmail)){
				flag=true;
				$(".parentInfo").find("#emailError").html("Invalid Email ID!");
			}
		}
		
		if( $("#studentIdById").val().trim() == ""){
			flag=true;
			$("#studentIdByIdError").html("Enter Registration no");
		}
		
		if(flag == false){
			var student = new Student();
			student.parentFname = parentFname;
			student.parentLname = parentLname;
			student.parentPhone = parentPhone;
			student.parentEmail = parentEmail;
			student.student_id = studentID;
			student.div_id = divisionId;
			student.batch_id = batchIDs;
			student.joiningDate = joiningDate.split("-").reverse().join("-");;
			student.studentInstRegNo = $("#studentIdById").val();
			var tRow = $('#dataTableForFees tbody').find('tr');
			var tData = feesDataTable.rows().data();
			var feesArray = [];
			if(tData && tData.length){
				for(var index=0;index<tData.length;index++){
					var student_Fees = new Student_Fees();
					student_Fees.student_id = studentID;
					student_Fees.div_id = (tData[index]).div_id
					student_Fees.batch_id = (tData[index]).batch_id;
					student_Fees.batch_fees = (tData[index]).batch_fees;
					student_Fees.discount = tRow.eq(index).find(".discount").val();
					student_Fees.discount_type  = tRow.eq(index).find(".percentage").is(':checked')?'per':'amt';
					student_Fees.fees_paid  = tRow.eq(index).find(".paidFees").val();
					feesArray.push(student_Fees);
				}	
			}
			var additionalValData = {};
			$(ADDITIONAL_FORM_FIELD_TO_SAVE).find("input").each(function(key,val){
				var fieldValue = $(val).val();
				var fieldId = $(val).attr("fieldId");
				additionalValData[fieldId] = fieldValue;
			});
			student.studentAdditionalInfo = JSON.stringify(additionalValData);
			var studentRegisterServiceBean = new StudentRegisterServiceBean ();
			studentRegisterServiceBean.student = student;
			studentRegisterServiceBean.student_FeesList = feesArray;
			var handler = {};
			handler.success = function(e){
			if(e == true){
			$.notify({message: 'Student Added successfully'},{type: 'success'});
			}else{
				if(e==false){
				$.notify({message: "Sufficient student ID's not available"},{type: 'danger'});
				}else if(e[0] == "regno"){
					$("#studentIdByIdError").html("Duplicate Registration no");		
				}
			}};
			handler.error = function(e){console.log(e)};
			studentRegisterServiceBean.studentClassId = $("#studentIdById").val();
			studentRegisterServiceBean.additionalFormFields = additionalValData;
			
			rest.post(addStudentByID,handler,JSON.stringify(studentRegisterServiceBean));
		}
	}else{
	$(".error").empty();
		$("#addphoneError").hide();
		$("#addemailError").hide();
		var regStringExpr = /^[a-zA-Z]+$/;
		var regPhoneNumber = /^[0-9]+$/;
		var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		var flag=false;
		var divisionId = $('#division').val();
		var batchIDs = $("#batches").val();
		var parentFname=$(".parentInfo").find("#fname").val().trim();
		var parentLname=$(".parentInfo").find("#lname").val().trim();
		var parentPhone=$(".parentInfo").find("#phone").val().trim();
		var parentEmail=$(".parentInfo").find("#email").val().trim();
		
		var studentFname=$(".studentInfoManually").find("#fname").val().trim();
		var studentLname=$(".studentInfoManually").find("#lname").val().trim();
		var studentPhone=$(".studentInfoManually").find("#phone").val().trim();
		var studentEmail=$(".studentInfoManually").find("#email").val().trim();
		var dob = $(".studentInfoManually").find("#dobfield").val();
		var address = $(".studentInfoManually").find("#address").val().trim();
		var state = $(".studentInfoManually").find("#state").val();
		var city = $(".studentInfoManually").find("#city").val().trim();
		var joiningDate = $(".studentInfoManually").find("#joiningDate").val().trim();
		if(divisionId=="-1"){
			flag=true;
			$("#divisionError").html("Please select class");
		}
		if(batchIDs=="" || batchIDs==null){
			flag=true;
			$("#batchError").html("Please select batch");
		}else{
			batchIDs = batchIDs.join(',')
		}
		if(parentFname==""){
			flag=true;
			$("#parentFnameError").html("Name Cannot be blank");
		}else if(!parentFname.match(regStringExpr)){
			flag=true;
			$("#parentFnameError").html("Only alphabets allowded!");
		}
		if(parentLname==""){
			flag=true;
			$("#parentLnameError").html("Name Cannot be blank");
		}else if(!parentLname.match(regStringExpr)){
			flag=true;
			$("#parentLnameError").html("Only alphabets allowded!");
		}
		
		if(studentFname==""){
			flag=true;
			$("#studentFnameError").html("Name Cannot be blank");
		}else if(!studentFname.match(regStringExpr)){
			flag=true;
			$("#studentFnameError").html("Only alphabets allowded!");
		}
		
		if(studentLname==""){
			flag=true;
			$("#studentLnameError").html("Name Cannot be blank");
		}else if(!studentLname.match(regStringExpr)){
			flag=true;
			$("#studentLnameError").html("Only alphabets allowded!");
		}
		
		if(city==""){
			flag=true;
			$("#cityError").html("Name Cannot be blank");
		}else if(!parentFname.match(regStringExpr)){
			flag=true;
			$("#cityError").html("Only alphabets allowded!");
		}
		
		if(studentPhone!=""){
			if(!studentPhone.match(regPhoneNumber)){
				flag=true;
				$(".studentInfoManually").find("#phoneError").html("Only numbers allowded!");
			}
		}
		
		if(parentPhone!=""){
			if(!parentPhone.match(regPhoneNumber)){
				flag=true;
				$(".parentInfo").find("#phoneError").html("Only numbers allowded!");
			}
		}
		
		if(parentEmail!=""){
			if(!filter.test(parentEmail)){
				flag=true;
				$(".parentInfo").find("#emailError").html("Invalid Email ID!");
			}
		}
		
		if(studentEmail!=""){
			if(!filter.test(studentEmail)){
				flag=true;
				$(".studentInfoManually").find("#emailError").html("Invalid Email ID!");
			}
		}
		
		if(dob==""){
			$("#dobError").html("Select Date of birth!");
			flag=true;
		}
		
		if(address==""){
			$("#addressError").html("Enter address!");
			flag=true;
		}
		
		if(state=="-1"){
			$("#stateError").html("Select State!");
			flag=true;
		}
		
		if(parentPhone == "" && studentPhone == ""){
			$("#addphoneError").show();
			flag=true;
		}
		
		if(parentEmail == "" && studentEmail == ""){
			$("#addemailError").show();
			flag=true;
		}
		if( $("#studentIdByManual").val().trim() == ""){
			flag=true;
			$("#studentIdByManualError").html("Enter Registration no");
		}
		if(flag == false){
		var registerBean = new RegisterBean();
		registerBean.fname = studentFname;
		registerBean.lname = studentLname;
		registerBean.phone1 = studentPhone;
		registerBean.email = studentEmail;
		registerBean.dob = dob;
		registerBean.addr1 = address;
		registerBean.city = city;
		registerBean.state = state;
		
		var student = new Student();
		student.parentFname = parentFname;
		student.parentLname = parentLname;
		student.parentPhone = parentPhone;
		student.parentEmail = parentEmail;
		student.fname = studentFname;
		student.lname = studentLname;
		student.phone = studentPhone;
		student.email = studentEmail;
		student.dob = dob.split("-").reverse().join("-");
		student.addr = address;
		student.city = city;
		student.state = state;
		student.studentInstRegNo = $("#studentIdByManual").val();
		student.joiningDate = joiningDate.split("-").reverse().join("-");;
		var tRow = $('#dataTableForFees tbody').find('tr');
		var tData = feesDataTable.rows().data();
		var feesArray = [];
		if(tData && tData.length){
			for(var index=0;index<tData.length;index++){
				var student_Fees = new Student_Fees();
				student_Fees.student_id = studentID;
				student_Fees.div_id = (tData[index]).div_id
				student_Fees.batch_id = (tData[index]).batch_id;
				student_Fees.batch_fees = (tData[index]).batch_fees;
				student_Fees.discount = tRow.eq(index).find(".discount").val();
				student_Fees.discount_type  = tRow.eq(index).find(".percentage").is(':checked')?'per':'amt';
				student_Fees.fees_paid  = tRow.eq(index).find(".paidFees").val();
				feesArray.push(student_Fees);
			}	
		}
		var additionalValData = {};
		$(ADDITIONAL_FORM_FIELD_TO_SAVE).find("input").each(function(key,val){
			var fieldValue = $(val).val();
			var fieldId = $(val).attr("fieldId");
			additionalValData[fieldId] = fieldValue;
		});
		student.studentAdditionalInfo = JSON.stringify(additionalValData);
		
		var studentRegisterServiceBean = new StudentRegisterServiceBean ();
		studentRegisterServiceBean.registerBean = registerBean;
		studentRegisterServiceBean.student = student;
		studentRegisterServiceBean.student_FeesList = feesArray;
		var handler = {};
		handler.success = function(e){
			if(e[0] == ""){
			$.notify({message: 'Student Added successfully'},{type: 'success'});
			}else if(e[0] == "ID"){
				$.notify({message: "Sufficient student ID's not available"},{type: 'danger'});	
			}else if(e.length > 0){
				for(i=0;i<e.length;i++){
					if(e[i] == "email"){
					$(".studentInfoManually").find("#emailError").html("Email ID already used,please enter different email!");
					}
					if(e[i] == "regno"){
						$("#studentIdByManualError").html("Duplicate Registration no");	
					}
				}
			}
			
		};
		studentRegisterServiceBean.additionalFormFields = additionalValData;
		studentRegisterServiceBean.studentClassId = $("#studentIdByManual").val();
		handler.error = function(e){console.log(e)};
		rest.post(addStudentManuallyUrl+"/"+divisionId+"/"+batchIDs,handler,JSON.stringify(studentRegisterServiceBean));
		}
	}
		
}
/*Save bean*/
function RegisterBean(){
	this.fname;
	this.lname;
	this.mname = "middlename";
	this.phone1;
	this.email;
	this.dob;
	this.addr1;
	this.city;
	this.state;	
}

function Student(){
	this.parentFname;
	this.parentLname;
	this.parentPhone;
	this.parentEmail;
}

function Student_Fees(){
	this.div_id;
	this.batch_id;
	this.batch_fees;
	
	this.student_id;
	this.discount;
	this.discount_type;
	this.final_fees_amt;
	this.fees_paid;
	this.fees_due;
}

function StudentRegisterServiceBean(){
	this.registerBean;
	this.student;
	this.student_FeesList = [];
	this.additionalFormFields;
	this.studentClassId;
}