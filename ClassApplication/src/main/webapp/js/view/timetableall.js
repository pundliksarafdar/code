var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var START_DATE = "#startDate";
var END_DATE = "#endDate";
var START_TIME = "#startTime";
var END_TIME = "#endTime";
var SUBJECT_SELECT = "#subjectSelect";
var TEACHER_SELECT = "#teacherSelect";
var REPETITION_SELECT = "#repetitionSelect";
var SAVE_SCHEDULE = "#buttons #save";
var SCHEDULE_FORM = "#scheduleForm";
var REP_SEL = "#repetitionSelect";

var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var getTimetable = "rest/schedule/getScheduleForMonth/";
var saveScheduleUrl = "rest/schedule/schedule";

var timtableData,
	calendar;
$(document).ready(function(){
	$("body").on("change",DIVISION_SELECT,getBatches)
		.on("click",'[data-event-id]',editTimeTableFunction)
		.on("change",BATCH_SELECT,getTimeTableData)
		.on("change",SUBJECT_SELECT,subjectSelectChange)
		.on("click",SAVE_SCHEDULE,saveSchedule);
		
		$('.btn-group button[data-calendar-view]').each(function() {
			var $this = $(this);
			$this.click(function() {
				calendar.view($this.data('calendar-view'));
			});
		});
		
		$(START_DATE).datetimepicker({
			pickTime: false,
			format:"YYYY-MM-DD"
		});
		$(END_DATE).datetimepicker({
			pickTime: false,
			format:"YYYY-MM-DD"
		});
		$(START_TIME).datetimepicker({
			pickDate: false,
			format:"HH:mm:ss"
		});
		$(END_TIME).datetimepicker({
			pickDate: false,
			format:"HH:mm:ss"
		});
		
		 $.validator.addMethod("valueNotEquals", function(value, element, arg){
		  return arg != value;
		 }, "Value must not equal arg.");
		 
		 $.validator.addMethod("endDateRequired", function(value, element, arg){
		  return ($(REP_SEL).find('[type="checkbox"]:checked').length == 0 || value.length > 0);
		 }, "Recurrence is set plz select end date");
		 
		 $(SCHEDULE_FORM).validate({
		  rules: {
		   divisionSelect: { valueNotEquals: "-1" },
		   batchSelect:{ valueNotEquals: "-1" },
		   subjectSelect:{ valueNotEquals: "-1" },
		   teacherSelect:{ valueNotEquals: "-1" },
			endDate:{endDateRequired:true}
		  },
		  messages: {
		   divisionSelect: { valueNotEquals: "Please select an division!" },
		   batchSelect: { valueNotEquals: "Please select an batch!" },
		   subjectSelect: { valueNotEquals: "Please select an subject!" },
		   teacherSelect: { valueNotEquals: "Please select an teacher!" }
		  }  
		 });
});

Date.prototype.format = function(){
	var that = this;
	return that.getDate()+"-"+(that.getMonth()+1)+"-"+that.getFullYear()+" "+that.getHours()+":"+that.getMinutes();
}
function getBatches(){
	var handler = {}
	var division = $(this).val();
	handler.success = getBatchSuccess;
	handler.error = getBatchError;
	rest.get(getBatchListUrl+division,handler);
}

function getBatchSuccess(batches){
	$(BATCH_SELECT).find('option').not('option[value="-1"]').remove();
	var optionsHasLink = "";
	var optionsNoLink = "";
	var optGroupHasLink;
	var optGroupNoLink;
	$.each(batches,function(index,val){
		if(val.feesLinkStatus == "Yes"){
			optionsHasLink = optionsHasLink + "<option value='"+val.batch.batch_id+"'>"+val.batch.batch_name+"</option>";
		}else{
			optionsNoLink = optionsNoLink + "<option value='"+val.batch.batch_id+"'>"+val.batch.batch_name+"</option>";
		}
	});
	
	if(optionsHasLink && optionsHasLink.trim().length){
		optGroupHasLink = '<optgroup label="Linked" linked="yes">'+optionsHasLink+'</optgroup>'
	}
	
	if(optionsNoLink && optionsNoLink.trim().length){
		optGroupNoLink = '<optgroup label="No link" linked="no">'+optionsNoLink+'</optgroup>';
	}
	$(BATCH_SELECT).append(optGroupHasLink+optGroupNoLink);	
}

function getBatchError(error){
}

function getTimeTableData(){
	var divId = $(DIVISION_SELECT).val();
	var batchId = $(BATCH_SELECT).val();
	var dateNow = new Date().getTime();
	var handler = {};
	handler.success = function(e){setTimetable(e)};
	handler.error = function(e){console.log(e)};
	rest.get(getTimetable+batchId+"/"+divId+"/"+dateNow+"/0",handler);
	filldropdown();
}

function setTimetable(data){
	timtableData = data;
	var options = {
		events_source: data,
		view: 'month',
		tmpl_path: 'tmpls/',
		tmpl_cache: false,
		day: 'now',
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
		classes: {
			months: {
				general: 'label'
			}
		}
	};

	calendar = $('#calendar').calendar(options);
	
	}

function onEdit(event){
		console.log(event);
		$(SUBJECT_SELECT).val(event.subId);
}	
function editTimeTableFunction(){
	var scheduleId = $(this).attr('timetableData');
}

function filldropdown()
{
	  var batchName = $(BATCH_SELECT).val();
	  var batchdivision=$(DIVISION_SELECT).val();
	  var reg;
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
			   var options = "";
					for(index=0;index<subjectnames.length;index++){
						var subjectname = subjectnames[index];
						var subjectId = subjectids[index];
						options = options+"<option value="+subjectId+">"+subjectname+"</option>";
					}
				$(SUBJECT_SELECT).find('option').not('option[value="-1"]').remove();	
				$(SUBJECT_SELECT).append(options);
			   }
		     
		   	},
		   error:function(data){
			   alert("error");
		   }
	});
}

function subjectSelectChange()
  {
	  var subname = $(SUBJECT_SELECT).val();
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
	               }else{
	            	   modal.launchAlert("Teacher","Teacher Not Available For This Subject");
	               }
			   	},
			   error:function(data){
				   alert("Teacher Not Available");
			   }
		});
  }
  
  function saveSchedule(){
	  var scheduleBean = new ScheduleBean();
		scheduleBean.div_id = $(DIVISION_SELECT).val();
		scheduleBean.batch_id = $(BATCH_SELECT).val();
		scheduleBean.sub_id = $(SUBJECT_SELECT).val();
		scheduleBean.teacher_id = $(TEACHER_SELECT).val();
		scheduleBean.start_time = $(START_TIME).find('input').val();
		scheduleBean.end_time = $(END_TIME).find('input').val();
		scheduleBean.date = $(START_DATE).find('input').val();
		var repeatation = $(REPETITION_SELECT).find('input[type="checkbox"]:checked');
		var repetations = [];
		for(var index=0;index<repeatation.length;index++){
			repetations.push($(repeatation[index]).val());
		}
		scheduleBean.rep_days = repetations.join(",");
		scheduleBean.start_date  = $(START_DATE).find('input').val();
		scheduleBean.end_date = $(END_DATE).find('input').val();
		
		var handler = {};
		handler.success = function(e){console.log(e)
			$.notify({message: "Schedule saved successfully"},{type: 'success'});
			getTimeTableData();
		}
		handler.error = function(e){console.log(e)}
		if($(SCHEDULE_FORM).valid()){
			rest.post(saveScheduleUrl,handler,JSON.stringify(scheduleBean));
		}
  }
  
  function validate(){
	  
  }
  
  function ScheduleBean(){
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