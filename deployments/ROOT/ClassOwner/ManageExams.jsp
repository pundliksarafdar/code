<%@page import="com.classapp.db.exams.MCQDetails"%>
<%@page import="com.user.UserBean"%>
<%@page import="com.classapp.db.Teacher.TeacherDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>  
 <%List list = (List)request.getAttribute(Constants.MCQS_LIST); 
 System.out.println(list);
 %>
 <style type="text/css">
.errorDiv{
    background-color:gray;
    border:1px solid black;
    width:400px;
    margin-bottom:8px;
}
ul.actionMessage li {
    color:yellow;
}
</style>
<script>
function(){
	   location.reload();
}
var subjectIds;
function getSelectedExamSubjects(){
	var subjects;
	subjects=$(".chkExamSubject:checked").map(function(){
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

function uploadExamFile(){	
			var divisionName=document.getElementById("divisionNameExam").value;
			var teacherName=document.getElementById("teacherNameExam").value;
			subjectIds="";
			var file=document.getElementById("uploadExam").files[0];
			getSelectedExamSubjects();			
			var formdata = new FormData();
			formdata.append("uploadFile", file);
			
			if(!divisionName || divisionName.trim()==""){
				 modal.launchAlert("Error","Error! division name cannot be blank.");
			}else if(!teacherName || teacherName.trim()==""){
				modal.launchAlert("Error","Error! Teacher login name cannot be blank.");				
			}else if(!subjectIds || subjectIds.trim()==""){
				modal.launchAlert("Error"," Error! Please select the subject.");				
			}else if(!file){
				modal.launchAlert("Error"," Error! Please choose the file to upload");				
			}else{
							
			$.ajax({
				type:"POST",
				url: "classOwnerServlet",
			    data: {
						methodToCall: "uploadExam",
						regId:'',
						divisionName:divisionName,
						teacherName:teacherName,
						//subjectIds:subjectIds,
						formdata:formdata,
						dataType: 'json'
				  },
				  success:function(e){				 
					   var resultJson = JSON.parse(e);
					      if(resultJson.status != 'error'){					   	 
					   	   modal.launchAlert("Success","Exam Added! Page will refresh in soon");
					   	   setTimeout(function(){
					   		   location.reload();
					   	   },2*1000);		   
					      }else{
					   		  if(!resultJson.message){
					   			modal.launchAlert("Error","Error!Unable to add");
					   	   	}else{
					   	   	modal.launchAlert("Error","Error!"+resultJson.message);
					   	   	}
					      }
				   	},
				   error:function(e){
						var resultJson = JSON.parse(e);
						if(!resultJson.message){
							modal.launchAlert("Error","Error! Unable to update");
						   	}else{
						   		modal.launchAlert("Error","Error!"+resultJson.message);
						   	}
				   }
				   
			});
			}	
		}
</script>
<div class="btn-group btn-group-sm">
 <s:if test="hasActionErrors()">
    <div class="errorDiv">
        <s:actionerror/>
    </div>
</s:if>
<s:if test="hasActionMessages()">
   <div class="welcome">
      <s:actionmessage/>
   </div>
</s:if>
  <s:form action="uploadExamAction" method="post" enctype="multipart/form-data">  
<table>
<tr>
     <td>Division</td><td><s:textfield name="divisionName" id="divisionName"/></td>
</tr>
<tr>
	<td>Teacher</td><td><s:textfield name="teacherName" id="teacherName"/></td>						
</tr>
<tr>				
	<td>Subject</td>
	<td><s:textfield name="examSubjectName" id="examSubjectName"/></td>
</tr>	
<tr>
			<td>Select File: </td><td><s:file id="uploadExam" name="uploadExam"/></td>
</tr>  	        
	      <tr>
	       <td><!--  <button type="button" class="btn btn-default close-btn" data-dismiss="modal">Clear</button> --></td>
	       <td><!--  <button type="button" class="btn btn-info" id="uploadExamFile" onclick="uploadExamFile()">Upload Exam</button> -->
	        <s:submit value="Upload Exam" /> </td>
	 </tr>
	 </table>
	 </s:form>
	</div>
<br><br>
<div class="panel-group" id="accordion">
<table border="1">
	<thead>
	<tr>
	<td>Sr. No.</td>
	<!--  <td> <input type="checkbox" class="chk" name="selectAll" id="selectAll" data-label="selectAll">Select All</<input></td>-->
	<td>Division </td>
	<td>Teacher </td>
	<td>Subject </td>
	<td>File Path</td>
	<td>Date of creation</td>
	</tr>
	</thead> 
	 <%
  int i = 0;
  if(null != list){
	  Iterator iteratorList = list.iterator();
	  while(iteratorList.hasNext()){
	  MCQDetails mcqDetails = (MCQDetails)iteratorList.next();	
  %>
  <tr>
  </tr>
   <%
  i++;
  
	  } 		
 } %>
 </table>
</div>
