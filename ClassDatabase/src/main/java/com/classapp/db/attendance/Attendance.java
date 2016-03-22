package com.classapp.db.attendance;

import java.sql.Date;

public class Attendance {
	int inst_id;
	int att_id;
	Date att_date;
	int div_id;
	int batch_id;
	int sub_id;
	int schedule_id;
	int student_id;
	String presentee;
	String note;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getAtt_id() {
		return att_id;
	}
	public void setAtt_id(int att_id) {
		this.att_id = att_id;
	}
	public Date getAtt_date() {
		return att_date;
	}
	public void setAtt_date(Date att_date) {
		this.att_date = att_date;
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
	public int getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(int schedule_id) {
		this.schedule_id = schedule_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getPresentee() {
		return presentee;
	}
	public void setPresentee(String presentee) {
		this.presentee = presentee;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
