package com.notification.bean;

public class MessageDetailBean {
	Integer studentId;
	String studentEmail;
	long studentPhone;
	String studentPhoneId;
	String studentPhoneType;
	
	String parentEmail;
	long parentPhone;
	String parentPhoneId;
	String parentPhoneType;
	
	boolean messageTypeSms;
	boolean messageTypeEmail;
	boolean messageTypePush;
	
	boolean sendToStudent;
	boolean sendToParent;

	
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public long getStudentPhone() {
		return studentPhone;
	}

	public void setStudentPhone(long studentPhone) {
		this.studentPhone = studentPhone;
	}

	public String getStudentPhoneType() {
		return studentPhoneType;
	}
	
	public void setStudentPhoneType(String studentPhoneType) {
		this.studentPhoneType = studentPhoneType;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}

	public long getParentPhone() {
		return parentPhone;
	}

	public void setParentPhone(long parentPhone) {
		this.parentPhone = parentPhone;
	}

	public String getParentPhoneType() {
		return parentPhoneType;
	}

	public void setParentPhoneType(String parentPhoneType) {
		this.parentPhoneType = parentPhoneType;
	}

	public boolean isMessageTypeSms() {
		return messageTypeSms;
	}

	public void setMessageTypeSms(boolean messageTypeSms) {
		this.messageTypeSms = messageTypeSms;
	}

	public boolean isMessageTypeEmail() {
		return messageTypeEmail;
	}

	public void setMessageTypeEmail(boolean messageTypeEmail) {
		this.messageTypeEmail = messageTypeEmail;
	}

	public boolean isMessageTypePush() {
		return messageTypePush;
	}

	public void setMessageTypePush(boolean messageTypePush) {
		this.messageTypePush = messageTypePush;
	}

	public boolean isSendToStudent() {
		return sendToStudent;
	}

	public void setSendToStudent(boolean sendToStudent) {
		this.sendToStudent = sendToStudent;
	}

	public boolean isSendToParent() {
		return sendToParent;
	}

	public void setSendToParent(boolean sendToParent) {
		this.sendToParent = sendToParent;
	}

	public String getStudentPhoneId() {
		return studentPhoneId;
	}

	public void setStudentPhoneId(String studentPhoneId) {
		this.studentPhoneId = studentPhoneId;
	}

	public String getParentPhoneId() {
		return parentPhoneId;
	}

	public void setParentPhoneId(String parentPhoneId) {
		this.parentPhoneId = parentPhoneId;
	}	
}
