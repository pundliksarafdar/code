package com.service.beans;

import java.sql.Date;
import java.sql.Time;

public class ScheduleBean {
	Integer class_id;
	Integer div_id;
	Integer batch_id;
	Integer sub_id;
	Integer teacher_id;
	Time start_time;
	Time end_time;
	Date startDate;
	Date endDate;
	int schedule_id;
	Boolean isWeekly;
	Boolean monday;
	Boolean tuesday;
	Boolean wednesday;
	Boolean thursday;
	Boolean friday;
	Boolean saturday;
	Boolean sunday;
	
	public Integer getClass_id() {
		return class_id;
	}
	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}
	public Integer getDiv_id() {
		return div_id;
	}
	public void setDiv_id(Integer div_id) {
		this.div_id = div_id;
	}
	public Integer getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}
	public Integer getSub_id() {
		return sub_id;
	}
	public void setSub_id(Integer sub_id) {
		this.sub_id = sub_id;
	}
	public Integer getTeacher_id() {
		return teacher_id;
	}
	public void setTeacher_id(Integer teacher_id) {
		this.teacher_id = teacher_id;
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
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(int schedule_id) {
		this.schedule_id = schedule_id;
	}
	public Boolean getIsWeekly() {
		return isWeekly;
	}
	public void setIsWeekly(Boolean isWeekly) {
		this.isWeekly = isWeekly;
	}
	public Boolean getMonday() {
		return monday;
	}
	public void setMonday(Boolean monday) {
		this.monday = monday;
	}
	public Boolean getTuesday() {
		return tuesday;
	}
	public void setTuesday(Boolean tuesday) {
		this.tuesday = tuesday;
	}
	public Boolean getWednesday() {
		return wednesday;
	}
	public void setWednesday(Boolean wednesday) {
		this.wednesday = wednesday;
	}
	public Boolean getThursday() {
		return thursday;
	}
	public void setThursday(Boolean thursday) {
		this.thursday = thursday;
	}
	public Boolean getFriday() {
		return friday;
	}
	public void setFriday(Boolean friday) {
		this.friday = friday;
	}
	public Boolean getSaturday() {
		return saturday;
	}
	public void setSaturday(Boolean saturday) {
		this.saturday = saturday;
	}
	public Boolean getSunday() {
		return sunday;
	}
	public void setSunday(Boolean sunday) {
		this.sunday = sunday;
	}
		
}
