package com.signon;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.user.UserBean;

public class Logout extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		Cookie cookie = new Cookie("logincreation", "loggedin");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		ActionContext.getContext().getSession().clear();
		return SUCCESS;
	}
}
