package com.classuser;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.user.UserBean;

public class GetClassListAction extends BaseAction{
 
	private static final long serialVersionUID = 1L;

	
	public String performBaseAction(UserBean userBean) {
		// TODO Auto-generated method stub
		ActionContext.getContext().getSession().put("isSearched", "false");
		return "success";  
	}
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		return null;
	}

}
