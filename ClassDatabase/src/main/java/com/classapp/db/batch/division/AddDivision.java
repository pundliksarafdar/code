package com.classapp.db.batch.division;


public class AddDivision {
	
	public static void addOrUpdateDivision(Division division){
		 DivisionDB.updateDb(division);		
	}
}
