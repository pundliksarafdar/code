package com.listner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.classapp.logger.AppLogger;

public class TestListner implements ServletContextListener{

	public void contextInitialized(ServletContextEvent sce) {
		AppLogger.logger("listner initialised");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		AppLogger.logger("listener destroyed");
	}

}
