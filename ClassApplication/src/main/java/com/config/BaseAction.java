package com.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.user.UserBean;


public abstract class BaseAction extends ActionSupport{
	UserBean userBean;
	HttpServletRequest request;
	HttpServletResponse response;
	Map<String, Object> session;
	public abstract String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session);
	@Override
	public String execute(){
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		session = ServletActionContext.getContext().getSession();
		
		userBean = (UserBean) ActionContext.getContext().getSession().get("user");
		if (null == userBean) {
			userBean = new UserBean();
		}
		String forward = performBaseAction(userBean,request,response,session); 
		( ActionContext.getContext().getSession()).put("user", userBean);
		return forward;
	}
}
