package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.datalayer.exam.MCQuestionPaper;
import com.datalayer.exam.Question;
import com.datalayer.exam.SubjectiveQuestionPaper;
import com.excel.student.StudentExcelData;
import com.service.beans.QuestionExcelUploadBean;
import com.service.beans.StudentExcelUploadBean;
import com.service.helper.NotificationServiceHelper;
import com.transaction.questionbank.QuestionBankTransaction;


@Path("/files")
public class ExcelUploadServiceImpl extends ServiceBase{
	
	@Context
	private HttpServletRequest request;
	
	@GET
	@Path("/test")
	public Response serviceOn() {
		return Response.status(200).entity("Service OK!..").build();
	}

		@POST
	    @Path("/upload/xls/{fileName}/{questiontype}/{divId}/{subId}/")
		@Produces(MediaType.APPLICATION_JSON)
	    public Response uploadQuestionExcelFile(@PathParam("fileName") String fileName, @PathParam("questiontype") String questionType, @PathParam("divId") int divId, @PathParam("subId") int subId) 
	 	{
			
		 String response="";
		 List<Question> listOfQuestions=null;
		 HashMap<String,ArrayList<String>> invalidQuestionResponseMap=null;
		 //int regId=getRegId();
		 int regId=4;
		 QuestionBankTransaction qbTransaction= new QuestionBankTransaction();
		 
		 	//Subjective question upload via excel
			 if(questionType.equals("1")){
				 fileName="E:\\exam2.xls";
				 SubjectiveQuestionPaper paper= new SubjectiveQuestionPaper(fileName);
				 paper.LoadQuestionPaper();	
				 invalidQuestionResponseMap=paper.getInvalidQuestionResponseMap();
				 listOfQuestions=paper.getQuestions();				 
				 response="Subjective Question Paper uploaded successfully";
			 }
			
			 //Objective question upload via excel
			 if(questionType.equals("2")){
				 fileName="E:\\exam1.xls";
				 MCQuestionPaper paper= new MCQuestionPaper(fileName);
				 paper.LoadQuestionPaper();
				 invalidQuestionResponseMap=paper.getInvalidQuestionResponseMap();
				 listOfQuestions=paper.getQuestions();
				 response="Objective Question Paper uploaded successfully";
			 }
			 
			List<Integer> responseList=qbTransaction.saveQuestionsInBulk(listOfQuestions, questionType, divId, subId, regId);
	        if(responseList.size()==listOfQuestions.size()){
	        	response="Unable to add all Questions. Please verify your input file.";
	        	
	        }else{
	        	if(responseList.size()==0){
	        		response="Successfully added all questions i.e. "+listOfQuestions.size()+" questions";
	        	}else{
	        		response="Successfully added "+(listOfQuestions.size()-responseList.size())+" questions. Unable to add "+responseList.size()+" questions. Correct entry for question number:";
	        		String questionnumberstr="";	        		
		        	for (Integer questionNumber : responseList) {
		        		if(questionnumberstr.equals("")){
		        			questionnumberstr=questionnumberstr+questionNumber;
		        		}else{
		        			questionnumberstr=questionnumberstr+","+questionNumber;
		        		}
					}
		        	response=response+questionnumberstr;
	        	}
	        }
	        
	        ArrayList<String> addedQuestionResponseList=new ArrayList<String>();
	        addedQuestionResponseList.add(response);
	        invalidQuestionResponseMap.put("addedQuestionsResponse",addedQuestionResponseList);
	        return Response.status(Status.OK).entity(invalidQuestionResponseMap).build();
	    }    
		
		@POST
	    @Path("/upload/xls/")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
	    public Response uploadQuestionExcelFile(QuestionExcelUploadBean questionFileBean) 
	 	{
			List<Question> listOfQuestions=null;
			String response="";
			int regId=getRegId();
			HashMap<String,ArrayList<String>> invalidQuestionResponseMap=null;
			QuestionBankTransaction qbTransaction= new QuestionBankTransaction();
		 	//Subjective question upload via excel
			 if(questionFileBean.getQues_type().equals("1")){				 
				 SubjectiveQuestionPaper paper= new SubjectiveQuestionPaper(questionFileBean.getFileName());
				 paper.LoadQuestionPaper();
				 invalidQuestionResponseMap=paper.getInvalidQuestionResponseMap();
				 listOfQuestions=paper.getQuestions();
				 response="Subjective Question Paper uploaded successfully";
			 }
			
			 //Objective question upload via excel
			 if(questionFileBean.getQues_type().equals("2")){				
				 MCQuestionPaper paper= new MCQuestionPaper(questionFileBean.getFileName());
				 paper.LoadQuestionPaper();
				 invalidQuestionResponseMap=paper.getInvalidQuestionResponseMap();
				 listOfQuestions=paper.getQuestions();
				 response="Objective Question Paper uploaded successfully";
			 }
			 List<Integer> responseList=qbTransaction.saveQuestionsInBulk(listOfQuestions, questionFileBean.getQues_type(), questionFileBean.getDiv_id(), questionFileBean.getSub_id(), regId);
		        if(responseList.size()==listOfQuestions.size()){
		        	response="Unable to add questions.";
		        	
		        }else{
		        	if(responseList.size()==0){
		        		 response = "Successfully added " + listOfQuestions.size() + " questions";
		        	}else{
		        		response="Successfully added "+(listOfQuestions.size()-responseList.size())+" questions. Unable to add "+responseList.size()+" questions. Correct entry for question number:";
		        		String questionnumberstr="";	        		
			        	for (Integer questionNumber : responseList) {
			        		if(questionnumberstr.equals("")){
			        			questionnumberstr=questionnumberstr+questionNumber;
			        		}else{
			        			questionnumberstr=questionnumberstr+","+questionNumber;
			        		}
						}
			        	response=response+questionnumberstr;
		        	}
		        }
		        
		        ArrayList<String> addedQuestionResponseList = new ArrayList<String>();
		        addedQuestionResponseList.add(response);
		        invalidQuestionResponseMap.put("addedQuestionsResponse", addedQuestionResponseList);
		        return Response.status((Response.Status)Response.Status.OK).entity(invalidQuestionResponseMap).build();
	    }    
		
				@POST
			    @Path("/upload/student/xls/")
				@Produces(MediaType.APPLICATION_JSON)
				@Consumes(MediaType.APPLICATION_JSON)
				public Response uploadStudentExcelFile(StudentExcelUploadBean studentFileBean) {
			        System.out.println("Start uploadStudentExcelFile:");
			        String response = "";
			        int regId = this.getRegId();
			        HashMap<String, ArrayList<String>> invalidStudentResponseMap = null;
			        StudentExcelData studentData = new StudentExcelData(studentFileBean.getFileName());
			        System.out.println("Loading data..");
			        studentData.loadStudents(regId, studentFileBean.getDivId(), studentFileBean.getBatchId());
			        System.out.println("Loading completed..\n Processing the data..");
			        studentData.processData(regId, studentFileBean.getDivId(), studentFileBean.getBatchId());
			        System.out.println("Processing completed..");
			        invalidStudentResponseMap = studentData.getInvalidStudentResponseMap();
			        response = "Students added successfully";
			        Set<Entry<Integer, Boolean>>  entrySet = studentData.getSuccessStudentMap().entrySet();
			        ArrayList<Integer> studentIds = new ArrayList<Integer>();
			        ArrayList<String> addedStudentResponseList = new ArrayList<String>();
			        int successCount = 0;
			        for (Entry<Integer, Boolean> entry : entrySet) {
			            if (entry.getValue()) {
			                response = "Student with student ID=" + entry.getKey() + " is added successfully";
			                studentIds.add(entry.getKey());
			                ++successCount;
			            } else {
			                response = "Unable to add student with student ID=" + entry.getKey();
			            }
			            addedStudentResponseList.add(response);
			        }
			        NotificationServiceHelper notificationServiceHelper = new NotificationServiceHelper();
			        notificationServiceHelper.sendManualRegistrationNotification(getRegId(), studentIds);
			        notificationServiceHelper.getStudentForFeesPaymentNotification(getRegId(), studentIds);
			        ArrayList<String> suuccessCountList = new ArrayList<String>();
			        suuccessCountList.add("" + successCount + " students added successfully.");
			        invalidStudentResponseMap.put("SUCCESS", suuccessCountList);
			        invalidStudentResponseMap.put("addedStudentsResponse", addedStudentResponseList);
			        return Response.status((Response.Status)Response.Status.OK).entity((Object)invalidStudentResponseMap).build();
			    }
}
