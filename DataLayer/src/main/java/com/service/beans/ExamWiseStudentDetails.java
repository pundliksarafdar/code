package com.service.beans;

import java.util.List;

public class ExamWiseStudentDetails {
	List<Integer> batchIds;
	List<StudentDetailMarks> detailMarklist;
	List<BatchExams> batchExamList;
	List<BatchExamSubjects> batchExamSubjectList;
	List<BatchExamDistinctSubjects> batchExamDistinctSubjectList;
	public List<StudentDetailMarks> getDetailMarklist() {
		return detailMarklist;
	}
	public void setDetailMarklist(List<StudentDetailMarks> detailMarklist) {
		this.detailMarklist = detailMarklist;
	}
	public List<BatchExams> getBatchExamList() {
		return batchExamList;
	}
	public void setBatchExamList(List<BatchExams> batchExamList) {
		this.batchExamList = batchExamList;
	}
	public List<BatchExamSubjects> getBatchExamSubjectList() {
		return batchExamSubjectList;
	}
	public void setBatchExamSubjectList(List<BatchExamSubjects> batchExamSubjectList) {
		this.batchExamSubjectList = batchExamSubjectList;
	}
	public List<BatchExamDistinctSubjects> getBatchExamDistinctSubjectList() {
		return batchExamDistinctSubjectList;
	}
	public void setBatchExamDistinctSubjectList(List<BatchExamDistinctSubjects> batchExamDistinctSubjectList) {
		this.batchExamDistinctSubjectList = batchExamDistinctSubjectList;
	}
	public List<Integer> getBatchIds() {
		return batchIds;
	}
	public void setBatchIds(List<Integer> batchIds) {
		this.batchIds = batchIds;
	}
	
}
