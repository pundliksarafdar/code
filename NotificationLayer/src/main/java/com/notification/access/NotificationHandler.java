package com.notification.access;

import java.util.List;

import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
import com.transaction.batch.BatchTransactions;
import com.transaction.student.StudentTransaction;

public class NotificationHandler {
	public boolean sendSmsToBatchStudent(Integer divId,Integer batchId,Integer instId,String message){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentDetails> studentDetails = studentTransaction.getAllStudentsDetails(batchId+"", divId+"", instId);
		for(StudentDetails studentDetail:studentDetails){
			System.out.println("Sms "+studentDetail.getStudentUserBean().getPhone1());
		}
		return false;
	}
	
	public boolean sendSmsToBatchParent(Integer divId,Integer batchId,Integer instId,String message){
		StudentTransaction studentTransaction = new StudentTransaction();
		/*
		List<Student> students = studentTransaction.getAllStudentsDetails(batchId+"", divId+"", instId);
		for(Student student:students){
			System.out.println("Email "+student.getParentEmail());
		}*/
		return false;
	}
	
	public boolean sendSmsToTeacher(String[] teacher,String message){
		
		return false;
	}
	
	
}
