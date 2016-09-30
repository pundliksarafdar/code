package com.classapp.db.roll;

import java.io.Serializable;

public class Inst_roll implements Serializable {
	int inst_id;
	int roll_id;
	String roll_desc;
	String parent_mod_access;
	String child_mod_access;
	boolean teacher;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getRoll_id() {
		return roll_id;
	}
	public void setRoll_id(int roll_id) {
		this.roll_id = roll_id;
	}
	public String getRoll_desc() {
		return roll_desc;
	}
	public void setRoll_desc(String roll_desc) {
		this.roll_desc = roll_desc;
	}
	public String getParent_mod_access() {
		return parent_mod_access;
	}
	public void setParent_mod_access(String parent_mod_access) {
		this.parent_mod_access = parent_mod_access;
	}
	public String getChild_mod_access() {
		return child_mod_access;
	}
	public void setChild_mod_access(String child_mod_access) {
		this.child_mod_access = child_mod_access;
	}
	public boolean isTeacher() {
		return teacher;
	}
	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}
	
}
