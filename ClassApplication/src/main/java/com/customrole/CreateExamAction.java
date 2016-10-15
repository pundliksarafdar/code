package com.customrole;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.header.Header;
import com.config.BaseAction;
import com.tranaction.header.HeaderTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class CreateExamAction extends BaseAction{

	List<Division> divisionList;
	List<Header> headerList;
	List<Exam> examList;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getInst_id();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionList = divisionTransactions.getAllDivisions(inst_id);
		String storage = userBean.getUserStatic().getHeaderPath();
		HeaderTransaction headerTransaction = new HeaderTransaction(storage);
		headerList = headerTransaction.getHeaderList(inst_id);
		ExamTransaction examTransaction = new ExamTransaction();
		examList = examTransaction.getExamList(inst_id);
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
	public List<Exam> getExamList() {
		return examList;
	}
	public void setExamList(List<Exam> examList) {
		examList = examList;
	}
	
}
