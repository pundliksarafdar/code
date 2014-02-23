package com.signon;

public class LoginBean {
	private String loginname;
	private String loginpass;
	
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
	
	@Override
	public String toString() {
		return "{loginname:\""+loginname+"\",loginpass:\""+loginpass+"\"}";
	}
	
	
}
