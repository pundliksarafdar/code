package com.classowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.BatchHelperBean;
import com.helper.DivisionHelperBean;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ManageBatchAction extends BaseAction{
	int divisionSize;
	
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getRegId());
		batchHelperBean.setBatchDetailsList();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		System.out.println("Size of batch list :"+batchHelperBean.getBatchDetailsList().size());		
		request.getSession().setAttribute(Constants.BATCHES_LIST, batchHelperBean.getBatchDetailsList());	
		List<Division> divisions= divisionTransactions.getAllDivisions(userBean.getRegId());
		setDivisionSize(divisions.size());
		List<String> divisionNames= new ArrayList<String>();
		List<String> Streams=new ArrayList<String>();
		List<String> ids=new ArrayList<String>();
		for (Division division : divisions) {
			divisionNames.add(division.getDivisionName());
			Streams.add(division.getStream());
			ids.add(division.getDivId()+"");
		}
		request.setAttribute(Constants.DIVISION_NAMES, divisionNames);
		request.setAttribute("streams", Streams);
		request.setAttribute("batcheids", ids);
		return SUCCESS;
	}

	public int getDivisionSize() {
		return divisionSize;
	}

	public void setDivisionSize(int divisionSize) {
		this.divisionSize = divisionSize;
	}
	
	
}

