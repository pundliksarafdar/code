package com.notification.bean;

public class MessageDetailBean {
	String from;
	String emailSubject;
	Integer studentId;
	String studentEmail;
	Long studentPhone;
	String studentPhoneId;
	String studentPhoneType;
	
	String parentEmail;
	Long parentPhone;
	String parentPhoneId;
	String parentPhoneType;
	
	String teacherEmail;
	Long teacherPhone;
	String teacherPhoneId;
	String teacherPhoneType;
	
	boolean messageTypeSms;
	boolean messageTypeEmail;
	boolean messageTypePush;
	
	boolean sendToStudent;
	boolean sendToParent;
	boolean sendToTeacher;
	
	//if message is null parse template and object 
	String smsMessage;
	String smsTemplate;
	Object smsObject;
	
	//if message is null parse template and object 
	String emailMessage;
	String emailTemplate;
	Object emailObject;
	
	//if message is null parse template and object 
	String parentEmailMessage;
	String parentEmailTemplate;
	Object parentEmailObject;
	
	String smsParentMessage;
	String smsParentTemplate;
	Object smsParentObject;
	//if message is null parse template and object 
	String pushMessage;
	String pushTemplate;
	Object pushObject;
	String smsType;
	
	String teacherEmailMessage;
	String teacherEmailTemplate;
	Object teacherEmailObject;
	
	String smsTeacherMessage;
	String smsTeacherTemplate;
	Object smsTeacherObject;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
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
	public Long getStudentPhone() {
		return studentPhone;
	}
	public void setStudentPhone(Long studentPhone) {
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
	public Long getParentPhone() {
		return parentPhone;
	}
	public void setParentPhone(Long parentPhone) {
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
	public String getParentEmailMessage() {
		return parentEmailMessage;
	}
	public void setParentEmailMessage(String parentEmailMessage) {
		this.parentEmailMessage = parentEmailMessage;
	}
	public String getParentEmailTemplate() {
		return parentEmailTemplate;
	}
	public void setParentEmailTemplate(String parentEmailTemplate) {
		this.parentEmailTemplate = parentEmailTemplate;
	}
	public Object getParentEmailObject() {
		return parentEmailObject;
	}
	public void setParentEmailObject(Object parentEmailObject) {
		this.parentEmailObject = parentEmailObject;
	}
	public String getSmsParentMessage() {
		return smsParentMessage;
	}
	public void setSmsParentMessage(String smsParentMessage) {
		this.smsParentMessage = smsParentMessage;
	}
	public String getSmsParentTemplate() {
		return smsParentTemplate;
	}
	public void setSmsParentTemplate(String smsParentTemplate) {
		this.smsParentTemplate = smsParentTemplate;
	}
	public Object getSmsParentObject() {
		return smsParentObject;
	}
	public void setSmsParentObject(Object smsParentObject) {
		this.smsParentObject = smsParentObject;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public boolean isSendToTeacher() {
		return sendToTeacher;
	}
	public void setSendToTeacher(boolean sendToTeacher) {
		this.sendToTeacher = sendToTeacher;
	}
	public String getTeacherEmailMessage() {
		return teacherEmailMessage;
	}
	public void setTeacherEmailMessage(String teacherEmailMessage) {
		this.teacherEmailMessage = teacherEmailMessage;
	}
	public String getTeacherEmailTemplate() {
		return teacherEmailTemplate;
	}
	public void setTeacherEmailTemplate(String teacherEmailTemplate) {
		this.teacherEmailTemplate = teacherEmailTemplate;
	}
	public Object getTeacherEmailObject() {
		return teacherEmailObject;
	}
	public void setTeacherEmailObject(Object teacherEmailObject) {
		this.teacherEmailObject = teacherEmailObject;
	}
	public String getSmsTeacherMessage() {
		return smsTeacherMessage;
	}
	public void setSmsTeacherMessage(String smsTeacherMessage) {
		this.smsTeacherMessage = smsTeacherMessage;
	}
	public String getSmsTeacherTemplate() {
		return smsTeacherTemplate;
	}
	public void setSmsTeacherTemplate(String smsTeacherTemplate) {
		this.smsTeacherTemplate = smsTeacherTemplate;
	}
	public Object getSmsTeacherObject() {
		return smsTeacherObject;
	}
	public void setSmsTeacherObject(Object smsTeacherObject) {
		this.smsTeacherObject = smsTeacherObject;
	}
	public String getTeacherEmail() {
		return teacherEmail;
	}
	public void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}
	public Long getTeacherPhone() {
		return teacherPhone;
	}
	public void setTeacherPhone(Long teacherPhone) {
		this.teacherPhone = teacherPhone;
	}
	public String getTeacherPhoneId() {
		return teacherPhoneId;
	}
	public void setTeacherPhoneId(String teacherPhoneId) {
		this.teacherPhoneId = teacherPhoneId;
	}
	public String getTeacherPhoneType() {
		return teacherPhoneType;
	}
	public void setTeacherPhoneType(String teacherPhoneType) {
		this.teacherPhoneType = teacherPhoneType;
	}
	
	
}
