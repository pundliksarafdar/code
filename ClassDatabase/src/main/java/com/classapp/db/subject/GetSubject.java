package com.classapp.db.subject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.datalayer.subject.Subject;

public class GetSubject {

	public List<Subject> getSubjects(int regId){
		List<Subjects> subjectList = null;
		List<Subject> subjects = new ArrayList<Subject>();
		String[] params = {"regId"};
		String[] paramsValue = {regId+""};
		SubjectDb subjectDb = new SubjectDb();
		String getAllSubjectsQuery = "from Subjects where regId =:regId";
		subjectList = subjectDb.getSubject(0,getAllSubjectsQuery, params, paramsValue);
		try {
			for(int i=0;i<subjectList.size();i++){
				Subject subject = new Subject();
				BeanUtils.copyProperties(subject, subjectList.get(i));
				subjects.add(subject);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return subjects;
	}
	
	public boolean isSubjectExists(int regId,String subject){
		boolean isSubjectExist =false;
		List<Subject> subjectsList = getSubjects(regId);
		
		for (int i = 0; i < subjectsList.size(); i++) {
			Subject subjects = subjectsList.get(i);
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
