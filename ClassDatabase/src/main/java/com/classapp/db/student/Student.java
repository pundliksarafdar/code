package com.classapp.db.student;

import java.io.Serializable;

public class Student implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int student_id;
	private int class_id;
	private int div_id;
	private String batch_id;
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
