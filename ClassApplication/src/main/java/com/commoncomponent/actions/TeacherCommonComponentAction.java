package com.commoncomponent.actions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class TeacherCommonComponentAction extends BaseAction{
	String forwardAction;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		String notes=(String) request.getParameter("notesadded");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List classids=teacherTransaction.getTeachersClass(userBean.getRegId());
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List classes=registerTransaction.getTeachersclassNames(classids);
		request.setAttribute("classes", classes);
		
		request.setAttribute("notes", notes);
		
		return SUCCESS;
	}
	public String getForwardAction() {
		return forwardAction;
	}
	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}
	
	

}
