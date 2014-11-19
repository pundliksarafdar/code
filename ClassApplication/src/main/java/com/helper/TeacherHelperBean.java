package com.helper;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Teacher.TeacherDetails;
import com.transaction.teacher.TeacherTransaction;

public class TeacherHelperBean {
	private int class_id;
	private List<TeacherDetails> teachers=new ArrayList<TeacherDetails>();
	
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public List<TeacherDetails> getTeachers() {
		TeacherTransaction teacherTransaction= new TeacherTransaction(class_id);
		teachers=teacherTransaction.getAllTeachersFromClass();
		return teachers;
	}
	
	public void setTeachers(List<TeacherDetails> teachers) {
		this.teachers = teachers;
	}
	
}
