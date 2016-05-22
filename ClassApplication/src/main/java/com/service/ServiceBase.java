package com.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.exception.NoUserBeanException;
import com.user.UserBean;

public class ServiceBase {
	@Context
	private HttpServletRequest request;
	
	private int regId;
	private UserBean userBean;

	public int getRegId() {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		if(null==userBean){
				throw new NoUserBeanException();
		}else
		{
			setUserBean(userBean);
		}
		return userBean.getRegId();
	}

	public void setRegId(int regId) {
		this.regId = regId;
	}

	public UserBean getUserBean() {
		if(null==userBean){
			userBean = (UserBean) request.getSession().getAttribute("user");
		}
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	
	
}
