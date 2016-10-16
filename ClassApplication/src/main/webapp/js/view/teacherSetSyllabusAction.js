var allbatches="";
var globalnotesrowcounter=0;
var noofrows=0;
var deletedrows=[];
var SUBJECT_DROPDOWN = ".subjectDropDown";	
var BATCH_DROPDOWN = ".batchDropDown";
var ADD_BUTTON = "#classownerUploadexamAddExam";
var teacherSubjectArray = [];
var divisionTempData = {};
divisionTempData.id = "-1";
divisionTempData.text = "Select Class";
var subjectTempData = {};
subjectTempData.id = "-1";
subjectTempData.text = "Select Subject";
var batchTempData = {};
batchTempData.id = "-1";
batchTempData.text = "Select Batch";

var CALENDAR_DATE = "#syllabusSetTime";
var TEACHER_SYLLABUS_TABLE = "#teacher-syllabus-planner-table";
var TEACHER_SYLLABUS_TABLE_HEADING = TEACHER_SYLLABUS_TABLE+"-heading";
var SAVE = "#saveNewSyllabus";
var SYLLABUS_SEARCH_MONTH = "#syllabusSearchMonth";

var saveSyllabusUrl = "rest/syllabus";
$(document).ready(function(){
	var uploadExam = new UploadExam();
	$(SAVE).on("click",uploadExam.saveSyllabus);
	$("#instituteSelect").change(function(){
		var divisionArray = [];
		var subjectArray = [];
		var batchArray = [];
		divisionArray.push(divisionTempData);
		subjectArray.push(subjectTempData);
		batchArray.push(batchTempData);
		$("#divisionSelect").empty();
		$("#subjectSelect").empty();
		$("#batch").empty();
		 $("#divisionSelect").select2({data:divisionArray,placeholder:"Type Class Name"});
		$("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
		 $("#batch").select2({data:null,placeholder:"Type Batch Name"});
		 
		var inst_id = $(this).val();
		if(inst_id != "-1"){
		$("#instituteError").html("");
		var handler = {};
		handler.success = function(e){
		console.log("Success",e);
		var divisionArray = [];
		if(e.divisionList.length > 0){
 	 $.each(e.divisionList,function(key,val){
			var data = {};
			data.id = val.divId;
			data.text = val.divisionName+" "+val.stream;
			divisionArray.push(data);
		});
 		teacherSubjectArray = e.subjectList;
	    $("#divisionSelect").select2({data:divisionArray,placeholder:"Type Topic Name"});
		}else{
			$("#divisionSelect").empty();
 	 		$("#divisionSelect").select2({data:"",placeholder:"Class not available"});	
		}
		}
		handler.error = function(e){console.log("Error",e)};
		rest.get("rest/teacher/getDivisionAndSubjects/"+inst_id,handler);
		}
	});
	
	$("select").on("change",function(e){
		$(".alert-danger").hide();
	});
	
	 $("#divisionSelect").on("change",function(e){
		 var subjectArray = [];
			var batchArray = [];
			subjectArray.push(subjectTempData);
			batchArray.push(batchTempData);
			$("#subjectSelect").empty();
			$("#batch").empty();
			$("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
			$("#batch").select2({data:null,placeholder:"Type Batch Name"});
		if($(this).val()!=-1){
			var uploadExam = new UploadExam();
			uploadExam.getSubjectsInDivision($(this).val());
			$("#divisionError").html("");
			
		}
	}); 
	 
	$("#subjectSelect").on("change",function(e){
		var batchArray = [];
		batchArray.push(batchTempData);
		$("#batch").empty();
		$("#batch").select2({data:null,placeholder:"Type Batch Name"});
		
		var subjectId = $(this).val();
		var divisionId = $("#divisionSelect").val();
		var inst_id = $("#instituteSelect").val();
		if(subjectId!="-1"){
			$("#subjectError").html("");
			uploadExam.getBatchFromDivisonNSubject(subjectId,divisionId,inst_id);	
		}else{
			$("#batchError").html("");
			$("#batch").select2({data:batchDataArray,placeholder:"type batch name"});
			$("#batch").select2("val", "");
			$("#batch").prop("disabled",true);
			
		}
	});
	
	$(CALENDAR_DATE).datetimepicker({
		pickTime: false,
		minViewMode:'days',
		format:"YYYY-MM-DD"
	});
	
	$(SYLLABUS_SEARCH_MONTH).datetimepicker({
		pickTime: false,
		minViewMode:'months',
		format:"YYYY-MM"
	}).on("dp.change",uploadExam.onDateChange);
	
	
});


function UploadExam(){
	
	var saveSyllabus = function(){
		var sBean = new SyllabusBean();
		sBean.instId = $("#instituteSelect").val();
		sBean.classId = $("#divisionSelect").val();
		sBean.subjectId = $("#subjectSelect").val();
		sBean.batchId = $("#batch").val().join?$("#batch").val().join(","):$("#batch").val();
		sBean.date = $("[name='syllabusSetTime']").val();
		sBean.syllabus = $("#syllabusToSet").val();
		var handler = {};
		handler.success = function(){$.notify({message: "Syllabus saved successfully"},{type: 'success'});onDateChange();};
		handler.error = function(){$.notify({message: "Syllabus can not save successfully"},{type: 'danger'});};
		rest.post(saveSyllabusUrl,handler,JSON.stringify(sBean),true);
	}
	
	var onDateChange = function(){
		var sBean = new SyllabusBean();
		sBean.instId = $("#instituteSelect").val();
		sBean.classId = $("#divisionSelect").val();
		sBean.subjectId = $("#subjectSelect").val();
		sBean.batchId = $("#batch").val().join?$("#batch").val().join(","):$("#batch").val();
		var date = $(SYLLABUS_SEARCH_MONTH).find('input').val();
		var handler = {};
		handler.success = function(data){loadTable(data);};
		handler.error = function(e){console.log(e)};
		
		rest.get(saveSyllabusUrl+"/"+date+"?"+$.param(sBean),handler);
	}
	
	var getSubjectsInDivision = function(division){

		var inst_id = $("#instituteSelect").val();
		var handler = {};
		handler.success = function(e){console.log("Success",e);
		$("#subjectSelect").empty();
		var subjectArray = [];
		var tempData = {};
		if(teacherSubjectArray.length > 0){
 		tempData.id = "-1";
 		tempData.text = "Select Subject";
 		subjectArray.push(tempData);
 		$.each(e,function(key,val){
 	 		$.each(teacherSubjectArray,function(innerKey,innerVal){
 	 			if(val.subjectId == innerVal.subjectId){
 				var data = {};
 				data.id = val.subjectId;
 				data.text = val.subjectName;
 				subjectArray.push(data);
 	 			return false;
 	 			}
 	 		});
 			});
 		if(subjectArray.length > 1){
	    $("#subjectSelect").select2({data:subjectArray,placeholder:"Type Subject Name"});
	    $("#subjectSelect").prop("disabled",false);
 		}else{
 	 		subjectArray = [];
 	 		$("#subjectSelect").select2({data:subjectArray,placeholder:"Subjects not available"});
 		    $("#subjectSelect").prop("disabled",false);
 	 	}
		}else{
 	 		subjectArray = [];
 	 		$("#subjectSelect").select2({data:subjectArray,placeholder:"Subjects not available"});
 		    $("#subjectSelect").prop("disabled",false);
 	 	}
		}
		handler.error = function(e){console.log("Error",e)}
		rest.get("rest/teacher/getSubjectOfDivision/"+inst_id+"/"+division,handler);
	}
	
	
	var getBatchFromDivisonNSubject = function(subjectId,divisionId,institute){
	
	$.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "getBatchesByDivisionNSubject",
		    	 divisionId: divisionId,
				 subjectId:subjectId,
				 institute:institute
		   		},
		   type:"POST",
		   success:function(data){
			   console.log(data);
			   displayBatchFromSubjectNDivision(data);
		   },
			error:function(){
		   		modal.launchAlert("Error","Error");
		   	}
		   });
	}
	
	var displaySubjectDropDown = function (data){
		var selectOptionDropdown = "#subjectSelect";
		$(selectOptionDropdown).find("option:not(:first)").remove();
		data = JSON.parse(data);
		if(data.subjectnames.trim()!=="" || data.subjectids.trim()!==""){
		var subjectnames = data.subjectnames.split(",");
		var subjectids = data.subjectids.split(",");
			for(subjectNameIndex in subjectnames){
				console.log(subjectnames[subjectNameIndex]);
				console.log(subjectids[subjectNameIndex]);
				$("<option/>",{
					value:subjectids[subjectNameIndex],
					text:subjectnames[subjectNameIndex]
				}).appendTo(selectOptionDropdown);
			}
			batchDataArray = [];
			$("#batchError").html("");
			$("#batch").select2({data:batchDataArray,placeholder:"type batch name"});
			$("#batch").select2("val", "");
			$("#batch").prop("disabled",true);
			
		}else{
			$("#subjectSelect").prop("disabled",true);
			batchDataArray = [];
			$("#batchError").html("");
			$("#batch").select2({data:batchDataArray,placeholder:"type batch name"});
			$("#batch").select2("val", "");
			$("#batch").prop("disabled",true);
		}
		
	}
	
	var displayBatchFromSubjectNDivision = function (data){
		var selectOptionDropdown = "#batch";
		var batchMenu = "#batchMenu";
		data = JSON.parse(data);
		data = data.batchlist;
		data = JSON.parse(data);
		$(batchMenu).children().not(".staticMenu").remove();
		$(batchMenu).find(".selectAllCheckbox").prop("checked",false);
		if(data.length !== 0){
		var batchDataArray = [];
		
		var index=0;
		$.each(data,function(index,subjectData){
			var batchDataObject = {};
			batchDataObject.id = subjectData.batch_id;
			batchDataObject.text = subjectData.batch_name;
			batchDataArray.push(batchDataObject);
		});
		$("#batch").empty();
		$("#batch").select2({data:batchDataArray,placeholder:"type batch name",multiple: true});
		$("#batch").prop("disabled",false);
		}else{
			$("#batch").prop("disabled",true);
			$(".alert-danger").text("Subjects for selected batch are not added.").show();
		}
	}
	
	function deleteRecord(data){
		var handler = {};
		handler.success = function(){$.notify({message: "Syllabus deleted successfully"},{type: 'success'});onDateChange();};
		handler.error = function(){$.notify({message: "Syllabus can not delete"},{type: 'danger'});};
		rest.deleteItemData(saveSyllabusUrl,handler,JSON.stringify(data));
	}
	
	var loadTable = function(data){
		var oTable  = $(TEACHER_SYLLABUS_TABLE).DataTable( {
			bDestroy:true,
			data: data,
			lengthChange: false,
			bFilter: false,
			columns:[
			
			{
				title: "Date",data:"date",sDefault:'&mdash;',render:function(data){return moment(data).format("YYYY-MM-DD");}
			},
			{
				title: "Syllabus",data:"syllabus",sDefault:'&mdash;'
			},
			{
				title: "Status",data:"status",render:function(data){console.log(data); return data==null?"&mdash;":data;}
			},
			{
				title: "",bSortable:false,data:null,render:function(data){if(data.editable){return "<input type='button' class='btn btn-success btn-xs edit' value='Edit'/>&nbsp;" +
						"<input type='button' class='btn btn-primary btn-xs delete' value='Delete'/>&nbsp;"}else{
							return "<span>Not Editable</span>";
						}
						
			}}
			],
			rowCallback:function(row, data, displayIndex, displayIndexFull){
				$(row).find(".delete").on("click",function(){
					deleteRecord(data);
				});
			}} );
		
		$(TEACHER_SYLLABUS_TABLE+' tbody').on('click', 'input.edit', function () {
	        var tr = $(this).closest('tr');
	        var row = oTable.row( tr );
	 
	        if ( row.child.isShown() ) {
	            // This row is already open - close it
	            row.child.hide();
	            tr.removeClass('shown');
	            $(this).val("Edit");
	        }
	        else {
	            // Open this row
	            row.child( format(row.data()) ).show();
	            tr.addClass('shown');
	            $(this).val("Cancel");
	        }
	    });
		
		function editSyllabus(data,syllabusText,date){
			data.syllabus = syllabusText;
			data.date = date;
			console.log(data);
			var handler = {};
			handler.success = function(){
				$.notify({message: "Syllabus saved successfully"},{type: 'success'});
				onDateChange();
			};
			handler.error = function(){$.notify({message: "Syllabus not saved successfully"},{type: 'danger'});};
			rest.put(saveSyllabusUrl,handler,JSON.stringify(data));
		}
		
		function format(data){
			var datePicker = '<div class="input-group form-group date" id="syllabusEditTime" >'+
            '<input type="text" class="form-control" name="syllabusEditTime" required placeholder="Date"/>'+
                '<span class="input-group-addon">'+
                    '<span class="glyphicon glyphicon-calendar"></span>'+
                '</span>'+
            '</div>';
			
			var dP = $(datePicker).datetimepicker({
				pickTime: false,
				minViewMode:'days',
				format:"YYYY-MM-DD"
			});
			dP.data("DateTimePicker").setDate(new Date(data.date));
			
			var syllabusText = $("<textarea/>",{text:data.syllabus}).addClass("form-control form-group");
			//var saveButton = "<input type='button' class='btn btn-success btn-xs form-group' value='Save'/>"
			var saveButton = $("<input/>",{value:"Save"}).addClass("btn btn-success btn-xs form-group").on("click",function(){
				var syllabus = syllabusText.val();
				var date = dP.find('input').val(); 
				editSyllabus(data,syllabus,date);
			});
			var div = $("<form/>",{role:"form"}).append(dP).append(syllabusText).append(saveButton);
			return div;
		}
		
		
		// Apply the search
		oTable.columns().every( function () {
	        var that = this;
	 
	        $( '#syllabusDatatableSearch' ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        } );
	    } );
	}
	
	this.getSubjectsInDivision = getSubjectsInDivision;
	this.displaySubjectDropDown = displaySubjectDropDown;
	this.getBatchFromDivisonNSubject = getBatchFromDivisonNSubject;
	this.displayBatchFromSubjectNDivision = displayBatchFromSubjectNDivision;
	this.loadTable = loadTable;
	this.saveSyllabus = saveSyllabus;
	this.onDateChange = onDateChange;
	}

function SyllabusBean(){
	this.instId;
	this.classId;
	this.subjectId;
	this.batchId;
	this.teacherId;
	this.date;
	this.syllabus;
}