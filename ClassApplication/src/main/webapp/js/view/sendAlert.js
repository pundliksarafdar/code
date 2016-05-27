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
			var handler = {};
			handler.success = function(data){
				loadBatchOfDiv(data);
			}
			handler.error = function(){
				
			}
			if($(this).val()!=-1){
				rest.get(getBatchUrl+$(this).val(),handler,true);
			}else{
				
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
		var option = "<option value='-1'>Select batch</option>";
		$.each(data,function(key,val){
			var text = val.batch_name;
			var id = val.batch_id;
			option += "<option value='"+id+"'>"+text+"</option>";
		});
		$(BATCH_SELECT).empty();
		$(BATCH_SELECT).append(option);
	}
	
	function loadExamList(){
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
		if($(BASE_FORM).valid()){
			var feeDueObj = new FeeDue();
			var feeDueObj = LoadData(feeDueObj);
			console.log(feeDueObj);
			rest.post(sendFeesDueAlertUrl,alertHandler,JSON.stringify(feeDueObj));
		}
		
	}
	
	function sendAttendance(){
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
	alertHandler.success = function(){};
	alertHandler.error = function(){};
})();