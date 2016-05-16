package com.service.beans;

import java.io.Serializable;

public class SendNotificationMesssageBean implements Serializable{
	String message;
	//Teacher related variable
	String messageType[];
	String teacher[];
	
	//Student and parent variables
	String sendTo[];
	String divisionSelect;
	String batchSelect;
	String messageTypeTOST[];
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String[] getMessageType() {
		return messageType;
	}
	public void setMessageType(String[] messageType) {
		this.messageType = messageType;
	}
	public String[] getTeacher() {
		return teacher;
	}
	public void setTeacher(String[] teacher) {
		this.teacher = teacher;
	}
	public String[] getSendTo() {
		return sendTo;
	}
	public void setSendTo(String[] sendTo) {
		this.sendTo = sendTo;
	}
	public String getDivisionSelect() {
		return divisionSelect;
	}
	public void setDivisionSelect(String divisionSelect) {
		this.divisionSelect = divisionSelect;
	}
	public String getBatchSelect() {
		return batchSelect;
	}
	public void setBatchSelect(String batchSelect) {
		this.batchSelect = batchSelect;
	}
	public String[] getMessageTypeTOST() {
		return messageTypeTOST;
	}
	public void setMessageTypeTOST(String[] messageTypeTOST) {
		this.messageTypeTOST = messageTypeTOST;
	}
	
	
	
}
