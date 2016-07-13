var VIEW_BTN = "#view";
var CLASS = "#classSelect";
var DIVISION = "#divisionSelect";
var BATCH = "#batchSelect";
var SUBJECT = "#subjectSelect";
var NOTES_DATE = "#notesDate";
var NOTES_CONTAINER = "#notesContainer";
var NOTES_MESSAGE_CONTAINER = "#notesShowMessageContainer";
var EXAM_MARKS_TABLE = "#notesTable";
var EXAM_MARKS_BY_EXAM = "#examMarksByExamTable";
var BACK_TO_LIST = "#showExamList";

var getClassUrl = "rest/student/getClasses";
var getDivisionUrl = "rest/student/getDivision/";
var getBatchUrl = "rest/student/getStudentBatches/";
var getSubjectUrl = "rest/student/getSubject/";
var getOnlineExamList = "rest/student/studentMarks";
var studentMarksByExamUrl = "rest/student/studentMarksByExam/";
$(document).ready(function(){
	loadClassList();
	
	$("body").on("change",CLASS,loadBatch)
		.on("change",DIVISION,loadBatch)
		.on("click",VIEW_BTN,getExamList)
		.on("change",'select',function(){
			$(NOTES_CONTAINER).hide();
			$(NOTES_MESSAGE_CONTAINER).show()
		})
		.on("click",BACK_TO_LIST,showExamList);
		
	$("#examMarksByExamTableWrap").hide();
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
		var optionStr = "";
		for(var i=0;i<batchData.length;i++){
				optionStr = optionStr + "<option value='"+batchData[i].batch_id+"'>"+batchData[i].batch_name+"</option>";
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


function loadBatch(){
	var handler = {};
	handler.success = function(data){loadBatchData(BATCH,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getBatchUrl+$(this).val(),handler);
}

function loadDivision(){
	var handler = {};
	handler.success = function(data){loadSelect(DIVISION,data)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getDivisionUrl+$(this).val(),handler);
}

 
function getExamList(){
	$("#examMarksByExamTableWrap").hide();
	var classId = $(CLASS).val();
	var batchId = $(BATCH).val();
	var subjectId = $(SUBJECT).val()
	var handler = {};
	handler.success = function(e){
		if(e.length){
			$(NOTES_CONTAINER).show();
			$("#marksGraph").empty();
			var chart = new Morris.Bar({
				barGap:1,
				barSize:30,
				barSizeRatio:0.5,
				hideHover:true,
				  element: 'marksGraph',
				  data: e,
				  xkey: 'examName',
				  ykeys: ['examMarks'],
				  labels: ['Marks']
				});
			$(NOTES_MESSAGE_CONTAINER).hide();
			showNotesList(e);
			chart.on("click",function(e,data){
				$("#notes").hide();
				showSubjectExamMarks(data.examId);
			});
		}else{
			$(NOTES_MESSAGE_CONTAINER).show().html("No data to display");
			
		}
	};
	
	handler.error = function(e){$.notify({message: "Error occured"},{type: 'danger'});
		$(NOTES_CONTAINER).hide();
		$(NOTES_MESSAGE_CONTAINER).show();
	};
	rest.get(getOnlineExamList+"/"+classId+"/"+batchId,handler);
}

function showNotesList(data){
	var dataTable = $(EXAM_MARKS_TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Exams",data:"examName",render:function(data,event,row){
				var div = '<a examId='+row.examId+'>'+row.examName+'</a>';
				return div;
			}},{title: "Marks",data:"examMarks"}
		],
		rowCallback:function(row,data,index){
			$(row).find('a').on("click",function(){
				$("#notes").hide();
				showSubjectExamMarks($(this).attr("examId"));
			});
		}
	});

}

function showSubjectExamMarks(examId){
	var classId = $(CLASS).val();
	var batchId = $(BATCH).val();
	
	var handler = {};
	handler.success = function(data){showExamSubjectList(data,examId)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(studentMarksByExamUrl+classId+"/"+batchId+"/"+examId,handler);
}

function showExamSubjectList(data){
	
	$("#examMarksByExamTableWrap").show();
	$("#marksBySubjectGraph").empty();
	var chart = new Morris.Bar({
		barSize:30,
		barSizeRatio:0.5,
		hideHover:true,
		  element: 'marksBySubjectGraph',
		  data: data,
		  xkey: 'subjectName',
		  ykeys: ['marks'],
		  labels: ['Subject']
		});
	
	var dataTable = $(EXAM_MARKS_BY_EXAM).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Subject name",data:"subjectName"},{title: "Marks",data:"marks"}
		]});

}

function showExamList(){
	$("#notes").show();
	$("#examMarksByExamTableWrap").hide();
}