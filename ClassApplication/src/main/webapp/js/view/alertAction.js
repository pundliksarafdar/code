(function(){
	var getTeacherListUrl = "/rest/classownerservice/teacher";
	var getBatchUrl = "/rest/classownerservice/getBatches/";
	var postnotificationUrl = "rest/notification/send";
	
	var BATCH_SELECT = "#batchSelect";
	var SEND_TO_TEACHER = "#sendMessageToTeacher";
	var TEACHER_FORM = "#selectTeacherMessage";
	var STUDENT_N_PARENT_FORM = "#selectStudentNParentMessage";
	var SEND_TO_STU_PAR = "#sendMessageToStuNPar";
	
	
	$(document).ready(function(){
		init();
		getTeacherList();
		
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
		$("select").select2();
	});
	
	function loadBatchOfDiv(data){
		if(data.length > 0){
		var option = "<option value='-1'>Select batch</option>";
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
		if($(TEACHER_FORM).valid()){
			var notificationObject = new NotificationObject();
			notificationObject.message = $("[name='teacherMessage']").val();
			//Teacher related variable
			notificationObject.messageType = [];
			var messageType = $(TEACHER_FORM).find('[name="messageType"]:checked');
			$.each(messageType,function(key,val){
				notificationObject.messageType.push($(val).val()); //Array
			});
			notificationObject.teacher = $('select#teachers').val();//Array
			
			/*notificationObject.sendTo = [];//Array
			notificationObject.divisionSelect = "";
			notificationObject.batchSelect = "";
			notificationObject.messageTypeTOST = [];//Array
			*/
			var handler = {};
			handler.success = function(e){
				var message = "Notification send successfully";
				var type = "success";
				if(e.constructor == Array){
					message = e.join("<Br/>");
					type = "warning";
				}
				$.notify({message: message},{type: type});
			}
			handler.error = function(e){
				var message = e.responseJSON.join("<Br/>");
				
				$.notify({message: message},{type: 'danger'});
			}
			rest.post(postnotificationUrl,handler,JSON.stringify(notificationObject),true);
		}
	}
	
	function sendToStuNPar(){
		if($(STUDENT_N_PARENT_FORM).valid()){
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
			handler.success = function(e){
				var message = "Notification send successfully";
				var type = "success";
				if(e.constructor == Array){
					message = e.join("<Br/>");
					type = "warning";
				}
				$.notify({message: message},{type: type});
			}
			handler.error = function(e){
				var message = e.responseJSON.join("<Br/>");
				
				$.notify({message: message},{type: 'danger'});
			}
		
			rest.post(postnotificationUrl,handler,JSON.stringify(notificationObject),true);
		}
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