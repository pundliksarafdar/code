package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.roll.Inst_roll;
import com.config.BaseAction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.instroll.InstRollTransaction;
import com.user.UserBean;

public class NoticeBoardAction extends BaseAction {
	List<Division> divisionList;
	List<Inst_roll> roleList;
	@Override
	public String performBaseAction(UserBean userBean, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionList = divisionTransactions.getAllDivisions(userBean.getRegId());
		InstRollTransaction rollTransaction = new InstRollTransaction();
		roleList = rollTransaction.getInstituteRoles(userBean.getRegId());
		return SUCCESS;
	}
	public List<Division> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}
	public List<Inst_roll> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Inst_roll> roleList) {
		this.roleList = roleList;
	}
	
	
}
