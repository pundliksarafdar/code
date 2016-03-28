package com.transaction.studentmarks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.classapp.db.exam.ExamDB;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.StudentMarksDB;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.ExamSubject;
import com.service.beans.StudentData;
import com.service.beans.StudentSubjectMarks;
import com.service.beans.SubjectsExam;

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
	
	public BatchStudentExamMarks getStudentExamMarks(int inst_id, int div_id,int batch_id, int exam_id) {
		ExamPaperDB db = new ExamPaperDB();
		List<ExamSubject> subjectlist = new ArrayList<ExamSubject>(); 
		List list =  db.getExamSubjects(inst_id, div_id, batch_id, exam_id);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object [] object = (Object []) iterator.next();
			ExamSubject subject = new ExamSubject();
			subject.setSubjectName((String)object[0]);
			subject.setSubjectId(((Number)object[1]).intValue());
			subject.setMarks(((Number)object[2]).intValue());
			subjectlist.add(subject);
		}
		StudentMarksDB marksDB=new StudentMarksDB();
		BatchStudentExamMarks batchStudentExamMarks = new BatchStudentExamMarks();
		List<StudentData> dataList = new ArrayList<StudentData>();
		List marksList =  marksDB.getStudentExamMarks(inst_id, div_id, batch_id, exam_id);
		StudentDB studentDB = new StudentDB();
		List students =  studentDB.getStudentrelatedtoBatchForExamMarks(batch_id+"", inst_id, div_id);
		for (Iterator iterator = students.iterator(); iterator.hasNext();) {
			Object[] obj = (Object[]) iterator.next();
			for (Iterator iterator2 = subjectlist.iterator(); iterator2.hasNext();) {
				ExamSubject examSubject = (ExamSubject) iterator2.next();
				 boolean flag = false;
				for (Iterator iterator3 = marksList.iterator(); iterator3.hasNext();) {
					Object[] object = (Object[]) iterator3.next();
					if((examSubject.getSubjectId() == ((Number)object[4]).intValue()) && ((Number)object[5]).intValue()==((Number)obj[2]).intValue()){
						StudentData data = new StudentData();
						data.setFname((String) object[0]);
						data.setLname((String) object[1]);
						data.setMarks(((Number)object[2]).intValue());
						dataList.add(data);
						flag = true;
						break;
					}
				}
				if(flag == false){
					StudentData data = new StudentData();
					data.setFname((String) obj[0]);
					data.setLname((String) obj[1]);
					data.setMarks(0);
					dataList.add(data);
				}
			}
		}
	//	boolean flag = true;
		/*Object[] object = null;
		
		for (Iterator iterator = marksList.iterator(); iterator.hasNext();) {
			 object = (Object[]) iterator.next();
			
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				Object[] obj = (Object[]) iterator2.next();
				if(((Number)obj[1]).intValue() == ((Number)object[4]).intValue()){
					StudentData data = new StudentData();
					data.setFname((String) object[0]);
					data.setLname((String) object[1]);
					data.setMarks(((Number)object[2]).intValue());
					dataList.add(data);
					
					break;
				}
			}
			if(flag == false){
				
			}
			
		}*/
		batchStudentExamMarks.setExamSubjectList(subjectlist);
		batchStudentExamMarks.setStudentDataList(dataList);
		return batchStudentExamMarks;
	}
	
	public StudentSubjectMarks getStudentExamSubjectMarks(int inst_id, int div_id,int batch_id, int sub_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List examList = examPaperDB.getSubjectsExam(inst_id, div_id, batch_id, sub_id);
		StudentMarksDB db = new StudentMarksDB();
		List list =  db.getStudentExamSubjectMarks(inst_id, div_id, batch_id, sub_id);
		List<SubjectsExam> subjectExamList = new ArrayList<SubjectsExam>();
		for (Iterator iterator = examList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			SubjectsExam subjectsExam = new SubjectsExam();
			subjectsExam.setExam_id(((Number)object[0]).intValue());
			subjectsExam.setExam_name((String) object[1]);
			subjectExamList.add(subjectsExam);
		}
		StudentDB studentDB = new StudentDB();
		List<StudentData> dataList = new ArrayList<StudentData>();
		List students =  studentDB.getStudentrelatedtoBatchForExamMarks(batch_id+"", inst_id, div_id);
		for (Iterator iterator = students.iterator(); iterator.hasNext();) {
			Object[] obj = (Object[]) iterator.next();
			for (Iterator iterator2 = subjectExamList.iterator(); iterator2.hasNext();) {
				SubjectsExam subjectsExam = (SubjectsExam) iterator2.next();
				boolean flag = false;
				for (Iterator iterator3 = list.iterator(); iterator3.hasNext();) {
					Object[] object = (Object[]) iterator3.next();
					if(((Number)object[4]).intValue() == subjectsExam.getExam_id() && ((Number)object[5]).intValue() == ((Number)obj[2]).intValue()){
						StudentData data = new StudentData();
						data.setFname((String) object[0]);
						data.setLname((String) object[1]);
						data.setMarks(((Number)object[2]).intValue());
						dataList.add(data);
						flag = true;
						break;
					}
				}
				if(flag == false){
					StudentData data = new StudentData();
					data.setFname((String) obj[0]);
					data.setLname((String) obj[1]);
					data.setMarks(0);
					dataList.add(data);
				}
			}
		}
		
		StudentSubjectMarks studentSubjectMarks = new StudentSubjectMarks();
		studentSubjectMarks.setExamList(subjectExamList);
		studentSubjectMarks.setStudentDataList(dataList);
		return studentSubjectMarks;
	}
}
