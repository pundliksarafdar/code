package com.global;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GlobalUtil {

	public static List getList(String string) {
		String [] stringarray=string.split(",");
		List list=new ArrayList();
		for (int i = 0; i < stringarray.length; i++) {
			list.add(stringarray[i]);
		}
		return list;
	}
	
	
}
