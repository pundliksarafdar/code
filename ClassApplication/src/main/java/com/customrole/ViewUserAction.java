package com.customrole;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.roll.CustomUser;
import com.classapp.db.roll.Inst_roll;
import com.config.BaseAction;
import com.transaction.instroll.InstRollTransaction;
import com.user.UserBean;

public class ViewUserAction extends BaseAction{
	List<Inst_roll> roleList;
	List<CustomUser> customUserList;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		InstRollTransaction transaction = new InstRollTransaction();
		roleList = transaction.getInstituteRoles(userBean.getInst_id());
		customUserList = transaction.getInstUsers(userBean.getInst_id());
		return SUCCESS;
	}
	public List<Inst_roll> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Inst_roll> roleList) {
		this.roleList = roleList;
	}
	public List<CustomUser> getCustomUserList() {
		return customUserList;
	}
	public void setCustomUserList(List<CustomUser> customUserList) {
		this.customUserList = customUserList;
	}
	
	
	
}