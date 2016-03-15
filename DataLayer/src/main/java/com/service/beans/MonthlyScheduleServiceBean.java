package com.service.beans;

import java.sql.Date;

public class MonthlyScheduleServiceBean {
	int id;
	String description;
	String title;
	int divId;
	String divname;
	int subId;
	String subjectname;
	int batchId;
	String batchName;
	long start;
	long end;
	String teacher;
	int teacher_id;
	int grp_id;
	String rep_days;
	Date start_date;
	Date end_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	public String getDivname() {
		return divname;
	}
	public void setDivname(String divname) {
		this.divname = divname;
	}
	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
	public String getSubjectname() {
		return subjectname;
	}
	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public int getTeacher_id() {
		return teacher_id;
	}
	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}
	public int getGrp_id() {
		return grp_id;
	}
	public void setGrp_id(int grp_id) {
		this.grp_id = grp_id;
	}
	public String getRep_days() {
		return rep_days;
	}
	public void setRep_days(String rep_days) {
		this.rep_days = rep_days;
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
	
	
}
