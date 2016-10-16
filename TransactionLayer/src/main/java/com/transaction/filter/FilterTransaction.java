package com.transaction.filter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDB;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.batch.division.Division;
import com.classapp.db.filter.FilterDb;
import com.classapp.db.subject.Subject;

public class FilterTransaction {
	public HashMap<String, List> getFilteredResult(HashMap<String, List<Integer>> filterParamMap,int instId){
		HashMap<String, List> hashMap = new HashMap<String, List>();
		FilterDb filterDb = new FilterDb();
		
		/*****************For subject************************/
		List<String>paramList = new ArrayList<String>();
		paramList.add("subjectId");
		List<Subject> subjects = filterDb.getFilteredResult(filterParamMap, instId, new Subject(),paramList,"institute_id");
		List<com.datalayer.subject.Subject> subjectsDatalayer = new ArrayList<com.datalayer.subject.Subject>();
		
		for(Subject subject:subjects){
			com.datalayer.subject.Subject destSubject = new com.datalayer.subject.Subject();
			try {
				BeanUtils.copyProperties(destSubject, subject);
				subjectsDatalayer.add(destSubject);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hashMap.put("subjects", subjectsDatalayer);
		
		/*****************For subject************************/
		paramList.clear();
		paramList.add("divId");
		List<Division> divisions = filterDb.getFilteredResult(filterParamMap, instId, new Division(),paramList,"institute_id");
		List<com.service.beans.Division> divisonDatalayer = new ArrayList<com.service.beans.Division>();
		
		for(Division division:divisions){
			com.service.beans.Division destDivision = new com.service.beans.Division();
			try {
				BeanUtils.copyProperties(destDivision, division);
				divisonDatalayer.add(destDivision);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hashMap.put("division", divisonDatalayer);
		
		/*****************For teacher************************/
		/*
		paramList.clear();
		paramList.add("user_id");
		List<Teacher> teachers = filterDb.getFilteredResult(filterParamMap, instId, new Teacher(),paramList,"class_id");
		List<com.datalayer.teacher.Teacher> teacherDatalayer = new ArrayList<com.datalayer.teacher.Teacher>();
		
		for(Teacher teacher:teachers){
			com.datalayer.teacher.Teacher destTeacher = new com.datalayer.teacher.Teacher();
			try {
				BeanUtils.copyProperties(destTeacher, teacher);
				teacherDatalayer.add(destTeacher);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		TeacherDB teacherDB = new TeacherDB(); 
		List<TeacherDetails> teacherDetails = teacherDB.getAllTeachersFromClass(instId);
		hashMap.put("teacher", teacherDetails);
		
		return hashMap;
	}
}
