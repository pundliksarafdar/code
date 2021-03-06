package com.transaction.batch.division;

import java.util.List;

import com.classapp.db.batch.division.AddDivision;
import com.classapp.db.batch.division.Division;
import com.classapp.db.batch.division.DivisionDB;
import com.classapp.db.batch.division.DivisionData;

public class DivisionTransactions {
	DivisionData divisionData;
	AddDivision addDivision;
	
	public DivisionTransactions() {
		divisionData= new DivisionData();
		addDivision= new AddDivision();
	}
	public boolean addUpdateDb(Division division){
		
		if (!isDivisionExist(division)) {
			addDivision.addOrUpdateDivision(division);
			return true;
		}		
		return false;
	}
	
	
	
	public boolean isDivisionExist(Division division){		
		return divisionData.isDivisionExist(division.getDivisionName());
	}
	
	 public int getDivisionID(Division division){
		 return divisionData.getDivisionId(division.getDivisionName(),division.getStream());
	 }
	 
	 public boolean rollbackDivision(Division division){
		 return divisionData.deleteDivision(division.getDivId());
	 }

	 public boolean addDivision(Division division,int classid) {
		 DivisionDB divisionDB=new DivisionDB();
		 if(!divisionDB.isDivisionExists(division))
		 {
			 divisionDB.updateDb(division);
			 return false;
		 }
		 return true;
	}

	public List<Division> getAllDivisions(Integer regId) {
		DivisionDB divisionDB=new DivisionDB();
		List<Division> divisions=divisionDB.getAllDivision(regId);
		return divisions;
		
	}
	 
	 public int getDivisionIDByName(String divisionName){
		 return divisionData.getDivisionId(divisionName);
	 }
	 
	 public boolean updateClass(Division division) {
		 DivisionDB db=new DivisionDB();
		 if(!db.isDivisionExists(division)){
			 
			 db.updateDb(division);
			 return false;
		 }
		return true;
	}
	 
	 public boolean deletedivision(int classid) {
		 DivisionDB db=new DivisionDB();
		 db.deletedivision(classid);
		 return true;
	}
	 
	 public Division getDidvisionByID(int divID) {
		DivisionDB db=new DivisionDB();
		return db.retriveByID(divID);
	}
	 
}
