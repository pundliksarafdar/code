package com.service.beans;

import java.sql.Time;

public class AttendanceScheduleServiceBean {
	String teacher;
	String sub_name;
	int sub_id;
	int schedule_id;
	Time start_time;
	Time end_time;
	boolean attendanceStatus;
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(int schedule_id) {
		this.schedule_id = schedule_id;
	}
	public Time getStart_time() {
		return start_time;
	}
	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}
	public Time getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}
	public boolean isAttendanceStatus() {
		return attendanceStatus;
	}
	public void setAttendanceStatus(boolean attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}
	
	
}
