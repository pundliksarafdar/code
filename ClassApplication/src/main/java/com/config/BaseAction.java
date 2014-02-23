package com.config;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.user.UserBean;

public abstract class BaseAction extends ActionSupport{
	UserBean userBean;
	public abstract String performBaseAction(UserBean userBean);
	@Override
	public String execute(){
		userBean = (UserBean) ActionContext.getContext().getSession().get("user");
		if (null == userBean) {
			userBean = new UserBean();
		}
		String forward = performBaseAction(userBean); 
		( ActionContext.getContext().getSession()).put("user", userBean);
		return forward;
	}
}
