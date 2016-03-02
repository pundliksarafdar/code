package com.service.beans;

import java.util.List;

public class BatchFeesDistributionServiceBean {
	int batch_id;
	int div_id;
	int fees_id;
	String fees_desc;
	int fees_structure_id;
	String fees_structure_desc;
	int fees_amount;
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public int getFees_id() {
		return fees_id;
	}
	public void setFees_id(int fees_id) {
		this.fees_id = fees_id;
	}
	public String getFees_desc() {
		return fees_desc;
	}
	public void setFees_desc(String fees_desc) {
		this.fees_desc = fees_desc;
	}
	public int getFees_structure_id() {
		return fees_structure_id;
	}
	public void setFees_structure_id(int fees_structure_id) {
		this.fees_structure_id = fees_structure_id;
	}
	public String getFees_structure_desc() {
		return fees_structure_desc;
	}
	public void setFees_structure_desc(String fees_structure_desc) {
		this.fees_structure_desc = fees_structure_desc;
	}
	public int getFees_amount() {
		return fees_amount;
	}
	public void setFees_amount(int fees_amount) {
		this.fees_amount = fees_amount;
	}
	
	
	
}
