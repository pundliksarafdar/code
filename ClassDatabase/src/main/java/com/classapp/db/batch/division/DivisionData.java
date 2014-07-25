package com.classapp.db.batch.division;


import java.util.List;


public class DivisionData {

	public static boolean isDivisionExist(String divisionName){
		
		boolean status = false;
		
		List<Division> divisionList = DivisionDB.getAllDivisions();
	
		if (null!=divisionList && !divisionList.isEmpty()) {
			for(int i = 0;i<divisionList.size();i++){
				Division division = divisionList.get(i);
				System.out.println("DivName in the list:"+division.getDivisionName());
				System.out.println("Division Name:"+divisionName);
				if(division.getDivisionName().equalsIgnoreCase(divisionName)){
					status = true;
					break;
				}
			}
		}else{
			status = false;
		}
		return status;
	}
	
public static int getDivisionId(String divisionName){		
		int div_id = DivisionDB.retrive(divisionName).getDivId();
		return div_id;
	}

}
