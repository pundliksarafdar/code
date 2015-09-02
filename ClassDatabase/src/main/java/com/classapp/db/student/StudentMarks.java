package com.classapp.db.student;

import java.io.Serializable;

public class StudentMarks implements Serializable {
	private int student_id;
	private int inst_id;
	private int sub_id;
	private int div_id;
	private int exam_id;
	private int marks;
	private String ans_ids;
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getAns_ids() {
		return ans_ids;
	}
	public void setAns_ids(String ans_ids) {
		this.ans_ids = ans_ids;
	}
}
