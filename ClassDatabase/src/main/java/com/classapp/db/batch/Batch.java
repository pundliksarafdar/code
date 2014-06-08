package com.classapp.db.batch;

public class Batch {
	private Integer regId;
	private Integer batchCode;
	private String batch;
	
	public Integer getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(Integer batchCode) {
		this.batchCode = batchCode;
	}
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	
}
