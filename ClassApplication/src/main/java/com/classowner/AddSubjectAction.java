package com.classowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.subject.Subject;
import com.helper.SubjectHelperBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.user.UserBean;


public class AddSubjectAction extends BaseAction{
	int currentPage;
	int totalPages;
	int endIndex;
	int startIndex;
	String actionname;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchTransactions batchTransactions = new BatchTransactions();
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List list = batchTransactions.getBatchData(userBean.getRegId());
		//List subjectList = subjectTransaction.getAllClassSubjects(userBean.getRegId());
		//request.setAttribute(Constants.BATCH_LIST, list);
		SubjectHelperBean subjectBean=new SubjectHelperBean();
		subjectBean.setClass_id(userBean.getRegId());
		List subjectList=new ArrayList();
		if("subjectadded".equals(actionname)){
			subjectList=subjectBean.getrecentlyaddedSubjectsfirst();
		}else{
			subjectList=subjectBean.getSubjects();
		}
		request.setAttribute("listOfSubjects", subjectList);
		if(subjectList!=null){
			int totalcount=subjectList.size();
			int remainder=0;
			if(totalcount>0){
				totalPages=totalcount/10;
				remainder=totalcount%10;
				if (remainder>0) {
					totalPages++;
				}
		
			}
			if (currentPage==0) {
				currentPage++;
			}
			
			if(currentPage>totalPages){
				currentPage--;
			}
			startIndex=(currentPage-1)*10;
			endIndex=startIndex+10;
			if(currentPage==totalPages && remainder>0){
				endIndex=startIndex+remainder;
			}
		}
	request.setAttribute(Constants.BATCH_LIST, list);
		//request.setAttribute(Constants.SUBJECT_LIST, subjectList);
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
	
	
	
}
