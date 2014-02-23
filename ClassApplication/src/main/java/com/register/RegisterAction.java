package com.register;

import com.config.BaseAction;
import com.config.Constants;
import com.user.UserBean;

public class RegisterAction extends BaseAction{

	@Override
	public String performBaseAction(UserBean userBean) {
		return Constants.SUCCESS;
	}
	
}
