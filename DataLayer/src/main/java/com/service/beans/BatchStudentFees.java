package com.service.beans;

import java.sql.Date;

public class BatchStudentFees {
	int student_id;
	String fname;
	String lname;
	double batch_fees;
	double discount;
	String discount_type;
	double final_fees_amt;
	double fees_paid;
	double fees_due;
	Date paidOn;
	
	
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
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
		return !discount_type.equals("amt")?" %":" Rs";
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
	public Date getPaidOn() {
		return paidOn;
	}
	public void setPaidOn(Date paidOn) {
		this.paidOn = paidOn;
	}
	
	
}
