package com.classapp.db.institutestats;

import java.io.Serializable;

public class InstituteStats{
	int inst_id;
	int alloc_ids;
	int used_ids;
	int avail_ids;
	double alloc_memory;
	double used_memory;
	double avail_memory;
	private Integer regId;
	private boolean smsAccess;
	private Integer smsAlloted;
	private Integer smsLeft;
	private boolean emailAccess;
	private String classname;
	
	
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getAlloc_ids() {
		return alloc_ids;
	}
	public void setAlloc_ids(int alloc_ids) {
		this.alloc_ids = alloc_ids;
	}
	public int getUsed_ids() {
		return used_ids;
	}
	public void setUsed_ids(int used_ids) {
		this.used_ids = used_ids;
	}
	public int getAvail_ids() {
		return avail_ids;
	}
	public void setAvail_ids(int avail_ids) {
		this.avail_ids = avail_ids;
	}
	public double getAlloc_memory() {
		return alloc_memory;
	}
	public void setAlloc_memory(double alloc_memory) {
		this.alloc_memory = alloc_memory;
	}
	public double getUsed_memory() {
		return used_memory;
	}
	public void setUsed_memory(double used_memory) {
		this.used_memory = used_memory;
	}
	public double getAvail_memory() {
		return avail_memory;
	}
	public void setAvail_memory(double avail_memory) {
		this.avail_memory = avail_memory;
	}
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	public boolean isSmsAccess() {
		return smsAccess;
	}
	public void setSmsAccess(boolean smsAccess) {
		this.smsAccess = smsAccess;
	}
	public Integer getSmsAlloted() {
		return smsAlloted;
	}
	public void setSmsAlloted(Integer smsAlloted) {
		this.smsAlloted = smsAlloted;
	}
	public Integer getSmsLeft() {
		return smsLeft;
	}
	public void setSmsLeft(Integer smsLeft) {
		this.smsLeft = smsLeft;
	}
	public boolean isEmailAccess() {
		return emailAccess;
	}
	public void setEmailAccess(boolean emailAccess) {
		this.emailAccess = emailAccess;
	}
	
	
}
