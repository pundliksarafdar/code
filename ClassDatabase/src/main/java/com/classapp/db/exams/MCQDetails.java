package com.classapp.db.exams;

import java.util.Date;

import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.subject.Subject;

public class MCQDetails {
	private Subject subject;
	private Division division;
	private RegisterBean teacher;
	private int exam_id;
	private int class_id;
	private Date creationDate;
	private String upload_path;
	
	public String getUpload_path() {
		return upload_path;
	}
	public void setUpload_path(String upload_path) {
		this.upload_path = upload_path;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	public RegisterBean getTeacher() {
		return teacher;
	}
	public void setTeacher(RegisterBean teacher) {
		this.teacher = teacher;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
