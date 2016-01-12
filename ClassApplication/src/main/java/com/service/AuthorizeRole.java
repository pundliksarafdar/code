package com.service;

public @interface AuthorizeRole {
	public enum ROLE{
		all,admin,classowner,teacher,student
	}
	
	ROLE role() default ROLE.all;
	
}
