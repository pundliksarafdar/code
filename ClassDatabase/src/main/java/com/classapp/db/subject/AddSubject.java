package com.classapp.db.subject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.subject.Subject;

public class AddSubject {
	public boolean addSubject(Subject subject){
		Subject subjects =new Subject();
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

	public boolean addSubject(Subject subject,int regID){
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
		SubjectDb subjectDb=new SubjectDb();
		String statusCode = subjectDb.updateDb(subjects,regID);
		if(!"0".equals(statusCode)){
			status = false;
		}else{
			status = true;
		}
		return status;
	}
	
	public boolean addSubjecttoclass(Subject subject,int classid){
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
		SubjectDb subjectDb=new SubjectDb();
		GetSubject getSubject=new GetSubject();
		List<Subjects> list=getSubject.getSubjectid(subject.getSubjectName());
		ClassSubjects classSubjects=new ClassSubjects();
		classSubjects.setClass_id(classid);
		classSubjects.setSub_id(list.get(0).getSubjectId());
		String statusCode = subjectDb.updateclasssubDb(classSubjects);
		if(!"0".equals(statusCode)){
			status = false;
		}else{
			status = true;
		}
		return status;
	}
	
}
