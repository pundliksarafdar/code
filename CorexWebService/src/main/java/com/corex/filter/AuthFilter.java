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
        	if(authToken==null || !authToken.trim().isEmpty()){
        		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        	}
        	
        	
        	System.out.println(authToken);
        }
	}

	
}
