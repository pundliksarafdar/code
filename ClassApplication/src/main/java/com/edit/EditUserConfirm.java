	package com.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.OldCellRecord;

import com.classapp.db.register.RegisterBean;
import com.classapp.logger.AppLogger;
import com.config.BaseAction;
import com.config.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.transaction.register.RegisterTransaction;
import com.user.UserBean;

public class EditUserConfirm extends BaseAction{

	private static final long serialVersionUID = 1L;
	private RegisterBean registerBean = new RegisterBean();
	String oldPassword;
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		AppLogger.logger(registerBean);
		registerBean.setRole(userBean.getRole());
		if(userBean.getRole() == 5){
			registerBean.setInst_roll(userBean.getInst_roll());
			registerBean.setInst_id(userBean.getInst_id());
		}
		ActionContext.getContext().getSession().put("registerbean", registerBean);
		getActionErrors().clear();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		RegisterBean oldRegisterBean = registerTransaction.getregistereduser(userBean.getRegId());
		if(oldPassword==null || (!"".equals(oldPassword.trim()) && !oldPassword.equals(oldRegisterBean.getLoginPass()))){
			addActionError("Old password is wrong");
			return ERROR;
		}
		if(userBean.getRole()==2){
			return "teacher.edituserreview";
		}else if(userBean.getRole()==3){
			return "student.edituserreview";
		}
		return Constants.SUCCESS;
	}
	
}
