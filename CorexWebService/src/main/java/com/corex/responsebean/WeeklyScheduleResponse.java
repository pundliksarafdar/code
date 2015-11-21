package com.corex.responsebean;

import java.sql.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WeeklyScheduleResponse {
	String name;
	String scheduledate;
	String teacherName;
	String time;
	String subject;
	String batchName;
	public WeeklyScheduleResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public WeeklyScheduleResponse(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScheduledate() {
		return scheduledate;
	}

	public void setScheduledate(String scheduledate) {
		this.scheduledate = scheduledate;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	
}
