package com.exception;

public class NoUserBeanException extends RuntimeException{
	public NoUserBeanException() {
		super(ExceptionCode.NO_USER_BEAN.toString());
	}
}
