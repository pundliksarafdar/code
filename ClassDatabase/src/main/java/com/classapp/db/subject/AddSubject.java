package com.classapp.db.subject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.subject.Subject;
import com.classapp.logger.AppLogger;

public class AddSubject {
	public boolean addSubject(Subject subject){
		Subject subjects =new Subject();
		try {
			BeanUtils.copyProperties(subjects, subject);
		} catch (IllegalAccessException e) {
			AppLogger.logger("In AddSubject IllegalAccessException......");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			AppLogger.logger("In AddSubject InvocationTargetException......");
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

	public boolean addSubject(Subject subject,int regID){
		Subjects subjects = new Subjects();
		try {
			BeanUtils.copyProperties(subjects, subject);
		} catch (IllegalAccessException e) {
			AppLogger.logger("In AddSubject IllegalAccessException......");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			AppLogger.logger("In AddSubject InvocationTargetException......");
			e.printStackTrace();
		} 
		boolean status = false;
		SubjectDb subjectDb=new SubjectDb();
		String statusCode = subjectDb.updateDb(subjects,regID);
		if(!"0".equals(statusCode)){
			status = false;
		}else{
			status = true;
		}
		return status;
	}
	
}
