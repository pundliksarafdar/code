package com.service.beans;

public class StudentDetailMarks {
	int batch_id;
	int sub_id;
	int exam_id;
	int marks;
	int avgMarks;
	int topperMarks;
	int examSubjectTotalMarks;
	String examPresentee;
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public int getAvgMarks() {
		return avgMarks;
	}
	public void setAvgMarks(int avgMarks) {
		this.avgMarks = avgMarks;
	}
	public int getTopperMarks() {
		return topperMarks;
	}
	public void setTopperMarks(int topperMarks) {
		this.topperMarks = topperMarks;
	}
	public String getExamPresentee() {
		return examPresentee;
	}
	public void setExamPresentee(String examPresentee) {
		this.examPresentee = examPresentee;
	}
	public int getExamSubjectTotalMarks() {
		return examSubjectTotalMarks;
	}
	public void setExamSubjectTotalMarks(int examSubjectTotalMarks) {
		this.examSubjectTotalMarks = examSubjectTotalMarks;
	}
	
}
