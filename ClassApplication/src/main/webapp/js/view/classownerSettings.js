var ClassownerSettingsUrl = "rest/ClassownerSettings";
var NO_SMS_ACCESS = "You dont have access to sms";
var NO_EMAIL_ACCESS = "You dont have access to email";
var NO_SMS_EMAIL_ACCESS = "You dont have access to sms and mail";
var NO_OF_SMS_LEFT = "You have {{smsleft}} out of {{smsTotal}}";

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
		var  handler = {};
		handler.success = function(e){console.log(e)}
		handler.error = function(e){console.log(e)}
		rest.post(ClassownerSettingsUrl,handler,JSON.stringify(objectToSave));
	});
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
	}else{
		if(!data.instituteStats.smsAccess){
			messageContainer.html(NO_SMS_ACCESS);
		}else if(!data.instituteStats.emailAccess){
			messageContainer.html(NO_EMAIL_ACCESS);
		}else{
			messageContainer.html(NO_OF_SMS_LEFT.replace("{{smsleft}}",data.instituteStats.smsLeft).replace("{{smsTotal}}",data.instituteStats.smsAlloted));
			
			var checkBox = $("#editNotificationSettingForm").find("[type='checkbox'][data-size='mini']");
			$.each(checkBox,function(key,val){
				var paramId = $(val).attr("id");
				if(data.classOwnerNotificationBean[paramId]){
					$(val).prop('checked',true);
				}
			});
			$("#editNotificationSettingForm").find("[type='checkbox'][data-size='mini']").bootstrapSwitch();
		}
	}
}