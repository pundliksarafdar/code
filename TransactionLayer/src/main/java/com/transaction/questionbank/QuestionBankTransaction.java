package com.transaction.questionbank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.classapp.db.question.Questionbank;
import com.classapp.db.question.QuestionbankDB;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import com.datalayer.exam.MCQuestion;
import com.datalayer.exam.Question;
import com.datalayer.exam.SubjectiveQuestion;
import com.classapp.logger.AppLogger;
import com.service.beans.ParaQuestionBean;
import com.transaction.image.ImageTransactions;

public class QuestionBankTransaction {

	public int getNextQuestionID(int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getNextQuestionID(inst_id, div_id, sub_id);
	}
	
	public List<Integer> saveQuestionsInBulk(List<Question> questionBeanList, String questionType, int divId, int subId, int regID){
		
		List<Integer> invalidQuestionList=new ArrayList<Integer>();
		for (Question question : questionBeanList) {
			Questionbank questionbank= new Questionbank();
			int questionNumber=0;
			//Subjective question
			if(questionType.equals("1")){
				SubjectiveQuestion subjectiveQuestion=(SubjectiveQuestion) question;
				
				questionbank.setAdded_by(regID);
				questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
				questionbank.setDiv_id(divId);
				questionbank.setSub_id(subId);
				questionbank.setInst_id(regID);
				questionbank.setMarks((int)subjectiveQuestion.getMarks());
				questionbank.setQue_type(questionType);
				questionbank.setQue_text(subjectiveQuestion.getQuestion());
				questionbank.setQues_status("");
				questionNumber=subjectiveQuestion.getQuestionNumber();				
			}else //Objective question
				if(questionType.equals("2")){
					MCQuestion mcQuestion=(MCQuestion) question;					
					questionbank.setAdded_by(regID);
					questionbank.setAns_id(mcQuestion.getCorrectAnswer());
					questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
					questionbank.setDiv_id(divId);
					questionbank.setSub_id(subId);
					questionbank.setInst_id(regID);
					questionbank.setMarks((int)mcQuestion.getMarks());
					questionbank.setQue_type(questionType);
					questionbank.setQue_text(mcQuestion.getQuestion());
					HashMap<String, String> optionMap=mcQuestion.getOptions();
					questionbank.setOpt_1(optionMap.get("1"));
					questionbank.setOpt_2(optionMap.get("2"));
					questionbank.setOpt_3(optionMap.get("3"));
					questionbank.setOpt_4(optionMap.get("4"));
					questionbank.setOpt_5(optionMap.get("5"));
					questionbank.setOpt_6(optionMap.get("6"));
					questionbank.setOpt_7(optionMap.get("7"));
					questionbank.setOpt_8(optionMap.get("8"));
					questionbank.setOpt_9(optionMap.get("9"));
					questionbank.setOpt_10(optionMap.get("10"));
					questionbank.setQues_status("");
					questionNumber=mcQuestion.getQuestionNumber();
			}
			int questionId=saveQuestion(questionbank);
			if(questionId==0){
				invalidQuestionList.add(questionNumber);
			}
		}
		return invalidQuestionList;
	}
	
	public int saveQuestion(Questionbank questionbank) {
		QuestionbankDB db = new QuestionbankDB();
		return db.saveQuestion(questionbank);
		
	}
	
	public boolean editQuestion(Questionbank questionbank) {
		QuestionbankDB db = new QuestionbankDB();
		return db.editQuestion(questionbank);
		
	}
	
	public List<Integer> getDistinctQuestionMarks(int sub_id,int div_id,int inst_id,String ques_type) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getdistinctQuestionMarks(sub_id, inst_id, div_id,ques_type);
	}
	
	public List<Integer> getDistinctQuestionRep(int sub_id,int div_id,int inst_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getdistinctQuestionRep(sub_id, inst_id, div_id);
	}
	
	public List<Questionbank> getSearchedQuestions(int marks,int sub_id,int inst_id,int div_id,int currentPage,int topic_id,String quesType) {
		QuestionbankDB db=new QuestionbankDB();
		return db.getSearchedQuestion( marks, sub_id, inst_id, div_id,currentPage,topic_id,quesType);
	}
	
	public int getTotalSearchedQuestionCount(int marks,int sub_id,int inst_id,int div_id,int topic_id,String quesType) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getSearchedQuestionCount(marks, sub_id, inst_id, div_id,topic_id,quesType);
	}
	
	public boolean deleteQuestion(int que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.deleteQuestion(que_id, inst_id, sub_id, div_id);
		return true;
	}
	
	public boolean updateDeleteQuestionStatus(int que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateDeleteQuestionStatus(que_id, inst_id, sub_id, div_id);
		return true;
	}
	
	public boolean updateSubjectiveQuestion(int que_id,int inst_id,int sub_id,int div_id, String que_text, int marks,int topic_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateSubjectiveQuestion(que_id, inst_id, sub_id, div_id, que_text, marks,topic_id);
		return true;
	}
	
	public boolean updateObjectiveQuestion(Questionbank questionbank) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateObjectiveQuestion(questionbank);
		return true;
	}
	
	public boolean updateParagraphQuestion(int que_id,int inst_id,int sub_id,int div_id, int marks,int topic_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateParagraphQuestion(que_id, inst_id, sub_id, div_id, marks,topic_id);
		return true;
	}
	
	public Questionbank getQuestion(int que_id,int inst_id,int sub_id,int div_id) {
	QuestionbankDB questionbankDB=new QuestionbankDB();
	return questionbankDB.getQuestion(que_id, inst_id, sub_id, div_id);
	}
	
	public List<Integer> getQuestionMarks(int inst_id,int sub_id,int div_id,List<Integer> que_id) {
		QuestionbankDB db=new QuestionbankDB();
		return db.getQuestionMarks(inst_id, sub_id, div_id, que_id);
	}
	public List<Integer> getQuestionrelatedtoTopics(int sub_id,int inst_id,int div_id,int topic_id){
		QuestionbankDB db=new QuestionbankDB();
		return db.getQuestionrelatedtoTopics(sub_id, inst_id, div_id, topic_id);
	}
	
	public boolean ExamQuestionStatus(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB db=new QuestionbankDB();
		return db.ExamQuestionStatus(que_id, inst_id, sub_id, div_id);
	}
		
		public boolean deleteQuestionList(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.deleteQuestionList(que_id, inst_id, sub_id, div_id);
	}
		
		public List<Integer> getDisabledQuestions(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.getDisabledQuestions(que_id, inst_id, sub_id, div_id);
	}
		public List<Integer> getQuestionrelatedtoSubject(int inst_id,int sub_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.getQuestionrelatedtoSubject(sub_id, inst_id);
	}
		
		public List<Integer> getQuestionrelatedtoClass(int inst_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.getQuestionrelatedtoClass(inst_id, div_id);
	}
		
		public boolean deleteQuestionrelatedtoClass(int inst_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.deleteQuestionrelatedtoClass(inst_id, div_id);
	}
		
		public boolean deleteQuestionrelatedtoSubject(int inst_id,int sub_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.deleteQuestionrelatedtosubject(inst_id, sub_id);
	}
		
		public boolean updateQuestionrelatedtoTopic(int inst_id,int sub_id,int div_id,int topic_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.updateQuestionrelatedtotopic(inst_id, sub_id, div_id, topic_id);
	}
		
		public boolean saveParaObject(String storagePath,ParaQuestionBean paraQuestionBean,int questionId){
			String questionpath = storagePath + File.separatorChar + paraQuestionBean.getInstId() + File.separatorChar
					+ "exam" + File.separatorChar + "paragraph"+ File.separatorChar+paraQuestionBean.getClassId()+ File.separatorChar+paraQuestionBean.getSubjectId() + File.separatorChar + questionId ;
			File file = new File(questionpath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
			}
			writeObject(questionpath, paraQuestionBean);
			
			ImageTransactions imageTransactions = new ImageTransactions(storagePath);
			imageTransactions.saveParagraphImage(paraQuestionBean, questionId, paraQuestionBean.getInstId());
			return true;
		}
		
		private void writeObject(String filePath,Object questionData){
			FileOutputStream fout = null;
			ObjectOutputStream oos = null;
			try {
				File file = new File(filePath);
				if(!file.exists() || file.isDirectory()){
					if(file.isDirectory()){
						delete(file);
					}
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				fout = new FileOutputStream(filePath);
				oos = new ObjectOutputStream(fout);
				oos.writeObject(questionData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(null!=oos)try {oos.close();} catch (IOException e) {e.printStackTrace();}
				if(null!=fout)try {fout.close();} catch (IOException e) {e.printStackTrace();}
			}
			
		}
		
		public static void delete(File file)
		    	throws IOException{
		 
		    	if(file.isDirectory()){
		 
		    		//directory is empty, then delete it
		    		if(file.list().length==0){
		 
		    		   file.delete();
		    		   AppLogger.logger("Directory is deleted : " 
		                                                 + file.getAbsolutePath());
		 
		    		}else{
		 
		    		   //list all the directory contents
		        	   String files[] = file.list();
		 
		        	   for (String temp : files) {
		        	      //construct the file structure
		        	      File fileDelete = new File(file, temp);
		 
		        	      //recursive delete
		        	     delete(fileDelete);
		        	   }
		 
		        	   //check the directory again, if empty then delete it
		        	   if(file.list().length==0){
		           	     file.delete();
		        	     AppLogger.logger("Directory is deleted : " 
		                                                  + file.getAbsolutePath());
		        	   }
		    		}
		 
		    	}else{
		    		//if file, then delete it
		    		file.delete();
		    		AppLogger.logger("File is deleted : " + file.getAbsolutePath());
		    	}
		    }

		
}
