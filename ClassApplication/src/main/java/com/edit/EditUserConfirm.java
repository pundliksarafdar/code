	package com.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.classapp.db.register.RegisterBean;
import com.config.BaseAction;
import com.config.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.user.UserBean;

public class EditUserConfirm extends BaseAction{

	private static final long serialVersionUID = 1L;
	private RegisterBean registerBean = new RegisterBean();
	String oldPassword;
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		System.out.println(registerBean);
		registerBean.setRole(userBean.getRole());
		ActionContext.getContext().getSession().put("registerbean", registerBean);
		getActionErrors().clear();
		if(oldPassword==null || (!"".equals(oldPassword.trim()) && !oldPassword.equals(userBean.getLoginBean().getLoginpass()))){
			addActionError("Old password is wrong");
			return ERROR;
		}
		return Constants.SUCCESS;
	}
	
}
