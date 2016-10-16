package com.classapp.utils;

import java.util.List;

import com.classapp.urlaccess.PathAccess;

public class Constants {
	public enum IMAGE_TYPE{
		L //Logo image
	}
	public static List<PathAccess> PATH_ACCESS_LIST;
	public static String LOGO_IMAGE_PATH = "imageLogo";
	
	public static enum LAST_FEE_PARAM{
		BATCH_FEE(3),DISCOUNT(4),DISCOUNT_TYPE(5),FEES_DUE(8),FEES_PAID(7),FINAL_FEES_AMT(9),TRANSACTION_DATE(10),FIRST_NAME(1),LAST_NAME(2);
		
		private final int ordinalvalue;
		private LAST_FEE_PARAM(int ordinalvalue){
			this.ordinalvalue = ordinalvalue;
		}
		public int getOrdinalvalue(){
			return this.ordinalvalue;
		}
	}
	
	public static enum SYLLABUS_STATE {
		COMPLETE,INCOMPLETE,EDITABLE
	}
	
}
