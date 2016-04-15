package com.service.beans;

import java.util.List;

public class OnlineExamPaper {
	int paper_id;
	int class_id;
	int pattern_id;
	String paper_description;
	int inst_id;
	int marks;
	List<OnlineExamPaperElement> onlineExamPaperElementList;
	public int getPaper_id() {
		return paper_id;
	}
	public void setPaper_id(int paper_id) {
		this.paper_id = paper_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getPattern_id() {
		return pattern_id;
	}
	public void setPattern_id(int pattern_id) {
		this.pattern_id = pattern_id;
	}
	public String getPaper_description() {
		return paper_description;
	}
	public void setPaper_description(String paper_description) {
		this.paper_description = paper_description;
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
	public List<OnlineExamPaperElement> getOnlineExamPaperElementList() {
		return onlineExamPaperElementList;
	}
	public void setOnlineExamPaperElementList(List<OnlineExamPaperElement> onlineExamPaperElementList) {
		this.onlineExamPaperElementList = onlineExamPaperElementList;
	}
	
}
