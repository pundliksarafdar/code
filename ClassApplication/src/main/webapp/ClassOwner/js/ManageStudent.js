var batchIds;
var subjectname;
var div_id;
var selectedStudentIds;
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

function getSelectedStudentsToDelete(){
	var studentIds;
	studentIds=$(".chkStudent:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<studentIds.size()){
		if(i==0){
			selectedStudentIds=selectedStudentIds+studentIds[0]+"";
		}else{
			selectedStudentIds=selectedStudentIds+","+studentIds[i];
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



function errorCallbackStudent(e){
	$('div#addStudentModal .progress').addClass('hide');
	$('div#addStudentModal .add').removeClass('hide');
	$('div#addStudentModal .error').show();
		$('div#addStudentModal .error').html('<strong>Error!</strong> Unable to add');
}

function successCallbackStudent(e){
$('div#addStudentModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#addStudentModal').modal('hide');
	   modal.launchAlert("Success","Student Added! Page will refresh in 2 sec");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
		   $('div#addStudentModal .add').removeClass('hide');
		   $('div#addStudentModal .error').show();
	   	if(!resultJson.message){
		   $('div#addStudentModal .error').html('<strong>Error!</strong> Unable to add');
	   	}else{
	   		$('div#addStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
   	}
}


function errorCallbackStudentModify(e){
	$('div#modifyStudentModal .progress').addClass('hide');
	$('div#modifyStudentModal .add').removeClass('hide');
	$('div#modifyStudentModal .error').show();
	var resultJson = JSON.parse(e);
	if(!resultJson.message){
		   $('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
	   	}else{
	   		$('div#modifyStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
}

function successCallbackStudentModify(e){
$('div#modifyStudentModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#modifyStudentModal').modal('hide');
	   modal.launchAlert("Success","Student Updated! Page will refresh in 2 sec");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
		   $('div#modifyStudentModal .add').removeClass('hide');
		   $('div#modifyStudentModal .error').show();
	   	if(!resultJson.message){
		   $('div#modifyStudentModal .error').html('<strong>Error!</strong> Unable to update');
	   	}else{
	   		$('div#modifyStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
   	}
}

function errorCallbackStudentDelete(e){
	$('div#deleteStudentModal .progress').addClass('hide');
	$('div#deleteStudentModal .add').removeClass('hide');
	$('div#deleteStudentModal .error').show();
	var resultJson = JSON.parse(e);
	if(!resultJson.message){
		   $('div#deleteStudentModal .error').html('<strong>Error!</strong> Unable to delete');
	   	}else{
	   		$('div#deleteStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
}

function successCallbackStudentDelete(e){
$('div#deleteStudentModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#deleteStudentModal').modal('hide');
	   modal.launchAlert("Success","Student Deleted! Page will refresh in 2 sec");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
		   $('div#deleteStudentModal .add').removeClass('hide');
		   $('div#deleteStudentModal .error').show();
	   	if(!resultJson.message){
		   $('div#deleteStudentModal .error').html('<strong>Error!</strong> Unable to delete');
	   	}else{
	   		$('div#deleteStudentModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
   	}
}



