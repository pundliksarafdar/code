package com.transaction.studentmarks;

import java.util.List;

import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.StudentMarksDB;

public class StudentMarksTransaction {

	public boolean saveStudentMarks(StudentMarks studentMarks) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.saveStudentMarks(studentMarks);
	}
	
	public List<StudentMarks> getStudentMarksList(int inst_id,int student_id,int sub_id,int div_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentMarksList(inst_id, student_id, sub_id, div_id);
	}
	
	public boolean isExamSolvedByStudent(int exam_id,int inst_id,int sub_id,int div_id,int student_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.isExamSolvedByStudent(exam_id, inst_id, sub_id, div_id, student_id);
	}
}
