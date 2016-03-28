package com.service.beans;

import java.util.List;

public class StudentSubjectMarks {
	List<SubjectsExam> examList;
	List<StudentData> studentDataList;
	public List<SubjectsExam> getExamList() {
		return examList;
	}
	public void setExamList(List<SubjectsExam> examList) {
		this.examList = examList;
	}
	public List<StudentData> getStudentDataList() {
		return studentDataList;
	}
	public void setStudentDataList(List<StudentData> studentDataList) {
		this.studentDataList = studentDataList;
	}
	
}
