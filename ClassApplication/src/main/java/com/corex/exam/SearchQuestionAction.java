package com.corex.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.CompExam;

import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class SearchQuestionAction extends BaseAction{
	String division,batch,subject;
	List<CompExam> compExams;
	List<Integer> marks;
	List<Integer> repeatation;
	String selectedMarks="-1";
	String selectedExamID="-1";
	String selectedRep="-1";
	String selectedTopic="-1";
	String subjectname;
	String divisionName;
	List<QuestionData> questionDataList;
	int totalPages;
	int currentPage;
	String searchedMarks;
	String searchedExam;
	String searchedRep;
	String searchedTopic;
	String questionedit;
	String actionname;
	String institute;
	int paginationstartindex;
	int paginationendindex;
	int role;
	List<Integer> createdIds;
	List<Topics> topics;
	List<Division> divisions;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		DivisionTransactions divisionTransaction = new DivisionTransactions();
		divisions=divisionTransaction.getAllDivisions(inst_id);
		/*role=userBean.getRole();
		if(institute!=null && !"".equals(institute)){
			UserStatic userStatic = userBean.getUserStatic();
			String storagePath = Constants.STORAGE_PATH+File.separator+institute;
			userStatic.setStorageSpace(storagePath);
			inst_id=Integer.parseInt(institute);
		}
		ExamTransaction examTransaction=new ExamTransaction();
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		compExams=examTransaction.getAllCompExamList();
		marks=bankTransaction.getDistinctQuestionMarks(Integer.parseInt(subject), Integer.parseInt(division), inst_id);
		repeatation=bankTransaction.getDistinctQuestionRep(Integer.parseInt(subject), Integer.parseInt(division), inst_id);
		topics=subjectTransaction.getTopics(inst_id, Integer.parseInt(subject), Integer.parseInt(division));
		if(!"cancleuploading".equals(actionname)){
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
		if(currentPage==0){
			searchedExam=selectedExamID;
			searchedMarks=selectedMarks;
			searchedRep=selectedRep;
			searchedTopic=selectedTopic;
		}
		
		if(currentPage==0){
			int totalCount=bankTransaction.getTotalSearchedQuestionCount(Integer.parseInt(selectedRep), selectedExamID, Integer.parseInt(selectedMarks), Integer.parseInt(subject), inst_id, Integer.parseInt(division),Integer.parseInt(selectedTopic));
			if(totalCount>0){
				int remainder=totalCount%50;
				totalPages=totalCount/50;
				if(remainder>0){
					totalPages++;
				}
			}	
			currentPage=1;
			}
			if("true".equals(questionedit)){
				int totalCount=bankTransaction.getTotalSearchedQuestionCount(Integer.parseInt(searchedRep), searchedExam, Integer.parseInt(searchedMarks), Integer.parseInt(subject), inst_id, Integer.parseInt(division),Integer.parseInt(searchedTopic));
				if(totalCount>0){
					int remainder=totalCount%50;
					totalPages=totalCount/50;
					if(remainder>0){
						totalPages++;
					}
				}else{
					totalPages=0;
				}	
			}
			if(totalPages<currentPage){
				currentPage--;
			}
			paginationstartindex=1;
		if(currentPage>3){
			paginationstartindex=currentPage-2;
			
		}
		paginationendindex=paginationstartindex+4;
		if(paginationendindex>totalPages){
			paginationendindex=totalPages;
			if(currentPage==totalPages){
				paginationstartindex=currentPage-4;
			}else{
				paginationstartindex=currentPage-3;
			}
			if(paginationstartindex<0){
				paginationstartindex=1;
			}
		}
		List<Questionbank> questionbanks=bankTransaction.getSearchedQuestions(Integer.parseInt(searchedRep), searchedExam, Integer.parseInt(searchedMarks), Integer.parseInt(subject), inst_id, Integer.parseInt(division),currentPage,Integer.parseInt(searchedTopic));
		UserStatic userStatic = userBean.getUserStatic();
		String questionPath = "";
		if(questionbanks!=null)
		{
			createdIds=new ArrayList<Integer>();
			questionDataList=new ArrayList<QuestionData>();
			for (int i = 0; i < questionbanks.size(); i++) {
				questionPath=userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionbanks.get(i).getQue_id();
				QuestionData questionData=(QuestionData) readObject(new File(questionPath));
				createdIds.add(questionbanks.get(i).getAdded_by());
				questionDataList.add(questionData);
			}
		}
		
		}
		if(userBean.getRole()==2){
			return "teacherquestionsearch";
		}*/
		return SUCCESS;
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

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public int getPaginationstartindex() {
		return paginationstartindex;
	}

	public void setPaginationstartindex(int paginationstartindex) {
		this.paginationstartindex = paginationstartindex;
	}

	public int getPaginationendindex() {
		return paginationendindex;
	}

	public void setPaginationendindex(int paginationendindex) {
		this.paginationendindex = paginationendindex;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public List<Integer> getCreatedIds() {
		return createdIds;
	}

	public void setCreatedIds(List<Integer> createdIds) {
		this.createdIds = createdIds;
	}

	public List<Topics> getTopics() {
		return topics;
	}

	public void setTopics(List<Topics> topics) {
		this.topics = topics;
	}

	public String getSelectedTopic() {
		return selectedTopic;
	}

	public void setSelectedTopic(String selectedTopic) {
		this.selectedTopic = selectedTopic;
	}

	public String getSearchedTopic() {
		return searchedTopic;
	}

	public void setSearchedTopic(String searchedTopic) {
		this.searchedTopic = searchedTopic;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}
	
	
}
