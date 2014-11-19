package com.classapp.db.classwithsubject;

import java.util.List;
import com.classapp.db.subject.Subject;

public class ClassWithSubjectsData {
	ClassWithSubjectsDB classWithSubjectsDB;
	
	public ClassWithSubjectsData() {
		classWithSubjectsDB=new ClassWithSubjectsDB();
	}
	public void addOrUpdateClassWithSubjects(ClassWithSubjects classWithSubjects){
		 classWithSubjectsDB.updateDb(classWithSubjects);		
	}
	
	public List<Subject> getAllSubjectsForClassID(int classId){
		List<Subject> listOfSubject= classWithSubjectsDB.getSubjects(classId);
		
		if(listOfSubject.size()!=0 && !listOfSubject.isEmpty()){
			System.out.println("size of Subject list is : "+listOfSubject.size());
			return listOfSubject;
		}
		return null;
	}
	
	public List<Subject> getAllSubjectsForClassID(List classId){
		List<Subject> listOfSubject= classWithSubjectsDB.getSubjects(classId);
		
		if(listOfSubject.size()!=0 && !listOfSubject.isEmpty()){
			System.out.println("size of Subject list is : "+listOfSubject.size());
			return listOfSubject;
		}
		return null;
	}
}
