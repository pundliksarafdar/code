(function(){
	var getTeacherListUrl = "/rest/customuserservice/getAllTeachers";
	var getBatchUrl = "/rest/customuserservice/getBatches/";
	var postnotificationUrl = "rest/customuserservice/sendText";
	var postnotificationTeacherUrl = "rest/customuserservice/sendTextTeacher";
	
	var BATCH_SELECT = "#batchSelect";
	var SEND_TO_TEACHER = "#sendMessageToTeacher";
	var TEACHER_FORM = "#selectTeacherMessage";
	var STUDENT_N_PARENT_FORM = "#selectStudentNParentMessage";
	var SEND_TO_STU_PAR = "#sendMessageToStuNPar";
	
	
	$(document).ready(function(){
		init();
		getTeacherList();
		
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			$("select").select2();
			$("#examSelect").select2({allowClear: true,placeholder:"Select Exam"}); 
		});
		
		$("#divisionSelect").on("change",function(e){
			var handler = {};
			handler.success = function(data){
				console.log(data);
				loadBatchOfDiv(data);
			}
			handler.error = function(){
				
			}
			if($(this).val()!=-1){
				rest.get(getBatchUrl+$(this).val(),handler,true);
			}else{
				$("#batchSelect").empty();
				 $("#batchSelect").select2().val("").change();
				 $("#batchSelect").select2({data:"",placeholder:"Select Batch"});
			}
		});
		
		$(SEND_TO_TEACHER).on("click",sendToTeacher);
		$(SEND_TO_STU_PAR).on("click",sendToStuNPar);
		/*$("select").select2();*/
		/*$("#examSelect").select2({placeholder:"Select Exam"});*/
	});
	
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
	
	function getTeacherList(){
		 var handler = {};
		 handler.success = function(data){
			 var sData = formatData(data);
			 $('select#teachers').select2({ allowClear: true,data:sData,placeholder: "Select Teacher/Teachers"});
		 }
		 handler.error = function(){
			 
		 }
		 rest.get(getTeacherListUrl,handler,true);
	}
	
	function formatData(data){
		var formatDate = [];
		$.each(data,function(key,val){
			var teacherObj = {};
			teacherObj.id =  val.teacherId;
			teacherObj.text = val.teacherBean.fname+" "+val.teacherBean.lname+" "+val.suffix;
			formatDate.push(teacherObj);
		});
		return formatDate;
	}
	
	function sendToTeacher(){
			$("#teacherNotificationSummary").empty();
			$("#teacherNotificationSummary").hide();
			$(".error").html("");
			var flag = true;
			if($('select#teachers').val() == null){
				$("#teacherSelectError").html("Select Teachers");
				flag = false;
			}
			if($("[name='teacherMessage']").val().trim() == ""){
				$("#teacherMessageError").html("Message cant be empty.");
				flag = false;
			}
			if($(TEACHER_FORM).find('[name="messageType"]:checked').length == 0){
				$("#teacherMessageTypeError").html("Select Message Type");
				flag = false;
			}
			if(flag){
			if(($("[name='teacherMessage']").val().length/160)>1 && $("#sms").is(':checked')){
			modal.modalConfirm("SMS","2 messages will be sent per teacher.Do you want to continue?","Cancel","Send",sendToTeacherConfirm);
			}else{
				sendToTeacherConfirm();
			}
			}
	}
	
	function sendToTeacherConfirm(){
		var notificationObject = new NotificationObject();
		notificationObject.message = $("[name='teacherMessage']").val();
		//Teacher related variable
		notificationObject.messageType = [];
		var messageType = $(TEACHER_FORM).find('[name="messageType"]:checked');
		$.each(messageType,function(key,val){
			notificationObject.messageType.push($(val).val()); //Array
		});
		notificationObject.teacher = $('select#teachers').val();//Array
		
		notificationObject.sendTo = [];//Array
		notificationObject.divisionSelect = "";
		notificationObject.batchSelect = "";
		notificationObject.messageTypeTOST = [];//Array
		
		var handler = {};
		handler.success = function(response){
			var message = "Notification send successfully";
			var type = "success";
			/*if(e.constructor == Array){
				message = e.join("<Br/>");
				type = "warning";
			}
			$.notify({message: message},{type: type});*/
			$("#teacherNotificationSummary").empty();
			$("#teacherNotificationSummary").show();
			if(response.status == null || response.status == ""){
				var totalSMSSent = 0;
				if($("#selectTeacherMessage #sms").is(":checked")){
						totalSMSSent = totalSMSSent + (response.totalTeachers-response.teachersWithoutPhone);			
				}	
				var totalEmailSent = 0;
				if($("#selectTeacherMessage #email").is(":checked")){
						totalEmailSent = totalEmailSent + (response.totalTeachers-response.teachersWithoutEmail);
				}	
				$("#teacherNotificationSummary").append("<div class='row'><div class='col-md-3'>Total teachers selected :"+response.totalTeachers+"</div>" +
						"<div class='col-md-2'>Total SMS Sent:"+totalSMSSent+"</div>" +
						"<div class='col-md-2'>Total Email Sent:"+totalEmailSent+"</div>" +
						"<a data-toggle='collapse' data-target='.a' style='cursor:pointer'>[ view details ]</a></div>");
				if($("#selectTeacherMessage #sms").is(":checked")){
						$("#teacherNotificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent :"+(response.totalTeachers-response.teachersWithoutPhone)+"</div>" +
								"<div class='col-md-4'>Teachers without phone no :"+response.teachersWithoutPhone+"</div></div>");
				}
				if($("#selectTeacherMessage #email").is(":checked")){
						$("#teacherNotificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent :"+(response.totalTeachers-response.teachersWithoutEmail)+"</div>" +
								"<div class='col-md-4'>Teachers without Email ID :"+response.teachersWithoutEmail+"</div></div>");
				}
				/*$.notify({message: "Notification sent successfully"},{type: 'success'});*/
			}else{
				$.notify({message: response.status},{type: 'danger'});
			}
		}
		handler.error = function(e){
			$.notify({message: "Error"},{type: 'danger'});
		}
		rest.post(postnotificationTeacherUrl,handler,JSON.stringify(notificationObject),true);
	}
	
	function sendToStuNPar(){
		$("#studentNotificationSummary").empty();
		$("#studentNotificationSummary").hide();
		$(".error").html("");
		var flag = true;
		if($(STUDENT_N_PARENT_FORM).find('[name="sendTo"]:checked').length == 0){
			$("#sendToStudentORParentError").html("Select recipients")
			flag = false;
		}
		if($("#divisionSelect").val() == "-1"){
			$("#sendToStudentORParentDivisionSelectError").html("Select Class");
			flag = false;
		}
		
		if($("#batchSelect").val() == "-1" || $("#batchSelect").val() == null){
			$("#sendToStudentORParentBatchSelectError").html("Select Batch");
			flag = false;
		}
		
		if($("[name='stuNParMessage']").val().trim() == ""){
			$("#stuNParMessageError").html("Message cant be empty.");
			flag = false;
		}
		
		if($(STUDENT_N_PARENT_FORM).find('[name="messageTypeTOST"]:checked').length == 0){
			$("#messageTypeTOSTError").html("Select Message Type");
			flag = false;
		}
		
		if(flag){
			if(($("[name='stuNParMessage']").val().length/160)>1 && $("#smstostdnparent").is(':checked')){
			modal.modalConfirm("SMS","2 messages will be sent per Student/Parent.Do you want to continue?","Cancel","Send",sendTOStudentNParentConfirm);
			}else{
				sendTOStudentNParentConfirm();
			}
			}		
	}
	
	function sendTOStudentNParentConfirm(){
		var notificationObject = new NotificationObject();
		notificationObject.message = $("[name='stuNParMessage']").val();
		notificationObject.messageTypeTOST = [];
		var messageType = $(STUDENT_N_PARENT_FORM).find('[name="messageTypeTOST"]:checked');
		$.each(messageType,function(key,val){
			notificationObject.messageTypeTOST.push($(val).val()); //Array
		});
		
		
		notificationObject.sendTo = [];//Array
		
		var messageType = $(STUDENT_N_PARENT_FORM).find('[name="sendTo"]:checked');
		$.each(messageType,function(key,val){
			notificationObject.sendTo.push($(val).val()); //Array
		});
		
		notificationObject.divisionSelect = $("#divisionSelect").val();
		notificationObject.batchSelect = $("#batchSelect").val();
	
		var handler = {};
		handler.success = function(response){
			var message = "Notification send successfully";
			var type = "success";
			/*if(e.constructor == Array){
				message = e.join("<Br/>");
				type = "warning";
			}
			$.notify({message: message},{type: type});*/
			$("#studentNotificationSummary").empty();
			$("#studentNotificationSummary").show();
			if(response.status == null || response.status == ""){
				var totalSMSSent = 0;
				if($("#smstostdnparent").is(":checked")){
					if($("#sendToStudent").is(":checked")){
						totalSMSSent = totalSMSSent + (response.totalStudents-response.studentsWithoutPhone);
					}
					if( $("#sendToParent").is(":checked")){
						totalSMSSent = totalSMSSent + (response.totalStudents-response.parentsWithoutPhone);
					}
				}	
				var totalEmailSent = 0;
				if($("#emailtostdnparent").is(":checked")){
					if($("#sendToStudent").is(":checked")){
						totalEmailSent = totalEmailSent + (response.totalStudents-response.studentsWithoutEmail);
					}
					if( $("#sendToParent").is(":checked")){
						totalEmailSent = totalEmailSent + (response.totalStudents-response.parentsWithoutEmail);
					}
				}	
				$("#studentNotificationSummary").append("<div class='row'><div class='col-md-3'>No of students in batch :"+response.totalStudents+"</div>" +
						"<div class='col-md-2'>Total SMS Sent:"+totalSMSSent+"</div>" +
						"<div class='col-md-2'>Total Email Sent:"+totalEmailSent+"</div>" +
						"<a data-toggle='collapse' data-target='.a' style='cursor:pointer'>[ view details ]</a></div>");
				if($("#smstostdnparent").is(":checked")){
					if($("#sendToStudent").is(":checked")){
						$("#studentNotificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of students :"+(response.totalStudents-response.studentsWithoutPhone)+"</div>" +
								"<div class='col-md-4'>Students without phone no :"+response.studentsWithoutPhone+"</div></div>")
					}
					if( $("#sendToParent").is(":checked")){
						$("#studentNotificationSummary").append("<div class='row collapse a'><div class='col-md-4'>SMS sent to No of parents :"+(response.totalStudents-response.parentsWithoutPhone)+"</div>" +
								"<div class='col-md-4'>Parents without phone no :"+response.parentsWithoutPhone+"</div></div>")
					}
				}
				if($("#emailtostdnparent").is(":checked")){
					if($("#sendToStudent").is(":checked")){
						$("#studentNotificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of students :"+(response.totalStudents-response.studentsWithoutEmail)+"</div>" +
								"<div class='col-md-4'>Students without Email ID :"+response.studentsWithoutEmail+"</div></div>")
					}
					if( $("#sendToParent").is(":checked")){
						$("#studentNotificationSummary").append("<div class='row collapse a'><div class='col-md-4'>Email sent to No of parents :"+(response.totalStudents-response.parentsWithoutEmail)+"</div>" +
								"<div class='col-md-4'>Parents without Email ID :"+response.parentsWithoutEmail+"</div></div>")
					}
				}
				/*$.notify({message: "Notification sent successfully"},{type: 'success'});*/
			}else{
				$.notify({message: response.status},{type: 'danger'});
			}
		}
		handler.error = function(e){
			$.notify({message: "Error"},{type: 'danger'});
		}
	
		rest.post(postnotificationUrl,handler,JSON.stringify(notificationObject),true);
	}
	
	function init(){
		$(TEACHER_FORM).validate({ // initialize the plugin
	        rules: {
	            'messageType': {
	                required: true
	                //,maxlength: 2
	            }
	        },
	        messages: {
	            'messageType': {
	                required: "You must send sms,email or push message",
	                maxlength: "Check no more than {0} boxes"
	            }
	        },
	        errorPlacement: function(error, element) {
	            error.insertAfter(element.parent()); // <- the default
	        }
	    });
		
		$.validator.addMethod("valueNotEquals", function(value, element, arg){
			  return arg != value;
			 }, "Please select a value");

		$(STUDENT_N_PARENT_FORM).validate({ // initialize the plugin
	        rules: {
	            'messageType': {
	                required: true
	                //,maxlength: 2
	            },
	            'divisionSelect': {
	            	valueNotEquals: "-1"
	            },
	            'sendTo':{
	                required: true
	                //,maxlength: 2
	            },
	            'messageTypeTOST':{
	                required: true
	                //,maxlength: 2
	            }
	        },
	        messages: {
	            'sendTo': {
	                required: "You must send message to either student or parent",
	                maxlength: "Check no more than {0} boxes"
	            },
	            'messageTypeTOST':{
	            	required: "You must send sms,email or push message",
	                maxlength: "Check no more than {0} boxes"
	            }
	        },
	        errorPlacement: function(error, element) {
	            error.insertAfter(element.parent()); // <- the default
	        }
	    });
	}
	
	function NotificationObject(){
		this.message;
		//Teacher related variable
		this.messageType; //Array
		this.teacher;//Array
		
		//Student and parent variables
		this.sendTo;//Array
		this.divisionSelect;
		this.batchSelect;
		this.messageTypeTOST;//Array
		
	}
})();