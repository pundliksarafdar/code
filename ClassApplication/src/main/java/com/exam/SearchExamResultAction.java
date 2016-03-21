package com.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.exam.Exam;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class SearchExamResultAction extends BaseAction {

	String batch,subject,division;
	List<Exam> examlist;
	int currentPage;
	int totalPages;
	String institute;
	int role;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {/*
		ExamTransaction examTransaction = new ExamTransaction();
		int inst_id= userBean.getRegId();
		role=userBean.getRole();
		if(userBean.getRole()==2){
			inst_id=Integer.parseInt(institute);
		}
		int totalCount=examTransaction.getExamCount(inst_id, Integer.parseInt(subject), Integer.parseInt(division), "-1",batch);
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
		examlist=examTransaction.getExam(inst_id, Integer.parseInt(subject), Integer.parseInt(division), "-1",currentPage,batch);
		if(userBean.getRole()==2){
			return "teacherexamsearch";
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
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
	

}
