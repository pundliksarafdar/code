var batchIds;
var subjectname;
var div_id;
function getSelectedCheckbox(){
	var subjects;
	subjects=$(".chk:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<subjects.size()){
		if(i==0){
			subjectname=subjectname+subjects[0]+"";
		}else{
			subjectname=subjectname+","+subjects[i];
		}
		i++;
	}
}


function getSelectedBatchesForStudent(){
	var batches;
	batches=$(".chkBatch:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<batches.size()){
		if(i==0){
			batchIds=batchIds+batches[0]+"";
		}else{
			batchIds=batchIds+","+batches[i];
		}
		i++;
	}
}

var batchName;
$(document).ready(function(){
	
	/*$('div#abc1').find('#subjectName').autocomplete("AutoComplete.jsp");*/
	
	
	$('div#addBatchModal').on('click','button#btn-add',function(){
		batchName = "";
		$('div#addBatchModal .error').html('');
		$('div#addBatchModal .error').hide();
		var regId;
		batchName = $('div#addBatchModal').find('#batchName').val();
		if(!batchName || batchName.trim()==""){
			$('div#addBatchModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Batch name cannot be blank');
			$('div#addBatchModal .error').show();
		}else{
			$('div#addBatchModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addBatch('',batchName,successCallback,errorCallback);
		}
	});
	
	
	$('div#addSubjectModal').on('click','button#btn-add',function(){
		batchName = "";
		$('div#addSubjectModal .error').html('');
		$('div#addSubjectModal .error').hide();
		var regId;
		subjectName = $('div#addSubjectModal').find('#subjectName').val();
		
		if(!subjectName || subjectName.trim()==""){
			$('div#addSubjectModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Subject name cannot be blank');
			$('div#addSubjectModal .error').show();
		}else{
			$('div#addSubjectModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addSubject('',subjectName,successCallbackSubject,errorCallbackSubject);
		}
	});
	
	$('div#addclassModal').on('click','button#btn-add',function(){
		batchName = "";
		$('div#addclassModal .error').html('');
		$('div#addclassModal .error').hide();
		var regId;
		className = $('div#addclassModal').find('#classname').val();
		stream= $('div#addclassModal').find('#stream').val();
		if(!className){
			$('div#addclassModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Subject name cannot be blank');
			$('div#addclassModal .error').show();
		}else{
			$('div#addclassModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addclass('',className,stream,successCallbackSubject,errorCallbackSubject);
		}
	});
	
	var subjectsname="";
	function getSelectedCheckbox()
	{
		var subjects;
		subjects=$(".chk:checked").map(function() {
			return this.value;
		});
	var i=0;
	while(i<subjects.size())
		{
		if(i==0)
			{
		subjectsname=subjectsname+subjects[0]+"";
			}else{
				subjectsname=subjectsname+","+subjects[i];
			}
		i++;
		}
	
	}
	
	
	$('div#addTeacherModal').on('click','button#btn-add',function(){
		subjectsname="";
		batchName = "";
		$('div#addTeacherModal .error').html('');
		$('div#addTeacherModal .error').hide();
		var regId;
		var regloginname=/^[a-z0-9]+[@._]*[a-z0-9]+$/;
		teacherID = $('div#addTeacherModal').find('#teacherID').val();
		getSelectedCheckbox();
		if(!teacherID || teacherID.trim()==""){
			$('div#addTeacherModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Teacher ID cannot be blank');
			$('div#addTeacherModal .error').show();
		}else if($("#teacherID").val().length<5 || !$("#teacherID").val().match(regloginname))
		{
			$('div#addTeacherModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Invalid Teacher ID');
			$('div#addTeacherModal .error').show();
		}else if(subjectsname=="")
			{
			$('div#addTeacherModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Please select atleast one subject');
			$('div#addTeacherModal .error').show();
			}else{
			$('div#addTeacherModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addTeacher('',subjectsname,teacherID,successCallbackAddTeacher,errorCallbackaddTeacher);
		}
	});
	
	$('[data-target="#addTeacherModal"]').on('click',function(){
		$('#addTeacherModal').find('.error').hide();
		$('div#addTeacherModal #classTimming').addClass('hide');
		$('div#addTeacherModal .setTimming').addClass('hide');
		$('div#addTeacherModal .add').removeClass('hide');
		$('div#addTeacherModal .progress').addClass('hide');
	});
	
	$('[data-target="#addBatchModal"]').on('click',function(){
		$('#addBatchModal').find('.error').hide();
		$('div#addBatchModal #classTimming').addClass('hide');
		$('div#addBatchModal .setTimming').addClass('hide');
		$('div#addBatchModal .add').removeClass('hide');
		$('div#addBatchModal .progress').addClass('hide');
	});
	
	$('[data-target="#addSubjectModal"]').on('click',function(){
		$('#addSubjectModal').find('.error').hide();
		$('div#addSubjectModal #classTimming').addClass('hide');
		$('div#addSubjectModal .setTimming').addClass('hide');
		$('div#addSubjectModal .add').removeClass('hide');
		$('div#addSubjectModal .progress').addClass('hide');

	});
	
	$('#fromDate,#toDate').datetimepicker({
		pickDate:false
	});
});

function deleteBatch(batchName){
	modal.modalConfirm("Delete Batch", "Do you want to delete batch?", "No", "Yes", deleteBatchConfirm, [batchName]);
}

function deleteBatchConfirm(batchName){
	$('#progressModal').modal('show');
	allAjax.deleteBatch("",batchName,successCallBackDeleteBatch,errorcallBackDeleteBacth);
}

function successCallBackDeleteBatch(){
	$('#progressModal').modal('hide');
	location.reload();
}

function errorcallBackDeleteBacth(){
	modal.launchAlert("Error", "Unable to delete batch");
}

function errorCallback(e){
		$('div#addBatchModal .progress').addClass('hide');
		$('div#addBatchModal .add').removeClass('hide');
		$('div#addBatchModal .error').show();
  		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallback(e){
	$('div#addBatchModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addBatchModal').modal('hide');
		   modal.launchAlert("Success","Batch Added! Page will refresh in 2 sec");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);		   
	   }else{
		   $('div#addBatchModal .add').removeClass('hide');
		   $('div#addBatchModal .error').show();
	   	if(!resultJson.message){
		   $('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
	   	}else{
	   		$('div#addBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
	   	}
}

function errorCallbackSubject(e){
	$('div#addSubjectModal .progress').addClass('hide');
	$('div#addSubjectModal .add').removeClass('hide');
	$('div#addSubjectModal .error').show();
		$('div#addSubjectModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallbackSubject(e){
$('div#addSubjectModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#addSubjectModal').modal('hide');
	   modal.launchAlert("Success","Subject Added! Page will refresh in 2 sec");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
	   $('div#addSubjectModal .add').removeClass('hide');
	   $('div#addSubjectModal .error').show();
   	if(!resultJson.message){
	   $('div#addSubjectModal .error').html('<strong>Error!</strong> Unable to add');
   	}else{
   		$('div#addSubjectModal .error').html('<strong>Error!</strong>'+resultJson.message);
   	}
   	}
}


function errorCallbackaddTeacher(e){
	$('div#addTeacherModal .progress').addClass('hide');
	$('div#addTeacherModal .add').removeClass('hide');
	$('div#addTeacherModal .error').show();
		$('div#addTeacherModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallbackAddTeacher(e){
$('div#addTeacherModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#addTeacherModal').modal('hide');
	   modal.launchAlert("Success","Teacher Added! Page will refresh in 2 sec");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
	   $('div#addTeacherModal .add').removeClass('hide');
	   $('div#addTeacherModal .error').show();
   	if(!resultJson.message){
	   $('div#addTeacherModal .error').html('<strong>Error!</strong> Unable to add');
   	}else{
   		$('div#addTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
   	}
   	}
}

function errorCallbackBatchTimming(e){
		$('div#addBatchModal .progress').addClass('hide');
		$('div#addBatchModal .setTimming').removeClass('hide');		   
		$('div#addBatchModal .error').show();
		$('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallbackBatchTimming(e){
	$('div#addBatchModal .progress').addClass('hide');
	var resultJson = JSON.parse(e);
	   if(resultJson.status != 'error'){
		   $('div#addBatchModal').modal('hide');
		   modal.launchAlert("Success","Batch Added! Page will refresh in 2 sec");
		   setTimeout(function(){
			   location.reload();
		   },2*1000);
		   
	   }else{
		   $('div#addBatchModal .setTimming').removeClass('hide');
		   $('div#addBatchModal .error').show();
		   if(!resultJso.message){
			   $('div#addBatchModal .error').html('<strong>Error!</strong> Unable to add');
		   	}else{
		   		$('div#addBatchModal .error').html('<strong>Error!</strong>'+resultJson.message);
		   	}
	   	}
}



/****************function called by AddSubject.jsp********************/
function BatchSubjectData(){
	this.batchCode = 0;
	this.subjectCode = 0;
}

var batchSubjectData = new BatchSubjectData();

function removeClass(){
	modal.modalConfirm("Delete", "Do you want to delete batch", "No", "Delete", deleteBatch, "Deleted");
}

function addSubject2Batch(subjectCode){
	$('#addSubjectToBatchModal').modal('show');
	batchSubjectData.subjectCode = subjectCode; 
}

function batchClick(batchCode){
	batchSubjectData.batchCode = batchCode;
	//alert(JSON.stringify(batchSubjectData));
	$("#addSubjectToBatchModal .btn-info").text("Loading....");
	$("#addSubjectToBatchModal .btn-info").removeAttr('onclick');
	addSubToBatch(batchSubjectData);
}

function editBatch(){
	$('#addSubjectToBatchModal').modal('show');
}

function addSub2batchDB(that){
	batchClick($(that).parents('.panel-body').find('#batchCode').val());
}

function addSubToBatch(batchData){
	$.ajax({
		url:"http://www.google.com",
		sucess:function(s){
			alert(batchData);
		},
		error:function(e){
			alert(e);
		}
	});
	
}