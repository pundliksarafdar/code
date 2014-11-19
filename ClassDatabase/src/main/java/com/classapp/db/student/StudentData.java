package com.classapp.db.student;

import java.util.List;

import com.classapp.db.register.RegisterBean;

public class StudentData {
	StudentDB studentdb;
	
	public StudentData() {
		this.studentdb=new StudentDB();
	}
	public void addOrUpdateStudent(Student student) {
		this.studentdb.updateDb(student);
	}

	public boolean isStudentExistInClass(Student student) {
		return this.studentdb.isStudentExistsInClass(student.getStudent_id(), student.getClass_id());
	}
	
	public boolean deleteStudentFromClass(int studentId,int classId) {
		if(this.studentdb.isStudentExistsInClass(studentId, classId)){
			return this.studentdb.deleteStudent(studentId,classId);
		}
		return false;
	}
	
	public RegisterBean getStudent(String studentLoginName){
		return this.studentdb.getStudent(studentLoginName);
	}
	
	public Student getStudentDetailsFromClass(int student_id, int class_id){
		return this.studentdb.getStudentDetailsFromClass(student_id,class_id);
	}
	
	public String[] getAssignedBatchesForStudent(String studentLoginName){
		return this.studentdb.getAssignedBatcheIds(studentLoginName);
	}
	
	public RegisterBean getStudentDetailsFromID(int student_id){
		return this.studentdb.getStudentDetailsFromID(student_id);
	}
	
	public List<StudentDetails> getAllStudentDetailsFromID(int class_id){
		return this.studentdb.getAllStudentsDetails(class_id);
	}
}
