var VIEW_BTN = "#view";
var CLASS = "#classSelect";
var DIVISION = "#divisionSelect";
var BATCH = "#batchSelect";
var SUBJECT = "#subjectSelect";
var NOTES_DATE = "#notesDate";
var NOTES_CONTAINER = "#notesContainer";
var NOTES_MESSAGE_CONTAINER = "#notesShowMessageContainer";
var NOTES_TABLE = "#notesTable";

var getClassUrl = "rest/student/getClasses";
var getDivisionUrl = "rest/student/getDivision/";
var getBatchUrl = "rest/student/getBatch/";
var getSubjectUrl = "rest/student/getSubject/";
var getNotestable = "rest/student/notes/";
var getOnlineExamList = "rest/student/getOnlineExamList";
var getOnlineExamSubjectListUrl = "rest/student/getOnlineExamSubjectList/";
$(document).ready(function(){
	loadClassList();
	
	$("body").on("change",CLASS,loadDivision)
		.on("change",DIVISION,loadBatch)
		.on("change",BATCH,loadSubject)
		.on("click",VIEW_BTN,getExamList)
		.on("change",'select',function(){
			$(NOTES_CONTAINER).hide();
			$(NOTES_MESSAGE_CONTAINER).show()
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

 
function getExamList(){
	var classId = $(CLASS).val();
	var divId = $(DIVISION).val();
	var batchId = $(BATCH).val();
	var subjectId = $(SUBJECT).val()
	var handler = {};
	handler.success = function(e){
		$(NOTES_CONTAINER).show();
		$(NOTES_MESSAGE_CONTAINER).hide();
		showNotesList(e);
	};
	
	handler.error = function(e){$.notify({message: "Error occured"},{type: 'danger'});
		$(NOTES_CONTAINER).hide();
		$(NOTES_MESSAGE_CONTAINER).show();
	};
	rest.get(getOnlineExamList+"/"+classId+"/"+divId+"/"+batchId,handler);
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

function showNotesList(data){
	var dataTable = $(NOTES_TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Notes",data:"exam_name",render:function(data,event,row){
				var div = '<a examId='+row.exam_id+'>'+row.exam_name+'</a>';
				return div;
			}}
		],
		rowCallback:function(row,data,index){
			$(row).find('a').on("click",function(){
				/*console.log(data);
				window.open("shownotes?"+"division="+data.divid+"&subject="+data.subid+"&institute="+data.classid+"&notesid="+data.notesid,"","width=500, height=500");
				*/
				showSubjectExam($(this).attr("examId"));
			});
		}
	});

}

function showSubjectExam(examId){
	var classId = $(CLASS).val();
	var divId = $(DIVISION).val();
	var batchId = $(BATCH).val();
	
	var handler = {};
	handler.success = function(data){showExamSubjectList(data,examId)};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getOnlineExamSubjectListUrl+classId+"/"+divId+"/"+batchId+"/"+examId,handler);
}

function showExamSubjectList(data,examId){
	var dataTable = $(NOTES_TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [
			{ title: "Subject",data:"sub_name",render:function(data,event,row){
				var div = '<a quePaperId='+row.question_paper_id+'>'+row.sub_name+'</a>';
				return div;
			}}
		],
		rowCallback:function(row,data,index){
			$(row).find('a').on("click",function(){
				/*console.log(data);
				window.open("shownotes?"+"division="+data.divid+"&subject="+data.subid+"&institute="+data.classid+"&notesid="+data.notesid,"","width=500, height=500");
				*/
				var classId = $(CLASS).val();
				var divId = $(DIVISION).val();
				var batchId = $(BATCH).val();
				
				var questionPaperId = $(this).attr("quePaperId");
				var examUrl = "attemptExam?inst_id="+classId+"&exam="+examId+"&batch="+batchId+"&division="+divId+"&question_paper_id="+questionPaperId;
				window.open(examUrl,"","width=500, height=500");
				console.log($(this).attr("quePaperId"));
			});
		}
	});

}