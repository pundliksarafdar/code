package com.corex.exam;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.user.UserBean;

public class ExamCriteriaAction extends BaseAction{

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		return SUCCESS;
	}

}
