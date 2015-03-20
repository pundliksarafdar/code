package com.tranaction.login;

import com.classapp.db.login.LoginCheck;
import com.classapp.login.LoginBeanMobile;
import com.classapp.login.UserBean;
import com.google.gson.Gson;

public class login {

	public String loginck(String loginname, String loginpass) {
		LoginCheck loginCheck = new LoginCheck();
		String userBeanJson = loginCheck.loadUserObject(loginname, loginpass);
		
		return userBeanJson;
	}

	public void loadBean(UserBean userBean, LoginBeanMobile loginBean) {
		LoginCheck loginCheck = new LoginCheck();
		String userBeanJson = loginck(loginBean.getUsername(),
				loginBean.getPassword());
		
		if (null != userBeanJson) {
			Gson gson = new Gson();
			userBean.setAddr1(gson.fromJson(userBeanJson, UserBean.class)
					.getAddr1());
			userBean.setAddr2(gson.fromJson(userBeanJson, UserBean.class)
					.getAddr2());
			userBean.setCity(gson.fromJson(userBeanJson, UserBean.class)
					.getCity());
			userBean.setCountry(gson.fromJson(userBeanJson, UserBean.class)
					.getCountry());
			userBean.setDob(gson.fromJson(userBeanJson, UserBean.class)
					.getDob());
			userBean.setFirstname(gson.fromJson(userBeanJson, UserBean.class)
					.getFirstname());
			userBean.setLastname(gson.fromJson(userBeanJson, UserBean.class)
					.getLastname());
			userBean.setMiddlename(gson.fromJson(userBeanJson, UserBean.class)
					.getMiddlename());
			userBean.setPhone1(gson.fromJson(userBeanJson, UserBean.class)
					.getPhone1());
			userBean.setPhone2(gson.fromJson(userBeanJson, UserBean.class)
					.getPhone2());
			userBean.setRole(gson.fromJson(userBeanJson, UserBean.class)
					.getRole());
			userBean.setState(gson.fromJson(userBeanJson, UserBean.class)
					.getState());
			userBean.setUsername(gson.fromJson(userBeanJson, UserBean.class)
					.getUsername());
			userBean.setStartdate(gson.fromJson(userBeanJson, UserBean.class)
					.getStartdate());
			userBean.setEnddate(gson.fromJson(userBeanJson, UserBean.class)
					.getEnddate());
			userBean.setRegId(gson.fromJson(userBeanJson, UserBean.class)
					.getRegId());
			userBean.setClassName(gson.fromJson(userBeanJson, UserBean.class)
					.getClassName());
			userBean.setLoginBean(loginBean);

			loginCheck.updateIdForUser(userBean.getRegId(),loginBean.getUserid(),loginBean.getDeviceId());
		}
	}
}
