package com.service.beans;

public class ExamSubject {
	private int exam_id;
	private int subjectId;
	private String subjectName;
	private int marks;
	private int batch_id;
	private boolean marksFlag;
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public boolean isMarksFlag() {
		return marksFlag;
	}
	public void setMarksFlag(boolean marksFlag) {
		this.marksFlag = marksFlag;
	}
	
}
