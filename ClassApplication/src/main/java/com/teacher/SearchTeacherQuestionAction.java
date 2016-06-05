package com.teacher;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.register.RegisterBean;
import com.config.BaseAction;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class SearchTeacherQuestionAction extends BaseAction {
	List<RegisterBean> registerBeanList; 
	int classId = -1;
	int subId = -1;
	int questionType = -1;
	int inst_id = -1;
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
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	
}
