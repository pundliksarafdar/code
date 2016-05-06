package com.service.beans;

import java.sql.Date;

public class Student_Fees_Transaction {
	int inst_id;
	int student_id;
	int fees_transaction_id;
	int div_id;
	int batch_id;
	double amt_paid;
	int added_by;
	String note;
	Date transaction_dt;
	String disType;
	float discount;
	
	
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	public String getDisType() {
		return disType;
	}
	public void setDisType(String disType) {
		this.disType = disType;
	}
	
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getFees_transaction_id() {
		return fees_transaction_id;
	}
	public void setFees_transaction_id(int fees_transaction_id) {
		this.fees_transaction_id = fees_transaction_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public double getAmt_paid() {
		return amt_paid;
	}
	public void setAmt_paid(double amt_paid) {
		this.amt_paid = amt_paid;
	}
	public int getAdded_by() {
		return added_by;
	}
	public void setAdded_by(int added_by) {
		this.added_by = added_by;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getTransaction_dt() {
		return transaction_dt;
	}
	public void setTransaction_dt(Date transaction_dt) {
		this.transaction_dt = transaction_dt;
	}
	
	
	
}
