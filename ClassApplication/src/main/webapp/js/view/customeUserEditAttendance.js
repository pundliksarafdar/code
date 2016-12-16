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
	$("body").on("change",DIVISION_SELECT,getBatches)
		.on("change",BATCH_SELECT,loadStudentTable);
	 $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  format: 'DD/MM/YYYY',
		  maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
	  }).data("DateTimePicker");
	 
	 $("#searchLectures").click(function(){
		 getSchedule();
	 });
	 $('#attendanceScheduleTable').on("click",".markAttendance",function(){
		 $("#commonAttendanceDiv").hide();
		 subject_id = $(this).closest("div").find("#sub_id").val();
		 var schedule_id = $(this).closest("div").find("#schedule_id").val();
		 getStudents(schedule_id);
	 });
	 $(".comonMarkAttendance").click(function(){
		 $("#commonAttendanceDiv").hide();
		 subject_id = -1;
		 schedule_id = -1;
		 getStudentsForCommonUpdate();
	 });
	 
	 $(".backtoSchedule").click(function(){
		 $("#attendanceStudentListDiv").hide();
			$("#attendanceScheduleDiv").show();
			$("#commonAttendanceDiv").show();
	 });
	 $(".saveAttendance").click(function(){
		 //$("#attendanceStudentListDiv").hide();
		//	$("#attendanceScheduleDiv").show();
		 var table = $('#attendanceStudentListTable').DataTable();
		 table.search( '' ).columns().search( '' ).draw();
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
			var commonHandler = {};
			commonHandler.success = function(e){
				$("#attendanceStudentListDiv").hide();
				$("#attendanceScheduleDiv").show();
				$("#commonAttendanceDiv").show();
				$.notify({message: "Attendanced updated successfully"},{type: 'success'});
			
			}
			commonHandler.error = function(e){
				$.notify({message: "Attendanced not updated successfully"},{type: 'danger'});
				}
			
			if(schedule_id == -1){
				rest.post("rest/customuserservice/updateCommonDayStudentAttendance",commonHandler,JSON.stringify(attendanceArr));	
				}else{
					rest.post("rest/customuserservice/updateStudentAttendance",handler,JSON.stringify(attendanceArr));
				}
	 });
	 
	 $('#myInput').on( 'keyup', function () {
		 var table = $('#attendanceStudentListTable').DataTable();
		 var searchText = this.value.split(",").join("|");
    table.search( searchText, true, false ).draw();
} );
	
});

function getBatches(){
	var divisionId = $('#divisionSelect').val();
	$("#batchSelect").val("-1")
	$("#batchSelect").find('option:gt(0)').remove();
	$("#attendanceScheduleDiv").hide();
	$("#attendanceStudentListDiv").hide();
	 $("#commonAttendanceDiv").hide();
	if(divisionId != "-1"){		
	$("#divisionError").html("");
	var handlers = {};
	handlers.success=function(data){
		$('#batchSelect').empty();
		var batchDataArray = [];
		   if(data.length > 0){
			    $("#batchSelect").append("<option value='-1'>Select Batch</option>");
			    	 $("#batchSelect").select2().val("-1").change();
			    	$.each(data,function(key,val){
			    		 $("#batchSelect").append("<option value='"+val.batch_id+"'>"+val.batch_name+"</option>");
					});
			    }else{
			    	 $("#batchSelect").empty();
					 $("#batchSelect").select2().val("").change();
					 $("#batchSelect").select2({data:"",placeholder:"Batch not available"});
			    }
	};
	handlers.error=function(){
		$.notify({message: "Error"},{type: 'danger'});
	};   
	
	rest.get("rest/customuserservice/getBatches/"+divisionId,handlers);
	
}else{
	$("#batchSelect").empty();
	 $("#batchSelect").select2().val("").change();
	 $("#batchSelect").select2({data:"",placeholder:"Select Batch"});
}
}


function getBatchError(error){
}

function loadStudentTable(data){
	var batchId = $(BATCH_SELECT).val();
	$("#attendanceScheduleDiv").hide();
	$("#attendanceStudentListDiv").hide();
	 $("#commonAttendanceDiv").hide();
	if(batchId != "-1"){
		$("#batchError").html("");
	}
	/*var handler = {};
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
	$(".validation-message").html("");
	var validationFlag = false;
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
	if(division == "-1"){
		$("#divisionError").html("Select Division!");
		validationFlag=true;
	}
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
	rest.get("rest/customuserservice/getScheduleForUpdateAttendance/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
	}
}
function createAttendanceScheduleTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#attendanceScheduleTable').DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [{title:"#",data:null},
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
	dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); 
	$("#attendanceScheduleDiv").show();
	$("#commonAttendanceDiv").show();
}
function getStudents(schedule_id){
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
	rest.get("rest/customuserservice/getStudentsForAttendanceUpdate/"+division+"/"+batch+"/"+subject_id+"/"+schedule_id+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
}

function getStudentsForCommonUpdate(){
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var handler = {};
	handler.success = function(e){console.log("Success",e);	
	createCommonStudentAttendanceTable(e);
	$("#attendanceStudentListDiv").show();
	$("#attendanceScheduleDiv").hide();
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/customuserservice/getStudentsForAttendance/"+division+"/"+batch,handler);
}

function createCommonStudentAttendanceTable(data){
	//data = JSON.parse(data);
	var i=0;
	var dataTable = $('#attendanceStudentListTable').DataTable({
		bDestroy:true,
		data: data,
		paging: false,
		lengthChange: false,
		columns: [
		          { title: "Roll No",data:null,render:function(data,event,row){
		        	  if(row.roll_no == 0){
		        		  return "-";  
		        	  }else{
		        	  return row.roll_no;
		        	  }
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
		        	  if(row.roll_no == 0){
		        	  return "-"; 
		        	  }else{
		        	  return row.roll_no;
		        	  }
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