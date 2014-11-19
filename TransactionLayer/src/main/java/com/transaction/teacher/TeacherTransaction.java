package com.transaction.teacher;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDB;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.register.RegisterBean;
import com.transaction.register.RegisterTransaction;

public class TeacherTransaction {
	TeacherDB teacherDB;
	int class_id;
	
	public TeacherTransaction() {
		teacherDB=new TeacherDB();
	}
	
	public TeacherTransaction(int class_id) {
		this.class_id=class_id;
		teacherDB=new TeacherDB();
	}
	public String addTeacher(String teacherLoginName,int regID,String subjects) {
		if(teacherDB.isTeacherRegistered(teacherLoginName))
			{
				if(!teacherDB.isTeacherExists(teacherLoginName,regID))
				{
					teacherDB.add(teacherLoginName,regID,subjects);
					return "added";
				}else{
					return "exists";
				}
				
			}else{
		
		return "false";
	}

}
	public boolean updateTeacher(Teacher teacher){		
		return teacherDB.updateDb(teacher);		
	}
	
	public Teacher getTeacher(int user_id, int class_id){		
		return teacherDB.getTeacher(user_id, class_id);
	}
	
	public boolean deleteTeacher(int user_id, int class_id){
		return teacherDB.deleteTeacher(user_id, class_id);
	}
	
	public List<TeacherDetails> getAllTeachersFromClass(int regId){
		return teacherDB.getAllTeachersFromClass(regId);
	}
	
	public List getSubjectTeacher(String subid) {
		
		TeacherDB teacherDB=new TeacherDB();
		List list=teacherDB.getSubjectTeacher(subid);
		return list;
		
	}
	public List getSubjectTeacher(String subid,int regId) {
		
		TeacherDB teacherDB=new TeacherDB();
		List list=teacherDB.getSubjectTeacher(subid,regId);
		return list;
		
	}

	
public List getTeachersClass(int regId) {
		
		TeacherDB teacherDB=new TeacherDB();
		List list=teacherDB.getTeachersClass(regId);
		return list;
		
	}

	public List<TeacherDetails> getAllTeachersFromClass(){
		return teacherDB.getAllTeachersFromClass(class_id);
	}
	
	public List getSubjectTeacherFromClass(String subid) {
		List list=teacherDB.getSubjectTeacher(subid,class_id);
		return list;		
	}
	
	public com.classapp.db.Teacher.Teacher getTeacher(int user_id){		
		return teacherDB.getTeacher(user_id, class_id);
	}
	
	public List<List<RegisterBean>> getScheduleTeacher(List<Schedule> Schedulelist,int regId) {
		TeacherDB teacherDB=new TeacherDB();
		int counter=0;
		List<List<RegisterBean>> registerBeans=new ArrayList<List<RegisterBean>>();
		while(counter<Schedulelist.size())
		{
		List list=teacherDB.getSubjectTeacher(Schedulelist.get(counter).getSub_id()+"",regId);
		RegisterBean bean=new RegisterBean();
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> teachers=registerTransaction.getTeacherName(list);
		registerBeans.add(teachers);
		counter++;
		}
		return registerBeans;
	}
	
}
