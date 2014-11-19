package com.helper;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.classwithsubject.ClassWithSubjectsData;
import com.classapp.db.subject.Subject;

public class SubjectHelperBean {
	
	private int class_id;
	private List<Subject> subjects=new ArrayList<Subject>();
	
	public SubjectHelperBean() {		
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public List<Subject> getSubjects() {
		ClassWithSubjectsData classWithSubjectsData= new ClassWithSubjectsData();
		subjects = classWithSubjectsData.getAllSubjectsForClassID(class_id);
		/*for (Subject subject : this.subjects) {
			System.out.println("Subject name :"+subject.getSubjectName());	
			
		}*/
		return subjects;
	}
}
