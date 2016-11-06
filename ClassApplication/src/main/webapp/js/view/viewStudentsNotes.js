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
var getBatchUrl = "rest/student/getStudentBatches/";
var getSubjectUrl = "rest/student/getSubject/";
var getNotestable = "rest/student/notes/";

$(document).ready(function(){
	loadClassList();
	
	$("body").on("change",CLASS,loadBatch)
		.on("change",DIVISION,loadBatch)
		.on("change",BATCH,loadSubject)
		.on("change",SUBJECT,getTimeTableData)
		.on("change",'select',function(){
			$(NOTES_CONTAINER).hide();
			$(NOTES_MESSAGE_CONTAINER).show()
		});
		
	
});

	var loadSelect = function(select,classData,type){
		var optionStr = "<option value='-1'>Select "+type+"</option>";
		for(classId in classData){
			if(classData.hasOwnProperty(classId)){
				optionStr = optionStr + "<option value='"+classId+"'>"+classData[classId]+"</option>";
			}
			
		}
		$(select).empty();
		$(select).append(optionStr);
		$(select).select2().val("-1").change();
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
	handler.success = function(data){loadSelect(CLASS,data,"Institute")};
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
	var batchId = $(BATCH).val();
	var subjectId = $(SUBJECT).val()
	
	if(classId==-1 || batchId==-1 || subjectId==-1 ){
		return false;
	}
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
	rest.get(getNotestable+classId+"/"+batchId+"/"+subjectId,handler);
}

function loadSubject(){
	var classId = $(CLASS).val();
	var batchId = $(BATCH).val();
	if(batchId != "-1"){
	var handler = {};
	handler.success = function(data){loadSelect(SUBJECT,data,"Subject")};
	handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
	rest.get(getSubjectUrl+batchId+"/"+classId,handler);
	}else{
		var optionStr = "<option value='-1'>Select Subject</option>";
		$(SUBJECT).empty();
		 $(SUBJECT).append(optionStr);
		 $(SUBJECT).select2().val("-1").change();
	}
}

function showNotesList(data){
	var dataTable = $(NOTES_TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns: [{title:"#",data:null,sWidth:"30px"},
			{ title: "Notes",data:null,render:function(data,event,row){
				var div = '<a noteid='+row.notesid+'>'+row.name+'</a>';
				return div;
			}}
		],
		rowCallback:function(row,data,index){
			$(row).find('a').on("click",function(){
				console.log(data);
				window.open("shownotes?"+"division="+data.divid+"&subject="+data.subid+"&institute="+data.inst_id+"&notesid="+data.notesid,"","width=500, height=500");
			});
		}
	});
	 dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw(); 

}