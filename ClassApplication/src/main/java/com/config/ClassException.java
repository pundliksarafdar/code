package com.config;

public class ClassException extends RuntimeException{
	public ClassException(RuntimeException e) {
		super(e);
		System.out.println(e);
	}
}
