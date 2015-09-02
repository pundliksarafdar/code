package com.classapp.db.notificationpkg;

import java.util.Date;

public class Notification {
	int msg_sid;
	int institute_id;
	String message;
	String batch;
	Date msg_date;
	String batch_name;
	
	public String getBatch_name() {
		return batch_name;
	}
	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	public int getMsg_sid() {
		return msg_sid;
	}
	public void setMsg_sid(int msg_sid) {
		this.msg_sid = msg_sid;
	}
	public int getInstitute_id() {
		return institute_id;
	}
	public void setInstitute_id(int institute_id) {
		this.institute_id = institute_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public Date getMsg_date() {
		return msg_date;
	}
	public void setMsg_date(Date msg_date) {
		this.msg_date = msg_date;
	}
}
