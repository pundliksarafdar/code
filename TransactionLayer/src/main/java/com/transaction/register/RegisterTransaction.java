package com.transaction.register;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.student.Student;

public class RegisterTransaction {

	public List getTeacherName(List TeacherIDs) {
		RegisterDB db=new RegisterDB();
		List list=db.getTeacherName(TeacherIDs);
		return list;
	}
	
	public List<RegisterBean> getScheduleTeacher(List<Schedule> list) {
		List<RegisterBean> teachers=new ArrayList<RegisterBean>();
		int i=0;
		RegisterDB db=new RegisterDB();
		while(i<list.size())
		{	
			RegisterBean bean=db.getscheduleTeacher(list.get(i).getTeacher_id());
			teachers.add(bean);
			i++;
		}
		
	
		return teachers;
	}

	public List<RegisterBean> getclassNames(List<Student> list) {
		RegisterDB registerDB=new RegisterDB();
		int counter=0;
		List<RegisterBean> registerBeans=new ArrayList<RegisterBean>();
		while(list.size()>counter)
		{
			RegisterBean bean=registerDB.getRegisterclass(list.get(counter).getClass_id());
			registerBeans.add(bean);
			counter++;
		}
		return registerBeans;
	}
	
	public List<RegisterBean> getTeachersclassNames(List classids) {
		RegisterDB registerDB=new RegisterDB();
		int counter=0;
		List<RegisterBean> registerBeans=new ArrayList<RegisterBean>();
		registerBeans=registerDB.getTeachersClassName(classids);
		return registerBeans;
	}
}