package com.helper;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.subject.Subject;
import com.classapp.db.subject.SubjectDb;

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
		SubjectDb db=new SubjectDb();
		subjects = db.retrivesublist(class_id);
		/*for (Subject subject : this.subjects) {
			System.out.println("Subject name :"+subject.getSubjectName());	
			
		}*/
		return subjects;
	}
}
