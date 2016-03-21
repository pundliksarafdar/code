package com.service.beans;

import java.util.List;

public class FeeStructure {
	Fees fees;
	List<FeesStructure> feesStructureList;
	public Fees getFees() {
		return fees;
	}
	public void setFees(Fees fees) {
		this.fees = fees;
	}
	public List<FeesStructure> getFeesStructureList() {
		return feesStructureList;
	}
	public void setFeesStructureList(List<FeesStructure> feesStructureList) {
		this.feesStructureList = feesStructureList;
	}
	
	
}
