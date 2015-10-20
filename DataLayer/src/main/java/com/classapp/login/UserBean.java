package com.classapp.login;

import java.util.HashMap;
import java.util.List;

public class UserBean{
	private String username;
	private String fullname;
	private String firstname;
	private String lastname;
	private String middlename;
	private Integer role;
	private String dob;
	private LoginBeanMobile loginBean;
	private String addr1;
	private String addr2;
	private String city;
	private String state;
	private String country;
	private String phone1;
	private String phone2;
	private String startdate;
	private String enddate;
	private Integer regId;
	private HashMap<String, List> studentData;
	private String className;
	private String inst_status;
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		if(role == null){
			this.role = 0;
		}else{
			this.role = role;
		}

	}
	public LoginBeanMobile getLoginBean() {
		return loginBean;
	}
	public void setLoginBean(LoginBeanMobile loginBean) {
		this.loginBean = loginBean;
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
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	public HashMap<String, List> getStudentData() {
		return studentData;
	}
	public void setStudentData(HashMap<String, List> studentData) {
		this.studentData = studentData;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getInst_status() {
		return inst_status;
	}
	public void setInst_status(String inst_status) {
		this.inst_status = inst_status;
	}
	
}
