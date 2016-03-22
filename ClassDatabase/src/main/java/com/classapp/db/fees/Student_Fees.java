package com.classapp.db.fees;

import java.io.Serializable;

public class Student_Fees implements Serializable {
	int inst_id;
	int div_id;
	int batch_id;
	int student_id;
	double batch_fees;
	double discount;
	String discount_type;
	double final_fees_amt;
	double fees_paid;
	double fees_due;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
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
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public double getBatch_fees() {
		return batch_fees;
	}
	public void setBatch_fees(double batch_fees) {
		this.batch_fees = batch_fees;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getDiscount_type() {
		return discount_type;
	}
	public void setDiscount_type(String discount_type) {
		this.discount_type = discount_type;
	}
	public double getFinal_fees_amt() {
		return final_fees_amt;
	}
	public void setFinal_fees_amt(double final_fees_amt) {
		this.final_fees_amt = final_fees_amt;
	}
	public double getFees_paid() {
		return fees_paid;
	}
	public void setFees_paid(double fees_paid) {
		this.fees_paid = fees_paid;
	}
	public double getFees_due() {
		return fees_due;
	}
	public void setFees_due(double fees_due) {
		this.fees_due = fees_due;
	}
	
	
	
}
