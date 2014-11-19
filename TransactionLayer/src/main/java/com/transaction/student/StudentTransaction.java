package com.transaction.student;

import java.util.List;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.student.StudentData;
import com.classapp.db.student.StudentDetails;

public class StudentTransaction {
	StudentData studentData;
	int class_id;
	public StudentTransaction() {
		this.studentData=new StudentData();		
	}
	
	public StudentTransaction(int class_id) {
		this.studentData=new StudentData();
		this.class_id=class_id;
	}
	
	public boolean addUpdateDb(Student student){
		boolean status = false;
		if (!this.studentData.isStudentExistInClass(student)){
			this.studentData.addOrUpdateStudent(student);
			return true;
		}	
		return status;
	}
	
	public RegisterBean getStudentDetailsFromID(Student student){		
			return this.studentData.getStudentDetailsFromID(student.getStudent_id());
			
	}
	
	public boolean updateStudentDb(Student student){
		boolean status = false;
		if (this.studentData.isStudentExistInClass(student)){
			this.studentData.addOrUpdateStudent(student);
			return true;
		}	
		return status;
	}

	public boolean deleteStudent(int studentId, int classId) {
		
		return this.studentData.deleteStudentFromClass(studentId, classId);
	}
	public RegisterBean getStudent(String studentLoginName){
		return  this.studentData.getStudent(studentLoginName);
	}
	
	public Student getStudentDetailsFromClass(int student_id){
		return  this.studentData.getStudentDetailsFromClass(student_id,class_id);
	}
		
	public List<StudentDetails> getAllStudentDetailsFromID(){
		return  this.studentData.getAllStudentDetailsFromID(class_id);
	}
	public List<Student> getStudent(int regID) {
		StudentDB studentDB=new StudentDB();
		List<Student> list=studentDB.getStudentinfo(regID);
		return list;
		
	}
	
	public Student getclassStudent(int studentID,int classID) {
		StudentDB studentDB=new StudentDB();
		Student list=studentDB.getclassStudent(studentID,classID);
		return list;
		
	}
}
