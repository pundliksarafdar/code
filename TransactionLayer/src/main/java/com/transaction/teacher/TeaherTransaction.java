package com.transaction.teacher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Teacher.TeacherDB;
import com.classapp.db.register.RegisterBean;
import com.datalayer.teacher.Teacher;
import com.transaction.register.RegisterTransaction;

public class TeaherTransaction {
	
	public String addTeacher(String teacherID,int regID,String subjects,String suffix) {
		TeacherDB teacherDB=new TeacherDB();
		com.classapp.db.Teacher.Teacher teacherbean=new com.classapp.db.Teacher.Teacher();
		
			
			if(teacherDB.isTeacherRegistered(teacherID))
			{
				if(!teacherDB.isTeacherExists(teacherID,regID))
				{
					teacherDB.add(teacherID,regID,subjects,suffix);
					return "added";
				}else{
					return "exists";
				}
				
			}else{
		
		return "false";
	}

}
	
	public List getSubjectTeacher(String subid,int regId) {
		
		TeacherDB teacherDB=new TeacherDB();
		List<com.classapp.db.Teacher.Teacher> list=teacherDB.getSubjectTeacher(subid,regId);
		return list;
		
	}
	
	public List<List<RegisterBean>> getScheduleTeacher(List<Schedule> Schedulelist,int regId) {
		TeacherDB teacherDB=new TeacherDB();
		int counter=0;
		List<List<RegisterBean>> registerBeans=new ArrayList<List<RegisterBean>>();
		while(counter<Schedulelist.size())
		{
		List<com.classapp.db.Teacher.Teacher> list=teacherDB.getSubjectTeacher(Schedulelist.get(counter).getSub_id()+"",regId);
		List teacherids=new ArrayList();
		int index=0;
		while (list.size()>index) {
			
			teacherids.add(list.get(index).getUser_id());
			index++;	
		}
		RegisterBean bean=new RegisterBean();
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> teachers=registerTransaction.getTeacherName(teacherids);
		registerBeans.add(teachers);
		counter++;
		}
		return registerBeans;
	}
	
	public List<List<String>> getScheduleTeacherSuffix(List<Schedule> Schedulelist,int regId) {
		TeacherDB teacherDB=new TeacherDB();
		int counter=0;
		List<List<String>> teachers=new ArrayList<List<String>>();
		while(counter<Schedulelist.size())
		{
		List<com.classapp.db.Teacher.Teacher> list=teacherDB.getSubjectTeacher(Schedulelist.get(counter).getSub_id()+"",regId);
		List teachersuffix=new ArrayList();
		int index=0;
		while (list.size()>index) {
			
			teachersuffix.add(list.get(index).getSuffix());
			index++;	
		}
		teachers.add(teachersuffix);
		counter++;
		}
		return teachers;
	}
	
}
