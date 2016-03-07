package com.service.beans;

import java.util.List;

public class StudentFeesServiceBean {
	List<Student_Fees> student_FeesList;
	List<Student_Fees_Transaction> student_Fees_TransactionList;
	public List<Student_Fees> getStudent_FeesList() {
		return student_FeesList;
	}
	public void setStudent_FeesList(List<Student_Fees> student_FeesList) {
		this.student_FeesList = student_FeesList;
	}
	public List<Student_Fees_Transaction> getStudent_Fees_TransactionList() {
		return student_Fees_TransactionList;
	}
	public void setStudent_Fees_TransactionList(
			List<Student_Fees_Transaction> student_Fees_TransactionList) {
		this.student_Fees_TransactionList = student_Fees_TransactionList;
	}
	
	
}
