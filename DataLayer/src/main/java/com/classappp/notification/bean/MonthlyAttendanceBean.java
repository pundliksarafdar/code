package com.classappp.notification.bean;

import java.util.List;

public class MonthlyAttendanceBean {
	String name;
	String montj = "Dec 2015";
	List<ThisMonth> thisMonths;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMontj() {
		return montj;
	}

	public void setMontj(String montj) {
		this.montj = montj;
	}


	public List<ThisMonth> getThisMonths() {
		return thisMonths;
	}



	public void setThisMonths(List<ThisMonth> thisMonths) {
		this.thisMonths = thisMonths;
	}



	public static class ThisMonth{
		String date;
		String absentOrPresent;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getAbsentOrPresent() {
			return absentOrPresent;
		}
		public void setAbsentOrPresent(String absentOrPresent) {
			this.absentOrPresent = absentOrPresent;
		}
		
		
	}
}
