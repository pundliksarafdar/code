package com.datalayer.batch;

import java.util.List;

public class BatchDataClass {
	private String batchName;
	private int batchId;
	private String registeredOn;
	private List<SubjectList> subjectList;
	private int candidatesInBatch;
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
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
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	
		
}
