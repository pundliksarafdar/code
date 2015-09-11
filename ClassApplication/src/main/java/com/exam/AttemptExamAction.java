package com.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.Subject;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.datalayer.exam.QuestionData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class AttemptExamAction extends BaseAction {

	String batch,subject,division;
	List<Exam> examlist;
	int currentPage;
	int totalPages;
	int role;
	int examID;
	String subjectname,divisionName;
	List<QuestionData> questionDataList;
	String answers;
	int lastPage;
	List<List<Integer>> currentpageanswers;
	String actionname;
	int Total_Marks;
	int TotalExam_Marks;
	String flag;
	int institute;
	Exam initiateExam;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		if(institute!=0){
			UserStatic userStatic = userBean.getUserStatic();
			String storagePath = Constants.STORAGE_PATH+File.separator+institute;
			userStatic.setStorageSpace(storagePath);
			inst_id=institute;
		}
		if("initiateexam".equals(actionname)){
			ExamTransaction examTransaction=new ExamTransaction();
			initiateExam=examTransaction.getExamToAttempt(inst_id, Integer.parseInt(subject), Integer.parseInt(division), examID);
			}else{
				if("examattempted".equals(actionname) && userBean.getRegId()==3){
					StudentMarks studentMarks=new StudentMarks();
					studentMarks.setDiv_id(Integer.parseInt(division));
					studentMarks.setExam_id(examID);
					studentMarks.setInst_id(inst_id);
					studentMarks.setStudent_id(userBean.getRegId());
					studentMarks.setSub_id(Integer.parseInt(subject));
					StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
					marksTransaction.saveStudentMarks(studentMarks);
				}
		Map<Integer,String>SelectedAnswerIds= (Map<Integer, String>) session.get("SelectedAnswerIds");
		if(SelectedAnswerIds==null){
			SelectedAnswerIds=new HashMap();
		}
		ExamTransaction examTransaction=new ExamTransaction();
		Exam exam =examTransaction.getExamToAttempt(inst_id, Integer.parseInt(subject), Integer.parseInt(division), examID);
		if("examSubmit".equals(actionname)){
			String queid_arr[]=	exam.getQue_ids().split(",");
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		List<Integer> ques_ids=new ArrayList<Integer>();
		for (int i = 0; i < queid_arr.length; i++) {
			ques_ids.add(Integer.parseInt(queid_arr[i]));
		}
		List<Integer> ques_marks=bankTransaction.getQuestionMarks(inst_id, Integer.parseInt(subject), Integer.parseInt(division), ques_ids);
		String finalAnsString="";
		for (int i = 1; i <= SelectedAnswerIds.size(); i++) {
			if(i==1){
			finalAnsString=SelectedAnswerIds.get(i);	
			}else{
			finalAnsString=finalAnsString+"/"+SelectedAnswerIds.get(i);
			}
		}
		String solvedans_arr[]=finalAnsString.split("/");
		String examans_arr[]=exam.getAns_ids().split("/");
		for (int i = 0; i < examans_arr.length; i++) {
			if(examans_arr[i].equals(solvedans_arr[i])){
				Total_Marks=Total_Marks+ques_marks.get(i);
			}
		}
		TotalExam_Marks=exam.getTotal_marks();
		if(Total_Marks>=exam.getPass_marks()){
			flag="Y";
		}else{
			flag="N";
		}
		if(userBean.getRole()==3){
		StudentMarks studentMarks=new StudentMarks();
		studentMarks.setAns_ids(finalAnsString);
		studentMarks.setDiv_id(Integer.parseInt(division));
		studentMarks.setExam_id(examID);
		studentMarks.setInst_id(institute);
		studentMarks.setMarks(Total_Marks);
		studentMarks.setStudent_id(userBean.getRegId());
		studentMarks.setSub_id(Integer.parseInt(subject));
		StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
		marksTransaction.saveStudentMarks(studentMarks);
		}
		SelectedAnswerIds=null;
		session.put("SelectedAnswerIds", SelectedAnswerIds);
		return "examresult";
		}else{
		if(exam!=null){
		String queid_arr[]=	exam.getQue_ids().split(",");
		int totalCount=queid_arr.length;
		if(totalCount>0){
			int remainder=totalCount%2;
			totalPages=totalCount/2;
			if(remainder>0){
				totalPages++;
			}
		}else{
			totalPages=0;
		}
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
		int questionStartIndex=0;
		if(currentPage!=0){
			questionStartIndex=(currentPage-1)*2;
		}
		int questionEndIndex=questionStartIndex+2;
		if (questionEndIndex>totalCount) {
			questionEndIndex=totalCount;
		}
		questionDataList=new ArrayList<QuestionData>();
		while(questionStartIndex<questionEndIndex){
		String questionNumber=queid_arr[questionStartIndex];
		String examPath = userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionNumber;
		//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
		File file = new File(examPath);
		QuestionData questionData = null;
		if(file.exists()){
			questionData = (QuestionData) readObject(file);
		}
		questionDataList.add(questionData);
		questionStartIndex++;
		}
		}
		if(currentPage==0){
			for (int i = 1; i <= totalPages; i++) {
				SelectedAnswerIds.put(i, "-1/-1");
				
			}
			session.put("SelectedAnswerIds", SelectedAnswerIds);
		}
		if(currentPage!=0){
			if(lastPage==0){
				lastPage++;
			}
			SelectedAnswerIds.put(lastPage, answers);
			session.put("SelectedAnswerIds", SelectedAnswerIds);
			currentpageanswers=new ArrayList<List<Integer>>();
			String string=SelectedAnswerIds.get(currentPage);
			if(!"".equals(string) && string !=null){
			String [] strArr=string.split("/");
			for (int i = 0; i < strArr.length; i++) {
				String[] str=strArr[i].split(",");
				List<Integer> list=new ArrayList<Integer>();
 						for (int j = 0; j < str.length; j++) {
							if(!"".equals(str[j])){
 							list.add(Integer.parseInt(str[j]));
							}else{
								list.add(-1);
							}
						}
 						currentpageanswers.add(list);
			}
		}
		}
		if (currentPage==0) {
			currentPage=1;
		}
		}
		}
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
	public List<Exam> getExamlist() {
		return examlist;
	}
	public void setExamlist(List<Exam> examlist) {
		this.examlist = examlist;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getExamID() {
		return examID;
	}
	public void setExamID(int examID) {
		this.examID = examID;
	}

	public List<QuestionData> getQuestionDataList() {
		return questionDataList;
	}

	public void setQuestionDataList(List<QuestionData> questionDataList) {
		this.questionDataList = questionDataList;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public List<List<Integer>> getCurrentpageanswers() {
		return currentpageanswers;
	}

	public void setCurrentpageanswers(List<List<Integer>> currentpageanswers) {
		this.currentpageanswers = currentpageanswers;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public int getTotal_Marks() {
		return Total_Marks;
	}

	public void setTotal_Marks(int total_Marks) {
		Total_Marks = total_Marks;
	}

	public int getTotalExam_Marks() {
		return TotalExam_Marks;
	}

	public void setTotalExam_Marks(int totalExam_Marks) {
		TotalExam_Marks = totalExam_Marks;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getInstitute() {
		return institute;
	}

	public void setInstitute(int institute) {
		this.institute = institute;
	}

	public Exam getInitiateExam() {
		return initiateExam;
	}

	public void setInitiateExam(Exam initiateExam) {
		this.initiateExam = initiateExam;
	}
	
	
}
