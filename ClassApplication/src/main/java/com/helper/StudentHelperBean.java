package com.helper;

import java.util.List;

import com.classapp.db.student.StudentDetails;
import com.transaction.student.StudentTransaction;



public class StudentHelperBean {
	private List<StudentDetails> students;
	
	private int class_id;

	public void setStudents(List<StudentDetails> students) {
		this.students = students;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	
	public List<StudentDetails> getStudents() {
		StudentTransaction studentTransaction= new StudentTransaction(class_id);
		students= studentTransaction.getAllStudentDetailsFromID();
		return students;
	}
}
