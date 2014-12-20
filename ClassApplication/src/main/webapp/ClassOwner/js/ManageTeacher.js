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
	   modal.launchAlert("Success","Student Added! Page will refresh in soon");
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


function errorCallbackTeacherModify(e){
	$('div#modifyTeacherModal .progress').addClass('hide');
	$('div#modifyTeacherModal .add').removeClass('hide');
	$('div#modifyTeacherModal .error').show();
	var resultJson = JSON.parse(e);
	if(!resultJson.message){
		   $('div#modifyTeacherModal .error').html('<strong>Error!</strong> Unable to update');
	   	}else{
	   		$('div#modifyTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
}

function successCallbackTeacherModify(e){
$('div#modifyTeacherModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#modifyTeacherModal').modal('hide');
	   modal.launchAlert("Success","Teacher Updated! Page will refresh in soon");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
		   $('div#modifyTeacherModal .add').removeClass('hide');
		   $('div#modifyTeacherModal .error').show();
	   	if(!resultJson.message){
		   $('div#modifyTeacherModal .error').html('<strong>Error!</strong> Unable to update');
	   	}else{
	   		$('div#modifyTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
   	}
}

function errorCallbackTeacherDelete(e){
	$('div#deleteTeacherModal .progress').addClass('hide');
	$('div#deleteTeacherModal .add').removeClass('hide');
	$('div#deleteTeacherModal .error').show();
	var resultJson = JSON.parse(e);
	if(!resultJson.message){
		   $('div#deleteTeacherModal .error').html('<strong>Error!</strong> Unable to delete');
	   	}else{
	   		$('div#deleteTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
}

function successCallbackTeacherDelete(e){
$('div#deleteTeacherModal .progress').addClass('hide');
var resultJson = JSON.parse(e);
   if(resultJson.status != 'error'){
	   $('div#deleteTeacherModal').modal('hide');
	   modal.launchAlert("Success","Teacher Deleted! Page will refresh in soon");
	   setTimeout(function(){
		   location.reload();
	   },2*1000);		   
   }else{
		   $('div#deleteTeacherModal .add').removeClass('hide');
		   $('div#deleteTeacherModal .error').show();
	   	if(!resultJson.message){
		   $('div#deleteTeacherModal .error').html('<strong>Error!</strong> Unable to delete');
	   	}else{
	   		$('div#deleteTeacherModal .error').html('<strong>Error!</strong>'+resultJson.message);
	   	}
   	}
}



