package com.customrole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.config.BaseAction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class TeacherSetSyllabusAction extends BaseAction{
	List<Division> divisionList;
	@Override
	public String performBaseAction(UserBean userBean, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionList = divisionTransactions.getAllDivisions(userBean.getInst_id());
		return SUCCESS;
	}
	public List<Division> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}
	
	
	
}
