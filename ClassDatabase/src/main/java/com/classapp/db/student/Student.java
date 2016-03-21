package com.classapp.db.student;

import java.io.Serializable;
import java.util.Map;

public class Student implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int student_id;
	private int class_id;
	private int div_id;
	private String batch_id;
	private String parentFname;
	private String parentLname;
	private String parentPhone;
	private String parentEmail;
	private String batchIdNRoll;
	
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getParentFname() {
		return parentFname;
	}
	public void setParentFname(String parentFname) {
		this.parentFname = parentFname;
	}
	public String getParentLname() {
		return parentLname;
	}
	public void setParentLname(String parentLname) {
		this.parentLname = parentLname;
	}
	public String getParentPhone() {
		return parentPhone;
	}
	public void setParentPhone(String parentPhone) {
		this.parentPhone = parentPhone;
	}
	public String getParentEmail() {
		return parentEmail;
	}
	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}
	public String getBatchIdNRoll() {
		return batchIdNRoll;
	}
	public void setBatchIdNRoll(String batchIdNRoll) {
		this.batchIdNRoll = batchIdNRoll;
	}
	@Override
	public boolean equals(Object arg0) {
		Student student1= (Student)arg0;
		if(student1.getDiv_id()!=this.getDiv_id()){
			return false;
		}else if(student1.getBatch_id()!=null){
			String[] batchesStudent1=student1.getBatch_id().split(",");
			String[] batchesCurrentStudent=this.getBatch_id().split(",");
			
			if(batchesStudent1.length!=batchesCurrentStudent.length){
				return false;
			}
			String currentBatchIds=this.getBatch_id();
			for (String batchId : batchesStudent1) {
				if(!currentBatchIds.contains(batchId)){
					return false;
				}
			}
		}
		
		return true;
	}
}
