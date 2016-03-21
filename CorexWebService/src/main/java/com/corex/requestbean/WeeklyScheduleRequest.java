package com.corex.requestbean;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WeeklyScheduleRequest {
	long date;
	int inst_id;
	int regID;
	int batch_ID;
	public WeeklyScheduleRequest(){
		
	}
public WeeklyScheduleRequest(long date,int inst_id,int regID,int batchID){
		this.date=date;
		this.inst_id=inst_id;
		this.regID=regID;
		this.batch_ID=batchID;
	}

public WeeklyScheduleRequest(String date,String inst_id,String regID,String batchID){
	this.date=Long.parseLong(date);
	this.inst_id=Integer.parseInt(inst_id);
	this.regID=Integer.parseInt(regID);
	this.batch_ID=Integer.parseInt(batchID);
}

public WeeklyScheduleRequest(String date){
	this.date=Long.parseLong(date);
}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getRegID() {
		return regID;
	}
	public void setRegID(int regID) {
		this.regID = regID;
	}
	public int getBatch_ID() {
		return batch_ID;
	}
	public void setBatch_ID(int batch_ID) {
		this.batch_ID = batch_ID;
	}
	
	
	}
