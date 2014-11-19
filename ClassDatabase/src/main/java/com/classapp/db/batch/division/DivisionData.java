package com.classapp.db.batch.division;


import java.util.List;


public class DivisionData {
	DivisionDB divisionDB;
	
	public DivisionData() {
		divisionDB=new DivisionDB();
	}

	public boolean isDivisionExist(String divisionName){
		
		boolean status = false;
		
		List<Division> divisionList = divisionDB.getAllDivisions();
	
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
	
public int getDivisionId(String divisionName,String stream){		
		int div_id =divisionDB.retrive(divisionName,stream).getDivId();
		return div_id;
	}

public boolean deleteDivision(int div_id){
	return divisionDB.removeByDivID(div_id);
}

public List<Division> getAllDivision(){
	return divisionDB.getAllDivisions();
}

}
