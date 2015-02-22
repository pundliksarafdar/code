package com.miscfunction;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

public class MiscFunction {
	/*
	 * Function to change the date format from yyyyMMdd to yyyy-MM-dd
	 */
	private static ServletContext servletContext;
	private MiscFunction(){
		
	}
	public static String dateFormater(String date){
		try{
			String year = date.substring(0,4);
			String month = date.substring(4, 6);
			String days = date.substring(6, 8);
			return year+"-"+month+"-"+days;
		}catch(Exception e){
			return "Error";
		}
		
	}
	
	public static String roleConverter(Integer role){
		if(0 == role){
			return "Admin";
		}else if(1 == role){
			return "Class Owner";
		}else if(2 == role){
			return "Class Teacher";
		}else{
			return "Student";
		}
	}
	
	public static ServletContext getServletContext(){
		if(null == servletContext){
			servletContext = ServletActionContext.getServletContext();
		}
		return servletContext;
	}
	
	public static void setServletContext(ServletContext context){
		servletContext = context;
	}
}
