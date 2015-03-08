package com.secret;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;
import com.config.BaseAction;
import com.transaction.register.RegisterTransaction;
import com.user.UserBean;

public class GetLoginDetailsAction extends BaseAction{
	List<RegisterBean> registerBeans;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		String show = ServiceMap.getSystemParam("7", "show");
		if("yes".equals(show)){
			RegisterTransaction registerTransaction = new RegisterTransaction();
			registerBeans = registerTransaction.getAllLogins();
			request.setAttribute(Constants.ALL_LOGINS, registerBeans);
			return SUCCESS;
		}
		return "EXCEPTION";
	}
	public List<RegisterBean> getRegisterBeans() {
		return registerBeans;
	}
	public void setRegisterBeans(List<RegisterBean> registerBeans) {
		this.registerBeans = registerBeans;
	}
	
	
		
}
