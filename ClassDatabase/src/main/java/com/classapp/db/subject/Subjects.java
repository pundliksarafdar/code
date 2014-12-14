package com.classapp.db.subject;

/**
 * @author Surajprakash
 *
 */
public class Subjects {
	private Integer subjectId;
	private String subjectName;
	private Integer institute_id;

	public Integer getInstitute_id() {
		return institute_id;
	}
	public void setInstitute_id(Integer institute_id) {
		this.institute_id = institute_id;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	
}
