package com.commoncomponent.actions;

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

public class StudentCommonComponent extends BaseAction{
	String forwardAction;
	String lable;
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
		if(list!=null){
			request.setAttribute("studentdivision",list.get(0).getDiv_id());
		}
		lable=getheader();
		return SUCCESS;
	}
	
	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getForwardAction() {
		return forwardAction;
	}
	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}
	public String getheader() {
		if("attemptexamlist".equals(forwardAction)){
			return "Attempt Exam";
		}else if("examMarks".equals(forwardAction)){
			return "Exam Marks";
		}else if("studentnotes".equals(forwardAction)){
			return "Notes";
		}
		
		return "";
	}
	

}
