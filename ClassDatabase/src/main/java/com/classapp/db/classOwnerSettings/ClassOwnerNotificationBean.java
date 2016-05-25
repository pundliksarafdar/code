package com.classapp.db.classOwnerSettings;

public class ClassOwnerNotificationBean {
	boolean smsPayment;
	boolean emailPayment;
	boolean smsPaymentDue;
	boolean emailPaymentDue;
	boolean smsAttendanceDaily;
	boolean emailAttendanceDaily;
	boolean smsAttendanceWeekly;
	boolean emailAttendanceWeekly;
	boolean smsAttendanceMonthly;
	boolean emailAttendanceMonthly;
	boolean smsTimetableNewEntry;
	boolean emailTimetableNewEntry;
	boolean smsTimetableEditEntry;
	boolean emailTimetableEditEntry;
	boolean smsProgressCardManual;
	boolean emailProgressCardManual;
	boolean smsProgressCardAfterEveryExam;
	boolean emailProgressCardAfterEveryExam;

	int emailAttendanceWeeklyThreshold;
	int emailAttendanceMonthlyThreshold;
	int regId;
	
	//This variable is used to identify on which day we need to send the notification
	String weeklyRecurrence;
	
	public String getWeeklyRecurrence() {
		return weeklyRecurrence;
	}
	public void setWeeklyRecurrence(String weeklyRecurrence) {
		this.weeklyRecurrence = weeklyRecurrence;
	}
	public int getRegId() {
		return regId;
	}
	public void setRegId(int regId) {
		this.regId = regId;
	}
	
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
	
}
