package com.service.beans;

import java.util.Date;

import com.classowner.SendMessageAction;
import com.serviceinterface.IAcademicAlert;

public class SendAcademicAlertAttendanceBean extends IAcademicAlert{
	String type;
	Date date;
	int minAttendace;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getMinAttendace() {
		return minAttendace;
	}
	public void setMinAttendace(int minAttendace) {
		this.minAttendace = minAttendace;
	}
	
	
}
