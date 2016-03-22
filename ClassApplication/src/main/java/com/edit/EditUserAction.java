package com.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.config.BaseAction;
import com.user.UserBean;


public class EditUserAction extends BaseAction{

	private static final long serialVersionUID = 1L;

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		if (userBean.getRole()==2) {
			return "teacher.edituser";
		}else if (userBean.getRole()==3) {
			return "student.edituser";
		}
		return SUCCESS;
	}
}
