package com.service.beans;

import java.util.List;

public class StudentProgressCard {
	private int student_id;
	private String student_name;
	private int roll_no;
	private String class_name;
	private String batch_name;
	private String inst_name;
	List<String[]> dataList;
	List<String> subject_name;
	List<String[]> examSMSDATA;
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public int getRoll_no() {
		return roll_no;
	}
	public void setRoll_no(int roll_no) {
		this.roll_no = roll_no;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getBatch_name() {
		return batch_name;
	}
	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	public String getInst_name() {
		return inst_name;
	}
	public void setInst_name(String inst_name) {
		this.inst_name = inst_name;
	}
	public List<String[]> getDataList() {
		return dataList;
	}
	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}
	public List<String> getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(List<String> subject_name) {
		this.subject_name = subject_name;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public List<String[]> getExamSMSDATA() {
		return examSMSDATA;
	}
	public void setExamSMSDATA(List<String[]> examSMSDATA) {
		this.examSMSDATA = examSMSDATA;
	}
	
	
}
