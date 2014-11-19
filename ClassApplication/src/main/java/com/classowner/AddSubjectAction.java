package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.classwithsubject.ClassWithSubjectsData;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.subject.Subject;
import com.helper.SubjectHelperBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.user.UserBean;


public class AddSubjectAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		BatchTransactions batchTransactions = new BatchTransactions();
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List list = batchTransactions.getBatchData(userBean.getRegId());
		//List subjectList = subjectTransaction.getAllClassSubjects(userBean.getRegId());
		//request.setAttribute(Constants.BATCH_LIST, list);
		SubjectHelperBean subjectBean=new SubjectHelperBean();
		subjectBean.setClass_id(userBean.getRegId());
		request.setAttribute("listOfSubjects", subjectBean.getSubjects());
		//List subjectList = ClassWithSubjectsData.getAllSubjectsForClassID(userBean.getRegId());
	request.setAttribute(Constants.BATCH_LIST, list);
		//request.setAttribute(Constants.SUBJECT_LIST, subjectList);
		return SUCCESS;
	}
	
}
