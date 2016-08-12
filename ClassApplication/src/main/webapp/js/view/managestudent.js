var BATCH_SELECT = "#batches"; 
var DIV_SELECT = "#division";
var ADD_STUDENT = "#addStudent";
var selectedBatchData = "";
var getBatchFeesUrl = "rest/feesservice/getBatchFees/";
var addStudentManuallyUrl = 'rest/classownerservice/addStudentByManually';
var addStudentByID = 'rest/classownerservice/addStudentByID';
$(document).ready(function(){
	$("body").on("change",BATCH_SELECT,selectBacth)
		.on("click",ADD_STUDENT,addStudent);
	showTable([]);
});

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
	console.log(data);
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
		
		if(flag == false){
			var student = new Student();
			student.parentFname = parentFname;
			student.parentLname = parentLname;
			student.parentPhone = parentPhone;
			student.parentEmail = parentEmail;
			student.student_id = studentID;
			student.div_id = divisionId;
			student.batch_id = batchIDs;
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
			
			var studentRegisterServiceBean = new StudentRegisterServiceBean ();
			studentRegisterServiceBean.student = student;
			studentRegisterServiceBean.student_FeesList = feesArray;
			var handler = {};
			handler.success = function(e){if(e == true){
			$.notify({message: 'Student Added successfully'},{type: 'success'});
			}else{
				$.notify({message: "Sufficient student ID's not available"},{type: 'danger'});	
			}};
			handler.error = function(e){console.log(e)};
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
		
		var studentRegisterServiceBean = new StudentRegisterServiceBean ();
		studentRegisterServiceBean.registerBean = registerBean;
		studentRegisterServiceBean.student = student;
		studentRegisterServiceBean.student_FeesList = feesArray;
		console.log(studentRegisterServiceBean);
		var handler = {};
		handler.success = function(e){
			if(e == ""){
			$.notify({message: 'Student Added successfully'},{type: 'success'});
			}else if(e == "ID"){
				$.notify({message: "Sufficient student ID's not available"},{type: 'danger'});	
			}else if(e == "email"){
				$(".studentInfoManually").find("#emailError").html("Email ID already used,please enter different email!");
			}
			
		};
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
}