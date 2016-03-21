package com.classapp.schedule;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Schedule implements Serializable {
int class_id;
int div_id;
int batch_id;
int sub_id;
int teacher_id;
Time start_time;
Time end_time;
Date date;
int schedule_id;

public int getSchedule_id() {
	return schedule_id;
}
public void setSchedule_id(int schedule_id) {
	this.schedule_id = schedule_id;
}
public int getClass_id() {
	return class_id;
}
public void setClass_id(int class_id) {
	this.class_id = class_id;
}
public int getDiv_id() {
	return div_id;
}
public void setDiv_id(int div_id) {
	this.div_id = div_id;
}
public int getBatch_id() {
	return batch_id;
}
public void setBatch_id(int batch_id) {
	this.batch_id = batch_id;
}
public int getSub_id() {
	return sub_id;
}
public void setSub_id(int sub_id) {
	this.sub_id = sub_id;
}
public int getTeacher_id() {
	return teacher_id;
}
public void setTeacher_id(int teacher_id) {
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
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
}
