package com.customrole;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exams.MCQDetails;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.MCQDataHelper;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ManageClassAction extends BaseAction{
	int currentPage;
	int totalPages;
	int endIndex;
	int startIndex;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		DivisionTransactions divisionTransactions =new DivisionTransactions();
	List<Division>	list=divisionTransactions.getAllDivisions(userBean.getInst_id());
	request.setAttribute("classes", list);
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
	
	
}
