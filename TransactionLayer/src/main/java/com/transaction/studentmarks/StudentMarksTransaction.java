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
	public int getStudentMarksCount(int inst_id,List<Integer> student_id,int sub_id,int div_id,int examID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentMarksCount(inst_id, student_id, sub_id, div_id,examID);
	}
	
	public List<StudentMarks> getStudentMarks(int inst_id,List<Integer> student_id,int sub_id,int div_id,int currentPage,int examID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentMarks(inst_id, student_id, sub_id, div_id,currentPage,examID);
	}
	
	public boolean deleteStudentMarksrelatedtosubject(int sub_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtosubject(sub_id);
	}
	
	public boolean deleteStudentMarksrelatedtodivision(int div_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtodivision(div_id);
	}
	
	public boolean deleteStudentMarksrelatedtoexam(int inst_id,int div_id,int sub_id,int exam_ID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtoexam(inst_id, div_id, sub_id, exam_ID);
	}
	
	public boolean deleteStudentMarksrelatedtostudentID(int inst_id,int student_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtostudentID(inst_id, student_id);
	}
	
	public StudentMarks getStudentExamMark(int inst_id,int student_id,int div_id,int sub_id,int exam_ID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentExamMark(inst_id, student_id, sub_id, div_id, exam_ID);
	}
}
