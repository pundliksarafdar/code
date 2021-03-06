package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.subject.Subject;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.SubjectHelperBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;


public class AddTopicAction extends BaseAction{
	String actionname;
	String subname;
	int subid;
	List<Division> divisionList;
	int currentPage;
	int totalPages;
	String subjectname;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		divisionList=divisionTransactions.getAllDivisions(userBean.getRegId());
		if(divisionList!=null){
		 int totalCount=divisionList.size();
		if(totalCount>0){
			int remainder=totalCount%2;
			totalPages=totalCount/2;
			if(remainder>0){
				totalPages++;
			}
		}
		}
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		Subject subject=subjectTransaction.getSubject(subid);
		subjectname=subject.getSubjectName();
		return "addtopicinitiate";
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
	public List<Division> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
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
	public String getSubjectname() {
		return subjectname;
	}
	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}
	
	
	
}
