package com.service.beans;

public class PrintDetailResponce {
	String instituteName;
	String address;
	String contactNo;
	BatchStudentFees batchStudentFees;
	FeeStructure feeStructure;
	
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
	
	
	
	
}
