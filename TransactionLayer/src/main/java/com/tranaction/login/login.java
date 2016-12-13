package com.tranaction.login;

import com.classapp.db.login.LoginCheck;
import com.classapp.login.LoginBeanMobile;
import com.classapp.login.UserBean;
import com.google.gson.Gson;

public class login {

	public UserBean loginck(String loginname, String loginpass) {
		LoginCheck loginCheck = new LoginCheck();
		UserBean userBeanJson = loginCheck.loadUserObject(loginname, loginpass);
		
		return userBeanJson;
	}

	public void loadBean(UserBean userBean, LoginBeanMobile loginBean) {
		LoginCheck loginCheck = new LoginCheck();
		UserBean userBeanJson = loginck(loginBean.getUsername(),
				loginBean.getPassword());
		}
	
	public UserBean UpdatedBean(String loginname) {
		LoginCheck loginCheck = new LoginCheck();
		UserBean userBeanJson = loginCheck.loadUpdatedUserObject(loginname);
		
		return userBeanJson;
	}
}
