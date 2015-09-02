package com.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.user.UserBean;

public class StudentChooseSubjectAction extends BaseAction{
	String successforward;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		BatchTransactions batchTransactions=new BatchTransactions();
		int regID=userBean.getRegId();
		StudentTransaction studentTransaction=new StudentTransaction();
		List<Student> list=studentTransaction.getStudent(regID);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> beans= registerTransaction.getclassNames(list);
		request.setAttribute("Classes", beans);
		return SUCCESS;
	}
	public String getSuccessforward() {
		return successforward;
	}
	public void setSuccessforward(String successforward) {
		this.successforward = successforward;
	}

	
}
