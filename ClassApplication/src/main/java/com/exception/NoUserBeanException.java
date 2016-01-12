package com.exception;

public class NoUserBeanException extends RuntimeException{
	public NoUserBeanException() {
		super("Userbean not available in session, Check is user is logged in....");
	}
}
