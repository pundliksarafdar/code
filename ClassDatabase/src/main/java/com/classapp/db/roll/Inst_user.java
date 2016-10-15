package com.classapp.db.roll;

import java.io.Serializable;
import java.sql.Date;

public class Inst_user implements Serializable {
	int inst_id;
	int user_id;
	int role_id;
	String education;
	Date joining_date;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public Date getJoining_date() {
		return joining_date;
	}
	public void setJoining_date(Date joining_date) {
		this.joining_date = joining_date;
	}
	
}
