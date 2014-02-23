package com.signon;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Logout extends ActionSupport{

	private static final long serialVersionUID = 1L;
	@Override
	public String execute() {
		ActionContext.getContext().getSession().clear();
		return SUCCESS;
	}
}
