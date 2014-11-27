package com.register;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.config.BaseAction;
import com.config.Constants;
import com.user.UserBean;

public class RegisterAction extends BaseAction{
	private RegisterBean registerBean;
	
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		return Constants.SUCCESS;
	}
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}
	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}
	
}
