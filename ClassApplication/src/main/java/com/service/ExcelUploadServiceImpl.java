package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.datalayer.exam.MCQuestionPaper;
import com.datalayer.exam.Question;
import com.datalayer.exam.SubjectiveQuestionPaper;
import com.service.beans.QuestionExcelUploadBean;
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
			return Response.ok(invalidQuestionResponseMap).build();
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
				return Response.ok(invalidQuestionResponseMap).build();
	    }    
}
