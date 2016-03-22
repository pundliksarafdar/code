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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.classapp.db.batch.division.Division;
import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.classapp.logger.AppLogger;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.ExamData;
import com.datalayer.exam.ParagraphQuestion;
import com.datalayer.exam.QuestionData;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class AddQuestionAction extends BaseAction{
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
	String searchedTopic;
	String institute;
	String quesstatus;
	List<Topics> topics;
	int topicID;
	int selectedtopicID;
	int optionImageCount[];
	int optionImageEndCount[];
	String selectedtopicName;
	String questiontype;
	/*Edit exam question*/
	String optionImagesPrev[];
	String questionImagesStr[];
	List<Division> divisions;
	List<Subject> subjects;
	String paraQuestions[];
	String paraPerQuestionMarks[];
	String paragraphText;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		if(institute!=null && !"".equals(institute)){
			UserStatic userStatic = userBean.getUserStatic();
			String storagePath = Constants.STORAGE_PATH+File.separator+institute;
			userStatic.setStorageSpace(storagePath);
			inst_id=Integer.parseInt(institute);
		}
		
		InstituteStatTransaction transaction = new InstituteStatTransaction();
		if(transaction.getStats(inst_id).getAvail_memory()<0){
			addActionError("Running out of memmory");
			return ERROR;
		}
		
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		//topics=subjectTransaction.getTopics(inst_id, Integer.parseInt(subject), Integer.parseInt(division));
		DivisionTransactions divisionTransaction = new DivisionTransactions();
		divisions = divisionTransaction.getAllDivisions(inst_id);
		if(null == actionname){
			
			actionname="submitquestions";
			if(userBean.getRole()==2){
				return "teacheraddquestion";
			}
			return "startaddingquestion";
		}else if("submitquestions".equals(actionname)){
			/*if(null != request.getSession().getAttribute("questionNumber")){
				questionNumber = (Integer) request.getSession().getAttribute("questionNumber");
			}else{
				questionNumber = 0;
			}*/
			String result = "startaddingquestion";
			if(userBean.getRole()==2){
				result= "teacheraddquestion";
			}	
			QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
			if("1".equals(questiontype)){
				Questionbank questionbank = new Questionbank();
				questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
				questionbank.setDiv_id(Integer.parseInt(division));
				questionbank.setExam_rep("1");
				questionbank.setInst_id(inst_id);
				questionbank.setMarks(questionmarks);
				questionbank.setQue_id(questionNumber);
				questionbank.setRep(0);
				questionbank.setSub_id(Integer.parseInt(subject));
				questionbank.setTopic_id(topicID);
				questionbank.setQues_status("");
				questionbank.setQue_text(question);
				questionbank.setQue_type(questiontype);
				bankTransaction.saveQuestion(questionbank);
			}else if("2".equals(questiontype)){
			if("submitquestions".equals(actionname)){
			//questionNumber=bankTransaction.getNextQuestionID(inst_id,Integer.parseInt(subject), Integer.parseInt(division));
			}
			subjectTransaction=new SubjectTransaction();
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
			questionData.setOptionImageCount(optionImageCount);
			//questionData.setQuestionNumber(questionNumber);
			
			List<String> questionImagesList = new ArrayList<String>();
			List<String> answerImagesList = new ArrayList<String>();
			
			if(null!=questionImagesStr){
				questionImagesList = Arrays.asList(questionImagesStr);
			}
			
			questionData.setQuestionImage(questionImagesList);
			if(optionImagesPrev!=null){
				answerImagesList = Arrays.asList(optionImagesPrev);
			}
			questionData.setAnswerImage(answerImagesList);
			
			//Create separate file for each question and join them in the save and submit
			UserStatic userStatic = userBean.getUserStatic();
			String examPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
			File file = new File(examPath);
			
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
			}
			writeObject(examPath, questionData);
			Questionbank questionbank=new Questionbank();
			questionbank.setAdded_by(userBean.getRegId());
			if(null!=answersOptionCheckBox){
				questionbank.setAns_id(answersOptionCheckBox.toString());
			}
			questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
			questionbank.setDiv_id(Integer.parseInt(division));
			questionbank.setExam_rep("1");
			questionbank.setInst_id(inst_id);
			questionbank.setMarks(questionmarks);
			questionbank.setQue_id(questionNumber);
			questionbank.setRep(0);
			questionbank.setSub_id(Integer.parseInt(subject));
			questionbank.setTopic_id(topicID);
			questionbank.setQues_status("");
			questionbank.setQue_text(question);
			if(answersOptionText.length>0){
				for (int i = 0; i < answersOptionText.length; i++) {
					switch (i) {
					case 0:
						questionbank.setOpt_1(answersOptionText[i]);
						break;
					case 1:
						questionbank.setOpt_2(answersOptionText[i]);
						break;
					case 2:
						questionbank.setOpt_3(answersOptionText[i]);
						break;		
					case 3:
						questionbank.setOpt_4(answersOptionText[i]);
						break;
					case 4:
						questionbank.setOpt_5(answersOptionText[i]);
						break;
					case 5:
						questionbank.setOpt_6(answersOptionText[i]);
						break;
					case 6:
						questionbank.setOpt_7(answersOptionText[i]);
						break;
					case 7:
						questionbank.setOpt_8(answersOptionText[i]);
						break;
					case 8:
						questionbank.setOpt_9(answersOptionText[i]);
						break;
					case 9:
						questionbank.setOpt_10(answersOptionText[i]);
						break;
					}
				}
			}
			questionbank.setQue_type(questiontype);
			bankTransaction.saveQuestion(questionbank);
			}else if("3".equals(questiontype)){
				Questionbank questionbank = new Questionbank();
				questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
				questionbank.setDiv_id(Integer.parseInt(division));
				questionbank.setExam_rep("1");
				questionbank.setInst_id(inst_id);
				questionbank.setMarks(questionmarks);
				questionbank.setSub_id(Integer.parseInt(subject));
				questionbank.setTopic_id(topicID);
				questionbank.setQues_status("");
				questionbank.setQue_type(questiontype);
				questionbank.setAdded_by(userBean.getRegId());
				questionNumber=  bankTransaction.saveQuestion(questionbank);
				ParagraphQuestion paragraphQuestion =new ParagraphQuestion();
				paragraphQuestion.setParagraphText(paragraphText);
				paragraphQuestion.setParaPerQuestionMarks(paraPerQuestionMarks);
				paragraphQuestion.setParaQuestions(paraQuestions);
				paragraphQuestion.setQuestionmarks(questionmarks);
				paragraphQuestion.setQuestionnumber(questionNumber);
				UserStatic userStatic = userBean.getUserStatic();
				String examPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
				File file = new File(examPath);
				
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
					try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
				}
				writeObject(examPath, paragraphQuestion);
			}
			if(currentPage!=0){
				result="questioneditsuccess";
			}
			topics=subjectTransaction.getTopics(inst_id, Integer.parseInt(subject), Integer.parseInt(division));
			subjects=subjectTransaction.getSubjectRelatedToDiv(Integer.parseInt(division), inst_id);
			quesstatus = "success";
			return result;
		}else if("navigatequestions".equals(actionname)||"editquestion".equals(actionname)){
			UserStatic userStatic = userBean.getUserStatic();
			 subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			examname = (String) request.getSession().getAttribute("examname");
			String examPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
			//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
			File file = new File(examPath);
			
			
			QuestionData questionData = null;
			if(file.exists()){
				questionData = (QuestionData) readObject(file);
			}
			
			int endCount = 0;
			int index = 0;
			optionImageCount = questionData.getOptionImageCount();
			if(null != optionImageCount){
				optionImageEndCount = new int[optionImageCount.length];
				for(int count:optionImageCount){
					endCount = endCount+count;
					optionImageEndCount[index]=endCount;
					index++;
				}
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
			QuestionBankTransaction questionBankTransaction=new QuestionBankTransaction();
			Questionbank questionbank=questionBankTransaction.getQuestion(questionNumber, inst_id,Integer.parseInt(subject), Integer.parseInt(division));
			selectedtopicID=questionbank.getTopic_id();
			if(topics!=null){
				for(int i = 0; i < topics.size(); i++) {
					if(topics.get(i).getTopic_id()==selectedtopicID){
						selectedtopicName=topics.get(i).getTopic_name();
						break;
					}
				}
			}
			request.setAttribute("questionData", questionData);
			actionname="SavenSubmit";
			String result = "startaddingquestion";
			if(userBean.getRole()==2){
				result= "teacheraddquestion";
			}
			return result;
		}else if("SavenSubmit".equals(actionname)){
			 subjectTransaction=new SubjectTransaction();
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
			List<String> questionImagesList;
			if(null!=questionImagesStr){
				questionImagesList = Arrays.asList(questionImagesStr);
			}else{
				questionImagesList = new ArrayList<String>();
			}
			
			if(null!=answersOptionCheckBox){
				questionData.setAnswers(Arrays.asList(answersOptionCheckBox.split(",")));
			}
			questionData.setQuestionImage(questionImagesList);
			questionData.setOptions(listOption);
			questionData.setQuestion(question);
			questionData.setQuestionNumber(questionNumber);
			questionData.setOptionImageCount(optionImageCount);
			if(null!=optionImagesPrev){
				questionData.setAnswerImage(Arrays.asList(optionImagesPrev));
			}else{
				questionData.setAnswerImage(new ArrayList<String>());
			}
			//Create separate file for each question and join them in the save and submit
			UserStatic userStatic = userBean.getUserStatic();
			String examPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
			File file = new File(examPath);
			
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
				try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
			}
			writeObject(examPath, questionData);
			QuestionBankTransaction questionBankTransaction=new QuestionBankTransaction();
			Questionbank questionbank=questionBankTransaction.getQuestion(questionNumber, inst_id,Integer.parseInt(subject), Integer.parseInt(division));
			if(null!=answersOptionCheckBox){
				questionbank.setAns_id(answersOptionCheckBox.toString());
			}
			questionbank.setMarks(questionmarks);
			questionbank.setTopic_id(topicID);
			questionBankTransaction.saveQuestion(questionbank);
			
			actionname="SavenSubmit";
			String result = "questioneditsuccess";
			return result;
		}else if("deletequestion".equals(actionname)){
			if("".equals(quesstatus)){
			UserStatic userStatic = userBean.getUserStatic();
			 subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			String questionPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
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
			bankTransaction.deleteQuestion(questionNumber, inst_id, Integer.parseInt(subject), Integer.parseInt(division));
			}else{
				QuestionBankTransaction questionBankTransaction=new QuestionBankTransaction();
				Questionbank questionbank=questionBankTransaction.getQuestion(questionNumber, inst_id,Integer.parseInt(subject), Integer.parseInt(division));
				questionbank.setQues_status("N");
				questionBankTransaction.saveQuestion(questionbank);
			}
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

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public String getQuesstatus() {
		return quesstatus;
	}

	public void setQuesstatus(String quesstatus) {
		this.quesstatus = quesstatus;
	}

	public List<Topics> getTopics() {
		return topics;
	}

	public void setTopics(List<Topics> topics) {
		this.topics = topics;
	}

	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public int getSelectedtopicID() {
		return selectedtopicID;
	}

	public void setSelectedtopicID(int selectedtopicID) {
		this.selectedtopicID = selectedtopicID;
	}

	public String getSelectedtopicName() {
		return selectedtopicName;
	}

	public void setSelectedtopicName(String selectedtopicName) {
		this.selectedtopicName = selectedtopicName;
	}

	public String getSearchedTopic() {
		return searchedTopic;
	}

	public void setSearchedTopic(String searchedTopic) {
		this.searchedTopic = searchedTopic;
	}

	public int[] getOptionImageCount() {
		return optionImageCount;
	}

	public void setOptionImageCount(int[] optionImageCount) {
		this.optionImageCount = optionImageCount;
	}

	public int[] getOptionImageEndCount() {
		return optionImageEndCount;
	}

	public void setOptionImageEndCount(int[] optionImageEndCount) {
		this.optionImageEndCount = optionImageEndCount;
	}

	public String[] getOptionImagesPrev() {
		return optionImagesPrev;
	}

	public void setOptionImagesPrev(String[] optionImagesPrev) {
		this.optionImagesPrev = optionImagesPrev;
	}

	public String[] getQuestionImagesStr() {
		return questionImagesStr;
	}

	public void setQuestionImagesStr(String[] questionImagesStr) {
		this.questionImagesStr = questionImagesStr;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}

	public String getQueationtype() {
		return questiontype;
	}

	public void setQueationtype(String questiontype) {
		this.questiontype = questiontype;
	}



	public String[] getParaQuestions() {
		return paraQuestions;
	}

	public void setParaQuestions(String[] paraQuestions) {
		this.paraQuestions = paraQuestions;
	}

	public String[] getParaPerQuestionMarks() {
		return paraPerQuestionMarks;
	}

	public void setParaPerQuestionMarks(String[] paraPerQuestionMarks) {
		this.paraPerQuestionMarks = paraPerQuestionMarks;
	}

	public String getParagraphText() {
		return paragraphText;
	}

	public void setParagraphText(String paragraphText) {
		this.paragraphText = paragraphText;
	}
	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
	
}
