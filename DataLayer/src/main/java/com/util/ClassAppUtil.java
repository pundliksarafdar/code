package com.util;

import java.util.StringTokenizer;

public class ClassAppUtil {
	public static String getValue(String allparam,String param){
		StringTokenizer stringTokenizer = new StringTokenizer(allparam,";");
		String serviceParamValue = null;
		while (stringTokenizer.hasMoreElements()) {
			String params = stringTokenizer.nextToken();
			if(params.toLowerCase().startsWith(param.toLowerCase())){
				serviceParamValue = params.substring(params.indexOf('=')+1);
			}
		}
		return serviceParamValue;
	}
	
	public static String changeStatus(String allParam,String param,String status){
		String paramsUpdated = null;
		StringTokenizer stringTokenizer = new StringTokenizer(allParam,";");
		String serviceParamValue = null;
		while (stringTokenizer.hasMoreElements()) {
			String params = stringTokenizer.nextToken();
			if(params.toLowerCase().startsWith(param.toLowerCase())){
				serviceParamValue = params.substring(params.indexOf('=')+1);
			}
		}
		return paramsUpdated;
	}
	
	public static void main(String[] args) {
		String var  = getValue("rollGenerated=yes;","rollGenerated");
		System.out.println(var);
	}
}
