package com.corex.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class SearchQuestionAction extends BaseAction{
	List<Division> divisions;
	int classId = -1;
	int subId = -1;
	int questionType = -1;
	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		DivisionTransactions divisionTransaction = new DivisionTransactions();
		divisions=divisionTransaction.getAllDivisions(inst_id);
		
		return SUCCESS;
	}
	
	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	
	
	
	
}
