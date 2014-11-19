package com.classowner;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.student.StudentData;
import com.classapp.persistence.Constants;
import com.config.BaseAction;
import com.helper.StudentHelperBean;
import com.user.UserBean;

public class ManageStudentAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		StudentHelperBean studentHelperBean= new StudentHelperBean();			
			studentHelperBean.setClass_id(userBean.getRegId());
			request.getSession().setAttribute(Constants.STUDENT_LIST, studentHelperBean.getStudents());
		return SUCCESS;
	}
}
