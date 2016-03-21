package com.classapp.db.fees;

public class FeesStructure {
	int fees_structure_id;
	int fees_id;
	int inst_id;
	String fees_structure_desc;
	public int getFees_structure_id() {
		return fees_structure_id;
	}
	public void setFees_structure_id(int fees_structure_id) {
		this.fees_structure_id = fees_structure_id;
	}
	public int getFees_id() {
		return fees_id;
	}
	public void setFees_id(int fees_id) {
		this.fees_id = fees_id;
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public String getFees_structure_desc() {
		return fees_structure_desc;
	}
	public void setFees_structure_desc(String fees_structure_desc) {
		this.fees_structure_desc = fees_structure_desc;
	}
	
}
