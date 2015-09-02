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

public class AttemptExamList extends BaseAction {

	String batch,subject,division;
	List<Exam> examlist;
	int currentPage;
	int totalPages;
	int role;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction = new ExamTransaction();
		int totalCount=examTransaction.getExamCount(1, 23, 29, "-1");
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
		examlist=examTransaction.getExam(1, 23, 29, "-1",currentPage);
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
	
	

}
