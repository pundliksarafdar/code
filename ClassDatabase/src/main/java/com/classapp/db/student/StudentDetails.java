package com.classapp.db.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.service.beans.ExamWiseStudentDetails;
import com.service.beans.ProgressCardServiceBean;
import com.service.beans.StudentDetailAttendanceData;
import com.service.beans.StudentFeesServiceBean;

public class StudentDetails {
	private RegisterBean studentUserBean;
	private int studentId;
	private String batcheIds;
	private Division division;
	private List<Batch> batches;	
	private int divID;
	private Student student;
	private int rollNo;
	private Map<Integer, Integer>batchIdNRoll;
	ExamWiseStudentDetails examWiseStudentDetails;
	StudentFeesServiceBean feesServiceBean;
	StudentDetailAttendanceData attendanceData;  
	
	public int getRollNo() {
		return rollNo;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
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
	public Map<Integer, Integer> getBatchIdNRoll() {
		return batchIdNRoll;
	}
	public void setBatchIdNRoll(Map<Integer, Integer> batchIdNRoll) {
		this.batchIdNRoll = batchIdNRoll;
	}
	public ExamWiseStudentDetails getExamWiseStudentDetails() {
		return examWiseStudentDetails;
	}
	public void setExamWiseStudentDetails(ExamWiseStudentDetails examWiseStudentDetails) {
		this.examWiseStudentDetails = examWiseStudentDetails;
	}
	public StudentDetailAttendanceData getAttendanceData() {
		return attendanceData;
	}
	public void setAttendanceData(StudentDetailAttendanceData attendanceData) {
		this.attendanceData = attendanceData;
	}
	public StudentFeesServiceBean getFeesServiceBean() {
		return feesServiceBean;
	}
	public void setFeesServiceBean(StudentFeesServiceBean feesServiceBean) {
		this.feesServiceBean = feesServiceBean;
	}
	
	
	
}
