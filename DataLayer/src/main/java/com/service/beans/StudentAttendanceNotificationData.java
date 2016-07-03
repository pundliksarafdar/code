package com.service.beans;

import java.sql.Date;

public class StudentAttendanceNotificationData {
	int student_id;
	String student_name;
	String parent_name;
	String inst_name;
	int total_lectures;
	int present_lectures;
	double average;
	Date att_date;
	Date start_date;
	Date end_date;
	String batch_name;
	String month;
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
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getInst_name() {
		return inst_name;
	}
	public void setInst_name(String inst_name) {
		this.inst_name = inst_name;
	}
	public String getBatch_name() {
		return batch_name;
	}
	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	
}
