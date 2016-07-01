package com.classapp.db.classOwnerSettings;

import java.sql.Date;

public class ClassOwnerNotificationBean {
	boolean smsPayment;
	boolean emailPayment;
	boolean parentPayment;
	boolean studentPayment;
	
	boolean smsPaymentDue;
	boolean emailPaymentDue;
	boolean parentPaymentDue;
	boolean studentPaymentDue;
	Date paymentDueDate;
	
	boolean smsAttendanceDaily;
	boolean emailAttendanceDaily;
	boolean parentAttendanceDaily;
	boolean studentAttendanceDaily;
	
	boolean smsAttendanceWeekly;
	boolean emailAttendanceWeekly;
	boolean parentAttendanceWeekly;
	boolean studentAttendanceWeekly;
	
	boolean smsAttendanceMonthly;
	boolean emailAttendanceMonthly;
	boolean parentAttendanceMonthly;
	boolean studentAttendanceMonthly;
	
	boolean smsTimetableNewEntry;
	boolean emailTimetableNewEntry;
	boolean parentTimetableNewEntry;
	boolean studentTimetableNewEntry;
	
	boolean smsTimetableEditEntry;
	boolean emailTimetableEditEntry;
	boolean parentTimetableEditEntry;
	boolean studentTimetableEditEntry;

	boolean smsProgressCardManual;
	boolean emailProgressCardManual;
	boolean parentProgressCardManual;
	boolean studentProgressCardManual;
	
	boolean smsProgressCardAfterEveryExam;
	boolean emailProgressCardAfterEveryExam;
	boolean parentProgressCardAfterEveryExam;
	boolean studentProgressCardAfterEveryExam;
	
	int emailAttendanceWeeklyThreshold;
	int emailAttendanceMonthlyThreshold;
	
	int regId;
	String weeklyRecurrence;
	
	public boolean isSmsPayment() {
		return smsPayment;
	}
	public void setSmsPayment(boolean smsPayment) {
		this.smsPayment = smsPayment;
	}
	public boolean isEmailPayment() {
		return emailPayment;
	}
	public void setEmailPayment(boolean emailPayment) {
		this.emailPayment = emailPayment;
	}
	public boolean isParentPayment() {
		return parentPayment;
	}
	public void setParentPayment(boolean parentPayment) {
		this.parentPayment = parentPayment;
	}
	public boolean isStudentPayment() {
		return studentPayment;
	}
	public void setStudentPayment(boolean studentPayment) {
		this.studentPayment = studentPayment;
	}
	public boolean isSmsPaymentDue() {
		return smsPaymentDue;
	}
	public void setSmsPaymentDue(boolean smsPaymentDue) {
		this.smsPaymentDue = smsPaymentDue;
	}
	public boolean isEmailPaymentDue() {
		return emailPaymentDue;
	}
	public void setEmailPaymentDue(boolean emailPaymentDue) {
		this.emailPaymentDue = emailPaymentDue;
	}
	public boolean isParentPaymentDue() {
		return parentPaymentDue;
	}
	public void setParentPaymentDue(boolean parentPaymentDue) {
		this.parentPaymentDue = parentPaymentDue;
	}
	public boolean isStudentPaymentDue() {
		return studentPaymentDue;
	}
	public void setStudentPaymentDue(boolean studentPaymentDue) {
		this.studentPaymentDue = studentPaymentDue;
	}
	public boolean isSmsAttendanceDaily() {
		return smsAttendanceDaily;
	}
	public void setSmsAttendanceDaily(boolean smsAttendanceDaily) {
		this.smsAttendanceDaily = smsAttendanceDaily;
	}
	public boolean isEmailAttendanceDaily() {
		return emailAttendanceDaily;
	}
	public void setEmailAttendanceDaily(boolean emailAttendanceDaily) {
		this.emailAttendanceDaily = emailAttendanceDaily;
	}
	public boolean isParentAttendanceDaily() {
		return parentAttendanceDaily;
	}
	public void setParentAttendanceDaily(boolean parentAttendanceDaily) {
		this.parentAttendanceDaily = parentAttendanceDaily;
	}
	public boolean isStudentAttendanceDaily() {
		return studentAttendanceDaily;
	}
	public void setStudentAttendanceDaily(boolean studentAttendanceDaily) {
		this.studentAttendanceDaily = studentAttendanceDaily;
	}
	public boolean isSmsAttendanceWeekly() {
		return smsAttendanceWeekly;
	}
	public void setSmsAttendanceWeekly(boolean smsAttendanceWeekly) {
		this.smsAttendanceWeekly = smsAttendanceWeekly;
	}
	public boolean isEmailAttendanceWeekly() {
		return emailAttendanceWeekly;
	}
	public void setEmailAttendanceWeekly(boolean emailAttendanceWeekly) {
		this.emailAttendanceWeekly = emailAttendanceWeekly;
	}
	public boolean isParentAttendanceWeekly() {
		return parentAttendanceWeekly;
	}
	public void setParentAttendanceWeekly(boolean parentAttendanceWeekly) {
		this.parentAttendanceWeekly = parentAttendanceWeekly;
	}
	public boolean isStudentAttendanceWeekly() {
		return studentAttendanceWeekly;
	}
	public void setStudentAttendanceWeekly(boolean studentAttendanceWeekly) {
		this.studentAttendanceWeekly = studentAttendanceWeekly;
	}
	public boolean isSmsAttendanceMonthly() {
		return smsAttendanceMonthly;
	}
	public void setSmsAttendanceMonthly(boolean smsAttendanceMonthly) {
		this.smsAttendanceMonthly = smsAttendanceMonthly;
	}
	public boolean isEmailAttendanceMonthly() {
		return emailAttendanceMonthly;
	}
	public void setEmailAttendanceMonthly(boolean emailAttendanceMonthly) {
		this.emailAttendanceMonthly = emailAttendanceMonthly;
	}
	public boolean isParentAttendanceMonthly() {
		return parentAttendanceMonthly;
	}
	public void setParentAttendanceMonthly(boolean parentAttendanceMonthly) {
		this.parentAttendanceMonthly = parentAttendanceMonthly;
	}
	public boolean isStudentAttendanceMonthly() {
		return studentAttendanceMonthly;
	}
	public void setStudentAttendanceMonthly(boolean studentAttendanceMonthly) {
		this.studentAttendanceMonthly = studentAttendanceMonthly;
	}
	public boolean isSmsTimetableNewEntry() {
		return smsTimetableNewEntry;
	}
	public void setSmsTimetableNewEntry(boolean smsTimetableNewEntry) {
		this.smsTimetableNewEntry = smsTimetableNewEntry;
	}
	public boolean isEmailTimetableNewEntry() {
		return emailTimetableNewEntry;
	}
	public void setEmailTimetableNewEntry(boolean emailTimetableNewEntry) {
		this.emailTimetableNewEntry = emailTimetableNewEntry;
	}
	public boolean isParentTimetableNewEntry() {
		return parentTimetableNewEntry;
	}
	public void setParentTimetableNewEntry(boolean parentTimetableNewEntry) {
		this.parentTimetableNewEntry = parentTimetableNewEntry;
	}
	public boolean isStudentTimetableNewEntry() {
		return studentTimetableNewEntry;
	}
	public void setStudentTimetableNewEntry(boolean studentTimetableNewEntry) {
		this.studentTimetableNewEntry = studentTimetableNewEntry;
	}
	public boolean isSmsTimetableEditEntry() {
		return smsTimetableEditEntry;
	}
	public void setSmsTimetableEditEntry(boolean smsTimetableEditEntry) {
		this.smsTimetableEditEntry = smsTimetableEditEntry;
	}
	public boolean isEmailTimetableEditEntry() {
		return emailTimetableEditEntry;
	}
	public void setEmailTimetableEditEntry(boolean emailTimetableEditEntry) {
		this.emailTimetableEditEntry = emailTimetableEditEntry;
	}
	public boolean isParentTimetableEditEntry() {
		return parentTimetableEditEntry;
	}
	public void setParentTimetableEditEntry(boolean parentTimetableEditEntry) {
		this.parentTimetableEditEntry = parentTimetableEditEntry;
	}
	public boolean isStudentTimetableEditEntry() {
		return studentTimetableEditEntry;
	}
	public void setStudentTimetableEditEntry(boolean studentTimetableEditEntry) {
		this.studentTimetableEditEntry = studentTimetableEditEntry;
	}
	public boolean isSmsProgressCardManual() {
		return smsProgressCardManual;
	}
	public void setSmsProgressCardManual(boolean smsProgressCardManual) {
		this.smsProgressCardManual = smsProgressCardManual;
	}
	public boolean isEmailProgressCardManual() {
		return emailProgressCardManual;
	}
	public void setEmailProgressCardManual(boolean emailProgressCardManual) {
		this.emailProgressCardManual = emailProgressCardManual;
	}
	public boolean isParentProgressCardManual() {
		return parentProgressCardManual;
	}
	public void setParentProgressCardManual(boolean parentProgressCardManual) {
		this.parentProgressCardManual = parentProgressCardManual;
	}
	public boolean isStudentProgressCardManual() {
		return studentProgressCardManual;
	}
	public void setStudentProgressCardManual(boolean studentProgressCardManual) {
		this.studentProgressCardManual = studentProgressCardManual;
	}
	public boolean isSmsProgressCardAfterEveryExam() {
		return smsProgressCardAfterEveryExam;
	}
	public void setSmsProgressCardAfterEveryExam(boolean smsProgressCardAfterEveryExam) {
		this.smsProgressCardAfterEveryExam = smsProgressCardAfterEveryExam;
	}
	public boolean isEmailProgressCardAfterEveryExam() {
		return emailProgressCardAfterEveryExam;
	}
	public void setEmailProgressCardAfterEveryExam(boolean emailProgressCardAfterEveryExam) {
		this.emailProgressCardAfterEveryExam = emailProgressCardAfterEveryExam;
	}
	public boolean isParentProgressCardAfterEveryExam() {
		return parentProgressCardAfterEveryExam;
	}
	public void setParentProgressCardAfterEveryExam(boolean parentProgressCardAfterEveryExam) {
		this.parentProgressCardAfterEveryExam = parentProgressCardAfterEveryExam;
	}
	public boolean isStudentProgressCardAfterEveryExam() {
		return studentProgressCardAfterEveryExam;
	}
	public void setStudentProgressCardAfterEveryExam(boolean studentProgressCardAfterEveryExam) {
		this.studentProgressCardAfterEveryExam = studentProgressCardAfterEveryExam;
	}
	public int getEmailAttendanceWeeklyThreshold() {
		return emailAttendanceWeeklyThreshold;
	}
	public void setEmailAttendanceWeeklyThreshold(int emailAttendanceWeeklyThreshold) {
		this.emailAttendanceWeeklyThreshold = emailAttendanceWeeklyThreshold;
	}
	public int getEmailAttendanceMonthlyThreshold() {
		return emailAttendanceMonthlyThreshold;
	}
	public void setEmailAttendanceMonthlyThreshold(int emailAttendanceMonthlyThreshold) {
		this.emailAttendanceMonthlyThreshold = emailAttendanceMonthlyThreshold;
	}
	public int getRegId() {
		return regId;
	}
	public void setRegId(int regId) {
		this.regId = regId;
	}
	public String getWeeklyRecurrence() {
		return weeklyRecurrence;
	}
	public void setWeeklyRecurrence(String weeklyRecurrence) {
		this.weeklyRecurrence = weeklyRecurrence;
	}
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	
		
	
}
