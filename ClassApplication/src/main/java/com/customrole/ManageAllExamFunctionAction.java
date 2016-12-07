package com.customrole;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.config.BaseAction;
import com.transaction.register.AdditionalFormFieldTransaction;
import com.user.UserBean;

public class ManageAllExamFunctionAction extends BaseAction{
	String AdditionalFieldJson;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(userBean.getInst_id());
		AdditionalFieldJson = bean.getFormField();
		return SUCCESS;
	}
	public String getAdditionalFieldJson() {
		return AdditionalFieldJson;
	}
	public void setAdditionalFieldJson(String additionalFieldJson) {
		AdditionalFieldJson = additionalFieldJson;
	}
}
