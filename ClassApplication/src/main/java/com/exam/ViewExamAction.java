package com.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.CompExam;
import com.classapp.db.exam.Exam;

import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class ViewExamAction extends BaseAction{
	String division,batch,subject;
	List<CompExam> compExams;
	List<Integer> marks;
	List<Integer> repeatation;
	String selectedMarks="-1";
	String selectedExamID="-1";
	String selectedRep="-1";
	String subjectname;
	String divisionName;
	List<QuestionData> questionDataList;
	int totalPages;
	int currentPage;
	String searchedMarks;
	String searchedExam;
	String searchedRep;
	String questionedit;
	String actionname;
	List<Integer> questionIds;
	String addedIds;
	String removedIds;
	int totalmarks;
	int noofquestions;
	String forwardhref;
	String institute;
	int examID;
	String examname;
	int searchcurrentPage;
	int searchtotalPages;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		 if("viewcomplete".equals(actionname)){
				currentPage=searchcurrentPage;
				totalPages=searchtotalPages;
				return "viewcomplete";
			}
		int inst_id=userBean.getRegId();
		if(institute!=null && !"".equals(institute)){
			UserStatic userStatic = userBean.getUserStatic();
			String storagePath = Constants.STORAGE_PATH+File.separator+institute;
			userStatic.setStorageSpace(storagePath);
			inst_id=Integer.parseInt(institute);
		}
		List<Integer> questionIdsList=(List<Integer>) session.get("viewquestionsIds");
		if("viewexam".equals(actionname)){
			questionIdsList=new ArrayList<Integer>();
			ExamTransaction examTransaction=new ExamTransaction();
			Exam exam=examTransaction.getExamToEdit(inst_id, Integer.parseInt(subject), Integer.parseInt(division), examID);
			String quesids[]=exam.getQue_ids().split(",");
			for (int i = 0; i < quesids.length; i++) {
				questionIdsList.add(Integer.parseInt(quesids[i]));
			}
			totalmarks=exam.getTotal_marks();
			examname=exam.getExam_name();
			session.put("viewquestionsIds",questionIdsList);
			
		}
		noofquestions=questionIdsList.size();
		totalPages=noofquestions/10;
		int remainder=noofquestions%10;
		if(remainder>0){
			totalPages++;
		}
		
		ExamTransaction examTransaction=new ExamTransaction();
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		compExams=examTransaction.getAllCompExamList();
		marks=bankTransaction.getDistinctQuestionMarks(Integer.parseInt(subject), Integer.parseInt(division), inst_id);
		repeatation=bankTransaction.getDistinctQuestionRep(Integer.parseInt(subject), Integer.parseInt(division), inst_id);

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
			if(currentPage==0){
				currentPage++;
			}
			if(questionIdsList!=null){
				questionDataList=new ArrayList<QuestionData>();
				UserStatic userStatic = userBean.getUserStatic();
				String questionPath = "";
				int startIndex=(currentPage-1)*10;
				int endIndex=startIndex+10;
				if(endIndex>noofquestions){
					endIndex=noofquestions;
				}
				//Set<Integer> keys=questionIdsMap.keySet();
				for (int i = startIndex; i < endIndex; i++) {
					questionPath=userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionIdsList.get(i);
					QuestionData questionData=(QuestionData) readObject(new File(questionPath));
					questionDataList.add(questionData);
				}
			}
			
	
		if(userBean.getRole()==2){
			return "teacherviewexam";
		}
		return "viewexam";
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
	
	public String getDivision() {
		return division;
	}

	public List<CompExam> getCompExams() {
		return compExams;
	}

	public void setCompExams(List<CompExam> compExams) {
		this.compExams = compExams;
	}

	public List<Integer> getMarks() {
		return marks;
	}

	public void setMarks(List<Integer> marks) {
		this.marks = marks;
	}

	public void setDivision(String division) {
		this.division = division;
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

	public List<Integer> getRepeatation() {
		return repeatation;
	}

	public void setRepeatation(List<Integer> repeatation) {
		this.repeatation = repeatation;
	}

	public String getSelectedMarks() {
		return selectedMarks;
	}

	public void setSelectedMarks(String selectedMarks) {
		this.selectedMarks = selectedMarks;
	}

	public String getSelectedExamID() {
		return selectedExamID;
	}

	public void setSelectedExamID(String selectedExamID) {
		this.selectedExamID = selectedExamID;
	}

	public String getSelectedRep() {
		return selectedRep;
	}

	public void setSelectedRep(String selectedRep) {
		this.selectedRep = selectedRep;
	}

	public List<QuestionData> getQuestionDataList() {
		return questionDataList;
	}

	public void setQuestionDataList(List<QuestionData> questionDataList) {
		this.questionDataList = questionDataList;
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

	public String getQuestionedit() {
		return questionedit;
	}

	public void setQuestionedit(String questionedit) {
		this.questionedit = questionedit;
	}

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public List<Integer> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Integer> questionIds) {
		this.questionIds = questionIds;
	}

	public String getAddedIds() {
		return addedIds;
	}

	public void setAddedIds(String addedIds) {
		this.addedIds = addedIds;
	}

	public String getRemovedIds() {
		return removedIds;
	}

	public void setRemovedIds(String removedIds) {
		this.removedIds = removedIds;
	}

	public int getTotalmarks() {
		return totalmarks;
	}

	public void setTotalmarks(int totalmarks) {
		this.totalmarks = totalmarks;
	}

	public int getNoofquestions() {
		return noofquestions;
	}

	public void setNoofquestions(int noofquestions) {
		this.noofquestions = noofquestions;
	}

	public String getForwardhref() {
		return forwardhref;
	}

	public void setForwardhref(String forwardhref) {
		this.forwardhref = forwardhref;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public int getExamID() {
		return examID;
	}

	public void setExamID(int examID) {
		this.examID = examID;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String examname) {
		this.examname = examname;
	}

	public int getSearchcurrentPage() {
		return searchcurrentPage;
	}

	public void setSearchcurrentPage(int searchcurrentPage) {
		this.searchcurrentPage = searchcurrentPage;
	}

	public int getSearchtotalPages() {
		return searchtotalPages;
	}

	public void setSearchtotalPages(int searchtotalPages) {
		this.searchtotalPages = searchtotalPages;
	}
	
	
}
