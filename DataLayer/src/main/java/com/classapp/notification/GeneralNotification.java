package com.classapp.notification;

import java.util.Date;

public class GeneralNotification {
	private String notificationTitle;
	private String notificationMessage;
	private Date notificationDate;
	private boolean dissmissable;
	private String notificationKey;
	private int notificationCount;
	
	public static enum NOTIFICATION_KEYS{
		REEVALUATION
		};
	private NOTIFICATION_KEYS keys;
		
	public String getNotificationTitle() {
		return notificationTitle;
	}
	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}
	public String getNotificationMessage() {
		return notificationMessage;
	}
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	public Date getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	public boolean isDissmissable() {
		return dissmissable;
	}
	public void setDissmissable(boolean dissmissable) {
		this.dissmissable = dissmissable;
	}
	public String getNotificationKey() {
		return notificationKey;
	}
	public void setNotificationKey(String notificationKey) {
		this.notificationKey = notificationKey;
	}
	public NOTIFICATION_KEYS getKeys() {
		return keys;
	}
	public void setKeys(NOTIFICATION_KEYS keys) {
		this.keys = keys;
	}
	public int getNotificationCount() {
		return notificationCount;
	}
	public void setNotificationCount(int notificationCount) {
		this.notificationCount = notificationCount;
	}
	
	
	
}
