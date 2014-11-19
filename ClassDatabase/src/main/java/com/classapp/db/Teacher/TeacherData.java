package com.classapp.db.Teacher;

import java.util.List;

public class TeacherData {
TeacherDB teacherDB;
	
	public TeacherData() {
		teacherDB=new TeacherDB();
	}
	
	public List<TeacherDetails> getAllTeachersFromClass(int regId){
		return teacherDB.getAllTeachersFromClass(regId);
	}
	
	public List getSubjectTeacher(String subid) {
		List list=teacherDB.getSubjectTeacher(subid);
		return list;
		
	}
	public List getSubjectTeacher(String subid,int regId) {
		List list=teacherDB.getSubjectTeacher(subid,regId);
		return list;		
	}
	
	public Teacher getTeacher(int user_id, int class_id){		
		return teacherDB.getTeacher(user_id, class_id);
	}
	
}
