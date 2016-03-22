package com.classapp.db.exam;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Exam implements Serializable{
	private int exam_id;
	private int inst_id;
	private String exam_name;
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public String getExam_name() {
		return exam_name;
	}
	public void setExam_name(String exam_name) {
		this.exam_name = exam_name;
	}
	
	

}
