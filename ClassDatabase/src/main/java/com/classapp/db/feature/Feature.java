package com.classapp.db.feature;

public class Feature {
	private Integer regId;
	private boolean smsAccess;
	private Integer smsAlloted;
	private Integer smsLeft;
	private boolean emailAccess;
	
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	public boolean isSmsAccess() {
		return smsAccess;
	}
	public void setSmsAccess(boolean smsAccess) {
		this.smsAccess = smsAccess;
	}
	public Integer getSmsAlloted() {
		return smsAlloted;
	}
	public void setSmsAlloted(Integer smsAlloted) {
		this.smsAlloted = smsAlloted;
	}
	public Integer getSmsLeft() {
		return smsLeft;
	}
	public void setSmsLeft(Integer smsLeft) {
		this.smsLeft = smsLeft;
	}
	public boolean isEmailAccess() {
		return emailAccess;
	}
	public void setEmailAccess(boolean emailAccess) {
		this.emailAccess = emailAccess;
	}
	
}
