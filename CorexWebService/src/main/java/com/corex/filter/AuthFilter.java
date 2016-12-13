package com.corex.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.corex.annotation.AuthorizeRole;
import com.corex.common.Common;
import com.mobile.bean.SessionBean;

public class AuthFilter implements ContainerRequestFilter{
	
	@Context
	private HttpServletRequest request;

	@Context
    private ResourceInfo resourceInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Method method = resourceInfo.getResourceMethod();
        
        boolean isAuthorizeAnnotiationPresent = method.isAnnotationPresent(AuthorizeRole.class);
        if(isAuthorizeAnnotiationPresent){
        	String authToken = request.getHeader("auth");
        	SessionBean sessionBean = Common.userMap.get(authToken);
        	if(authToken==null || authToken.trim().isEmpty() || sessionBean==null || !sessionBean.isValid()){
        		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        	}
        	System.out.println(authToken);
        }
        
	}

	
}
