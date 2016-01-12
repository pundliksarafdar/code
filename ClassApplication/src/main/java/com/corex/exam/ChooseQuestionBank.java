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
import com.user.UserBean;

public class ChooseQuestionBank extends BaseAction{
	String division,batch,subject;
	List<CompExam> compExams;
	List<Integer> marks;
	List<Integer> repeatation;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction=new ExamTransaction();
		compExams=examTransaction.getAllCompExamList();
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		marks=bankTransaction.getDistinctQuestionMarks(Integer.parseInt(subject), Integer.parseInt(division), userBean.getRegId(),"1");
		repeatation=bankTransaction.getDistinctQuestionRep(Integer.parseInt(subject), Integer.parseInt(division), userBean.getRegId());
		return SUCCESS;
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
	
}
