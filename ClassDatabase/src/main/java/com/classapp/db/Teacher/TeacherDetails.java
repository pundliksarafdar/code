package com.classapp.db.Teacher;

import java.util.Iterator;
import java.util.List;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.subject.Subject;

public class TeacherDetails {
	private RegisterBean teacherBean;
	private List<Subject> subjects;
	private int teacherId;
	private String subjectIds;
	private String suffix;
	
	
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public RegisterBean getTeacherBean() {
		return teacherBean;
	}
	public void setTeacherBean(RegisterBean teacherBean) {
		this.teacherBean = teacherBean;
	}
	public List<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	
	public String getSubjectNames(){
	 	String subjectNames="";
	 	if(this.subjects.size()!=0){
	 	if(this.subjects.size()==1){
	 	subjectNames=this.subjects.get(0).getSubjectName(); 
	 	}else{	 
	 	Iterator subjectList=this.subjects.iterator();
	 	Subject subject=(Subject)subjectList.next();
	 	subjectNames=subject.getSubjectName();
	 	while(subjectList.hasNext()){
	 	subject=(Subject)subjectList.next();
	 	subjectNames=subjectNames+", "+subject.getSubjectName();
	 	}
	 	}
	 	}
	  /* Iterator subjectList=this.subjects.iterator();
	  while(subjectList.hasNext()){
	  Subject subject=(Subject)subjectList.next();
	  subjectNames=subjectNames+", "+subject.getSubjectName();
	  }*/
	  return subjectNames;
	 }

}
