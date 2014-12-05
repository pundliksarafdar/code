package com.classowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.student.StudentData;
import com.classapp.persistence.Constants;
import com.config.BaseAction;
import com.helper.DivisionHelperBean;
import com.helper.StudentHelperBean;
import com.transaction.batch.BatchTransactions;
import com.user.UserBean;

public class ManageStudentAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		StudentHelperBean studentHelperBean= new StudentHelperBean();	
		DivisionHelperBean divisionHelperBean = new DivisionHelperBean();
		BatchTransactions batchTransactions=new BatchTransactions();
		List list=batchTransactions.getAllBatches(userBean.getRegId());
		request.setAttribute("batches", list);
			studentHelperBean.setClass_id(userBean.getRegId());
			request.getSession().setAttribute(Constants.STUDENT_LIST, studentHelperBean.getStudents());
			List<Division> divisions= divisionHelperBean.getListOfDivision();
			List<String> divisionNames= new ArrayList<String>();
			for (Division division : divisions) {
				divisionNames.add(division.getDivisionName());
			}
			request.setAttribute(Constants.DIVISION_NAMES, divisionNames);
		return SUCCESS;
	}
}
