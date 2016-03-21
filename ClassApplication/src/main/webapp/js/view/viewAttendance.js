/*****All urls******/
var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var getAllBatchStudentsFeesUrl = "rest/feesservice/getAllBatchStudentsFees/";
var subject_id = "";
/*************/
var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var optionSelect = {
		onText:"Present",
		offText:"Absent",
		onColor:"success",
		offColor:"danger"
	};

$(document).ready(function(){
	
	$('#testTable').DataTable();
	$("body").on("change",DIVISION_SELECT,getBatches)
		.on("change",BATCH_SELECT,loadStudentTable);
	 $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	 
	 $("#searchLectures").click(function(){
		 if($("#attendanceType").val()== "1"){
			 $("#dailyAttendance").show();
			 $("#monthlyAttendance").hide();
		 getSchedule();
		 }else if($("#attendanceType").val()== "2"){
			 $("#monthlyAttendance").show();
			 $("#dailyAttendance").hide();
			 getWeeklySchedule();
		 }else{
			 $("#monthlyAttendance").show();
			 $("#dailyAttendance").hide();
			 getMonthlySchedule();
		 }
	 });
	 $('#attendanceScheduleTable').on("click",".markAttendance",function(){
		 subject_id = $(this).closest("div").find("#sub_id").val();
		 getStudents();
	 });
	 
	 $(".backtoSchedule").click(function(){
		 $("#attendanceStudentListDiv").hide();
			$("#attendanceScheduleDiv").show();
	 });
	 $(".saveAttendance").click(function(){
		 //$("#attendanceStudentListDiv").hide();
		//	$("#attendanceScheduleDiv").show();
			var division = $("#divisionSelect").val();
			var batch = $("#batchSelect").val();
			var date = $("#date").val().split("/");
			var attendanceArr = [];
			for(var i=0 ; i<$(".presentee").length;i++){
				attendance={};
				attendance.sub_id = subject_id;
				attendance.div_id = division;
				attendance.batch_id = batch;
				attendance.student_id = $($(".presentee")[i]).closest(".presenteeDiv").find("#student_id").val();
				attendance.att_id = $($(".presentee")[i]).closest(".presenteeDiv").find("#att_id").val();
				if($(".presentee")[i].checked){ 
				attendance.presentee = "P";
				}else{
					attendance.presentee = "A";
				}
				attendance.att_date = new Date(date[2],parseInt(date[1])-1,date[0]);
				attendanceArr.push(attendance);
			}
			console.log(attendanceArr);
			var handler = {};
			handler.success = function(e){console.log("Success",e);
			
			}
			handler.error = function(e){console.log("Error",e)}
			rest.post("rest/attendance/updateStudentAttendance",handler,JSON.stringify(attendanceArr));
	 });
	 
	 $('#myInput').on( 'keyup', function () {
		 var table = $('#attendanceStudentListTable').DataTable();
		 var searchText = this.value.split(",").join("|");
    table.search( searchText, true, false ).draw();
} );
	
});

function getBatches(){
	$(".chkBatch:checked").removeAttr('checked');
	$('#checkboxes').children().remove();
	$('div#addStudentModal .error').hide();
	var divisionId = $('#divisionSelect').val();

	if(!divisionId || divisionId.trim()=="" || divisionId == -1){
		$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please select a division');
		$('div#addStudentModal .error').show();
	}else{		
	  $.ajax({
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "fetchBatchesForDivision",
			 regId:'',
			 divisionId:divisionId,						 
	   		},
	   type:"POST",
	   success:function(e){
		   $('#batchSelect').empty();
		   var batchDataArray = [];
		    var data = JSON.parse(e);
		   /* $.each(data.batches,function(key,val){
				var data = {};
				data.id = val.batch_id;
				data.text = val.batch_name;
				batchDataArray.push(data);
			});
		    $("#batchSelect").select({data:batchDataArray,placeholder:"type batch name"});*/
		    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
		    if(data.batches != null){
		    	$.each(data.batches,function(key,val){
		    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
				});
		    }
	   	},
	   error:function(e){
		   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
			$('div#addStudentModal .error').show();
	   }
	   
});
	
}
}


function getBatchError(error){
}

function loadStudentTable(data){
	var batchId = $(BATCH_SELECT).val();
	var divId = $(DIVISION_SELECT).val();
	var handler = {};
	handler.succes = loadStudentTableSuccess;
	handler.error = loadStudentTableError;
	rest.get(getAllBatchStudentsFeesUrl+divId+"/"+batchId,handler);
}

function loadStudentTableSuccess(data){
	console.log(data);
}

function loadStudentTableError(){
	
}

function getWeeklySchedule(){
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
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
	rest.get("rest/attendance/getStudentsWeeklyAttendance/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
}

function getMonthlySchedule(){
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
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
	rest.get("rest/attendance/getStudentsMonthlyAttendance/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
}

function getSchedule(){
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	createAttendanceScheduleTable(e);
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/attendance/getStudentsDailyAttendance/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
}
function createMonthlyAttendanceScheduleTable(data,start,end){
	$('#attendanceStudentListTable').empty();
	var presenteeDates = [];
	
	var hString = "<thead><tr><th>Student Name</th>";
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
		hString = hString + "<tr><td>"+data[i].student_name+"</td>";
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
	hString = hString + "</tbody>";
	$('#attendanceStudentListTable').append(hString);
	$('#attendanceStudentListTable').DataTable();
	
}

function createWeeklyAttendanceScheduleTable(data,weekDays){
	$('#attendanceStudentListTable').empty();
	var presenteeDates = [];
	
	var hString = "<thead><tr><th>Student Name</th>";
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
		hString = hString + "<tr><td>"+data[i].student_name+"</td>";
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
	hString = hString + "</tbody>";
	$('#attendanceStudentListTable').append(hString);
	$('#attendanceStudentListTable').DataTable();
	
}

function createAttendanceScheduleTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#attendanceScheduleTable');
	$('#attendanceScheduleTable').empty();
	var htmlString = "";
	htmlString = htmlString + "<thead><tr><th>Student Name</th>";
	if(data.length>0){
	for(i=0;i<data[0].dailyTimeTableList.length;i++){
			htmlString = htmlString + "<th>"+data[0].dailyTimeTableList[i].start_time+"-"+data[0].dailyTimeTableList[i].end_time+"</th>";
	}
	}
	htmlString = htmlString + "<th>Present lectures</th><th>Total lectures</th><th>Average</th></tr></thead><tbody>";
	if(data.length>0){
	for(i=0;i<data.length;i++){
		htmlString = htmlString + "<tr><td>"+data[i].student_name+"</td>";
		for(j=0;j<data[j].presentee.length;j++){
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
function getStudents(){
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
	var handler = {};
	handler.success = function(e){console.log("Success",e);	
	createStudentAttendanceTable(e);
	$("#attendanceStudentListDiv").show();
	$("#attendanceScheduleDiv").hide();
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/attendance/getStudentsForAttendanceUpdate/"+division+"/"+batch+"/"+subject_id+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
}

function createStudentAttendanceTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#attendanceStudentListTable').DataTable({
		bDestroy:true,
		data: data,
		paging: false,
		lengthChange: false,
		columns: [
		          { title: "Roll No",data:null,render:function(data,event,row){
		        	  return row.roll_no;
		          },sWidth:"10%"},
			{ title: "Student",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.fname+" "+row.lname+'</div>';
				return div;
			},sWidth:"50%"},
			{ title: "Attendance",data:null,render:function(data,event,row){
										if (row.presentee == "P") {
											return "<div class='presenteeDiv'><input type='checkbox' data-size=\"mini\"/ class='presentee' checked><input type='hidden' value='"
													+ row.student_id
													+ "' id='student_id'><input type='hidden' value='"
													+ row.att_id
													+ "' id='att_id'></div>"
										} else {
											return "<div class='presenteeDiv'><input type='checkbox' data-size=\"mini\"/ class='presentee'><input type='hidden' value='"
													+ row.student_id
													+ "' id='student_id'><input type='hidden' value='"
													+ row.att_id
													+ "' id='att_id'></div>"
										}
			}
				
			,swidth:'30%'
			}
		]
	});
	$("#attendanceStudentListTable input[type=\"checkbox\"]").bootstrapSwitch(optionSelect);
}