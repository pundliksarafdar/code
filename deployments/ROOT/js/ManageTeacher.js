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

$(document).ready(function(){
	
	$('div#addStudentModal').on('click','button#btn-add',function(){
		batchIds = "";
		
		$('div#addStudentModal .error').html('');
		$('div#addStudentModal .error').hide();
		var studentLgName;
		
		studentLgName = $('div#addStudentModal').find('#studentLoginName').val();
		
		getSelectedBatchesForStudent();		
		if(!studentLgName || studentLgName.trim()==""){
			$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Student11 login name cannot be blank');
			$('div#addStudentModal .error').show();
		}else{
			$('div#addStudentModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.addStudent('',studentLgName,batchIds,successCallbackStudent,errorCallbackStudent);
		}
		
	});
	
	$('div#modifyStudentModal').on('click','button#btn-searchStudent',function(){
		$('div#modifyStudentModal .error').html('');
		$('div#modifyStudentModal .error').hide();
		var studentLgName;
		
		studentLgName = $('div#modifyStudentModal').find('#studentLoginNameModify').val();
		
			
		if(!studentLgName || studentLgName.trim()==""){
			$('div#modifyStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Student login name cannot be blank');
			$('div#modifyStudentModal .error').show();
		}else{
			$('div#modifyStudentModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.searchStudent('',studentLgName,successCallbackStudent,errorCallbackStudent);
		}
		
	});
	
	$('div#searchStudentModal').on('click','button#btn-searchStudent',function(){
		$('div#searchStudentModal .error').html('');
		$('div#searchStudentModal .error').hide();
		var studentLgName;
		
		studentLgName = $('div#searchStudentModal').find('#studentLoginNameModify').val();
		
			
		if(!studentLgName || studentLgName.trim()==""){
			$('div#searchStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Student login name cannot be blank');
			$('div#searchStudentModal .error').show();
		}else{
			$('div#searchStudentModal .progress').removeClass('hide');
			$('.add').addClass('hide');
			allAjax.searchStudent('',studentLgName,successCallbackStudent,errorCallbackStudent);
		}
		
	});
	
	
	$('[data-target="#addStudentModal"]').on('click',function(){
		$('#addStudentModal').find('.error').hide();
		$('div#addStudentModal #classTimming').addClass('hide');
		$('div#addStudentModal .setTimming').addClass('hide');
		$('div#addStudentModal .add').removeClass('hide');
		$('div#addStudentModal .progress').addClass('hide');

	});
	
	$('[data-target="#modifyStudentModal"]').on('click',function(){
		$('#modifyStudentModal').find('.error').hide();
		$('div#modifyStudentModal #classTimming').addClass('hide');
		$('div#modifyStudentModal .setTimming').addClass('hide');
		$('div#modifyStudentModal .search').removeClass('hide');
		$('div#modifyStudentModal .progress').addClass('hide');

	});
	
	$('[data-target="#modifyTeacherModal"]').on('click',function(){
		$('#modifyTeacherModal').find('.error').hide();
		$('div#modifyTeacherModal #classTimming').addClass('hide');
		$('div#modifyTeacherModal .setTimming').addClass('hide');
		$('div#modifyTeacherModal .search').removeClass('hide');
		$('div#modifyTeacherModal .progress').addClass('hide');

	});
	
	$('[data-target="#searchStudentModal"]').on('click',function(){
		$('#searchStudentModal').find('.error').hide();
		$('div#searchStudentModal #classTimming').addClass('hide');
		$('div#searchStudentModal .setTimming').addClass('hide');
		$('div#searchStudentModal .search').removeClass('hide');
		$('div#searchStudentModal .progress').addClass('hide');

	});
	
	$('#fromDate,#toDate').datetimepicker({
		pickDate:false
	});
});

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



