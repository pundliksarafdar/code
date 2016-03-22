package com.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.classapp.db.register.RegisterBean;
import com.config.BaseAction;
import com.signon.LoginBean;
import com.signon.LoginUser;
import com.transaction.register.RegisterTransaction;
import com.user.UserBean;

public class EditUserSave extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RegisterBean registerBean;
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}

	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		RegisterTransaction registerTransaction = new RegisterTransaction();
		if(registerTransaction.updateUser(registerBean, userBean.getRegId())){
			LoginUser loginUser = new LoginUser();
			loginUser.loadUpdatedBean(userBean, userBean.getLoginBean(),response,session);
			if(userBean.getRole()==2){
				return "teacher.editusersuccess";
			}else if(userBean.getRole()==3){
				return "student.editusersuccess";
			}
			return "success";
		}else{
			
			return ERROR;
		}
		
	}
	
}
