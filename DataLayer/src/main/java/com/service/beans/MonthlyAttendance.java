package com.service.beans;

import java.sql.Date;
import java.util.List;

public class MonthlyAttendance {
	String student_name;
	int student_id;
	String[] daily_presentee_percentange;
	String[] daily_presentee;
	Date date;
	int total_monthly_presentee;
	int total_monthly_lectures;
	double total_prsentee_percentage;
	List<MonthlyCount> monthlyCountList;
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	
	public String[] getDaily_presentee_percentange() {
		return daily_presentee_percentange;
	}
	public void setDaily_presentee_percentange(String[] daily_presentee_percentange) {
		this.daily_presentee_percentange = daily_presentee_percentange;
	}
	public String[] getDaily_presentee() {
		return daily_presentee;
	}
	public void setDaily_presentee(String[] daily_presentee) {
		this.daily_presentee = daily_presentee;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getTotal_monthly_presentee() {
		return total_monthly_presentee;
	}
	public void setTotal_monthly_presentee(int total_monthly_presentee) {
		this.total_monthly_presentee = total_monthly_presentee;
	}
	public List<MonthlyCount> getMonthlyCountList() {
		return monthlyCountList;
	}
	public void setMonthlyCountList(List<MonthlyCount> monthlyCountList) {
		this.monthlyCountList = monthlyCountList;
	}
	public int getTotal_monthly_lectures() {
		return total_monthly_lectures;
	}
	public void setTotal_monthly_lectures(int total_monthly_lectures) {
		this.total_monthly_lectures = total_monthly_lectures;
	}
	public double getTotal_prsentee_percentage() {
		return total_prsentee_percentage;
	}
	public void setTotal_prsentee_percentage(double total_prsentee_percentage) {
		this.total_prsentee_percentage = total_prsentee_percentage;
	}
	
}
