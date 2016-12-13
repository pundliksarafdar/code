package com.mobile.bean;

import java.util.Date;

public class SyllabusBean {
	Integer id;
    Date date;
    private Integer teacherId;
    private String teacherName;
    private Integer subjectId;
    private String subjectName;
    private Integer divisionId;
    private String divisionName;
    private String Syllabus;
    private String teacherRemark;
    private String status;
    
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getSyllabus() {
		return Syllabus;
	}
	public void setSyllabus(String syllabus) {
		Syllabus = syllabus;
	}
	public String getTeacherRemark() {
		return teacherRemark;
	}
	public void setTeacherRemark(String teacherRemark) {
		this.teacherRemark = teacherRemark;
	}
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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
	public Integer getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}
    
    
}
