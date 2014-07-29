package com.transaction.teacher;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.Teacher.TeacherDB;
import com.datalayer.teacher.Teacher;

public class TeaherTransaction {
	
	public String addTeacher(String teacherID,int regID,String subjects) {
		TeacherDB teacherDB=new TeacherDB();
		com.classapp.db.Teacher.Teacher teacherbean=new com.classapp.db.Teacher.Teacher();
		
			
			if(teacherDB.isTeacherRegistered(teacherID))
			{
				if(!teacherDB.isTeacherExists(teacherID,regID))
				{
					teacherDB.add(teacherID,regID,subjects);
					return "added";
				}else{
					return "exists";
				}
				
			}else{
		
		return "false";
	}

}
	
	public List getSubjectTeacher(String subid) {
		
		TeacherDB teacherDB=new TeacherDB();
		List list=teacherDB.getSubjectTeacher(subid);
		return list;
		
	}
}
