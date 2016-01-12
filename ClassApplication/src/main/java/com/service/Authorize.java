package com.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
	public enum Role {
		ADMIN("administrator"),
		SECURITY("securityofficer"),
		ALL("All");
		
		public final String roleValue;

		private Role(String roleValue) {
			this.roleValue = roleValue;
		}
	
	}
	Role rolename();
	boolean refreshSession() default false;
}