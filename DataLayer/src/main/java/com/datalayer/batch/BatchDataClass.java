package com.datalayer.batch;

public class BatchDataClass {
	private String batchName;
	private String registeredOn;
	private Timmings timmings;
	private SubjectList subjectList;
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
	public Timmings getTimmings() {
		return timmings;
	}
	public void setTimmings(Timmings timmings) {
		this.timmings = timmings;
	}
	public SubjectList getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(SubjectList subjectList) {
		this.subjectList = subjectList;
	}
	public int getCandidatesInBatch() {
		return candidatesInBatch;
	}
	public void setCandidatesInBatch(int candidatesInBatch) {
		this.candidatesInBatch = candidatesInBatch;
	}
	
}
