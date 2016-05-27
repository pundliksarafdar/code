package com.service.beans;

import java.sql.Date;

public class StudentAttendanceNotificationData {
	int student_id;
	String student_name;
	String parent_name;
	int total_lectures;
	int present_lectures;
	double average;
	Date att_date;
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public int getTotal_lectures() {
		return total_lectures;
	}
	public void setTotal_lectures(int total_lectures) {
		this.total_lectures = total_lectures;
	}
	public int getPresent_lectures() {
		return present_lectures;
	}
	public void setPresent_lectures(int present_lectures) {
		this.present_lectures = present_lectures;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public Date getAtt_date() {
		return att_date;
	}
	public void setAtt_date(Date att_date) {
		this.att_date = att_date;
	}
	
	
}
