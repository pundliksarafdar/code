package com.mobile.bean;

import java.util.Date;

import com.classapp.login.UserBean;

public class SessionBean {
	private static final int SECONDS = 10*60;
	private String token;
	private Date timestamp;
	private UserBean userBean;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	public void refresh(){
		setTimestamp(new Date());
	}
	
	public boolean isValid() {
		   return System.currentTimeMillis() - timestamp.getTime() < 1000 * SECONDS;
		}
}
