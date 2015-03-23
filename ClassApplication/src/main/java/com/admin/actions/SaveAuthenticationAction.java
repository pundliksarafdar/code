package com.admin.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.urlaccess.PathAccess;
import com.config.BaseAction;
import com.transaction.urlaccess.URLTransaction;
import com.user.UserBean;

public class SaveAuthenticationAction extends BaseAction{
	
	String linkname[];
	String linkAccess[];
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		List<PathAccess> listPathAccesses = new ArrayList<PathAccess>();
		
		for (int i = 0; i < linkname.length; i++) {
			PathAccess pathAccess = new PathAccess();
			pathAccess.setAccess(linkAccess[i]);
			pathAccess.setPaths(linkname[i]);
			listPathAccesses.add(pathAccess);
		}
		
		URLTransaction urlTransaction = new URLTransaction();
		urlTransaction.setURLAccessBatch(listPathAccesses);
		return SUCCESS;
	}
	public String[] getLinkname() {
		return linkname;
	}
	public void setLinkname(String[] linkname) {
		this.linkname = linkname;
	}
	public String[] getLinkAccess() {
		return linkAccess;
	}
	public void setLinkAccess(String[] linkAccess) {
		this.linkAccess = linkAccess;
	}
	
	
}
