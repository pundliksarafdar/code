package com.corex.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.corex.common.Common;

public class MobileServiceBase {
	@Context
	private HttpServletRequest request;

	public final int getRegId(){
		String authToken = request.getHeader("auth");
		int regId = Common.userMap.get(authToken).getUserBean().getRegId();
		return regId;
	}
}
