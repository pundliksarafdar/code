package com.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.StudentMarks;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.datalayer.exam.StudentExamData;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class ExamMarksAction extends BaseAction {

	String batch,subject,division;
	List<Exam> examlist;
	int currentPage;
	int totalPages;
	int role;
	int institute;
	int examlistcurrentPage;
	int examlisttotalPages;
	int examID;
	List<StudentExamData> studentExamData;
	String actionname;
	List<StudentExamData> examData;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction = new ExamTransaction();
		
		int totalCount=0;
		if(!"viewstudentmarks".equals(actionname)){
		totalCount=examTransaction.getExamCount(userBean.getRegId(),  Integer.parseInt(subject), Integer.parseInt(division), "-1",batch);
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
		
		examlist=examTransaction.getExam(userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division), "-1",currentPage,batch);
		}else{
			StudentTransaction studentTransaction=new StudentTransaction();
			List<Integer> studentIds=studentTransaction.getStudentsFromBatches(userBean.getRegId(), Integer.parseInt(division), batch);
			if (studentIds!=null) {
				if (studentIds.size()>0) {
					
			
			StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
			totalCount=marksTransaction.getStudentMarksCount(userBean.getRegId() ,studentIds, Integer.parseInt(subject), Integer.parseInt(division), examID);
			if(totalCount>0){
				int remainder=totalCount%100;
				totalPages=totalCount/100;
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
			List<StudentMarks> marks=marksTransaction.getStudentMarks(userBean.getRegId(), studentIds, Integer.parseInt(subject), Integer.parseInt(division), currentPage, examID);
			if(marks!=null){
				List<Integer> students=new ArrayList<Integer>();
				RegisterBean bean=new RegisterBean();
				RegisterTransaction registerTransaction=new RegisterTransaction();
				
				for (int i = 0; i < marks.size(); i++) {
					students.add(marks.get(i).getStudent_id());
				}
				List<RegisterBean> registerBeans=registerTransaction.getStudents(students);
				examData=new ArrayList<StudentExamData>();
				for (int i = 0; i < marks.size(); i++) {
					StudentExamData studentExamData =new StudentExamData();
					for (int j = 0; j < registerBeans.size(); j++) {
						if(marks.get(i).getStudent_id()==registerBeans.get(i).getRegId()){
							studentExamData.setStudent_name(registerBeans.get(j).getFname()+" "+registerBeans.get(j).getLname());
							studentExamData.setMarks(marks.get(i).getMarks());
							examData.add(studentExamData);
						}
					}
				}
			}
				}
			}
		}
		return "viewexammarks";
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
	public int getExamlistcurrentPage() {
		return examlistcurrentPage;
	}
	public void setExamlistcurrentPage(int examlistcurrentPage) {
		this.examlistcurrentPage = examlistcurrentPage;
	}
	public int getExamlisttotalPages() {
		return examlisttotalPages;
	}
	public void setExamlisttotalPages(int examlisttotalPages) {
		this.examlisttotalPages = examlisttotalPages;
	}
	public int getExamID() {
		return examID;
	}
	public void setExamID(int examID) {
		this.examID = examID;
	}
	public List<StudentExamData> getExamData() {
		return examData;
	}
	public void setExamData(List<StudentExamData> examData) {
		this.examData = examData;
	}
	
	

}
