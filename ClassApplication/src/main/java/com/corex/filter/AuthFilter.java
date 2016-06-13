package com.corex.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.core.ResourceMethodInvoker;

import com.service.AuthorizeRole;
import com.user.UserBean;


public class AuthFilter implements ContainerRequestFilter{
	@Context
	private HttpServletRequest request;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Filter.............");
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        
        boolean isAuthorizeAnnotiationPresent = method.isAnnotationPresent(AuthorizeRole.class);
        if(isAuthorizeAnnotiationPresent){
        	AuthorizeRole authorizeRole = method.getAnnotation(AuthorizeRole.class);
        	AuthorizeRole.ROLE role= authorizeRole.role();
        	System.out.println(role);
        	boolean isValid = validateRole(role.toString());
        	if(!isValid){
        		requestContext.abortWith(Response.status(Response.Status.BAD_GATEWAY).build());
        	}
        }
		
	}
	
	private boolean validateRole(String role){
		if(role.equals("all")){
			return true;
		}
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		if(userBean==null){
			return false;
		}
		
		if(userBean.getRole()==0 && role.indexOf("admin")!=-1){
			return true;
		}else if(userBean.getRole()==1 && role.indexOf("classowner")!=-1){
			return true;
		}else if(userBean.getRole()==2 && role.indexOf("teacher")!=-1){
			return true;
		}else if(userBean.getRole()==3 && role.indexOf("student")!=-1){
			return true;
		}
		
		return false;
	}
	
}
