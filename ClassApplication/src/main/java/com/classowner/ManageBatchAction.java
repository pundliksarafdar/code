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
import com.user.UserBean;

public class ManageBatchAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getRegId());
		batchHelperBean.setBatchDetailsList();
		DivisionHelperBean divisionHelperBean = new DivisionHelperBean();
		System.out.println("Size of batch list :"+batchHelperBean.getBatchDetailsList().size());		
		request.getSession().setAttribute(Constants.BATCHES_LIST, batchHelperBean.getBatchDetailsList());	
		List<Division> divisions= divisionHelperBean.getListOfDivision();
		List<String> divisionNames= new ArrayList<String>();
		for (Division division : divisions) {
			divisionNames.add(division.getDivisionName());
		}
		request.setAttribute(Constants.DIVISION_NAMES, divisionNames);
		return SUCCESS;
	}
}

