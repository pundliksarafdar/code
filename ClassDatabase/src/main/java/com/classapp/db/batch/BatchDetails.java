package com.classapp.db.batch;

import java.util.List;

import com.classapp.db.batch.division.Division;
import com.classapp.db.subject.Subject;

public class BatchDetails {
	private Batch batch;
	private List<Subject> subjects;
	private Division division;
	
	public List<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	
	public String getSubjectNames(){
		String subjectNames="";
		
			for (Subject subject : this.subjects) {
				if(subjectNames.equals("")){
					subjectNames=subject.getSubjectName();
				}else{
					subjectNames=subjectNames+","+subject.getSubjectName();
				}
			}
			
			return subjectNames;		
	}
	
	public String getSubjectIds(){
		String subjectIds="";
		
			for (Subject subject : this.subjects) {
				if(subjectIds.equals("")){
					subjectIds=subject.getSubjectId()+"";
				}else{
					subjectIds=subjectIds+","+subject.getSubjectId();
				}
			}
			
			return subjectIds;		
	}
}
