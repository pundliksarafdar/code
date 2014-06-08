package com.datalayer.batch;

import java.util.List;

public class BatchDataClass {
	private String batchName;
	private Integer batchCode;
	private String registeredOn;
	private List<SubjectList> subjectList;
	private int candidatesInBatch;
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public Integer getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(Integer batchCode) {
		this.batchCode = batchCode;
	}
	public String getRegisteredOn() {
		return registeredOn;
	}
	public void setRegisteredOn(String registeredOn) {
		this.registeredOn = registeredOn;
	}
	public List<SubjectList> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<SubjectList> subjectList) {
		this.subjectList = subjectList;
	}
	public int getCandidatesInBatch() {
		return candidatesInBatch;
	}
	public void setCandidatesInBatch(int candidatesInBatch) {
		this.candidatesInBatch = candidatesInBatch;
	}
	
		
}
