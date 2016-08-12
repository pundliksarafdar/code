<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style type="text/css">

#teacherTable .editEnabled .editable{
	display:block;
}

#teacherTable .editEnabled .default{
	display:none;
}

#teacherTable .editable{
	display:none;
}

#teacherTable .default{
	display:show;
}

.suffixError, .subjectError{
color:red;
}

</style>
<script>
	/*
	function
	*/
	
	function validateInput(inputText){
		var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9]{1,}$/;
		var isValidInput = CHAR_AND_NUM_VALIDATION.test(inputText);
		return isValidInput;
	}
	function filterteacherTable(){
		$('#teacherTable').DataTable().search(
			$('#teacherTableSearchCustom').val()
			).draw();
	}
	
	function deleteteacherPrompt(){
		var teacherIdToDelete = $(this).closest("tr").find("#teacherId").val();
		var divisionId = $(this).closest("tr").find("#divisionId").val();
		deleteteacherConfirm(divisionId,teacherIdToDelete);	
	}
	
	function deleteteacherConfirm(divisionId,teacherIdToDelete){
		modal.modalConfirm("Delete","Do you want to delete","Cancel","Delete",deleteteacher,[divisionId,teacherIdToDelete]);
	}
	
	function deleteteacher(divisionId,teacherIdToDelete){
				$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "deleteTeacher",
						 regId:'',
						 teacherdivisionid:divisionId,
						 teacherId:teacherIdToDelete
				   		},
				   type:"POST",
				   success:function(data){
					   $.notify({message: "teacher successfuly deleted"},{type: 'success'});
					   $('input[type="hidden"]#teacherId[value="'+teacherIdToDelete+'"]').closest("tr").remove();
				   	},
				   error:function(data){
				
				   }
			});
	}
	
	function enableEdit(){
		$(this).closest("tr").find(".subjectError").empty();
		$(this).closest("tr").find(".suffixError").empty();
		var subjectData = getAllSubjects();
		var preselectedsubjects=$(this).closest("tr").find("select").val();
				$(this).closest("tr").find("select").select2({
			data:subjectData
		}).val(preselectedsubjects).change();
	
		var subjectName = $(this).closest("tr").find(".defaultteacherSuffix").text().trim();
		$(this).closest("tr").find(".editteacherSuffix").val(subjectName);
		$(this).closest("tr").addClass("editEnabled");
	}

	function cancelEdit(){
		$(this).closest("tr").removeClass("editEnabled");
	}
	var that;
	function saveteacher(){
		$(this).closest("tr").find(".subjectError").empty();
		$(this).closest("tr").find(".suffixError").empty();
		var teacherId = $(this).closest("tr").find("#teacherId").val();
		var teacherSuffix = $(this).closest("tr").find(".editteacherSuffix").val().trim();
		var subjectIds = $(this).closest("tr").find(".selectSubject").val();
		that=$(this);
		saveteacherAjax(teacherId,teacherSuffix,subjectIds);
	}
	
	function saveteacherAjax(teacherId,teacherSuffix,subjectIds){
		var flag=false;
		if(subjectIds!=null){
			subjectIds = subjectIds.join(',');
		}else{
			flag=true;
			that.closest("tr").find(".subjectError").html("Please select atleast one Subject!");
		}
		
		if(teacherSuffix!=""){
			if(!validateInput(teacherSuffix)){
				flag=true;
				that.closest("tr").find(".suffixError").html("Only numbers and aplhabets are allowded!");
			}
		}
		if(flag==false){
		$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "updateteacher",
						 regId:'',
						 subIds:subjectIds,
						 teacherId:teacherId,
						 suffix:teacherSuffix
				   		},
				   type:"POST",
				   success:function(data){					  
						getAllteacheres();
				   	},
				   error:function(data){
				   
				   }
			});
		}
	}
	
	function getAllteacheres(){
		$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getAllTeachers"
				   		},
				   type:"POST",
				   success:function(data){					  
						showAllteacher(data);
				   	},
				   error:function(data){
				   
				   }
			});
	}
	
	function showAllteacher(data){
		data = JSON.parse(data);
		console.log(data);
		var dataTable = $('#teacherTable').DataTable({
			bDestroy:true,
			data: data.instituteTeachers,
			lengthChange: false,
			columns: [
				{ title: "Name",data:"teacherBean",render:function(data,event,row){
					var div = '<div>'+data.fname+" "+data.lname+'</div>';
					var teacherIdHidden = '<input type="hidden" value="'+data.regId+'" id="teacherId">';
					return div + teacherIdHidden;
				},sWidth:"20%"},
				{ title: "Suffix",data:"suffix",render:function(data,event,row){
					var div = '<div class="default defaultteacherSuffix">'+data+'</div>';
					var input= '<input type="text" value="'+data+'" class="form-control editable editteacherSuffix" maxlength="5">';
					var span='<span class="editable suffixError"></span>';
					return div + input + span;
				},sWidth:"20%"},
				{ title: "Subjects",data:"subjects",render:function(data,event,row){
					var subjectsStr = "";
					var selectTag = '<div class="editable"><select class="selectSubject" multiple="" style="width:100%">';
					var subjects;
					$.each(data,function(key,val){
						selectTag = selectTag + '<option selected="selected" value="'+val.subjectId+'">'+val.subjectName+'</option>';
						subjectsStr =  subjectsStr + ","+ val.subjectName;
					});
					subjectsStr = subjectsStr.replace(",","");
					subjectsStr = '<div class="default">'+subjectsStr+'</div>';
					selectTag = selectTag+"</select></div>";
					var span = '<span class="editable subjectError"></span>'
					return subjectsStr + selectTag + span;
				},sWidth:"30%"},
				{ title: "Manage",data:null,render:function(data,event,row){
					var buttons = '<div class="default">'+
						'<input type="button" class="btn btn-xs btn-primary btn-teacher-edit" value="Edit">&nbsp;'+
						'<input type="button" class="btn btn-xs btn-danger btn-teacher-delete" value="Delete">'+
					'</div>'+
					'<div class="editable">'+
						'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>'
					return buttons;
				},sWidth:"10%"}
			]
		});
		
		
		
		/* $("#addteacherSelectSubject").select2({
			data:subjectData,
			placeholder:"Search and add subject"
		}); */
		
	}
	
	function getAllSubjects(){
		var subjectDataArray = [];
		var subjectData = {};
		$.ajax({
				 url: "classOwnerServlet",
				async:false,
				   data: {
				    	 methodToCall: "getAllSubjects"
				   		},
				   type:"POST",
				   success:function(data){
						subjectData = JSON.parse(data);
				   	},
				   error:function(data){
				   
				   }
			});
			
			$.each(subjectData.instituteSubjects,function(key,val){
				var data = {};
				data.id = val.subjectId;
				data.text = val.subjectName;
				subjectDataArray.push(data);
			});
			
			return subjectDataArray;
	}
	
	function addteacher(){
		var teacherName = $("#addteacherteacherName").val();
		var	divisonNameT = $("#addteacherSelectDivision").val();
		var	subject = $("#addteacherSelectSubject").val().join(",");
		addteacherConfirm(teacherName,divisonNameT.toString(),subject);
	}
	
	function addteacherConfirm(teacherName,divisionName,subject){
			console.log(teacherName,divisionName,subject);	
			$.ajax({
			   url: "classOwnerServlet",
			   data: {
					 methodToCall: "addteacher",
					 regId:'',
					 teacherName:teacherName,
					 divisionName:divisionName,
					 subjectname:subject
					},
			   type:"POST",
			   success:function(e){
					$.notify({message: "teacher successfuly added"},{type: 'success'});
					getAllteacheres();		
				},
			   error:function(e){
					$.notify({message: "Error"},{type: 'danger'});
				}
			});
			
	}
	
	$(document).ready(function(){
		getAllteacheres();
	
		$(this).on("click",".btn-teacher-delete",deleteteacherPrompt)
			.on("click",".btn-teacher-edit",enableEdit)
			.on("click",".btn-cancel",cancelEdit)
			.on("click",".btn-save",saveteacher)
			.on("click",".btn-add-teacher",addteacher);	
		
		$('#teacherTableSearchCustom').on("keyup click",filterteacherTable);
		//$("#teacherTable").DataTable({lengthChange: false});
	});
</script>
</head>
<body>
<ul class="nav nav-tabs" style="border-radius:10px">
  <li><a href="manageteacher">Add Teacher</a></li>
  <li class="active"><a href="viewteacherstab"  data-toggle = "tab">View Teachers</a></li>
</ul>
  
<div class = "tab-content" style="padding: 2%">
 <div id="viewteacherstab" class = "tab-pane fade in active">
<table id="teacherTable" class="table" width="100%">
</table>
</div>
</div>

</body>
</html>