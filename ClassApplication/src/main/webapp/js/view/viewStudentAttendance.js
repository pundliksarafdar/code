var VIEW_BTN = "#viewAttendance";
var INSTITUTE = "#instituteSelect";
var DIVISION = "#divisionSelect";
var BATCH = "#batchSelect";
var SUBJECT = "#subjectSelect";
var NOTES_DATE = "#notesDate";
var NOTES_CONTAINER = "#notesContainer";
var NOTES_MESSAGE_CONTAINER = "#notesShowMessageContainer";
var NOTES_TABLE = "#notesTable";

var getClassUrl = "rest/student/getClasses";
var getDivisionUrl = "rest/student/getDivision/";
var getBatchUrl = "rest/student/getStudentBatches/";
var getSubjectUrl = "rest/student/getSubject/";
var getDailyAttendanceURL = "rest/student/getStudentDailyAttendance/";
var getWeeklyAttendanceURL = "rest/student/getStudentsWeeklyAttendance/";
var getMonthlyAttendanceURL = "rest/student/getStudentsMonthlyAttendance/";

$(document).ready(function(){
	loadClassList();
	$( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	
	$("body").on("change",INSTITUTE,loadBatch)
		.on("change",'select',function(){
			$(NOTES_CONTAINER).hide();
			$(NOTES_MESSAGE_CONTAINER).show()
		});
	
	 $(VIEW_BTN).click(function(){
		 if($("#attendanceType").val()== "1"){
			 $("#dailyAttendance").show();
			 $("#monthlyAttendance").hide();
			 getDailyAttendance();
		 }else if($("#attendanceType").val()== "2"){
			 $("#monthlyAttendance").show();
			 $("#dailyAttendance").hide();
			 getWeeklyAttendance();
		 }else{
			 $("#monthlyAttendance").show();
			 $("#dailyAttendance").hide();
			 getMonthlyAttendance();
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
	
	function loadBatchData(select,batchData){
		var optionStr = "<option value='-1'>Select Batch</option>";
		$(select).empty();
		for(var i=0;i<batchData.length;i++){
				optionStr = optionStr + "<option value='"+batchData[i].batch_id+"'>"+batchData[i].batch_name+"</option>";
		}
		$(select).append(optionStr);
		$(BATCH).select2().val("-1").change();
	}

function loadClassList(){
	var handler = {};
	handler.success = function(data){loadSelect(INSTITUTE,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getClassUrl,handler);
}


function loadBatch(){
	if($(this).val()!= "-1"){
	var handler = {};
	handler.success = function(data){loadBatchData(BATCH,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getBatchUrl+$(this).val(),handler);
	}else{
		var optionStr = "<option value='-1'>Select Batch</option>";
		$(BATCH).empty();
		 $(BATCH).append(optionStr);
		 $(BATCH).select2().val("-1").change();
	}
}


function getDailyAttendance(){
	$(".validation-message").html("");
	var validationFlag = false;
	var inst_id = $(INSTITUTE).val();
	var batch = $(BATCH).val();
	var date = $("#date").val().split("/");
	if(batch == "-1" || batch == null){
		$("#batchError").html("Select Batch!");
		validationFlag=true;
	}
	if(date == ""){
		$("#dateError").html("Select Date!");
		validationFlag=true;
	}
	if(validationFlag ==false){
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	createAttendanceScheduleTable(e);
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get(getDailyAttendanceURL+inst_id+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
	}
	
	function createAttendanceScheduleTable(data){
		//data = JSON.parse(data);
		var i=0;
		var dataTable = $('#attendanceScheduleTable');
		$('#attendanceScheduleTable').empty();
		var htmlString = "";
		htmlString = htmlString + "<thead><tr><th>Roll No</th><th>Student Name</th>";
		if(data.length>0){
		for(i=0;i<data[0].dailyTimeTableList.length;i++){
				htmlString = htmlString + "<th>"+data[0].dailyTimeTableList[i].start_time+"-"+data[0].dailyTimeTableList[i].end_time+"</th>";
		}
		}
		htmlString = htmlString + "<th>Present lectures</th><th>Total lectures</th><th>Average</th></tr></thead><tbody>";
		if(data.length>0){
		for(i=0;i<data.length;i++){
			if(data[i].roll_no == 0){
				htmlString = htmlString + "<tr><td>-</td>";
			}else{
				htmlString = htmlString + "<tr><td>"+data[i].roll_no+"</td>";
			}
			htmlString = htmlString + "<td>"+data[i].student_name+"</td>";
			for(j=0;j<data[i].presentee.length;j++){
				if(data[i].presentee[j] == "P"){
				htmlString = htmlString + "<td style='color:green'>"+data[i].presentee[j]+"</td>";
				}else{
					htmlString = htmlString + "<td style='color:red'>"+data[i].presentee[j]+"</td>";
				}
			}
			htmlString = htmlString + "<td>"+data[i].present_lectures+"</td><td>"+data[i].total_lectures+"</td><td>"+data[i].avg+"%</td>";
			htmlString = htmlString + "</tr>";
		}
		}
		htmlString = htmlString + "</tbody>";
		$('#attendanceScheduleTable').append(htmlString);
		$('#attendanceScheduleTable').DataTable();
		
	}
}


function getWeeklyAttendance(){
	$(".validation-message").html("");
	var validationFlag = false;
	var inst_id = $(INSTITUTE).val();
	var batch = $(BATCH).val();
	var date = $("#date").val().split("/");
	if(batch == "-1" || batch == null){
		$("#batchError").html("Select Batch!");
		validationFlag=true;
	}
	if(date == ""){
		$("#dateError").html("Select Date!");
		validationFlag=true;
	}
	if(validationFlag ==false){
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	var dates = new Date(date[2],parseInt(date[1])-1,date[0]), y = dates.getFullYear(), m = dates.getMonth();
	//var curr = new Date; // get current date
	var first = dates.getDate() - dates.getDay(); // First day is the day of the month - the day of the week
	var last = first + 6; // last day is the first day + 6
	var weekDays = []
	for (i=0;i<7;i++){
		var firstday = new Date(dates.setDate(first+i));
		console.log(firstday);
		weekDays.push(firstday);
	}

	createWeeklyAttendanceScheduleTable(e,weekDays);
	
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get(getWeeklyAttendanceURL+inst_id+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
	}
}

function createWeeklyAttendanceScheduleTable(data,weekDays){
	$('#monthlyAttendance').empty();
	var presenteeDates = [];
	
	var hString = "<table class='table-bordered' style='width:100%'><thead><tr><th>Roll No</th><th>Student Name</th>";
	for(i=0;i<=6;i++){
		hString = hString + "<th>"+weekDays[i].getDate()+"</th>";
	}
	hString = hString + "<th>Present lectures</th><th>Total lectures</th><th>Average</th></tr></thead><tbody>";
	if(data.length>0){
	for(var k=0;k<data[0].monthlyCountList.length;k++){
		presenteeDates.push(data[0].monthlyCountList[k].date);
	}
	console.log(presenteeDates);
	for(i=0;i<data.length;i++){
		if(data[i].roll_no == 0){
			hString = hString + "<tr><td>-</td>";
		}else{
			hString = hString + "<tr><td>"+data[i].roll_no+"</td>";
		}
		hString = hString + "<td>"+data[i].student_name+"</td>";
		var counter = 0;
		for(j=0;j<presenteeDates.length;j++){
			while(counter<=6){
			if(new Date(presenteeDates[j]).getDate() == weekDays[counter].getDate()){
				hString = hString + "<td style='color:green'>"+data[i].daily_presentee_percentange[j]+"</td>";
				counter++;
				break;
			}else{
				hString = hString + "<td style='color:red'>-</td>";
			}
			counter++;
			}
		}
		while(counter<=6){
			hString = hString + "<td style='color:red'>-</td>";
			counter++;
		}
		hString = hString + "<td style='color:red'>"+data[i].total_monthly_presentee+"</td>";
		hString = hString + "<td style='color:red'>"+data[i].total_monthly_lectures+"</td>";
		hString = hString + "<td style='color:red'>"+data[i].total_prsentee_percentage+"</td>";
		hString = hString + "</tr>";
	}
	}
	hString = hString + "</tbody></table>";
	$('#monthlyAttendance').append(hString);
	$('#monthlyAttendance').find('table').DataTable();
	
}


function getMonthlyAttendance(){
	$(".validation-message").html("");
	var validationFlag = false;
	var inst_id = $(INSTITUTE).val();
	var batch = $(BATCH).val();
	var date = $("#date").val().split("/");
	if(batch == "-1" || batch == null){
		$("#batchError").html("Select Batch!");
		validationFlag=true;
	}
	if(date == ""){
		$("#dateError").html("Select Date!");
		validationFlag=true;
	}
	if(validationFlag ==false){
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	var dates = new Date(date[2],parseInt(date[1])-1,date[0]), y = dates.getFullYear(), m = dates.getMonth();
	var firstDay = new Date(y, m, 1);
	var lastDay = new Date(y, m + 1, 0);
	console.log(firstDay);
	console.log(lastDay);
	createMonthlyAttendanceScheduleTable(e,firstDay.getDate(),lastDay.getDate());
	
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get(getMonthlyAttendanceURL+inst_id+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
	}
}

function createMonthlyAttendanceScheduleTable(data,start,end){
	$('#monthlyAttendance').empty();
	var presenteeDates = [];
	
	var hString = "<table class='table-bordered' cellspacing='0' style='width:100%'><thead><tr><th>Roll No</th><th>Student Name</th>";
	for(i=start;i<=end;i++){
		hString = hString + "<th>"+i+"</th>";
	}
	hString = hString + "<th>Present lectures</th><th>Total lectures</th><th>Average</th></tr></thead><tbody>";
	if(data.length>0){
	for(var k=0;k<data[0].monthlyCountList.length;k++){
		presenteeDates.push(data[0].monthlyCountList[k].date);
	}
	console.log(presenteeDates);
	for(i=0;i<data.length;i++){
		if(data[i].roll_no == 0){
			hString = hString + "<tr><td>-</td>";
		}else{
			hString = hString + "<tr><td>"+data[i].roll_no+"</td>";
		}
		hString = hString + "<td>"+data[i].student_name+"</td>";
		var counter = start;
		for(j=0;j<presenteeDates.length;j++){
			while(counter<=end){
			if(new Date(presenteeDates[j]).getDate() == counter){
				hString = hString + "<td style='color:green'>"+data[i].daily_presentee_percentange[j]+"</td>";
				counter++;
				break;
			}else{
				hString = hString + "<td style='color:red'>-</td>";
			}
			counter++;
			}
		}
		while(counter<=end){
			hString = hString + "<td style='color:red'>-</td>";
			counter++;
		}
		hString = hString + "<td style='color:red'>"+data[i].total_monthly_presentee+"</td>";
		hString = hString + "<td style='color:red'>"+data[i].total_monthly_lectures+"</td>";
		hString = hString + "<td style='color:red'>"+data[i].total_prsentee_percentage+"</td>";
		hString = hString + "</tr>";
	}
	}
	hString = hString + "</tbody></table>";
	$('#monthlyAttendance').append(hString);
	$('#monthlyAttendance').find("table").DataTable({
       
        scrollX:        true,
        scrollCollapse: true,
        paging:         true,
        "pageLength": 50,
        fixedColumns:   {
            leftColumns: 2,
            rightColumns: 3
        }
    } );
	
}