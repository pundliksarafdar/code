package com.corex.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.exam.CompExam;
import com.config.BaseAction;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class DeleteExamAction extends BaseAction{
	String division,batch,subject;
	String actionname;
	int currentPage;
	int totalPages;
	int examID;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction=new ExamTransaction();
		StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
		marksTransaction.deleteStudentMarksrelatedtoexam(userBean.getRegId(), Integer.parseInt(division), Integer.parseInt(subject), examID);
		examTransaction.deleteExam(examID, userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
		actionname="deleteExam";
		return SUCCESS;
	}
	
	public String getDivision() {
		return division;
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

	public String getCommonaction() {
		return actionname;
	}

	public void setCommonaction(String commonaction) {
		this.actionname = commonaction;
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

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public int getExamID() {
		return examID;
	}

	public void setExamID(int examID) {
		this.examID = examID;
	}
	

	
}
