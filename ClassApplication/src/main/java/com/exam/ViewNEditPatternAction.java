package com.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ViewNEditPatternAction extends BaseAction {

List<Division> divisionList;	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionList=divisionTransactions.getAllDivisions(inst_id);
		
		return SUCCESS;
	}
	public List<Division> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}
	
	

}
