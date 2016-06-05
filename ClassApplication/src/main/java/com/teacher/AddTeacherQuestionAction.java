package com.teacher;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class AddTeacherQuestionAction extends BaseAction{
	List<RegisterBean> registerBeanList; 
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		List list = teacherTransaction.getTeachersClass(userBean.getRegId());
		if(list.size()>0){
		RegisterTransaction registerTransaction = new RegisterTransaction();
		registerBeanList = registerTransaction.getTeachersInstitutes(list);
		}
		return SUCCESS;
	}
	public List<RegisterBean> getRegisterBeanList() {
		return registerBeanList;
	}
	public void setRegisterBeanList(List<RegisterBean> registerBeanList) {
		this.registerBeanList = registerBeanList;
	}
}
