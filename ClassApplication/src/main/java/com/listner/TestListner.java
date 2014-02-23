package com.listner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestListner implements ServletContextListener{

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("listner initialised");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("listener destroyed");
	}

}
