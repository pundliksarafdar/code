package com.transaction.batch.division;

import com.classapp.db.batch.division.AddDivision;
import com.classapp.db.batch.division.Division;
import com.classapp.db.batch.division.DivisionData;

public class DivisionTransactions {
	
	public static boolean addUpdateDb(Division division){
		
		if (!isDivisionExist(division)) {
			AddDivision.addOrUpdateDivision(division);
			return true;
		}		
		return false;
	}
	
	public static boolean isDivisionExist(Division division){		
		return DivisionData.isDivisionExist(division.getDivisionName());
	}
	
	 public static int getDivisionID(Division division){
		 return DivisionData.getDivisionId(division.getDivisionName());
	 }
}
