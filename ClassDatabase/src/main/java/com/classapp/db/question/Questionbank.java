package com.classapp.db.question;

import java.io.Serializable;
import java.sql.Date;

public class Questionbank implements Serializable {

	private int inst_id;
	private int div_id; 
	private int sub_id; 
	private int que_id; 
	private int added_by; 
	private int modified_by; 
	private Date created_dt; 
	private Date modified_dt; 
	private int rep; 
	private int marks;
	private String ans_id;
	private String exam_rep;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getQue_id() {
		return que_id;
	}
	public void setQue_id(int que_id) {
		this.que_id = que_id;
	}
	public int getAdded_by() {
		return added_by;
	}
	public void setAdded_by(int added_by) {
		this.added_by = added_by;
	}
	public int getModified_by() {
		return modified_by;
	}
	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}
	public Date getCreated_dt() {
		return created_dt;
	}
	public void setCreated_dt(Date created_dt) {
		this.created_dt = created_dt;
	}
	public Date getModified_dt() {
		return modified_dt;
	}
	public void setModified_dt(Date modified_dt) {
		this.modified_dt = modified_dt;
	}
	public int getRep() {
		return rep;
	}
	public void setRep(int rep) {
		this.rep = rep;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getAns_id() {
		return ans_id;
	}
	public void setAns_id(String ans_id) {
		this.ans_id = ans_id;
	}
	public String getExam_rep() {
		return exam_rep;
	}
	public void setExam_rep(String exam_rep) {
		this.exam_rep = exam_rep;
	}
	
}
