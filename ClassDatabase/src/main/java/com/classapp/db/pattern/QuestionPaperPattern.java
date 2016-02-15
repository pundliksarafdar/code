package com.classapp.db.pattern;

import java.io.Serializable;

public class QuestionPaperPattern implements Serializable{
	int inst_id;
	int div_id;
	int sub_id;
	int pattern_id;
	String pattern_name;
	int marks;
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
	public int getPattern_id() {
		return pattern_id;
	}
	public void setPattern_id(int pattern_id) {
		this.pattern_id = pattern_id;
	}
	public String getPattern_name() {
		return pattern_name;
	}
	public void setPattern_name(String pattern_name) {
		this.pattern_name = pattern_name;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	
	
}
