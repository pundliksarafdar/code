package com.signon;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.classapp.db.login.LoginCheck;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.Gson;
import com.tranaction.login.login;
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
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		String forward = null;
		if(null != loginBean){
			userBean.setLoginBean(loginBean);
		    forward = loadBean(userBean, loginBean);
		}if(null == userBean.getRegId()){
			addActionError("Invalid Username/Password");
			forward = ERROR;
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
		login loginCheck=new login();
		String userBeanJson = loginCheck.loginck(loginBean.getLoginname(), loginBean.getLoginpass());
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
			userBean.setStartdate(gson.fromJson(userBeanJson, UserBean.class).getStartdate());
			userBean.setEnddate(gson.fromJson(userBeanJson, UserBean.class).getEnddate());
			userBean.setRegId(gson.fromJson(userBeanJson, UserBean.class).getRegId());
			userBean.setLoginBean(loginBean);

			
			//Check for acceptance
			/*
			if(null != userBean.getRole() && 0 != userBean.getRole() && 10 != userBean.getRole()){
				if(null!=userBean.getStartdate()){
					return SUCCESS;
				}else{
					return Constants.UNACCEPTED;
				}
			}
			*/
			
			if (null != userBean.getRole() && 0 != userBean.getRole()
					&& 10 != userBean.getRole()) {
				if (null != userBean.getStartdate()) {

					if (null != userBean.getRole() && 9 < userBean.getRole()) {
						return Constants.ACCESSBLOCKED;
					}
					if ((null != userBean.getRole()) && 0 == userBean.getRole()) {
						return SUCCESS;
					} else if ((null != userBean.getRole())
							&& 1 == userBean.getRole()) {
						return Constants.CLASSOWNER;
					} else if ((null != userBean.getRole())
							&& 2 == userBean.getRole()) {
						return Constants.CLASSTEACHER;
					} else if ((null != userBean.getRole())
							&& 3 == userBean.getRole()) {
						return Constants.CLASSSTUDENT;
					} else {
						return ERROR;
					}
				} else {
					//return Constants.UNACCEPTED;
					return Constants.SUCCESS;
				}
			} else {
				return SUCCESS;
			}
		}else{
			return ERROR;
		}
	}
}