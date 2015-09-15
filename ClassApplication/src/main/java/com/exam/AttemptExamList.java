package com.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.db.student.StudentMarks;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.datalayer.exam.StudentExamData;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class AttemptExamList extends BaseAction {

	String batch,subject,division;
	List<Exam> examlist;
	int currentPage;
	int totalPages;
	int role;
	int institute;
	List<StudentExamData> studentExamData;
	String actionname;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction = new ExamTransaction();
		
		int totalCount=0;
		if(userBean.getRole()==3){
			BatchTransactions batchTransactions=new BatchTransactions();
			Batch studentbatch=batchTransactions.getBatch(Integer.parseInt(batch),institute,Integer.parseInt(division));
			division=studentbatch.getDiv_id()+"";
			StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
		List<StudentMarks>	marks=marksTransaction.getStudentMarksList(institute, userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
		List<Integer> examIds=new ArrayList<Integer>();
		if(marks!=null){
		for (int i = 0; i < marks.size(); i++) {
			examIds.add(marks.get(i).getExam_id());
		}
		}
		examlist=examTransaction.getExamforStudents(institute, Integer.parseInt(subject), Integer.parseInt(division), examIds);
		if(examlist!=null){
		totalCount=examlist.size();
		}
		}else{
			totalCount=examTransaction.getExamCount(userBean.getRegId(),  Integer.parseInt(subject), Integer.parseInt(division), "-1",batch);
		}
		if(totalCount>0){
			int remainder=totalCount%2;
			totalPages=totalCount/2;
			if(remainder>0){
				totalPages++;
			}
		}else{
			totalPages=0;
		}
		if(totalPages<currentPage){
			currentPage--;
		}
		if(currentPage==0){
			currentPage++;
		}
		if(userBean.getRole()==3){
			/*examlist=examTransaction.getExam(institute, Integer.parseInt(subject), -1, "Y",currentPage,"-1");
			StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
			List<StudentMarks> studentMarks=marksTransaction.getStudentMarksList(institute, userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
		*/
			int startindex=0;
			if(currentPage>1){
				startindex=(currentPage-1)*2;
			}
			int endindex=startindex+2;
			
			if(examlist!=null){
				if(endindex>examlist.size()){
					endindex=examlist.size();
				}
				studentExamData=new ArrayList<StudentExamData>();
				for (int i = startindex; i < endindex; i++) {
					StudentExamData examData=new StudentExamData();
					examData.setExam_id(examlist.get(i).getExam_id());
					examData.setExam_name(examlist.get(i).getExam_name());
					studentExamData.add(examData);
				}
			}
			
		}else{
		examlist=examTransaction.getExam(userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division), "-1",currentPage,batch);
		}
		
		if (userBean.getRole()==3) {
			return "studentexamlist";
		}
		return SUCCESS;
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
	public int getInstitute() {
		return institute;
	}
	public void setInstitute(int institute) {
		this.institute = institute;
	}
	public List<StudentExamData> getStudentExamData() {
		return studentExamData;
	}
	public void setStudentExamData(List<StudentExamData> studentExamData) {
		this.studentExamData = studentExamData;
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	
	

}
