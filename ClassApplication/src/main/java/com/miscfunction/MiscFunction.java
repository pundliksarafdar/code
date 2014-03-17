package com.miscfunction;

public class MiscFunction {
	/*
	 * Function to change the date format from yyyyMMdd to yyyy-MM-dd
	 */
	public static String dateFormater(String date){
		String year = date.substring(0,4);
		String month = date.substring(4, 6);
		String days = date.substring(6, 8);
		
		return year+"-"+month+"-"+days;
	}
}
