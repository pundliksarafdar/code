package com.service.beans;

public class StudentFessNotificationData {
	int batch_id;
	String student_name;
	String parent_name;
	String inst_name;
	double total_fees;
	double fees_paid;
	double fee_due;
	String batch_name;
	double amount_paid;
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public double getTotal_fees() {
		return total_fees;
	}
	public void setTotal_fees(double total_fees) {
		this.total_fees = total_fees;
	}
	public double getFees_paid() {
		return fees_paid;
	}
	public void setFees_paid(double fees_paid) {
		this.fees_paid = fees_paid;
	}
	public double getFee_due() {
		return fee_due;
	}
	public void setFee_due(double fee_due) {
		this.fee_due = fee_due;
	}
	public String getBatch_name() {
		return batch_name;
	}
	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	public double getAmount_paid() {
		return amount_paid;
	}
	public void setAmount_paid(double amount_paid) {
		this.amount_paid = amount_paid;
	}
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public String getInst_name() {
		return inst_name;
	}
	public void setInst_name(String inst_name) {
		this.inst_name = inst_name;
	}
	
}
