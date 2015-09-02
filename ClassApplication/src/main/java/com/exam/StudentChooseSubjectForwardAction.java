package com.exam;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.config.Constants;
import com.user.UserBean;

public class StudentChooseSubjectForwardAction extends BaseAction{
	private String successforward;
	private String classname,batch,subject;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		request.getSession().setAttribute(Constants.CLASSNAME, classname);
		request.getSession().setAttribute(Constants.BATCH, batch);
		request.getSession().setAttribute(Constants.SUBJECT, subject);
		return successforward;
	}
	public String getSuccessforward() {
		return successforward;
	}
	public void setSuccessforward(String successforward) {
		this.successforward = successforward;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	
	
}
