package com.notification.bean;

public class MessageDetailBean {
	String from;
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
	
	//if message is null parse template and object 
	String smsMessage;
	String smsTemplate;
	Object smsObject;
	
	//if message is null parse template and object 
	String emailMessage;
	String emailTemplate;
	Object emailObject;
		
	//if message is null parse template and object 
	String pushMessage;
	String pushTemplate;
	Object pushObject;
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
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
	public String getStudentPhoneId() {
		return studentPhoneId;
	}
	public void setStudentPhoneId(String studentPhoneId) {
		this.studentPhoneId = studentPhoneId;
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
	public String getParentPhoneId() {
		return parentPhoneId;
	}
	public void setParentPhoneId(String parentPhoneId) {
		this.parentPhoneId = parentPhoneId;
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
	public String getSmsMessage() {
		return smsMessage;
	}
	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}
	public String getSmsTemplate() {
		return smsTemplate;
	}
	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}
	public Object getSmsObject() {
		return smsObject;
	}
	public void setSmsObject(Object smsObject) {
		this.smsObject = smsObject;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	public String getEmailTemplate() {
		return emailTemplate;
	}
	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
	public Object getEmailObject() {
		return emailObject;
	}
	public void setEmailObject(Object emailObject) {
		this.emailObject = emailObject;
	}
	public String getPushMessage() {
		return pushMessage;
	}
	public void setPushMessage(String pushMessage) {
		this.pushMessage = pushMessage;
	}
	public String getPushTemplate() {
		return pushTemplate;
	}
	public void setPushTemplate(String pushTemplate) {
		this.pushTemplate = pushTemplate;
	}
	public Object getPushObject() {
		return pushObject;
	}
	public void setPushObject(Object pushObject) {
		this.pushObject = pushObject;
	}
	
	
}
