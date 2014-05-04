package com.tranaction.subject;

import com.classapp.db.subject.AddSubject;
import com.classapp.db.subject.GetSubject;
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
	
}
