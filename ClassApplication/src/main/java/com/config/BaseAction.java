package com.config;

import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.ServiceMode;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.classapp.db.urlaccess.PathAccess;
import com.classapp.logger.AppLogger;
import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;
import com.classapp.servicetable.SiteMap;
import com.classapp.servicetable.SiteMapData;
import com.google.gson.Gson;
import com.miscfunction.MiscFunction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;
import com.transaction.urlaccess.URLTransaction;
import com.user.UserBean;


public abstract class BaseAction extends ActionSupport implements Parameterizable{
	UserBean userBean;
	HttpServletRequest request;
	HttpServletResponse response;
	Map<String, Object> session;
	Map<String, String> params;
	String forward;
	String sessionMessageError;
	String moduleAccess;
	String accessType;
	private static final Logger logger = Logger.getLogger(BaseAction.class);
	public abstract String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session);
	@Override
	public String execute() throws Exception{
		try{
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		session = ServletActionContext.getContext().getSession();
		
		HttpSession sessionHttp = request.getSession();
		sessionHttp.setMaxInactiveInterval(10*60);
		
		
		//HttpSession sessionHttp = request.getSession();
		AppLogger.logger(sessionHttp.getMaxInactiveInterval());
		ServletContext servletContext = ServletActionContext.getServletContext();
		MiscFunction.setServletContext(servletContext);
		
		request.setAttribute("param", params);
		userBean = (UserBean) ActionContext.getContext().getSession().get("user");
		if((null == userBean || null == userBean.getUsername()) && !(params.containsKey("ignoresession")/* && "true".equals(params.get("ignoresession"))*/)){
			sessionMessageError = "You session expired, Please login again";
			return "logoutglobal";
		}
		
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
			if(roleInt == 5 &&  !"custom_user".equals(forward)){
				String[] child_mod_access = (String[])session.get("child_mod_access");
				String[] parent_mod_access = (String[])session.get("parent_mod_access");
				if(!ArrayUtils.contains(child_mod_access,moduleAccess) && "C".equals(accessType)){
					return "UNAUTHRISED";
				}else if(!ArrayUtils.contains(parent_mod_access,moduleAccess) && "P".equals(accessType)){
					return "UNAUTHRISED";
				}
				
			}
			List<SiteMapData> siteMapDatas = SiteMap.getSiteMapList(roleInt.toString());
			Gson gson = new Gson();
			String siteMapData = gson.toJson(siteMapDatas);
			(ActionContext.getContext().getSession()).put("sitemapdata", siteMapData);
			
		ActionMapping mapping = (ActionMapping) request.getAttribute("struts.actionMapping");
		//This action name is used to load view js
		String actionName = mapping.getName();
		request.setAttribute("actionName", actionName);
		
		com.classapp.urlaccess.PathAccess pathAccess = new com.classapp.urlaccess.PathAccess();
		pathAccess.setPaths(mapping.getName());
		if(com.classapp.utils.Constants.PATH_ACCESS_LIST.contains(pathAccess) && isHavingAccess(mapping.getName(), roleInt) && !params.containsKey("ignoresession")  && roleInt != 5){
			return "UNAUTHRISED";
		} 
		if("logout".equals(mapping.getName())){
			ActionContext.getContext().getSession().remove("user");
			ActionContext.getContext().setSession(null);
			return SUCCESS;
		}else{
			(ActionContext.getContext().getSession()).put("user", userBean);
		}
		}catch(Exception e){
			e.printStackTrace();
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

	public boolean isHavingAccess(String action,int role){
		boolean allowed = false;
		URLTransaction urlTransaction = new URLTransaction();
		return urlTransaction.isAcessible(action, role);
	}
	public String getModuleAccess() {
		return moduleAccess;
	}
	public void setModuleAccess(String moduleAccess) {
		this.moduleAccess = moduleAccess;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
}
