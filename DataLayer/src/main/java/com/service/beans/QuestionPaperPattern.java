package com.service.beans;

import java.io.Serializable;
import java.util.List;

public class QuestionPaperPattern implements Serializable {
	int pattern_id;
	String pattern_name;
	int class_id;
	int sub_id;
	int inst_id;
	int marks;
	List<QuestionPaperStructure> questionPaperStructure;
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
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public List<QuestionPaperStructure> getQuestionPaperStructure() {
		return questionPaperStructure;
	}
	public void setQuestionPaperStructure(
			List<QuestionPaperStructure> questionPaperStructure) {
		this.questionPaperStructure = questionPaperStructure;
	}
	
	
	
	
}
