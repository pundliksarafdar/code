package com.signon;

public class LoginBean {
	private String loginname;
	private String loginpass;
	private long lastLogin;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	
	public long getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}
	@Override
	public String toString() {
		return "{loginname:\""+loginname+"\",loginpass:\""+loginpass+"\"}";
	}
	
	
}
