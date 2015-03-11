package com.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.servicetable.ServiceMap;

public class LoadConfig extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		ServiceMap.loadServiceMap();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			doPost(req, resp);
			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		init();
		System.out.println("Session timeout"+req.getSession().getMaxInactiveInterval());
		req.getSession().setMaxInactiveInterval(10);
		resp.getWriter().write("Loaded successfully");
		
		
	}
}
