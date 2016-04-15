package com.service.beans;

public class QuestionExcelUploadBean {
	int sub_id;
	int div_id;
	int inst_id;
	String ques_type;
	String fileName;
	
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
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public String getQues_type() {
		return ques_type;
	}
	public void setQues_type(String ques_type) {
		this.ques_type = ques_type;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
