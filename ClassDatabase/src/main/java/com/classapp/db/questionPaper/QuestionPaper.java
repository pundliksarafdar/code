package com.classapp.db.questionPaper;

import java.sql.Date;

public class QuestionPaper {
	int inst_id;
	int div_id;
	int paper_id;
	String paper_description;
	Date created_dt;
	Date modified_dt;
	int created_by;
	int modified_by;
	int marks;
	int sub_id;
	String compSubjectIds;
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
	public int getPaper_id() {
		return paper_id;
	}
	public void setPaper_id(int paper_id) {
		this.paper_id = paper_id;
	}
	public String getPaper_description() {
		return paper_description;
	}
	public void setPaper_description(String paper_description) {
		this.paper_description = paper_description;
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
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public String getCompSubjectIds() {
		return compSubjectIds;
	}
	public void setCompSubjectIds(String compSubjectIds) {
		this.compSubjectIds = compSubjectIds;
	}
	
	
}
