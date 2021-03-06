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
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class SendMessageAction extends BaseAction{
	String to;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchTransactions batchTransactions=new BatchTransactions();
		int regID=userBean.getRegId();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<Division> divisions= divisionTransactions.getAllDivisions(userBean.getRegId());
		request.setAttribute(Constants.DIVISION, divisions);
		List batch=batchTransactions.getAllBatches(regID);
		request.setAttribute("batch", batch);
		return SUCCESS;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}	
	
	
}
