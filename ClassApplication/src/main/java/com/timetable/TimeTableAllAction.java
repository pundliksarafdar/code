package com.timetable;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class TimeTableAllAction extends BaseAction{
	List<Division>divisions;

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		DivisionTransactions transactions = new DivisionTransactions();
		divisions = transactions.getAllDivisions(userBean.getRegId());

		return SUCCESS;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}
}
