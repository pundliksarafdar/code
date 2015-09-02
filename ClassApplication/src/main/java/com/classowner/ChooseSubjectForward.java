package com.classowner;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.config.Constants;
import com.user.UserBean;

public class ChooseSubjectForward extends BaseAction{
	String successaction,subject,division,batch;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		request.getSession().setAttribute(Constants.DIVISION,division );
		request.getSession().setAttribute(Constants.SUBJECT, subject);
		request.getSession().setAttribute(Constants.BATCH, batch);
		return successaction;
	}
	
	public String getSuccessaction() {
		return successaction;
	}
	public void setSuccessaction(String successaction) {
		this.successaction = successaction;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	
}
