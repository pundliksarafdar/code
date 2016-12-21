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
.scrollable-menu {
    height: auto;
    max-height: 200px;
    overflow-x: hidden;
}

.error{
color: red;
}



.select2-container .select2-selection--single{
height: 34px;
}

.select2-container .select2-selection--single .select2-selection__rendered{
padding-top: 2px;
}

.dataTables_filter {
     display: none;
}

#classTable .editEnabled .editable{
	display:block;
}

#classTable .editEnabled .default{
	display:none;
}

#classTable .editable{
	display:none;
}

#classTable .default{
	display:show;
}
.morris-hover{position:absolute;z-index:1000}.morris-hover.morris-default-style{border-radius:10px;padding:6px;color:#666;background:rgba(255,255,255,0.8);border:solid 2px rgba(230,230,230,0.8);font-family:sans-serif;font-size:12px;text-align:center}.morris-hover.morris-default-style .morris-hover-row-label{font-weight:bold;margin:0.25em 0}
.morris-hover.morris-default-style .morris-hover-point{white-space:nowrap;margin:0.1em 0}

</style>
 
<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.STUDENT_LIST); %>
<script>
/*!
 * jQuery Typeahead
 * Copyright (C) 2015 RunningCoder.org
 * Licensed under the MIT license
 *
 * @author Tom Bertrand
 * @version 2.3.0 (2015-12-25)
 * @link http://www.runningcoder.org/jquerytypeahead/
*/
var formFieldJSON = [];
var editStudent = {};
var generateRollNoUrl = "rest/classownerservice/setRollNumber/";
var studentId="";
var wayOfAddition="";
var globalBatchID = "";
var globalDivisionID = "";
var graphData = [];
var enabledEdit = false;
	$(document).ready(function(){
		$('#joiningDatePicker').datetimepicker({
			format : 'DD-MM-YYYY',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		});
		
		if($("#studentNameSearch").val() != ""){
			var studentName = $("#studentNameSearch").val();
			$.ajax({
				url:"classOwnerServlet",
				data:{
					methodToCall:"getStudentByName",
					studentName:studentName
				},
				type:"post",
				success:function(data){
					successCallbackclass(data,"name");
				},error:function(){
					}
			});	
		}
		$(".containerData").hide();
		$(this).on("click",".btn-batch-edit",enableEdit)
		.on("click",".btn-cancel",cancelEdit)
		.on("click",".btn-save",saveStudent)
		.on("click",".btn-batch-delete",deleteStudentPrompt)
		.on("click",".btn-student-details",studentDetails)
		.on("change","#batch",onBatchChange)
		.on("click",".generateRollNumber",generateRoll);
		var allNames = getNames();
	        $('#studentNameSearch').typeahead({
	            minLength: 1,
	            order: "asc",
	            hint: true,
	            offset : true,
	            template: "{{display}}",
	            source: allNames,
	            debug: true
	        });
		
	    
			$('#datetimepicker').datetimepicker({
			format : 'YYYY-MM-DD',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		});
		
		$("#batches").select2({data:'',placeholder:"type batch name"});
		
		$("#division").change(function(){
			getBatchesOfDivision();
		});
		
		$("#searchStudentByName").click(function(){
			var studentName = $("#studentNameSearch").val();
			$.ajax({
				url:"classOwnerServlet",
				data:{
					methodToCall:"getStudentByName",
					studentName:studentName
				},
				type:"post",
				success:function(data){
					successCallbackclass(data,"name");
				},error:function(){
					}
			});	
			
		});
		
		
		
	$(".searchStudentByBatch").click(function(){
		$("#divisionError").empty();
		$("#batchError").empty();
		var divisionID = $(".searchStudent").find("#division").val();
		var batchID = $(".searchStudent").find("#batch").val();
		var flag = false;
		if(divisionID == "-1"){
			$("#divisionError").html("Select Division!");
			flag=true;
		}
		if(batchID == "-1" || batchID == null){
			$("#batchError").html("Select Batch!");
			flag=true;
		}
		if(flag == false){
			globalBatchID = batchID;
			globalDivisionID = divisionID;
		$.ajax({
			url:"classOwnerServlet",
			data:{
				batchID:batchID,
				methodToCall:"getstudentsrelatedtobatch",
				divisionID:divisionID
			},
			type:"post",
			success:function(data){
				successCallbackclass(data,"batch");
			},error:function(){
				}
		});
		}
		});
	
		$("#back").click(function(){
			$(".studentList").show();
			$(".studentDetailsDiv").hide();
		});
		
		 $("#classTable").on("change",".selectDivision",function(){
			 if(enabledEdit == false){
			var divisionId	 = $(this).val();
			var batchDataArray = [];
			var that = $(this);
			 $.ajax({
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "fetchBatchesForDivision",
						 regId:'',
						 divisionId:divisionId						 
				   		},
				   type:"POST",
				   async:false,
				   success:function(e){
					   that.closest("tr").find(".selectBatch").empty();
					    var data = JSON.parse(e);
					    if(data.status!="error"){
					    var tempData ={};
					    tempData.id = "-1";
					    tempData.text = "Select Batch";
					    batchDataArray.push(tempData);
						var batchData = {};
					    $.each(data.batches,function(key,val){
							var data = {};
							data.id = val.batch_id;
							data.text = val.batch_name;
							batchDataArray.push(data);
							batchData[data.id] = val;
						});
					    that.closest("tr").find(".selectBatch").select2({data:batchDataArray,placeholder:"type batch name"}).data("batchData",batchData);
					    }else{
					    	that.closest("tr").find(".selectBatch").select2({data:"",placeholder:"No batch found"});
					    }
				   	},
				   error:function(e){
					   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
						$('div#addStudentModal .error').show();
				   }
				   
			});
			}
			 enabledEdit = false;
		}); 
		$('.nav-tabs a[href="#marksTab"]').on('shown.bs.tab', function () {
			
			if(graphData.length > 0){
				for (i =0; i <graphData.length;i++){
					
					if(graphData[i].length>0){
						$("#graphData"+i).empty();
						console.log(JSON.stringify(graphData[i]));
						new Morris.Line({
					        element: 'graphData'+i,
					        data:graphData[i],
					        xkey: 'sub',
					        ykeys: ['marks','avg','topper'],
					        labels: ['Marks','Average','Topper'],
					        parseTime:false,
					        ymax : 100
					      });
					}
				}
			}});
		
		$("#feesTab").on("click",".payFees",function(){
			var batch_id = $(this).prop("id");
			var serviceFees_Transaction = {};
			serviceFees_Transaction.batch_id = batch_id;
			serviceFees_Transaction.student_id = studentId;
			serviceFees_Transaction.amt_paid = $(this).closest(".amtData").find("#amt").val();
			var that = $(this);
			var handlers = {};
			handlers.success=function(){
				$.notify({message: "Fee paid successfuly"},{type: 'success'});
				var amt = that.closest(".amtData").find("#amt").val()
				var updatedfeesPaid =  parseFloat(that.closest(".feesData").find(".feesPaid").html()) + parseFloat(amt) ;
				var updatedfeesDue = that.closest(".feesData").find(".feesDue").html() - amt;
				that.closest(".feesData").find(".feesPaid").html(updatedfeesPaid);
				that.closest(".feesData").find(".feesDue").html(updatedfeesDue); 
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			
			rest.post("rest/feesservice/saveStudentBatchFeesTransaction/",handlers,JSON.stringify(serviceFees_Transaction));
		});
		
		$("#classTable").on("click",".btn-certificate-print",function(){
			$(".error").html("");
			var flag = true;
			var student_id = $($(this).closest("tr")).find("#studentId").val();
			var cert_id = $("#certificate").val();
			if(cert_id == "-1"){
				$("#certificateSelectError").html("Select certificate template");
				flag = false;
			}
			if(flag){
			var handlers = {};
			handlers.success=function(data){
				var win =window.open();
				win.document.write(data);
				win.print();
				win.close();
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			
			rest.get("rest/classownerservice/getCertificateForPrint/"+cert_id+"/"+student_id,handlers);
			}
		});
		
		$("#editStudentPersonalRecord").click(function(){
			$(".studentDetailsDiv").hide();
			$("#editPersonalDetailsDiv").show();
			$("#editPersonalDetailsStudentFName").val(editStudent.fname);
			$("#editPersonalDetailsStudentLName").val(editStudent.lname);
			$("#editPersonalDetailsStudentMobile").val(editStudent.phone);
			$("#editPersonalDetailsStudentEmail").val(editStudent.email);
			$("#editPersonalDetailsParentFName").val(editStudent.parentFname);
			$("#editPersonalDetailsParentLName").val(editStudent.parentLname);
			$("#editPersonalDetailsParentMobile").val(editStudent.parentPhone);
			$("#editPersonalDetailsParentEmail").val(editStudent.parentEmail);
			$("#editPersonalDetailsStudentDOB").val(editStudent.dob.split("-").reverse().join("-"));
			$("#editPersonalDetailsStudentAddress").val(editStudent.addr);
			$("#editPersonalDetailsStudentCity").val(editStudent.city);
			$("#editPersonalDetailsStudentState").val(editStudent.state);
			$("#editPersonalDetailsRegNo").val(editStudent.studentInstRegNo);
			$("#editPersonalDetailsJoiningDatePicker").val(editStudent.joiningDate.split("-").reverse().join("-"));
			if(editStudent.gender != null){
			$("#editPersonalDetailsGender").val(editStudent.gender).change();
			}else{
			$("#editPersonalDetailsGender").val("-1").change();	
			}
			$(".addtionaEditFields").remove();
			var additionalFieldStudentInfo = JSON.parse(editStudent.studentAdditionalInfo);
			var additionalFieldHtml = "";
			additionalFieldHtml = additionalFieldHtml + '<div class="addtionalEditFieldsDiv"><div class="row addtionaEditFields"><b>Additional Details</b></div>';
			var i =0;
			 $.each(formFieldJSON,function(key,val){
				if(i%2 == 0){
				additionalFieldHtml = additionalFieldHtml + "<div class='row addtionaEditFields'>";
				}
				additionalFieldHtml = additionalFieldHtml + "<div class='col-md-2'><b>"+val+"</b></div>";
				if(additionalFieldStudentInfo[key] != undefined){
				additionalFieldHtml = additionalFieldHtml + '<div class="col-md-3"><input type="text" fieldId="'+key+'" class="form-control" value="'+additionalFieldStudentInfo[key]+'"></div>';
				}else{
					additionalFieldHtml = additionalFieldHtml + '<div class="col-md-3"><input type="text" fieldId="'+key+'" class="form-control"></div>';	
				}
				if((i+1)%2 == 0 || i == (formFieldJSON.length-1)){
				additionalFieldHtml = additionalFieldHtml + "</div>";
				}
				i++;
			});
			 additionalFieldHtml = additionalFieldHtml + "</div>";
			$(".updateAction").before(additionalFieldHtml);
			$('#statebtn').html(editStudent.state+'&nbsp;<span class="caret"></span>');
		});
		
		$("#statebtn").parents(".btn-group").find("li").on("mouseup",function(){
			$("#statebtn").parents(".form-group").find('.danger').remove();
			$('#statebtn').html($(this).text()+'&nbsp;<span class="caret"></span>');
			$('#editPersonalDetailsStudentState').val($(this).text());
			$("#statebtn").focus();
		});
		
		$("#editPersonalDetailsEditCancel").click(function(){
			$(".studentDetailsDiv").show();
			$("#editPersonalDetailsDiv").hide();
		});
		
		$("#editPersonalDetailsSave").click(function(){
			$(".error").html("");
			var regPhoneNumber = /^[0-9]+$/;
			var regStringExpr = /^[a-zA-Z]+$/;
			var regAddressExpr = /^[a-zA-Z0-9#, ]+$/;
			var textonly=/^[a-zA-Z]+$/;
			var flag = true;
			if($("#editPersonalDetailsStudentFName").val().trim() == ""){
				$("#editPersonalDetailsStudentFNameError").html("Enter first name")
				flag = false;
			}else if(!$("#editPersonalDetailsStudentFName").val().trim().match(regStringExpr)){
				$("#editPersonalDetailsStudentFNameError").html("First Name is invalid. Only A-Z characters are allowed.")
				flag = false;
			}
			if($("#editPersonalDetailsStudentLName").val().trim() == ""){
				$("#editPersonalDetailsStudentLNameError").html("Enter last name")
				flag = false;
			}else if(!$("#editPersonalDetailsStudentLName").val().trim().match(regStringExpr)){
				$("#editPersonalDetailsStudentLNameError").html("Last Name is invalid. Only A-Z characters are allowed.")
				flag = false;
			}
			if(!$("#editPersonalDetailsStudentMobile").val().match(regPhoneNumber) || $("#editPersonalDetailsStudentMobile").val().trim().length != 10){
				$("#editPersonalDetailsStudentMobileError").html("Invalid phone no.")
				flag = false;
			}
			if($("#editPersonalDetailsStudentEmail").val().trim() != ""){
			if(!validateEmail($("#editPersonalDetailsStudentEmail").val().trim())){
				$("#editPersonalDetailsStudentEmailError").html("Invalid Email ID")
				flag = false;
			}
			}
			
			if($("#editPersonalDetailsStudentState").val()=="-1" ){
				$("#editPersonalDetailsStudentStateError").html("Select State")
				flag = false;
			}
			if($("#editPersonalDetailsStudentCity").val().trim() == ""){
				$("#editPersonalDetailsStudentCityError").html("Enter city.")	
				flag = false;
			}else if(!$("#editPersonalDetailsStudentCity").val().match(textonly)){
				$("#editPersonalDetailsStudentCityError").html("Invalid City. Only A-Z characters are allowed.")
				flag = false;
			}
			
			if($("#editPersonalDetailsParentFName").val().trim() == ""){
				$("#editPersonalDetailsParentFNameError").html("Enter first name")
				flag = false;
			}else if(!$("#editPersonalDetailsParentFName").val().trim().match(regStringExpr)){
				$("#editPersonalDetailsParentFNameError").html("First Name is invalid. Only A-Z characters are allowed.")
				flag = false;
			}
			if($("#editPersonalDetailsParentLName").val().trim() == ""){
				$("#editPersonalDetailsParentLNameError").html("Enter last name")
				flag = false;
			}else if(!$("#editPersonalDetailsParentLName").val().trim().match(regStringExpr)){
				$("#editPersonalDetailsParentLNameError").html("Last Name is invalid. Only A-Z characters are allowed.")
				flag = false;
			}
			if(!$("#editPersonalDetailsParentMobile").val().match(regPhoneNumber) || $("#editPersonalDetailsParentMobile").val().trim().length != 10){
				$("#editPersonalDetailsParentMobileError").html("Invalid phone no.")
				flag = false;
			}
			if($("#editPersonalDetailsParentEmail").val().trim() != ""){
			if(!validateEmail($("#editPersonalDetailsParentEmail").val().trim())){
				$("#editPersonalDetailsParentEmailError").html("Invalid Email ID")
				flag = false;
			}
			}
			
			if($("#editPersonalDetailsStudentAddress").val().trim() == ""){
				$("#editPersonalDetailsStudentAddressError").html("Enter address")
				flag = false;
			}else if(!$("#editPersonalDetailsStudentAddress").val().trim().match(regAddressExpr)){
				$("#editPersonalDetailsStudentAddressError").html("Invalid Address")
				flag = false;
			}
			
			if($("#editPersonalDetailsRegNo").val().trim() == ""){
				$("#editPersonalDetailsRegNoError").html("Enter Registration No")
				flag = false;
			}
			
			if($("#editPersonalDetailsGender").val() == "-1"){
				$("#editPersonalDetailsGenderError").html("Select Gender")
				flag = false;	
			}
			
			if(flag){
			var additionalValData = {};
			$(".addtionalEditFieldsDiv").find("input").each(function(key,val){
				var fieldValue = $(val).val();
				var fieldId = $(val).attr("fieldId");
				additionalValData[fieldId] = fieldValue;
			});
			editStudent.studentAdditionalInfo = JSON.stringify(additionalValData);
			editStudent.fname = $("#editPersonalDetailsStudentFName").val();
			editStudent.lname = $("#editPersonalDetailsStudentLName").val();
			editStudent.phone = $("#editPersonalDetailsStudentMobile").val();
			editStudent.email = $("#editPersonalDetailsStudentEmail").val();
			editStudent.parentFname = $("#editPersonalDetailsParentFName").val();
			editStudent.parentLname = $("#editPersonalDetailsParentLName").val();
			editStudent.parentPhone = $("#editPersonalDetailsParentMobile").val();
			editStudent.parentEmail = $("#editPersonalDetailsParentEmail").val();
			editStudent.dob = $("#editPersonalDetailsStudentDOB").val().split("-").reverse().join("-");
			editStudent.addr = $("#editPersonalDetailsStudentAddress").val();
			editStudent.city = $("#editPersonalDetailsStudentCity").val();
			editStudent.state = $("#editPersonalDetailsStudentState").val();
			editStudent.studentInstRegNo = $("#editPersonalDetailsRegNo").val();
			editStudent.joiningDate = $("#editPersonalDetailsJoiningDatePicker").val().split("-").reverse().join("-");
			editStudent.gender = $("#editPersonalDetailsGender").val();
			var handlers = {};
			handlers.success=function(data){
				if(data){
				$(".studentDetailsDiv").show();
				$("#editPersonalDetailsDiv").hide();
				$.notify({message: "Student Details update"},{type: 'success'});
				studentDetailsAfterUpdate(editStudent.student_id)
				}else{
					$("#editPersonalDetailsRegNoError").html("Duplicate Registration No")	
				}
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			console.log(editStudent)
			rest.put("rest/classownerservice/updateStudentDetails/",handlers,JSON.stringify(editStudent));
			}
		});
		
		$("#rollNoModalOK").click(function(){
			var gender = $('input[name=sortgender]:checked').val();
			var sortby = $('input[name=sortby]:checked').val();
			var handler = {};
			handler.success = function(e){
				$.notify({message: "Roll number generated"},{type: 'success'});
				var batchData = $("#batch").data("batchData")[$("#batch").val()];
				if(batchData.status == null){
					$("#batch").data("batchData")[$("#batch").val()].status = "rollGenerated=yes";
					generateButtonChange();
				}
				$(".searchStudentByBatch").click();
			};
			handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
			rest.post(generateRollNoUrl+$("#division").val()+"/"+$("#batch").val()+"/"+gender+"/"+sortby,handler);
		});
	});
	
	function validateEmail(sEmail) {
		var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		if (filter.test(sEmail)) {
		return true;
		}
		else {
		return false;
		}
		}
	
	function createAttenanceTab(data){
		$("#attendanceTab").empty();
		var monthNames = {
				1 : 'Jan',
				2 : 'Feb',
				3 : 'Mar',
				4 : 'Apr',
				5 : 'May',
				6 : 'Jun',
				7 : 'Jul',
				8 : 'Aug',
				9 : 'Sep',
				10: 'Oct',
				11: 'Nov',
				12: 'Dec'
				
		}
		var htmlString = "";
		for(i=0;i<data.batchDataList.length;i++){
			htmlString = htmlString + "<div style='text-decoration:underline;font-weight:bold'>Batch : "+data.batchDataList[i].batch_name+"</div>"
			if(data.batchDataList[i].years != null){
			for(y=0;y<data.batchDataList[i].years.length;y++){
				htmlString = htmlString + "<div>Year : "+data.batchDataList[i].years[y]+"</div>"
		 htmlString = htmlString + "<table class='table table-bordered'><thead><tr><td></td>";
			for(j=0;j<data.batchDataList[i].subjectList.length;j++){
				htmlString = htmlString + "<td>"+data.batchDataList[i].subjectList[j].subjectName+"</td>"
			}
			htmlString = htmlString + "<td>Total Lectures</td><td>Attended Lectures</td><td>Average</td></tr></thead>"
			for(monthCount = 1 ;monthCount<=12 ;monthCount++){
				htmlString = htmlString + "<tr><td>"+monthNames[monthCount]+"</td>";
				var totalLectures = 0 ;
				var attendedLectures = 0;
			for(j=0;j<data.batchDataList[i].subjectList.length;j++){
				var tempTotalLectures = 0 ;
				var tempAttendedLectures = 0;
				for(k=0;k<data.monthWiseTotalLectureList.length;k++){
					if(data.monthWiseTotalLectureList[k].batch_id == data.batchDataList[i].batch_id && 
							data.monthWiseTotalLectureList[k].sub_id == data.batchDataList[i].subjectList[j].subjectId &&
							monthCount == data.monthWiseTotalLectureList[k].month && data.monthWiseTotalLectureList[k].year == data.batchDataList[i].years[y]){
						tempTotalLectures = data.monthWiseTotalLectureList[k].count;
						break;
					}
				}
				for(k=0;k<data.monthWiseAttendanceList.length;k++){
					if(data.monthWiseAttendanceList[k].batch_id == data.batchDataList[i].batch_id && 
							data.monthWiseAttendanceList[k].sub_id == data.batchDataList[i].subjectList[j].subjectId &&
							monthCount == data.monthWiseAttendanceList[k].month && data.monthWiseAttendanceList[k].year == data.batchDataList[i].years[y]){
						if(tempTotalLectures > 0){
						htmlString = htmlString + "<td>"+parseFloat(((data.monthWiseAttendanceList[k].count/tempTotalLectures)*100).toFixed(2))+"%</td>";
						tempAttendedLectures = data.monthWiseAttendanceList[k].count;
						}else{
							htmlString = htmlString + "<td>0%</td>";
						}
						break;
					}
				}
				totalLectures = totalLectures + tempTotalLectures;
				attendedLectures = attendedLectures + tempAttendedLectures;
				}
			htmlString = htmlString + "<td>"+totalLectures+"</td>";
			htmlString = htmlString + "<td>"+attendedLectures+"</td>";
			if(totalLectures > 0){
			htmlString = htmlString + "<td>"+parseFloat(((attendedLectures/totalLectures)*100).toFixed(2))+"%</td>";
			}else{
				htmlString = htmlString + "<td>0%</td>";
			}
				htmlString = htmlString + "</tr>";
			}
			htmlString = htmlString + "</thead></table>";
		}
			}else{
				htmlString = htmlString + "<div class='well'>Attendance Data is not available</div>";
			}
		}
		
		$("#attendanceTab").append(htmlString);
	}
	
	function createFeesTab(data,batches){
		$("#feesTab").empty();
		var htmlString = ""
		for(i=0;i<batches.length;i++){
			htmlString = htmlString + "<div style='text-decoration:underline;font-weight:bold'>Batch : "+batches[i].batch_name+"</div>";
			var flag = false;
			for(j=0;j<data.student_FeesList.length;j++){
				if(data.student_FeesList[j].batch_id == batches[i].batch_id){
					htmlString = htmlString + "<div class='well feesData'>";
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Batch Fees</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;"+data.student_FeesList[j].batch_fees+"</div></div>";
					if(data.student_FeesList[j].discount_type == 'Amt'){
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Discount</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;"+data.student_FeesList[j].discount+"</div></div>";
					}else{
						htmlString = htmlString + "<div class='row'><div class='col-md-3'>Discount</div><div class='col-md-1'>:</div><div class='col-md-2'>"+data.student_FeesList[j].discount+"%</div></div>";	
					}
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Final Fees</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;"+data.student_FeesList[j].final_fees_amt+"</div></div>";
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Fees Paid</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;<span class='feesPaid'>"+data.student_FeesList[j].fees_paid+"</span></div></div>";
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Remaining Fees</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;<span class='feesDue'>"+data.student_FeesList[j].fees_due+"</span></div></div>";
					/* htmlString = htmlString + "<div class='row amtData'><div class='col-md-2'><input type='number' min=0 id='amt' value='0' class='form-control'></div><div class='col-md-1'><button class='btn btn-primary btn-sm payFees' id='"+batches[i].batch_id+"'>Pay</button></div></div>"; */
					htmlString = htmlString + "</div>";
					flag = true;
				} 
			}
			if(flag == false){
				htmlString = htmlString + "<div class='well'>Fees Data is not available.</div>";
			}
		}
		
		$("#feesTab").append(htmlString);
	}
	function createProgressCard(data,studentBatches){
		graphData = [];
		$("#marksTab #marksTabData").empty();
		var htmlString = "";
		if(data.detailMarklist != null){
		if(studentBatches != null){	
	 	for(outer = 0 ; studentBatches.length > outer;outer++){
		var newBatchData = "<div class='container'><b>Batch : "+studentBatches[outer].batch_name+"</b><div class='row'><table class='table table-bordered'><thead><tr><th></th>";
		var subjectFlag = false;
		for(i =0 ; i<data.batchExamDistinctSubjectList.length;i++){
			if(data.batchExamDistinctSubjectList[i].batch_id == studentBatches[outer].batch_id){
			newBatchData = newBatchData + "<th>"+data.batchExamDistinctSubjectList[i].subject_name+"</th>";
			subjectFlag = true;
			}
		}
		if(subjectFlag == true ){
		newBatchData = newBatchData + "<th>Total</th>";
		newBatchData = newBatchData + "<th>Out Of</th>";
		newBatchData = newBatchData + "<th>Percentage</th>";
		newBatchData = newBatchData + "</tr></thead>";
		 newBatchData = newBatchData + "<tbody>";
		 
		for(i =0 ; i<data.batchExamList.length;i++){
			if(data.batchExamList[i].batch_id == studentBatches[outer].batch_id){
				var innerGraphData = [];
			newBatchData = newBatchData + "<tr><td>"+data.batchExamList[i].exam_name+"</td>";
			var total_marks = 0;
					for(k=0;k<data.batchExamDistinctSubjectList.length;k++){
						if(data.batchExamDistinctSubjectList[k].batch_id == studentBatches[outer].batch_id){
						var flag = false;
							for(l=0;l<data.detailMarklist.length;l++){
								if(data.batchExamList[i].exam_id == data.detailMarklist[l].exam_id 
										&& data.batchExamDistinctSubjectList[k].subject_id == data.detailMarklist[l].sub_id && 
										data.detailMarklist[l].batch_id==data.batchExamList[i].batch_id){
									var obj = {};
									if(data.detailMarklist[l].examPresentee == ""){
										obj.marks = 0;
										newBatchData = newBatchData + "<td>-</td>";
									}else if(data.detailMarklist[l].examPresentee == "A"){
										obj.marks = 0;
										newBatchData = newBatchData + "<td>A</td>";
									}else{
									if(data.detailMarklist[l].marks>0){
									obj.marks = parseFloat(((data.detailMarklist[l].marks/data.detailMarklist[l].examSubjectTotalMarks)*100).toFixed(2));
									}else{
										obj.marks = 0;
									}
									newBatchData = newBatchData + "<td>"+data.detailMarklist[l].marks+"</td>";
									total_marks = total_marks + data.detailMarklist[l].marks;
									}
									if(data.detailMarklist[l].avgMarks > 0){
									obj.avg =  parseFloat(((data.detailMarklist[l].avgMarks/data.detailMarklist[l].examSubjectTotalMarks)*100).toFixed(2));;
									}else{
										obj.avg = 0;
									}
									if(data.detailMarklist[l].topperMarks > 0){
									obj.topper =  parseFloat(((data.detailMarklist[l].topperMarks/data.detailMarklist[l].examSubjectTotalMarks)*100).toFixed(2));;
									}else{
										obj.topper = 0;
									}
									obj.sub = data.batchExamDistinctSubjectList[k].subject_name;
									obj.exam_name = "Batch : "+studentBatches[outer].batch_name+" / Exam : "+data.batchExamList[i].exam_name;
									innerGraphData.push(obj);
									flag = true;
									break;
								}
							}
							if(flag == false ){
								newBatchData = newBatchData + "<td>-</td>";
							}
						}
					}
					newBatchData = newBatchData + "<td>"+total_marks+"</td>";
					newBatchData = newBatchData + "<td>"+data.batchExamList[i].marks+"</td>";
					if(data.batchExamList[i].marks > 0){
					newBatchData = newBatchData + "<td>"+parseFloat(((total_marks/data.batchExamList[i].marks)*100).toFixed(2))+"%</td>";
					}else{
						newBatchData = newBatchData + "<td>0%</td>";
					}
					graphData.push(innerGraphData);
			newBatchData = newBatchData + "</tr>";
			}
		} 
		newBatchData = newBatchData +"</tbody></table></div></div>";
		}else{
			newBatchData = "<div class='container'><b>Batch : "+studentBatches[outer].batch_name+"</b><div class='well'>Marks Data is not available.</div></div>"
		}
		htmlString = htmlString + newBatchData;
 }
 }
 	console.log("Graph Data"+graphData);
		//$("#marksTab #marksTabData").append(htmlString);
		}else{
			htmlString = htmlString + "<div class='well'>Marks Data is not available.</div>";
		}
		$("#marksTab #marksTabData").append(htmlString);
	}
	
	function generateRoll(){
		$("#rollNoModal").modal('toggle');
		/* var handler = {};
		handler.success = function(e){
			$.notify({message: "Roll number generated"},{type: 'success'});
			var batchData = $("#batch").data("batchData")[$("#batch").val()];
			if(batchData.status == null){
				$("#batch").data("batchData")[$("#batch").val()].status = "rollGenerated=yes";
				generateButtonChange();
			}
			$(".searchStudentByBatch").click();
		};
		handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post(generateRollNoUrl+$("#division").val()+"/"+$("#batch").val(),handler); */
	}
	function onBatchChange(){
		$(".containerData").hide();
		var batchID = $(".searchStudent").find("#batch").val();
		if(batchID!=-1){
			var batchData = $("#batch").data("batchData")[batchID];
			if(!batchData.status || batchData.status.indexOf("rollGenerated=yes")==-1){
				$('.generateRollNumber').removeClass("hide").text("Generate roll number").attr("data-toggle","tooltip").attr("data-original-title","Roll number for this batch are not generated, please generate roll number");
			}else{
				$('.generateRollNumber').removeClass("hide").text("Re Generate roll number").attr("data-toggle","tooltip").attr("data-original-title","Roll number for this batch are already generated, Click to regenerate, this may loose previously generated roll number");
			}
			$('.generateRollNumber').tooltip();
		}else{
			$('.generateRollNumber').addClass('hide');
		}
	}
	
	function generateButtonChange(){
		$('.generateRollNumber').text("Re Generate roll number").attr("data-toggle","tooltip").attr("data-original-title","Roll number for this batch are already generated, Click to regenerate, this may loose previously generated roll number");
	}
	
	function studentDetailsAfterUpdate(studentId){
		$(".studentDetails").find("#batchID").val(globalBatchID);
		$(".studentDetails").find("#divisionID").val(globalDivisionID);
		$(".studentDetails").find("#studentID").val($(this).closest("tr").find("#studentId").val());
		$(".studentDetails").find("#currentPage").val($(".paginate_button current").text());
		 $("#generalTab").find(".crendentialDetails").empty();
		var formdata=$(".studentDetails").serialize();
		// studentId = $(this).closest("tr").find("#studentId").val();
		var handlers = {};

		handlers.success = function(data){console.log("Success",data);
		
		$("#studentDetailsBday").html(data.student.dob.substring(8)+"/"+data.student.dob.substring(5,7)+"/"+data.student.dob.substring(0,4));
		   $("#studentDetailsName").html(data.student.fname+" "+data.student.lname);
		   var batchNames = "";
		   $.each(data.batches,function(key,val){
				batchNames =  batchNames + ","+ val.batch_name;
			});
		   editStudent = data.student;
		   batchNames = batchNames.replace(",","");
		   $("#studentInstId").html(data.student.studentInstRegNo);
		   $("#studentDetailsClass").html(data.division.divisionName+" "+data.division.stream);
		   $("#studentDetailsBatch").html(batchNames);
		   $("#studentDetailsStudentPhone").html(data.student.phone);
		   $("#studentDetailsStudentEmail").html(data.student.email);
		   $("#studentDetailsAddress").html(data.student.addr+","+data.student.city+","+data.student.state);
		   $("#studentDetailsParentName").html(data.student.parentFname+" "+data.student.parentLname);
		   $("#studentDetailsParentPhone").html(data.student.parentPhone);
		   $("#studentDetailsParentEmail").html(data.student.parentEmail);
		   if(data.student.joiningDate != null){
			   $("#studentDetailsJoiningDate").html(data.student.joiningDate.split("-").reverse().join("/"));
			   }
		   if(data.student.gender != null){
			   $("#studentDetailsGender").html(data.student.gender);
			}else{
				$("#studentDetailsGender").html("-");
			}
		   if(data.studentUserBean.status == "M" || data.studentUserBean.status == "E"){
			 $("#generalTab").append("<div class='crendentialDetails'><div class='row'><label>Credential Information</label></div>"+
					 	"<div class='row'><div class='col-md-2'>Username</div>"+
				    	"<div class='col-md-1'>:</div>"+
				    	"<div class='col-md-3'>"+data.studentUserBean.loginName+"</div>"+
				    	"<div class='col-md-2'>Password</div>"+
				    	"<div class='col-md-1'>:</div>"+
				    	"<div class='col-md-3'>"+data.studentUserBean.loginPass+"</div></div>");  
		   }
		   createProgressCard(data.examWiseStudentDetails,data.batches);
		   createAttenanceTab(data.attendanceData);
		   createFeesTab(data.feesServiceBean,data.batches);
		$(".studentDetailsDiv").show();
		$("#marksGraphData").empty();
		if(graphData.length > 0){
		
			for (i =0; i <graphData.length;i++){
		if(graphData[i].length>0){
			$("#marksGraphData").append("<div id='graph"+i+"' style='border:outset;margin:1%'><div align='center' style='border-bottom:outset;font-weight:bold'>"+graphData[i][0].exam_name+"</div><div id='graphData"+i+"'></div></div>");
		}}}
		/* setTimeout(function(){
			
		},5000);
		 */
		$(".studentList").hide();
		 
		 /****Show additional information********/
		 if(data.additionalStudentInfoBean && data.additionalFormFieldBeanDl){
			 $("#generalTabAdditionalInfo").empty();
			  formFieldJSON = JSON.parse(data.additionalFormFieldBeanDl.formField);
			 $.each(data.additionalStudentInfoBean,function(key,val){
				 var additionalData = $("#additionalData").html();
				 var $additionalData = $(additionalData);
				 if(formFieldJSON[key]){
					 $additionalData.find("#label").html(formFieldJSON[key]);
					 $additionalData.find("#data").html(val);
					 $("#generalTabAdditionalInfo").append($additionalData);	 
				 }
				 
			 });
			 
		 }
		 
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getStudentDetails/"+studentId,handlers);
	}
	
	
	function studentDetails(){
		$("#generalTab").find("span").html("");
		$(".studentDetails").find("#batchID").val(globalBatchID);
		$(".studentDetails").find("#divisionID").val(globalDivisionID);
		$(".studentDetails").find("#studentID").val($(this).closest("tr").find("#studentId").val());
		$(".studentDetails").find("#currentPage").val($(".paginate_button current").text());
		 $("#generalTab").find(".crendentialDetails").empty();
		var formdata=$(".studentDetails").serialize();
		 studentId = $(this).closest("tr").find("#studentId").val();
		var handlers = {};

		handlers.success = function(data){console.log("Success",data);
		
		$("#studentDetailsBday").html(data.student.dob.substring(8)+"/"+data.student.dob.substring(5,7)+"/"+data.student.dob.substring(0,4));
		   $("#studentDetailsName").html(data.student.fname+" "+data.student.lname);
		   var batchNames = "";
		   $.each(data.batches,function(key,val){
				batchNames =  batchNames + ","+ val.batch_name;
			});
		   editStudent = data.student;
		   batchNames = batchNames.replace(",","");
		   $("#studentInstId").html(data.student.studentInstRegNo);
		   $("#studentDetailsClass").html(data.division.divisionName+" "+data.division.stream);
		   $("#studentDetailsBatch").html(batchNames);
		   $("#studentDetailsStudentPhone").html(data.student.phone);
		   $("#studentDetailsStudentEmail").html(data.student.email);
		   $("#studentDetailsAddress").html(data.student.addr+","+data.student.city+","+data.student.state);
		   $("#studentDetailsParentName").html(data.student.parentFname+" "+data.student.parentLname);
		   $("#studentDetailsParentPhone").html(data.student.parentPhone);
		   $("#studentDetailsParentEmail").html(data.student.parentEmail);
		   if(data.student.joiningDate != null){
		   $("#studentDetailsJoiningDate").html(data.student.joiningDate.split("-").reverse().join("/"));
		   }
		   if(data.student.gender != null){
			   $("#studentDetailsGender").html(data.student.gender);
			}else{
				$("#studentDetailsGender").html("-");
			}
		   
		   if(data.studentUserBean.status == "M" || data.studentUserBean.status == "E"){
			 $("#generalTab").append("<div class='crendentialDetails'><div class='row'><label>Credential Information</label></div>"+
					 	"<div class='row'><div class='col-md-2'>Username</div>"+
				    	"<div class='col-md-1'>:</div>"+
				    	"<div class='col-md-3'>"+data.studentUserBean.loginName+"</div>"+
				    	"<div class='col-md-2'>Password</div>"+
				    	"<div class='col-md-1'>:</div>"+
				    	"<div class='col-md-3'>"+data.studentUserBean.loginPass+"</div></div>");  
		   }
		   createProgressCard(data.examWiseStudentDetails,data.batches);
		   createAttenanceTab(data.attendanceData);
		   createFeesTab(data.feesServiceBean,data.batches);
		$(".studentDetailsDiv").show();
		$("#marksGraphData").empty();
		if(graphData.length > 0){
		
			for (i =0; i <graphData.length;i++){
		if(graphData[i].length>0){
			$("#marksGraphData").append("<div id='graph"+i+"' style='border:outset;margin:1%'><div align='center' style='border-bottom:outset;font-weight:bold'>"+graphData[i][0].exam_name+"</div><div id='graphData"+i+"'></div></div>");
		}}}
		/* setTimeout(function(){
			
		},5000);
		 */
		$(".studentList").hide();
		 
		 /****Show additional information********/
		 if(data.additionalStudentInfoBean && data.additionalFormFieldBeanDl){
			 $("#generalTabAdditionalInfo").empty();
			  formFieldJSON = JSON.parse(data.additionalFormFieldBeanDl.formField);
			 $.each(data.additionalStudentInfoBean,function(key,val){
				 var additionalData = $("#additionalData").html();
				 var $additionalData = $(additionalData);
				 if(formFieldJSON[key]){
					 $additionalData.find("#label").html(formFieldJSON[key]);
					 $additionalData.find("#data").html(val);
					 $("#generalTabAdditionalInfo").append($additionalData);	 
				 }
				 
			 });
			 
		 }
		 
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getStudentDetails/"+studentId,handlers);
	}
	function enableEdit(){
		enabledEdit = true;
		var batchData = getBatchesForStudent($(this));
		var classData = getAllClasses($(this));
		//var subjectName = $(this).closest("tr").find(".defaultteacherSuffix").text().trim();
		//$(this).closest("tr").find(".editteacherSuffix").val(subjectName);
		$(this).closest("tr").addClass("editEnabled");
	}


	function cancelEdit(){
		//$(this).closest("tr").find(".selectSubject").val(selectSubject).change();
		$(this).closest("tr").removeClass("editEnabled");
		$(this).closest("tr").find(".error").empty();
	}
	var that;
	function saveStudent(){
		$(this).closest("tr").find(".error").empty();
		//$(this).closest("tr").find(".suffixError").empty();
		var studentId = $(this).closest("tr").find("#studentId").val();
		//var teacherSuffix = $(this).closest("tr").find(".editteacherSuffix").val().trim();
		var batchIds = $(this).closest("tr").find(".selectBatch").val();
		that=$(this);
		updateStudentAjax(studentId,batchIds);
	}
	
	function updateStudentAjax(studentId,batchIds){
		var regNumber = /^[0-9]+$/;
		var flag=false;
		var batchIdsStr = "";
		var rollNo = that.closest("tr").find(".rollNo").val();
		if(rollNo != undefined){
			rollNo.trim();
		}else{
			rollNo = "";
		}
		var divstr = that.closest("tr").find(".selectDivision").select2('data')[0].text;
		var div = that.closest("tr").find(".selectDivision").val();
		if(batchIds == "" || batchIds == null){
			flag = true;
			that.closest("tr").find(".batchError").html("Select Batch!");
		}else{
			 batchIdsStr = batchIds.join(',');	
		}
		
		if(!rollNo.match(regNumber) && rollNo != ""){
			flag = true;
			that.closest("tr").find(".rollError").html("Invalid Roll no");
		}
		
		if(flag==false){
		$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "updateStudent",
						 regId:'',
						 batchIds:batchIdsStr,
						 studentId:studentId,
						 currentBatch:globalBatchID,
						 rollNo:rollNo,
						 div:div
				   		},
				   type:"POST",
				   success:function(data){
					   data = JSON.parse(data);
					   if(data.status != "roll"){
					   that.closest("tr").find(".selectBatch").select2().val(batchIds).change();
					   var batchStr = "";
					   $.each(that.closest("tr").find(".selectBatch").select2('data'),function(key,val){
							batchStr =  batchStr + ","+ val.text;
						});
					   batchStr = batchStr.replace(",","");
					   that.closest("tr").find(".defaultBatchname").html(batchStr);
					   that.closest("tr").find(".defaultDiv").html(divstr);
					   if(rollNo != ""){
					   that.closest("tr").find(".defaultRollNo").html(rollNo);
					   }
					   that.closest("tr").removeClass("editEnabled");
				   	}else{
				   		that.closest("tr").find(".rollError").html("Duplicate Roll no");
				   	}
				   },
				   error:function(data){
				   
				   }
			});
		}
	}
	
	function deleteStudentPrompt(){
		var batchIdToDelete = $(this).closest("tr").find("#batchId").val();
		var studentId = $(this).closest("tr").find("#studentId").val();
		deleteBatchConfirm(studentId,$(this));	
	}
	
	function deleteBatchConfirm(studentId,that){
		modal.modalConfirm("Delete","Do you want to delete?","Cancel","Delete",deleteStudent,[studentId,that]);
	}
	
	function deleteStudent(studentId,that){
				/* $.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "deleteStudent",
						 regId:'',
						 studentId:studentId
				   		},
				   type:"POST",
				   success:function(data){
					   $.notify({message: "Student successfuly deleted"},{type: 'success'});
					   $('input[type="hidden"]#studentId[value="'+studentId+'"]').closest("tr").remove();
				   	},
				   error:function(data){
				
				   }
			}); */
		var handlers = {};
		handlers.success=function(){
			var table = $("#classTable").DataTable();
			that.closest("tr").addClass('selected');
			table.row('.selected').remove().draw( false );
			$.notify({message: "Student successfuly deleted"},{type: 'success'});
		};   
		handlers.error=function(){
			$.notify({message: "Student not deleted"},{type: 'danger'});
		};   
		
		rest.deleteItem("rest/commonDelete/deleteStudent/"+studentId,handlers);
	}
	
	function successCallbackclass(data,type){
		$(".containerData").show();
		data = JSON.parse(data);
		var status = data.status;
		$(".generateRollNumber").hide();
		if(status != "error"){
		if(type == "name"){
			dataTable = $('#classTable').DataTable({
				language: {
				        "emptyTable":     "Students not available"
				    },
				bDestroy:true,
				data: data.studentList,
				lengthChange: true,
				columns: [
					{title:"#",data:null},
					{ title: "Student Name",data:"student",render:function(data,event,row){
						var input = "<input type='hidden' id='studentId' value='"+data.student_id +"'>";
						var modifiedObj = data.fname+" "+data.lname;
						return modifiedObj+input;
					}},
					{ title: "Division",data:"division",render:function(data,event,row){
						/* console.log(row);
						var modifiedObj = data.divisionName;
						return modifiedObj; */
						var divisionNames = "";
						var selectTag = '<div class="editable"><select class="selectDivision" style="width:100%">';
						selectTag = selectTag+"</select></div>";
						var subjects;
						if(data != null){
						selectTag = selectTag + '<input type="hidden" class="editDivID" value="'+data.divId+'">';
						divisionNames = '<div class="default defaultDiv">'+data.divisionName+" "+data.stream+'</div>';
					}else{
						divisionNames = '<div class="default defaultDiv"></div>';
						selectTag = selectTag + '<input type="hidden" class="editDivID" value="-1">';
					}
						
						var span = '<span class="editable subjectError"></span>'
						return divisionNames + selectTag + span;
					}},
					{ title: "Batch",data:"batches",render:function(data,event,row){
						var batchNames = "";
						var selectTag = '<div class="editable"><select class="selectBatch" multiple="" style="width:100%">';
						var subjects;
						$.each(data,function(key,val){
							selectTag = selectTag + '<option selected="selected" value="'+val.batch_id+'">'+val.batch_name+'</option>';
							batchNames =  batchNames + ","+ val.batch_name;
						});
						batchNames = batchNames.replace(",","");
						batchNames = '<div class="default defaultBatchname">'+batchNames+'</div>';
						selectTag = selectTag+"</select></div>";
						var span = '<span class="editable batchError error"></span>'
						return batchNames + selectTag + span;
						}},
					{ title: "Actions",data:null,render:function(data){
						var buttons = '<div class="default">'+
						'<input type="button" class="btn btn-xs btn-primary btn-batch-edit" value="Edit">&nbsp;'+
						'<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete">&nbsp;'+
						'<input type="button" class="btn btn-xs btn-info btn-student-details" value="Details">&nbsp;'+
						'<input type="button" class="btn btn-xs btn-primary btn-certificate-print" value="Print Certificate">&nbsp;'+
					'</div>'+
					'<div class="editable">'+
						'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>'
					
					return buttons;
						}}
				]
			});
			
			dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw();	
		}else{
			if(data.studentList.length>0){			$(".generateRollNumber").show();}
		dataTable = $('#classTable').DataTable({
			language: {
			        "emptyTable":     "Students not available"
			    },
			bDestroy:true,
			data: data.studentList,
			lengthChange: true,
			columns: [
				{title:"Roll no",data:"rollNo",render:function(data,event,row){
					if(data == 0){
						return  '<div class="editable"><input type="text" class="rollNo form-control"></div>'+'<div class="default defaultRollNo">-</div>'+'<span class="editable rollError error"></span>';	
					}else{
					return '<div class="editable"><input type="text" class="rollNo form-control" value="'+data+'"></div>'+'<div class="default defaultRollNo">'+data+'</div>'+'<span class="editable rollError error"></span>';
					}
				}},
				{ title: "Student Name",data:"student",render:function(data,event,row){
					var input = "<input type='hidden' id='studentId' value='"+data.student_id +"'>";
					var modifiedObj = data.fname+" "+data.lname;
					return modifiedObj+input;
				}},
				{ title: "Division",data:"division",render:function(data,event,row){
					/* console.log(row);
					var modifiedObj = data.divisionName;
					return modifiedObj; */
					var divisionNames = "";
					var selectTag = '<div class="editable"><select class="selectDivision" style="width:100%">';
					selectTag = selectTag+"</select></div>";
					var subjects;
					if(data != null){
					selectTag = selectTag + '<input type="hidden" class="editDivID" value="'+data.divId+'">';
					divisionNames = '<div class="default defaultDiv">'+data.divisionName+" "+data.stream+'</div>';
				}else{
					divisionNames = '<div class="default defaultDiv"></div>';
					selectTag = selectTag + '<input type="hidden" class="editDivID" value="-1">';
				}
					
					var span = '<span class="editable subjectError"></span>'
					return divisionNames + selectTag + span;
				}},
				{ title: "Batch",data:"batches",render:function(data,event,row){
					var batchNames = "";
					var selectTag = '<div class="editable"><select class="selectBatch" multiple="" style="width:100%">';
					var subjects;
					$.each(data,function(key,val){
						selectTag = selectTag + '<option selected="selected" value="'+val.batch_id+'">'+val.batch_name+'</option>';
						batchNames =  batchNames + ","+ val.batch_name;
					});
					batchNames = batchNames.replace(",","");
					batchNames = '<div class="default defaultBatchname">'+batchNames+'</div>';
					selectTag = selectTag+"</select></div>";
					var span = '<span class="editable batchError error"></span>'
					return batchNames + selectTag + span;
					}},
				{ title: "Action",data:null,render:function(data){
					var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-xs btn-primary btn-batch-edit" value="Edit">&nbsp;'+
					'<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete">&nbsp;'+
					'<input type="button" class="btn btn-xs btn-info btn-student-details" value="Details">&nbsp;'+
					'<input type="button" class="btn btn-xs btn-primary btn-certificate-print" value="Print Certificate">&nbsp;'+
				'</div>'+
				'<div class="editable">'+
					'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
					'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>&nbsp;'+
				'</div>'
				
				return buttons;
					}}
			]
		});
		
		/* dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); */
		}}else{
			that.closest('.addclassContainer').find(".addclassnameerror").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Class already exists!!');
		}
	}
	var preselectedsubjects="";
	function getBatchesForStudent(that){
		preselectedsubjects=that.closest("tr").find(".selectBatch").val();
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = that.closest("tr").find(".editDivID").val();
		var batchDataArray = [];
		if(!divisionId || divisionId.trim()=="" || divisionId == -1){
			$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please select a division');
			$('div#addStudentModal .error').show();
			that.closest("tr").find(".selectBatch").select2({data:"",placeholder:"Select Batch"});
		}else{		
		  /* $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchBatchesForDivision",
				 regId:'',
				 divisionId:divisionId						 
		   		},
		   type:"POST",
		   async:false,
		   success:function(e){
			    var data = JSON.parse(e);
			    if(data.status!="error"){
				var batchData = {};
			    $.each(data.batches,function(key,val){
					var data = {};
					data.id = val.batch_id;
					data.text = val.batch_name;
					batchDataArray.push(data);
					batchData[data.id] = val;
				});
			    }
		   	},
		   error:function(e){
			   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
				$('div#addStudentModal .error').show();
		   }
		   
	}); */
	
			var handlers = {};
			handlers.success=function(data){
				 $.each(data,function(key,val){
						var data = {};
						data.id = val.batch_id;
						data.text = val.batch_name;
						batchDataArray.push(data);
						//batchData[data.id] = val;
					});
				console.log(batchDataArray);
				
					that.closest("tr").find(".selectBatch").select2({
				data:batchDataArray
			}).val(preselectedsubjects).change();
				return batchDataArray;
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			if(divisionId != "-1"){
			rest.get("rest/classownerservice/getBatches/"+divisionId,handlers);
			}else{
				that.closest("tr").find(".selectBatch").select2({data:"",placeholder:"Select Batch"});
			}
	}
	}
	
	function getBatchesOfDivision(){
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = $('#division').val();
		var batchDataArray = [];
		if(!divisionId || divisionId.trim()=="" || divisionId == -1){
			$('#batch').empty();
			var tempData ={};
		    tempData.id = "-1";
		    tempData.text = "Select Batch";
		    $("#batch").select2({data:tempData,placeholder:"Select Batch"});
		}else{		
		  $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchBatchesForDivision",
				 regId:'',
				 divisionId:divisionId						 
		   		},
		   type:"POST",
		   async:false,
		   success:function(e){
			   $('#batch').empty();
			    var data = JSON.parse(e);
			    if(data.status!="error"){
			    var tempData ={};
			    tempData.id = "-1";
			    tempData.text = "Select Batch";
			    batchDataArray.push(tempData);
				var batchData = {};
			    $.each(data.batches,function(key,val){
					var data = {};
					data.id = val.batch_id;
					data.text = val.batch_name;
					batchDataArray.push(data);
					batchData[data.id] = val;
				});
			    $("#batch").select2({data:batchDataArray,placeholder:"type batch name"}).data("batchData",batchData);
			    }else{
			    	$("#batch").select2({data:"",placeholder:"No batch found"});
			    }
		   	},
		   error:function(e){
			   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
				$('div#addStudentModal .error').show();
		   }
		   
	});

	}
	}

	function getNames(){
		var names = {};
		  $.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "fetchNamesForSuggestion"						 
			   		},
			   type:"POST",
			   async : false,
			   success:function(e){
				   var data = JSON.parse(e);
				   names =  data.names;
			   },
			   error:function(e){
				   }
			   });
		return names;
	}
	
	function getAllClasses(that){
		var classDataArray = [];
		var classData = {};
		/* $.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getAllClasses"
				   		},
				   type:"POST",
					async:false,
				   success:function(data){
						classData = JSON.parse(data);
				   	},
				   error:function(data){
				   
				   }
			}); */
		var handlers = {};
		handlers.success=function(e){
			$.each(e,function(data,val){
				var data = {};
				data.id = val.divId;
				data.text = val.divisionName+" "+val.stream;
				classDataArray.push(data);
			});
			console.log(classDataArray);
			if(classDataArray.length > 0){
			 var preselectedclass=that.closest("tr").find(".editDivID").val();
				that.closest("tr").find(".selectDivision").select2({
					data:classDataArray
				}).val(preselectedclass).change(); 
				//that.closest("tr").find(".selectDivision").trigger("change");
			}else{
				that.closest("tr").find(".selectDivision").select2({data:"",placeholder:"No Class found"});
			}
			return classDataArray;
		};   
		handlers.error=function(){
			$.notify({message: "Error"},{type: 'danger'});
		};   
		
		rest.get("rest/classownerservice/getAllClasses",handlers);
			
			
	}
</script>

<body>
<div class="studentList">
<ul class="nav nav-tabs" style="border-radius:10px">
  <li><a href="managestudent">Add Student</a></li>
   <li><a href="bulkStudentUpload">Add Student Through File</a></li>
  <li class="active"><a href="#viewstudenttab" data-toggle = "tab">View Student</a></li>
</ul>

<div id="viewstudenttab">
<div class="well">
<div class="row searchStudent">
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
<select id="batch" style="width:100%" class="form-control">
	<option value="-1">Select Batch</option>
</select>
<span class="error" id="batchError"></span>
</div>
<div class="col-md-2">
<button class="btn btn-primary searchStudentByBatch" id="searchStudentByBatch">Search</button>
</div>
	<div class="col-md-1"><button class="btn btn-primary" style="border-radius:55%;background: grey;border: grey" disabled="disabled">OR</button></div>
<div class="col-md-3">
<form action="javascript:void(0)">
        <div class="typeahead-container">
            <div class="typeahead-field">

            <span class="typeahead-query">
                <input id="studentNameSearch"
                       name="studentNameSearch"
                       type="search"
                       autofocus
                       autocomplete="off" Placeholder="Search By Name" class="form-control" style="border-radius:4px 0 0 4px" value="<c:out value='${studentNameSearch}'></c:out>">
            </span>
            <span class="typeahead-button">
                <button type="submit" id="searchStudentByName" style="border-radius:0 4px 4px 0">
                    <span class="typeahead-search-icon" ></span>
                </button>
            </span>

            </div>
        </div>
    </form>
</div>
</div>
</div>
</div>
<div class="container containerData">
<div class="row">
<div class="col-md-3">
<select id="certificate" class="form-control">
					<option value="-1">Select Certificate</option>
					<c:forEach items="${certificateList}" var="certificate" varStatus="counter">
					<option value=<c:out value='${certificate.cert_id }'></c:out>><c:out
								value="${certificate.cert_desc }"></c:out>
					</c:forEach>
				</select>
<div id="certificateSelectError" class="error"></div>
</div>
<div class="col-md-3">
<button class="btn btn-primary generateRollNumber hide">Generate Roll No</button>
</div>
</div>
<br/><br/>
<table class="table table-striped classTable" id="classTable" >
	<thead>
		<th>Roll no</th><th>Student name</th><th>Division</th><th>Batch</th><th></th>
	</thead>
	<tbody></tbody>
</table>
</div>
<form action="studentDetails" class="studentDetails">
<input type="hidden" id="studentID" name="studentID">
<input type="hidden" id="batchID" name="batchID">
<input type="hidden" id="divisionID" name="divisionID">
<input type="hidden" id="currentPage" name="currentPage">
</form>
</div>
<div class="studentDetailsDiv" style="display: none">
<div class="container">
<div class="row">
<button class="btn btn-primary" id="back">Back To Student List</button>
</div>
</div>
<div class="container" style="padding-top: 2%">
<div class="row">
	<div class="col-md-4">Name : <span id="studentDetailsName"></span></div>
	<div class="col-md-3">Class : <span id="studentDetailsClass"></span></div>
	<div class="col-md-3">Batch : <span id="studentDetailsBatch"></span></div>
	<div class="col-md-2"><button id="editStudentPersonalRecord" class="btn btn-primary">Edit Details</button></div>
</div>
<div class="row" style="padding-top: 1%">
<ul class="nav nav-tabs" style="border-radius:10px">
  <li class="active"><a href="#generalTab" data-toggle = "tab">General</a></li>
  <li><a href="#marksTab" data-toggle = "tab">Marks</a></li>
  <li><a href="#feesTab" data-toggle = "tab">Fees</a></li>
  <li><a href="#attendanceTab" data-toggle = "tab">Attendance</a></li>
</ul>
</div>
<div class="tab-content" style="padding-top: 1%">
  <div id="generalTab" class="tab-pane fade in active">
   <div class="row"><label>Student Information</label></div>
    <div class="row">
    	<div class="col-md-2">Registration number</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentInstId"></span></div>
    </div>
    <div class="row">
    	<div class="col-md-2">Birth Date</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsBday"></span></div>
    	<div class="col-md-2">Phone No</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsStudentPhone"></span></div>
    </div>
    <div class="row">
    	<div class="col-md-2">Email ID</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsStudentEmail"></span></div>
    	<div class="col-md-2">Address</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsAddress"></span></div>
    </div>
     <div class="row">
    	<div class="col-md-2">Institute Joining Date</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsJoiningDate"></span></div>
    	<div class="col-md-2">Gender</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsGender"></span></div>
    </div>
    <div class="row"><label>Parents Information</label></div>
    <div class="row">
    	<div class="col-md-2">Parent Name</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsParentName"></span></div>
    	<div class="col-md-2">Phone No</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsParentPhone"></span></div>
    </div>
    <div class="row">
    	<div class="col-md-2">Email ID</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsParentEmail"></span></div>
    </div>
    
    <div class="row"><label>Additional Information</label></div>
    <div id="generalTabAdditionalInfo">
    
    </div>
  </div>
  <div id="marksTab" class="tab-pane fade marksTab">
    <div id="marksTabData"></div>
    <div id="marksGraphData"></div>
  </div>
  <div id="feesTab" class="tab-pane fade">
    <h3>Fees</h3>
  </div>
  <div id="attendanceTab" class="tab-pane fade">
  </div>
</div>
</div>
</div>

<div class="container" style="display: none" id="editPersonalDetailsDiv">
<div class="row"><b>Student Details</b></div>
<div class="row">
	<div class="col-md-2"><b>Registration number:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsRegNo" class="form-control">
						  <span id="editPersonalDetailsRegNoError" class="error"></span>	
	</div> 
</div>	
<div class="row">
	<div class="col-md-2"><b>First Name:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsStudentFName" class="form-control">
						  <span id="editPersonalDetailsStudentFNameError" class="error"></span>	
	</div> 
	<div class="col-md-2"><b>Last Name:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsStudentLName" class="form-control">
						  <span id="editPersonalDetailsStudentLNameError" class="error"></span>		
	</div> 
</div>
<div class="row">
	<div class="col-md-2"><b>Mobile:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsStudentMobile" class="form-control">
						  <span id="editPersonalDetailsStudentMobileError" class="error"></span>		
	</div> 
	<div class="col-md-2"><b>Email:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsStudentEmail" class="form-control">
						  <span id="editPersonalDetailsStudentEmailError" class="error"></span>	
	</div> 
</div>
<div class="row">
	<div class="col-md-2"><b>DOB:</b></div> 
	<div class="col-md-3"><div id="datetimepicker" class="input-group" style="width :250px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="editPersonalDetailsStudentDOB" name="editPersonalDetailsStudentDOB" required="required"  readonly/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					<span id="editPersonalDetailsStudentDOBError" class="error"></span>	
					</div> 
	<div class="col-md-2"><b>Address:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsStudentAddress" class="form-control">
						  <span id="editPersonalDetailsStudentAddressError" class="error"></span>		
	</div> 
</div>
<div class="row">
	<div class="col-md-2"><b>City:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsStudentCity" class="form-control">
						  <span id="editPersonalDetailsStudentCityError" class="error"></span>	
	</div> 
	<div class="col-md-2"><b>State:</b></div> 
	<div class="col-md-3">
	<input type="hidden" class="form-control" name="editPersonalDetailsStudentState" id="editPersonalDetailsStudentState" required="required" value="-1"'/>
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
			</div></div> 
</div>
<div class="row">
	<div class="col-md-2"><b>Joining Date:</b></div> 
	<div class="col-md-3"><div id="joiningDatePicker" class="input-group" style="width :250px;">
						<input class="form-control" data-format="YYYY-MM-DD"
							type="text"  id="editPersonalDetailsJoiningDatePicker" name="editPersonalDetailsJoiningDatePicker" required="required"  readonly/> <span class="input-group-addon add-on"> <i
							class="glyphicon glyphicon-calendar glyphicon-time"></i>
						</span>
					</div>
					<span id="editPersonalDetailsStudentDOBError" class="error"></span>	
					</div> 
	<div class="col-md-2">Gender:</div>
		<div class="col-md-3">
		<select id="editPersonalDetailsGender" class="form-control" style="width:100%">
			<option value="-1">Select Gender</option>
			<option value="M">Male</option>
			<option value="F">Female</option>
		</select>
		<span id="editPersonalDetailsGenderError" class="error"></span>
		</div>
</div>
<div class="row"><b>Parent Details</b></div>
<div class="row">
	<div class="col-md-2"><b>First Name:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsParentFName" class="form-control">
						  <span id="editPersonalDetailsParentFNameError" class="error"></span>	
	</div> 
	<div class="col-md-2"><b>Last Name:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsParentLName" class="form-control">
						   <span id="editPersonalDetailsParentLNameError" class="error"></span>
	</div> 
</div>
<div class="row">
	<div class="col-md-2"><b>Mobile:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsParentMobile" class="form-control">
						   <span id="editPersonalDetailsParentMobileError" class="error"></span>	
	</div> 
	<div class="col-md-2"><b>Email:</b></div> 
	<div class="col-md-3"><input type="text" id="editPersonalDetailsParentEmail" class="form-control">
						   <span id="editPersonalDetailsParentEmailError" class="error"></span>
	</div> 
</div>
<div class="row updateAction">
	<div class="col-md-1"><button class="btn btn-success btn-sm" id="editPersonalDetailsSave">Save</button></div> 
	<div class="col-md-1"><button class="btn btn-danger btn-sm" id="editPersonalDetailsEditCancel">Cancel</button></div>
</div>
</div>

<!-- This template is used to set data -->
<div class="hide" id="additionalData">
<div class="row">
    	<div class="col-md-2" id="label"></div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="data">13/02/1990</span></div>
    </div>
</div>

<div class="modal fade" id="rollNoModal">
    <div class="modal-dialog">
    <div class="modal-content">
 		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="myModalLabel">Generate Roll No.</h4>
        </div>
        <div class="modal-body" id="rollNoModalmessage">
          <div class="well">
          	<input type="radio" name="sortgender" value="M">Male First
          	<input type="radio" name="sortgender" value="F">Female First
          	<input type="radio" name="sortgender" value="X" checked="checked">Mixed
          </div>
          <div class="well">
          	<input type="radio" name="sortby" value="FN" checked="checked">First Name
          	<input type="radio" name="sortby" value="LN">Last Name
          </div>
        </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" id="rollNoModalOK">Ok</button>
      	</div>
    </div>
</div>
</div>
</body>
</html>
		
