package com.customrole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.subject.Subject;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.SubjectHelperBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;


public class AddSubjectAction extends BaseAction{
	List<Subject> listOfSubjects;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		SubjectHelperBean subjectBean=new SubjectHelperBean();
		subjectBean.setClass_id(userBean.getInst_id());
		listOfSubjects=subjectBean.getSubjects();
		return SUCCESS;
	}
	public List<Subject> getListOfSubjects() {
		return listOfSubjects;
	}
	public void setListOfSubjects(List<Subject> listOfSubjects) {
		this.listOfSubjects = listOfSubjects;
	}	
}
