package com.corex.responsebean;

import com.classapp.login.UserBean;


public class LoginResponse {
	
	private UserBean userBean;
	private ResultBean resultBean;
	
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public ResultBean getResultBean() {
		if(null == resultBean){
			resultBean = new ResultBean();
		}
		return resultBean;
	}
	public void setResultBean(ResultBean resultBean) {
		this.resultBean = resultBean;
	}
	
	public void setCode(String code, String message) {
		getResultBean().setCode(code);
		getResultBean().setMessage(message);
	}
	
	
}
