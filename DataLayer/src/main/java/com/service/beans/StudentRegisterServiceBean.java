package com.service.beans;

import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonObject;

public class StudentRegisterServiceBean {
	RegisterBean registerBean;
	List<Student_Fees> student_FeesList;
	Student student;
	HashMap<String, String>additionalFormFields;
	
	public RegisterBean getRegisterBean() {
		return registerBean;
	}
	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}
	public List<Student_Fees> getStudent_FeesList() {
		return student_FeesList;
	}
	public void setStudent_FeesList(List<Student_Fees> student_FeesList) {
		this.student_FeesList = student_FeesList;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public HashMap<String, String> getAdditionalFormFields() {
		return additionalFormFields;
	}
	public void setAdditionalFormFields(HashMap<String, String> additionalFormFields) {
		this.additionalFormFields = additionalFormFields;
	}
	
	
}
