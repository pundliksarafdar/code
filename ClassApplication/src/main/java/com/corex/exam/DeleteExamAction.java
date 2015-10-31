package com.corex.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.exam.CompExam;
import com.classapp.db.exam.Exam;
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
		Exam  exam=examTransaction.getExam(userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division), examID);
		String[] quesids=exam.getQue_ids().split(",");
		List<Integer> quesidsList=new ArrayList<Integer>();
		for (int i = 0; i < quesids.length; i++) {
			quesidsList.add(Integer.parseInt(quesids[i]));
		}
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		List<Integer> disabledqueIds=bankTransaction.getDisabledQuestions(quesidsList, userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
		if(disabledqueIds!=null){
		for (int i = 0; i < disabledqueIds.size(); i++) {
			List<Exam> exams=examTransaction.isQuestionAvailableInExam(userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division), quesidsList.get(i)+"");	
			if(exams!=null){
				if(exams.size()==1){
					bankTransaction.deleteQuestion(disabledqueIds.get(i), userBean.getRegId(), Integer.parseInt(subject), Integer.parseInt(division));
				}
			}
		}
		}
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
