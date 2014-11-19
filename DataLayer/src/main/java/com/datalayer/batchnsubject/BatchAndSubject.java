package com.datalayer.batchnsubject;

import java.util.Date;

public class BatchAndSubject {
	int rowId;
	int batch_id;
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	int subjectCode;
	int teacherCode;
	Date startTime;
	Date endTime;
	int regId;
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	
	public int getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}
	public int getTeacherCode() {
		return teacherCode;
	}
	public void setTeacherCode(int teacherCode) {
		this.teacherCode = teacherCode;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getRegId() {
		return regId;
	}
	public void setRegId(int regId) {
		this.regId = regId;
	}
	
	
	
}
