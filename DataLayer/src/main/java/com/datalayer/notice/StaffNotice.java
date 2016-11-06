package com.datalayer.notice;

import java.io.Serializable;
import java.sql.Date;

public class StaffNotice implements Serializable {
	private int inst_id;
	private int notice_id;
	private String role;
	private String notice;
	private Date start_date;
	private Date end_date;
	private String role_Desc;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
	public String getRole_Desc() {
		return role_Desc;
	}
	public void setRole_Desc(String role_Desc) {
		this.role_Desc = role_Desc;
	}
	
	
}
