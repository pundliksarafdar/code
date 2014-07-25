package com.transaction.teacher;

import java.lang.reflect.InvocationTargetException;

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
}
