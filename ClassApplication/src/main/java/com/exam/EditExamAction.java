package com.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.CompExam;
import com.classapp.db.exam.Exam;
import com.classapp.db.header.Header;

import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.tranaction.header.HeaderTransaction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class EditExamAction extends BaseAction{
	List<Division> divisionList;
	List<Header> headerList;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionList = divisionTransactions.getAllDivisions(inst_id);
		HeaderTransaction headerTransaction = new HeaderTransaction(userBean.getUserStatic().getHeaderPath());
		headerList = headerTransaction.getHeaderList(inst_id);
		ExamTransaction examTransaction = new ExamTransaction();
		return SUCCESS;
	}
	public List<Division> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}
	public List<Header> getHeaderList() {
		return headerList;
	}
	public void setHeaderList(List<Header> headerList) {
		this.headerList = headerList;
	}
	
}
