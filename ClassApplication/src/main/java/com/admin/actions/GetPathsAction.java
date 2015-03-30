package com.admin.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.urlaccess.PathAccess;
import com.config.BaseAction;
import com.config.Constants;
import com.transaction.urlaccess.URLTransaction;
import com.user.UserBean;

public class GetPathsAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		List<PathAccess> pathAccesses = new ArrayList<PathAccess>();
		for(String path:Constants.ALL_ACTION_URLs) {
			PathAccess pathAccess = new PathAccess();
			pathAccess.setAccess("0,1,2,3");
			pathAccess.setPaths(path);
			pathAccesses.add(pathAccess);
		}
		URLTransaction urlTransaction = new URLTransaction();
		List<PathAccess> pathAccessesMerged = urlTransaction.getURLAndAccess(pathAccesses);
		
		request.getSession().setAttribute(Constants.ACTION_URLS, pathAccessesMerged);
		return SUCCESS;
	}
}
