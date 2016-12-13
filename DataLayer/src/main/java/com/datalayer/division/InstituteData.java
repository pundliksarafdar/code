package com.datalayer.division;

public class InstituteData {
	String batchName;
	String divisionName;
	String stream;
	String classname;
	int divId;
	int batchId;
	int classId;
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	
	@Override
	public boolean equals(Object obj) {
		InstituteData instituteData = (InstituteData)obj;
		boolean isEqual = instituteData.batchId == this.batchId && instituteData.classId == this.classId && instituteData.divId == this.divId;
		return isEqual;
	}
	
	@Override
	public int hashCode() {
		return ((Integer)this.batchId).hashCode() + ((Integer)this.classId).hashCode() + ((Integer)this.divId).hashCode();
	}
}
