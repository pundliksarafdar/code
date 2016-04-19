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
	$("#instituteSelect").change(function(){
		var inst_id = $(this).val();
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		$("#divisionSelect").empty();
		var divisionArray = [];
		var tempData = {};
 		tempData.id = "-1";
 		tempData.text = "Select Class";
 		divisionArray.push(tempData);
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 	 	for(i=0;i<divisionArray.length;i++){
 	 		$("#divisionSelect").append("<option value='"+divisionArray[i].id+"'>"+divisionArray[i].text+"</option>")
 	 	}
	   // $("#division").select2({data:divisionArray,placeholder:"Type Topic Name"});
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);

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
			handler.success = function(e){
				$("#attendanceStudentListDiv").hide();
				$("#attendanceScheduleDiv").show();	
				$.notify({message: "Attendanced updated successfully"},{type: 'success'});
			
			}
			handler.error = function(e){$.notify({message: "Attendanced not updated successfully"},{type: 'danger'});}
			rest.post("rest/teacher/updateStudentAttendance/"+inst_id,handler,JSON.stringify(attendanceArr));
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
	var inst_id = $("#instituteSelect").val();
	if(!divisionId || divisionId.trim()=="" || divisionId == -1){
		$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please select a division');
		$('div#addStudentModal .error').show();
	}else{		
		var handlers = {};
		handlers.success = function(e){console.log("Success",e);
		 $('#batchSelect').empty();
		   var batchDataArray = [];
		    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
		    if(e != null){
		    	$.each(e,function(key,val){
		    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
				});
		    }
		};
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.get("rest/teacher/getBatchesofDivision/"+inst_id+"/"+divisionId,handlers);
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

function getSchedule(){
	var inst_id = $("#instituteSelect").val();
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	createAttendanceScheduleTable(e);
	
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/teacher/getScheduleForUpdateAttendance/"+inst_id+"/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
}
function createAttendanceScheduleTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#attendanceScheduleTable').DataTable({
		bDestroy:true,
		data: data,
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
				return "<div><input type='button' class='btn btn-primary btn-sm markAttendance' value='Update Attendance'><input type='hidden' id='sub_id' value='"+row.sub_id+"'><input type='hidden' id='schedule_id' value='"+row.schedule_id+"'></div>";
			},sWidth:"20%"}
		]
	});
	
}
function getStudents(){
	var inst_id = $("#instituteSelect").val();
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
	rest.get("rest/teacher/getStudentsForAttendanceUpdate/"+inst_id+"/"+division+"/"+batch+"/"+subject_id+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
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