(function(){
	var getTeacherListUrl = "/rest/classownerservice/teacher";
	var getBatchUrl = "/rest/classownerservice/getBatches/";
	var postnotificationUrl = "rest/notification/send";
	var getExamListUrl = "rest/classownerservice/getExamList/";
	
	var sendAlertBase = "rest/notification/sendAcademicAlerts";
	var sendFeesDueAlertUrl = sendAlertBase+"/feeDue";
	var sendAttendanceAlertUrl = sendAlertBase+"/attendace";
	var sendProgressCardAlertUrl = sendAlertBase+"/progressCard";
	
	var DIVISION_SELECT = "#sendAlert #divisionSelect";
	var BATCH_SELECT = "#sendAlert #batchSelect";
	var EXAM_SELECT = "#sendAlert #examSelect";
	var DATEPICKER = "#datepicker";
	var DAY_DATE_PICKER = DATEPICKER + "Day";
	var WEEK_DATE_PICKER = DATEPICKER + "Week";
	var MONTH_DATE_PICKER = DATEPICKER + "Month";
	var SEND_FEE_DUE = "#sendFeeDue";
	var SEND_ATTENDANCE = ".sendAttendance";
	var SEND_PROGRESS_CARD = "#sendProgressCard";
	var BASE_FORM = "#baseform";
	
	$(document).ready(function(){
		
		$("body").on("change",DIVISION_SELECT,function(e){
			$("#notificationSummary").hide();
			var handler = {};
			handler.success = function(data){
				loadBatchOfDiv(data);
			}
			handler.error = function(){
				
			}
			if($(this).val()!=-1){
				rest.get(getBatchUrl+$(this).val(),handler,true);
			}else{
				$(BATCH_SELECT).empty();
				 $(BATCH_SELECT).select2().val("").change();
				 $(BATCH_SELECT).select2({data:"",placeholder:"Select Batch"});
			}
		}).on("change",BATCH_SELECT,loadExamList).
		on("click",SEND_FEE_DUE,sendFeeDue).
		on("click",SEND_ATTENDANCE,sendAttendance).
		on("click",SEND_PROGRESS_CARD,sendProgressCard);
		$(DAY_DATE_PICKER).datetimepicker({pickTime: false,format:"YYYY-MM-DD"});
		$(WEEK_DATE_PICKER).datetimepicker({pickTime:false,format:"YYYY-MM-DD"});
		$(MONTH_DATE_PICKER).datetimepicker({pickTime:false,format:"YYYY-MM",minViewMode:'months'});
		var validator = validateInit();
		$(EXAM_SELECT).select2({placeholder: "Select exam list"});
		
	});
	
	function validateInit(){
		var validatorForm = $(BASE_FORM).validate({
			  rules: {
			   divisionSelect: { valueNotEquals: "-1" },
			   batchSelect:{ valueNotEquals: "-1" }
			  },
			  messages: {
			   divisionSelect: { valueNotEquals: "Please select an division!" },
			   batchSelect: { valueNotEquals: "Please select an batch!" },
			   type:{required:"Please select atleast one method"},
			   sendTo:{required:"Please select atlease one receipent"}
			  } ,
			  errorPlacement:function(err,elem){
				  err.insertAfter(elem.parent());
			  }
			 });
		return validatorForm;
	}
	
	function loadBatchOfDiv(data){
		if(data.length > 0){
		var option = "<option value='-1'>Select Batch</option>";
		$.each(data,function(key,val){
			var text = val.batch_name;
			var id = val.batch_id;
			option += "<option value='"+id+"'>"+text+"</option>";
		});
		$(BATCH_SELECT).empty();
		$(BATCH_SELECT).append(option);
		$(BATCH_SELECT).select2().val("-1").change();
		}else{
			$(BATCH_SELECT).empty();
			 $(BATCH_SELECT).select2().val("").change();
			 $(BATCH_SELECT).select2({data:"",placeholder:"Batch not available"});
		}
	}
	
	function loadExamList(){
		$("#notificationSummary").hide();
		var handler = {};
		handler.success = showExamList;
		handler.error = function(){};
		rest.post(getExamListUrl+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val(),handler)
	}
	
	
	function showExamList(data){
		console.log(data);
		var examList = "";
		$.each(data,function(key,val){
			examList += "<option value="+val.exam_id+">"+val.exam_name+"</option>"
		});
		$(EXAM_SELECT).html(examList);
		$(EXAM_SELECT).select2({placeholder: "Select exam list"});
	}
	
	function sendFeeDue(){
		$("#notificationSummary").hide();
		if($(BASE_FORM).valid()){
			var feeDueObj = new FeeDue();
			var feeDueObj = LoadData(feeDueObj);
			console.log(feeDueObj);
			rest.post(sendFeesDueAlertUrl,alertHandler,JSON.stringify(feeDueObj));
		}
		
	}
	
	function sendAttendance(){
		$("#notificationSummary").hide();
		if($(this).closest("form").valid() && $(BASE_FORM).valid()){
			var attendance = new Attnedance();
			var attendance = LoadData(attendance);
			attendance.type = $(this).closest(".row").find(".type").val();
			attendance.date = $(this).closest(".row").find('.date').data("DateTimePicker").getDate().toDate();
			attendance.minAttendace = $(this).closest(".row").find("#minAttendance").val();
			console.log(attendance);
			rest.post(sendAttendanceAlertUrl,alertHandler,JSON.stringify(attendance));
		}
		
	}
	
	function sendProgressCard(){
		$("#notificationSummary").hide();
		if($(this).closest("form").valid() && $(BASE_FORM).valid()){
			var progressCard = new ProgressCard();
			var progressCard = LoadData(progressCard);
			progressCard.examList = $("#examSelect").val();
			rest.post(sendProgressCardAlertUrl,alertHandler,JSON.stringify(progressCard));
		}
	}
	
	function LoadData(Obj){
		Obj.batchId = $(BATCH_SELECT).val();
		Obj.divId = $(DIVISION_SELECT).val();
		Obj.sms = $("#sendAlert #sms").is(":checked");
		Obj.email = $("#sendAlert #email").is(":checked");
		Obj.parent = $("#sendAlert #parent").is(":checked");
		Obj.student = $("#sendAlert #student").is(":checked");
		return Obj;
	}
	
	
	/*Requests bean*/
	function BaseAlertBean(){}
	BaseAlertBean.prototype.batchId;
	BaseAlertBean.prototype.divId;
	BaseAlertBean.prototype.sms;
	BaseAlertBean.prototype.email;
	BaseAlertBean.prototype.parent;
	BaseAlertBean.prototype.student;
	
	function FeeDue(){}
	FeeDue.prototype = Object.create(BaseAlertBean.prototype);
	FeeDue.prototype.constructor = FeeDue;
	
	function Attnedance(){}
	Attnedance.prototype = Object.create(BaseAlertBean.prototype);
	Attnedance.prototype.type;
	Attnedance.prototype.date;
	Attnedance.prototype.minAttendace;
	Attnedance.prototype.constructor = Attnedance;
	
	function ProgressCard(){}
	ProgressCard.prototype = Object.create(BaseAlertBean.prototype);
	Attnedance.prototype.examList;
	ProgressCard.prototype.constructor = ProgressCard;
	
	var alertHandler = {};
	alertHandler.success = function(response){
		$("#notificationSummary").empty();
		$("#notificationSummary").show();
		if(response.status == null || response.status == ""){
			var totalSMSSent = 0;
			if($("#sendAlert #sms").is(":checked")){
				if($("#sendAlert #student").is(":checked")){
					totalSMSSent = totalSMSSent + (response.criteriaStudents-response.studentsWithoutPhone);
				}
				if( $("#sendAlert #parent").is(":checked")){
					totalSMSSent = totalSMSSent + (response.criteriaStudents-response.parentsWithoutPhone);
				}
			}	
			var totalEmailSent = 0;
			if($("#sendAlert #email").is(":checked")){
				if($("#sendAlert #student").is(":checked")){
					totalEmailSent = totalEmailSent + (response.criteriaStudents-response.studentsWithoutEmail);
				}
				if( $("#sendAlert #parent").is(":checked")){
					totalEmailSent = totalEmailSent + (response.criteriaStudents-response.parentsWithoutEmail);
				}
			}	
			$("#notificationSummary").append("<div class='row'><div class='col-md-3'>No of students in batch :"+response.totalStudents+"</div>" +
					"<div class='col-md-3'>"+response.criteriaMsg+"</div><div class='col-md-2'>Total SMS Sent:"+totalSMSSent+"</div>" +
					"<div class='col-md-2'>Total Email Sent:"+totalEmailSent+"</div>" +
					"<a data-toggle='collapse' data-target='.a' style='cursor:pointer'>[ view details ]</a></div>");
			if($("#sendAlert #sms").is(":checked")){
				if($("#sendAlert #student").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of students :"+(response.criteriaStudents-response.studentsWithoutPhone)+"</div>" +
							"<div class='col-md-4'>Students without phone no :"+response.studentsWithoutPhone+"</div></div>")
				}
				if( $("#sendAlert #parent").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of parents :"+(response.criteriaStudents-response.parentsWithoutPhone)+"</div>" +
							"<div class='col-md-4'>Parents without phone no :"+response.parentsWithoutPhone+"</div></div>")
				}
			}
			if($("#sendAlert #email").is(":checked")){
				if($("#sendAlert #student").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of students :"+(response.criteriaStudents-response.studentsWithoutEmail)+"</div>" +
							"<div class='col-md-4'>Students without Email ID :"+response.studentsWithoutEmail+"</div></div>")
				}
				if( $("#sendAlert #parent").is(":checked")){
					$("#notificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of parents :"+(response.criteriaStudents-response.parentsWithoutEmail)+"</div>" +
							"<div class='col-md-4'>Parents without Email ID :"+response.parentsWithoutEmail+"</div></div>")
				}
			}
			/*$.notify({message: "Notification sent successfully"},{type: 'success'});*/
		}else{
			$.notify({message: response.status},{type: 'danger'});
		}
	};
	alertHandler.error = function(){};
})();