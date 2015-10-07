package com.classapp.db.subject;

import java.util.List;

import com.datalayer.subject.Subject;

public class SubjectTest {

	private SubjectTest(){
		
	}
	
	public static void main(String[] args) {
			/*AddSubject addSubject = new AddSubject();
			Subject subject = new Subject();
			subject.setRegId(34);
			subject.setSubjectName("SubjectName");
			addSubject.addSubject(subject);
			*/
		GetSubject getSubject = new GetSubject();
		/*List<Subject> subjects = getSubject.getSubjects(34); 
		for(int i=0;i<subjects.size();i++){
			AppLogger.logger(subjects.get(i).getSubjectName());
		}
		
		AppLogger.logger(getSubject.isSubjectExists(34, "subject11"));*/
	}
}
