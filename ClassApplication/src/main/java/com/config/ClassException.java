package com.config;

import com.classapp.logger.AppLogger;

public class ClassException extends RuntimeException{
	public ClassException(RuntimeException e) {
		super(e);
		AppLogger.logger(e);
	}
}
