/*****All urls******/
var getBatchListUrl = "rest/feesservice/getInstituteBatch/";
var getAllBatchStudentsFeesUrl = "rest/feesservice/getAllBatchStudentsFees/";
var subject_id = "";
var schedule_id = "";
var subjectThat = "";
/*************/
var DIVISION_SELECT = "#divisionSelect";
var BATCH_SELECT = "#batchSelect";
var optionSelect = {
		onText:"Present",
		offText:"Absent",
		onColor:"success",
		offColor:"danger"
	};
var teacherSubjectArray = [];
var divisionTempData = {};
divisionTempData.id = "-1";
divisionTempData.text = "Select Class";
var batchTempData = {};
batchTempData.id = "-1";
batchTempData.text = "Select Batch";

$(document).ready(function(){
	$("#instituteSelect").change(function(){
		$("#attendanceStudentListDiv").hide();
		$("#attendanceScheduleDiv").hide();
		var divisionArray = [];
		var batchArray = [];
		divisionArray.push(divisionTempData);
		batchArray.push(batchTempData);
		$("#divisionSelect").empty();
		$("#batchSelect").empty();
		 $("#divisionSelect").select2({data:divisionArray});
		 $("#batchSelect").select2({data:batchArray});
		 
		var inst_id = $(this).val();
		if(inst_id != "-1"){
		$("#instituteError").html("");
		var handler = {};
		handler.success = function(e){
			if(e.divisionList.length > 0){
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 	 $("#divisionSelect").select2({data:divisionArray});
 	 	teacherSubjectArray = e.subjectList;
		}else{
			$("#divisionSelect").empty();
 	 		$("#divisionSelect").select2({data:"",placeholder:"Class not available"});	
		}
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}

	});
	$("body").on("change",DIVISION_SELECT,getBatches)
		.on("change",BATCH_SELECT,loadStudentTable);
	 $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	 
	 $("#searchLectures").click(function(){
		 getSchedule();
	 });
	 $('#attendanceScheduleTable').on("click",".markAttendance",function(){
		 subject_id = $(this).closest("div").find("#sub_id").val();
		 schedule_id= $(this).closest("div").find("#schedule_id").val();
		 subjectThat = $(this);
		 getStudents();
	 });
	 
	 $(".backtoSchedule").click(function(){
		 $("#attendanceStudentListDiv").hide();
			$("#attendanceScheduleDiv").show();
	 });
	 $(".saveAttendance").click(function(){
		 //$("#attendanceStudentListDiv").hide();
		//	$("#attendanceScheduleDiv").show();
		 var inst_id = $("#instituteSelect").val();
			var division = $("#divisionSelect").val();
			var batch = $("#batchSelect").val();
			var date = $("#date").val().split("/");
			var attendanceArr = [];
			for(var i=0 ; i<$(".presentee").length;i++){
				attendance={};
				attendance.schedule_id = schedule_id;
				attendance.sub_id = subject_id;
				attendance.div_id = division;
				attendance.batch_id = batch;
				attendance.student_id = $($(".presentee")[i]).closest(".presenteeDiv").find("#student_id").val();
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
			handler.success = function(e){
				subjectThat.prop("disabled",true);
				subjectThat.closest("tr").find(".attendanceStatus").html("Yes");
				$("#attendanceStudentListDiv").hide();
				$("#attendanceScheduleDiv").show();
				$.notify({message: "Attendanced saved successfully"},{type: 'success'});
			
			}
			handler.error = function(e){
				$.notify({message: "Attendanced not saved successfully"},{type: 'danger'});
				}
			rest.post("rest/teacher/saveStudentAttendance/"+inst_id,handler,JSON.stringify(attendanceArr));
	 });
	 
	 $('#myInput').on( 'keyup', function () {
		 var table = $('#attendanceStudentListTable').DataTable();
		 var searchText = this.value.split(",").join("|");
    table.search( searchText, true, false ).draw();
} );
	
});

function getBatches(){
	$("#attendanceStudentListDiv").hide();
	$("#attendanceScheduleDiv").hide();
	var batchArray = [];
	batchArray.push(batchTempData);
	$("#batchSelect").empty();
	$("#batchSelect").select2({data:batchArray});
	 
	var divisionId = $('#divisionSelect').val();
	var inst_id = $("#instituteSelect").val();
	if(!divisionId || divisionId.trim()=="" || divisionId == "-1"){
		
	}else{		
		$("#divisionError").html("");
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		   var batchDataArray = [];
		    if(e != null){
		    	if(e.length > 0){
		    	$.each(e,function(key,val){
		    		var data = {};
					data.id = val.batch_id;
					data.text = val.batch_name;
					batchArray.push(data);
				});
		    	$("#batchSelect").select2({data:batchArray});
		    	}else{
		    		batchArray = [];
		    		$("#batchSelect").empty();
		    		$("#batchSelect").select2({data:batchArray,placeholder:"Batch not available"});
		    	}
		    }
		};
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.get("rest/teacher/getBatchesofDivision/"+inst_id+"/"+divisionId,handlers);
}
}


function getBatchError(error){
}

function loadStudentTable(data){
	$("#attendanceStudentListDiv").hide();
	$("#attendanceScheduleDiv").hide();
	var batchId = $(BATCH_SELECT).val();
	if(batchId != "-1"){
		$("#batchError").html("");
	}
	/*var divId = $(DIVISION_SELECT).val();
	var handler = {};
	handler.succes = loadStudentTableSuccess;
	handler.error = loadStudentTableError;
	rest.get(getAllBatchStudentsFeesUrl+divId+"/"+batchId,handler);*/
}

function loadStudentTableSuccess(data){
	console.log(data);
}

function loadStudentTableError(){
	
}

function getSchedule(){
	$("#attendanceStudentListDiv").hide();
	$("#attendanceScheduleDiv").hide();
	$(".validation-message").html("");
	var validationFlag = false;
	var inst_id = $("#instituteSelect").val();
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
	if(inst_id == "-1"){
		$("#instituteError").html("Select Institute");
		validationFlag = true;
	}
	if(division == "-1" || division == "" || division == null){
		$("#divisionError").html("Select Class");
		validationFlag = true;
	}
	if(batch == "-1" || batch == "" || batch == null){
		$("#batchError").html("Select Batch");
		validationFlag = true;
	}
	if(date.length < 3){
		$("#dateError").html("Select Date");
		validationFlag = true;
	}
	if(!validationFlag){
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	createAttendanceScheduleTable(e);
	$("#attendanceScheduleDiv").show();
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/teacher/getScheduleForAttendance/"+inst_id+"/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
	}
}
function createAttendanceScheduleTable(data){
	//data = JSON.parse(data);
	var attendanceData = [];
	 $.each(data,function(key,val){
	 		$.each(teacherSubjectArray,function(innerKey,innerVal){
	 			if(val.sub_id == innerVal.subjectId){
	 				attendanceData.push(val);
	 			return false;
	 			}
	 		});
			});
	var i=0;
	var dataTable = $('#attendanceScheduleTable').DataTable({
		bDestroy:true,
		data: attendanceData,
		lengthChange: false,
		columns: [
			{ title: "Teacher",data:null,render:function(data,event,row){
				var div = '<div class="default defaultBatchName">'+row.teacher+'</div>';
				return div;
			},sWidth:"30%"},
			{ title: "Subject",data:null,render:function(data,event,row){
				return row.sub_name;
			},sWidth:"20%"},
			{ title: "Time",data:null,render:function(data,event,row){
				return row.start_time+" - "+row.end_time;
			},sWidth:"20%"},
			{ title: "Attendance",data:null,render:function(data,event,row){
				if(row.attendanceStatus == true){
				return "<div><input type='button' class='btn btn-primary btn-sm markAttendance' value='Mark Attendance' disabled></div>";
				}else{
					return "<div><input type='button' class='btn btn-primary btn-sm markAttendance' value='Mark Attendance'><input type='hidden' id='sub_id' value='"+row.sub_id+"'><input type='hidden' id='schedule_id' value='"+row.schedule_id+"'></div>";	
				}	
			},sWidth:"20%"},
			{ title: "Attendance Filled",data:null,render:function(data,event,row){
				if(row.attendanceStatus == true){
				return "<div class='attendanceStatus'>Yes</div>";
				}else{
					return "<div class='attendanceStatus'>No</div>";
				}
			},sWidth:"20%"}
		]
	});
	
}
function getStudents(){
	var inst_id = $("#instituteSelect").val();
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var handler = {};
	handler.success = function(e){console.log("Success",e);	
	createStudentAttendanceTable(e);
	$("#attendanceStudentListDiv").show();
	$("#attendanceScheduleDiv").hide();
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/teacher/getStudentsForAttendance/"+inst_id+"/"+division+"/"+batch,handler);
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
				return "<div class='presenteeDiv'><input type='checkbox' data-size=\"mini\"/ class='presentee' checked><input type='hidden' value='"+row.student_id+"' id='student_id'></div>"}
			,swidth:'30%'
			}
		]
	});
	$("#attendanceStudentListTable input[type=\"checkbox\"]").bootstrapSwitch(optionSelect);
}