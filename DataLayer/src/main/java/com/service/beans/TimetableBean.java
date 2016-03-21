package com.service.beans;

import java.util.List;


public class TimetableBean {
	/*Bean can be used as requst or response*/
	
	private int timetableId;
	private int classid;
	private int batch;
	private int subject;
	private long teacher;
	
	/*This fields are used for response */
	private String className;
	private String batchName;
	private String subjectName;
	private String teacherName;
	
	private String starttime;
	private String endtime;
	private long startDate;
	private long endDate;
	private List<Integer> occuredOn;
	
	public int getTimetableId() {
		return timetableId;
	}
	public void setTimetableId(int timetableId) {
		this.timetableId = timetableId;
	}
	public int getClassid() {
		return classid;
	}
	public void setClassid(int classid) {
		this.classid = classid;
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public long getTeacher() {
		return teacher;
	}
	public void setTeacher(long teacher) {
		this.teacher = teacher;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public List<Integer> getOccuredOn() {
		return occuredOn;
	}
	public void setOccuredOn(List<Integer> occuredOn) {
		this.occuredOn = occuredOn;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	
	
}
