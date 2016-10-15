package com.customrole;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.logger.AppLogger;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class AddNotesOptionAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> divisions=divisionTransactions.getAllDivisions(userBean.getInst_id());
		request.setAttribute("divisions", divisions);
		return SUCCESS;
	}
}
