package com.service.beans;

import java.util.List;

public class BatchStudentExamMarks {
	List<StudentData> studentDataList;
	List<ExamSubject> examSubjectList;
	public List<StudentData> getStudentDataList() {
		return studentDataList;
	}
	public void setStudentDataList(List<StudentData> studentDataList) {
		this.studentDataList = studentDataList;
	}
	public List<ExamSubject> getExamSubjectList() {
		return examSubjectList;
	}
	public void setExamSubjectList(List<ExamSubject> examSubjectList) {
		this.examSubjectList = examSubjectList;
	}
	
}
