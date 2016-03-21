package com.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts2.dispatcher.StaticContentLoader;

import com.classapp.logger.AppLogger;

//import AppLogger;

public class Test {
	static Logger logger=Logger.getLogger(Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			logger.info("its working");
			AppLogger.logger("yo");
		
	}
	

	public static void logger(String log) {
		/*String log4jConfigFile = "log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);*/
		logger.info(log);
		
	}
}
