package com.helper;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.batch.division.Division;
import com.classapp.db.batch.division.DivisionData;

public class DivisionHelperBean {
	List<Division> listOfDivision=new ArrayList<Division>();

	public List<Division> getListOfDivision() {
	DivisionData divisionData=new DivisionData();
		listOfDivision=divisionData.getAllDivision();
		return listOfDivision;
	}
	
	
}
