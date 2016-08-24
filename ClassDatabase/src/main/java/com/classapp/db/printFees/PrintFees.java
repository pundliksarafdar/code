package com.classapp.db.printFees;

import java.io.Serializable;

public class PrintFees implements Serializable{
	int inst_id;
	int receipt_id;
	int student_id;
	double fees_transaction_id;
	String fee_type;
	
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getReceipt_id() {
		return receipt_id;
	}
	public void setReceipt_id(int receipt_id) {
		this.receipt_id = receipt_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public double getFees_transaction_id() {
		return fees_transaction_id;
	}
	public void setFees_transaction_id(double fees_transaction_id) {
		this.fees_transaction_id = fees_transaction_id;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	
	
}
