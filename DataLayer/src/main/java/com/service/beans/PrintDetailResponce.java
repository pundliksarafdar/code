package com.service.beans;

public class PrintDetailResponce {
	String instituteName;
	String address;
	String contactNo;
	BatchStudentFees batchStudentFees;
	FeeStructure feeStructure;
	int feeReceiptNumber;
	String todaysDate;
	String ammountInWords;
	double feesPayingNow;
	
	public String getInstituteName() {
		return instituteName;
	}
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public BatchStudentFees getBatchStudentFees() {
		return batchStudentFees;
	}
	public void setBatchStudentFees(BatchStudentFees batchStudentFees) {
		this.batchStudentFees = batchStudentFees;
	}
	public FeeStructure getFeeStructure() {
		return feeStructure;
	}
	public void setFeeStructure(FeeStructure feeStructure) {
		this.feeStructure = feeStructure;
	}
	public int getFeeReceiptNumber() {
		return feeReceiptNumber;
	}
	public void setFeeReceiptNumber(int feeReceiptNumber) {
		this.feeReceiptNumber = feeReceiptNumber;
	}
	public String getTodaysDate() {
		return todaysDate;
	}
	public void setTodaysDate(String todaysDate) {
		this.todaysDate = todaysDate;
	}
	public String getAmmountInWords() {
		return ammountInWords;
	}
	public void setAmmountInWords(String ammountInWords) {
		this.ammountInWords = ammountInWords;
	}
	public double getFeesPayingNow() {
		return feesPayingNow;
	}
	public void setFeesPayingNow(double feesPayingNow) {
		this.feesPayingNow = feesPayingNow;
	}
	
}
