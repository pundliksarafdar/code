package com.service.beans;

import java.util.List;

import com.datalayer.subject.Subject;

public class ProgressCardServiceBean {
	List<ExamSubject> examSubjectList;
	List<Subject> subjectList;
	List<StudentData> studentDataList;
	List<Exam> examList;
	public List<ExamSubject> getExamSubjectList() {
		return examSubjectList;
	}
	public void setExamSubjectList(List<ExamSubject> examSubjectList) {
		this.examSubjectList = examSubjectList;
	}
	public List<Subject> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}
	public List<StudentData> getStudentDataList() {
		return studentDataList;
	}
	public void setStudentDataList(List<StudentData> studentDataList) {
		this.studentDataList = studentDataList;
	}
	public List<Exam> getExamList() {
		return examList;
	}
	public void setExamList(List<Exam> examList) {
		this.examList = examList;
	}
	
}
