package com.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.header.Header;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.StudentMarks;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.datalayer.exam.StudentExamData;
import com.tranaction.header.HeaderTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.user.UserBean;

public class ProgressCardAction extends BaseAction {

	List<Division> divisionList;
	List<Header> headerList;
	String instituteName;
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
		instituteName = userBean.getClassName();
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
	public String getInstituteName() {
		return instituteName;
	}
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	

}
