
<%@page import="com.classapp.db.Teacher.TeacherDetails"%>
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@taglib prefix="s" uri="http://java.sun.com/jstl/core"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<script type="text/javascript" src="js/ManageTeacher.js"></script>
<script type="text/javascript" src="js/AddSubject.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.TEACHER_LIST); %>
<script>
var subjectIds;
var searchedteacherID;
var subjectData = getAllSubjects();
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
	
function canceledit(){
	//alert(batchID+" "+pagenumber);
	 $("#teachertomodify").hide();
	 $("#modifyTeacherModalbody").empty();
	 $("#paginateform").show();
	 $("#baseteachertable").show();
	
}
function getSelectedSubjectsForAddTeacher(){
	var subjects=$(".chkSubjectAddTeacher:checked").map(function(){
	return this.value;
	});
		
	var i=0;
	while(i<subjects.size()){
		if(i==0){
			subjectIds=subjectIds+subjects[0]+"";
		}else{
			subjectIds=subjectIds+","+subjects[i];
		}
		i++;
	}
}

function getSelectedSubjectsForTeacher(){
	var subjects=$(".chkSubjectTeacher:checked").map(function(){
	return this.value;
	});
	
	var i=0;
	while(i<subjects.size()){
		if(i==0){
			subjectIds=subjectIds+subjects[0]+"";
		}else{
			subjectIds=subjectIds+","+subjects[i];
		}
		i++;
	}
}


function validateInput(inputText){
	var CHAR_AND_NUM_VALIDATION = /^[a-zA-Z0-9]{1,}$/;
	var isValidInput = CHAR_AND_NUM_VALIDATION.test(inputText);
	return isValidInput;
}

	$(document).ready(function(){
		$('div#modifyTeacherModal .error').html('');
		$('div#modifyTeacherModal .error').hide();
		$("#addTeacherSearch").click(function(){
			$("#teacherAddStatus").hide();
			var username=$("#username").val().trim();
			var email=$("#email").val().trim();
			 $.ajax({
	                url: 'classOwnerServlet',
	                data:{
				    	 methodToCall: "addTeacherSearch",
				    	 username:username,
				    	 email:email
				   		}, 
				   	type:"POST", 
				   	 success:function(e){
				   		var resultJson = JSON.parse(e);
				   		var status=resultJson.status;
				   		if(status=="available"){
				   			$("#firstname").html(resultJson.firstname);
				   			$("#lastname").html(resultJson.lastname);
				   			$("#searchedusername").html(username);
				   			$("#searchedrmailid").html(email);
				   			searchedteacherID=resultJson.teacherID;
				   			$("#teacherdetailsdiv").show();
				   			$("#teachernotavailablediv").hide();
				   		}else{
				   			$("#teacherdetailsdiv").hide();
				   			$("#teachernotavailablediv").show();
				   		}
				   	 },
				   	 error:function(e){
				   		 }
				   	 });
			
		});
		
		$("#addteacher").click(function(){
			var	subject = $("#addTeacherSelectSubject").val();
			var suffix=$("#teachersuffix").val();
			$("#subjecterror").empty();
			$("#suffixerror").empty();
			var flag=false;
			if(suffix!=""){
			if(!validateInput(suffix)){
				$("#suffixerror").html("Only numbers and alphabets allowded!");
				flag=true;
			}
			}
			if(subject=="" || subject==null){
				$("#subjecterror").html("Please select atleast one subject");
				flag=true;
			}
			
			if(flag==false){
				subject = $("#addTeacherSelectSubject").val().join(",");
			$.ajax({
                url: 'classOwnerServlet',
                data:{
			    	 methodToCall: "addTeacher",
			    	 regId:'',
			    	 subjects:subject,
			    	 teacherID:searchedteacherID,
			    	 suffix:suffix
			   		}, 
			   	type:"POST", 
			   	 success:function(e){
			   		var resultJson = JSON.parse(e);
			   		var status=resultJson.status;
			   		 if(status=="success"){
			   		$.notify({message: "Teacher Added Successfully."},{type: 'success'});
			   		/* $("#teacherAddStatus").html("Teacher Added Successfully."); */
			   		 }else{
			   		$.notify({message: "Teacher alrady present in your institute."},{type: 'info'});
			   		/* $("#teacherAddStatus").html("Teacher alrady present in your institute."); */
			   		 }
			   		/* $("#teacherAddStatus").show(); */
			   	 },
			   	 error:function(e){}
			   		
		});
			}
		});
		$("#addTeacherSelectSubject").select2({
			data:subjectData,
			placeholder:"Search and add subject"
		});
		

	});
	
</script>
<body>
<ul class="nav nav-tabs" style="border-radius:10px">
  <li class="active"><a href="#addteachertab" data-toggle = "tab">Add Teacher</a></li>
  <li><a href="viewteachers">View Teachers</a></li>
</ul>
  
<div  class = "tab-content">
 <div id="addteachertab" class = "tab-pane fade in active">
	<div class="well">
	<div class="row" >
		<div class="form-group col-lg-3">
			<input type="text" id="username" class="form-control" placeholder="ClassFloor ID">
		</div>

		<div class="form-group col-lg-3">
			<input type="email" id="email" class="form-control" placeholder="Email">
		</div>
	
	<div class="form-group col-lg-3" >
		<button type="submit" class="btn btn-primary" id="addTeacherSearch">Search</button>
	</div>
	
	</div>
</div>
<div class="container" id="teacherdetailsdiv" style="display: none;">
<div class="row">
<font size="3"><b>Teacher Details</b></font>
</div>
<div class="row" style="padding: 1%">
<table class="table table-striped">
<tr>
<td><font size="3">First Name</font></td><td> :</td> <td><span id="firstname"></span></td>
</tr>
<tr>
<td><font size="3"> Last Name </font></td> <td>:</td> <td><span id="lastname"></span></td>
</tr>
<tr>
<td><font size="3">ClassFloor ID </font></td> <td>:</td> <td><span id="searchedusername"></span></td>
</tr>
<tr>
<td><font size="3">Email ID </font></td> <td>:</td><td> <span id="searchedrmailid"></span></td>
</tr>
<tr>
<td><font size="3">Suffix </font></td> <td>:</td><td> <input type="text" class="form-control" id="teachersuffix" ><span id="suffixerror" class="suffixerror" style="color: red"></span></td>
</tr>
<tr>
<td><font size="3">Allocate Subjects </font></td>
<td>:</td>
<td>
		<select id="addTeacherSelectSubject" multiple="multiple" style="width:100%">
		</select>
		<span id="subjecterror" class="subjecterror" style="color:red"></span>
		</td>	
</tr>
<tr>
<td></td><td></td>
	<td>
		<button class="btn btn-primary" id="addteacher" >Add</button>
</td>
</tr>
</table>
</div>
</div>
<div class="container alert alert-info" align="center" id="teachernotavailablediv" style="display: none;">
	Invalid Teacher Details.
</div>
<div class="container alert alert-info" align="center" id="teacherAddStatus" style="display: none;">
</div>
</div>
</div>
</body>
</html>