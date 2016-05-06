var VIEW_BTN = "#view";
var CLASS = "#classSelect";
var DIVISION = "#divisionSelect";
var BATCH = "#batchSelect";
var SUBJECT = "#subjectSelect";
var CALENDAR_DATE = "#calendarDate";
var CALENDAR_CONTAINER = "#calendarContainer";
var CALENDAR_MESSAGE_CONTAINER = "#calendarShowMessageContainer";

var getClassUrl = "rest/student/getClasses";
var getDivisionUrl = "rest/student/getDivision/";
var getBatchUrl = "rest/student/getBatch/";
var getSubjectUrl = "rest/student/getSubject/";
var getTimetable = "rest/student/monthlySchedule/";

$(document).ready(function(){
	loadClassList();
	
	$("body").on("click",VIEW_BTN,viewTimetable)
		.on("change",CLASS,loadDivision)
		.on("change",DIVISION,loadBatch)
		.on("change",BATCH,loadSubject)
		.on("dp.change",CALENDAR_DATE,onCalendarDateChange)
		.on("click",VIEW_BTN,getTimeTableData)
		.on("change",'select',function(){
			$(CALENDAR_CONTAINER).hide();
			$(CALENDAR_MESSAGE_CONTAINER).show()
		});
		
		$(CALENDAR_DATE).datetimepicker({
			pickTime: false,
			minViewMode:'months',
			format:"YYYY-MM",
			date:'now'
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
});

	var loadSelect = function(select,classData){
		var optionStr = "";
		for(classId in classData){
			if(classData.hasOwnProperty(classId)){
				optionStr = optionStr + "<option value='"+classId+"'>"+classData[classId]+"</option>";
			}
			
		}
		$(select).find("option:not([value='-1'])").remove();
		$(select).append(optionStr);
	}

function loadClassList(){
	var handler = {};
	handler.success = function(data){loadSelect(CLASS,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getClassUrl,handler);
}

function viewTimetable(){
	
}

function loadBatch(){
	var handler = {};
	handler.success = function(data){loadSelect(BATCH,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getBatchUrl+$(this).val(),handler);
}

function loadDivision(){
	var handler = {};
	handler.success = function(data){loadSelect(DIVISION,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getDivisionUrl+$(this).val(),handler);
}

function onCalendarDateChange(){
	getTimeTableData();
} 
function getTimeTableData(){
	var classId = $(CLASS).val();
	var divId = $(DIVISION).val();
	var batchId = $(BATCH).val();
	var dateNow = new Date($(CALENDAR_DATE).find('input').val()).getTime();
	if(isNaN(dateNow)){
		dateNow = new Date().getTime();
	}
	var handler = {};
	handler.success = function(e){
		$(CALENDAR_CONTAINER).show();
		$(CALENDAR_MESSAGE_CONTAINER).hide();
		setTimetable(e);
	};
	
	handler.error = function(e){$.notify({message: "Error occured"},{type: 'danger'});
		$(CALENDAR_CONTAINER).hide();
		$(CALENDAR_MESSAGE_CONTAINER).show();
	};
	rest.get(getTimetable+classId+"/"+batchId+"/"+divId+"/"+dateNow+"/0",handler);
	//filldropdown();
}

function loadSubject(){
	var classId = $(CLASS).val();
	var divId = $(DIVISION).val();
	var batchId = $(BATCH).val();
	
	var handler = {};
	handler.success = function(data){loadSelect(SUBJECT,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getSubjectUrl+divId+"/"+batchId+"/"+classId,handler);
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
		classes: {
			months: {
				general: 'label'
			}
		}
	};

	calendar = $('#calendar').calendar(options);
	
	}