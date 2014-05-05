package com.tranaction.subject;

import java.util.List;

import com.classapp.db.subject.AddSubject;
import com.classapp.db.subject.GetSubject;
import com.classapp.db.subject.Subjects;
import com.datalayer.subject.Subject;

public class SubjectTransaction {
	public boolean addUpdateSubjectToDb(Subject subject){
		boolean status = false;
		AddSubject addSubject = new AddSubject();
		GetSubject getSubject = new GetSubject();
		if(!getSubject.isSubjectExists(subject.getRegId(), subject.getSubjectName())){
			status = addSubject.addSubject(subject);
		}else{
			status = false;
		}
		return status;
	}
	
	public List<Subject> getAllSubjects(int regId){
		GetSubject getSubject = new GetSubject();
		List<Subject> allSubject = getSubject.getSubjects(regId);
		return allSubject;
	}
}
