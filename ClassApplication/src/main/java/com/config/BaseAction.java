package com.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

import com.classapp.servicetable.ServiceMap;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.user.UserBean;


public abstract class BaseAction extends ActionSupport{
	UserBean userBean;
	HttpServletRequest request;
	HttpServletResponse response;
	Map<String, Object> session;
	String forward;
	public abstract String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session);
	@Override
	public String execute() throws Exception{
		try{
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		session = ServletActionContext.getContext().getSession();
		
		userBean = (UserBean) ActionContext.getContext().getSession().get("user");
		if (null == userBean) {
			userBean = new UserBean();
		}
			forward = performBaseAction(userBean,request,response,session);
			
		if(null == userBean.getRegId()){
			ActionContext.getContext().getSession().remove("user");
			return "logout";
		}else{
			( ActionContext.getContext().getSession()).put("user", userBean);
		}
		}catch(Exception e){
			String errorCode = ServiceMap.getSystemParam("3","show");
			if(null!=errorCode && "yes".equalsIgnoreCase("yes"))
				forward = "syserror";
			else
				throw e;
		}
		return forward;
	}
}
