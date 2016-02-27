package com.classapp.db.exam;

import java.sql.Date;

public class Exam_Paper {
	private int exam_paper_id;
	private int inst_id;
	private int div_id;
	private int exam_id;
	private int sub_id;
	private int marks;
	private int created_by;
	private int modified_by;
	private Date created_dt;
	private Date modified_dt;
	private String duration;
	private int question_paper_id;
	private String header_id;
	public int getExam_paper_id() {
		return exam_paper_id;
	}
	public void setExam_paper_id(int exam_paper_id) {
		this.exam_paper_id = exam_paper_id;
	}
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
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
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
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getQuestion_paper_id() {
		return question_paper_id;
	}
	public void setQuestion_paper_id(int question_paper_id) {
		this.question_paper_id = question_paper_id;
	}
	public String getHeader_id() {
		return header_id;
	}
	public void setHeader_id(String header_id) {
		this.header_id = header_id;
	}
	
	
}
