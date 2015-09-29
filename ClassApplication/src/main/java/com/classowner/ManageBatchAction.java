package com.classowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.BatchDetails;
import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.BatchHelperBean;
import com.helper.DivisionHelperBean;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ManageBatchAction extends BaseAction{
	int divisionSize;
	int batchSize;
	int currentPage;
	int totalPages;
	int endIndex;
	int startIndex;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getRegId());
		batchHelperBean.setBatchDetailsList();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<BatchDetails> batchList = batchHelperBean.getBatchDetailsList();
				
		request.getSession().setAttribute(Constants.BATCHES_LIST, batchList);	
		List<Division> divisions= divisionTransactions.getAllDivisions(userBean.getRegId());
		setDivisionSize(divisions.size());
		List<String> divisionNames= new ArrayList<String>();
		List<String> Streams=new ArrayList<String>();
		List<String> ids=new ArrayList<String>();
		divisionSize = divisions.size();
		batchSize = batchList.size();
		for (Division division : divisions) {
			divisionNames.add(division.getDivisionName());
			Streams.add(division.getStream());
			ids.add(division.getDivId()+"");
		}
		if(batchList!=null){
			int totalcount=batchList.size();
			int remainder=0;
			if(totalcount>0){
				totalPages=totalcount/10;
				remainder=totalcount%10;
				if (remainder>0) {
					totalPages++;
				}
		
			}
			if (currentPage==0) {
				currentPage++;
			}
			
			if(currentPage>totalPages){
				currentPage--;
			}
			startIndex=(currentPage-1)*10;
			endIndex=startIndex+10;
			if(currentPage==totalPages && remainder>0){
				endIndex=startIndex+remainder;
			}
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

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	
}

