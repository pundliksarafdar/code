package com.service.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateExamBean implements Serializable{
	private Map<String, List<String>>examMap;
	
	private int instId;
	private int subId;
	private int division;
	private int batchId;
	private int questionPaperId;
	private int examId;
	
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public Map<String, List<String>> getExamMap() {
		return examMap;
	}
	public void setExamMap(Map<String, List<String>> examMap) {
		this.examMap = examMap;
	}
	public int getDivision() {
		return division;
	}
	public void setDivision(int division) {
		this.division = division;
	}
	public int getQuestionPaperId() {
		return questionPaperId;
	}
	public void setQuestionPaperId(int questionPaperId) {
		this.questionPaperId = questionPaperId;
	}
	public int getInstId() {
		return instId;
	}
	public void setInstId(int instId) {
		this.instId = instId;
	}
	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	
	
}
