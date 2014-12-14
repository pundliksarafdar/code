package com.classapp.db.subject;

public class Subject {
	private Integer subjectId;
	private String subjectName;
	private Integer institute_id;
	
	public String getSubjectName() {
		return subjectName;
	}
	public Integer getInstitute_id() {
		return institute_id;
	}
	public void setInstitute_id(Integer institute_id) {
		this.institute_id = institute_id;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	
	@Override
	public boolean equals(Object obj) {
		Subject subject=(Subject)obj;
		if(subject.getSubjectId()==this.subjectId && subject.getSubjectName().equals(this.subjectName)){
			return true;
		}
		return false;
	}
	
}
