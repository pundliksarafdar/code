<%@page import="com.classapp.db.batch.division.Division"%>
<meta http-equiv="cache-control" content="max-age=0" />
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<style>
#dataTableForFees_filter{
display: none;
}
.scrollable-menu {
    height: auto;
    max-height: 200px;
    overflow-x: hidden;
}

.error{
color: red;
}

.discount .selectToggle:not(.active){
	    border-right: 5px solid red;
}

.discount .selectToggle.active{
	    border-left: 5px solid red;
}

.discount .selectToggle .percentage{
	display:block;
}

.discount .selectToggle .amount{
	display:none;
}

.discount .selectToggle.active .percentage{
	display:none;
}

.discount .selectToggle.active .amount{
	display:block;
}
</style>
<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.STUDENT_LIST); %>
<script>
var optionSelect = {
	onText:"%",
	offText:"&#x20b9;"
}

var studentID="";
var wayOfAddition="";
	$(document).ready(function(){
		$("body").on("switchChange.bootstrapSwitch input","#dataTableForFees input",calculateFee);
		$("#statebtn").parents(".btn-group").find("li").on("mouseup",function(){
			$("#statebtn").parents(".form-group").find('.danger').remove();
			$('#statebtn').html($(this).text()+'&nbsp;<span class="caret"></span>');
			$('#state').val($(this).text());
			$("#statebtn").focus();
		});
		
		$('#datetimepicker').datetimepicker({
			format : 'DD-MM-YYYY',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		});
		
		
		$('#joiningDatePicker').datetimepicker({
			format : 'DD-MM-YYYY',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		}).data("DateTimePicker").setDate(new Date());
		$('#manualljoiningDatePicker').datetimepicker({
			format : 'DD-MM-YYYY',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		}).data("DateTimePicker").setDate(new Date());
		
		$("#batches").select2({data:'',placeholder:"type batch name"});
		
		$("#division").change(function(){
			getBatchesOfDivision();
		});
		/* $("#batches").change(function(){
						var divId=$("#division").val();
						var batch = $("#batches").val();
						if(divId!="-1" && batch!="-1" && batch!="" && batch!=null && batch!="Select Batch"){
						 	$("#uploadStudentExcelBtn").removeAttr('disabled');
						 	$("#uploadStudentExcelBtn").empty();
						}else{
							$("#uploadStudentExcelBtn").prop("disabled",true);
			 			}
			 		}); */
		$("#classfloorIDAddition").click(function(){
			wayOfAddition="byStudentID";
			$("#addsuccess").hide();
			$(".studentform").show();
			$(".classfloorID").show();
			$(".studentInfobyID").show();
			$(".studentInfoManually").hide();
			/* $("#errorMSGDiv").hide();
			$("#errorMSGDiv").empty();
			$("#countDiv").hide(); */
			
		});
		
		$("#manualAddition").click(function(){
			wayOfAddition="manual";
			$("#addsuccess").hide();
			$(".studentform").show();
			$(".classfloorID").hide();
			$(".studentInfoManually").show();
			$(".studentInfobyID").hide();
			/* $("#errorMSGDiv").hide();
			$("#errorMSGDiv").empty();
			$("#countDiv").hide(); */
		});
		
		$("#classfloorID").change(function(){
			$("#addsuccess").hide();
			$("#classfloorIDerror").empty();
			studentID="";
			$(".studentInfobyID").find("#fname").empty();
	    	$(".studentInfobyID").find("#lname").empty();
	    	$(".studentInfobyID").find("#email").empty();
	    	$(".studentInfobyID").find("#phone").empty();
			var classfloorID = $("#classfloorID").val();
			/* $.ajax({
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getStudentByLoginID",
				    	 classfloorID:classfloorID					 
				   		},
				   type:"POST",
				   success:function(e){
					    var data = JSON.parse(e);
					    var status=data.status;
					    if(status=="valid"){
					    	$(".studentInfobyID").find("#fname").html(data.firstname);
					    	$(".studentInfobyID").find("#lname").html(data.lastname);
					    	$(".studentInfobyID").find("#email").html(data.email);
					    	$(".studentInfobyID").find("#phone").html(data.phone);
					    	studentID=data.regID;
					    }else if(status=="invalid"){
					    	$("#classfloorIDerror").html("Invalid ID");
					    }else{
					    	$("#classfloorIDerror").html("Student already exists in institute!!");
					    }
					  
				   	},
				   error:function(e){
					   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
						$('div#addStudentModal .error').show();
				   }
				   
			}); */
			
			 var handlers = {};
				handlers.success = function(data){
					if(data != false ){
						if(data.regId == -1){
							$("#classfloorIDerror").html("Invalid ID");
						}else{	
						$(".studentInfobyID").find("#fname").html(data.fname);
				    	$(".studentInfobyID").find("#lname").html(data.lname);
				    	$(".studentInfobyID").find("#email").html(data.email);
				    	$(".studentInfobyID").find("#phone").html(data.phone1);
				    	studentID=data.regId;
						}
					}else{
						$("#classfloorIDerror").html("Student already exists in institute!!");
					}
				}
				handlers.error = function(e){}
				rest.get("rest/customuserservice/getStudentByLoginID/"+classfloorID,handlers);
		});
		/*
		$("#addStudent").click(function(){
			$("#addsuccess").hide();
			if(wayOfAddition=="byStudentID"){
				addStudentByID();
			}else if(wayOfAddition=="manual"){
				addStudentManually();
			}
		});
		*/
		/* $("#uploadStudentExcelBtn").on("click",function(e){
			$("#addsuccess").hide();
			$(".studentform").hide();
			$(".classfloorID").hide();
			$(".studentInfoManually").hide();
			$(".studentInfobyID").hide();
			$("#countDiv").empty();
			$('#errorMSGDiv').empty();
			$('#errorMSGDiv').show();
			$("#countDiv").show();
			var handler = {};
			handler.success = function(e){				
				var uri = "rest/classownerservice/addExcelFile/"+e.fileid;
				var handlers = {};
				handlers.success = function(e){
					var divisionId = $('#division').val();
					
					var batchIDs = $("#batches").val();
					
					var StudentExcelUploadBean= {};
					StudentExcelUploadBean.divId=divisionId;
					StudentExcelUploadBean.batchId=batchIDs;
					StudentExcelUploadBean.fileName=e.fileid;
					
					var studentExcelUploadBean = JSON.stringify(StudentExcelUploadBean);
					
					var handlersSuccess = {};
					handlersSuccess.success = function(successResp){
						$("#countDiv").append(successResp.SUCCESS[0]);
						var errorResponse=successResp.ERROR;												
						if(errorResponse!=null && !errorResponse==""){
							var content="";
							for(var i=0; i<errorResponse.length; i++){
								content=content+"<tr>";
								var errorMessages=errorResponse[i].split("#");																
								content=content+"<td>"+errorMessages[0]+"</td><td>";
									for(var j=1;j<errorMessages.length;j++){										
										content=content+errorMessages[j]+"<br>";									
									}
								content="</td>"+content+"</tr>";
							}							
							var table='<table class="table"><thead><tr><th>Row number</th><th>Messages</th></tr></thead><tbody>'+content+'</tbody></table>';
							$("#errorMSGDiv").append(table);
							$($("#errorMSGDiv").find("table")).DataTable({
								paging : false,
								scrollY:"200px"
							});
						}
						
					}
					rest.post("rest/files/upload/student/xls/", handlersSuccess,
							studentExcelUploadBean, false);
					
					console.log("Success",e);
					}
				handlers.error = function(e){console.log("Error",e)}
				rest.post(uri,handlers);
			}
			handler.error = function(){};
			
			var submitDataFile = $(".excelUpload")[0];
			var file=document.getElementById("excelUploadBrowseID").value;
			var flagUpload=true;
			if(file==""){				
				$("#browseExcelErrorSpan").html("Please select the file!");
				flagUpload=false;
			}else{
				$("#browseExcelErrorSpan").html("");
				flagUpload=true;
			}
			if(flagUpload==true){
				rest.uploadExcelFile(submitDataFile ,handler,false);
			}
						
		}); */
	});
	
	function calculateFee(){
		var tableRow = $(this).closest('tr');
		var data = feesDataTable.row(tableRow).data();
		var discount = tableRow.find('.discount').val();
		var paidFees = tableRow.find('.paidFees').val();
		var totalFees = data.batch_fees;
		var percentage = tableRow.find('[type="checkbox"]').is(':checked');
		remainingFee = percentage?(totalFees - (totalFees*discount*0.01) - paidFees):(totalFees-discount - paidFees);
		console.log(discount,paidFees,totalFees,percentage,remainingFee);
		tableRow.find('.remaingFee').html(remainingFee+' &#x20b9;');
	}
	/* function addStudentByID(){
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
		$.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addStudentByID",
					 studentID:studentID,
					 divisionId:divisionId,
					 batchIDs:batchIDs,
					 parentFname:parentFname,
					 parentLname:parentLname,
					 parentPhone:parentPhone,
					 parentEmail:parentEmail
			   		},
			   type:"POST",
			   success:function(e){
				   $("#addsuccess").show();
				   $(".studentform").find("input").val("");
			   },
			   error(e){
				   }
			   });
		}
		
	} */
	
	function addStudentManually(){
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
		
		if(flag==false){
		/* $.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addStudentByManually",
					 divisionId:divisionId,
					 batchIDs:batchIDs,
					 parentFname:parentFname,
					 parentLname:parentLname,
					 parentPhone:parentPhone,
					 parentEmail:parentEmail,
					 studentFname:studentFname,
					 studentLname:studentLname,
					 studentPhone:studentPhone,
					 studentEmail:studentEmail,
					 dob:dob,
					 address:address,
					 state:state,
					 city:city
			   		},
			   type:"POST",
			   success:function(e){
				   $("#addsuccess").show();
				   $(".studentform").find("input").val("");
				   $("#statebtn").parents(".form-group").find('.danger').remove();
					$('#statebtn').html('State&nbsp;<span class="caret"></span>');
					$('#state').val("");
			   },
			   error(e){
				   }
			   }); */
		}
	}
	
	function getBatchesOfDivision(){
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = $('#division').val();

		if(!divisionId || divisionId.trim()=="" || divisionId == -1){
			$("#batches").empty();
			 $("#batches").select2().val("").change();
			 $("#batches").select2({data:"",placeholder:"Select Batch"});
		}else{
		  var handlers = {};
			handlers.success = function(data){
				 var batchDataArray = [];
				$('#batches').empty();
				if(data.length > 0){
				 $.each(data,function(key,val){
						var data = {};
						data.id = val.batch_id;
						data.text = val.batch_name;
						batchDataArray.push(data);
					});
				    $("#batches").select2({data:batchDataArray,placeholder:"type batch name"});
				}else{
			    	 $("#batches").select2({data:null,placeholder:"Batch not found"});
			    }
			}
			handlers.error = function(e){}
			rest.get("rest/customuserservice/getBatches/"+divisionId,handlers);
		
	}
	}

</script>

<body>
<jsp:include page="ManageStudentHeader.jsp" >
		<jsp:param value="active" name="customUserManageStudent"/>
	</jsp:include>

<div id="addstudenttab">
<div class="well">
<div class="row">
<div class="col-md-3">
	<select id="division" class="form-control">
		<option value="-1">Select Class</option>
		<c:forEach items="${divisions}" var="division">
			<option value=<c:out value='${division.divId }'></c:out>><c:out value="${division.divisionName }"></c:out> <c:out value="${division.stream }"></c:out></option>
		</c:forEach>
	</select>
	<span class="error" id="divisionError"></span>
</div>
<div class="col-md-3">
<select id="batches" multiple="multiple" style="width:100%">
	<option>Select Batch</option>
</select>
<span class="error" id="batchError"></span>
</div>
<div class="col-md-2">
		<button id="classfloorIDAddition" class="btn btn-primary btn-sm">Add Student By ClassFloor ID</button>
	</div>
	<div class="col-md-1" align="center" style="padding-left: 3%">
		<button class="btn btn-primary" style="border-radius:55%;background: grey;border: grey" disabled="disabled">OR</button>
	</div>
	<div class="col-md-2">
		<button id="manualAddition" class="btn btn-primary btn-sm">Add Student Manually</button>
	</div>
</div>
</div>



<div class="row">
<div id="errorMSGDiv"></div>
</div>

<div class="well studentform" style="padding-top: 2%;border: 2px solid;border-color:  rgb(5, 116, 148);margin-top: 1%;display: none;">
<div class="row">
<div class="col-md-4 alert alert-success" style="padding: 0px;margin-left: 5%;display: none;" id="addsuccess"><span>Student Added Successfully!!</span></div>
<div class="col-md-4 alert alert-danger" style="padding: 0px;margin-left: 5%;display: none;" id="addphoneError"><span>Please enter atleast one phone no!!</span></div>
<div class="col-md-4 alert alert-danger" style="padding: 0px;margin-left: 5%;display: none;" id="addemailError"><span>Please enter atleast one email!!</span></div>
</div>
<div class="row classfloorID" style="padding-left: 2%">
<div class="col-md-2"><strong align="left">Class-Floor ID</strong></div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="classfloorID" class="form-control"><span id="classfloorIDerror" style="color: red"></span></div>
</div>

<div class="studentInfobyID" style="padding-left: 2%">
<div class="row" style="background: rgba(5, 116, 148, 0.45);padding-top: 0.3%;">
<div class="col-md-2" ><strong align="left">Student Information</strong></div>
</div>

<div class="row" style="padding-top: 1%">
<div class="col-md-2">First Name</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><span id="fname"></span></div>
<div class="col-md-2">Last Name</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><span id="lname"></span></div>
</div>

<div class="row">
<div class="col-md-2">Phone No</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><span id="phone"></span></div>
<div class="col-md-2">Email ID</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><span id="email"></span></div>
</div>

<div class="row" style="padding-top: 1%">
	<div class="col-md-2">Registration number</div>
	<div class="col-md-1">:</div>
	<div class="col-md-3"><input type="text" id="studentIdById" class="form-control" maxlength="20"><span id="studentIdByIdError" class="error"></span></div>
	<div class="col-md-2">Institute Joining Date</div>
	<div class="col-md-1">:</div>
	<div class="col-md-3"><div id="joiningDatePicker" class="input-group" style="width :270px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="joiningDate" name="registerBean.dob" required="required"  readonly /> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div></div>
</div>
</div>

<div class="studentInfoManually" style="padding-left: 2%">
<div class="row" style="background: rgba(5, 116, 148, 0.45);padding-top: 0.3%;">
<div class="col-md-2"><strong align="left">Student Information</strong></div>
</div>
<div class="row" style="padding-top: 1%">
<div class="col-md-2">First Name</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="fname" class="form-control" maxlength="20"><span id="studentFnameError" class="error"></span></div>
<div class="col-md-2">Last Name</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="lname" class="form-control" maxlength="20"><span id="studentLnameError" class="error"></span></div>
</div>

<div class="row">
<div class="col-md-2">Phone No</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="phone" class="form-control" maxlength="10"><span id="phoneError" class="error"></span></div>
<div class="col-md-2">Email ID</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="email" class="form-control" maxlength="50"><span id="emailError" class="error"></span></div>
</div>

<div class="row">
<div class="col-md-2">Date Of Birth</div>
<div class="col-md-1">:</div>
<div class="col-sm-3" align="left">	
					<div id="datetimepicker" class="input-group" style="width :270px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="dobfield" name="registerBean.dob" required="required"  readonly /> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
<span id="dobError" class="error"></span>					
</div>
<div class="col-md-2">Address</div>
<div class="col-md-1">:</div>
<div class="col-sm-3">
<input type="text" class="form-control" name="registerBean.addr1"  id="address" required="required" maxlength="50"/>
<span id="addressError" class="error"></span>
</div>
</div>

<div class="row">
<div class="col-md-2">city</div>
<div class="col-md-1">:</div>
<div class="col-sm-3">
<input type="text" class="form-control" name="registerBean.city" id="city" required="required" maxlength="20" />
<span id="cityError" class="error"></span>
</div>
<div class="col-md-2">State</div>
<div class="col-md-1">:</div>
<div class="col-sm-3" align="left">
<input type="hidden" class="form-control" name="registerBean.state" id="state" required="required" value="-1"'/>
			<div class="btn-group">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" id="statebtn">
						State <span class="caret"></span>
					</button>
					<ul class="dropdown-menu scrollable-menu" role="menu" >
						<li value="Andhra Pradesh"><a href="javascript:void(0)">Andhra Pradesh</a></li>
						<li value="Arunachal Pradesh"><a href="javascript:void(0)">Arunachal Pradesh</a><li>
						<li value="Assam"><a href="javascript:void(0)">Assam</a></li>
						<li value="Bihar"><a href="javascript:void(0)">Bihar</a></li>
						<li value="Chhattisgarh"><a href="javascript:void(0)">Chhattisgarh</a></li>
						<li value="Goa"><a href="javascript:void(0)">Goa</a></li>
						<li value="Gujarat"><a href="javascript:void(0)">Gujarat</a></li>
						<li value="Haryana"><a href="javascript:void(0)">Haryana</a></li>
						<li value="Himachal Pradesh"><a href="javascript:void(0)">Himachal Pradesh</a></li>
						<li value="Jammu and Kashmir"><a href="javascript:void(0)">Jammu and Kashmir</a></li>
						<li value="Jharkhand"><a href="javascript:void(0)">Jharkhand</a></li>
						<li value="Karnataka"><a href="javascript:void(0)">Karnataka</a></li>
						<li value="Kerala"><a href="javascript:void(0)">Kerala</a></li>
						<li><a href="javascript:void(0)">Madhya Pradesh</a></li>
						<li><a href="javascript:void(0)">Maharashtra</a></li>
						<li><a href="javascript:void(0)">Manipur</a></li>
						<li><a href="javascript:void(0)">Meghalaya</a></li>
						<li><a href="javascript:void(0)">Mizoram</a></li>
						<li><a href="javascript:void(0)">Nagaland</a></li>
						<li><a href="javascript:void(0)">Orissa</a></li>
						<li><a href="javascript:void(0)">Punjab</a></li>
						<li><a href="javascript:void(0)">Rajasthan</a></li>
						<li><a href="javascript:void(0)">Sikkim</a></li>
						<li><a href="javascript:void(0)">Tamil Nadu</a></li>
						<li><a href="javascript:void(0)">Telangana</a></li>
						<li><a href="javascript:void(0)">Tripura</a></li>
						<li><a href="javascript:void(0)">Uttar Pradesh</a></li>
						<li><a href="javascript:void(0)">Uttarakhand</a></li>
						<li><a href="javascript:void(0)">West Bengal</a></li>
					</ul>
			</div>
			<span id="stateError" class="error"></span>
			</div>
			</div>
	<div class="row" style="padding-top: 1%">
		<div class="col-md-2">Registration number</div>
		<div class="col-md-1">:</div>
		<div class="col-md-3"><input type="text" id="studentIdByManual" class="form-control" maxlength="20"><span id="studentIdByManualError" class="error"></span></div>
		<div class="col-md-2">Institute Joining Date</div>
		<div class="col-md-1">:</div>
		<div class="col-md-3"><div id="manualljoiningDatePicker" class="input-group" style="width :270px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="joiningDate" name="registerBean.dob" required="required"  readonly /> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div></div>
	</div>
	<div class="row" style="padding-top: 1%">
		<div class="col-md-2">Gender</div>
		<div class="col-md-1">:</div>
		<div class="col-md-3">
		<select id="gender" class="form-control" style="width:100%">
			<option value="-1">Select Gender</option>
			<option value="M">Male</option>
			<option value="F">Female</option>
		</select>
		<span id="genderError" class="error"></span>
		</div>
	</div>
</div>


<div class="parentInfo" style="padding-left: 2%">
<div class="row" style="background: rgba(5, 116, 148, 0.45);padding-top: 0.3%;">
<div class="col-md-2"><strong align="left">Parents Information</strong></div>
</div>
<div class="row" style="padding-top: 1%">
<div class="col-md-2">First Name</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="fname" class="form-control" maxlength="20"><span id="parentFnameError" class="error"></span></div>
<div class="col-md-2">Last Name</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="lname" class="form-control" maxlength="20"><span id="parentLnameError" class="error"></span></div>
</div>

<div class="row">
<div class="col-md-2">Phone No</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="phone" class="form-control" maxlength="10"><span id="phoneError" class="error"></span></div>
<div class="col-md-2">Email ID</div>
<div class="col-md-1">:</div>
<div class="col-md-3"><input type="text" id="email" class="form-control" maxlength="50"><span id="emailError" class="error"></span></div>
</div>
</div>
<div style="padding-left: 2%">
<div class="row" style="background: rgba(5, 116, 148, 0.45);padding-top: 0.3%;">
<div class="col-md-2"><strong align="left">Addition Form Field</strong></div>
</div>
<div style="padding-top: 1%" id="additionalFormFieldsToSave">
</div>
</div>
<div style="padding-left: 2%">
<div class="row" style="background: rgba(5, 116, 148, 0.45);padding-top: 0.3%;">
<div class="col-md-2"><strong align="left">Fees Details</strong></div>
</div>
<div style="padding-top: 1%">
	<table id="dataTableForFees" class="table table-striped" style="width:80%"></table>
</div>
</div>
<div class="row" style="padding: 2%">
<div class="col-md-offset-6 col-md-1"><button class="btn btn-primary" id="addStudent">Submit</button></div>
</div>


</div>

<div id="additionalFormFields" class="hide">
<div class="row">
	<div class="col-md-2" id="formFieldLabel">Email ID</div>
	<div class="col-md-1">:</div>
	<div class="col-md-3"><input id="formFieldValue" type="text" class="form-control"><span id="emailError" class="error"></span></div>
</div>
	
</div>
</body>
</html>