package com.classapp.schedule;

import java.sql.Time;

public class Scheduledata {
int inst_id;	
String inst_name;
String batch_name;
Time start_time;
Time end_time;
String subject_name;
String division_name;
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
public int getInst_id() {
	return inst_id;
}
public void setInst_id(int inst_id) {
	this.inst_id = inst_id;
}
public String getSubject_name() {
	return subject_name;
}
public void setSubject_name(String subject_name) {
	this.subject_name = subject_name;
}
public String getDivision_name() {
	return division_name;
}
public void setDivision_name(String division_name) {
	this.division_name = division_name;
}



}
