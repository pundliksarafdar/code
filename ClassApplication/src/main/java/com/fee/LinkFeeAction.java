package com.fee;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.header.Header;
import com.config.BaseAction;
import com.tranaction.header.HeaderTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class LinkFeeAction extends BaseAction{
	List<Division>divisions;
	List<Header> headers;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		DivisionTransactions transactions = new DivisionTransactions();
		divisions = transactions.getAllDivisions(userBean.getRegId());
		
		HeaderTransaction headerTransaction = new HeaderTransaction(userBean.getUserStatic().getHeaderPath());
		headers = headerTransaction.getHeaderList(userBean.getRegId());
		
		return SUCCESS;
	}
	
	public List<Division> getDivisions() {
		return divisions;
	}
	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}
	
}
