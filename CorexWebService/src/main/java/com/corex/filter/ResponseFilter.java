package com.corex.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;

import com.corex.common.Common;
import com.mobile.bean.SessionBean;

public class ResponseFilter implements ContainerResponseFilter{

	@Context
	private HttpServletRequest request;

	@Context
    private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		String auth = requestContext.getHeaderString("auth");
		SessionBean sessionBean = Common.userMap.get(auth);
		if(sessionBean!=null)sessionBean.refresh();
	}

}
