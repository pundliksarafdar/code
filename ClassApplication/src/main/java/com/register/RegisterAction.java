package com.register;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.classapp.servicetable.ServiceMap;
import com.config.BaseAction;
import com.config.Constants;
import com.user.UserBean;

public class RegisterAction extends BaseAction{
	private RegisterBean registerBean;
	
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		String isDebugging = ServiceMap.getSystemParam("6", "isdebug");
		boolean isDebuggingBool = null!=isDebugging && isDebugging.equals("yes");
		if(isDebuggingBool){
		request = ServletActionContext.getRequest();
		request.setAttribute(Constants.ERROR_MESSAGE, "Under debug/Test mode, email can be reused.");
		}
		return Constants.SUCCESS;
	}
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}
	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}
	
}
