package com.service.beans;

public class StudentExcelUploadBean {
	String fileName;
	int divId;
	int[] batchId;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	
	public int[] getBatchId() {
		return batchId;
	}
	public void setBatchId(int[] batchId) {
		this.batchId = batchId;
	}
	
	@Override
	public String toString() {
		return "StudentExcelUploadBean [fileName=" + fileName + ", divId="
				+ divId + ", batchId=" + batchId + "]";
	}		
}
