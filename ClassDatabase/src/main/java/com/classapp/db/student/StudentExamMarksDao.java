package com.classapp.db.student;

import java.math.BigDecimal;

public class StudentExamMarksDao {
	int examId;
	String examName;
	BigDecimal examMarks;
	int batchId;
	int divId;
	int instId;
	
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public BigDecimal getExamMarks() {
		return examMarks;
	}
	public void setExamMarks(BigDecimal examMarks) {
		this.examMarks = examMarks;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	public int getInstId() {
		return instId;
	}
	public void setInstId(int instId) {
		this.instId = instId;
	}
	
	
}
