package com.classapp.db.batch.division;


public class AddDivision {
	DivisionDB divisionDB;
	public AddDivision() {
		divisionDB=new DivisionDB();
	}
	public void addOrUpdateDivision(Division division){
		
		 divisionDB.updateDb(division);		
	}
}
