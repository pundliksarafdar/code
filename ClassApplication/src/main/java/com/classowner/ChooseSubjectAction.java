package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class ChooseSubjectAction extends BaseAction{
	String successaction;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {

		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List list=subjectTransaction.getAllClassSubjects(userBean.getRegId());
		request.setAttribute("subjects", list);
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> divisions=divisionTransactions.getAllDivisions(userBean.getRegId());
		request.setAttribute("divisions", divisions);
		return SUCCESS;
	}
	
	public String getSuccessaction() {
		return successaction;
	}
	public void setSuccessaction(String successaction) {
		this.successaction = successaction;
	}

	
}
