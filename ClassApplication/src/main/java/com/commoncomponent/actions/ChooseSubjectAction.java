package com.commoncomponent.actions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ChooseSubjectAction extends BaseAction{
	List<Division> divisions;
	String forwardAction;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisions = divisionTransactions.getAllDivisions(userBean.getRegId());
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
	

}
