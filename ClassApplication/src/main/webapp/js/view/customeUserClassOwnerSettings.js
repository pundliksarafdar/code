var ClassownerSettingsUrl = "rest/customuserservice/setting";
var NO_SMS_ACCESS = "You dont have access to sms";
var NO_EMAIL_ACCESS = "You dont have access to email";
var NO_SMS_EMAIL_ACCESS = "You dont have access to sms and mail";
var NO_OF_SMS_LEFT = "You have {{smsleft}} sms left out of total {{smsTotal}} sms";
var WEEKLY_DAY_SELECTION = "#weeklyDaySelection";
var SAVE = "#save";
$(document).ready(function(){
	loadData();
	$(document).on("click",SAVE,function(){
		var objectToSave = {};
		var checkBox = $("#editNotificationSettingForm").find("[type='checkbox'][data-size='mini']");
		$.each(checkBox,function(key,val){
			var paramId = $(val).attr("id");
			objectToSave[paramId] = $(val).is(":checked");
		});
		objectToSave.emailAttendanceWeeklyThreshold = $("#editNotificationSettingForm").find("#emailAttendanceWeeklyThreshold").val();
		objectToSave.emailAttendanceMonthlyThreshold = $("#editNotificationSettingForm").find("#emailAttendanceMonthlyThreshold").val();
		var paymentDueDate = $("#paymentDueDate").val();
		if(paymentDueDate != ""){
			paymentDueDate = paymentDueDate.split("/");
			paymentDueDate = paymentDueDate[2]+"-"+paymentDueDate[1]+"-"+paymentDueDate[0];
		}
		objectToSave.paymentDueDate = paymentDueDate;
		var recurrenceSelection = $(WEEKLY_DAY_SELECTION).find('input[type="checkbox"]');
		
		var weeklyRecurrence = {};
		$.each(recurrenceSelection,function(key,val){
			var key = $(val).attr("id");
			var isChecked = $(val).is(':checked');
			weeklyRecurrence[key] = isChecked;
		});
		//console.log(weeklyRecurrence);
		objectToSave.weeklyRecurrence = JSON.stringify(weeklyRecurrence);
		var  handler = {};
		handler.success = function(e){$.notify({message: "Settings saved successfully successfully"},{type: 'success'});}
		handler.error = function(e){$.notify({message: "Error occured"},{type: 'danger'});}
		rest.post(ClassownerSettingsUrl,handler,JSON.stringify(objectToSave));
	});
	 $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
});

function loadData(){
	var handler = {};
	handler.success = onDataLoad;
	handler.error = function(e){console.log(e)};
	rest.get(ClassownerSettingsUrl,handler);
}

function onDataLoad(data){
	var messageContainer = $("#editNotificationSettingForm").find('.alert');
	if(!data || !data.instituteStats || !(data.instituteStats.smsAccess || data.instituteStats.emailAccess)){
		messageContainer.html(NO_SMS_EMAIL_ACCESS);
		$(SAVE).addClass("hide");
		$(".smsAccess").attr("disabled","disabled");
		$(".emailAccess").attr("disabled","disabled");
	}else{
		if(!data.instituteStats.smsAccess){
			messageContainer.html(NO_SMS_ACCESS);
			$(".smsAccess").attr("disabled","disabled");
		}else if(!data.instituteStats.emailAccess){
			messageContainer.html(NO_EMAIL_ACCESS);
			$(".emailAccess").attr("disabled","disabled");
		}else{
			messageContainer.html(NO_OF_SMS_LEFT.replace("{{smsleft}}",data.instituteStats.smsLeft).replace("{{smsTotal}}",data.instituteStats.smsAlloted));
			
			var checkBox = $("#editNotificationSettingForm").find("[type='checkbox'][data-size='mini']");
			$.each(checkBox,function(key,val){
				var paramId = $(val).attr("id");
				if(data.classOwnerNotificationBean[paramId]){
					$(val).prop('checked',true);
				}
			});
			var recurrenceSelection = $(WEEKLY_DAY_SELECTION).find('input[type="checkbox"]');
			
			var weeklyRecurrence = JSON.parse(data.classOwnerNotificationBean.weeklyRecurrence);
			$.each(recurrenceSelection,function(key,val){
				var key = $(val).attr("id");
				if(weeklyRecurrence && weeklyRecurrence[key]){
					$(val).prop("checked",true);
				}
			});
			var paymentDueDate = data.classOwnerNotificationBean.paymentDueDate;
			if(paymentDueDate != null){
				paymentDueDate = paymentDueDate.split("-");
				paymentDueDate = paymentDueDate[2]+"/"+paymentDueDate[1]+"/"+paymentDueDate[0];
				$("#paymentDueDate").val(paymentDueDate);
			}
			$("#editNotificationSettingForm").find("#emailAttendanceWeeklyThreshold").val(data.classOwnerNotificationBean.emailAttendanceWeeklyThreshold);
			$("#editNotificationSettingForm").find("#emailAttendanceMonthlyThreshold").val(data.classOwnerNotificationBean.emailAttendanceMonthlyThreshold);
		}
	}
	$("#editNotificationSettingForm").find("[type='checkbox'][data-size='mini']").bootstrapSwitch();
}