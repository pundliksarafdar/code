package com.service.beans;

public class StudentFessNotificationData {
	String student_name;
	String parent_name;
	double total_fees;
	double fees_paid;
	double fee_due;
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
	
}
