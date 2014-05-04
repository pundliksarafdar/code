package com.classapp.db.subject;

import java.util.List;

public class GetSubject {

	public List<Subjects> getSubjects(int regId){
		List<Subjects> subjectList = null;
		String[] params = {"regId"};
		String[] paramsValue = {regId+""};
		SubjectDb subjectDb = new SubjectDb();
		String getAllSubjectsQuery = "from Subjects where regId =:regId";
		subjectList = subjectDb.getSubject(0,getAllSubjectsQuery, params, paramsValue);
		return subjectList;
	}
	
	public boolean isSubjectExists(int regId,String subject){
		boolean isSubjectExist =false;
		List<Subjects> subjectsList = getSubjects(regId);
		
		for (int i = 0; i < subjectsList.size(); i++) {
			Subjects subjects = subjectsList.get(i);
			if(subjects.getSubjectName().equalsIgnoreCase(subject)){
				isSubjectExist = true;
				break;
			}else{
				isSubjectExist = false;
			}
		}
		return isSubjectExist;
	}
}
