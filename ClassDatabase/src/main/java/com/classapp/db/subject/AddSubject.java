package com.classapp.db.subject;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.datalayer.subject.Subject;

public class AddSubject {
	public boolean addSubject(Subject subject){
		Subjects subjects = new Subjects();
		try {
			BeanUtils.copyProperties(subjects, subject);
		} catch (IllegalAccessException e) {
			System.out.println("In AddSubject IllegalAccessException......");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("In AddSubject InvocationTargetException......");
			e.printStackTrace();
		} 
		boolean status = false;
		SubjectDb subjectDb = new SubjectDb();
		String statusCode = subjectDb.updateDb(subjects);
		if(!"0".equals(statusCode)){
			status = false;
		}else{
			status = true;
		}
		return status;
	}
}
