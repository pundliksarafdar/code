package com.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentMarks;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.datalayer.exam.StudentExamData;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class StudentExamMarksAction extends BaseAction {

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
			Map<String, Object> session) {/*
		ExamTransaction examTransaction = new ExamTransaction();
		
		int totalCount=0;
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=(Student) studentTransaction.getclassStudent(userBean.getRegId(), institute);
		BatchTransactions batchTransactions=new BatchTransactions();
		Batch studentbatch=batchTransactions.getBatch(Integer.parseInt(batch),institute,student.getDiv_id());
			division=studentbatch.getDiv_id()+"";
			StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
			List<StudentMarks> studentMarks= marksTransaction.getStudentMarksList(institute, userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
			if(studentMarks!=null && studentMarks.size()>0){
				totalCount=studentMarks.size();
		if(totalCount>0){
			int remainder=totalCount%10;
			totalPages=totalCount/10;
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
		List<Integer> examIds=new ArrayList<Integer>();
		for (int i = 0; i < studentMarks.size(); i++) {
			examIds.add(studentMarks.get(i).getExam_id());
		}
		int startindex=0;
		if(currentPage>1){
			startindex=(currentPage-1)*10;
		}
		int endindex=startindex+10;
		
			examlist=examTransaction.getExamByIDs(institute, Integer.parseInt(subject), Integer.parseInt(division), examIds);
			if(examlist!=null){
				if(endindex>examlist.size()){
					endindex=examlist.size();
				}
				studentExamData=new ArrayList<StudentExamData>();
				for (int i = startindex; i < endindex; i++) {
					StudentExamData examData=new StudentExamData();
					examData.setExam_id(examlist.get(i).getExam_id());
					examData.setExam_name(examlist.get(i).getExam_name());
					if(studentMarks!=null){
						for (int j = 0; j < studentMarks.size(); j++) {
							if(studentMarks.get(j).getExam_id()==examlist.get(i).getExam_id()){
								examData.setExamAttempted("Y");
								examData.setMarks(studentMarks.get(j).getMarks());
								break;
							}else{
								examData.setExamAttempted("N");
							}
						}
					}else{
						examData.setExamAttempted("N");
					}
					studentExamData.add(examData);
				}
			}
			}
	*/	return SUCCESS;
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
