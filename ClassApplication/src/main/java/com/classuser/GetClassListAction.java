package com.classuser;

import com.config.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.user.UserBean;

public class GetClassListAction extends BaseAction{
 
	private static final long serialVersionUID = 1L;

	@Override
	public String performBaseAction(UserBean userBean) {
		// TODO Auto-generated method stub
		ActionContext.getContext().getSession().put("isSearched", "false");
		return "success";  
	}

}
