package com.tranaction.login;

import com.classapp.db.login.LoginCheck;

public class login {

	public String loginck(String loginname,String loginpass) {
		LoginCheck loginCheck = new LoginCheck();
		String userBeanJson = loginCheck.loadUserObject(loginname , loginpass);
		return userBeanJson;
	}
}
