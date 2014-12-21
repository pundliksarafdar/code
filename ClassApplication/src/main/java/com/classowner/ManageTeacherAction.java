package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.persistence.Constants;
import com.config.BaseAction;
import com.helper.TeacherHelperBean;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class ManageTeacherAction extends BaseAction{
	int teacherListSize = 0;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		
			TeacherHelperBean teacherHelperBean= new TeacherHelperBean();
			teacherHelperBean.setClass_id(userBean.getRegId());
			List<TeacherDetails> teacherList = teacherHelperBean.getTeachers();
			request.getSession().setAttribute(Constants.TEACHER_LIST,teacherHelperBean.getTeachers());
			if(null!=teacherList){
				teacherListSize = teacherList.size();
			}
			 
		return SUCCESS;
	}
	public int getTeacherListSize() {
		return teacherListSize;
	}
	public void setTeacherListSize(int teacherListSize) {
		this.teacherListSize = teacherListSize;
	}
	
	
}
