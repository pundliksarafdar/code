package com.transaction.batch.division;

import java.util.List;

import com.classapp.db.batch.division.AddDivision;
import com.classapp.db.batch.division.ClassDivision;
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
		 }
		 int div_id=divisionDB.getdivisionID(division);
		 ClassDivision classDivision=new ClassDivision();
			classDivision.setClass_id(classid);
			classDivision.setDiv_id(div_id);
			if(divisionDB.isclassDivisionExists(classDivision))
			{
		 return true;
			}else{
				divisionDB.addclassdivision(classDivision);
				return false;
			}
	}

	public List<Division> getAllDivisions(Integer regId) {
		DivisionDB divisionDB=new DivisionDB();
		List<Division> divisions=divisionDB.getAllDivision(regId);
		return divisions;
		
	}
	 
	 public int getDivisionIDByName(String divisionName){
		 return divisionData.getDivisionId(divisionName);
	 }
	 
}
