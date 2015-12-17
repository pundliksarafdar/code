package com.classowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.subject.Subject;
import com.helper.SubjectHelperBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;


public class AddSubjectAction extends BaseAction{
	int currentPage;
	int totalPages;
	int endIndex;
	int startIndex;
	String actionname;
	int ClasscurrentPage;
	int ClasstotalPages;
	int ClassendIndex;
	int ClassstartIndex;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchTransactions batchTransactions = new BatchTransactions();
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List list = batchTransactions.getBatchData(userBean.getRegId());
		SubjectHelperBean subjectBean=new SubjectHelperBean();
		subjectBean.setClass_id(userBean.getRegId());
		List subjectList=new ArrayList();
		subjectList=subjectBean.getSubjects();
		request.setAttribute("listOfSubjects", subjectList);
		return SUCCESS;
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
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public int getClasscurrentPage() {
		return ClasscurrentPage;
	}
	public void setClasscurrentPage(int classcurrentPage) {
		ClasscurrentPage = classcurrentPage;
	}
	public int getClasstotalPages() {
		return ClasstotalPages;
	}
	public void setClasstotalPages(int classtotalPages) {
		ClasstotalPages = classtotalPages;
	}
	public int getClassendIndex() {
		return ClassendIndex;
	}
	public void setClassendIndex(int classendIndex) {
		ClassendIndex = classendIndex;
	}
	public int getClassstartIndex() {
		return ClassstartIndex;
	}
	public void setClassstartIndex(int classstartIndex) {
		ClassstartIndex = classstartIndex;
	}
	
	
	
}
