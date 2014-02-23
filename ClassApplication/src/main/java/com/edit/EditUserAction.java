package com.edit;

import com.config.BaseAction;
import com.user.UserBean;

public class EditUserAction extends BaseAction{

	private static final long serialVersionUID = 1L;

	@Override
	public String performBaseAction(UserBean userBean) {
		
		return "success";
	}
	
}
