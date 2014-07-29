package com.timetable;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.transaction.batch.BatchTransactions;
import com.user.UserBean;

public class CreateTimeTableAction extends BaseAction {

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		BatchTransactions batchTransactions=new BatchTransactions();
		int regID=userBean.getRegId();
		
		List batch=batchTransactions.getAllBatches(regID);
		request.setAttribute("batch", batch);
		// TODO Auto-generated method stub
		return SUCCESS;
	}

}
