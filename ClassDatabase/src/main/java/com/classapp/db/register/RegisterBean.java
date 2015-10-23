package com.classapp.db.register;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer regId = 0;
	private String fname;
	private String mname;
	private String lname;
	private String dob;
	private String className;
	private String loginName;
	private String loginPass;
	private String loginPassRe;
	private String addr1;
	private String addr2;
	private String city;
	private String state;
	private String country;
	private String phone1;
	private String phone2;
	private String registrationDate; 
	private String startDate;
	private String endDate;
	private String renewedDates;
	private String daysLeft;
	private Integer role = -1;
	private Integer div_id;
	private String email;
	private String activationcode;
	private String status;
	private String inst_status;
	private Date lastlogin;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActivationcode() {
		return activationcode;
	}
	public void setActivationcode(String activationcode) {
		this.activationcode = activationcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getDiv_id() {
		return div_id;
	}
	public void setDiv_id(Integer div_id) {
		this.div_id = div_id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPass() {
		return loginPass;
	}
	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}
	public String getLoginPassRe() {
		return loginPassRe;
	}
	public void setLoginPassRe(String loginPassRe) {
		this.loginPassRe = loginPassRe;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRenewedDates() {
		return renewedDates;
	}
	public void setRenewedDates(String renewedDates) {
		this.renewedDates = renewedDates;
	}
	
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getDaysLeft() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date dateStart = dateFormat.parse(this.startDate);
			Date dateEnd = dateFormat.parse(this.endDate);
			Long daysLeft1 = dateEnd.getTime()-dateStart.getTime();
			//Date monthLeft = new Date(daysLeft1);
			this.daysLeft = (daysLeft1 /(24 * 60 * 60 * 1000))+"";//dateFormat.format(monthLeft).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.daysLeft;
	}
	public void setDaysLeft(String daysLeft) {
		this.daysLeft = daysLeft;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getInst_status() {
		return inst_status;
	}
	public void setInst_status(String inst_status) {
		this.inst_status = inst_status;
	}
	
	
}
