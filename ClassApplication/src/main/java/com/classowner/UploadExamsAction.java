package com.classowner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.mail.search.SubjectTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.CompExam;
import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.datalayer.exam.ExamData;
import com.datalayer.exam.QuestionData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class UploadExamsAction extends BaseAction{
	String actionname,examname,exammarks,question;
	String answersOptionText[],answersOptionCheckBox;
	File optionImages[],questionImages[];
	int uploadedMarks,questionmarks,questionNumber;
	ExamData examData;
	String batchname,subjectname,divisionName;
	String batch,subject,division;
	List<Integer> answerList;
	int indexOption=0;
	int totalPages;
	int currentPage;
	String searchedMarks;
	String searchedExam;
	String searchedRep;
	
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		if(null == actionname){
			actionname="submitquestions";
			return "startuploadingexam";
		}else if("submitquestions".equals(actionname) || "SavenSubmit".equals(actionname)){
			/*if(null != request.getSession().getAttribute("questionNumber")){
				questionNumber = (Integer) request.getSession().getAttribute("questionNumber");
			}else{
				questionNumber = 0;
			}*/
			QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
			if("submitquestions".equals(actionname)){
			questionNumber=bankTransaction.getNextQuestionID(userBean.getRegId(),Integer.parseInt(subject), Integer.parseInt(division));
			}
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			//request.getSession().setAttribute("questionNumber", ++questionNumber);
			
			QuestionData questionData = new QuestionData();
			questionData.setMarks(questionmarks);
			List<String> listOption = new ArrayList<String>();
			if(null!=answersOptionText){
				listOption = Arrays.asList(answersOptionText);
			}
			
			if(null!=answersOptionCheckBox){
				questionData.setAnswers(Arrays.asList(answersOptionCheckBox.split(",")));
			}
			questionData.setOptions(listOption);
			questionData.setQuestion(question);
			questionData.setQuestionNumber(questionNumber);
			String result = "startuploadingexam";
		
			//Create separate file for each question and join them in the save and submit
			UserStatic userStatic = userBean.getUserStatic();
			String examPath = userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionNumber;
			File file = new File(examPath);
			
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
			}
			writeObject(examPath, questionData);
			Questionbank questionbank=new Questionbank();
			questionbank.setAdded_by(userBean.getRegId());
			questionbank.setAns_id(answersOptionCheckBox.toString());
			questionbank.setCreated_dt(new Date(2015, 7, 13));
			questionbank.setDiv_id(Integer.parseInt(division));
			questionbank.setExam_rep("1");
			questionbank.setInst_id(userBean.getRegId());
			questionbank.setMarks(questionmarks);
			questionbank.setQue_id(questionNumber);
			questionbank.setRep(0);
			questionbank.setSub_id(Integer.parseInt(subject));
			bankTransaction.saveQuestion(questionbank);
			if(currentPage!=0){
				result="questioneditsuccess";
			}
			return result;
		}else if("navigatequestions".equals(actionname)||"editquestion".equals(actionname)){
			UserStatic userStatic = userBean.getUserStatic();
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			/*if("editquestion".equals(actionname)){
				
				String examPath = userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionNumber; 
				File file = new File(examPath);
				String[] files = file.list();
				
				int totalQuestionNumbers = files.length;
				int examMarks = 0;
				for (String filename:files) {
					QuestionData questionData = (QuestionData) readObject(new File(examPath+File.separator+filename));
					examMarks+=questionData.getMarks();
				}
				
				request.getSession().setAttribute("examname",examname);
				request.getSession().setAttribute("uploadedMarks",examMarks);
				request.getSession().setAttribute("exammarks",examMarks);
				request.getSession().setAttribute("totalQuestion",totalQuestionNumbers);
				
				questionNumber = 1;
				
			}*/
			examname = (String) request.getSession().getAttribute("examname");
			String examPath = userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionNumber;
			//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
			File file = new File(examPath);
			QuestionData questionData = null;
			if(file.exists()){
				questionData = (QuestionData) readObject(file);
			}
			if (questionData!=null) {
				answerList=new ArrayList<Integer>();
				if (questionData.getAnswers()!=null) {
					for (int i = 0; i < questionData.getAnswers().size(); i++) {
						answerList.add(Integer.parseInt(questionData.getAnswers().get(i).trim()));
					}
				}
				indexOption=questionData.getOptions().size();
			}
			request.setAttribute("questionData", questionData);
			actionname="SavenSubmit";
			String result = "startuploadingexam";
			return result;
		}else if("deletequestion".equals(actionname)){
			UserStatic userStatic = userBean.getUserStatic();
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			String questionPath = userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionNumber;
			//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
			File file = new File(questionPath);
			if(file.exists()){
				try {
					delete(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
			bankTransaction.deleteQuestion(questionNumber, userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
			
			return "questiondelete";
		}else if("cancleuploading".equals(actionname)){
			if (currentPage==0) {
				actionname="cancleuploading";
			}else{
				actionname="";
			}
			return "cancleuploading";
		}else{
			return ERROR;
		}
		
	}
	
	public String saveExam(UserBean userBean,HttpServletRequest request){
		UserStatic userStatic = userBean.getUserStatic();
		String result = "examlandingpage";
		Object totalQuestionNo = request.getSession().getAttribute("totalQuestion");
		int totalQuestion = null!=totalQuestionNo?(Integer)totalQuestionNo:0;
		ArrayList<QuestionData> questionDatas = new ArrayList<QuestionData>();
		for (int i = 1; i <= totalQuestion; i++) {
			String questionsFile = userStatic.getExamPath()+File.separator+examname+File.separator+i;
			File eachQuestionFile = new File(questionsFile);
			QuestionData questionData2 = (QuestionData) readObject(eachQuestionFile);
			questionDatas.add(questionData2);
			eachQuestionFile.delete();
		}
		String finalExamPath = userStatic.getExamPath()+File.separator+examname;
		writeObject(finalExamPath, questionDatas);
		
		request.getSession().removeAttribute("uploadedMarks");
		request.getSession().removeAttribute("questionNumber");
		request.getSession().removeAttribute("uploadedMarks");
		request.getSession().removeAttribute("examname");
		request.getSession().removeAttribute("exammarks");
		request.getSession().removeAttribute("totalQuestion");
		
		request.getSession().setAttribute("totalQuestion", questionNumber);
		return result;
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
	
	private Object readObject(File file) {
		Object object = null;
		FileInputStream fin = null;
		ObjectInputStream objectInputStream = null;
		try{
			fin = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fin);
			object =  objectInputStream.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=objectInputStream)try {objectInputStream.close();} catch (IOException e) {e.printStackTrace();}
			if(null!=fin)try {fin.close();} catch (IOException e) {e.printStackTrace();}
		}
		return object;
	}
	
	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	 
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " 
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
	        	     System.out.println("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
	
	public int splitQuestion(String examPath){
		File file = new File(examPath);
		int questionNumber = 0;
		List<QuestionData> questionDatas = (List<QuestionData>)readObject(file);
		file.delete();
		for (QuestionData questionData:questionDatas) {
			questionNumber++;
			writeObject(examPath+File.separator+questionNumber, questionData);
		}
		
		return 0;
	}
	
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public String getExamname() {
		return examname;
	}
	public void setExamname(String examname) {
		this.examname = examname;
	}
	public String getExammarks() {
		return exammarks;
	}
	public void setExammarks(String exammarks) {
		this.exammarks = exammarks;
	}
	public int getUploadedMarks() {
		return uploadedMarks;
	}
	public void setUploadedMarks(int uploadedMarks) {
		this.uploadedMarks = uploadedMarks;
	}
	public String[] getAnswersOptionText() {
		return answersOptionText;
	}
	public void setAnswersOptionText(String[] answersOptionText) {
		this.answersOptionText = answersOptionText;
	}
	
	public File[] getOptionImages() {
		return optionImages;
	}
	public void setOptionImages(File[] optionImages) {
		this.optionImages = optionImages;
	}
	public File[] getQuestionImages() {
		return questionImages;
	}
	public void setQuestionImages(File[] questionImages) {
		this.questionImages = questionImages;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getQuestionmarks() {
		return questionmarks;
	}
	public void setQuestionmarks(int questionmarks) {
		this.questionmarks = questionmarks;
	}
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public ExamData getExamData() {
		return examData;
	}

	public void setExamData(ExamData examData) {
		this.examData = examData;
	}

	public String getBatchname() {
		return batchname;
	}

	public void setBatchname(String batchname) {
		this.batchname = batchname;
	}

	public String getSubjectname() {
		return subjectname;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getAnswersOptionCheckBox() {
		return answersOptionCheckBox;
	}

	public void setAnswersOptionCheckBox(String answersOptionCheckBox) {
		this.answersOptionCheckBox = answersOptionCheckBox;
	}

	public List<Integer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Integer> answerList) {
		this.answerList = answerList;
	}

	public int getIndexOption() {
		return indexOption;
	}

	public void setIndexOption(int indexOption) {
		this.indexOption = indexOption;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearchedMarks() {
		return searchedMarks;
	}

	public void setSearchedMarks(String searchedMarks) {
		this.searchedMarks = searchedMarks;
	}

	public String getSearchedExam() {
		return searchedExam;
	}

	public void setSearchedExam(String searchedExam) {
		this.searchedExam = searchedExam;
	}

	public String getSearchedRep() {
		return searchedRep;
	}

	public void setSearchedRep(String searchedRep) {
		this.searchedRep = searchedRep;
	}
	
	
}
