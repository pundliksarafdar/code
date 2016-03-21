package com.service.beans;

import java.util.List;

public class DailyAttendance {
	List<DailyTimeTable> dailyTimeTableList;
	String student_name;
	int student_id;
	String[] presentee;
	int total_lectures;
	int present_lectures;
	double avg;
	public List<DailyTimeTable> getDailyTimeTableList() {
		return dailyTimeTableList;
	}
	public void setDailyTimeTableList(List<DailyTimeTable> dailyTimeTableList) {
		this.dailyTimeTableList = dailyTimeTableList;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String[] getPresentee() {
		return presentee;
	}
	public void setPresentee(String[] presentee) {
		this.presentee = presentee;
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
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	
	
}
