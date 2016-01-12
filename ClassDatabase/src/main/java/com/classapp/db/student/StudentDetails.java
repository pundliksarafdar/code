package com.classapp.db.student;

import java.util.List;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;

public class StudentDetails {
	private RegisterBean studentUserBean;
	private int studentId;
	private String batcheIds;
	private Division division;
	private List<Batch> batches;	
	private int divID;
	private Student student;
	public int getDivID() {
		return divID;
	}
	public void setDivID(int divID) {
		this.divID = divID;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	public String getBatcheIds() {
		return batcheIds;
	}
	public void setBatcheIds(String batcheIds) {
		this.batcheIds = batcheIds;
	}
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	public RegisterBean getStudentUserBean() {
		return studentUserBean;
	}
	public void setStudentUserBean(RegisterBean studentUserBean) {
		this.studentUserBean = studentUserBean;
	}
	public List<Batch> getBatches() {
		return batches;
	}
	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
}
