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

$(document).ready(function(){
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
			rest.post("rest/attendance/saveStudentAttendance",handler,JSON.stringify(attendanceArr));
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

function getSchedule(){
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var date = $("#date").val().split("/");
	var handler = {};
	handler.success = function(e){console.log("Success",e);
	createAttendanceScheduleTable(e);
	
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/attendance/getScheduleForAttendance/"+division+"/"+batch+"/"+new Date(date[2],parseInt(date[1])-1,date[0]).getTime(),handler);
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
	var division = $("#divisionSelect").val();
	var batch = $("#batchSelect").val();
	var handler = {};
	handler.success = function(e){console.log("Success",e);	
	createStudentAttendanceTable(e);
	$("#attendanceStudentListDiv").show();
	$("#attendanceScheduleDiv").hide();
	}
	handler.error = function(e){console.log("Error",e)}
	rest.get("rest/attendance/getStudentsForAttendance/"+division+"/"+batch,handler);
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