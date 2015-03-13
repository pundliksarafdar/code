package com.config;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;
import com.classapp.servicetable.SiteMap;
import com.classapp.servicetable.SiteMapData;
import com.google.gson.Gson;
import com.miscfunction.MiscFunction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;
import com.user.UserBean;


public abstract class BaseAction extends ActionSupport implements Parameterizable{
	UserBean userBean;
	HttpServletRequest request;
	HttpServletResponse response;
	Map<String, Object> session;
	Map<String, String> params;
	String forward;
	String sessionMessageError;
	public abstract String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session);
	@Override
	public String execute() throws Exception{
		try{
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		session = ServletActionContext.getContext().getSession();

		/*
		HttpSession sessionHttp = request.getSession();
		sessionHttp.setMaxInactiveInterval(60);
		*/
		
		ServletContext servletContext = ServletActionContext.getServletContext();
		MiscFunction.setServletContext(servletContext);
		
		request.setAttribute("param", params);
		userBean = (UserBean) ActionContext.getContext().getSession().get("user");
		if((null == userBean || null == userBean.getUsername()) && !(params.containsKey("ignoresession")/* && "true".equals(params.get("ignoresession"))*/)){
			sessionMessageError = "You session expired, Please login again";
			return "logoutglobal";
		}
		
		//userBean = (UserBean) ActionContext.getContext().getSession().get("user");
		if (null == userBean) {
			userBean = new UserBean();
		}
			forward = performBaseAction(userBean,request,response,session);
		
			Integer role = userBean.getRole();
			Integer roleInt;
			if(null==role){
				roleInt = -1;
			}else{
				roleInt = role;
			}
			List<SiteMapData> siteMapDatas = SiteMap.getSiteMapList(roleInt.toString());
			Gson gson = new Gson();
			String siteMapData = gson.toJson(siteMapDatas);
			(ActionContext.getContext().getSession()).put("sitemapdata", siteMapData);
			
		ActionMapping mapping = (ActionMapping) request.getAttribute("struts.actionMapping");
		
		if("logout".equals(mapping.getName())){
			ActionContext.getContext().getSession().remove("user");
			ActionContext.getContext().setSession(null);
			return SUCCESS;
		}else{
			(ActionContext.getContext().getSession()).put("user", userBean);
		}
		}catch(Exception e){
			String errorCode = ServiceMap.getSystemParam(Constants.SHOW_STACK_TRACE,"show");
			if(null!=errorCode && "yes".equalsIgnoreCase(errorCode))
				forward = "syserror";
			else
				throw e;
		}
		return forward;
	}
	
	
	@Override
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	@Override
	public void addParam(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getParams() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getSessionMessageError() {
		return sessionMessageError;
	}
	public void setSessionMessageError(String sessionMessageError) {
		this.sessionMessageError = sessionMessageError;
	}

	
}
