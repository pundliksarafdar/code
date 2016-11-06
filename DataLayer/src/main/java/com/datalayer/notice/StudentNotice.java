package com.datalayer.notice;

import java.io.Serializable;
import java.sql.Date;

public class StudentNotice implements Serializable{
	private int inst_id;
	private int notice_id;
	private int div_id;
	private String batch_id;
	private String notice;
	private Date start_date;
	private Date end_date;
	private String div_name;
	private String batch_names;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(int notice_id) {
		this.notice_id = notice_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
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
	public String getDiv_name() {
		return div_name;
	}
	public void setDiv_name(String div_name) {
		this.div_name = div_name;
	}
	public String getBatch_names() {
		return batch_names;
	}
	public void setBatch_names(String batch_names) {
		this.batch_names = batch_names;
	}
	
}
