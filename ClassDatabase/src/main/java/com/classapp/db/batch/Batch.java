package com.classapp.db.batch;

public class Batch {
	private int batch_id;
	private int class_id;
	private int div_id;
	private String batch_name;
	private String sub_id;
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public String getBatch_name() {
		return batch_name;
	}
	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	public String getSub_id() {
		return sub_id;
	}
	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}
	
	@Override
	public boolean equals(Object obj) {
		Batch batch=(Batch)obj;
		if(batch.getBatch_id()==this.batch_id && batch.getBatch_name().equals(this.batch_name) && batch.getClass_id()==this.class_id && batch.getDiv_id()==this.div_id && batch.getSub_id().equals(this.sub_id)){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
