package com.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.Subject;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.service.beans.OnlineExamPaper;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class AttemptExamAction extends BaseAction {

	int batch,subject,division,question_paper_id,exam,inst_id;
	OnlineExamPaper onlineExamPaper;
	int questionPaperSize;
	boolean isSolved = true;
	boolean isShowAnswer = true;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		//patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		//if inst_id is 0 this means classowner is calling as we are not sending data
		//if it is non 0 then we are sending data and student is accessing
		if(inst_id==0){
			inst_id = userBean.getRegId();
		}
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id,userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(Constants.STORAGE_PATH+File.separator+inst_id+File.separator+"QuestionPaper");
		patternTransaction.setStoragePath(Constants.STORAGE_PATH);
		onlineExamPaper = patternTransaction.getOnlineQuestionPaper(division,question_paper_id,inst_id);
		questionPaperSize = onlineExamPaper.getOnlineExamPaperElementList().size();
		
		ExamTransaction examTransaction = new ExamTransaction();
		isSolved = examTransaction.isExamSolved(userBean.getRegId(), inst_id, subject, division, batch, exam);
		if(isSolved && userBean.getRole()==3){
			return "solved";
		}
	return SUCCESS;
	}

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}

	public int getSubject() {
		return subject;
	}

	public void setSubject(int subject) {
		this.subject = subject;
	}

	public int getDivision() {
		return division;
	}

	public void setDivision(int division) {
		this.division = division;
	}

	public int getQuestion_paper_id() {
		return question_paper_id;
	}

	public void setQuestion_paper_id(int question_paper_id) {
		this.question_paper_id = question_paper_id;
	}

	public OnlineExamPaper getOnlineExamPaper() {
		return onlineExamPaper;
	}

	public void setOnlineExamPaper(OnlineExamPaper onlineExamPaper) {
		this.onlineExamPaper = onlineExamPaper;
	}

	public int getQuestionPaperSize() {
		return questionPaperSize;
	}

	public void setQuestionPaperSize(int questionPaperSize) {
		this.questionPaperSize = questionPaperSize;
	}

	public int getInst_id() {
		return inst_id;
	}

	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}

	public int getExam() {
		return exam;
	}

	public void setExam(int exam) {
		this.exam = exam;
	}
	
	
	
	
}
