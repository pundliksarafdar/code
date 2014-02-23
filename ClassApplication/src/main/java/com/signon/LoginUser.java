package com.signon;

import com.classapp.db.login.LoginCheck;
import com.config.BaseAction;
import com.google.gson.Gson;
import com.user.UserBean;

public class LoginUser extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	private LoginBean loginBean;
	
	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	
	@Override
	public String performBaseAction(UserBean userBean) {
		String forward = null;
		if(null != loginBean){
			userBean.setLoginBean(loginBean);
		    forward = loadBean(userBean, loginBean);
		}else{
			loginBean = userBean.getLoginBean();
			if(null != loginBean){
				userBean.setLoginBean(loginBean);
			    forward = loadBean(userBean, loginBean);
			}else{
				forward = ERROR;
			}
		}
		return forward;
	}
	
	public String loadBean(UserBean userBean,LoginBean loginBean){
		LoginCheck loginCheck = new LoginCheck();
		String userBeanJson = loginCheck.loadUserObject(loginBean.getLoginname(), loginBean.getLoginpass());
		if(null!=userBeanJson){
			Gson gson = new Gson();
			userBean.setAddr1( gson.fromJson(userBeanJson, UserBean.class).getAddr1());
			userBean.setAddr2( gson.fromJson(userBeanJson, UserBean.class).getAddr2());
			userBean.setCity( gson.fromJson(userBeanJson, UserBean.class).getCity());
			userBean.setCountry( gson.fromJson(userBeanJson, UserBean.class).getCountry());
			userBean.setDob( gson.fromJson(userBeanJson, UserBean.class).getDob());
			userBean.setFirstname( gson.fromJson(userBeanJson, UserBean.class).getFirstname());
			userBean.setLastname( gson.fromJson(userBeanJson, UserBean.class).getLastname());
			userBean.setMiddlename(gson.fromJson(userBeanJson, UserBean.class).getMiddlename());
			userBean.setPhone1(gson.fromJson(userBeanJson, UserBean.class).getPhone1());
			userBean.setPhone2(gson.fromJson(userBeanJson, UserBean.class).getPhone2());
			userBean.setRole(gson.fromJson(userBeanJson, UserBean.class).getRole());
			userBean.setState(gson.fromJson(userBeanJson, UserBean.class).getState());
			userBean.setUsername(gson.fromJson(userBeanJson, UserBean.class).getUsername());
			
			userBean.setLoginBean(loginBean);
			return SUCCESS;
		}else{
			return ERROR;
		}
	}
}