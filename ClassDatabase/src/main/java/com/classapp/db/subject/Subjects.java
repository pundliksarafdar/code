package com.classapp.db.subject;

/**
 * @author Surajprakash
 *
 */
public class Subjects {
	private Integer subjectId;
	private String subjectName;
	private Integer institute_id;
	private String sub_type;
	private String com_subjects;
	
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
	public String getSub_type() {
		return sub_type;
	}
	public void setSub_type(String sub_type) {
		this.sub_type = sub_type;
	}
	public String getCom_subjects() {
		return com_subjects;
	}
	public void setCom_subjects(String com_subjects) {
		this.com_subjects = com_subjects;
	}

	
}
