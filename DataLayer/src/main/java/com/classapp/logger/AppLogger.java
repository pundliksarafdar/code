package com.classapp.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class AppLogger {
	static Logger logger=Logger.getLogger(AppLogger.class);
	/*static{
		logger.setLevel(Level.ERROR);
	}*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
	}

	public static void logger(Object log) {
	logger.info(log);
	}
	
	public static void logError(Object log) {
		logger.error(log);
		}
	public static void logDebug(Object log) {
		logger.debug(log);
		}
}
