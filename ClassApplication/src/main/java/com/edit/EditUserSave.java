package com.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classapp.db.register.RegisterBean;
import com.config.BaseAction;
import com.user.UserBean;

public class EditUserSave extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RegisterBean registerBean;
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}

	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		return "success";
	}
	
}
