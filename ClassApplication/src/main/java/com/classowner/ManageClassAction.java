package com.classowner;

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
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		DivisionTransactions divisionTransactions =new DivisionTransactions();
	List<Division>	list=divisionTransactions.getAllDivisions(userBean.getRegId());
	request.setAttribute("classes", list);
		return SUCCESS;
	}	
}