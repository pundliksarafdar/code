package com.commoncomponent.actions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ChooseSubjectAction extends BaseAction{
	List<Division> divisions;
	String forwardAction;
	String hideBatch;
	String lable;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisions = divisionTransactions.getAllDivisions(userBean.getRegId());
		lable=getheader();
		return SUCCESS;
		
	}
	public List<Division> getDivisions() {
		return divisions;
	}
	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}
	public String getForwardAction() {
		return forwardAction;
	}
	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	
	public String getHideBatch() {
		//Return false is hiebatch is not specified
		if(null == hideBatch || hideBatch.trim().length()==0){
			return "false";
		}else{
			return hideBatch;
		}
	}
	public void setHideBatch(String hideBatch) {
		this.hideBatch = hideBatch;
	}
	public String getheader() {
		if("uploadexams".equals(forwardAction)){
			return "Add Question";
		}else if("searchQuestion".equals(forwardAction)){
			return "Search Question";
		}else if("generateexampreaction".equals(forwardAction)){
			return "Generate Exam";
		}else if("manualexam".equals(forwardAction)){
			return "Manual Exam";
		}else if("listExam".equals(forwardAction)){
			return "Search Exam";
		}else if("attemptexamlist".equals(forwardAction)){
			return "Attempt Exam";
		}else if("studentexammarks".equals(forwardAction)){
			return "Student Marks";
		}else if("addnotesoption".equals(forwardAction)){
			return "Add Notes";
		}else if("seenotes".equals(forwardAction)){
			return "See Notes";
		}
		
		return "";
	}
	

}
