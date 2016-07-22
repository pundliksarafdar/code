var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var START_DATE = "#startDate";
var END_DATE = "#endDate";
var START_TIME = "#startTime";
var END_TIME = "#endTime";
var CALENDAR_DATE = "#calendarDate";
var SUBJECT_SELECT = "#subjectSelect";
var TEACHER_SELECT = "#teacherSelect";
var REPETITION_SELECT = "#repetitionSelect";
var SAVE_SCHEDULE = "#buttons #save";
var EDIT_SCHEDULE = "#buttons #edit";
var RESET_SCHEDULE = "#buttons #reset";
var SCHEDULE_FORM = "#scheduleForm";
var REP_SEL = "#repetitionSelect";
var CALENDAR_CONTAINER = "#calendarContainer";

var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var getTimetable = "rest/schedule/getScheduleForMonth/";
var saveScheduleUrl = "rest/schedule/schedule";
var old_date;
var timtableData,
	calendar,
	validatorForm;

$(document).ready(function(){
	$(CALENDAR_CONTAINER).hide();
	$("body").on("change",DIVISION_SELECT,getBatches)
		.on("click",'[data-event-id]',editTimeTableFunction)
		.on("change",BATCH_SELECT,batchChange)
		.on("change",SUBJECT_SELECT,subjectSelectChange)
		.on("click",SAVE_SCHEDULE,saveSchedule)
		.on("click",EDIT_SCHEDULE,editSchedule)
		.on("click",RESET_SCHEDULE,resetSchedule)
		.on("dp.change",CALENDAR_DATE,onCalendarDateChange)
		.on("mouseleave","#addModifyTimetableForm",function(){
			$("#addModifyTimetableForm").tooltip("hide");
		})
		.on("mouseenter","#addModifyTimetableForm",function(){
			var isEdit = $(SAVE_SCHEDULE).hasClass("hide");
			if(isEdit){
				$("#addModifyTimetableForm").tooltip("show");
			}
			
		});
		
		$('.btn-group button[data-calendar-view]').click(function() {
			var viewCalendar = $(this).data('calendar-view');
			calendar.view(viewCalendar);
			
			if(viewCalendar=="day" || viewCalendar=="week"){
				$(CALENDAR_DATE).find('input').val("");
				$(CALENDAR_DATE).data("DateTimePicker").destroy();
				$(CALENDAR_DATE).datetimepicker({
					pickTime: false,
					minViewMode:'days',
					format:"YYYY-MM-DD"
				});
			}else if(viewCalendar=="month"){
				$(CALENDAR_DATE).find('input').val("");
				$(CALENDAR_DATE).data("DateTimePicker").destroy();
				$(CALENDAR_DATE).datetimepicker({
					pickTime: false,
					minViewMode:'months',
					format:"YYYY-MM"
				});
			}
			
		});
		
		$(START_DATE).datetimepicker({
			pickTime: false,
			format:"YYYY-MM-DD"
		}).data("DateTimePicker").setDate(getNextDate());
		
		$(END_DATE).datetimepicker({
			pickTime: false,
			useCurrent:true,
			format:"YYYY-MM-DD"
		}).data("DateTimePicker").setDate(getNextDate($(START_DATE).data("DateTimePicker").getDate().toDate()));
		
		$(START_TIME).datetimepicker({
			pickDate: false,
			format:"HH:mm"
		}).data("DateTimePicker").setDate(getNextDate());
		
		$(END_TIME).datetimepicker({
			pickDate: false,
			format:"HH:mm"
		}).data("DateTimePicker").setDate(getNextDate($(START_TIME).data("DateTimePicker").getDate().toDate()));;
		
		$(CALENDAR_DATE).datetimepicker({
			pickTime: false,
			minViewMode:'months',
			format:"YYYY-MM"
		});
		
		markTodaysDayOfWeek();
		
		 $.validator.addMethod("valueNotEquals", function(value, element, arg){
			 if(arg.constructor == Array){
				 return (arg.indexOf(value)==-1);
			 }
			 return arg != value;
		 }, "Value must not equal arg.");
		 
		 $.validator.addMethod("endDateRequired", function(value, element, arg){
		  return ($(REP_SEL).find('[type="checkbox"]:checked').length == 0 || value.length > 0);
		 }, "Recurrence is set plz select end date");
		 
		 validatorForm = $(SCHEDULE_FORM).validate({
		  rules: {
		   divisionSelect: { valueNotEquals: "-1"},
		   batchSelect:{ valueNotEquals: ["-1",null,""] },
		   subjectSelect:{ valueNotEquals: ["-1",null,""] },
		   teacherSelect:{ valueNotEquals: ["-1",null,""] },
			endDate:{endDateRequired:true}
		  },
		  messages: {
		   divisionSelect: { valueNotEquals: "Please select an division!" },
		   batchSelect: { valueNotEquals: "Please select an batch!" },
		   subjectSelect: { valueNotEquals: "Please select an subject!" },
		   teacherSelect: { valueNotEquals: "Please select an teacher!" }
		  }  
		 });
		 
		 $(START_DATE).on("dp.change",function(){
			 $(END_DATE).data("DateTimePicker").setDate(getNextDate($(START_DATE).data("DateTimePicker").getDate().toDate()));
			 markTodaysDayOfWeek($(START_DATE).data("DateTimePicker").getDate().toDate());
		 });
		 
		 $(START_TIME).on("dp.change",function(){
			 $(END_TIME).data("DateTimePicker").setDate(getNextDate($(START_TIME).data("DateTimePicker").getDate().toDate())); 
		 });
		 
		 $("#repeat").on("init.bootstrapSwitch switchChange.bootstrapSwitch ",function(event, state){
			 if(state){
				 $(".repeatDependant").show();
			 }else{
				 $(".repeatDependant").hide();
			 }
		 }).bootstrapSwitch();
});

Date.prototype.format = function(){
	var that = this;
	return that.getDate()+"-"+(that.getMonth()+1)+"-"+that.getFullYear()+" "+that.getHours()+":"+that.getMinutes();
}

function onCalendarDateChange(){
	getTimeTableData();
}
function editSchedule(){
	var editRole = $(EDIT_SCHEDULE).attr("data-edit-roll");
	var event = $(EDIT_SCHEDULE).data("eventData")
	var scheduleBean = new ScheduleBean();
		
	if(editRole == "all"){
		scheduleBean.grp_id = event.grp_id;
	}else{
		scheduleBean.schedule_id = (event.id).split("/")[0];
	}
		scheduleBean.div_id = event.divId;
		scheduleBean.batch_id = event.batchId;
		scheduleBean.sub_id = $(SUBJECT_SELECT).val();
		scheduleBean.teacher_id = $(TEACHER_SELECT).val();
		scheduleBean.start_time = $(START_TIME).find('input').val().length<=5?$(START_TIME).find('input').val()+":00":$(START_TIME).find('input').val();
		scheduleBean.end_time = $(END_TIME).find('input').val().length<=5?$(END_TIME).find('input').val()+":00":$(END_TIME).find('input').val();
		scheduleBean.date = $(START_DATE).find('input').val();
		scheduleBean.old_date = old_date;
		var repeatation = $(REPETITION_SELECT).find('input[type="checkbox"]:checked');
		var repetations = [];
		for(var index=0;index<repeatation.length;index++){
			repetations.push($(repeatation[index]).val());
		}
		var state = $("#repeat").bootstrapSwitch('state');
		if(state){
			scheduleBean.rep_days = repetations.join(",");
		}
		scheduleBean.start_date  = $(START_DATE).find('input').val();
		scheduleBean.end_date = $(END_DATE).find('input').val();
		
		var handler = {};
		handler.success = function(e){
			$.notify({message: "Schedule saved successfully"},{type: 'success'});
			getTimeTableData();
		}
		handler.error = function(e){
			$.notify({message: e.message},{type: 'danger'});
		}
		if($(SCHEDULE_FORM).valid()){
			rest.put(saveScheduleUrl,handler,JSON.stringify(scheduleBean));
			
		}
}

function resetSchedule(){
	$(SUBJECT_SELECT).val(-1);
	$(TEACHER_SELECT).val(-1);
	$(SAVE_SCHEDULE).removeClass('hide');
	$(EDIT_SCHEDULE).addClass('hide');
	$(RESET_SCHEDULE).addClass('hide');
	setTimeout(function(){
		$(SCHEDULE_FORM).find('label.error').remove();
		$(SCHEDULE_FORM).find('.error').removeClass('error');	
		validatorForm.resetForm();
	},1000);
	
	
	
}

function getBatches(){
	$(BATCH_SELECT).empty();
	var handler = {}
	var division = $(this).val();
	if(division>-1){
		handler.success = getBatchSuccess;
		handler.error = getBatchError;
		rest.get(getBatchListUrl+division,handler);
	}else{
		var options = "<option value='-1'>Select Batch</option>";
		$(BATCH_SELECT).append(options);	
		$(BATCH_SELECT).select2().val("-1").change();
		$(CALENDAR_CONTAINER).hide();
	}
}

function getBatchSuccess(batches){
	if(batches.length >0){
	var options = "<option value='-1'>Select Batch</option>";
	$.each(batches,function(index,val){
			options = options + "<option value='"+val.batch.batch_id+"'>"+val.batch.batch_name+"</option>";		
	});
	$(BATCH_SELECT).append(options);
	$(BATCH_SELECT).select2().val("-1").change();
	}else{
		$(BATCH_SELECT).empty();
		$(BATCH_SELECT).select2().val("").change();
		$(BATCH_SELECT).select2({data:"",placeholder:"Batch not available"});
	}
}

function getBatchError(error){
}

function batchChange(){
	$(SUBJECT_SELECT).select2().val("-1").change();
	getTimeTableData();
}
function getTimeTableData(){
	var divId = $(DIVISION_SELECT).val();
	var batchId = $(BATCH_SELECT).val();
	var dateNow = new Date($(CALENDAR_DATE).find('input').val()).getTime();
	if(isNaN(dateNow)){
		dateNow = new Date().getTime();
	}
	var handler = {};
	handler.success = function(e){setTimetable(e)};
	handler.error = function(e){$(CALENDAR_CONTAINER).hide();};
	if(batchId != "-1" && batchId!=null && batchId != ""){
	rest.get(getTimetable+batchId+"/"+divId+"/"+dateNow+"/0",handler);
	}
	filldropdown();
}

function setTimetable(data){
	var dateTime = $(CALENDAR_DATE).find('input').val();
	if(dateTime.trim().length == 0){
		dateTime = "now";
	}else if(dateTime.trim().length<=7){
		dateTime = dateTime+"-01";
	}
	$(CALENDAR_CONTAINER).show();
	var view = $('.btn-group button[data-calendar-view].active').data('calendar-view');
	timtableData = data;
	var options = {
		events_source: data,
		view: view,
		tmpl_path: 'tmpls/',
		tmpl_cache: false,
		day: dateTime,
		modal:"#events-modal",
		onAfterEventsLoad: function(events) {
			if(!events) {
				return;
			}
			var list = $('.event-list');
			list.html('');

			$.each(events, function(key, val) {
				$(document.createElement('li'))
					.html('<a href="' + val.url + '">' + val.title + '</a>')
					.appendTo(list);
			});
		},
		onAfterViewLoad: function(view) {
			$('.page-header h3').text(this.getTitle());
			$('.btn-group button').removeClass('active');
			$('button[data-calendar-view="' + view + '"]').addClass('active');
		},
		onCorexEditBtnClicked:function(event){
			onEdit(event);	
		},
		onCorexDeleteBtnClicked:function(event){
			var handler = {};
			handler.success = function(){getTimeTableData();resetSchedule()};
			handler.error = function(){getTimeTableData();resetSchedule()};
			
			if(event.grp_id){
				modal.modalYesAndNo("Delete","Do you want to delete?","Current",function(){
					rest.deleteItem(saveScheduleUrl+"/"+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val()+"/"+event.id+"/"+event.date+"/0",handler);
				},"All",function(){
					rest.deleteItem(saveScheduleUrl+"/"+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val()+"/0/"+event.date+"/"+event.grp_id,handler);
				});
			}else{
				modal.modalConfirm("Delete","Do you want to delete?","No","Yes",function(){
					rest.deleteItem(saveScheduleUrl+"/"+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val()+"/"+event.id+"/"+event.date+"/0",handler);
				},undefined);
			}	
		},
		classes: {
			months: {
				general: 'label'
			}
		}
	};

	calendar = $('#calendar').calendar(options);
	
	}

function onEdit(event){
		$("#repeat").bootstrapSwitch('state',false,false);
		$(SAVE_SCHEDULE).addClass('hide');
		$(EDIT_SCHEDULE).removeClass('hide');
		$(RESET_SCHEDULE).removeClass('hide');
		$(EDIT_SCHEDULE).data("eventData",event);
		var startTime = moment(event.start).format("HH:mm:ss");
		var endTime = moment(event.end).format("HH:mm:ss");
		$(START_DATE).find('input').val( moment(event.date).format("YYYY-MM-DD"));
		old_date = moment(event.date).format("YYYY-MM-DD");
		if(event.grp_id){
			modal.modalConfirm("Timetable", "Do you want to update schedule for all upcomming event", "Current", "All",loadAllUpcommingEvent,[event]);
		}
		$(EDIT_SCHEDULE).attr("data-edit-roll","specific");
		$(SUBJECT_SELECT).val(event.subId).trigger("change",[event]);
		$(START_TIME).find('input').val(startTime);
		$(END_TIME).find('input').val(endTime);
		
		$("#addModifyTimetableForm").tooltip("show");
		$("#divisionSelect").focus();
}	

function loadAllUpcommingEvent(event){
		$("#repeat").bootstrapSwitch('state',true,false);
		$(EDIT_SCHEDULE).attr("data-edit-roll","all");
		if(event.grp_id){
			if(event.rep_days){
				$("#repetitionSelect").find("input").removeAttr("checked");
				var repArr = event.rep_days.split(",");
				for(index=0;index<repArr.length;index++){
					$("#repetitionSelect").find("input[value='"+repArr[index]+"']").prop("checked",true);	
				}
			}
		}
		var startDate = moment(event.start_date).format("YYYY-MM-DD");
		var startTime = moment(event.start_date).format("HH:mm:ss");
		var endTime = moment(event.end).format("HH:mm:ss");
		var endDate = moment(event.end_date).format("YYYY-MM-DD");
		$(SUBJECT_SELECT).val(event.subId);
		$(START_DATE).find('input').val(startDate);
		$(START_TIME).find('input').val(startTime);
		$(END_DATE).find('input').val(endDate);
		$(END_TIME).find('input').val(endTime);
		
}

function editTimeTableFunction(){
	var scheduleId = $(this).attr('timetableData');
}

function filldropdown()
{	
	$(SUBJECT_SELECT).empty();
	  var batchName = $(BATCH_SELECT).val();
	  var batchdivision=$(DIVISION_SELECT).val();
	  var reg;
	  if(batchName != "-1" && batchName!=null && batchName != ""){
	  $.ajax({
		   url: "classOwnerServlet",
		    data: {
		    	 	methodToCall: "fetchBatchSubject",
		    		 batchName:batchName,
		    		 batchdivision:batchdivision
		   		}, 
		   type:"POST",
		   success:function(data){
			   var resultJson = JSON.parse(data);
			   var subjectstatus=resultJson.subjectstatus;
			   if(subjectstatus==""){
			   var subjectnames= resultJson.Batchsubjects.split(",");
			   var subjectids= resultJson.BatchsubjectsIds.split(",");
			   var options = "<option value='-1'>Select Subject</option>";
					for(index=0;index<subjectnames.length;index++){
						var subjectname = subjectnames[index];
						var subjectId = subjectids[index];
						options = options+"<option value="+subjectId+">"+subjectname+"</option>";
					}
				$(SUBJECT_SELECT).find('option').not('option[value="-1"]').remove();	
				$(SUBJECT_SELECT).append(options);
				$(SUBJECT_SELECT).select2().val("-1").change();
			   }else{
					$(SUBJECT_SELECT).empty();
					$(SUBJECT_SELECT).select2().val("").change();
					$(SUBJECT_SELECT).select2({data:"",placeholder:"Subject not available"});
			   }
		     
		   	},
		   error:function(data){
			   alert("error");
		   }
	});
	  }else{
		  	var options = "<option value='-1'>Select Subject</option>";
		  	$(SUBJECT_SELECT).append(options);
			$(SUBJECT_SELECT).select2().val("-1").change();
	  }
}

function subjectSelectChange(e,eventSelected)
  {
	$(TEACHER_SELECT).empty();
	console.log(eventSelected && eventSelected.teacher_id ?eventSelected.teacher_id:"-1");
	  var subname = $(SUBJECT_SELECT).val();
	  if(subname != "-1" && subname != null && subname != ""){
		  var reg;
		  $.ajax({
			   url: "classOwnerServlet",
			    data: {
			    	 methodToCall: "fetchSubjectTeacher",
			    	 subname:subname
			   		}, 
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   var firstname= resultJson.firstname.split(",");
				   var lastname= resultJson.lastname.split(",");
				   var teacherid= resultJson.teacherid.split(",");
				   var suffix=resultJson.suffix.split(",");
				      var counter=1;
				   var sell1Select = $(TEACHER_SELECT);
	               if(sell1Select.prop) { 
	                  var sell1Options = sell1Select.prop("options"); 
	                } else { 
	                  var sell1Options = sell1Select.attr("options"); 
	                       } 
	               $("option", sell1Select).remove();  
	               
	               sell1Options[0]= new Option("Select Teacher", "-1");
	               
	               var limit=firstname.length;
	               if(teacherid[0]!=""){
	               for(var i=0;i<limit;i++)
	            	   {
	            	   if(suffix[i]==null){
	            		   suffix[i]="";
	            	   }
	            	   sell1Options[i+1]= new Option(firstname[i]+" "+lastname[i]+" "+suffix[i], teacherid[i]);
	            	   }
	               $(TEACHER_SELECT).select2().val("-1").change();
	               if(eventSelected && eventSelected.teacher_id) {
	            	   $(TEACHER_SELECT).select2().val(eventSelected.teacher_id).change();
	               }
	               }else{
	            	   $(TEACHER_SELECT).empty();
					   $(TEACHER_SELECT).select2().val("").change();
					   $(TEACHER_SELECT).select2({data:"",placeholder:"Teacher not available"});
	               }
			   	},
			   error:function(data){
				   alert("Teacher Not Available");
			   }
		});
	  }else{
		  var options = "<option value='-1'>Select Teacher</option>";
		  	$(TEACHER_SELECT).append(options);
			$(TEACHER_SELECT).select2().val("-1").change();
	  }
  }
  
  function saveSchedule(){
	  var scheduleBean = new ScheduleBean();
		scheduleBean.div_id = $(DIVISION_SELECT).val();
		scheduleBean.batch_id = $(BATCH_SELECT).val();
		scheduleBean.sub_id = $(SUBJECT_SELECT).val();
		scheduleBean.teacher_id = $(TEACHER_SELECT).val();
		scheduleBean.start_time = $(START_TIME).find('input').val()+":00";
		scheduleBean.end_time = $(END_TIME).find('input').val()+":00";
		scheduleBean.date = $(START_DATE).find('input').val();
		var repeatation = $(REPETITION_SELECT).find('input[type="checkbox"]:checked');
		
		/*
		 * As set repetation or not is depend  on the repetation so checkboxes ie rep_days parameter
		 * So if repetation is desabled clear the checkboxes if checked
		 */
		
		var state = $("#repeat").bootstrapSwitch('state');
		if(!state){
			repeatation = [];
		}
		var repetations = [];
		for(var index=0;index<repeatation.length;index++){
			repetations.push($(repeatation[index]).val());
		}
		scheduleBean.rep_days = repetations.join(",");
		scheduleBean.start_date  = $(START_DATE).find('input').val();
		scheduleBean.end_date = $(END_DATE).find('input').val();
		
		var handler = {};
		handler.success = function(e){
			$.notify({message: "Schedule saved successfully"},{type: 'success'});
			getTimeTableData();
		}
		handler.error = function(e){
		$.notify({message: e.responseJSON.message},{type: 'danger'});
		}
		if($(SCHEDULE_FORM).valid()){
			rest.post(saveScheduleUrl,handler,JSON.stringify(scheduleBean));
		}
  }
  
  function validate(){
	  
  }
  
  function getNextDate(date){
	  var min = 30;
	  
	  if(date){
		  if(date.getMinutes()>=30)	  min = 60;
	  }else if((new Date()).getMinutes()>30){
		  min = 60;
	  }
	  if(date){
		  return new Date(date.setMinutes(min)); 
	  }else{
		  return new Date((new Date).setMinutes(min));
	  }
	  
  }
  
  function markTodaysDayOfWeek(date){
	  var dayOfWeek;
	  if(date){
		  dayOfWeek = date.getDay()+1;
	  }else{
		  dayOfWeek = new Date().getDay()+1;
		  
	  }
	  $('[type="checkbox"]').prop("checked",false);
	  $('[type="checkbox"][value='+dayOfWeek+']').prop("checked",true);
  }
  
  function ScheduleBean(){
	this.grp_id;
	this.schedule_id;	
    this.div_id;
    this.batch_id;
    this.sub_id;
    this.teacher_id;
    this.start_time;
    this.end_time;
    this.date;
    this.rep_days;
    this.start_date;
    this.end_date;
  }