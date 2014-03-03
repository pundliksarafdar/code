package com.register;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.classapp.db.login.LoginCheck;
import com.classapp.db.register.RegisterUser;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.Gson;
import com.signon.LoginBean;
import com.signon.LoginUser;
import com.user.UserBean;

public class RegisterUserAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RegisterBean registerBean;

	public RegisterBean getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}

	@Override
	public String performBaseAction(UserBean userBean) {
		RegisterUser registerUser = new RegisterUser();
		Gson gson = new Gson();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String formatedDate = dateFormat.format(date);
		registerBean.setDob(registerBean.getDob().replace("-", ""));
		registerBean.setRegistrationDate(formatedDate);
		String registerReq = gson.toJson(registerBean);
		String status = registerUser.registerUser(registerReq);
		System.out.println("In Register user action - Register User Status..."+status);
		if("success".equals(status)){
			LoginBean loginBean = new LoginBean();
			loginBean.setLoginname(registerBean.getLoginName());
			loginBean.setLoginpass(registerBean.getLoginPass());
			LoginUser loginUser = new LoginUser();
			String forward = null;
			if(null != loginBean){
				userBean.setLoginBean(loginBean);
			    forward = loginUser.loadBean(userBean, loginBean);
			}
			
			return forward;
		}else{
			addActionError(status);
			return "error";
		}
	}	
}
