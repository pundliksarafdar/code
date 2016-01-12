package com.exception;

public class NoUserBeanException extends RuntimeException{
	public NoUserBeanException() {
		super("User not actve");
	}
}
